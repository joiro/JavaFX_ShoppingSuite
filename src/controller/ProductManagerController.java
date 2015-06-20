package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Product;

public class ProductManagerController {
	
	// Views
	@FXML TableView<Product> productTable;
	@FXML TableColumn productColumn;
	@FXML TableColumn categoryColumn;
	
	@FXML Button editProduct, saveChanges, discardChanges;
	
	@FXML TextField searchProducts, nameField, categoryField, priceField;
	@FXML TextArea descriptionArea;
	@FXML Label pID, pName, pPrice, pCategory, pRating, pDescription;
	@FXML ImageView pImage;

	private Main main;
	private DatabaseCRUD db = new DatabaseCRUD();

	public void setMain(Main main) {
		this.main = main;
		//main.loadProducts();
		this.setProductTable();
		
	}
	
	public void setProductTable() {
		productColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
		categoryColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("category"));
		
		productTable.getSelectionModel().selectedItemProperty().addListener(
        		new ChangeListener<Product>() {
                    public void changed(ObservableValue<? extends Product> observable, Product oldValue, Product newValue) {
                    	showProductDetails(newValue);
                    }
                });
        ObservableList<Product> productData = main.getProductList();
		
        // Wrap the ObservableList in a FilteredList (initially display all data).
		FilteredList<Product> filteredList = new FilteredList<>(productData, p -> true);
		
		// Set the filter Predicate whenever the filter changes.
		searchProducts.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredList.setPredicate(product -> {
				// If filter text is empty, display all customers.
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}
				String lowerCaseFilter = newValue.toLowerCase();
				if (product.getName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true; // Filter matches last name.
				}
				return false; // Does not match.
			});
		});

		// Wrap the FilteredList in a SortedList. 
		SortedList<Product> SortedList = new SortedList<>(filteredList);
		
		//  Bind the SortedList comparator to the TableView comparator.
		SortedList.comparatorProperty().bind(productTable.comparatorProperty());
		
		// Add sorted (and filtered) data to the table.
		productTable.setItems(SortedList);	
		productTable.getSelectionModel().select(0);
	}
	
	public void showProductDetails(Product product) {
		pID.setText(Integer.toString(product.getPID()));
		pName.setText(product.getName());
		pCategory.setText(product.getCategory());
		pPrice.setText(Double.toString(product.getPrice()));
		//pRating.setText(Integer.toString(product.getRating()));
		pDescription.setText(product.getDescription());
		Image productImage = new Image(product.getImage());
		pImage.setImage(productImage);
	}
	
	public void handleEditProduct() {
		editProduct.setVisible(false);
		saveChanges.setVisible(true);
		discardChanges.setVisible(true);
		nameField.setVisible(true);
		pName.setVisible(false);
		categoryField.setVisible(true);
		priceField.setVisible(true);
		descriptionArea.setVisible(true);
		
		Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
		
		nameField.setText(selectedProduct.getName());
		categoryField.setText(selectedProduct.getCategory());
		priceField.setText(Double.toString(selectedProduct.getPrice()));
		descriptionArea.setText(selectedProduct.getDescription());
	}
	
	@FXML
	public void handleChanges(Event event) {
		Button btn = (Button) event.getSource();
		saveChanges.setVisible(false);
		discardChanges.setVisible(false);
		pName.setVisible(true);
		editProduct.setVisible(true);
		nameField.setVisible(false);
		categoryField.setVisible(false);
		priceField.setVisible(false);
		descriptionArea.setVisible(false);
		
		if(btn.getId().equals("saveChanges")){
			Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
			db.update("product","pName = '"+nameField.getText()+
					"', pCategory = '"+categoryField.getText()+
					"', pDescription = '"+descriptionArea.getText()+
					"', pPrice = ",Double.parseDouble(priceField.getText()),
					"pID ",selectedProduct.getPID());
			main.loadProducts();
		}
	}
	
	@FXML
	public void toDashboard() {
		main.showDashboard();
	}

}
