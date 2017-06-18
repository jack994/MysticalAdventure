
public abstract class NPC extends Character {

	private String description;
	private String speech;
	private boolean active;
	private String firstSpeech;
	private boolean firstTimeMet;

	public NPC(String name, String description, int HP, boolean active, String speech) {
		super(name, HP);
		this.description = description;
		this.active = active;
		this.speech = speech;
		setFirstTimeMet(true);
	}

	public String die() {
		isAlive = false;
		return this.dropAllItems();
	}

	public String dropAllItems() {

		Weapon wp = weapon;
		if (!weapon.getName().equals("none")) {
			currentRoom.addTool((Tools) weapon);
		}
		weapon = null;
		return super.dropAllItems() + " " + wp.getName();
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
