(ns pw-tempfile)

(comment "temporary file management")

(import '(java.io File))

(defn filename
  [arg]
  (str (System/getProperty "user.dir") "/deleteme" arg))

(defn file-exists?
  [filename]
  (. (new File filename) exists))
