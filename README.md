Testing [Apache math optimisation](https://commons.apache.org/proper/commons-math/userguide/optimization.html).

Tested
	BOBYQA algorithm, which enables multivariate optimisation without gradients.
	Levenberg-Marquardt algorithm, which requires the gradient with respect to the parameters (Jacobian)

Octave optimisation gives similar results (src/octave folder).
Build with gradle (downloads the Apache Math 3.6.1 dependency for you).
Utilises JavaFX for visualisation (either use oracle Java , or install the prerequisite packages to work with openjdk...)


