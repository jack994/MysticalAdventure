import java.io.Serializable;

/**
 * Player class 
 * @author giacomobenso
 */
public class Player extends Character implements Serializable{

	private static final long serialVersionUID = 1L;

	public Player(String name) { 
		super(name, 100, 100);
	}

	public String die() {
		MysticalAdventure.die(); //call the die method in the MysticalAdventure class
		return "";
	}

	public void spend(int money) {
		if (this.money >= money)
			this.money -= money;
	}
	
	/**
	 * buy a tool from the merchant in the current room, if it exists
	 * @param merchant
	 * @param tool
	 * @return the item to be bought
	 */
	public Tool buyToolFromMerchant(Merchant merchant, String tool){
		Tool t;
		if((t = merchant.getToolFromString(tool)) != null){
			if(this.getMoneyAmount() >= (t.getValue() * merchant.getPriceModifier())){
				return t;
			}
		}
		return null;
	}

	/**
	 * equip a carried weapon so that it can be used
	 * @param weapon
	 * @return
	 */
	public String equipWeapon(Weapon weapon){
		for(int i =0; i< itemsHeld.size(); i++){
			if(weapon.equals(itemsHeld.get(i))){
				this.setWeapon(weapon);
				return weapon.getName() + " equipaggiata";
			}
		}
		return null;
	}
	
	/**
	 * check how many ingredients have been found
	 * @return the number of ingredients found (in the arraylist)
	 */
	public int ingredientsFound(){
		int ret = 0;
		for(Tool t : itemsHeld){
			if(t.getClass() == Ingredient.class)
				ret ++;
		}
		return ret;
	}


}