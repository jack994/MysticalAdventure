import java.io.Serializable;

public class NpcGood extends NPC implements Serializable{

	public NpcGood(String name, String description, int HP,int money, boolean active, String speech) {
		super(name, description, HP, money, active, speech);
	}

	public String interact(Character pl) {
		if(isActive()){
			return getSpeech();
		}
		return "";
	}

}
