import java.io.Serializable;

/**
 * NPCs which are not enemies
 * @author giacomobenso
 */
public class NpcGood extends NPC implements Serializable{
	
	private static final long serialVersionUID = 1L;

	public NpcGood(String name, String description, int HP,int money, boolean active, String speech) {
		super(name, description, HP, money, active, speech);
	}

	/**
	 * get the speech if it's the first time you meet the character
	 */
	public String interact(Character pl) {
		if(isActive() && isFirstTimeMet()){
			return "<BR><BR>" + getSpeech() + "<BR><BR>";
		}
		return "";
	}

}
