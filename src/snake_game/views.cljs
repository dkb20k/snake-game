(ns snake-game.views
  (:require [re-frame.core :as re-frame]))

(defn board
  []
  (let [player (re-frame/subscribe [:player])
        bonuses (re-frame/subscribe [:bonuses])
        game-state (re-frame/subscribe [:game-state])
        [size-x size-y] (deref (re-frame/subscribe [:board-size]))]
    [:table {:class (str "board " (name @game-state))} [:tbody
                   (doall (for [y (range size-y)
                                :let [body (into #{} (:body @player))
                                      bonuses (into #{} @bonuses)]]
                            ^{:key (str y)}
                            [:tr
                             (for [x (range size-x)]
                               (let [current-position [x y]]
                                 (cond
                                   (bonuses current-position) ^{:key (str x " " y)} [:td.bonus]
                                   (body current-position) ^{:key (str x " " y)} [:td.player]
                                   :else ^{:key (str x " " y)} [:td.cell])))]
                            ))]
     ]))

(defn score-board
  []
  (let [score (re-frame/subscribe [:score])
        max-score (re-frame/subscribe [:max-score])]
    [:div
     [:h2 (str "Current score: " @score)]
     [:h2 (str "Max score: " @max-score)]
     [:button.btn.btn-default {:on-click #(re-frame/dispatch [:new-game])} "Restart game"]]))

(defn game
  []
  [:div.container [:div.row
                   [:div.col-md-6 [:div (board)]]
                   [:div.col-md-6 [:div (score-board)]]]])