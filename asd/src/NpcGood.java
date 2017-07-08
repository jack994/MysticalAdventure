

public class NpcGood extends NPC {

	public NpcGood(String name, String description, int HP, boolean active, String speech) {
		super(name, description, HP, active, speech);
	}

	public String interact(Character pl) {
		if(isActive()){
			return getSpeech();
		}
		return "";
	}

}
