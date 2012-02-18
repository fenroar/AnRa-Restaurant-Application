package project.AnRa.Management;

public class MealType {

	final private String typeName;

	// Amount of ingredient used in grams
	final private int onionAmount;
	final private int green_pepperAmount;
	final private int mushroomAmount;
	final private int beansproutsAmount;
	final private int pineappleAmount;
	final private int gingerAmount;
	final private int spring_onionAmount;
	final private int babycornAmount;
	final private int bamboo_shootAmount;

	public String getMealType() {
		return typeName;
	}

	public int getOnionAmount() {
		return onionAmount;
	}

	public int getGreenPepperAmount() {
		return green_pepperAmount;
	}

	public int getMushroomAmount() {
		return mushroomAmount;
	}

	public int getBeansproutsAmount() {
		return beansproutsAmount;
	}

	public int getPineappleAmount() {
		return pineappleAmount;
	}

	public int getGingerAmount() {
		return gingerAmount;
	}

	public int getSpringOnionAmount() {
		return spring_onionAmount;
	}

	public int getBabyCornAmount() {
		return babycornAmount;
	}

	public int getBambooShootAmount() {
		return bamboo_shootAmount;
	}

	public MealType(final String name, final int onion, final int green_pepper,
			final int mushroom, final int beansprouts, final int pineapple, final int ginger,
			final int spring_onion, final int babycorn, final int bambooshoots) {
		typeName = name;
		onionAmount = onion;
		green_pepperAmount = green_pepper;
		mushroomAmount = mushroom;
		beansproutsAmount = beansprouts;
		pineappleAmount = pineapple;
		gingerAmount = ginger;
		spring_onionAmount = spring_onion;
		babycornAmount = babycorn;
		bamboo_shootAmount = bambooshoots;
	}

}
