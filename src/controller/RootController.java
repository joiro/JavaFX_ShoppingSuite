package controller;

import javafx.fxml.FXML;
import javafx.stage.Stage;

public class RootController {
	
	private Main main;
	private Stage primaryStage;

    public void setPrimaryStage(Stage primaryStage) {
    	this.primaryStage = primaryStage;
    }
    
    public void setMain(Main main) {
    	this.main = main;
    }
    
   @FXML
   public void handleClose() {
	   System.exit(0);
   }
    
}
