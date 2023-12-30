(defproject url-shortener "0.1.0-SNAPSHOT"
  :description "A simple url shortener"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [org.clojure/java.jdbc "0.7.12"]
                 [mysql/mysql-connector-java "8.0.33"]
                 [com.github.seancorfield/honeysql "2.4.1066"]
                 [javax.servlet/servlet-api "2.5"]
                 [ring "1.11.0-alpha4"]
                 [metosin/reitit "0.7.0-alpha7"]
                 [metosin/muuntaja "0.6.8"]
                 [ring/ring-json "0.5.1"]]
  :main ^:skip-aot url-shortener.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
