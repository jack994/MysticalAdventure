import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.apache.commons.lang3.*;
import java.util.Random;
import javax.swing.*;

/**
 * the core class of the game, where all the instructions are processed 
 * and all the actions are handled.
 * @author giacomobenso
 */
public class Game {

	public GameWindow frame; //GUI
	public Player currentPlayer;
	private Room StartRoom;
	private Map map; //the map containing all the elements of the game
	public static EStack THESTACK = new EStack(10); //stack needed for the commands history
	public final Weapon NN = new Weapon("none", "no weapon", 0, 1, 0.95f); //no weapon
	private int correctDoor = 1;

	public Game() { // constructor
		frame = new GameWindow();
		frame.getTextBox().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				THESTACK.reset();
				write();
			}
		});
		map = new Map();
		StartRoom = map.createRoom();
		currentPlayer = new Player("Eldor");
		currentPlayer.setCurrentRoom(StartRoom);
	}

	//--------------------------------------------------------------------------------------
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
	//--------------------------------------------------------------------------------------
	
	/**
	 * method called with the action-listener of the text-box, it processes the text in the text-box
	 * as command giving a 'reply' to it in HTML which is placed in the text-area.
	 */
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
				if(currentPlayer.getCurrentRoom().getName().equals("INCORRECT") || 
				currentPlayer.getLifeRemaining() <=0){ // incorrect room at lorwin's or 0 life remaining means you are dead
					currentPlayer.die();
				}
		}
	}
	
	/**
	 * method used to process the given command, it has a method for each 'firstword' of the command
	 * @param command: the 'String' passed
	 * @return the string to be returned and placed in the text-box
	 */
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
		} else if (firstWord.equals("talk") || firstWord.equals("speak")) {
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
		}else if (firstWord.equals("map")){
			return openMap(command);
		}else if (firstWord.equals("eat")){
			return eat(command);
		}else
			return "You can't use this command, either it does not exist or you don't have the correct tools/items"
					+ " to use it.<BR>You can check your currently available commands writing 'help'.";
	}

	/**
	 * checks if the tool picked up creates a new commandWord
	 * @param t : tool to be checked
	 */
	public void checkNewCommand(Tool t) {
		if (t.getName().equals("matches")) {
			Command.addCommand("light up");
		}
		if (t.getName().equals("key")) {
			Command.addCommand("open");
		}
		if(t.getName().equals("map piece") && (currentPlayer.getToolFromString("map piece") == null)){
			Command.addCommand("map");
		}
	}
	
	public String eat(Command command){
		if(!command.hasSecondWord()){
			return "what do you want to eat? type 'eat -item-'";
		}
		if(currentPlayer.getToolFromString(command.getSecondWord()) == null){
			return "you cannot eat " + command.getSecondWord();
		}
		if(currentPlayer.getToolFromString(command.getSecondWord()).getName().equals("raspberry") || 
				currentPlayer.getToolFromString(command.getSecondWord()).getName().equals("carrot") ||
				currentPlayer.getToolFromString(command.getSecondWord()).getName().equals("apple")){
			currentPlayer.removeObjCalled(command.getSecondWord());
			frame.removeItemFromMenu(command.getSecondWord());
			return "you eat " + command.getSecondWord() + ", the perfect snack for a big boy like you!";
		}
			return "you cannot eat " + command.getSecondWord();
	}
	
	/**
	 * open the map in a new window, the image opened depends on how many pieces you gathered
	 * @param command
	 * @return the string to be returned and placed in the text-box
	 */
	public String openMap(Command command){
		JFrame nf = new JFrame();
		
		if(frame.getMapPieces() == 1){
			nf.getContentPane().add(new JLabel(new ImageIcon("./lib/mapPieces/piece1.jpg")));
			nf.setSize(new Dimension(260, 460));
		}
		else if(frame.getMapPieces() == 2){
			nf.getContentPane().add(new JLabel(new ImageIcon("./lib/mapPieces/piece2.jpg")));
			nf.setSize(new Dimension(450, 400));
		}
		else if(frame.getMapPieces() == 3){
			nf.getContentPane().add(new JLabel(new ImageIcon("./lib/mapPieces/piece3.jpg")));
			nf.setSize(new Dimension(600, 450));
		}
		nf.setVisible(true);
		
		return "map opened";
	}
	
	/**
	 * buy a tool from the merchant, the item is removed from him and added to your inventory
	 * @param command
	 * @return the string to be returned and placed in the text-box
	 */
	public String buyFromMerchant(Command command){
		Merchant merchant;
		if((merchant = currentPlayer.getCurrentRoom().getMerchant()) == null){
			return "there is no merchant in this area";
		}
		if(!command.hasSecondWord()){
			return "what do you want to buy? type 'buy -item-'";
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
	
	/**
	 * use an item you are carrying
	 * @param command
	 * @return the string to be returned and placed in the text-box
	 */
	public String useItem(Command command){
		if(!command.hasSecondWord()){
			return "what do you want to use? write 'use -item-'";
		}
		//if the item used is a potion:
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

	/**
	 * light up the torch, to use this command you need to have the torch equipped and you need to have picked up 
	 * the matches.
	 * @param command
	 * @return the string to be returned and placed in the text-box
	 */
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
						currentPlayer.getWeapon().setDescription("the torch flames glow");
						return "you light up the torch"+ beingattacked();
					}
					return "torch needs to be equiped first"+ beingattacked();
				}
			}
			return "Yuo are not carrying any torch."+ beingattacked();
		}
		return "you can't light up " + command.getSecondWord()+ beingattacked();
	}

	/**
	 * open the door, to use this command you need a key or a passepartout.
	 * @param command
	 * @return the string to be returned and placed in the text-box
	 */
	public String openDoor(Command command) {
		Tool ky;
		if ((ky =currentPlayer.getToolFromString("key")) == null && 
				(ky =currentPlayer.getToolFromString("passepartout")) == null) {
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
						map.addPassage(11, 13, "south"); //create the passage
						map.addPassage(13, 11, "north"); //create the passage
					}
					else{
						map.addPassage(11, 12, "south"); //create the passage
					}
					frame.removeItemFromMenu(ky.getName()); //remove the key
					currentPlayer.getCurrentRoom().getItemNamed("door " + tmp[1]).setDescription("The Door is Open");
					currentPlayer.removeObjCalled(ky.getName());
					return "you unlocked the Door number " + tmp[1] + ", the key magically disappears."
							+ "<BR>the passage is now open (south)";
				} else
					return "that door does not exist";
			}
		}		
			if (command.getSecondWord().equals("door") && currentPlayer.getToolFromString("passepartout") != null){
				if (currentPlayer.getCurrentRoom().getName().equals("THE WOOD - Quiet area")) {
					currentPlayer.getCurrentRoom().getItemNamed("door").setDescription("The Door is Open");
					map.addPassage(15, 16, "west"); //create the passage
					map.addPassage(16, 15, "east"); //create the passage
					return "you opened the door, the passage is now open (west)";
				}
				else if (currentPlayer.getCurrentRoom().getName().equals("THE CAVE BEYOND THE WATERFALL")) {
					currentPlayer.getCurrentRoom().getItemNamed("door").setDescription("The Door is Open");
					map.addPassage(17, 19, "east"); //create the passage
					map.addPassage(19, 17, "west"); //create the passage
					return "you opened the door, the passage is now open (east)";
				}
			}
					
		return "you can't open " + command.getSecondWord()+ beingattacked();
	}

	/**
	 * talk loud, you do not speak to someone, just pronounce the words loud
	 * @param command
	 * @return the string to be returned and placed in the text-box
	 */
	public String saySomething(Command command) {
		NPC npc;
		if ((command.getSecondWord().equals("moon"))
				&& currentPlayer.getCurrentRoom().getName().equals("THE MEADOW")) { // riddle solved
			NPC treant;
			if(!(treant = currentPlayer.getCurrentRoom().getNPCNamed("treant")).getSpeech().equals("Good job"
					+ " with the riddle, now you can pass.")){
				map.addPassage(3, 4, "south"); //create the passage 
				treant.setSpeech("Good job with the riddle, you can now pass.");
				return "the treant slowly moves left, there is now a passage where he sat (south).";
			}else{
				return "you already opened the passage";
			}
		}
		else if((npc = currentPlayer.getCurrentRoom().getNPCNamed("demogorgon")) != null){
			if(!npc.isAlive){
				if(command.getSecondWord().equals("15546")){
					currentPlayer.setCurrentRoom(map.getRoom(20)); //go to the oblivion
					return "you start feeling dizzy and you suddenly lose senses...<BR><BR>" + 
							currentPlayer.getCurrentRoom().getNameAndDescription();
				}
				else{
					return "nothing happens";
				}
			}else {
				return "nothing happens"+ beingattacked();
			}
		}
		else if((command.getSecondWord().equals("7") || command.getSecondWord().equals("seven"))
				&& currentPlayer.getCurrentRoom().getName().equals("THE VALLEY")){
			currentPlayer.getCurrentRoom().setDescription("<i>in the middle of this area there is a river"
					+ " and all around flowers grow luxuriant. east you can see a waterfall with a small entrance beside it. "
					+ "North, a dark entrance looks like a tunnel.</i>");
				map.addPassage(6, 17, "east"); //create passage
				map.addPassage(17, 6, "west"); //create passage
				return "two of the trees over the waterfall move slightly and the flow of the waterfall canges,"
						+ " a passage (east) has been uncovered" + beingattacked();
		}
		else {
			return "nothing happens"+ beingattacked();
		}
	}

	/**
	 * move from the current room to the next one
	 * @param command
	 * @return the string to be returned and placed in the text-box
	 */
	public String goRoom(Command command) {

		String toReturn = "";

		if (currentPlayer.getCurrentRoom().hasDirection(command.getSecondWord())) { //if the direction exists
			Room next = currentPlayer.getCurrentRoom().getDirectionRoom(command.getSecondWord());
			currentPlayer.setCurrentRoom(next);			
			enlight(currentPlayer.getCurrentRoom()); // enlight the room if you are carying a torch
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

	/**
	 * take the tool and add it to the bag, it could be a weapon, a normal tool or some money
	 * @param command
	 * @return the string to be returned and placed in the text-box
	 */
	public String pickTool(Command command) {
		Item temp;
		if (!command.hasSecondWord()) {
			return "what do you want to " + command.getFirstWord() + "?";
		} else if (command.getSecondWord().equals("money")) {
			int mon;
			if((mon = currentPlayer.getCurrentRoom().getMoney()) > 0){
				currentPlayer.getCurrentRoom().removeMoney(mon);
				currentPlayer.addMoney(mon);
				return "money added: " + mon + beingattacked();
			}
			for (Item f : currentPlayer.getCurrentRoom().getItemsArray()) {
				if (f.getClass() == Fixed.class) {
					if (((Fixed) f).hasBeenOpened() && ((Fixed) f).getMoney() > 0) {
						currentPlayer.addMoney(mon = ((Fixed) f).getMoney());
						((Fixed) f).removeAllMoney();
						return "money added: " + mon + beingattacked();
					}
				}
			}
			return "there is no money to take";
		} else {
			//if you take any of the two flowers in the cave the other one disappears
			if ((temp = currentPlayer.getCurrentRoom().getItemNamed(command.getSecondWord())) != null) { 
				if (frame.BagFull() && (temp.getClass() == Tool.class)) {
					return "Your bag is full";
				}
				if (temp.getClass() == Tool.class || temp.getClass() == Weapon.class) {
					if(temp.getName().equals("hibiscus flower")){
						currentPlayer.getCurrentRoom().removeItemNamed("belladonna flower");
					}
					checkNewCommand((Tool) temp);
					currentPlayer.addObjCalled(temp.getName());
					frame.addItemToMenu((Tool) temp);
					currentPlayer.currentRoom.removeItemNamed(temp.getName());
					return "you picked up " + temp.getName() + beingattacked();
				}
				else if(temp.getClass() == Ingredient.class){
					if(temp.getName().equals("belladonna flower")){
					currentPlayer.getCurrentRoom().removeItemNamed("hibiscus flower");
				}
					frame.addIngredientToMenu((Ingredient) temp); // add it to the ingredients 
					currentPlayer.addObjCalled(temp.getName());
					currentPlayer.currentRoom.removeItemNamed(temp.getName());
					if(currentPlayer.ingredientsFound() == 3){
						NpcGood druid = new NpcGood("druid","a druid with enormous horns", 999, 150, true, currentPlayer.getName()
								+ "!! you found all the ingredients! I always believed in you!<BR>there is something you have to know though,"
								+ " you are not a real person, I summoned you to fulfil my mission to save the wood.<BR>And there is more, "
								+ "my potion to heal the wood needs a fourth ingredient, which is the soul of the summoning who found the three"
								+ " ingredients.<BR>I am sorry...");
						map.getRoom(1).addnpcs(druid); //add the druid to his house
					}
					else if(temp.getName().equals("dremora hearth")){ //if you take the heart of the dremora
						currentPlayer.setCurrentRoom(map.getRoom(14)); //get back to the wood
						return "you start feeling dizzy and you suddenly lose senses...<BR><BR>" + 
								currentPlayer.getCurrentRoom().getNameAndDescription() + beingattacked();
					}
					return "you picked up " + temp.getName() + beingattacked();
				}
				else {
					return "you can't pick up " + temp.getName() + " try the command 'examine " + temp.getName() + "'"
							+ beingattacked();
				}
			}
			return "you don't see any " + command.getSecondWord() + beingattacked();
		}
	}

	/**
	 * drop a tool, remove it from your inventory and drop it in the current room
	 * @param command
	 * @return the string to be returned and placed in the text-box
	 */
	public String dropTool(Command command) {
		Tool t = currentPlayer.getToolFromString(command.getSecondWord());
		if (!command.hasSecondWord())
			return "what do you want to " + command.getFirstWord() + "?" + beingattacked();
		else {
			if (t != null) {
				String remove;
				if (currentPlayer.getWeapon().getName().equals(t.getName())) { //if weapon removed
					currentPlayer.setWeapon(NN);
					frame.getWeaponLabel().setText(NN.getName());
				}
				frame.removeItemFromMenu(remove = t.getName());
				currentPlayer.removeObjCalled(command.getSecondWord());
				currentPlayer.currentRoom.addTool(t);
				if(remove.equals("key") || remove.equals("torch") || 
						(remove.equals("map piece") && (currentPlayer.getToolFromString("map piece") == null))){ 
					//remove the appropriate command if item dropped
					Command.removeCommand(remove);
				}
				return "you dropped " + t.getName() + beingattacked();
			}
		}
		return "You are not carrying " + command.getSecondWord() + beingattacked();
	}

	/**
	 * examine an item to get the description and/or the tools/money contained
	 * @param command
	 * @return the string to be returned and placed in the text-box
	 */
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

	/**
	 * attack an enemy (you can only attack NpcBad)
	 * @param command
	 * @return the string to be returned and placed in the text-box
	 */
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
						enemy.setDescription("The Demogorgon has a collar with some numbers written on it: 6, 66, 426, 2586");
						return "your sword shines, "+ enemy.getName() + " start burning form the inside and dies."+
						"<BR>" + ((NpcBad)enemy).die();
					}
					else if(enemy.getName().equals("lord dremora") && currentPlayer.getWeapon().getName().equals("demon-slayer")){
						enemy.setLifeRemaining(enemy.getLifeRemaining() - 100);
						if(enemy.getLifeRemaining() < 150){
							return "your sword shines, "+ enemy.getName() + " start burning form the inside and dies."+ "<BR>" + enemy.die();
						}
					}
				return currentPlayer.attackTarget((NpcBad)enemy) + "<BR><BR>" + ((NpcBad)enemy).attackTarget(currentPlayer);
			}
			else
				return "you cannot attack " + command.getSecondWord();
		}
		return "There is no " + command.getSecondWord();
	}

	/**
	 * equip a weapon, only weapons can be equipped. you need to have the weapon in your inventory
	 * @param command
	 * @return the string to be returned and placed in the text-box
	 */
	public String equip(Command command) {
		if (!command.hasSecondWord()) {
			return "what do you want to equip?";
		}
		Tool t;
		if ((t = currentPlayer.getToolFromString(command.getSecondWord())) != null) {
			if (t.getClass() == Weapon.class && !currentPlayer.getWeapon().getName().equals(command.getSecondWord())){
				frame.getWeaponLabel().setText(t.getName());
				if (currentPlayer.getWeapon().getName().equals("torch")) { // put out the torch
					currentPlayer.getWeapon().setDamage(3);
					enlight(currentPlayer.getCurrentRoom());
					currentPlayer.getWeapon().setDescription("a wooden torch, someone has already used it. "
							+ "it should still be possible to light it up");
					return "you put out the torch.<BR>" + currentPlayer.equipWeapon((Weapon) t) + beingattacked();
				}
				return currentPlayer.equipWeapon((Weapon) t) + beingattacked();
			} else {
				return "it would be useless" + beingattacked();
			}
		}
		return "There is no " + command.getSecondWord() + beingattacked();
	}

	/**
	 * speak to a character, it behaves differently depending on its class 
	 * @param command
	 * @return
	 */
	public String speakToChar(Command command) {
		if (!command.hasSecondWord()) {
			return "you: blablablabla. <BR>if you want to speak to someone write 'speak to <subject>'"
					+ beingattacked();
		}
		NPC npc;
		if ((npc = currentPlayer.getCurrentRoom().getNPCNamed(command.getSecondWord())) != null) {
			if (npc.getClass() == NpcGood.class) {
				if (npc.getName().equals("druid") && currentPlayer.getCurrentRoom().getName().equals("THE LIVING ROOM")
						&& npc.getHP() == 1000){ // 1000 hp means first time you meet the druid (beginning)
					currentPlayer.getCurrentRoom().removeNpcNamed("druid");
				}
				else if(npc.getName().equals("druid") && currentPlayer.getCurrentRoom().getName().equals("THE LIVING ROOM")
						&& npc.getHP() == 999){ // 999 hp means second time you meet the druid (end)
					return npc.getSpeech() + "<BR><BR><p style='font-size: 25px;'>YOU DIED!</p>" + createEndCode();
				}
				else if (npc.getName().equals("lorwin")) { //if it's lorwin
					Tool ing;
					if((ing = npc.getToolFromString("phoenix plum")) != null){
						frame.addIngredientToMenu((Ingredient)ing);
						String speech = npc.getSpeech();
						npc.setSecondSpeech("Hello my little friend, You know, you never stop learning.");
						return speech + "<BR><BR>" + currentPlayer.addObjCalled(ing.getName());
					}
				}
				else if (npc.getName().equals("scared man") && 
						(currentPlayer.getToolFromString("demogorgon tooth")!= null)) {
					npc.setSecondSpeech("Holy Guacamole you have defeated the demogorgon, I can tell it"
							+ " from the tooth you are carrying!<BR>here, take your prize, I will never be "
							+ "grateful enough.<BR>Ah, I almost forgot to tell you that a weird druid passed here"
							+ ", he was looking for you and he said that he sent someone to look for a <b>belladonna flower</b>"
							+ " and the <b>hart of the king of the oblivion</b>,"
							+ " I really don't know what he ment with it.<BR>Now I must go, see you around.");
					Tool k = npc.getToolFromString("passepartout"); //a key that opens any door
					npc.removeObjCalled("passepartout");
					currentPlayer.addObj(k);
					frame.addItemToMenu(k);
					checkNewCommand(k);
					currentPlayer.getCurrentRoom().removeNpcNamed("scared man");
					//add some enemies:
					NpcBad goblin = new NpcBad("goblin","a red goblin",50,100,true,"arghaaaaaalllhhh",7);
					goblin.addObj(new Tool("apple", "a green apple, seems quite sour", 5));
					map.getRoom(8).addnpcs(goblin);
					map.getRoom(14).addnpcs(new NpcBad("wolf","a huge grey wolf with yellow eyes",50,40,true,"grrrrrrrrhhhh",5));
					
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

	/**
	 * print the help message containing the possible commands and insructions
	 * @param command
	 * @return the string to be returned and placed in the text-box
	 */
	public String printHelp(Command command) {

		return "<b>Your commands are:</b> <i>" + command.listCommands() + "</i><BR>"
				+ "commands can be combined with other words or objects (usually charachters or items in the room),"
				+ " some examples are : <b>'go north', 'examine', 'examine chest', 'pick up key', 'attack goblin',"
				+ " 'say hello'.</b> <BR>P.S. : Commands can change during the course of the story.";

	}

	/**
	 * print the welcome message
	 * @param dead : if false prints normal welcome message otherwise it adds 'YOU DIED'
	 */
	public void printWelcome(boolean dead) {
		String toChange = "<html><body>";
		if(dead){
			toChange += "<p style='font-size: 40px'>YOU DIED!</p>";
		}
		toChange += "<h1>welcome to THE MYSTICAL ADVENTURE</h1>";
		if(!dead){
			toChange += "<p>This is a text based game, this means you will read some description and type some commands"
					+ " to perform some actions.<BR>If you neen help with the commands write 'help'</p>";
		}
		toChange += "<p>"	+ currentPlayer.getCurrentRoom().getNameAndDescription() + "</p>" + "</html>";
		toChange = toChange.replaceAll("<p>", "<p style=font-size:13px>");
		frame.getPane().setText(toChange);
	}
	
	/**
	 * start the game printing the welcome message
	 */
	public void start() {
		printWelcome(false);
	}

	/**
	 * the active NpcBads attack the player whatever action he does, this method makes the enemy
	 * attack the player
	 * @return the string to be returned and placed in the text-box
	 */
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

	/**
	 * enlight the room allowing the items and the directions to be seen
	 * @param currentR: the room to be enlit
	 */
	public void enlight(Room currentR) {

		if (currentPlayer.getWeapon().getName().equals("torch") && currentPlayer.getWeapon().getDamage() == 5) {
			if (currentR.getName().equals("THE WOOD - East")) {
				currentR.setDark(false);
				currentR.setDescription("<I>the trees in this part of the wood are thicker"
						+ " but the light of your torch heps you see better</i>");
			} else if (currentR.getName().equals("THE TUNNEL")) {
				currentR.setDark(false);
				currentPlayer.getCurrentRoom().setDescription("<i>The light of your torch enlights the tunnel</i>");
				map.addPassage(7, 10, "east"); //create passage
				map.addPassage(10, 7, "west"); //create passage
			}
			else if(currentR.getName().equals("THE CAVE BEYOND THE WATERFALL")){
				currentR.setDark(false);
				currentR.setDescription("<i>An empty cave with moist walls, some grass and flowers on the floor.<BR><BR>"
						+ "You hear a voice saying: 'ONLY ONE FLOWER CAN BE TAKEN IN THIS CAVE'</i>");
			}
			else if(currentR.getName().equals("THE CAVE BEYOND THE WATERFALL - Behind the door")){
				currentR.setDark(false);
				currentR.setDescription("<i>the roof is so low you have to crawl to get in. "
				+ "There is a strong smell of moist and the floor is wet.</i>");
			}
		} else {
			if (currentR.getName().equals("THE WOOD - East")) {
				currentR.setDark(true);
				currentR.setDescription("<i>the trees in this part of the wood are thicker and you struggle"
						+ " to see anything in this area</i>");
			} else if (currentR.getName().equals("THE TUNNEL")) {
				currentR.setDark(true);
				currentPlayer.getCurrentRoom().setDescription(
						"<i>You can see nothing but the entrance" + " of the tunnel behind you. it's really dark</i>");
			}
			else if(currentR.getName().equals("THE CAVE BEYOND THE WATERFALL")){
				currentR.setDark(true);
				currentR.setDescription("<i>The area is quite dark</i>");
			}
			else if(currentR.getName().equals("THE CAVE BEYOND THE WATERFALL - Behind the door")){
				currentR.setDark(true);
				currentR.setDescription("<i>The area is quite dark</i>");
			}
		}

	}
	
	/**
	 * swap the results of the riddle on the doors and change the length of the codeword
	 * in the riddle so that the result changes.
	 * The numbers on the doors are randomized and change every time you enter the room.
	 */
	private void changeDoors() {
		if (currentPlayer.getCurrentRoom().getName().equals("THE WOOD - South")) {
			Random rand = new Random();
			int code = rand.nextInt(4);
			map.setLorwinCode(code);
			String desc = "The door is locked, on it someone wrote the number ";
			currentPlayer.getCurrentRoom().setDescription("<i>This area is surrounded by rock walls, seven doors are on the south "
				+ "side of the area, above them a well defined engraving says: 'In the Lorwin code a "
				+ "valid codeword does not contain any digit more than once and "
				+ "cannot contain both 0 and 1 in the same codeword.<BR>"
				+ "How many possible strings of "+ map.getLorwinCodeLength() +" digits are there?'</i>");
			

			for (int i = 1; i < 8; i++) { //for each door
				int num = rand.nextInt(100) + 10; //number to add/subtract from the correct result
				boolean b = rand.nextBoolean(); //to randomize whether to add or subtract the random from the result
				if (b) {
					((Fixed) currentPlayer.getCurrentRoom().getItemNamed("door " + i))
							.setDescription(desc + (map.getLorwinCodeSolution() + num));
				} else {
					((Fixed) currentPlayer.getCurrentRoom().getItemNamed("door " + i))
							.setDescription(desc + (map.getLorwinCodeSolution() - num));
				}
			}
			int n = rand.nextInt(7) + 1; //set the correct result on a random door
			correctDoor = n;
			((Fixed) currentPlayer.getCurrentRoom().getItemNamed("door " + n))
					.setDescription(desc + map.getLorwinCodeSolution());
		}
	}
	
	private String createEndCode(){
//------generate code---------------------
		Random rand = new Random();
		String code = "";
		code += (rand.nextInt(7) + 1) + "k";
		if(rand.nextBoolean()){
			code += "t";
		}
		else{
			code += "w";
		}
		code += "35bb"+ (rand.nextInt(4) + 3);
		if(rand.nextBoolean()){
			if(rand.nextBoolean()){
				code += "rd";
			}
			else{
				code += "sa";
			}
		}
		else{
			if(rand.nextBoolean()){
				code += "gp";
			}
			else{
				code += "tx";
			}
		}
		code += (rand.nextInt(4) + 3);
//------------------------------------------
		try {
			Thread.sleep(3000);     //pause (sleep)
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		JFrame nf = new JFrame(); //new window with congrats and code
			
		nf.getContentPane().add(new JLabel("Congratulations!!!"));
		nf.getContentPane().add(new JLabel("you have completed 'The Mystical Adventure'"));
		nf.getContentPane().add(new JLabel("You can send the following code by E-mail to giacomobenso94@gmail.com"));
		nf.getContentPane().add(new JLabel(code));
		nf.getContentPane().add(new JLabel("if you are among the first 10 people to complete the game your name will be added to the official 'The Mystical Adventure' page"));
		nf.setSize(new Dimension(600, 600));
		
		return "";
		
	}
}
