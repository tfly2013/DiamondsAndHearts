public class Event {
	private static Random gen = new Random;
	private String name;
	private String description;

	// private EventType class specifies the syntax for event types
	private class EventType {
		String name, description;
		int frequency;
		void applyEvent() {} //TODO

		EventType(String name, String desc, Int freq) {
			this.name = name;
			this.frequency = freq;
			this.description = desc;
		}
	}

	// define statitistical distribution of types
	public static final EventType[] allTypes = {
		new EventType("Extra turn",	"",		4),
		new EventType("Extra card",	"",		4),
		new EventType("Skip Turn",	"",		4),
		new EventType("Reverse",	"",		4),
		new EventType("Shield",		"",		4),
		new EventType("Barter",		"",		2),
		new EventType("Strength",	"",		2),
		new EventType("Fortitude",	"",		2),
		new EventType("Absorb",		"",		1),
		new EventType("Drain",		"",		1),
		new EventType("Revive",		"",		1) };

	public Event () {
		// select an event based on the given distribution
		List<EventType> selection = new ArrayList<EventType>();
		for (int i=0; i<allTypes.length(); i++) {
			for (int j=0; j<allTypes[i].frequency; j++)
				selection.add(allTypes[i]);
		}
		int choice = gen.nextInt(allTypes.length);
		name = selection[choice].name;
		description = selection[choice].description;
		
	}
}

