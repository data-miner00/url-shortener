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
      (r/not-found {:message "Not found"}))))

(defn create-redirect [req]
  (let [url (get-in req [:body-params :url])
        slug (slug/generate-slug)]
    (db/insert-redirect! slug url)
    (r/response {:link (str "http://localhost:3001/" slug)})))

(defn create-custom-redirect [req]
  (let [url (get-in req [:body-params :url])
        slug (get-in req [:body-params :slug])] 
    (db/insert-redirect! slug url)
    (r/response {:link (str "http://localhost:3001/" slug)})))

(def app
  (ring/ring-handler 
   (ring/router 
    [ "/"
     [":slug" redirect]
     ["api/"
      ["redirect" {:post create-redirect}]
      ["custom-redirect" {:post create-custom-redirect}]]
     ["" {:handler (fn [_req] {:body "welcome to url shortener" :status 200})}]] 
    {:data {:muuntaja m/instance 
            :middleware [muuntaja/format-middleware]}})))

(defn start []
  (ring-jetty/run-jetty #'app {:port 3001
                             :join? false}))
(def server (start))

(defn -main [& _args] server)

(comment(.stop server))