import java.io.Serializable;

/**
 * abstract class used as superclass for the classes NpcBad and NpcGood
 * @author giacomobenso
 *
 */
public abstract class NPC extends Character implements Serializable{
	
	private static final long serialVersionUID = 1L;

	protected String description;
	protected boolean firstTimeMet;
	protected String secondSpeech;
	protected String speech;
	protected boolean active; //auto-attacker for NPCBad, auto-talker for NPCGood

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
		if(firstTimeMet && (speech != null)){
			firstTimeMet = false;
			return "<b>"+ name + ": "+ "</b>" + speech;
		}
		if(secondSpeech != null){
			return "<b>"+ name + ": "+ "</b>" + secondSpeech;
		}
		else{
			return "<b>"+ name + ": "+ "</b>" + speech;
		}
	}
	
	public void setSecondSpeech(String Nspeech){
		secondSpeech = Nspeech; 
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
	
	public void setDescription(String description){
		this.description = description;
	}
	
	public Weapon getWeapon() {
		return weapon;
	}

	public void setWeapon(Weapon weapon) {
		this.weapon = weapon;
	}
}
