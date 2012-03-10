(ns clj-circle-demo.core
  (:use [rosado.processing]
        [rosado.processing.applet])
  (:gen-class))

(def screen-size 800)

(def radar-length (atom 350))

(def radar-colour (atom 125))

(def direction (atom 0))

(def sweep-increment (atom 0.01))

(def draw-bob (atom nil))

(defn radar-centre []
	[(/ screen-size 2) (/ screen-size 2)])

(defn radar-end [direction start-x start-y]
	(let [ sine (sin direction)
		cosine (cos direction)]
	[(+ (* sine @radar-length) start-x) (+ (* cosine @radar-length) start-y)]))

(defn increment-sweep [current-direction]
	(if (> current-direction (* Math/PI 2))
		0
		(+ current-direction @sweep-increment)))

(defn draw []
	(let [[centre-x centre-y] (radar-centre)
			[end-x end-y] (radar-end @direction centre-x centre-y)]
		(stroke @radar-colour)
		(swap! direction increment-sweep)
		(line centre-x centre-y end-x end-y)
		(if @draw-bob
			(@draw-bob end-x end-y))))

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

(defn constant [new-value] (fn[current-value] new-value))

(def current-bob-width (atom 5))
(def black 0)
(def increasing (atom true))
(def bob-colour (atom black))

(defn draw-bob-circle [x y radius]
	(stroke @bob-colour)
		(fill @bob-colour)
		(ellipse x y radius radius))

(defn draw-fixed-bob [x y]
	(let [bob-width 5
		bob-color black]
		(draw-bob x y bob-width)))

(defn draw-teardrop-bob [x y]
	(let [bob-width @current-bob-width
		bob-color black
		next-width (mod (+ 10 (inc @current-bob-width)) 25)]
		(draw-bob-circle x y bob-width)
		(swap! current-bob-width (constant next-width))))

(defn draw-fluxing-bob [x y]
	(defn next-bob-width [current-width]
		(cond
			(> current-width 150) (swap! increasing (constant false))
			(< current-width 2) (swap! increasing (constant true)))

		(if @increasing
			(inc current-width)
			(dec current-width)))

	(let [bob-width @current-bob-width
		bob-color black
		next-width (next-bob-width @current-bob-width)]
		(draw-bob-circle x y bob-width)
		(swap! current-bob-width (constant next-width))))