import java.io.Serializable;

public class Player extends Character implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	public Player(String name) {
		super(name, 100, 100);
	}

	public String getItems() {
		String items = "";
		for (int i = 0; i < itemsHeld.size(); i++) {
			if (!itemsHeld.get(i).equals(itemsHeld.get(itemsHeld.size() - 1)))
				items = items + itemsHeld.get(i).getName() + ", ";
			else
				items = items + itemsHeld.get(i).getName() + ". ";
		}
		return "you are carrying: " + items;
	}

	public String die() {
		//////////////////////////
		///////// TODO/////////////
		//////////////////////////
		return "you died";
	}

	public void spend(int money) {
		if (this.money >= money)
			this.money -= money;
	}

	public String equipWeapon(Weapon weapon){
		for(int i =0; i< itemsHeld.size(); i++){
			if(weapon.equals(itemsHeld.get(i))){
				this.setWeapon(weapon);
				return weapon.getName() + " equipped";
			}
		}
		return null;
	}


}