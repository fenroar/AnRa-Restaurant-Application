package project.AnRa.Management;

public class Meal {
	
	final private String mealName;
	final private String mealPrice;
	
	public String getMealName() {
		return mealName;
	}
	
	public String getMealPrice() {
		return mealPrice;
	}
	
	public Meal(final String name, final String price){
		mealName = name;
		mealPrice = price;
	}

}
