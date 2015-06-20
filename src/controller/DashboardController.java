package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import model.OrderDetails;
import model.Sales;

public class DashboardController {
	
	// Views
	private ToggleGroup group;
	@FXML private ListView<String> listView;
	@FXML private RadioButton year;
	@FXML private RadioButton month;
	@FXML private RadioButton week;
	
	// Pie Chart
	@FXML private PieChart pieChart = new PieChart();
	
	// Line Chart Components
	@FXML private LineChart<String,Number> lineChart;
	private XYChart.Series series;
	@FXML private CategoryAxis xAxis;
	@FXML private NumberAxis yAxis;
	
	@FXML private ObservableList<String> p = FXCollections.observableArrayList();
	@FXML private ObservableList<Sales> sales = FXCollections.observableArrayList();
	@FXML private ObservableList<OrderDetails> orderDetails = FXCollections.observableArrayList();

	private Main main;
	private DatabaseCRUD db = new DatabaseCRUD();
	
	@FXML
	private void initialize() {
		group = new ToggleGroup();
		week.setToggleGroup(group);
		month.setToggleGroup(group);
		year.setToggleGroup(group);
		
		group.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
            public void changed(ObservableValue<? extends Toggle> ov,
                Toggle toggle, Toggle new_toggle) {
            		if (new_toggle == month) {
							salesChart("MONTH");
            		} else if (new_toggle == week) {
							salesChart("YEARWEEK");
            		} else if (new_toggle == year) {
							salesChart("YEAR");
            		}
            }
        });
	}
	
	public void setMain(Main main) {
		this.main = main;
		salesChart("MONTH");
		pieChart();
		setTopProducts();
	}
	
	public void testQuery() {
		
	}
	
	// draw the Sales Chart
	public void salesChart(String time) {
		// empty the chart before redrawing
		lineChart.getData().removeAll(series);
		
		// call the database for the stats
		main.getMonthlyStats(time);
		
		// receive the observable list
		sales = main.getMonthlyStatsList();
		
		// Instantiate the axises
		xAxis = new CategoryAxis();
		yAxis = new NumberAxis();     
                
        lineChart.setTitle("Sales 2015");
        lineChart.setCreateSymbols(false);
                                
        series = new XYChart.Series();
        for (int i=0; i<main.getMonthlyStatsList().size();i++){
        	series.getData().add(new XYChart.Data(sales.get(i).getPeriod(),sales.get(i).getAmount()));
        }
        lineChart.getData().add(series);
        lineChart.setLegendVisible(false);
	}
	
	// show category stats in pieChart
	public void pieChart() {
		pieChart.getData().removeAll();
		orderDetails = main.getOrderDetailsList();
		
		ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
		for (int i=0; i<main.getOrderDetailsList().size();i++) {
			pieChartData.add(new PieChart.Data(orderDetails.get(i).getCategory(),orderDetails.get(i).getNo()));
		}
		pieChart.setData(pieChartData);
		pieChart.setLegendVisible(false);
		pieChart.setStartAngle(90.00);
	}
	
	// get the top 10 products in the listView
	public void setTopProducts() {
		listView.getItems().clear();
		for (int i = 0; i<main.getTopProductList().size();i++) {
			String item = main.getTopProductList().get(i);
			p.add(i+1+". "+item);
		}
		listView.setItems(p);
	}
	
	
	@FXML
	public void openProductManager() {
		main.showProductManager();
	}
	
	@FXML
	public void openCustomerManager() {
		main.showCustomerManager();
	}
	
	@FXML
	public void openAnalytics() {
		main.showAnalytics();
	}
	
	@FXML
	public void openLayout() {
		main.showLayout();
	}
	
	@FXML
	public void handleUpdate() {
		salesChart("MONTH");
		main.loadStatistics();
		pieChart();
		setTopProducts();
	}
}
