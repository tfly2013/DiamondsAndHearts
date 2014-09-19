public class Table {
	private Player[] players;
	private int dp;
	private int playsThisTurn;	

	private void executeCard(Player player, Card card) {
		if (!player.canAfford(card.dp)) {
			--ERROR!!!1!
		}

		player.pay(this, card.dp);

		iterator = card.actions.iterator()
		while (iterator.hasNext()) {
			current = iterator.next();
			if (current.suit == 0) {
				current.target.hp -= current.rank;
			}
			if (current.suit == 1) {
				player.hp += current.rank;
			}				
			if (current.suit == 2) {
				if (current.target.dp < current.rank) {
					player.dp += current.target.dp
					current.target.dp = 0;
				} else {
					player.dp += current.rank;
					current.target.dp -= current.rank;
				}
			}
		}
	}
}

