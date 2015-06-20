package controller;

import java.util.Optional;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Customer;

public class CustomerManagerController {
	
	// Views
	@FXML private TableView<Customer> customerTable;
	@FXML private TableColumn cIDColumn;
	@FXML private TableColumn cNameColumn;
	@FXML private TextField searchCustomers;
	@FXML private Label cID, cFirstName, cLastName, cEmail, cStreet, cCity, cPostcode, cCountry;
	
	private Main main;
	private DatabaseCRUD db = new DatabaseCRUD();

	public void setMain(Main main) {
		this.main = main;
		this.setCustomerTable();
		customerTable.getSelectionModel().select(0);
	}
	
	public void setCustomerTable() {
		cIDColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("cID"));
		cNameColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("lastName"));
		customerTable.getSelectionModel().selectedItemProperty().addListener(
        		new ChangeListener<Customer>() {
                    public void changed(ObservableValue<? extends Customer> observable, Customer oldValue, Customer newValue) {
                    	showCustomerDetails(newValue);
                    }
                });
        ObservableList<Customer> customerData = main.getCustomerList();
		FilteredList<Customer> filteredList = new FilteredList<>(customerData, p -> true);
		searchCustomers.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredList.setPredicate(customer -> {
				// If filter text is empty, display all customers.
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}
				String lowerCaseFilter = newValue.toLowerCase();
				if (customer.getLastName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true; // Filter matches last name.
				}
				return false; // Does not match.
			});
		});
		SortedList<Customer> SortedList = new SortedList<>(filteredList);
		SortedList.comparatorProperty().bind(customerTable.comparatorProperty());
		customerTable.setItems(SortedList);	
	}
	
	public void showCustomerDetails(Customer customer) {
		cID.setText(Integer.toString(customer.getCID()));
		cFirstName.setText(customer.getFirstName());
		cLastName.setText(customer.getLastName());
		cEmail.setText(customer.getEmailAddress());
		cStreet.setText(customer.getStreet());
		cCity.setText(customer.getCity());
		cPostcode.setText(customer.getPostcode());
		cCountry.setText(customer.getCountry());
	}
	
	@FXML
	private void deleteCustomer() {
		Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("delete Customer");
		alert.setHeaderText(null);
		alert.setContentText("Are you sure you want to delete this profile?");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			db.delete("customer","cID",selectedCustomer.getCID());
		} else {
			alert.close();
		}
	}
	
	@FXML
	public void toDashboard() {
		main.showDashboard();
	}

}
