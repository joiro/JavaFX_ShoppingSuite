package controller;
	
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;


//import controller.DBConnect;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Customer;
import model.OrderDetails;
import model.Product;
import model.Sales;


public class Main extends Application {
	
	private Stage primaryStage;
	private BorderPane root;
	
	public static void main(String[] args) { launch(args); }
	
	@Override
	public void start(Stage primaryStage) throws SQLException {
		this.primaryStage = primaryStage;
		this.primaryStage.setResizable(false);
		loadStatistics();
		loadProducts();
		getAllCustomers();
		showRootView();
	}
	
	public void loadStatistics() {
		getTopProducts();
		getOrderDetails();
		
	}
	
	public void showRootView() {
		try {
			// Load the fxml file and load the pane
			FXMLLoader loader = new FXMLLoader(Main.class.getResource("/view/RootView.fxml"));
			root = loader.load();
			
			// give access to the controller file
			RootController rootController = loader.getController();
			
			// create and set the scene
			Scene scene = new Scene(root);

            // set dimensions
			primaryStage.setMinWidth(1200.00);
			primaryStage.setMinHeight(720.00);
            
			// create scene and display it
            primaryStage.setScene(scene);
            primaryStage.show();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		showDashboard();
	}
	
	public void showDashboard() {
		try {
			FXMLLoader  loader = new FXMLLoader(Main.class.getResource("/view/Dashboard.fxml"));
			AnchorPane basePane = loader.load();
			root.setCenter(basePane);
			
			primaryStage.setTitle("Shop Manager");
			
			// Give acces to the controller file
			DashboardController baseViewController = loader.getController();
			baseViewController.setMain(this);
		}
		catch (IOException e){
			e.printStackTrace();
		}
	}
	
	public void showProductManager() {
		try {
			FXMLLoader  loader = new FXMLLoader(Main.class.getResource("/view/ProductManager.fxml"));
			AnchorPane productManagerPane = loader.load();
			root.setCenter(productManagerPane);
			
			primaryStage.setTitle("Product Manager");
			
			// Give acces to the controller file
			ProductManagerController productManagerController = loader.getController();
			productManagerController.setMain(this);
		}
		catch (IOException e){
			e.printStackTrace();
		}
	}
	
	public void showCustomerManager() {
		try {
			FXMLLoader  loader = new FXMLLoader(Main.class.getResource("/view/CustomerManager.fxml"));
			AnchorPane customerManagerPane = loader.load();
			root.setCenter(customerManagerPane);
			
			primaryStage.setTitle("Customer Manager");
			
			// Give acces to the controller file
			CustomerManagerController customerManagerController = loader.getController();
			customerManagerController.setMain(this);
		}
		catch (IOException e){
			e.printStackTrace();
		}
	}
	
	public void showAnalytics() {
		try {
			FXMLLoader  loader = new FXMLLoader(Main.class.getResource("/view/Analytics.fxml"));
			AnchorPane analyticsPane = loader.load();
			root.setCenter(analyticsPane);
			
			primaryStage.setTitle("Analytics");
			
			// Give acces to the controller file
			AnalyticsController analyticsController = loader.getController();
			analyticsController.setMain(this);
		}
		catch (IOException e){
			e.printStackTrace();
		}
	}
	
	public void showLayout() {
		try {
			FXMLLoader  loader = new FXMLLoader(Main.class.getResource("/view/Layout.fxml"));
			AnchorPane layoutPane = loader.load();
			root.setCenter(layoutPane);
			
			primaryStage.setTitle("Layout");
			
			// Give acces to the controller file
			LayoutController layoutController = loader.getController();
			layoutController.setMain(this);
		}
		catch (IOException e){
			e.printStackTrace();
		}
	}
	
	
	public void showWizard() {
		try {
			// Connect View
			FXMLLoader loader = new FXMLLoader(Main.class.getResource("/view/WizardView.fxml"));
			AnchorPane pane = loader.load();
			
			Stage stage = new Stage();
			
			// Connect controller
			WizardController wizardController = loader.getController();
			wizardController.setMain(this, stage);
			
			Scene scene = new Scene(pane);
			
			stage.initOwner(primaryStage);
			stage.initModality(Modality.WINDOW_MODAL);
			
			// Set stage dimensions
			stage.setMinWidth(600.00);
			stage.setMinHeight(400.00);
			
			stage.setScene(scene);
			stage.show();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
// DATABASE HANDLING
	
	// GET MONTHLY SALES CHART INFORMATION
	
	private ObservableList<Sales> monthlyStats = FXCollections.observableArrayList();
	public ObservableList<Sales> getMonthlyStatsList() { return monthlyStats; }
	
	public void getMonthlyStats(String period) {
		String period1 = period;
		if (period == "YEARWEEK"){
			period1 = "WEEK";
		} else if (period == "MONTH") {
			
		}
		monthlyStats.remove(0, monthlyStats.size());
		String SQL = "SELECT sum(oAmount) as amount, "+period1+"(oDate) as period "
				+ "FROM orders "
				+ "GROUP BY "+period+"(oDate) ";
				//+ "LIMIT 6";
		try (
				Connection c = DBConnect.connect();
				ResultSet rs = c.createStatement().executeQuery(SQL);
		) {
			while(rs.next()) {
				String sPeriod = rs.getString("period");
				Double sAmount = rs.getDouble("amount");
				monthlyStats.add(new Sales(sPeriod, sAmount));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// GET CATEGORY STATS
	private ObservableList<OrderDetails> orderDetails = FXCollections.observableArrayList();
	public ObservableList<OrderDetails> getOrderDetailsList() { return orderDetails; }
	
	public void getOrderDetails() {
		orderDetails.remove(0, orderDetails.size());
		String SQL = "select pCategory, sum(odProduct * odQuantity) as 'No ordered' "
				+ "from orderDetails, product "
				+ "where product.pID = orderdetails.odProduct "
				+ "group by pCategory";
		try (
				Connection c = DBConnect.connect();
				ResultSet rs = c.createStatement().executeQuery(SQL);
		) {
			while(rs.next()) {
				String category = rs.getString("pCategory");
				int no = rs.getInt("No ordered");
				orderDetails.add(new OrderDetails(category, no));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	// GET TOP PRODUCTS
	
	private ObservableList<String> topProducts = FXCollections.observableArrayList();
	public ObservableList<String> getTopProductList() { return topProducts; }
	
	public void getTopProducts() {
		topProducts.clear();
		String SQL = "SELECT pName, sum(odQuantity) as count "
				+ "FROM product, orderDetails "
				+ "WHERE product.pID = orderDetails.odProduct "
				+ "GROUP BY pID "
				+ "ORDER BY count desc "
				+ "LIMIT 10";
		try (
				Connection c = DBConnect.connect();
				ResultSet rs = c.createStatement().executeQuery(SQL);
		) {
			while(rs.next()) {
				String product = rs.getString("pName");
				int count = rs.getInt("count");
				topProducts.add(product+" ("+count+")");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// GET ALL PRODUCTS
	
	private ObservableList<Product> products = FXCollections.observableArrayList();
	public ObservableList<Product> getProductList() { return products; }
	
	// read product data from database and set to ObservableList
	public void loadProducts() {
		//products.clear();
		products.remove(0, products.size());
		String SQL = "SELECT * from PRODUCT";
		try (
				Connection c = DBConnect.connect();
	            ResultSet rs = c.createStatement().executeQuery(SQL);
		) {
            while(rs.next()){
            	int pID = rs.getInt("pID");
            	String name = rs.getString("pName");
            	double price = rs.getDouble("pPrice");
            	String category = rs.getString("pCategory");
            	String  image = rs.getString("pImage");
            	int rating = rs.getInt("pRating");
            	String description = rs.getString("pDescription");
            	int stock = rs.getInt("pStockQuantity");
            	products.add(new Product(pID, name, price, category, image, rating, description, stock));
            }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// GET ALL CUSTOMERS
	
	private ObservableList<Customer> customers = FXCollections.observableArrayList();
	public ObservableList<Customer> getCustomerList() { return customers; }
	
	public void getAllCustomers() {
		customers.clear();
		String SQL = "SELECT * FROM customer";
		try (
				Connection c = DBConnect.connect();
				ResultSet rs = c.createStatement().executeQuery(SQL);
				) {
			while(rs.next()) {
				int cID = rs.getInt("cID");
				String cFirstName = rs.getString("cFirstName");
				String cLastName = rs.getString("cLastName");
				String cEmail = rs.getString("cEmail");
				String cStreet = rs.getString("cStreet");
				String cCity = rs.getString("cCity");
				String cPostcode = rs.getString("cPostcode");
				String cCountry = rs.getString("cCountry");
				customers.add(new Customer(cID, cFirstName, cLastName, cStreet, cCity, cEmail, cPostcode, cCountry));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}