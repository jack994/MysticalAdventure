
public class NpcBad extends NPC {

	public NpcBad(String name, String description, int HP, boolean active, String speech) {
		super(name, description, HP, active, speech);
	}

	public String interact(Character pl) {
		if(isActive()){
			return attackTarget(pl);
		}
		else{
			return getSpeech();
		}
	}

}

