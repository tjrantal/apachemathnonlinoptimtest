///SAMPLE FROM APACHE-MATH

package timo.jyu;

 

//Import apache math classes
import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BOBYQAOptimizer;
import org.apache.commons.math3.util.Pair;

import java.util.Arrays;	//Fill in an array with a value

public class BOBYQAOptimisationTest{
	double[][] observedPoints;
	double[][] fittedPoints;

	/**The error function class*/
	public class CircleErrorFunction implements MultivariateFunction {
			/**Implement the MultivariateFunction interface. This is the summed squares error
				@input x are the parameters that are being optimised
			*/
        @Override
        public double value(double[] x) {
            double f = 0;	//Sum of squared residuals
            for (int i = 0; i < observedPoints.length; ++i) {
                f += Math.pow(Math.sqrt(Math.pow(observedPoints[i][0]- x[0],2d) 
                		+Math.pow(observedPoints[i][1]- x[1],2d) 
                		) - x[2]
                		,2);
            }
            return f;
        }
    }	
	
	public BOBYQAOptimisationTest(double[][] observedPoints){
		this.observedPoints = observedPoints;
		// least squares problem to solve : modeled radius should be close to target radius
		double[] initGuess = new double[] { 70d, 40d, 40d };
		//System.out.println(String.format("Init X %.1f Y %.1f rad %.1f",initGuess[0],initGuess[1],initGuess[2]));
		int dim = initGuess.length;
		int additionalInterpolationPoints = 0;
      int numIterpolationPoints = 2 * dim + 1 + additionalInterpolationPoints;
      int maxEvaluations = 2000;
        BOBYQAOptimizer optim = new BOBYQAOptimizer(numIterpolationPoints);
        PointValuePair result = optim.optimize(new MaxEval(maxEvaluations),
                           new ObjectiveFunction(new CircleErrorFunction()),
                           GoalType.MINIMIZE,
                           SimpleBounds.unbounded(dim),
                           new InitialGuess(initGuess));
		
		double[] fit = new double[]{result.getPoint()[0],result.getPoint()[1],result.getPoint()[2]};
		System.out.println(String.format("BOBYQA Fit X %.1f Y %.1f rad %.1f",fit[0],fit[1],fit[2]));
		System.out.println("RMS: " + result.getValue());
		//System.out.println("evaluations: "   + optimum.getEvaluations());
		//System.out.println("iterations: "    + optimum.getIterations());
		
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
