import java.io.Serializable;

public class NpcBad extends NPC implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private int bonusAttack;
	
	public NpcBad(String name, String description, int HP,int money, boolean active, String speech, int bonusAttack) {
		super(name, description, HP, money, active, speech);
		this.bonusAttack = bonusAttack;
	}

	public String interact(Character pl) {
		if(isActive()){
			return "<BR><BR>" + attackTarget(pl) + "<BR><BR>";
		}
		else{
			return "<BR><BR>" + getSpeech() + "<BR><BR>";
		}
	}

	public String attackTarget(Character target){
		this.getWeapon().setDamage(this.getWeapon().getDamage() + bonusAttack);
		return super.attackTarget(target);
	}
}

