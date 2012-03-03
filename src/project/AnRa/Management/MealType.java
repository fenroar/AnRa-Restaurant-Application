package project.AnRa.Management;

public class MealType {

	final private String typeName;

	// Amount of ingredient used in grams
	final private String onionAmount;
	final private String green_pepperAmount;
	final private String mushroomAmount;
	final private String beansproutsAmount;
	final private String pineappleAmount;
	final private String gingerAmount;
	final private String spring_onionAmount;
	final private String babycornAmount;
	final private String bamboo_shootAmount;

	public String getMealType() {
		return typeName;
	}

	public String getOnionAmount() {
		return onionAmount;
	}

	public String getGreenPepperAmount() {
		return green_pepperAmount;
	}

	public String getMushroomAmount() {
		return mushroomAmount;
	}

	public String getBeansproutsAmount() {
		return beansproutsAmount;
	}

	public String getPineappleAmount() {
		return pineappleAmount;
	}

	public String getGingerAmount() {
		return gingerAmount;
	}

	public String getSpringOnionAmount() {
		return spring_onionAmount;
	}

	public String getBabyCornAmount() {
		return babycornAmount;
	}

	public String getBambooShootAmount() {
		return bamboo_shootAmount;
	}

	public MealType(final String name, final String onion, final String green_pepper,
			final String mushroom, final String beansprouts, final String pineapple, final String ginger,
			final String spring_onion, final String babycorn, final String bambooshoots) {
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
