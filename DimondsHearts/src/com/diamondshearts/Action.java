public class Action {
	private static Random gen = new Random;
	private Int rank;
	private String suit;
	private String description;

	// private Suit class specifies syntax for suits
	private class Suit {
		String name, description;
		int frequency;

		Suit(String name, String desc, Int freq) {
			this.name = name;
			this.frequency = freq;
			this.description = desc;
		}
	}

	// define statistical distribution of suits
	public static final Suit[] allSuits = {
		new Suit("Spade",		"",		1),
		new Suit("Heart",		"",		1),
		new Suit("Diamond",		"",		3),
		new Suit("Club",		"",		3) };

	public Action () {
		// select a suit based on the given distribution
		List<Suit> selection = new ArrayList<Suit>();
		for (int i = 0; i < allSuits.length(); i++) {
			for (int j = 0; j < allSuits[i].frequency; j++)
				selection.add(allSuits[i]);
		}
		int choice = gen.nextInt(allTypes.length());
		suit = selection.get(choice).name;
		description = selection.get(choice).description;

		// define statistical distribution of ranks
		int[] rankDistribution = {1,1,1,2,2,2,3,3,3,4,4,5,5,6,6,7,8,9};
		choice = gen.next(rankDistribution.length());
		rank = rankDistribution[choice];
	}
}

