package controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.stage.Stage;
import model.Order;

public class AnalyticsController {
	
	// Views
	@FXML private ScatterChart<Date, Number> scatterChart;
	@FXML private CategoryAxis catAxis;
	@FXML private NumberAxis numAxis;
	
	
	private Main main;
	
	private DatabaseCRUD db = new DatabaseCRUD();
	private MainApp mainApp = new MainApp();

	public void setMain(Main main) {
		this.main = main;
		getChartData();
	}
	
	public void getChartData() {
		mainApp.loadOrders();
		
		// Instantiate the axises
		catAxis = new CategoryAxis();
		numAxis = new NumberAxis(); 
		
		scatterChart.setTitle("ScatterChart 'Test'");
		Series series = new XYChart.Series();
		
		for (Order item : mainApp.getOrderList()) {
			DateFormat df = new SimpleDateFormat("dd/MM/yy");
			Date date = item.getDate();
			String chartDate = df.format(date);
			series.getData().add(new XYChart.Data(chartDate,item.getTotalSum()));
		}	
		scatterChart.getData().add(series);
	}
	
	@FXML
	public void handleWizard() {
		main.showWizard();
	}
	
	@FXML
	public void toDashboard() {
		main.showDashboard();
	}
}
