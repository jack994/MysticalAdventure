
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public abstract class Character implements Serializable{

	private static final long serialVersionUID = 1L;
	
	protected String name;
	protected boolean isAlive;
	protected ArrayList<Tool> itemsHeld;
	protected Ingredient[] ingredients;
	protected Room currentRoom;
	protected int money;
	protected int HP; // initial life
	protected int lifeRemaining;
	protected Weapon weapon;

	public Character(String name, int HP, int money) {
		itemsHeld = new ArrayList<Tool>();
		isAlive = true;
		this.name = name;
		this.HP = HP;
		this.money = money;
		lifeRemaining = 100;
		Weapon NN = new Weapon("nessuna", "nessun'arma", 0, 1, 0.95f);//Ciao
		weapon = NN; // test commento
		ingredients = new Ingredient[GameWindow.numOfIngredients];
	}

	//-------------abstract methods-------------//
	
	public abstract String getItems();
	public abstract String die();
	
	
	//------------------getter methods-----------------//
	
	public String getName() {
		return name;
	}

	public boolean isAlive() {
		return isAlive;
	}

	public int getHP() {
		return HP;
	}
	
	public int getLifeRemaining(){
		return lifeRemaining;
	}
	
	public int getMoneyAmount() {
		return money;
	}

	public Room getCurrentRoom() {
		return currentRoom;
	}
	
	public Weapon getWeapon() { //altro commento
		return weapon;
	}
	
	public ArrayList<Tool> getItemsHeldArray(){
		return itemsHeld;
	}
	//----------------setter methods-------------------//
	
	public void addMoney(int money) {
		this.money += money;
		MysticalAdventure.GAME.frame.getMoneyLabel().setText(this.money + "");
	}
	
	public void removeMoney(int money){
		if(this.money > money){
		this.money -= money;
		MysticalAdventure.GAME.frame.getMoneyLabel().setText(this.money + "");
		}
	}
	
	public void setCurrentRoom(Room room) {
		currentRoom = room;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public void setLifeRemaining(int life){
		this.lifeRemaining = life;
	}
	
	public void setHP(int hP) {
		HP = hP;
	}
	
	public void setWeapon(Weapon weapon){
		this.weapon = weapon;
	}
	
	//---------------------------------------------//

	/**
	 * add object to the ArrayList containing the items the character is carying.
	 * @param o
	 */
	public void addObj(Tool t) {
		itemsHeld.add(t);
	}

	/**
	 * @param s: string to be recognised
	 * @return the object in the arraylist corresponding to the string passed
	 */
	public Tool getToolFromString(String s) {
		for (Tool x : itemsHeld) {
			if (x.getName().equals(s)) {
				return x;
			}
		}
		return null;
	}
	
	public Ingredient getIngredient(String name){
		for(int i = 0; i < ingredients.length; i ++){
			if(ingredients[i] != null)
			if(ingredients[i].getName().equals(name)){
				return ingredients[i];
			}
		}
		return null;
	}
	
	public String addIngredient(Ingredient ing){
		for(int i = 0; i < ingredients.length; i ++){
			if(ingredients[i] == null){
				ingredients[i] = ing;
				return "<BR><BR>" + ing.getName() + " aggiunto all'inventario di " + this.getName() + " <BR>";
			}
		}
		return "non puoi trasportare più di 3 ingredienti";
	}
	
	public void removeIngredient(Ingredient ing){
		for(int i = 0; i < ingredients.length; i ++){
			if(ingredients[i] != null){
			if(ingredients[i].equals(ing)){
				ingredients[i] = null;
			}
			}
		}
	}

	/**
	 * add an object to the ArrayList containing the items carried.
	 * @param o: String corresponding to the item.
	 * @return a string describing the action.
	 */
	public String addObjCalled(String o) {
		String b = "";
		Item t;
		if((t = currentRoom.getItemNamed(o))!= null){
			itemsHeld.add((Tool) t);
			b = o;
		}
	
		if (b.equals("")) {
			return null;
		} else {
			return this.getName() + " ha aggiuto all'inventario: " + b;
		}
	}

	/**
	 * remove an object from the ArrayList containing the items carried.
	 * @param o: String corresponding to the item.
	 * @return a string describing the action.
	 */
	public String removeObjCalled(String o) {
		
		for(Tool t : itemsHeld){
			if (t.getName().equals(o)){
				itemsHeld.remove(t);
				return this.getName() + " ha lasciato cadere: " + o;
			}
		}
		return null;
	}
	
	/**
	 * drop all items carried from the character and add them to the current room.
	 * @return a string describing the action.
	 */
	public String dropAllItems(){
		String all = "";
		for(Tool x: itemsHeld){
			this.currentRoom.addTool(x);
			all = all + x.getName() + ", " ;
		}
		itemsHeld.clear();
		if(all.length() > 3){
			all = all.substring(0, (all.length() -2));
		}
		return this.getName() + " dropped: " + all;//-----------------------------------------------------------------------------------------------
	}
	
	/**
	 * the target is attacked with the equipped weapon, if the target has no remaining life it is set as dead.
	 * @param target: character to be attacked
	 * @return a string describing the action.
	 */
	public String attackTarget(Character target){
		if(!this.isAlive){
			return "";
		}
		Random rand = new Random();
		int damage;
		
		int r = rand.nextInt(101);
		if(r < this.getWeapon().getPrecisionOutOf100()){
			damage = this.weapon.getDamage();
			if(this.getClass() == NpcBad.class)
				damage += ((NpcBad) this).getBonusAttack();
			target.setLifeRemaining(target.getLifeRemaining() - damage);
			if(target.getLifeRemaining() <= 0 && target.getClass() != Player.class){
				return target.getName() + " e' morto<BR>" + target.die();
			}
			if(target.getClass() == Player.class){
				MysticalAdventure.GAME.frame.decreaseLife(damage);   // decrease life in the green life bar
				return this.getName() + " attacca "+ target.getName();
			}
			return this.getName() + " attacca "+ target.getName() + "<BR>" + " vita restante " + target.getName() + ": " + target.getLifeRemaining() + "%";
		}
		else{
			return this.getName() + " attacca "+ target.getName() + "<BR>" + target.getName() + " mancato!";
		}
	}

}
