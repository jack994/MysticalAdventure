import java.io.Serializable;

/**
 * enemies class
 * @author giacomobenso
 */
public class NpcBad extends NPC implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private int bonusAttack;
	
	public NpcBad(String name, String description, int HP,int money, boolean active, String speech, int bonusAttack) {
		super(name, description, HP, money, active, speech);
		this.setBonusAttack(bonusAttack);
	}

	public int getBonusAttack() {
		return bonusAttack;
	}

	public void setBonusAttack(int bonusAttack) {
		this.bonusAttack = bonusAttack;
	}
	
	public String die() {
		isAlive = false;
		return this.getName() + " e' morto" + "<BR>" + dropAllItems();
	}
	
	/**
	 * attack target is active, else talk
	 */
	public String interact(Character pl) { 
		if(isAlive){
		if(isActive()){
			return "<BR><BR>" + attackTarget(pl) + "<BR><BR>";
		}
		else{
			return "<BR><BR>" + getSpeech() + "<BR><BR>";
		}
		}
		else{
			return "";
		}
	}
	
	/**
	 * drop all items carried from the character and add them to the current room.
	 * @return a string describing the action.
	 */
	public String dropAllItems() {
		String all = "";
		for (Tool x : itemsHeld) {
			this.currentRoom.addTool(x);
			all = all + x.getName() + ", ";
		}
		Weapon wp = weapon;
		if (!weapon.getName().equals("nessuna")) {
			currentRoom.addTool((Tool) weapon);
			all += wp.getName() + ", ";
		}
		itemsHeld.clear();
		if (all.length() > 3) {
			all = all.substring(0, (all.length() - 2));
		}
		int mon;
		this.removeMoney(mon = this.getMoneyAmount());
		this.getCurrentRoom().addMoney(mon);
		if (all.equals("")) {
			if (mon < 1)
				return this.getName() + ": non ha con se' nessun'oggetto ";
			else
				return "Monete lasciate: " + mon;
		} else {
			if (mon < 1)
				return this.getName() + " ha lasciato cadere: " + all;
			else
				return this.getName() + " ha lasciato cadere: " + all + "<BR>monete lasciate: " + mon;
		}
	}
}

