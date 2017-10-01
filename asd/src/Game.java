import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
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
	private boolean end = false; //end of the game

	private Timer timer; //timer needed for the end of the game
	private int counter = 7; //counter used in the timer (seconds of delay)

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
				currentPlayer.getLifeRemaning() <=0){ // incorrect room at lorwin's or 0 life remaining means you are dead
					currentPlayer.die();
				}
				if(end){
					createEndCode();
					end = false;
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
		} else if (firstWord.equals("drink")){
			return drink(command);
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
	public String checkNewCommand(Tool t) {
		if (t.getName().equals("matches")) {
			Command.addCommand("light up");
			return "<BR><BR>NEW COMMAND ADDED: 'light up'";
		}
		if (t.getName().equals("key") || t.getName().equals("skeleton key")) {
			Command.addCommand("open");
			return "<BR><BR>NEW COMMAND ADDED: 'open'";
		}
		if (t.getName().equals("potion") && !Command.commandWords.contains("drink")) {
			Command.addCommand("drink");
			return "<BR><BR>NEW COMMAND ADDED: 'drink'";
		}
		if(t.getName().equals("map piece") && !Command.commandWords.contains("map")){
			Command.addCommand("map");
			return "<BR><BR>NEW COMMAND ADDED: 'map'";
		}
		return "";
	}
	
	public String eat(Command command){
		if(!command.hasSecondWord()){
			return "What do you want to eat? Type 'eat -item-'";
		}
		if(currentPlayer.getToolFromString(command.getSecondWord()) == null){
			return "You cannot eat " + command.getSecondWord();
		}
		if(currentPlayer.getToolFromString(command.getSecondWord()).getName().equals("raspberry") || 
				currentPlayer.getToolFromString(command.getSecondWord()).getName().equals("carrot") ||
				currentPlayer.getToolFromString(command.getSecondWord()).getName().equals("apple")){
			currentPlayer.removeObjCalled(command.getSecondWord());
			frame.removeItemFromMenu(command.getSecondWord());
			return "You eat " + command.getSecondWord() + ", the perfect snack for a big boy like you!";
		}
			return "You cannot eat " + command.getSecondWord();
	}
	
	/**
	 * open the map in a new window, the image opened depends on how many pieces you gathered
	 * @param command
	 * @return the string to be returned and placed in the text-box
	 */
	public String openMap(Command command){
		if(currentPlayer.getCurrentRoom().isDark()){
			return "It's too dark to perform this action.";
		}
		
		JFrame nf = new JFrame();
		
		if(frame.getMapPieces() == 1){
			nf.getContentPane().add(new JLabel(new ImageIcon("./lib/mapPieces/piece1.jpg")));
			nf.setSize(new Dimension(266, 500));
		}
		else if(frame.getMapPieces() == 2){
			nf.getContentPane().add(new JLabel(new ImageIcon("./lib/mapPieces/piece2.jpg")));
			nf.setSize(new Dimension(455, 500));
		}
		else if(frame.getMapPieces() == 3){
			nf.getContentPane().add(new JLabel(new ImageIcon("./lib/mapPieces/piece3.jpg")));
			nf.setSize(new Dimension(667, 500));
		}
		nf.setVisible(true);
		
		return "Map opened";
	}
	
	/**
	 * buy a tool from the merchant, the item is removed from him and added to your inventory
	 * @param command
	 * @return the string to be returned and placed in the text-box
	 */
	public String buyFromMerchant(Command command){
		Merchant merchant;
		if((merchant = currentPlayer.getCurrentRoom().getMerchant()) == null){
			return "There is no merchant in this area";
		}
		if(!command.hasSecondWord()){
			return "What do you want to buy? Type 'buy -item-'";
		}
		if (frame.BagFull() && currentPlayer.getToolFromString(command.getSecondWord()) == null) {
			return "Your bag is full";
		}
		Tool t;
		if((t = currentPlayer.buyToolFromMerchant(merchant, command.getSecondWord())) != null){
			currentPlayer.removeMoney(t.getValue() * merchant.getPriceModifier());
			merchant.removeObjCalled(t.getName());
			currentPlayer.addObj(t);
			frame.addItemToMenu(t);
			return "You bought " + command.getSecondWord() + checkNewCommand(t);
		}
		else{
			return "You cannot buy " + command.getSecondWord();
		}
	}
	
	/**
	 * drink something you are carrying
	 * @param command
	 * @return the string to be returned and placed in the text-box
	 */
	public String drink(Command command){
		if(!command.hasSecondWord()){
			return "What do you want to drink? Type 'drink -item-'";
		}
		//if the item drinked is a potion:
		if(command.getSecondWord().equals("potion") && currentPlayer.getToolFromString("potion") != null){
			int life;
			if((currentPlayer.getHP() - currentPlayer.getLifeRemaning()) > (currentPlayer.getHP() -50)){
				currentPlayer.setLifeRemaining(life = currentPlayer.getLifeRemaning() + 50);
				GameWindow.greenLabelsCounter = life;
				frame.resetLifelabel();
				currentPlayer.removeObjCalled("potion");
				frame.removeItemFromMenu("potion");
				Command.removeCommand("potion", currentPlayer);
				return "50 HP healed";
			}	
			else{
				int healed = currentPlayer.getHP() - currentPlayer.getLifeRemaning();
				currentPlayer.setLifeRemaining(life = currentPlayer.getHP());
				GameWindow.greenLabelsCounter = life;
				frame.resetLifelabel();
				currentPlayer.removeObjCalled("potion");
				frame.removeItemFromMenu("potion");
				Command.removeCommand("potion", currentPlayer);
				return healed + " HP healed";
			}
		}
		return "You cannot drink this item";
	}

	/**
	 * light up the torch, to use this command you need to have the torch equipped and you need to have picked up 
	 * the matches.
	 * @param command
	 * @return the string to be returned and placed in the text-box
	 */
	public String lightUp(Command command) {
		if (currentPlayer.getToolFromString("matches") == null) {
			return "You cannot light up anything without the appropriate tool" + beingattacked();
		}
		if (!command.hasSecondWord()) {
			return "What do you want to light up? <BR>Type 'light up -item-'"+ beingattacked();
		}
		if (currentPlayer.getWeapon().getName().equals("torch") && currentPlayer.getWeapon().getDamage() == 5) {
			return "Your torch has already been lit"+ beingattacked();
		}
		if (command.getSecondWord().equals("torch")) {
			if (currentPlayer.getToolFromString("torch") != null) {
				if (currentPlayer.getCurrentRoom().isDark()) {
					return "you can't see anything! Try to do it where there is some light"+ beingattacked();
				} else {
					if (currentPlayer.getWeapon().getName().equals("torch")) {
						currentPlayer.getWeapon().setDamage(5); // damage
																// increased by
																// fire (also
																// new
																// functionality)
						currentPlayer.getWeapon().setDescription("The torch flames glow");
						return "You ignite the torch"+ beingattacked();
					}
					return "Torch needs to be equiped first"+ beingattacked();
				}
			}
			return "Yuo are not carrying any torch."+ beingattacked();
		}
		return "You can't light up " + command.getSecondWord()+ beingattacked();
	}

	/**
	 * open the door, to use this command you need a key or a skeleton key.
	 * @param command
	 * @return the string to be returned and placed in the text-box
	 */
	public String openDoor(Command command) {
		if(currentPlayer.getCurrentRoom().isDark()){
			return "It's too dark to perform this action.";
		}
		Tool ky;
		if ((ky =currentPlayer.getToolFromString("key")) == null && 
				(ky =currentPlayer.getToolFromString("skeleton key")) == null) {
			return "You cannot open anything without the appropriate key"+ beingattacked();
		}
		if (!command.hasSecondWord()) {
			return "What do you want to open? <BR>Type 'open -item-'"+ beingattacked();
		}
		if (currentPlayer.getCurrentRoom().getName().equals("THE FOREST - South")) {
			if(currentPlayer.getToolFromString("key") == null){
				return "You need the appropriate key to open the door";
			}
			if (command.getSecondWord().equals("door"))
				return "Which one of them? write 'open door -number-'";
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
					//remove the open command if not holding a skeleton key or another key
					Command.removeCommand("key", currentPlayer);
					return "You unlocked the Door number " + tmp[1] + ", the key magically disappears."
							+ "<BR>The passage is now open (south)";
				} else
					return "That door does not exist";
			}
		}		
			if (command.getSecondWord().equals("door")){
				if(currentPlayer.getToolFromString("skeleton key") == null){
					return "You need the appropriate key to open the door";
				}
				if (currentPlayer.getCurrentRoom().getName().equals("THE FOREST - Quiet area")) {
					if(currentPlayer.getCurrentRoom().getItemNamed("door").getDescription().equals("The Door is open")){
						return "That door is already open";
					}
					currentPlayer.getCurrentRoom().getItemNamed("door").setDescription("The Door is open");
					map.addPassage(15, 16, "west"); //create the passage
					map.addPassage(16, 15, "east"); //create the passage
					return "You unlocked the door and the passage is now open (west)";
				}
				else if (currentPlayer.getCurrentRoom().getName().equals("THE CAVE BEYOND THE WATERFALL")) {
					if(currentPlayer.getCurrentRoom().getItemNamed("door").getDescription().equals("The Door is Open")){
						return "That door is already open";
					}
					currentPlayer.getCurrentRoom().getItemNamed("door").setDescription("The Door is Open");
					map.addPassage(17, 19, "east"); //create the passage
					map.addPassage(19, 17, "west"); //create the passage
					return "You unlocked the door, the passage is now open (east)";
				}
			}
					
		return "You can't open " + command.getSecondWord()+ beingattacked();
	}

	/**
	 * talk loud, you do not speak to someone, just pronounce the words loud
	 * @param command
	 * @return the string to be returned and placed in the text-box
	 */
	public String saySomething(Command command) {
		if (!command.hasSecondWord())
			return "What do you want to say? Type 'say -word/sentance-'" + beingattacked();
		NPC npc;
		if ((command.getSecondWord().equals("moon"))
				&& currentPlayer.getCurrentRoom().getName().equals("THE CLEARING")) { // riddle solved
			NPC treant;
			if(!(treant = currentPlayer.getCurrentRoom().getNPCNamed("treant")).getSpeech().equals("Congratulations"
					+ " you have solved the riddle, now you can pass.")){
				map.addPassage(3, 4, "south"); //create the passage 
				treant.setSpeech("Congratulations you have solved the riddle, you can now pass.");
				if(map.getRoom(1).getNPCNamed("druid")!= null){
					map.getRoom(1).removeNpcNamed("druid");
				}
				return "<B>treant:</b> <i>Seven</i> is such a good number..."
						+ "<BR><BR>The treant slowly moves to the left, there is now a passage where he sat (south).";
			}else{
				return "You already opened the passage";
			}
		}
		else if((npc = currentPlayer.getCurrentRoom().getNPCNamed("demogorgon")) != null){
			if(!npc.isAlive){
				String toReturn;
				if(command.getSecondWord().equals("15546") && !currentPlayer.isGoneOblivion()){ //go to the oblivion
					currentPlayer.setGoneOblivion(true);
					currentPlayer.setCurrentRoom(map.getRoom(20));
					toReturn = "You start feeling disorientated and your eyes start to feel heavy...<BR><BR>" + 
							currentPlayer.getCurrentRoom().getNameAndDescription();
					if (!(currentPlayer.getCurrentRoom().getNPCArray()).isEmpty()) {
						for (NPC npc1 : currentPlayer.getCurrentRoom().getNPCArray()) {
							toReturn += npc1.interact(currentPlayer);
						}
					} 
				return toReturn;					
				}
				else{
					return "Nothing happens";
				}
			}else {
				return "Nothing happens"+ beingattacked();
			}
		}
		else if((command.getSecondWord().equals("7") || command.getSecondWord().equals("seven"))
				&& currentPlayer.getCurrentRoom().getName().equals("THE VALLEY")){
			currentPlayer.getCurrentRoom().setDescription("<i>In front of you, you see a shallow body of water"
					+ " and all around flowers like you have never seen before. East you can see a waterfall with a small entrance beside it. "
					+ "North, there is a dark entrance.</i>");
				map.addPassage(6, 17, "east"); //create passage
				map.addPassage(17, 6, "west"); //create passage
				return "Two of the trees over the waterfall move slightly and the flow of the waterfall changes,"
						+ " a passage (east) has been uncovered." + beingattacked();
		}
		else {
			return "Nothing happens"+ beingattacked();
		}
	}

	/**
	 * move from the current room to the next one
	 * @param command
	 * @return the string to be returned and placed in the text-box
	 */
	public String goRoom(Command command) {
		if(!command.hasSecondWord()){
			return "Where do you want to go? Type 'go -direction-'";
		}		
		
		String toReturn = "";
		Room prev;
		
		if ((prev = currentPlayer.getCurrentRoom()).hasDirection(command.getSecondWord())) { //if the direction exists
			Room next = currentPlayer.getCurrentRoom().getDirectionRoom(command.getSecondWord());
			currentPlayer.setCurrentRoom(next);	
			enlight(currentPlayer.getCurrentRoom()); // illuminate the room if you are carrying a torch
			changeDoors(); // change results on the doors for lorwin's riddle
			toReturn += additionalDescription(prev, next); //additional description in some rooms
			toReturn += currentPlayer.getCurrentRoom().getNameAndDescription();
			if(currentPlayer.getCurrentRoom().getNPCNamed("druid") != null && currentPlayer.getCurrentRoom().getName().equals("THE LIVING ROOM")
					&& currentPlayer.getCurrentRoom().getNPCNamed("druid").getHP() == 999){ // 999 hp means second time you meet the druid (end)
				end = true;
				return toReturn + "<BR><BR>" +currentPlayer.getCurrentRoom().getNPCNamed("druid").getSpeech() + 
						"<BR><BR><p style='font-size: 40px'>YOU DIED!</p>" + currentPlayer.die();
			}
			if (!(currentPlayer.getCurrentRoom().getNPCArray()).isEmpty()) {
				for (NPC npc : currentPlayer.getCurrentRoom().getNPCArray()) {
					toReturn += npc.interact(currentPlayer);
				}
			}
		} else {
			toReturn = "You can't go in that direction!";
		}
		return toReturn;
	}

	/**
	 * take the tool and add it to the bag, it could be a weapon, a normal tool or some money
	 * @param command
	 * @return the string to be returned and placed in the text-box
	 */
	public String pickTool(Command command) {
		if(currentPlayer.getCurrentRoom().isDark()){
			return "It's too dark to perform this action.";
		}
		Item temp;
		if (!command.hasSecondWord()){
			return "What do you want to " + command.getFirstWord() + "? Type '" 
		+ command.getFirstWord() + " -item-'" + beingattacked();
		}
		else if (command.getSecondWord().equals("money")) {
			int mon;
			if((mon = currentPlayer.getCurrentRoom().getMoney()) > 0){
				currentPlayer.getCurrentRoom().removeMoney(mon);
				currentPlayer.addMoney(mon);
				return "Money added: " + mon + beingattacked();
			}
			for (Item f : currentPlayer.getCurrentRoom().getItemsArray()) {
				if (f.getClass() == Fixed.class) {
					if (((Fixed) f).hasBeenOpened() && ((Fixed) f).getMoney() > 0) {
						currentPlayer.addMoney(mon = ((Fixed) f).getMoney());
						((Fixed) f).removeAllMoney();
						return "Money added: " + mon + beingattacked();
					}
				}
			}
			return "There is no money to take";
		} else {
			
			if ((temp = currentPlayer.getCurrentRoom().getItemNamed(command.getSecondWord())) != null) { 
				if (temp.getClass() == Tool.class || temp.getClass() == Weapon.class) {
					if (frame.BagFull() && currentPlayer.getToolFromString(command.getSecondWord()) == null) {
						return "Your bag is full";
					}
					//if you take any of the two flowers in the cave the other one disappears
					if(temp.getName().equals("hibiscus flower")){ 
						currentPlayer.getCurrentRoom().removeItemNamed("belladonna flower");
					}
					currentPlayer.addObjCalled(temp.getName());
					frame.addItemToMenu((Tool) temp);
					currentPlayer.currentRoom.removeItemNamed(temp.getName());
					return "You picked up " + temp.getName() + checkNewCommand((Tool) temp) + beingattacked();
				}
				else if(temp.getClass() == Ingredient.class){
					if(temp.getName().equals("belladonna flower")){
					currentPlayer.getCurrentRoom().removeItemNamed("hibiscus flower");
				}
					frame.addItemToMenu((Ingredient) temp); // add it to the ingredients 
					currentPlayer.addObjCalled(temp.getName());
					currentPlayer.currentRoom.removeItemNamed(temp.getName());
					if(currentPlayer.ingredientsFound() == 3){
						NpcGood druid = new NpcGood("druid","A mysteryous being who dwarfs over you, many of his features resemble humans but"
								+ " you can't help but notice his goat like horns and piercing red eyes.", 999, 150, true, currentPlayer.getName()
								+ "!! you found all the ingredients! I always believed in you!<BR>There is something you have to know though,"
								+ " you are not a real person, I summoned you to fulfil my goal to save the forest.<BR>And there is more, "
								+ "my potion to heal the forest needs a fourth ingredient, which is the soul of the summoning who found the three"
								+ " ingredients.<BR>I am sorry...");
						map.getRoom(1).addnpcs(druid); //add the druid to his house
					}
					if(temp.getName().equals("dremora heart") && currentPlayer.getCurrentRoom().getName().equals("THE OBLIVION")){ //if you take the heart of the dremora
						currentPlayer.setCurrentRoom(map.getRoom(14)); //get back to the forest
						return "You start feeling disorientated and your eyes start to feel heavy...<BR><BR>" + 
								currentPlayer.getCurrentRoom().getNameAndDescription() + beingattacked();
					}
					return "You picked up " + temp.getName() + beingattacked();
				}
				else {
					return "You can't pick up " + temp.getName() + " try the command 'examine " + temp.getName() + "'"
							+ beingattacked();
				}
			}
			return "You don't see any " + command.getSecondWord() + beingattacked();
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
			return "What do you want to " + command.getFirstWord() + "? Type '" 
		+ command.getFirstWord() + " -item-'" + beingattacked();
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
				if(remove.equals("key") || remove.equals("skeleton key") || remove.equals("torch") || 
						remove.equals("map piece")){ 
					//remove the appropriate command if item dropped
					Command.removeCommand(remove, currentPlayer);
				}
				return "You dropped " + t.getName() + beingattacked();
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
		if (!command.hasSecondWord() || command.getSecondWord().equals("room") || 
				command.getSecondWord().equals("area")) {
			return currentPlayer.currentRoom.getNameAndDescription() + beingattacked();
		}
		if(currentPlayer.getCurrentRoom().isDark()){
			return "It's too dark to perform this action.";
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
					ret += "Items : " + t.getToolList();
				}
				if (t.getMoney() != 0) {
					if (t.getDescription() != null && t.getToolList().isEmpty())
						ret += "<BR><BR>";
					else if (!t.getToolList().isEmpty())
						ret += "<BR>";
					ret += "Money : " + t.getMoney();
				}
				if(t.getMoney() == 0 && t.getToolList().isEmpty() && t.getDescription() == null){
					ret = t.getName() + " is empty";
				}
				return ret + beingattacked();
			}
		} else if ((npc = currentPlayer.currentRoom.getNPCNamed(command.getSecondWord())) != null) {
			return npc.getDescription();
		}
		return "There is no " + command.getSecondWord() + " to examine" + beingattacked();
	}

	/**
	 * attack an enemy (you can only attack NpcBad)
	 * @param command
	 * @return the string to be returned and placed in the text-box
	 */
	public String attack(Command command) {
		if(currentPlayer.getCurrentRoom().isDark()){
			return "It's too dark to perform this action.";
		}
		if (!command.hasSecondWord()) {
			return "Who do you want to attack? type 'attack -target-'";
		}
		NPC enemy;
		if ((enemy = currentPlayer.currentRoom.getNPCNamed(command.getSecondWord())) != null) {
			if (!enemy.isAlive()) {
				return enemy.getName() + " is already dead";
			} else if (enemy.getClass() == NpcBad.class){
					if(enemy.getName().equals("demogorgon") && currentPlayer.getWeapon().getName().equals("demon-slayer")){
						enemy.setLifeRemaining(0);
						enemy.setDescription("The Demogorgon has a collar with some numbers written on it: 6, 66, 426, 2586");
						return "Your sword glows, "+ enemy.getName() + " start burning from inside and crumbles into ashes."+
						"<BR>" + ((NpcBad)enemy).die();
					}
					else if(enemy.getName().equals("lord dremora") && currentPlayer.getWeapon().getName().equals("demon-slayer")){
						enemy.setLifeRemaining(enemy.getLifeRemaning() - 100);
						if(enemy.getLifeRemaning() < 150){
							return "Your sword glows bright white, "+ enemy.getName() + " start burning from inside and crumbles into ashes."+ 
						"<BR>" + ((NpcBad)enemy).die();
						}
					}
				return currentPlayer.attackTarget((NpcBad)enemy) + "<BR><BR>" + ((NpcBad)enemy).attackTarget(currentPlayer);
			}
			else
				return "You cannot attack " + command.getSecondWord();
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
			return "What do you want to equip? type 'equip -item-'";
		}
		Tool t;
		if ((t = currentPlayer.getToolFromString(command.getSecondWord())) != null) {
			if (t.getClass() == Weapon.class && !currentPlayer.getWeapon().getName().equals(command.getSecondWord())){
				frame.getWeaponLabel().setText(t.getName());
				if (currentPlayer.getWeapon().getName().equals("torch")) { // put out the torch
					currentPlayer.getWeapon().setDamage(3);
					enlight(currentPlayer.getCurrentRoom());
					currentPlayer.getWeapon().setDescription("A wooden torch, someone has already used it. "
							+ "it should still be possible to light it up.");
					return "You put out the torch.<BR>" + currentPlayer.equipWeapon((Weapon) t) + beingattacked();
				}
				return currentPlayer.equipWeapon((Weapon) t) + beingattacked();
			} else {
				return "It would be useless" + beingattacked();
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
			return "You: blablablabla. <BR>if you want to speak to someone write 'speak to -subject-'"
					+ beingattacked();
		}
		NPC npc;
		if ((npc = currentPlayer.getCurrentRoom().getNPCNamed(command.getSecondWord())) != null) {
			if (npc.getClass() == NpcGood.class) {
				if (npc.getName().equals("druid") && currentPlayer.getCurrentRoom().getName().equals("THE LIVING ROOM")
						&& npc.getHP() == 1000){ // 1000 hp means first time you meet the druid (beginning)
					currentPlayer.getCurrentRoom().removeNpcNamed("druid");
				}
				else if (npc.getName().equals("lorwin")) { //if it's lorwin
					Tool ing;
					if((ing = npc.getToolFromString("phoenix plume")) != null){
						frame.addItemToMenu((Ingredient)ing);
						String speech = npc.getSpeech();
						npc.setSecondSpeech("Hello my little friend, the key to knowledge is "
								+ "learning and without that the world is a dark place.");
						return speech + "<BR><BR>" + currentPlayer.addObjCalled(ing.getName());
					}
				}
				else if (npc.getName().equals("scared man") && 
						(currentPlayer.getToolFromString("demogorgon tooth")!= null)) {		
					if (frame.BagFull()) {
						return "Your bag is full, drop something and come back!";
					}
					if(npc.isFirstTimeMet()){
						npc.setFirstTimeMet(false);
					}
					npc.setSecondSpeech("B...bb...by the holy light, you have defeated the demogorgon, I...I..I can tell"
							+ " from that too..to..tooth you're carrying!<BR>he..he..here, take your prize, I will never be "
							+ "grateful enough.<BR>Oh also, I...I..I almost forgot to tell you that a goat horned man passed by"
							+ ", he was looking fo..f..for you I think. He told me that he sent someone to look for a <b>belladonna flower</b>"
							+ " and the <b>hart of the king of the oblivion</b>,"
							+ " I really don't know what he meant with it.<BR>Now I mu...mus...must go, thank you stranger.");
					Tool k = npc.getToolFromString("skeleton key"); //a key that opens any door
					npc.removeObjCalled("skeleton key");
					currentPlayer.addObj(k);
					frame.addItemToMenu(k);
					currentPlayer.getCurrentRoom().removeNpcNamed("scared man");
					//add some enemies:
					NpcBad goblin = new NpcBad("goblin","An ugly red goblin",80,150,true,"arghaaaaaalllhhh",9);
					goblin.addObj(new Tool("apple", "A green apple, it seems to be rotten.", 5));
					map.getRoom(8).addnpcs(goblin);
					map.getRoom(14).addnpcs(new NpcBad("wolf","A huge 4 legged creature with pulsing yellow eyes.",90,40,true,"grrrrrrrrhhhh",5));				
					return npc.getSpeech() + "<BR><BR>" + k.getName() + " added to " + 
					currentPlayer.getName() + "'s inventory" + checkNewCommand(k);
				}
				return npc.getSpeech() + beingattacked();
			} else {
				return npc.getSpeech() + beingattacked();
			}
		}
		return "You can't speak to " + command.getSecondWord() + beingattacked();
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
				+ " 'say hello'.</b> <BR>Commands can change during the course of the story.<BR>"
				+ "You can save your progress by clicking the 'save' button on the bottom-left corner of the screen."
				+ "<BR>If you die your progress will be lost.";
	}

	/**
	 * print the welcome message
	 * @param dead : if false prints normal welcome message otherwise prints 'YOU DIED',
	 * it it's you have never saved the game or you are dead it also adds the introduction.
	 */
	public void printWelcome(boolean dead) {
		String toChange = "<html><body>";
		if(dead){
			toChange += "<p style='font-size: 40px'>YOU DIED!</p>";
		}
		toChange += "<h1>welcome to THE MYSTICAL ADVENTURE</h1>";
		if(!dead){
			toChange += "<p>This is a text based adventure, you will be taken on a journey where you control the story.<BR>"
					+ " This means you will read some passages and type some commands to control the player."
					+ " <BR>You can type your instructions on the <B>white text-box</b> at the bottom"
					+ " of the window.<BR>If you neen help with the commands write 'help'</p>";
		}
		File fileIn = new File("./lib/savedGame/savedGameDeath.ser");
		File fileOut = new File("./lib/savedGame/savedGame.ser");
		try {
			if (FileUtils.contentEquals(fileIn, fileOut)){
				toChange += "<BR><hr><p>You open your eyes... <BR>You feel confused and you don't know where you are, "
						+ "you cannot even remember what happend in the past....</p>";
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
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
			if (currentR.getName().equals("THE FOREST - East")) {
				currentR.setDark(false);
				currentR.setDescription("<i>The trees in this part of the forest are dense making you question what's around every shadow."
						+ " But the light of your torch heps you see better</i>");
			} else if (currentR.getName().equals("THE TUNNEL")) {
				currentR.setDark(false);
				currentPlayer.getCurrentRoom().setDescription("<i>The light of your torch illuminates the tunnel.</i>");
				map.addPassage(7, 10, "east"); //create passage
				map.addPassage(10, 7, "west"); //create passage
			}
			else if(currentR.getName().equals("THE CAVE BEYOND THE WATERFALL")){
				currentR.setDark(false);
				currentR.setDescription("<i>An empty cave with moist walls, some fungi and flowers on the floor.</i>");
			}
			else if(currentR.getName().equals("THE CAVE BEYOND THE WATERFALL - Behind the door")){
				currentR.setDark(false);
				currentR.setDescription("<i>The roof is so low you have to crawl to get in. "
				+ "The floor is damp and a smell of blood attracts your attenction, you notice a corpse on the left corner.</i>");
			}
		} else {
			if (currentR.getName().equals("THE FOREST - East")) {
				currentR.setDark(true);
				currentR.setDescription("<i>The trees in this part of the forest are dense and you struggle"
						+ " to see anything in this area</i>");
			} else if (currentR.getName().equals("THE TUNNEL")) {
				currentR.setDark(true);
				currentPlayer.getCurrentRoom().setDescription(
						"<i>You can see nothing but the entrance of the tunnel behind you.</i>");
			}
			else if(currentR.getName().equals("THE CAVE BEYOND THE WATERFALL")){
				currentR.setDark(true);
				currentR.setDescription("<i>The area is quite dark and humid</i>");
			}
			else if(currentR.getName().equals("THE CAVE BEYOND THE WATERFALL - Behind the door")){
				currentR.setDark(true);
				currentR.setDescription("<i>The area is quite dark and humid</i>");
			}
		}

	}
	
	/**
	 * swap the results of the riddle on the doors and change the length of the codeword
	 * in the riddle so that the result changes.
	 * The numbers on the doors are randomized and change every time you enter the room.
	 */
	private void changeDoors() {
		if (currentPlayer.getCurrentRoom().getName().equals("THE FOREST - South")) {
			Random rand = new Random();
			int code = rand.nextInt(4);
			map.setLorwinCode(code);
			String desc = "The door is locked, on it someone wrote the number ";
			currentPlayer.getCurrentRoom().setDescription("<i>This area is surrounded by rock walls, seven doors are on the south "
				+ "side of the area, above them a well defined engraving saying: 'In the Lorwin code a "
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
	
	/**
	 * giving additional info for some specific rooms arriving from others
	 * @param prev: arriving from this room
	 * @param next: heading to this room
	 * @return
	 */
	private String additionalDescription(Room prev, Room next){
		
		if(prev.getName().equals("THE CLEARING") && next.getName().equals("THE LIVING ROOM"))
		return "You walk towards the front door of the house, looking up you notice some smoke coming out from the chimeny."
				+ " You open the door and get in...<BR><BR>";
		
		if(prev.getName().equals("THE FOREST - Dirty spot") && next.getName().equals("LITTLE CABIN"))
			return "You move west towards the little cabin and once in front of the door, you open it...<BR><BR>";
		
		if(prev.getName().equals("THE VALLEY") && next.getName().equals("THE CAVE BEYOND THE WATERFALL"))
			return "You get in and suddenly you hear a voice saying: 'ONLY ONE FLOWER CAN BE TAKEN IN THIS CAVE'<BR><BR>";
		
		return "";
	}
	
	private void createEndCode(){
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
		
		actione(code);
		
	}
	
	public void createFrame(String code){
		JFrame nf = new JFrame(); //new window with congrats and code
		JTextPane pp = new JTextPane();
		pp.setContentType("text/html");
		pp.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		pp.setEditable(false);
		pp.setText("<html><body style='text-align: center; font-size: 18px;'>"
				+ "<BR><BR><h1 style='color: red;font-size: 22px;'>Congratulations!!!</h1>"
				+ "<p>you have completed 'The Mystical Adventure'</p>"
				+ "<p>You can send the following code and the last words pronounced by the druid by E-mail to giacomobenso94@gmail.com</p><p><b>"
				+ code + "</b></p><p>If you are among the first 10 people to complete the game your name will be added to the official"
						+ " 'The Mystical Adventure' page</p></body></html>");
		nf.getContentPane().add(pp);
		nf.setSize(new Dimension(800, 600));
		nf.setVisible(true);
	}
	
	public void actione(String code){
		ActionListener action = new ActionListener()
        {   
            @Override
            public void actionPerformed(ActionEvent event)
            {
                if(counter == 0)
                {
                    timer.stop();
                    createFrame(code);
                }
                else
                {
                    counter--;
                }
            }
        };
	timer = new Timer(1000, action);
    timer.setInitialDelay(0);
    timer.start();
	}
}


