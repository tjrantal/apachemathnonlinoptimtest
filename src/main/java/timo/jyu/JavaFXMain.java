package timo.jyu;


//Visualisation with javaFX
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

public class JavaFXMain extends Application{
	/*Override start to get the javafx class to start*/
    @Override 
    public void start(Stage stage) {
		
		//Create dataset
		double radius = 40+30*Math.random();
		double[] initCentre = new double[]{50d+40d*Math.random(),20d+40d*Math.random()};
		System.out.println(String.format("Observed parameters X %.1f Y %.1f rad %.1f",initCentre[0],initCentre[1],radius));
		
		double[][] observedPoints = Utils.getCircle(initCentre,radius,10d);
		
		//Get the optimisation here
		LMOptimTest lmot = new LMOptimTest(observedPoints);
		//double[][] lminput = lmot.getInput();
		double[][] lmoutput = lmot.getOutput();
		
		BOBYQAOptimisationTest ot = new BOBYQAOptimisationTest(observedPoints);
		//double[][] input = ot.getInput();
		double[][] output = ot.getOutput();
		
		
        stage.setTitle("Scatter Chart Sample");
        final NumberAxis xAxis = new NumberAxis(-100, 200, 10);
        final NumberAxis yAxis = new NumberAxis(-100, 200, 10);        
        final ScatterChart<Number,Number> sc = new
            ScatterChart<Number,Number>(xAxis,yAxis);
        xAxis.setLabel("X");                
        yAxis.setLabel("Y");
        sc.setTitle("Visualisation");
       
        XYChart.Series series1 = new XYChart.Series();
        series1.setName("input");
		for (int i =0; i<observedPoints.length;++i){
			series1.getData().add(new XYChart.Data(observedPoints[i][0],observedPoints[i][1]));
		}
        
        XYChart.Series series2 = new XYChart.Series();
        series2.setName("output");
        for (int i =0; i<output.length;++i){
			series2.getData().add(new XYChart.Data(output[i][0],output[i][1]));
		}
		
		
		XYChart.Series series3 = new XYChart.Series();
        series3.setName("lmoutput");
        for (int i =0; i<lmoutput.length;++i){
			series3.getData().add(new XYChart.Data(lmoutput[i][0],lmoutput[i][1]));
		}
		
 
        sc.getData().addAll(series1, series2,series3);
        Scene scene  = new Scene(sc, 500, 500);
        stage.setScene(scene);
        stage.show();
    }
	
	
    public static void main(String[] args) {
        launch(args);	//Used to start javaFX application
    }
	
	
}