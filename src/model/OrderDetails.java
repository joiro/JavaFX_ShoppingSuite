package model;

public class OrderDetails {
	String category;
	int no;
	
	public OrderDetails(String category, int no) {
		this.category = category;
		this.no = no;
	}
	
	// GETTER
	public String getCategory() { return category; }
	public int getNo() { return no; }
}
