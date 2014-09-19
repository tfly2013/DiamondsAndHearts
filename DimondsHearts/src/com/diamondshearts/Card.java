
public class Card {
	private static Random gen = new Random;

	List<Action> actions = new ArrayList<Action>();
	List<Event> events = new ArrayList<Event>();
	
	public Card() {
		// define the distribution of number of actions/event per card
		int[] actionCountDist = {1,2,3};
		int[] eventCountDist = {1,2,3};

		// add new actions/events
		for (int i=0; i<gen.nextInt(actionCountDist.length()); i++)
			actions.add(new Action());
		for (i=0; i<gen.nextInt(eventCountDist.length()); i++)
			events.add(new Event());
	}
}
