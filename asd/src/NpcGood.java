import java.io.Serializable;

public class NpcGood extends NPC implements Serializable{
	
	private static final long serialVersionUID = 1L;

	public NpcGood(String name, String description, int HP,int money, boolean active, String speech) {
		super(name, description, HP, money, active, speech);
	}

	public String interact(Character pl) {
		if(isActive() && isFirstTimeMet()){
			return "<BR><BR>" + getSpeech() + "<BR><BR>";
		}
		return "";
	}

}
