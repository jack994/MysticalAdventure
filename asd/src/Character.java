
import java.util.ArrayList;
import java.util.Random;

public abstract class Character {

	protected String name;
	protected boolean isAlive;
	protected ArrayList<Tools> itemsHeld;
	protected Room currentRoom;
	protected int money;
	protected int HP;
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

	public abstract String die();

	public double getMoneyAmount() {
		return money;
	}

	public void addMoney(int money) {
		this.money += money;
	}

	public Room getCurrentRoom() {
		return currentRoom;
	}

	public void setCurrentRoom(Room room) {
		currentRoom = room;
	}

	public abstract String getItems();

	public void addObj(Tools o) {

		itemsHeld.add(o);
	}

	public Tools getToolFromString(String s) {
		for (Tools x : itemsHeld) {
			if (x.getName().equals(s)) {
				return x;
			}
		}
		return null;
	}

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

	
	public void setName(String name) {
		this.name = name;
	}

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
	public void setLifeRemaining(int life){
		this.lifeRemaining = life;
	}
	
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
			return target.getName() + " remaining life: " + target.getLifeRemaining() + "%";
		}
		else{
			return this.getName() + "Misses!";
		}
	}

	public void setHP(int hP) {
		HP = hP;
	}

	public Weapon getWeapon() {
		return weapon;
	}
	
	public void setWeapon(Weapon weapon){
		this.weapon = weapon;
	}

}
