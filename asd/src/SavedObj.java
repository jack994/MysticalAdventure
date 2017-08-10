import java.io.Serializable;
import java.util.ArrayList;

public class SavedObj implements Serializable{

	private static final long serialVersionUID = 1L;
	private Player currentPlayer;
	private Map map;
	private ArrayList<String> commands;
	
	public SavedObj(){}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(Player currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
	}

	public ArrayList<String> getCommands() {
		return commands;
	}

	public void setCommands(ArrayList<String> commands) {
		this.commands = commands;
	}

	
}
