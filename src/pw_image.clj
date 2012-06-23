(ns pw-image)

(comment "Image processing")

(import '(java.awt.image BufferedImage)
        '(java.io File)
        '(javax.imageio ImageIO))

(defn image "Creates an image to write to."
  [width height]
  (new BufferedImage width height (BufferedImage/TYPE_INT_RGB)))

(defn colour "Creates a colour as an array of 3 integers."
  [r g b]
  (int-array [r g b]))

(defn draw "Draws a colour to a pixel of an image"
  [image x y c]
  (if (and (> x -1) (> y -1) (< x (. image getWidth)) (< y (. image getHeight)))
    (. image setRGB (int x) (int y) c)))

(defn write-to-file "Writes an image to file."
  [image f]
  (. ImageIO write image "png" (new File f)))
