(ns snake-game.events
  (:require [re-frame.core :as r]
            [snake-game.utils :as utils]))

(def initial-state
  {:board-size  [30 30]
   :max-bonuses 10
   :score       1
   :max-score   1
   :game-state  :stop
   :player      {:body      [[1 5] [1 4] [1 3]]
                 :direction [1 0]}
   :bonuses     #{[5 5] [7 7] [6 6] [8 8] [13 3] [2 5]}})

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
      (assoc-in db [:bonuses] (conj bonuses (utils/pick-free-position board-size player bonuses)))
      db)))

(r/reg-event-db
  :new-game
  (fn [db]
    (-> db
        (assoc-in [:score] 0)
        (assoc-in [:player :body] (conj [] (utils/pick-free-position (:board-size db) [[]] [])))
        (assoc-in [:player :direction] (rand-nth (vals utils/key-codes-mapping)))
        (assoc-in [:game-state] :run))))

(r/reg-event-fx
  :next-state
  (fn [cofx]
    (let [db (:db cofx)]
      (if (= :run (:game-state db))
        (utils/calculate-next-state db)
        {:db db}))))