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
		return "stai trasportando: " + items;
	}

	public String die() {
		MysticalAdventure.die();
		return "";
	}

	public void spend(int money) {
		if (this.money >= money)
			this.money -= money;
	}
	
	public Tool buyToolFromMerchant(Merchant merchant, String tool){
		Tool t;
		if((t = merchant.getToolFromString(tool)) != null){
			if(this.getMoneyAmount() >= (t.getValue() * merchant.getPriceModifier())){
				return t;
			}
		}
		return null;
	}

	public String equipWeapon(Weapon weapon){
		for(int i =0; i< itemsHeld.size(); i++){
			if(weapon.equals(itemsHeld.get(i))){
				this.setWeapon(weapon);
				return "hai equipaggiato:" + weapon.getName();
			}
		}
		return null;
	}


}