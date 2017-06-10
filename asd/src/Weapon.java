
public class Weapon extends Tools {
	
	private int damage;
	private float precision;

	public Weapon(String name, String description, int value, int damage, float incMiss) {
		super(name, description, value);
		this.damage = damage;
		precision = incMiss;
	}

	public float getPrecision() {
		return precision;
	}
	
	public float getPrecisionOutOf100() {
		return precision * 100;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

}
