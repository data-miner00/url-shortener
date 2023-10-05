(ns url-shortener.core
  (:require [ring.adapter.jetty :as ring-jetty]
            [reitit.ring :as ring]
            [ring.util.response :as r]
            [muuntaja.core :as m]
            [reitit.ring.middleware.muuntaja :as muuntaja]
            [url-shortener.db :as db]
            [url-shortener.slug :as slug])
  (:gen-class))

(defn redirect [req]
  (let [slug (get-in req [:path-params :slug])
        url (db/query-redirect slug)]
    (if url
      (r/redirect url 307)
      (r/not-found "Not found"))))

(defn create-redirect [req]
  (let [url (get-in req [:body-params :url])
        slug (slug/generate-slug)]
    (db/insert-redirect! slug url)
    (r/response (str "http://localhost:3001/" slug))))

(def app
  (ring/ring-handler 
   (ring/router 
    [ "/"
     [":slug" redirect]
     ["api/"
      ["redirect" {:post create-redirect}]]
     ["" {:handler (fn [req] {:body "welcome to url shortener" :status 200})}]] 
    {:data {:muuntaja m/instance 
            :middleware [muuntaja/format-middleware]}})))

(defn start []
  (ring-jetty/run-jetty #'app {:port 3001
                             :join? false}))

(defn -main [& args]
  (def server (start)))

(comment(.stop server))