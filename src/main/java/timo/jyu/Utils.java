//A helper class to create a fit dataset
package timo.jyu;
public class Utils{
	public static double[][] getCircle(double[] cent,double rad,double noise){
		double[][] ret = new double[8][];
		for (int i = 0; i < ret.length; ++i){
			ret[i] = new double[]{cent[0]+rad*Math.cos(((double) i)/((double) ret.length)*2d*Math.PI)+Math.random()*noise,
				cent[1]+rad*Math.sin(((double) i)/((double) ret.length)*2d*Math.PI)+Math.random()*noise};
		}
		return ret;
	}
}