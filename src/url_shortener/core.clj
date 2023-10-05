(ns url-shortener.core
  (:require [ring.adapter.jetty :as ring-jetty]
            [reitit.ring :as ring]
            [muuntaja.core :as m]
            [reitit.ring.middleware.muuntaja :as muuntaja])
  (:gen-class))

(def app
  (ring/ring-handler 
   (ring/router 
    [ "/" 
     ["" {:handler (fn [req] {:body "hello" :status 200})}]] 
    {:data {:muuntaja m/instance 
            :middleware [muuntaja/format-middleware]}})))

(defn start []
  (ring-jetty/run-jetty #'app {:port 3001
                             :join? false}))

(defn -main [& args]
  (def server (start)))

(comment(.stop server))