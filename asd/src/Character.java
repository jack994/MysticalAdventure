
import java.util.ArrayList;
import java.util.Random;

public abstract class Character {

	protected String name;
	protected boolean isAlive;
	protected ArrayList<Tools> itemsHeld;
	protected Room currentRoom;
	protected int money;
	protected int HP; // initial life
	protected int lifeRemaining;
	protected Weapon weapon;

	public Character(String name, int HP) {
		itemsHeld = new ArrayList<Tools>();
		isAlive = true;
		this.name = name;
		this.HP = HP;
		lifeRemaining = 100;
		Weapon NN = new Weapon("none","no weapon",0,1,0.5f);
		weapon = NN;
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
	
	public double getMoneyAmount() {
		return money;
	}

	public Room getCurrentRoom() {
		return currentRoom;
	}
	
	public Weapon getWeapon() {
		return weapon;
	}
	
	//----------------setter methods-------------------//
	
	public void addMoney(int money) {
		this.money += money;
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
	public void addObj(Tools o) {
		itemsHeld.add(o);
	}

	/**
	 * @param s: string to be recognised
	 * @return the object in the arraylist corresponding to the string passed
	 */
	public Tools getToolFromString(String s) {
		for (Tools x : itemsHeld) {
			if (x.getName().equals(s)) {
				return x;
			}
		}
		return null;
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
			itemsHeld.add((Tools) t);
			b = o;
		}
	
		if (b.equals("")) {
			return null;
		} else {
			return this.getName() + " took " + b;
		}
	}

	/**
	 * remove an object from the ArrayList containing the items carried.
	 * @param o: String corresponding to the item.
	 * @return a string describing the action.
	 */
	public String removeObjCalled(String o) {
		String b = "";
		for (int i = 0; i < itemsHeld.size(); i++) {
			b = itemsHeld.get(i).getName();
			if (b.equals(o)) {
				itemsHeld.remove(i);
				break;
			}
		}
		if (b.equals("")) {
			return this.getName() + " doesn't have such a thing";
		} else {
			return this.getName() + " dropped " + b;
		}
	}
	
	/**
	 * drop all items carried from the character and add them to the current room.
	 * @return a string describing the action.
	 */
	public String dropAllItems(){
		String all = "";
		for(Tools x: itemsHeld){
			this.currentRoom.addTool(x);
			all = all + x.getName() + ", " ;
		}
		itemsHeld.clear();
		if(all.length() > 3){
			all = all.substring(0, (all.length() -3));
		}
		return this.getName() + " dropped: " + all; 
	}
	
	/**
	 * the target is attacked with the equipped weapon, if the target has no remaining life it is set as dead.
	 * @param target: character to be attacked
	 * @return a string describing the action.
	 */
	public String attackTarget(Character target){
		Random rand = new Random();
		int damage;
		
		int r = rand.nextInt(100);
		if(r < this.getWeapon().getPrecisionOutOf100()){
			damage = (100 * this.weapon.getDamage()) / target.getHP();
			target.setLifeRemaining(target.getLifeRemaining() - damage);
			if(target.getLifeRemaining() <= 0){
				return target.getName() + " is dead<BR>" + target.die();
			}
			return this.getName() + " attacks "+ target.getName() + "<BR>" + target.getName() + " remaining life: " + target.getLifeRemaining() + "%";
		}
		else{
			return this.getName() + " attacks "+ target.getName() + "<BR>" + this.getName() + " misses!";
		}
	}

}
