///SAMPLE FROM APACHE-MATH

package timo.jyu;

 

//Import apache math classes
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresProblem;
import org.apache.commons.math3.fitting.leastsquares.MultivariateJacobianFunction;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.util.Pair;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresBuilder;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer;
import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer;

import java.util.Arrays;	//Fill in an array with a value

public class OptimisationTest{
	final double radius = 70.0;
	Vector2D[] observedPoints;
	Vector2D[] fittedPoints;
	double[] knownCentre;
	/*
	final Vector2D[] observedPoints = new Vector2D[] {
		new Vector2D( 30.0,  68.0),
		new Vector2D( 50.0,  -6.0),
		new Vector2D(110.0, -20.0),
		new Vector2D( 35.0,  15.0),
		new Vector2D( 45.0,  97.0)
	};
	*/
	
	private Vector2D[] getCircle(double[] cent,double rad,double noise){
		Vector2D[] ret = new Vector2D[8];
		for (int i = 0; i < ret.length; ++i){
			ret[i] = new Vector2D(cent[0]+rad*Math.cos(((double) i)/((double) ret.length)*2d*Math.PI)+Math.random()*noise,
				cent[1]+rad*Math.sin(((double) i)/((double) ret.length)*2d*Math.PI)+Math.random()*noise);
		}
		return ret;
	}

	// the model function components are the distances to current estimated center,
	// they should be as close as possible to the specified radius
	//Required for the optimiser
	//Note that the data needs to be fed into the MJF somehow (in this case it's in the parent class)
	MultivariateJacobianFunction distancesToCurrentCenter = new MultivariateJacobianFunction() {
		//This interface has to be implemented. Returns function values, and the Jacobian
		public Pair<RealVector, RealMatrix> value(final RealVector point) {
			 Vector2D center = new Vector2D(point.getEntry(0), point.getEntry(1));
			 RealVector value = new ArrayRealVector(observedPoints.length);
			 RealMatrix jacobian = new Array2DRowRealMatrix(observedPoints.length, 2);
			 for (int i = 0; i < observedPoints.length; ++i) {
			     Vector2D o = observedPoints[i];
			     double modelI = Vector2D.distance(o, center);
			     value.setEntry(i, modelI);
			     // derivative with respect to p0 = x center
			     jacobian.setEntry(i, 0, (center.getX() - o.getX()) / modelI);
			     // derivative with respect to p1 = y center
			     jacobian.setEntry(i, 1, (center.getX() - o.getX()) / modelI);
			 }

			 return new Pair<RealVector, RealMatrix>(value, jacobian);

		}
	};

	public OptimisationTest(){
		// the target is to have all points at the specified radius from the center
		knownCentre = new double[]{50d+40d*Math.random(),20d+40d*Math.random()};
		System.out.println(String.format("Known X %.1f Y %.1f",knownCentre[0],knownCentre[1]));
		
		observedPoints = getCircle(knownCentre,radius,0d);
		
		double[] prescribedDistances = new double[observedPoints.length];
		Arrays.fill(prescribedDistances, radius);

		// least squares problem to solve : modeled radius should be close to target radius
		double[] initGuess = new double[] { 70d, 40d };
		System.out.println(String.format("Init X %.1f Y %.1f",initGuess[0],initGuess[1]));
		
		
		
		LeastSquaresProblem problem = new LeastSquaresBuilder().
				                       start(initGuess).	//Initial guess
				                       model(distancesToCurrentCenter). //MJF
				                       target(prescribedDistances).	//Target values
				                       lazyEvaluation(false).
				                       maxEvaluations(100000).
				                       maxIterations(100000).
				                       build();
     	LevenbergMarquardtOptimizer optimiser  = new LevenbergMarquardtOptimizer().
                                    withCostRelativeTolerance(Math.ulp(1d)).
                                    withParameterRelativeTolerance(Math.ulp(1d));
		LeastSquaresOptimizer.Optimum optimum = optimiser.optimize(problem);
		Vector2D fittedCenter = new Vector2D(optimum.getPoint().getEntry(0), optimum.getPoint().getEntry(1));
		System.out.println("fitted center: " + fittedCenter.getX() + " " + fittedCenter.getY());
		System.out.println("RMS: "           + optimum.getRMS());
		System.out.println("evaluations: "   + optimum.getEvaluations());
		System.out.println("iterations: "    + optimum.getIterations());
		
		//Create fitted circle points
		fittedPoints = getCircle(new double[]{fittedCenter.getX(), fittedCenter.getY()},radius,0d);
		
	}
	
	public double[][] getInput(){
		return getPoints(observedPoints);
	}
	
	public double[][] getOutput(){
		return getPoints(fittedPoints);
	}
	
	private double[][] getPoints(Vector2D[] coords){
		double[][] ret = new double[coords.length][];
		for (int i =0; i<coords.length;++i){
			ret[i] = new double[]{coords[i].getX(),coords[i].getY()};
		}
		return ret;
	}
	
	
	/**Test the class without JavaFX visualisation*/
	public static void main(String[] a){	
		OptimisationTest ot = new OptimisationTest();		
	}
	
	 
	 
	
}
