public class Player {
	private int		d;
	private int		h;
	private List<Card>	hand;
	private List<Card>	deck;
	private int		cardsDrawn;	

	private void drawCard(int cost) {
		--check if the card can be afforded
		--deduct funds and append card
	}

	private Boolean canSurvive(int amount) {
		return (this.h >= amount); 
	}

	private void hit(Player target, int power) {
		
	}

	private Boolean canAfford(int amount) {
		return (this.dp >= amount);
	}
	
	private void rob(Player target, int power) {
		if (target.dp < power) {
			power = target.dp;
			target.dp = 0;
		} else {
			target.dp -= power;
		}
		this.dp += power;
		return;
	}

	private void pay(Table table, int amount) {
		if (!this.canAfford(amount) {
			--ERROR
		}
		this.dp -= amount;
		table.dp += amount;
		return;
	}
}

