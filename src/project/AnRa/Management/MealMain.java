package project.AnRa.Management;

public class MealMain {

	final private String mainName;

	// Amount of ingredient used in grams
	final private String chickenAmount;
	final private String beefAmount;
	final private String porkAmount;
	final private String prawnAmount;
	final private String charSiuAmount;
	final private String hamAmount;
	final private String kingPrawnAmount;

	public String getMealmain() {
		return mainName;
	}

	public String getChicken() {
		return chickenAmount;
	}

	public String getBeef() {
		return beefAmount;
	}

	public String getPorkAmount() {
		return porkAmount;
	}

	public String getPrawnAmount() {
		return prawnAmount;
	}

	public String getCharSiuAmount() {
		return charSiuAmount;
	}

	public String getHamAmount() {
		return hamAmount;
	}

	public String getKingPrawnAmount() {
		return kingPrawnAmount;
	}

	public MealMain(final String name, final String chicken, final String beef,
			final String pork, final String prawn, final String charSiu,
			final String ham, final String kingPrawn) {
		mainName = name;
		chickenAmount = chicken;
		beefAmount = beef;
		porkAmount = pork;
		prawnAmount = prawn;
		charSiuAmount = charSiu;
		hamAmount = ham;
		kingPrawnAmount = kingPrawn;
	}

}
