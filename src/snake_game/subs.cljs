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

(re-frame/reg-sub
  :score
  (fn [db _]
    (:score db)))

(re-frame/reg-sub
  :max-score
  (fn [db _]
    (:max-score db)))

(re-frame/reg-sub
  :game-state
  (fn [db _]
    (:game-state db)))