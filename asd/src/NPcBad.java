
public class NPcBad extends Character {

	private String description;
	
	public NPcBad(String name, String description, int HP) {
		super(name, HP);
		this.description = description;

	}
	
	public String getDescription(){
		return description;
	}
	
	public String getItems(){
		String items = "";
		for(int i = 0 ; i < itemsHeld.size() ; i++){
			if(!itemsHeld.get(i).equals(itemsHeld.get(itemsHeld.size() -1)))
				items = items + itemsHeld.get(i).getDescription() + ", " ;
			else items = items + itemsHeld.get(i).getDescription() + ". " ;
		}
		return name + " is carrying: " + items;
		}
	
	public String dropAllItems(){
		
		Weapon wp = weapon;
		if(!weapon.getName().equals("none")){
			currentRoom.addTool((Tools)weapon);
		}	
		weapon = null;
		return super.dropAllItems() + " " + wp.getName();
	}
	
	public String die() {
		isAlive = false;
		return this.dropAllItems();
		}

	public Weapon getWeapon() {
		return weapon;
	}

	public void setWeapon(Weapon weapon) {
		this.weapon = weapon;
	}

	}

