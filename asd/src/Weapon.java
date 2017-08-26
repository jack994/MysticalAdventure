import java.io.Serializable;

/**
 * a tool with damage and precision, it can be equipped
 * @author giacomobenso
 */
public class Weapon extends Tool implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private int damage;
	private float precision; // out of 1

	public Weapon(String name, String description, int value, int damage, float incMiss) {
		super(name, description, value);
		this.damage = damage;
		precision = incMiss;
	}

	public float getPrecision() {
		return precision;
	}
	
	public String getDescription(){
		return super.getDescription() + "<BR>Damage: " + this.getDamage()
		+ " - Precision: " + this.getPrecisionOutOf100() + "%";
	}
	
	public int getPrecisionOutOf100() {
		return (int) (precision * 100);
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

}
