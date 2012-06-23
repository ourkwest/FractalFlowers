(ns playing)

(comment "Fractal Flowers")

(comment "Image processing")

(import '(java.awt.image BufferedImage)
        '(java.io File)
		'(javax.imageio ImageIO))

(defn colour "Creates a colour as an array of 3 integers."
  [r g b]
  (int-array [r g b]))
  
  
(defn make-colour "test annonymous function overloading by arity"
  [r g b]
  (fn 
    ([] (+ (* r 255 255) (* g 255) b)) 
	([other] (println r g b other))))
  
		
(defn image "Creates an image to write to."
  [width height]
  (new BufferedImage width height (BufferedImage/TYPE_INT_RGB)))

(defn draw "Draws a colour to a pixel of an image"
  [image x y c]
  (if (and (> x -1) (> y -1) (< x (. image getWidth)) (< y (. image getHeight)))
    (. image setRGB (int x) (int y) c)))

(defn write-to-file "Writes an image to file."
  [image f]
  (. ImageIO write image "png" (new File f)))

(defn filename
  [arg]
  (str (System/getProperty "user.dir") "/deleteme" arg))
  
(def img (image 800 800))
(defn done [] (write-to-file img (filename ".png")))

(defn file-exists?
  [filename]
  (. (new File filename) exists))
  
(def html (str "<html><body><img src=\"" (filename ".png") "\"/></body></html>"))
  
(let [f (filename ".html")]
  (if (not (file-exists? f)) (spit f html)))

  
(comment "geometry")

(defstruct place :x :y :r :s :c)

(defn drawplace
  [p]
  (draw img (p :x) (p :y) (int (p :c))))
  
(defn writeplace
  [p]
  (println p))

(defn addplace 
  [p2 p1]
  (struct place 
    (+ (p1 :x) (* (. Math cos (p1 :r)) (p2 :x) (p1 :s)) (* (. Math sin (p1 :r)) (p2 :y) (p1 :s)))
	(+ (p1 :y) (* (. Math sin (p1 :r)) (p2 :x) (p1 :s)) (* (. Math cos (p1 :r)) (p2 :y) (p1 :s) -1))
	(+ (p1 :r) (p2 :r))
	(* (p1 :s) (p2 :s))
	(mod (* (p1 :c) (p2 :c)) (* 255 255 255))))

(defn drawblob
  [p pr]
  (dorun (map (fn [r] (drawplace (addplace pr (assoc p :r r)))) (range 0 6))))
	
(defn draw-addplace
  [p2 p1]
  (do
    (drawblob p1 (struct place 0 2 0 1 1))
	(addplace p2 p1)))
	
(def centre (struct place 400 400 0 1 3))
  
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
  [fn1 fn2 n]
  (loop [fna fn1 fnb fn2 n n]
    (if (< n 1) 
	  fna
	  (recur (f-then fn1 (f-both fna fnb)) (f-then fn2 (f-both fnb fna)) (dec n)))))
		
		
(comment "fractal tree")
(comment "                                     x y r s c")
(def fn-a (partial draw-addplace (struct place 0 1 0.01 1 5)))
(def fn-b (partial draw-addplace (struct place 0 1 -0.01 1 7)))
(def fn-s (partial draw-addplace (struct place 0 0 -0.1 0.75 1)))

(def fn-c (f-then fn-s (f-dup fn-a 150)))
(def fn-d (f-then fn-s (f-dup fn-b 175)))
	  
(def fn-f (f-tree fn-c fn-d 9))

(defn fun []
  (fn-f centre)
  (fn-f (assoc centre :r (Math/PI))))
