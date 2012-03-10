(ns clj-circle-demo.core
  (:use [rosado.processing]
        [rosado.processing.applet])
  (:gen-class))

(def screen-size 800)

(def radar-range (atom 400))

(def radar-colour (atom 125))

(def direction (atom 0))

(def sweep-increment (atom 0.1))

(defn radar-centre []
	[(/ screen-size 2) (/ screen-size 2)])

(defn radar-end [direction start-x start-y]
	(let [ sine (sin direction)
		cosine (cos direction)]
	[(+ (* sine @radar-range) start-x) (+ (* cosine @radar-range) start-y)]))

(defn increment-sweep [current-direction]
	(if (> current-direction (* Math/PI 2))
		0
		(+ current-direction @sweep-increment)))

(defn draw []
	(let [[centre-x centre-y] (radar-centre)
			[end-x end-y] (radar-end @direction centre-x centre-y)]
		(stroke @radar-colour)
		(swap! direction inc)
		(line centre-x centre-y end-x end-y)
		)
)

(defn setup []
   (smooth)
  (no-stroke)
  (fill 226)
  (framerate 20))

;; Now we just need to define an applet:

(defapplet radar
	:title "Radar interactive demo"
	:setup setup
	:draw draw
	:size [screen-size screen-size])


(defn start-demo []
	(run radar))

(defn stop-demo []
	(stop radar))