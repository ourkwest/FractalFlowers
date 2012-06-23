(ns pw-image)

(comment "Image processing")

(import '(java.awt.image BufferedImage)
        '(java.awt Color RenderingHints)
        '(java.io File)
        '(javax.imageio ImageIO)
        '(java.util HashMap))

(def hints (new HashMap))
(. hints put RenderingHints/KEY_ANTIALIASING RenderingHints/VALUE_ANTIALIAS_ON)

(defn image "Creates an image to write to."
  [width height]
  (new BufferedImage width height (BufferedImage/TYPE_INT_RGB)))

(defn merge-colour
  [c1 c2 prop]
  (let [inv (- 1 prop)] 
    (new Color 
         (int (+ (* (. c1 getRed)   prop) (* (. c2 getRed)   inv)))
         (int (+ (* (. c1 getGreen) prop) (* (. c2 getGreen) inv)))
         (int (+ (* (. c1 getBlue)  prop) (* (. c2 getBlue)  inv)))
         (int (+ (* (. c1 getAlpha) prop) (* (. c2 getAlpha) inv))))))

(defn draw "Draws a colour to a pixel of an image"
  [image x y c]
  (if (and (> x -1) (> y -1) (< x (. image getWidth)) (< y (. image getHeight)))
    (. image setRGB (int x) (int y) c)))

(defn spot "Draws a colour in a circle on an image"
  [image x y r c]
  (if (and (> x -1) (> y -1) (< x (. image getWidth)) (< y (. image getHeight)))
    (let [g (. image getGraphics) diameter (* r 2)]
      (. g setColor c)
      (. g setRenderingHints hints)
      (. g fillArc (- x r) (- y r) diameter diameter 0 360))))

(defn write-to-file "Writes an image to file."
  [image f]
  (. ImageIO write image "png" (new File f)))
