(ns snake-game.views
  (:require [re-frame.core :as re-frame]))

(defn game
  []
  [:div.container [:div.row []]])

(defn board
  []
  (let [player (re-frame/subscribe [:player])
        bonuses (re-frame/subscribe [:bonuses])
        [size-x size-y] (deref (re-frame/subscribe [:board-size]))]
    [:table.board [:tbody
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
