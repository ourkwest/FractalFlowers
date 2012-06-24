(ns pw-play)

(use 'pw-image 'pw-tempfile)
(import '(java.awt Color))

(comment "Fractal Flowers")
  
(def html (str "<html><body><img src=\"" (filename ".png") "\"/></body></html>"))
  
(let [f (filename ".html")]
  (if (not (file-exists? f)) (spit f html)))

(def img (image 800 800))
(defn done [] (write-to-file img (filename ".png")))
  
(comment "geometry")

(defstruct place :x :y :r :s :c)

(defn drawplace
  [p]
  (spot img (p :x) (p :y) (* (p :s) 25) (p :c)))

(defn addplace 
  [p2 p1]
  (struct place 
          (+ (p1 :x) (* (. Math cos (p1 :r)) (p2 :x) (p1 :s)) (* (. Math sin (p1 :r)) (p2 :y) (p1 :s)))
          (+ (p1 :y) (* (. Math sin (p1 :r)) (p2 :x) (p1 :s)) (* (. Math cos (p1 :r)) (p2 :y) (p1 :s) -1))
          (+ (p1 :r) (p2 :r))
          (* (p1 :s) (p2 :s))
          (merge-colour (p1 :c) (p2 :c) 0.01)))

(defn draw-addplace
  [p2 p1]
  (do
    (drawplace p1)
    (addplace p2 p1)))
	
(def centre (struct place 410 700 -0.4 1.4 (new Color 255 255 0 50)))
  
(defn f-then
  [fn1 fn2]
  (fn [arg] (fn2 (fn1 arg))))
  
(defn f-both
  [fn1 fn2]
  (fn [arg] (do (fn1 arg) (fn2 arg))))
  
(defn f-dup
  [f n]
  (fn [arg] 
    (loop [arg arg n n]
      (if (< n 1) 
        arg
        (recur (f arg) (dec n))))))
		
(defn f-tree
  ([fn1 fn2 n]
    (loop [fna fn1 fnb fn2 n n]
      (if (< n 1) 
        fna
        (recur (f-then fn1 (f-both fna fnb)) (f-then fn2 (f-both fnb fna)) (dec n)))))
  ([fn1 fn2 leaf n]
    (loop [l leaf n n]
      (if (< n 1) 
        l
        (recur  (f-both (f-then fn1 l) (f-then fn2 l)) (dec n))))))
		
		
(comment "fractal tree")
(comment "                                     x y r s c")
(def fn-a (partial draw-addplace (struct place 0 1 0.005 1 (new Color 255 127 0 5))))
(def fn-b (partial draw-addplace (struct place 0 1 -0.005 1 (new Color 200 200 0 5))))
(def fn-sa (partial draw-addplace (struct place 8 0 -0.1 0.75 (new Color 255 255 255 5))))
(def fn-sb (partial draw-addplace (struct place -8 0 -0.1 0.75 (new Color 255 255 255 5))))

(def fn-l1 (partial draw-addplace (struct place 0 10 -0.1 0.99 (new Color 0 255 0 50))))
(def fn-leaf (f-dup fn-l1 250))

(def fn-c (f-then fn-sa (f-dup fn-a 150)))
(def fn-d (f-then fn-sb (f-dup fn-b 175)))
	  
(def fn-f (f-then fn-c (f-tree fn-c fn-d fn-leaf 9)))

(defn fun []
  (fn-f centre)
  (comment (fn-f (assoc centre :r (+ (Math/PI) (centre :r)))))
  (done))

(fun)
