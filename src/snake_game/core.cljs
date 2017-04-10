(ns snake-game.core
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [snake-game.views :as views]
            [snake-game.events :as events]
            [snake-game.subs :as subs]
            [goog.events :as gevents]
            [re-frisk.core :as re-frisk]))

(def key-codes-mapping {38 [0 -1]
                        40 [0 1]
                        39 [1 0]
                        37 [-1 0]})

(enable-console-print!)

(defonce game-activity
         (js/setInterval #(re-frame/dispatch [:next-state]) 200))

(defonce key-handler
         (gevents/listen js/window "keydown"
                        (fn [e]
                          (let [key-code (.-keyCode e)]
                            (when (contains? key-codes-mapping key-code)
                              (re-frame/dispatch [:change-direction (key-codes-mapping key-code)]))))))

(defn run []
  (re-frisk/enable-frisk!)
  (re-frame/dispatch-sync [:initialize-db])
  (reagent/render [views/board]
                  (js/document.getElementById "app")))

(run)