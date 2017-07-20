import java.io.Serializable;

/**
 * abstract class used as superclass for the classes NpcBad and NpcGood
 * @author giacomobenso
 *
 */
public abstract class NPC extends Character implements Serializable{

	private String description;
	private boolean firstTimeMet;
	private String firstSpeech;
	private String speech;
	private boolean active; //auto-attacker for NPCBad, auto-talker for NPCGood

	public NPC(String name, String description, int HP, int money, boolean active, String speech) {
		super(name, HP, money);
		this.description = description;
		this.active = active;
		this.speech = speech;
		setFirstTimeMet(true);
	}
	
	public abstract String interact(Character pl);

	public String die() {
		isAlive = false;
		return this.dropAllItems();
	}

	public String dropAllItems() {

		Weapon wp = weapon;
		if (!weapon.getName().equals("none")) {
			currentRoom.addTool((Tool) weapon);
			return super.dropAllItems() + ", " + wp.getName();
		}
		weapon = null;
		return super.dropAllItems();
	}
	
	public void setSpeech(String newSpeech){
		speech = newSpeech;
	}

	public String getSpeech(){
		if(firstTimeMet && (firstSpeech != null)){
			firstTimeMet = false;
			return "<b>"+ name + ": "+ "</b>" + firstSpeech;
		}
		return "<b>"+ name + ": "+ "</b>" + speech;
	}
	
	public void setFirstSpeech(String firstSpeech){
		this.firstSpeech = firstSpeech; 
	}

	public boolean isActive() {
		return active;
	}

	public String getDescription() {
		return description;
	}

	public String getItems() {
		String items = "";
		for (int i = 0; i < itemsHeld.size(); i++) {
			if (!itemsHeld.get(i).equals(itemsHeld.get(itemsHeld.size() - 1)))
				items = items + itemsHeld.get(i).getDescription() + ", ";
			else
				items = items + itemsHeld.get(i).getDescription() + ". ";
		}
		return name + " is carrying: " + items;
	}

	public boolean isFirstTimeMet() {
		return firstTimeMet;
	}

	public void setFirstTimeMet(boolean firstTimeMet) {
		this.firstTimeMet = firstTimeMet;
	}
	
	public Weapon getWeapon() {
		return weapon;
	}

	public void setWeapon(Weapon weapon) {
		this.weapon = weapon;
	}
}
