(ns url-shortener.db
  (:require [clojure.java.jdbc :as j]))

(def mysql-db {:host "localhost"
               :port 3306
               :dbtype "mysql"
               :dbname "shorturl"
               :user "root"
               :password ""})

(j/query mysql-db
  ["select * from redirects"])
