(ns snake-game.events
  (:require [re-frame.core :as r]))

(def initial-state
  {:board-size  [30 30]
   :max-bonuses 10
   :score       0
   :max-score   0
   :player      {:body      [[1 5] [1 4] [1 3]]
                 :direction [1 0]}
   :bonuses     [[5 5] [7 7] [6 6] [8 8] [13 3] [2 5]]})

(defn pick-free-position
  [board-size player bonuses]
  (let [[max-x max-y] board-size
        board (for [x (range max-x)
                    y (range max-y)]
                [x y])
        board (remove #(contains? (:body player) %) board)
        board (remove #(contains? bonuses %) board)]
    (rand-nth board)))

(r/reg-event-db
  :initialize-db
  (fn [cofx]
    initial-state))

(r/reg-event-db
  :change-direction
  (fn [db [_ new-direction]]
    (assoc-in db [:player :direction] new-direction)))

(r/reg-event-db
  :generate-bonuses
  (fn [{:keys [max-bonuses bonuses player board-size] :as db}]
    (if (< (count bonuses) max-bonuses)
      (assoc-in db [:bonuses] (conj bonuses (pick-free-position board-size player bonuses)))
      db)))

(r/reg-event-fx
  :next-state
  (fn [cofx]
    (let [db (:db cofx)
          {:keys [player board-size bonuses score max-score]} db
          {:keys [body direction]} player
          new-position (->>
                         (map + (first body) direction)
                         (into [])
                         (conj []))
          tail (if ((into #{} bonuses) (first new-position))
                 body
                 (-> body
                     butlast))
          new-body (into new-position tail)
          score (if (contains? bonuses (first new-position))
                  (inc score)
                  score)
          max-score (if (> score max-score)
                      score
                      max-score)
          bonuses (filter #(not= % (first new-position)) bonuses)]
      {:db       (-> db
                     (assoc-in [:score] score)
                     (assoc-in [:max-score] max-score)
                     (assoc-in [:bonuses] bonuses)
                     (assoc-in [:player :body] new-body))
       :dispatch [:generate-bonuses]})))
