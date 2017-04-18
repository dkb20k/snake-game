(ns snake-game.utils)

(def key-codes-mapping {38 [0 -1]
                        40 [0 1]
                        39 [1 0]
                        37 [-1 0]})

(defn pick-free-position
  [board-size player bonuses]
  (let [[max-x max-y] board-size
        player (into #{} (:body player))
        bonuses (into #{} bonuses)
        board (for [x (range max-x)
                    y (range max-y)]
                [x y])
        board (remove #(player %) board)
        board (remove #(bonuses %) board)]
    (rand-nth board)))

(defn check-collision
  [current-position player board-size]
  (let [[x y] current-position
        [max-x max-y] board-size
        player (into #{} player)]
    (or
      (player current-position)
      (or (>= x max-x)
          (< x 0))
      (or (>= y max-y)
          (< y 0)))))

(defn calculate-next-state
  [db]
  (let [{:keys [player board-size bonuses score max-score]} db
        {:keys [body direction]} player
        bonuses (into #{} bonuses)
        new-position (map + (first body) direction)
        tail (if (bonuses new-position)
               body
               (butlast body))
        new-body (into (conj [] new-position) tail)
        score (if (bonuses new-position)
                (inc score)
                score)
        max-score (if (> score max-score)
                    score
                    max-score)
        bonuses (filter #(not= % new-position) bonuses)
        collision? (check-collision new-position body board-size)]
    (if collision?
      {:db (assoc-in db [:game-state] :stop)}
      {:db       (-> db
                     (assoc-in [:score] score)
                     (assoc-in [:max-score] max-score)
                     (assoc-in [:bonuses] bonuses)
                     (assoc-in [:player :body] new-body))
       :dispatch [:generate-bonuses]})))