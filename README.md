Testing [Apache math optimisation](https://commons.apache.org/proper/commons-math/userguide/optimization.html).

Used BOBYQA algorithm, which enables multivariate optimisation without gradients.
Octave optimisation gives the same result (src/octave folder).
Build with gradle (downloads the Apache Math 3.6.1 dependency for you).
Utilises JavaFX for visualisation (either use oracle Java , or install the prerequisite packages to work with openjdk...)

Change BOBYQAOptimisationTest.java line 62 (at the time of writing this README) last parameter to test with noise, e.g.

observedPoints = getCircle(initCentre,radius,10d);

As is, the input and output points will overlay.
