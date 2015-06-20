package controller;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class WizardController {
	
	// Views
	@FXML private ComboBox<String> comboTable1, comboTable2, comboFactor1, comboFactor2;
	@FXML private Label table2, factor2, selectFactors, selectTable2;
	
	private boolean firstTableLoaded, firstFactorLoaded;
	private ObservableList<String> tableNames;
	ObservableList<String> columnNames1, columnNames2;
	
	private Main main;
	private DatabaseCRUD db = new DatabaseCRUD();
	private Stage stage;
	
	public void initialize() {
		// Set changeListener for the comboBoxes
		comboTable1.valueProperty().addListener((observable, oldValue, newValue) -> {
			System.out.println("comboTable1: "+newValue);
			if (newValue !=null) {
				setFactorBox(newValue.toString());
			}
		});
		
		comboTable2.valueProperty().addListener((observable, oldValue, newValue) -> {
			System.out.println("comboTable2: "+newValue);
			setFactorBox2(newValue.toString());
		});
	}
	
	public void setMain(Main main, Stage stage) {
		this.main = main;
		this.stage = stage;
		setTablesBoxes();
	}
	// fill comboBoxes with tables names
	public void setTablesBoxes() {
		db.getTableNames();
		tableNames = db.getTableNameList();
		comboTable1.setItems(tableNames);
		comboTable2.setItems(tableNames);
	}
	
	// fill comboBoxes with factor names when a table was selected
	public void setFactorBox(String table) {
		db.getColumnNames(table);
				System.out.println("setFactorBox 1");
				columnNames1 = db.getColumnNameList();
				comboFactor1.setItems(columnNames1);
				enableTableTwo();
	}
	
	// fill comboBoxes with factor names when a table was selected
	public void setFactorBox2(String table) {
		db.getColumnNames(table);
				System.out.println("setFactorBox 2");
				columnNames2 = db.getColumnNameList();
				comboFactor2.setItems(columnNames2);
	}
	
	// enables the second set of comboBoxes
	public void enableTableTwo() {
		selectTable2.setDisable(false);
		comboTable2.setDisable(false);
		comboFactor2.setDisable(false);
		factor2.setDisable(false);
		table2.setDisable(false);
	}
	
	@FXML
	public void handleCancel() {
		stage.close();
	}
	
	@FXML
	public void handleNext() {
		System.out.println("handleNext");
	}
}
