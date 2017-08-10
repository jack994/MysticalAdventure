import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.apache.commons.lang3.*;
import java.util.Random;

public class Game {

	public GameWindow frame;
	public Player currentPlayer;
	private Room StartRoom;
	private Map map;
	public static EStack THESTACK = new EStack(10);
	public final Weapon NN = new Weapon("none", "no weapon", 0, 1, 0.5f);
	private int correctDoor = 1;

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
	}
	
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
	
	public Room getStartRoom() {
		return StartRoom;
	}

	public void write() {
		String toAdd = "";
		if (!frame.getTextBox().getText().equals("")) {
				if (!frame.getPane().getText().equals("")) {
					toAdd = frame.getPane().getText().replace("</html>", "");
				}			
				THESTACK.push(frame.getTextBox().getText());
				toAdd = toAdd + "<p><b>" + " > " + frame.getTextBox().getText() + "</b></p>";
				String[] a = Command.contanisInstruction(frame.getTextBox().getText().toLowerCase());
				Command command2 = new Command(a[0], a[1].trim());
				toAdd = toAdd + "<p>" + processCommand(command2) + "</p>";
				frame.getTextBox().setText("");
				toAdd = toAdd.replace("</body>", "");
				toAdd = toAdd + "</body></html>";
				toAdd = toAdd.replaceAll("<p>", "<p style='font-size:13px'>");
				toAdd = toAdd.replaceAll("<table>", "<table style='border: 1px solid black; font-size: 15px;'>");
				toAdd = toAdd.replaceAll("<th>", "<th style='padding: 5px; border: 1px solid black; font-size: 15px;'>");
				toAdd = toAdd.replaceAll("<td>", "<td style='padding: 5px; border: 1px solid black; font-size: 15px;'>");
				frame.getPane().setText(toAdd);
				if(currentPlayer.getCurrentRoom().getName().equals("INCORRECT") || currentPlayer.getLifeRemaining() <=0){
					currentPlayer.die();
				}
		}
	}

	public String processCommand(Command command) {
		String firstWord = command.getFirstWord();

		if (firstWord.equals("go")) {
			return goRoom(command);
		} else if (firstWord.equals("help")) {
			return printHelp(command);
		} else if (firstWord.equals("pick up") || firstWord.equals("take")) {
			return pickTool(command);
		} else if (firstWord.equals("drop") || firstWord.equals("leave")) {
			return dropTool(command);
		} else if (firstWord.equals("attack")) {
			return attack(command);
		} else if (firstWord.equals("equip")) {
			return equip(command);
		} else if (firstWord.equals("examine")) {
			return examineObj(command);
		} else if (firstWord.equals("speak to") || firstWord.equals("talk to") || firstWord.equals("talk")
				|| firstWord.equals("speak")) {
			return speakToChar(command);
		} else if (firstWord.equals("say")) {
			return saySomething(command);
		} else if (firstWord.equals("light up")) {
			return lightUp(command);
		} else if (firstWord.equals("open")){
			return openDoor(command);
		} else if (firstWord.equals("use")){
			return useItem(command);
		} else if (firstWord.equals("buy")){
			return buyFromMerchant(command);
		} else
			return "You can't use this command, either it does not exist or you don't have the correct tools/items"
					+ " to use it.<BR>You can check your currently available commands writing 'help'.";
	}

	/**
	 * checks if the tool picked up creates a new commandWord
	 * 
	 * @param t
	 */
	public void checkNewCommand(Tool t) {
		if (t.getName().equals("matches")) {
			Command.addCommand("light up");
		}
		if (t.getName().equals("key")) {
			Command.addCommand("open");
		}
	}
	
	public String buyFromMerchant(Command command){
		Merchant merchant;
		if((merchant = currentPlayer.getCurrentRoom().getMerchant()) == null){
			return "there is no merchant in this area";
		}
		if(!command.hasSecondWord()){
			return "what do you want to buy? write 'buy -item-'";
		}
		Tool t;
		if((t = currentPlayer.buyToolFromMerchant(merchant, command.getSecondWord())) != null){
			currentPlayer.removeMoney(t.getValue() * merchant.getPriceModifier());
			merchant.removeObjCalled(t.getName());
			currentPlayer.addObj(t);
			frame.addItemToMenu(t);
			return "you bought " + command.getSecondWord();
		}
		else{
			return "you cannot buy " + command.getSecondWord();
		}
	}
	
	public String useItem(Command command){
		if(!command.hasSecondWord()){
			return "what do you want to use? write 'use -item-'";
		}
		if(command.getSecondWord().equals("potion") && currentPlayer.getToolFromString("potion") != null){
			int life;
			if((currentPlayer.getHP() - currentPlayer.getLifeRemaining()) > 50){
				currentPlayer.setLifeRemaining(life = currentPlayer.getLifeRemaining() + 50);
				GameWindow.greenLabelsCounter = life;
				frame.resetLifelabel();
				currentPlayer.removeObjCalled("potion");
				frame.removeItemFromMenu("potion");
				return "50 LP healed";
			}	
			else{
				int healed = currentPlayer.getHP() - currentPlayer.getLifeRemaining();
				currentPlayer.setLifeRemaining(life = currentPlayer.getHP());
				GameWindow.greenLabelsCounter = life;
				frame.resetLifelabel();
				currentPlayer.removeObjCalled("potion");
				frame.removeItemFromMenu("potion");
				return healed + " LP healed";
			}
		}
		return "You cannot use this item right now";
	}

	public String lightUp(Command command) {
		if (currentPlayer.getToolFromString("matches") == null) {
			return "you cannot light up anything without the appropriate tool" + beingattacked();
		}
		if (!command.hasSecondWord()) {
			return "What do you want to light up? <BR>Write 'light up -object-'"+ beingattacked();
		}
		if (currentPlayer.getWeapon().getName().equals("torch") && currentPlayer.getWeapon().getDamage() == 5) {
			return "your torch has already been lit"+ beingattacked();
		}
		if (command.getSecondWord().equals("torch") || command.getSecondWord().equals("the torch")) {
			if (currentPlayer.getToolFromString("torch") != null) {
				if (currentPlayer.getCurrentRoom().getName().equals("THE WOOD - East")
						|| currentPlayer.getCurrentRoom().getName().equals("THE TUNNEL")) {
					return "you can see nothing! try to do it where there is some light"+ beingattacked();
				} else {
					if (currentPlayer.getWeapon().getName().equals("torch")) {
						currentPlayer.getWeapon().setDamage(5); // damage
																// increased by
																// fire (also
																// new
																// functionality)
						currentPlayer.getWeapon().setDescription("the torch flames shine bright");
						return "you light up the torch"+ beingattacked();
					}
					return "torch needs to be equiped first"+ beingattacked();
				}
			}
			return "Yuo are not carrying any torch."+ beingattacked();
		}
		return "you can't light up " + command.getSecondWord()+ beingattacked();
	}

	public String openDoor(Command command) {
		Tool ky;
		if ((ky =currentPlayer.getToolFromString("key")) == null) {
			return "you cannot open anything without the appropriate key"+ beingattacked();
		}
		if (!command.hasSecondWord()) {
			return "What do you want to open? <BR>Write 'open -object-'"+ beingattacked();
		}
		if (currentPlayer.getCurrentRoom().getName().equals("THE WOOD - South")) {
			if (command.getSecondWord().equals("door") || command.getSecondWord().equals("the door"))
				return "which one of them? write 'open door -number-'";
			String[] tmp = command.getSecondWord().split(" ");
			if (tmp[0].equals("door") && StringUtils.isNumeric(tmp[1])) {
				if (Integer.parseInt(tmp[1]) < 8 && Integer.parseInt(tmp[1]) > 0) {
					if(Integer.parseInt(tmp[1]) == correctDoor){
						map.addPassage(11, 13, "south");
						map.addPassage(13, 11, "north");
					}
					else{
						map.addPassage(11, 12, "south");
					}
					frame.removeItemFromMenu(ky.getName());    //remove the key
					currentPlayer.getCurrentRoom().getItemNamed("door " + tmp[1]).setDescription("The Door is Open");
					currentPlayer.removeObjCalled(ky.getName());
					return "you unlocked the Door number " + tmp[1] + ", the key magically disappears."
							+ "<BR>the passage is now open (south)";
				} else
					return "that door does not exist";
			}
		}		
			if (command.getSecondWord().equals("door") || command.getSecondWord().equals("the door") 
					&& currentPlayer.getToolFromString("passepartout") != null){
				if (currentPlayer.getCurrentRoom().getName().equals("THE WOOD - Quiet area")) {
					currentPlayer.getCurrentRoom().getItemNamed("door").setDescription("The Door is Open");
					map.addPassage(15, 16, "west");
					map.addPassage(16, 15, "east");
					return "you opened the door, the passage is now open (west)";
				}
			}
					
		return "you can't open " + command.getSecondWord()+ beingattacked();
	}

	public String saySomething(Command command) {
		if ((command.getSecondWord().equals("moon") || command.getSecondWord().equals("the moon"))
				&& currentPlayer.getCurrentRoom().getName().equals("THE MEADOW")) { // riddle solved
			NPC treant;
			if(!(treant = currentPlayer.getCurrentRoom().getNPCNamed("treant")).getSpeech().equals("Good job"
					+ " with the riddle, now you can pass.")){
				map.addPassage(3, 4, "south");
				treant.setSpeech("Good job with the riddle, now you can pass.");
				return "the treant slowly moves left, there is now a passage where he sat (south).";
			}else{
				return "you already opened the passage";
			}
		}
		else if((command.getSecondWord().equals("7") || command.getSecondWord().equals("seven"))
				&& currentPlayer.getCurrentRoom().getName().equals("THE VALLEY")){
			currentPlayer.getCurrentRoom().setDescription("in the middle of this area there is a river"
					+ " and all around flowers grow luxuriant. east you can see a waterfall with a small entrance beside it. "
					+ "North, a dark entrance looks like a tunnel.");
				map.addPassage(6, 17, "east");
				map.addPassage(17, 6, "west");
				return "two of the trees over the waterfall move slightly and the flow of the waterfall canges,"
						+ " a passage (east) has been uncovered";
		}
		else {
			return "nothing happens"+ beingattacked();
		}
	}

	public String goRoom(Command command) {

		String toReturn = "";

		if (currentPlayer.getCurrentRoom().hasDirection(command.getSecondWord())) {
			Room next = currentPlayer.getCurrentRoom().getDirectionRoom(command.getSecondWord());
			currentPlayer.setCurrentRoom(next);			
			enlight(currentPlayer.getCurrentRoom()); // enlight the room if you
														// are carying a torch
			changeDoors(); // change results on the doors for lorwin's riddle
			toReturn = currentPlayer.getCurrentRoom().getNameAndDescription();
			if (!(currentPlayer.getCurrentRoom().getNPCArray()).isEmpty()) {
				for (NPC npc : currentPlayer.getCurrentRoom().getNPCArray()) {
					toReturn += npc.interact(currentPlayer);
				}
			}
		} else {
			toReturn = "you can't go in that direction!";
		}
		return toReturn;
	}

	public String pickTool(Command command) {
		if (frame.BagFull()) {
			return "Your bag is full";
		}
		Item temp;
		if (!command.hasSecondWord()) {
			return "what do you want to " + command.getFirstWord() + "?";
		} else if (command.getSecondWord().equals("money")) {
			for (Item f : currentPlayer.getCurrentRoom().getItemsArray()) {
				if (f.getClass() == Fixed.class) {
					if (((Fixed) f).hasBeenOpened() && ((Fixed) f).getMoney() > 0) {
						int mon;
						currentPlayer.addMoney(mon = ((Fixed) f).getMoney());
						((Fixed) f).removeAllMoney();
						return "money added: " + mon;
					}
				}
			}
			return "there is no money to take";
		} else {
			if ((temp = currentPlayer.getCurrentRoom().getItemNamed(command.getSecondWord())) != null) {
				if (temp.getClass() == Tool.class || temp.getClass() == Weapon.class) {
					currentPlayer.addObjCalled(temp.getName());
					frame.addItemToMenu((Tool) temp);
					currentPlayer.currentRoom.removeItemNamed(command.getSecondWord());
					checkNewCommand((Tool) temp);
					return "you picked up " + temp.getName() + beingattacked();
				} else {
					return "you can't pick up " + temp.getName() + " try the command 'examine " + temp.getName() + "'"
							+ beingattacked();
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
				String remove;
				if (currentPlayer.getWeapon().getName().equals(t.getName())) {
					currentPlayer.setWeapon(NN);
					frame.getWeaponLabel().setText(NN.getName());
				}
				frame.removeItemFromMenu(remove = t.getName());
				currentPlayer.removeObjCalled(command.getSecondWord());
				currentPlayer.currentRoom.addTool(t);
				if((remove.equals("key") || remove.equals("torch"))){
					Command.removeCommand(remove);
				}
				return "you dropped " + t.getName() + beingattacked();
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
		String ret = "";
		if (((tmp = currentPlayer.getCurrentRoom().getItemNamed(command.getSecondWord())) != null)
				|| ((tmp = currentPlayer.getToolFromString(command.getSecondWord())) != null)) {
			if (tmp.getClass() == Tool.class || tmp.getClass() == Weapon.class) {
				Tool t = (Tool) tmp;
				return t.getDescription() + beingattacked();
			} else {
				Fixed t = (Fixed) tmp;
				t.open();
				if (t.getDescription() != null) {
					ret += t.getDescription();
				}
				if (!t.getToolList().isEmpty()) {
					if (t.getDescription() != null)
						ret += "<BR><BR>";
					ret += "items contained : " + t.getToolList();
				}
				if (t.getMoney() != 0) {
					if (t.getDescription() != null && t.getToolList().isEmpty())
						ret += "<BR><BR>";
					else if (!t.getToolList().isEmpty())
						ret += "<BR>";
					ret += "money contained: " + t.getMoney();
				}
				if(t.getMoney() == 0 && t.getToolList().isEmpty() && t.getDescription() == null){
					ret = t.getName() + " is empty";
				}
				return ret + beingattacked();
			}
		} else if ((npc = currentPlayer.currentRoom.getNPCNamed(command.getSecondWord())) != null) {
			return npc.getDescription();
		}
		return "there is no " + command.getSecondWord() + beingattacked() + " to examine";
	}

	public String attack(Command command) {
		if (!command.hasSecondWord()) {
			return "Who do you want to attack? write 'attack -target-'";
		}
		NPC enemy;
		if ((enemy = currentPlayer.currentRoom.getNPCNamed(command.getSecondWord())) != null) {
			if (!enemy.isAlive()) {
				return enemy.getName() + " is already dead";
			} else if (enemy.getClass() == NpcBad.class){
					if(enemy.getName().equals("demogorgon") && currentPlayer.getWeapon().getName().equals("demon-slayer")){
						enemy.setLifeRemaining(0);
						return enemy.getName() + " start burning form the inside and dies."+ "<BR>" + enemy.die();
					}
				return currentPlayer.attackTarget(enemy) + "<BR><BR>" + enemy.attackTarget(currentPlayer);
			}
			else
				return "you cannot attack " + command.getSecondWord();
		}
		return "There is no " + command.getSecondWord();
	}

	public String equip(Command command) {
		if (!command.hasSecondWord()) {
			return "what do you want to equip?";
		}
		Tool t;
		if ((t = currentPlayer.getToolFromString(command.getSecondWord())) != null) {
			if (t.getClass() == Weapon.class) {
				frame.getWeaponLabel().setText(t.getName());
				if (currentPlayer.getWeapon().getName().equals("torch")) { // put
																			// out
																			// the
																			// torch
					currentPlayer.getWeapon().setDamage(3);
					currentPlayer.getWeapon().setDescription("a wooden torch, someone has already used it. "
							+ "it should still be possible to light it up");
				}
				return currentPlayer.equipWeapon((Weapon) t) + beingattacked();
			} else {
				return "it would be useless" + beingattacked();
			}
		}
		return "There is no " + command.getSecondWord() + beingattacked();
	}

	public String speakToChar(Command command) {
		if (!command.hasSecondWord()) {
			return "you: blablablabla. <BR>if you want to speak to someone write 'speak to <subject>'"
					+ beingattacked();
		}
		NPC npc;
		if ((npc = currentPlayer.getCurrentRoom().getNPCNamed(command.getSecondWord())) != null) {
			if (npc.getClass() == NpcGood.class) {
				if (npc.getName().equals("druid") && currentPlayer.getCurrentRoom().getName().equals("THE LIVING ROOM")){
					currentPlayer.getCurrentRoom().removeNpcNamed("druid");
				}
				else if (npc.getName().equals("lorwin")) {
					Ingredient ing;
					if((ing = npc.getIngredient("phoenix plum")) != null){
						npc.removeIngredient(ing);
						frame.addIngredientToMenu(ing);
						String speech = npc.getSpeech();
						npc.setSecondSpeech("Hello my little friend, You know, you never stop learning.");
						return speech + currentPlayer.addIngredient(ing);
					}
				}
				else if (npc.getName().equals("scared man") && 
						(currentPlayer.getToolFromString("Demogorgon tooth")!= null)) {
					npc.setSecondSpeech("Holy Guacamole you have defeated the demogorgon, I call tell it"
							+ " from the tooth you are carrying!<BR>here, take your prize, I will never be "
							+ "grateful enough.<BR>Now I must go, see you around.");
					Tool k = npc.getToolFromString("passepartout");
					npc.removeObjCalled("passepartout");
					currentPlayer.addObj(k);
					frame.addItemToMenu(k);
					checkNewCommand(k);
					currentPlayer.getCurrentRoom().removeNpcNamed("scared man");
					return npc.getSpeech() + "<BR><BR>" + k.getName() + " added to " + 
					currentPlayer.getName() + "'s inventory";
				}
				return npc.getSpeech() + beingattacked();
			} else {
				return npc.getSpeech() + beingattacked();
			}
		}
		return "you can't speak to " + command.getSecondWord() + beingattacked();
	}

	public String printHelp(Command command) {

		return "<b>Your commands are:</b> <i>" + command.listCommands() + "</i><BR>"
				+ "commands can be combined with other words or objects (usually charachters or items in the room),"
				+ " some examples are : <b>'go north', 'examine', 'examine chest', 'pick up key', 'attack goblin',"
				+ " 'say hello'.</b> <BR>P.S. : Commands can change during the course of the story";

	}

	public void printWelcome(boolean dead) {
		String toChange = "<html><body>";
		if(dead){
			toChange += "<p style='font-size: 40px'>YOU DIED!</p>";
		}
		toChange += "<h1>welcome to THE MYSTICAL ADVENTURE</h1>" + "<p>"
				+ currentPlayer.getCurrentRoom().getNameAndDescription() + "</p>" + "</html>";
		toChange = toChange.replaceAll("<p>", "<p style=font-size:13px>");
		frame.getPane().setText(toChange);

	}

	public void start() {
		printWelcome(false);
	}

	public String beingattacked() {
		for (NPC attacker : currentPlayer.getCurrentRoom().getNPCArray()) {
			if ((attacker.getClass() == NpcBad.class) && attacker.isActive() && attacker.isAlive()) {
				return "<BR><BR>" + attacker.attackTarget(currentPlayer);
			} else {
				return "";
			}
		}
		return "";
	}

	public void enlight(Room currentR) {

		if (currentPlayer.getWeapon().getName().equals("torch") && currentPlayer.getWeapon().getDamage() == 5) {
			if (currentR.getName().equals("THE WOOD - East")) {
				currentR.setDark(false);
				currentR.setDescription("the trees in this part of the wood are thicker"
						+ " but the light of your torch heps you see better");
			} else if (currentR.getName().equals("THE TUNNEL")) {
				currentR.setDark(false);
				currentPlayer.getCurrentRoom().setDescription("The light of your torch enlights the tunnel");
				map.addPassage(7, 10, "east");
				map.addPassage(10, 7, "west");
			}
		} else {
			if (currentR.getName().equals("THE WOOD - East")) {
				currentR.setDark(true);
				currentR.setDescription("the trees in this part of the wood are thicker and you struggle"
						+ " to see anything in this area");
			} else if (currentR.getName().equals("THE TUNNEL")) {
				currentR.setDark(true);
				currentPlayer.getCurrentRoom().setDescription(
						"You can see nothing but the entrance" + " of the tunnel behind you. it's really dark");
			}
		}

	}

	public void changeDoors() {
		if (currentPlayer.getCurrentRoom().getName().equals("THE WOOD - South")) {
			Random rand = new Random();
			int code = rand.nextInt(4);
			map.setLorwinCode(code);
			String desc = "The door is locked, on it someone wrote the number ";
			currentPlayer.getCurrentRoom().setDescription("This area is surrounded by rock walls, seven doors are on the south "
				+ "side of the area, above them a well defined engraving says: 'In the Lorwin code a "
				+ "valid codeword does not contain any digit more than once and "
				+ "cannot contain both 0 and 1 in the same codeword.<BR>"
				+ "How many possible strings of "+ map.getLorwinCodeLength() +" digits are there?'");
			

			for (int i = 1; i < 8; i++) {
				int num = rand.nextInt(100) + 10;
				boolean b = rand.nextBoolean();
				if (b) {
					((Fixed) currentPlayer.getCurrentRoom().getItemNamed("door " + i))
							.setDescription(desc + (map.getLorwinCodeSolution() + num));
				} else {
					((Fixed) currentPlayer.getCurrentRoom().getItemNamed("door " + i))
							.setDescription(desc + (map.getLorwinCodeSolution() - num));
				}
			}
			int n = rand.nextInt(7) + 1;
			correctDoor = n;
			((Fixed) currentPlayer.getCurrentRoom().getItemNamed("door " + n))
					.setDescription(desc + map.getLorwinCodeSolution());
		}
	}
}
