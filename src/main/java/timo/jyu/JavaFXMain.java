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
		
		//Get the optimisation here
		OptimisationTest ot = new OptimisationTest();
		double[][] input = ot.getInput();
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
		for (int i =0; i<input.length;++i){
			series1.getData().add(new XYChart.Data(input[i][0],input[i][1]));
		}
        
        XYChart.Series series2 = new XYChart.Series();
        series2.setName("output");
        for (int i =0; i<input.length;++i){
			series2.getData().add(new XYChart.Data(output[i][0],output[i][1]));
		}
 
        sc.getData().addAll(series1, series2);
        Scene scene  = new Scene(sc, 500, 500);
        stage.setScene(scene);
        stage.show();
    }
	
	
    public static void main(String[] args) {
        launch(args);	//Used to start javaFX application
    }
	
	
}