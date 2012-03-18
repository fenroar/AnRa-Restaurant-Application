package project.AnRa.Management;

public class MealItem {
	final private String id, name, main_id, type_id, purchase_count;
	
	public MealItem(String mId, String mName, String mMain_id, String mType_id, String mPurchaseCount) {
		id = mId;
		name = mName;
		main_id = mMain_id;
		type_id = mType_id;
		purchase_count = mPurchaseCount;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getMain_id() {
		return main_id;
	}

	public String getType_id() {
		return type_id;
	}

	public String getPurchase_count() {
		return purchase_count;
	}
	
	public String toString() {
		return this.getName();
	}

}
