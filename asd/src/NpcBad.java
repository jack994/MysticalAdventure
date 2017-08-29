import java.io.Serializable;

/**
 * enemies class
 * @author giacomobenso
 */
public class NpcBad extends NPC implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private int bonusAttack;
	
	public NpcBad(String name, String description, int HP,int money, boolean active, String speech, int bonusAttack) {
		super(name, description, HP, money, active, speech);
		this.setBonusAttack(bonusAttack);
	}

	public int getBonusAttack() {
		return bonusAttack;
	}

	public void setBonusAttack(int bonusAttack) {
		this.bonusAttack = bonusAttack;
	}
	
	/**
	 * attack target is active, else talk
	 */
	public String interact(Character pl) { 
		if(isAlive){
		if(isActive()){
			return "<BR><BR>" + attackTarget(pl) + "<BR><BR>";
		}
		else{
			return "<BR><BR>" + getSpeech() + "<BR><BR>";
		}
		}
		else{
			return "";
		}
	}

	
}

