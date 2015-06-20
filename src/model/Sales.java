package model;

public class Sales {
	
	String period;
	double amount;
	
	public Sales(String period, double amount) {
		this.period = period;
		this.amount = amount;
	}
	
	// getter
	public String getPeriod() { return period; }
	public double getAmount() { return amount; }
	
}
