package controller;

import javafx.fxml.FXML;

public class LayoutController {
	
	private Main main;

	public void setMain(Main main) {
		this.main = main;
	}
	
	@FXML
	public void toDashboard() {
		main.showDashboard();
	}

}
