package timo.jyu;

//Import apache math classes for Levenber - Marquardt optimisation
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresProblem;
import org.apache.commons.math3.fitting.leastsquares.MultivariateJacobianFunction;
import org.apache.commons.math3.util.Pair;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresBuilder;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer;
import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer;

import java.util.Arrays;	//Fill in an array with a value

public class LMOptimTest{
	double[][] observedPoints;
	double[][] fittedPoints;

	// the model function components are the distances to current estimated center,
	// they should be as close as possible to the specified radius
	//Required for the optimiser
	//Note that the data needs to be fed into the MJF somehow (in this case it's in the parent class)
	MultivariateJacobianFunction errorFunction = new MultivariateJacobianFunction() {
		//This interface has to be implemented. Returns function values, and the Jacobian
		public Pair<RealVector, RealMatrix> value(final RealVector point) {
			 RealVector value = new ArrayRealVector(observedPoints.length);
			 RealMatrix jacobian = new Array2DRowRealMatrix(observedPoints.length, point.getDimension());
			 for (int i = 0; i < observedPoints.length; ++i) {
			     value.setEntry(i, getError(observedPoints[i],point));	//Error function result
				 //Jacobian, partial derivatives w.r.t. the parameters in columns
				 //Error function = sqrt((Xo-Xp)^2+(Yo-Yp)^2)-r = 0 -> (Xo-Xp)^2+(Yo-Yp)^2 = r^2
			     jacobian.setEntry(i, 0, -2d/Math.pow(point.getEntry(2),2d)*(observedPoints[i][0] - point.getEntry(0)));
			     jacobian.setEntry(i, 1, -2d/Math.pow(point.getEntry(2),2d)*(observedPoints[i][1] - point.getEntry(1)));
				 jacobian.setEntry(i, 2, -2*Math.pow(getDist(observedPoints[i],point),2d)*Math.pow(point.getEntry(2),-3d));
			 }
			 return new Pair<RealVector, RealMatrix>(value, jacobian);
		}
		
		//Calculate distance between origin (params[0 and 1]) and coordinate (test[0 and 1])
		private double getDist(double[] test, RealVector params){
			return Math.sqrt(Math.pow(test[0]-params.getEntry(0),2d)+Math.pow(test[1]-params.getEntry(1),2d));
		}
		
		private double getError(double[] test, RealVector params){
			return Math.pow(getDist(test,params),2d)/Math.pow(params.getEntry(2),2d);
		}
	};

	public LMOptimTest(double[][] observedPoints){
		this.observedPoints = observedPoints;		
		double[] prescribedDistances = new double[observedPoints.length];
		Arrays.fill(prescribedDistances, 1d);	//Optimisation is based on the error function -> target = 0

		// least squares problem to solve : modeled radius should be close to target radius
		double[] initGuess = new double[] { 70d, 40d, 40d};
		//System.out.println(String.format("Init X %.1f Y %.1f rad %.1f",initGuess[0],initGuess[1],initGuess[2]));
		
		LeastSquaresProblem problem = new LeastSquaresBuilder().
				                       start(initGuess).	//Initial guess
				                       model(errorFunction). //MJF
				                       target(prescribedDistances).	//Target values
				                       lazyEvaluation(false).
				                       maxEvaluations(100000).
				                       maxIterations(100000).
				                       build();
     	LevenbergMarquardtOptimizer optimiser  = new LevenbergMarquardtOptimizer().
                                    withCostRelativeTolerance(10*Math.ulp(1d)).
                                    withParameterRelativeTolerance(10*Math.ulp(1d));
		LeastSquaresOptimizer.Optimum optimum = optimiser.optimize(problem);
		double[] fit = new double[]{optimum.getPoint().getEntry(0),optimum.getPoint().getEntry(1),optimum.getPoint().getEntry(2)};
		System.out.println(String.format("L-M Fit X %.1f Y %.1f rad %.1f",fit[0],fit[1],fit[2]));
		System.out.println("RMS: "           + optimum.getRMS());
		System.out.println("evaluations: "   + optimum.getEvaluations());
		System.out.println("iterations: "    + optimum.getIterations());
		
		//Create fitted circle points
		fittedPoints = Utils.getCircle(new double[]{fit[0], fit[1]},fit[2],0d);
		
	}
	
	public double[][] getInput(){
		return getPoints(observedPoints);
	}
	
	public double[][] getOutput(){
		return getPoints(fittedPoints);
	}
	
	//Create a copy of the coordinates
	private double[][] getPoints(double[][] coords){
		double[][] ret = new double[coords.length][];
		for (int i =0; i<coords.length;++i){
			ret[i] = new double[]{coords[i][0],coords[i][1]};
		}
		return ret;
	}

}