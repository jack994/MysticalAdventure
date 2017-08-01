import java.io.Serializable;

public class SavedObj implements Serializable{

	private static final long serialVersionUID = 1L;
	private Player currentPlayer;
	private Map map;
	
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
	
}
