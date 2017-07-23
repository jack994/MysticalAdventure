import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

public class Game implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public static GameWindow frame;
	private Command callerCommand;
	public Player currentPlayer;
	private Room StartRoom;
	private Map map;
	public static EStack THESTACK = new EStack(10);
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
		currentPlayer = new Player("Giacomo");
		currentPlayer.setCurrentRoom(StartRoom);
		callerCommand = new Command();
	}

	public void write() {
		String toAdd = "";
		if (!frame.getTextBox().getText().equals("")) {
			if (!frame.getPane().getText().equals("")) {
				toAdd = frame.getPane().getText().replace("</html>", "");
			}
			THESTACK.push(frame.getTextBox().getText());
			toAdd = toAdd + "<p><b>" + " > " + frame.getTextBox().getText() + "</b></p>";
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
		
		if(firstWord.equals("go")){
			return goRoom(command);
		}
		else if(firstWord.equals("help")){
			return printHelp(command);
		}
		else if(firstWord.equals("pick up") || firstWord.equals("take")){
			return pickTool(command);
		}
		else if(firstWord.equals("drop") || firstWord.equals("leave")){
			return dropTool(command);
		}
		else if(firstWord.equals("attack"))	{
			return attack(command);
		}
		else if(firstWord.equals("equip")){
			return equip(command);
		}
		else if(firstWord.equals("examine")){
			return examineObj(command);
		}
		else if(firstWord.equals("speak to") || firstWord.equals("talk to") 
				|| firstWord.equals("talk") || firstWord.equals("speak")){
			return speakToChar(command);
		}
		else if(currentPlayer.currentRoom.getName().equals("THE MEADOW") && 
				firstWord.equals("say")){
			return saySomething(command);
		}
		else if(firstWord.equals("light up")){
			return lightUp(command);
		}
		else return "I can't understand, write 'help' for the command list";

	}
	
	/**
	 * checks if the tool picked up creates a new commandWord
	 * @param t
	 */
	public void checkNewCommand(Tool t){ 
		if(t.getName().equals("matches")){
			callerCommand.addCommand("light up");
		}
	}
	
	public String lightUp(Command command){
		if(!command.hasSecondWord()){
			return "What do you want to light up? <BR>Write 'light up <object>'";
		}		
		if(command.getSecondWord().equals("torch") || command.getSecondWord().equals("the torch")){
			if(currentPlayer.getToolFromString("torch") != null){
				if(currentPlayer.getCurrentRoom().getName().equals("THE WOOD - East") ||
						currentPlayer.getCurrentRoom().getName().equals("THE TUNNEL")){
					return "you can see nothing! try to do it where there is some light";
				}else{
					if(currentPlayer.getWeapon().getName().equals("torch")){
						currentPlayer.getWeapon().setDamage(5); // damage increased by fire (also new functionality)
						currentPlayer.getWeapon().setDescription("the torch flames shine bright");
						return "you light up the torch";
					}
					return "torch needs to be equiped first"; 
				}
			}
			return "Yuo are not carrying any torch.";
		}
		return "you can't light up " + command.getSecondWord();
	}
	
	public String saySomething(Command command){
		
		if((command.getSecondWord().equals("moon") || command.getSecondWord().equals("the moon")) && 
				currentPlayer.getCurrentRoom().getName().equals("THE MEADOW")){ //treant riddle solved
			map.addPassage(3, 4, "south");
			currentPlayer.getCurrentRoom().getNPCNamed("treant").setSpeech("Good job with the riddle, now you can pass.");
			return "the treant slowly moves left, there is now a passage where he sat (south).";
		}else{
			return "nothing happens";
		}
	}

	public String goRoom(Command command) {
		
		String toReturn = "";
		
		if (currentPlayer.getCurrentRoom().hasDirection(command.getSecondWord())) {
			Room next = currentPlayer.getCurrentRoom().getDirectionRoom(command.getSecondWord());
			currentPlayer.setCurrentRoom(next);
			enlight(currentPlayer.getCurrentRoom()); //enlight the room if you are carying a torch			
			toReturn = currentPlayer.getCurrentRoom().getNameAndDescription();
			if(!(currentPlayer.getCurrentRoom().getNPCArray()).isEmpty()){
				for(NPC npc : currentPlayer.getCurrentRoom().getNPCArray()){
					toReturn += npc.interact(currentPlayer);
				}
			}
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
		if (!command.hasSecondWord()){
			return "what do you want to " + command.getFirstWord() + "?";
		}
		else if(command.getSecondWord().equals("money")){
			for(Item f : currentPlayer.getCurrentRoom().getItemsArray()){
				if(f.getClass() == Fixed.class){
					if(((Fixed) f).hasBeenOpened() && ((Fixed) f).getMoney() > 0){
						int mon;
						currentPlayer.addMoney(mon = ((Fixed) f).getMoney());
						((Fixed) f).removeAllMoney();
						return "money added: " + mon;
					}
				}
			}
			return "error money not found";
		}
		else {
			if ((temp = currentPlayer.getCurrentRoom().getItemNamed(command.getSecondWord())) != null) {
				if (temp.getClass() == Tool.class || temp.getClass() == Weapon.class) {
					currentPlayer.addObjCalled(temp.getName());
					frame.addItemToMenu((Tool) temp);
					currentPlayer.currentRoom.removeItemNamed(command.getSecondWord());
					checkNewCommand((Tool) temp);
					return "you picked up " + temp.getName() + beingattacked();
				} else {
					return "you can't pick up " + temp.getName() + " try the command \"examine " + temp.getName()
							+ "\"" + beingattacked();
				}
			}
			return "you don't see any " + command.getSecondWord() + beingattacked();
		}
	}

	public String dropTool(Command command) {
		Tool t = currentPlayer.getToolFromString(command.getSecondWord());
		if (!command.hasSecondWord())
			return "what do you want to " + command.getFirstWord() + "?" + beingattacked();
		else {
			if (t != null) {
				if(currentPlayer.getWeapon().getName().equals(t.getName())){
					currentPlayer.setWeapon(NN);
					frame.getWeaponLabel().setText(NN.getName());
				}	
				frame.removeItemFromMenu(t.getName());
				currentPlayer.currentRoom.addTool(t);
				return currentPlayer.removeObjCalled(command.getSecondWord()) + beingattacked();
			}
		}
		return "You are not carrying " + command.getSecondWord() + beingattacked();
	}

	public String examineObj(Command command) {
		if (!command.hasSecondWord() || command.getSecondWord().equals("room")) {
			return currentPlayer.currentRoom.getNameAndDescription() + beingattacked();
		}
		Item tmp;
		NPC npc;
		if (((tmp = currentPlayer.getCurrentRoom().getItemNamed(command.getSecondWord())) != null)
				|| ((tmp = currentPlayer.getToolFromString(command.getSecondWord())) != null) 
				) {
			if (tmp.getClass() == Tool.class || tmp.getClass() == Weapon.class) {
				Tool t = (Tool) tmp;
				return t.getDescription() + beingattacked();
			} else {
				Fixed t = (Fixed) tmp;
				t.open();
				if (!t.getToolList().isEmpty()) {
					return "items contained : " + t.getToolList() + "<BR>" + "money contained: " 
				+ t.getMoney() + beingattacked();
				} else {
					return "money contained: " + t.getMoney() + beingattacked();
				}
			}
		}
		else if((npc = currentPlayer.currentRoom.getNPCNamed(command.getSecondWord())) != null){
			return npc.getDescription();
		}
		return "I don't understand what you mean with " + command.getSecondWord() + beingattacked();
	}
	
	public String attack(Command command){
		if(!command.hasSecondWord()){
			return "Who do you want to attack? write 'attack <target>'";
		}
		NPC enemy;
		if((enemy = currentPlayer.currentRoom.getNPCNamed(command.getSecondWord())) != null){
			if(!enemy.isAlive()){
				return enemy.getName() + " is already dead";
			}
			else if(enemy.getClass() == NpcBad.class)
				return currentPlayer.attackTarget(enemy) + "<BR><BR>" + enemy.attackTarget(currentPlayer);
			else return "you cannot attack " + command.getSecondWord();
		}
		return "There is no " + command.getSecondWord();
	}

	public String equip(Command command){
		if(!command.hasSecondWord()){
			return "what do you want to equip?";
		}
		Tool t;
		if((t = currentPlayer.getToolFromString(command.getSecondWord()))!= null){
			if(t.getClass() == Weapon.class){
				frame.getWeaponLabel().setText(t.getName());
				if(currentPlayer.getWeapon().getName().equals("torch")){ // put out the torch
					currentPlayer.getWeapon().setDamage(3);
					currentPlayer.getWeapon().setDescription("a wooden torch, someone has already used it. "
							+ "it should still be possible to light it up");
				}
				return currentPlayer.equipWeapon((Weapon)t) + beingattacked();
			}
			else{
				return "it would be useless" + beingattacked();
			}
		}
		return "There is no " + command.getSecondWord() + beingattacked();
	}
	
	public String speakToChar(Command command){
		if(!command.hasSecondWord()){
			return "you: blablablabla. <BR>if you want to speak to someone write 'speak to <subject>'" + beingattacked();
		}
		NPC npc;
		if((npc = currentPlayer.getCurrentRoom().getNPCNamed(command.getSecondWord())) != null){
			if(npc.getClass() == NpcGood.class){
				if(npc.getName().equals("druid")){
					currentPlayer.getCurrentRoom().removeNpcNamed("druid");
				}
				return npc.getSpeech() + beingattacked();
			}
			else{
				return npc.getSpeech() + "<BR>" + npc.attackTarget(currentPlayer);
			}
		}
		return "you can't speak to " + command.getSecondWord() + beingattacked();
	}
	
	public String printHelp(Command command) {

			return "<b>Your commands are:</b> <i>" + command.listCommands() + "</i><BR>" + 
			"commands can be combined with other words or objects (usually charachters or items in the room),"
			+ " some examples are : <b>'go north', 'examine', 'examine chest', 'pick up key', 'attack goblin',"
			+ " 'say hello'.</b>";
		
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
	
	public String beingattacked(){
		for(NPC attacker : currentPlayer.getCurrentRoom().getNPCArray()){
			if((attacker.getClass() == NpcBad.class) && attacker.isActive() && attacker.isAlive()){
				return "<BR><BR>" + attacker.attackTarget(currentPlayer);
			}
			else{
				return "";
			}
		}
		return "";
	}
	
	public void enlight(Room currentR){
		
		if(currentPlayer.getWeapon().getName().equals("torch") && currentPlayer.getWeapon().getDamage() == 5){
			if(currentR.getName().equals("THE WOOD - East")){
				currentR.setDark(false);
				currentR.setDescription("the trees in this part of the wood are thicker"
						+ " but the light of your torch heps you see better");
			}
			else if(currentR.getName().equals("THE TUNNEL")){
				currentR.setDark(false);
				currentPlayer.getCurrentRoom().setDescription("The light of your torch enlights the tunnel");
				map.addPassage(7, 10, "east");
				map.addPassage(10, 7, "west");
			}
		}
		else{
			if(currentR.getName().equals("THE WOOD - East")){
				currentR.setDark(true);
				currentR.setDescription("the trees in this part of the wood are thicker and you struggle"
						+ " to see anything in this area");
			}
			else if(currentR.getName().equals("THE TUNNEL")){
				currentR.setDark(true);
				currentPlayer.getCurrentRoom().setDescription("You can see nothing but the entrance"
						+ " of the tunnel behind you. it's really dark");
			}
		}
	
	}
	
}
