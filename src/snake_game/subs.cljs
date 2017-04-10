(ns snake-game.subs
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
  :player
  (fn [db _]
    (:player db)))

(re-frame/reg-sub
  :bonuses
  (fn [db _]
    (:bonuses db)))

(re-frame/reg-sub
  :board-size
  (fn [db _]
    (:board-size db)))
