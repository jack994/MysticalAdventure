import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Game {
	private GameWindow frame;
	private Command callerCommand;
	private Player currentPlayer;
	private Room StartRoom;
	private Map map;
	public static EStack THESTACK = new EStack(10);;
	public final Weapon NN = new Weapon("none","no weapon",0,1,0.5f);

	public Game() {
		frame = new GameWindow();
		frame.getTextBox().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				THESTACK.reset();
				write();
			}
		});
		map = new Map();
		StartRoom = map.createRoom();
		currentPlayer = new Player("");
		currentPlayer.setCurrentRoom(StartRoom);
	}

	public void write() {
		String toAdd = "";
		if (!frame.getTextBox().getText().equals("")) {
			if (!frame.getPane().getText().equals("")) {
				toAdd = frame.getPane().getText().replace("</html>", "");
			}
			THESTACK.push(frame.getTextBox().getText());
			toAdd = toAdd + "<p><b>" + " > " + frame.getTextBox().getText() + "</b></p>";
			callerCommand = new Command();
			String[] a = callerCommand.contanisInstruction(frame.getTextBox().getText().toLowerCase());
			Command command2 = new Command(a[0], a[1].trim());
			toAdd = toAdd + "<p>" + processCommand(command2) + "</p>";
			frame.getTextBox().setText("");
			toAdd = toAdd.replace("</body>", "");
			toAdd = toAdd + "</body></html>";
			toAdd = toAdd.replaceAll("<p>", "<p style=font-size:13px>");
			frame.getPane().setText(toAdd);
		}
	}

	public String processCommand(Command command) {
		String firstWord = command.getFirstWord();

		switch (firstWord) {
		case "go":
			return goRoom(command);
		case "help": // tofinish///////////
			return printHelp(command);
		case "pick up":
			return pickTool(command);
		case "drop":
			return dropTool(command);
		case "examine":
			return examineObj(command);
		case "attack":
			return attack(command);
		case "equip":
			return equip(command);
		default:
			return "I can't understand what you mean, write 'help' for the command list";
		}

	}

	public String goRoom(Command command) {
		String toReturn = "";

		if (currentPlayer.getCurrentRoom().hasDirection(command.getSecondWord())) {
			Room next = currentPlayer.getCurrentRoom().getDirectionRoom(command.getSecondWord());
			currentPlayer.setCurrentRoom(next);
			toReturn = currentPlayer.getCurrentRoom().getNameAndDescription();
		} else {
			toReturn = "you can't go in that direction!";
		}
		return toReturn;
	}
	
	public String pickTool(Command command) {
		if(frame.BagFull()){
			return "Your bag is full";
		}
		Item temp;
		if (!command.hasSecondWord())
			return "what do you want to " + command.getFirstWord() + "?";
		else {
			if ((temp = currentPlayer.getCurrentRoom().getItemNamed(command.getSecondWord())) != null) {
				if (temp.getClass() == Tools.class || temp.getClass() == Weapon.class) {
					currentPlayer.addObjCalled(temp.getName());
					frame.addItemToMenu((Tools) temp);
					currentPlayer.currentRoom.removeItemNamed(command.getSecondWord());
					return "you picked up " + temp.getName();
				} else {
					return "you can't pick up " + temp.getName() + " try the command \"examine " + temp.getName()
							+ "\"";
				}
			}
			return "you don't see any " + command.getSecondWord();
		}
	}

	public String dropTool(Command command) {
		Tools t = currentPlayer.getToolFromString(command.getSecondWord());
		if (!command.hasSecondWord())
			return "what do you want to " + command.getFirstWord() + "?";
		else {
			if (t != null) {
				if(currentPlayer.getWeapon().getName().equals(t.getName())){
					currentPlayer.setWeapon(NN);
					frame.getWeaponLabel().setText(NN.getName());
				}	
				frame.removeItemFromMenu(t.getName());
				currentPlayer.currentRoom.addTool(t);
				return currentPlayer.removeObjCalled(command.getSecondWord());
			}
		}
		return "You are not carrying " + command.getSecondWord();
	}

	public String examineObj(Command command) {
		if (!command.hasSecondWord() || command.getSecondWord().equals("room")) {
			return currentPlayer.currentRoom.getNameAndDescription();
		}
		Item tmp;
		if (((tmp = currentPlayer.getCurrentRoom().getItemNamed(command.getSecondWord())) != null)
				|| ((tmp = currentPlayer.getToolFromString(command.getSecondWord())) != null) 
				) {
			if (tmp.getClass() == Tools.class || tmp.getClass() == Weapon.class) {
				Tools t = (Tools) tmp;
				return t.getDescription();
			} else {
				Fixed t = (Fixed) tmp;
				t.open();
				if (!t.getToolList().isEmpty()) {
					return "items contained : " + t.getToolList() + "<BR>" + "money contained: " + t.getMoney();
				} else {
					return "money contained: " + t.getMoney();
				}
			}
		}
		return "I don't understand what you mean with " + command.getSecondWord();
	}
	
	public String attack(Command command){
		if(!command.hasSecondWord()){
			return "Who do you want to attack?";
		}
		NPcBad enemy;
		if((enemy = currentPlayer.currentRoom.getNPCNamed(command.getSecondWord())) != null){
			if(!enemy.isAlive()){
				return enemy.getName() + " is already dead";
			}
			return currentPlayer.attackTarget(enemy);
		}
		return "There is no " + command.getSecondWord();
	}

	public String equip(Command command){
		if(!command.hasSecondWord()){
			return "what do you want to equip?";
		}
		Tools t;
		if((t = currentPlayer.getToolFromString(command.getSecondWord()))!= null){
			if(t.getClass() == Weapon.class){
				frame.getWeaponLabel().setText(t.getName());
				return currentPlayer.equipWeapon((Weapon)t);
			}
			else{
				return "you can only equip weapons";
			}
		}
		return "There is no " + command.getSecondWord();
	}
	
	
	public String printHelp(Command command) {
		if (command.hasSecondWord()) {
			return "the 'help' command needs to be used by its own!";
		} else {
			callerCommand = new Command();
			return "your commands are: " + command.listCommands();
		}
	}

	public void printWelcome() {
		String toChange = "<html>" + "<h1>welcome to THE MYSTICAL ADVENTURE</h1>" + "<p>"
				+ currentPlayer.getCurrentRoom().getNameAndDescription() + "</p>" + "</html>";
		toChange = toChange.replaceAll("<p>", "<p style=font-size:13px>");
		frame.getPane().setText(toChange);

	}

	public void start() {
		printWelcome();
	}

	public static void main(String[] args) {

		Game g = new Game();
		g.start();
	}
}
