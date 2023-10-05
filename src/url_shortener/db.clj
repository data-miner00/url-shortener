(ns url-shortener.db
  (:require [clojure.java.jdbc :as j]
            [honey.sql :as sql]
            [honey.sql.helpers :refer :all]))

(def mysql-db {:host "localhost"
               :port 3306
               :dbtype "mysql"
               :dbname "shorturl"
               :user "root"
               :password ""})

(def select-from-redirect
  (-> (select :*)
      (from :redirects)
      (sql/format)))

(defn query [q]
  (j/query mysql-db q))

(defn insert! [q]
  (j/db-do-prepared mysql-db q))

(defn insert-redirect! [slug url]
  (insert! (-> (insert-into :redirects)
               (columns :slug :url)
               (values [[slug, url]])
               (sql/format))))

(defn query-redirect [slug]
  (-> (query (-> (select :url)
             (from :redirects)
             (where [:= :slug slug])
             (sql/format)))
      first
      :url))

; Query to db
(query select-from-redirect)

; Workspace for testing code
(comment
  (query (-> (select :*)
             (from :redirects)
             (sql/format)))
  (insert! (-> (insert-into :redirects)
               (columns :slug :url)
               (values
                [["gh", "https://github.com"]
                 ["gg", "https://google.com"]])
               (sql/format {:pretty true})))
  (insert-redirect! "yt" "https://youtube.com")
  (query-redirect "yt")
  )

