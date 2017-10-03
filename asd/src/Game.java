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
	public final Weapon NN = new Weapon("nessuna", "nessuna arma", 0, 1, 0.95f); //no weapon
	private int correctDoor = 1;
	private boolean end = false; //end of the game

	private Timer timer; //timer needed for the end of the game
	private int counter = 7; //counter used in the timer (seconds of delay)

	public Game() { // constructor
		frame = new GameWindow();
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
			
				if(currentPlayer.getCurrentRoom().getName().equals("SBAGLIATO") || 
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

		if (firstWord.equals("vai")) {
			return goRoom(command);
		} else if (firstWord.equals("aiuto")) {
			return printHelp(command);
		} else if (firstWord.equals("prendi") || firstWord.equals("raccogli")) {
			return pickTool(command);
		} else if (firstWord.equals("butta") || firstWord.equals("lascia")) {
			return dropTool(command);
		} else if (firstWord.equals("attacca")) {
			return attack(command);
		} else if (firstWord.equals("equipaggia")) {
			return equip(command);
		} else if (firstWord.equals("esamina")) {
			return examineObj(command);
		} else if (firstWord.equals("parla")) {
			return speakToChar(command);
		} else if (firstWord.equals("pronuncia")) {
			return saySomething(command);
		} else if (firstWord.equals("accendi")) {
			return lightUp(command);
		} else if (firstWord.equals("apri")){
			return openDoor(command);
		} else if (firstWord.equals("bevi")){
			return drink(command);
		} else if (firstWord.equals("compra")){
			return buyFromMerchant(command);
		}else if (firstWord.equals("mappa")){
			return openMap(command);
		}else if(firstWord.equals("mangia")){
			return eat(command);
		}else
			
			return "Non puoi usare questo comando, o non esiste o non e' quello appropriato"
			+ " da usare.<BR>Puoi vedere i comandi utilizzabili scrivendo 'aiuto'.";
	}

	/**
	 * checks if the tool picked up creates a new commandWord
	 * @param t : tool to be checked
	 */
	public String checkNewCommand(Tool t) {
		if (t.getName().equals("fiammiferi")) {
			Command.addCommand("accendi");
			return "<BR><BR>AGGIUNTO NUOVO COMANDO: 'accendi'";
		}
		if (t.getName().equals("chiave") || t.getName().equals("passepartout")) {
			Command.addCommand("apri");
			return "<BR><BR>AGGIUNTO NUOVO COMANDO: 'apri'";
		}
		if (t.getName().equals("pozione") && !Command.commandWords.contains("bevi")) {
			Command.addCommand("bevi");
			return "<BR><BR>AGGIUNTO NUOVO COMANDO: 'bevi'";
		}
		if(t.getName().equals("pezzo di mappa") && !Command.commandWords.contains("mappa")){
			Command.addCommand("mappa");
			return "<BR><BR>AGGIUNTO NUOVO COMANDO: 'mappa'";
		}
		return "";
	}
	
	public String eat(Command command){
		 		if(!command.hasSecondWord()){
		 			return "Cosa vorresti mangiare? Scrivi 'mangia -oggetto-'";
		 		}
		 		if(currentPlayer.getToolFromString(command.getSecondWord()) == null){
		 			return "Impossibile mangiare: " + command.getSecondWord();
		 		}
		 		if(currentPlayer.getToolFromString(command.getSecondWord()).getName().equals("lampone") || 
		 				currentPlayer.getToolFromString(command.getSecondWord()).getName().equals("carota") ||
		 				currentPlayer.getToolFromString(command.getSecondWord()).getName().equals("mela")){
		 			currentPlayer.removeObjCalled(command.getSecondWord());
		 			frame.removeItemFromMenu(command.getSecondWord());
		 			return "Hai mangiato: " + command.getSecondWord() + "<BR>La merenda perfetta per un ragazzone come te!";
		 		}
		 			return "Impossibile mangiare: " + command.getSecondWord();
		 	}
	
	/**
	 * open the map in a new window, the image opened depends on how many pieces you gathered
	 * @param command
	 * @return the string to be returned and placed in the text-box
	 */
	public String openMap(Command command){
		if(currentPlayer.getCurrentRoom().isDark()){
			return "E' troppo buio per compiere quest'azione.";
		}
		
		JFrame nf = new JFrame();
		String path = MysticalAdventure.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		path = path.substring(0, path.lastIndexOf("/") + 1);
		
		if(frame.getMapPieces() == 1){
			nf.getContentPane().add(new JLabel(new ImageIcon(path + "lib/mpp/tt/piece/piece1.jpg")));
			nf.setSize(new Dimension(303, 500));
		}
		else if(frame.getMapPieces() == 2){
			nf.getContentPane().add(new JLabel(new ImageIcon(path + "lib/mpp/tt/piece/piece2.jpg")));
			nf.setSize(new Dimension(476, 500));
		}
		else if(frame.getMapPieces() == 3){
			nf.getContentPane().add(new JLabel(new ImageIcon(path + "lib/mpp/tt/piece/piece3.jpg")));
			nf.setSize(new Dimension(667, 500));
		}
		nf.setVisible(true);
		return "Apri la mappa...";
	}
	
	/**
	 * buy a tool from the merchant, the item is removed from him and added to your inventory
	 * @param command
	 * @return the string to be returned and placed in the text-box
	 */
	public String buyFromMerchant(Command command){
		Merchant merchant;
		if((merchant = currentPlayer.getCurrentRoom().getMerchant()) == null){
			return "In questa zona non e' presente il mercante";
		}
		if(!command.hasSecondWord()){

			return "Cosa vorresti comprare? scrivi 'Compra -oggetto-'";
		}
		if (frame.BagFull() && currentPlayer.getToolFromString(command.getSecondWord()) == null) {
			 return "Lo zaino e' pieno";
		}
		Tool t;
		if((t = currentPlayer.buyToolFromMerchant(merchant, command.getSecondWord())) != null){
			currentPlayer.removeMoney(t.getValue() * merchant.getPriceModifier());
			merchant.removeObjCalled(t.getName());
			currentPlayer.addObj(t);
			frame.addItemToMenu(t); 
			return "Oggetto comprato: " + command.getSecondWord() + checkNewCommand(t);
		}
		else{
			return "Impossibile comprare: " + command.getSecondWord();
		}
	}
	
	/**
	 * drink something you are carrying
	 * @param command
	 * @return the string to be returned and placed in the text-box
	 */
	public String drink(Command command){
		if(!command.hasSecondWord()){
			return "Cosa vorresti bere? Scrivi 'bevi -oggetto-'";
		}
		//if the item drinked is a potion:
		if(command.getSecondWord().equals("pozione") && currentPlayer.getToolFromString("pozione") != null){
			int life;
			if((currentPlayer.getHP() - currentPlayer.getLifeRemaning()) > (currentPlayer.getHP() -50)){
				currentPlayer.setLifeRemaining(life = currentPlayer.getLifeRemaning() + 50);
				GameWindow.greenLabelsCounter = life;
				frame.resetLifelabel();
				currentPlayer.removeObjCalled("pozione");
				frame.removeItemFromMenu("pozione");
				Command.removeCommand("pozione", currentPlayer);
				return "50 HP recuperati";

			}	
			else{
				int healed = currentPlayer.getHP() - currentPlayer.getLifeRemaning();
				currentPlayer.setLifeRemaining(life = currentPlayer.getHP());
				GameWindow.greenLabelsCounter = life;
				frame.resetLifelabel();
				currentPlayer.removeObjCalled("pozione");
				frame.removeItemFromMenu("pozione");
				Command.removeCommand("pozione", currentPlayer);
				return healed + " HP recuperati";
			}
		}
		return "Non puoi bere questo oggetto";
	}

	/**
	 * light up the torch, to use this command you need to have the torch equipped and you need to have picked up 
	 * the matches.
	 * @param command
	 * @return the string to be returned and placed in the text-box
	 */
	public String lightUp(Command command) {
		if (currentPlayer.getToolFromString("fiammiferi") == null) {
			return "Non puoi accendere niente senza l'oggetto appropriato" + beingattacked();
		}
		if (!command.hasSecondWord()) {
			return "Cosa vorresti accendere? <BR>Scrivi 'accendi -oggetto-'"+ beingattacked();
		}
		if (currentPlayer.getWeapon().getName().equals("torcia") && currentPlayer.getWeapon().getDamage() == 5) {
			return "La torcia e' gia' accesa"+ beingattacked();
		}
		if (command.getSecondWord().equals("torcia")) {
			if (currentPlayer.getToolFromString("torcia") != null) {
				if (currentPlayer.getCurrentRoom().isDark()) {
					return "Non si vede niente! prova a farlo dove c'e' un po' di luce"+ beingattacked();
				} else {
					if (currentPlayer.getWeapon().getName().equals("torcia")) {
						currentPlayer.getWeapon().setDamage(5); // damage
																// increased by
																// fire (also
																// new
																// functionality)
						currentPlayer.getWeapon().setDescription("La fiamma della torcia brilla al buio");
						return "Hai acceso la torcia"+ beingattacked();
					}
					return "La torcia deve essere prima equipaggiata"+ beingattacked();
				}
			}
			return "Non hai una torcia con te"+ beingattacked();
		}
		return "Impossibile accendere: " + command.getSecondWord()+ beingattacked();
	}

	/**
	 * open the door, to use this command you need a key or a skeleton key.
	 * @param command
	 * @return the string to be returned and placed in the text-box
	 */
	public String openDoor(Command command) {
		if(currentPlayer.getCurrentRoom().isDark()){
			return "E' troppo buio per compiere quest'azione.";
		}		
		Tool ky;
		if ((ky =currentPlayer.getToolFromString("chiave")) == null && 
				(ky =currentPlayer.getToolFromString("passepartout")) == null) {
			return "Hai bisogno della chiave appropriata per aprire la porta"+ beingattacked();
		}
		if (!command.hasSecondWord()) {
			return "Cosa vorresti aprire? <BR>Scrivi 'apri -oggetto-'"+ beingattacked();
		}
		if (currentPlayer.getCurrentRoom().getName().equals("IL BOSCO - Sud")) {
			if(currentPlayer.getToolFromString("chiave") == null){
				return "Hai bisogno della chiave appropriata per aprire la porta";
			}
			if (command.getSecondWord().equals("porta") || command.getSecondWord().equals("la porta"))
				return "Quale vorresti aprire? Scrivi 'apri porta -numero-'";
			String[] tmp = command.getSecondWord().split(" ");
			if (tmp[0].equals("porta") && StringUtils.isNumeric(tmp[1])) {
				if (Integer.parseInt(tmp[1]) < 8 && Integer.parseInt(tmp[1]) > 0) {
					if(Integer.parseInt(tmp[1]) == correctDoor){
						map.addPassage(11, 13, "sud"); //create the passage
						map.addPassage(13, 11, "nord"); //create the passage
					}
					else{
						map.addPassage(11, 12, "sud"); //create the passage
					}
					frame.removeItemFromMenu(ky.getName()); //remove the key
					currentPlayer.getCurrentRoom().getItemNamed("porta " + tmp[1]).setDescription("La porta e' aperta");
					currentPlayer.removeObjCalled(ky.getName());
					//remove the open command if not holding a skeleton key or another key
					Command.removeCommand("chiave", currentPlayer);
					return "Hai aperto la porta numero " + tmp[1] + ", la chiave sparisce magicamente."
							+ "<BR>IL passaggio e' ora aperto (sud)";
				} else
					return "Quella porta non esiste";
			}
		}		
		if (command.getSecondWord().equals("porta")){
			if(currentPlayer.getToolFromString("passepartout") == null){
				return "Hai bisogno della chiave appropriata per aprire la porta";
			}
				if (currentPlayer.getCurrentRoom().getName().equals("IL BOSCO - Area tranquilla")) {
					if(currentPlayer.getCurrentRoom().getItemNamed("porta").getDescription().equals("La porta e' aperta")){
						return "La porta e' gia' aperta";
					}
					currentPlayer.getCurrentRoom().getItemNamed("porta").setDescription("La porta e' aperta");
					map.addPassage(15, 16, "ovest"); //create the passage
					map.addPassage(16, 15, "est"); //create the passage
					return "Hai aperto la porta, il passaggio e' ora aperto (ovest)";
				}
				else if (currentPlayer.getCurrentRoom().getName().equals("LA GROTTA DIETRO LA CASCATA")) {
					if(currentPlayer.getCurrentRoom().getItemNamed("porta").getDescription().equals("La porta e' aperta")){
						return "La porta e' gia' aperta";
					}
					currentPlayer.getCurrentRoom().getItemNamed("porta").setDescription("La porta e' aperta");
					map.addPassage(17, 19, "est"); //create the passage
					map.addPassage(19, 17, "ovest"); //create the passage
					return "Hai aperto la porta, il passaggio e' ora aperto (est)";
				}
			}
		return "Impossibile aprire: " + command.getSecondWord() + beingattacked();
	}

	/**
	 * talk loud, you do not speak to someone, just pronounce the words loud
	 * @param command
	 * @return the string to be returned and placed in the text-box
	 */
	public String saySomething(Command command) {
		if(!command.hasSecondWord()){
			return "Che cosa vuoi pronunciare? Scrivi 'pronuncia -parola/frase-'";
		}
		NPC npc;
		if ((command.getSecondWord().equals("luna"))
				&& currentPlayer.getCurrentRoom().getName().equals("LA RADURA")) { // riddle solved
			NPC treant;
			if(!(treant = currentPlayer.getCurrentRoom().getNPCNamed("treant")).getSpeech().equals("Congratulazioni, "
					+ "hai risolto l'indovinello! ora puoi passare.")){
				map.addPassage(3, 4, "sud"); //create the passage 
				treant.setSpeech("Bel lavoro con l'indovinello, ora puoi passare.");
				if(map.getRoom(1).getNPCNamed("druid")!= null){
					map.getRoom(1).removeNpcNamed("druid");
				}
				return "<B>treant:</b> <i>Sette</i> E' un proprio un bel numero..."
				+ "<BR><BR>Il treant si muove lentamente verso sinistra liberando il passaggio davanti al quale era seduto (sud).";
			}else{
				return "Hai gia' aperto il passaggio";
			}
		}
		else if((npc = currentPlayer.getCurrentRoom().getNPCNamed("demogorgone")) != null){
			if(!npc.isAlive){
				String toReturn;
				if(command.getSecondWord().equals("15546") && !currentPlayer.isGoneOblivion()){ //go to the oblivion
					currentPlayer.setGoneOblivion(true);
					currentPlayer.setCurrentRoom(map.getRoom(20));
					toReturn = "Dopo aver proninciato i numeri inizi a sentirti debole e perdi i sensi...<BR><BR>" + 
							currentPlayer.getCurrentRoom().getNameAndDescription();
					if (!(currentPlayer.getCurrentRoom().getNPCArray()).isEmpty()) {
						for (NPC npc1 : currentPlayer.getCurrentRoom().getNPCArray()) {
							toReturn += npc1.interact(currentPlayer);
							}
						}
					return toReturn;
				}
				else{
					return "Non succede niente";
				}
			}else {
				return "Non succede niente"+ beingattacked();
			}
		}
		else if((command.getSecondWord().equals("7") || command.getSecondWord().equals("sette"))
				&& currentPlayer.getCurrentRoom().getName().equals("LA VALLE")){
			currentPlayer.getCurrentRoom().setDescription("<i>Ti trovi in una valle, gli alberi sono meno presenti in questa zona che, attraversata da un ruscello"
					+ " da vita ad una vegetazione ricca di fiori e piccole piante. Ad est si puo' vedere una cascata con una piccola entrata a fianco. "
					+ "A nord c'e' un ingersso buio, sembra un tunnel..</i>");
				map.addPassage(6, 17, "est"); //create passage
				map.addPassage(17, 6, "ovest"); //create passage
				return "Due alberi sopra la cascata si muovono leggermente e il flusso dell'acqua cambia."
						+ " Un nuovo passaggio (est) e' stato scoperto"+ beingattacked();
		}
		else {
			return "Non succede niente"+ beingattacked();
		}
	}

	/**
	 * move from the current room to the next one
	 * @param command
	 * @return the string to be returned and placed in the text-box
	 */
	public String goRoom(Command command) {
		if(!command.hasSecondWord()){
			return "In che direzione vuoi andare? Scrivi 'vai a -direzione-'";
		}

		String toReturn = "";
		Room prev;
		
		if ((prev = currentPlayer.getCurrentRoom()).hasDirection(command.getSecondWord())) { //if the direction exists
			Room next = currentPlayer.getCurrentRoom().getDirectionRoom(command.getSecondWord());
			currentPlayer.setCurrentRoom(next);			
			enlight(currentPlayer.getCurrentRoom()); // illuminate the room if you are carrying a torch
			changeDoors(); // change results on the doors for lorwin's riddle
			toReturn += additionalDescription(prev, next); // additional description for the room
			toReturn += currentPlayer.getCurrentRoom().getNameAndDescription();
			if(currentPlayer.getCurrentRoom().getNPCNamed("druid") != null && 
					currentPlayer.getCurrentRoom().getName().equals("CASA DEL DRUIDO - Il salotto")
					&& currentPlayer.getCurrentRoom().getNPCNamed("druid").getHP() == 999){ // 999 hp means second time you meet the druid (end)
				end = true;
				return toReturn + "<BR><BR>" +currentPlayer.getCurrentRoom().getNPCNamed("druid").getSpeech() +
						"<BR><BR><p style='font-size: 40px'>SEI MORTO!</p>" + currentPlayer.die();
				}
			if (!(currentPlayer.getCurrentRoom().getNPCArray()).isEmpty()) {
				for (NPC npc : currentPlayer.getCurrentRoom().getNPCArray()) {
					toReturn += npc.interact(currentPlayer);
				}
			}
		} else {
			toReturn = "Non puoi andare in quella direzione!";
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
			return "E' troppo buio per compiere quest'azione.";
		}
		Item temp;
		if (!command.hasSecondWord()) {
			return "Cosa vorresti prendere? Scrivi 'prendi -oggetto-'";
		} else if (command.getSecondWord().equals("monete")) {
			int mon;
			if((mon = currentPlayer.getCurrentRoom().getMoney()) > 0){
				currentPlayer.getCurrentRoom().removeMoney(mon);
				currentPlayer.addMoney(mon);
				return "Monete aggiunte: " + mon + beingattacked();
			}
			for (Item f : currentPlayer.getCurrentRoom().getItemsArray()) {
				if (f.getClass() == Fixed.class) {
					if (((Fixed) f).hasBeenOpened() && ((Fixed) f).getMoney() > 0) {
						currentPlayer.addMoney(mon = ((Fixed) f).getMoney());
						((Fixed) f).removeAllMoney();
						return "Monete aggiunte: " + mon + beingattacked();

					}
				}
			}
			return "Non ci sono monete da prendere";
		} else {

			if ((temp = currentPlayer.getCurrentRoom().getItemNamed(command.getSecondWord())) != null) {
				if (temp.getClass() == Tool.class || temp.getClass() == Weapon.class) {
					if (frame.BagFull() && currentPlayer.getToolFromString(command.getSecondWord()) == null) {
						return "Lo zaino e' pieno";
					}
					//if you take any of the two flowers in the cave the other one disappears
					if(temp.getName().equals("fiore di ibisco")){
						currentPlayer.getCurrentRoom().removeItemNamed("fiore di belladonna");
					}
					currentPlayer.addObjCalled(temp.getName());
					frame.addItemToMenu((Tool) temp);
					currentPlayer.currentRoom.removeItemNamed(temp.getName());
					return "Oggetto raccolto: " + temp.getName() + checkNewCommand((Tool) temp) + beingattacked();
				}
				else if(temp.getClass() == Ingredient.class){
					if(temp.getName().equals("fiore di belladonna")){
					currentPlayer.getCurrentRoom().removeItemNamed("fiore di ibisco");
				}
					frame.addItemToMenu((Ingredient) temp); // add it to the ingredients 
					currentPlayer.addObjCalled(temp.getName());
					currentPlayer.currentRoom.removeItemNamed(temp.getName());
					if(currentPlayer.ingredientsFound() == 3){
						NpcGood druid = new NpcGood("druido","Un misterioso essere alto e imponente, le sue sembianze sono umane ma non puoi fare a meno di notare"
								+ " lesue corna da capra e i suoi penetranti occhi rossi", 999, 150, true, currentPlayer.getName()
								+ "!!! Hai trovato tutti gli ingredienti! Ho sempre creduto in te!<BR>C'e' pero' una cosa che dovresti sapere,"
								+ " tu non sei una persona reale, ti ho evocato per portare a termine la mia missione di salvare il bosco.<BR>E c'e' di piu'... "
								+ "La pozione per curare il bosco ha bisogno di un quarto ingrediente, quest'ultimo e' l'anima dell'evocazione che ha trvato gli altri tre."
								+ " <BR>Mi dispiace...");
						map.getRoom(1).addnpcs(druid); //add the druid to his house
					}
					else if(temp.getName().equals("cuore di dremora") && currentPlayer.getCurrentRoom().getName().equals("OBLIVION")){ //if you take the heart of the dremora
						currentPlayer.setCurrentRoom(map.getRoom(14)); //get back to the wood
						return "Inizi a sentirti debole e perdi i sensi...<BR><BR>" + 
								currentPlayer.getCurrentRoom().getNameAndDescription() + beingattacked();
					}
					return "Oggetto raccolto: " + temp.getName() + beingattacked();
				}
				else {
					return "Impossibile raccogliere: " + temp.getName() + "; prova con il comando 'esamina " + temp.getName() + "'"
							+ beingattacked();
				}
			}
			return "Impossibile prendere: " + command.getSecondWord() + beingattacked();
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
			return "Cosa vorresti buttare? Scrivi 'butta -oggetto-'" + beingattacked();
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
				if(remove.equals("chiave") || remove.equals("passepartout") || remove.equals("torcia") || 
						remove.equals("pezzo di mappa")){ 
					//remove the appropriate command if item dropped
					Command.removeCommand(remove, currentPlayer);
				}
				return "Hai lasciato : " + t.getName() + beingattacked();
			}
		}
		return "Impossibile lasciare: " + command.getSecondWord() + beingattacked();
	}

	/**
	 * examine an item to get the description and/or the tools/money contained
	 * @param command
	 * @return the string to be returned and placed in the text-box
	 */
	public String examineObj(Command command) {
		if (!command.hasSecondWord() || command.getSecondWord().equals("stanza") || command.getSecondWord().equals("area")) {
			return currentPlayer.currentRoom.getNameAndDescription() + beingattacked();
		}
		if(currentPlayer.getCurrentRoom().isDark()){
			return "E' troppo buio per compiere quest'azione.";
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
					ret += "oggetti : " + t.getToolList();

				}
				if (t.getMoney() != 0) {
					if (t.getDescription() != null && t.getToolList().isEmpty())
						ret += "<BR><BR>";
					else if (!t.getToolList().isEmpty())
						ret += "<BR>";
					ret += "monete : " + t.getMoney();
				}
				if(t.getMoney() == 0 && t.getToolList().isEmpty() && t.getDescription() == null){
					ret = t.getName() + " e' vuoto";
				}
				return ret + beingattacked();
			}
		} else if ((npc = currentPlayer.currentRoom.getNPCNamed(command.getSecondWord())) != null) {
			return npc.getDescription();
		}
		return command.getSecondWord()   + ": impossibile da esaminare" + beingattacked();
	}

	/**
	 * attack an enemy (you can only attack NpcBad)
	 * @param command
	 * @return the string to be returned and placed in the text-box
	 */
	public String attack(Command command) {
		if(currentPlayer.getCurrentRoom().isDark()){
			return "E' troppo buio per compiere quest'azione.";
		}
		if (!command.hasSecondWord()) {
			return "Chi vorresti attaccare? Scrivi 'attacca -obiettivo-'";
		}
		NPC enemy;
		if ((enemy = currentPlayer.currentRoom.getNPCNamed(command.getSecondWord())) != null) {
			if (!enemy.isAlive()) {
				return enemy.getName() + " gia' ucciso";
			} else if (enemy.getClass() == NpcBad.class){
					if(enemy.getName().equals("demogorgone") && currentPlayer.getWeapon().getName().equals("ammazza demoni")){
						enemy.setLifeRemaining(0);
						enemy.setDescription("Il Demogorgone ha un collare con su scritti alcuni numeri: 6, 66, 426, 2586");
						return "La tua spada brilla, il "+ enemy.getName() + " comincia a bruciare dall'interno e si trasforma in cenere." +
						"<BR><BR>" + ((NpcBad)enemy).die();
					}
					else if(enemy.getName().equals("lord dremora") && currentPlayer.getWeapon().getName().equals("ammazza demoni")){
						enemy.setLifeRemaining(enemy.getLifeRemaning() - 100);
						if(enemy.getLifeRemaning() < 150){
							return "La tua spada brilla, il "+ enemy.getName() + " comincia a bruciare dall'interno e si trasforma in cenere."+
						"<BR><BR>" + ((NpcBad)enemy).die();
						}
					}
				return currentPlayer.attackTarget(((NpcBad)enemy)) + "<BR><BR>" + ((NpcBad)enemy).attackTarget(currentPlayer);
			}
			else
				return "Impossibile attaccare: " + command.getSecondWord();

		}
		return command.getSecondWord() + " insesistente";
	}

	/**
	 * equip a weapon, only weapons can be equipped. you need to have the weapon in your inventory
	 * @param command
	 * @return the string to be returned and placed in the text-box
	 */
	public String equip(Command command) {
		if (!command.hasSecondWord()) {
			return "Cosa vorresti equipaggiare? Scrivi equipaggia -oggetto-";
		}
		Tool t;
		if ((t = currentPlayer.getToolFromString(command.getSecondWord())) != null) {
			if (t.getClass() == Weapon.class && !currentPlayer.getWeapon().getName().equals(command.getSecondWord())){
				frame.getWeaponLabel().setText(t.getName());
				if (currentPlayer.getWeapon().getName().equals("torcia")) { // put out the torch
					currentPlayer.getWeapon().setDamage(3);
					enlight(currentPlayer.getCurrentRoom());
					currentPlayer.getWeapon().setDescription("Una torcia di legno, qualcuno l'ha gia' usata "
							+ "ma dovrebbe essere ancora possibile accenderla");
					return "Spegni la torcia.<BR>" + currentPlayer.equipWeapon((Weapon) t) + beingattacked();
				}
				return currentPlayer.equipWeapon((Weapon) t) + beingattacked();
			} else {
				return "Non servirebbe a nulla" + beingattacked();
			}
		}
		return command.getSecondWord()+ " non esiste" + beingattacked();
	}

	/**
	 * speak to a character, it behaves differently depending on its class 
	 * @param command
	 * @return
	 */
	public String speakToChar(Command command) {
		if (!command.hasSecondWord()) {
			return "Tu: blablablabla. <BR>Se vuoi parlare con qualcuno scrivi 'parla con -soggetto-'"
					+ beingattacked();
		}
		NPC npc;
		if ((npc = currentPlayer.getCurrentRoom().getNPCNamed(command.getSecondWord())) != null) {
			if (npc.getClass() == NpcGood.class) {
				if (npc.getName().equals("druido") && currentPlayer.getCurrentRoom().getName().equals("CASA DEL DRUIDO - Il salotto")
						&& npc.getHP() == 1000){ // 1000 hp means first time you meet the druid (beginning)
					currentPlayer.getCurrentRoom().removeNpcNamed("druido");
				}
				else if (npc.getName().equals("lorwin")) { //if it's lorwin
					Tool ing;
					if((ing = npc.getToolFromString("piuma di fenice")) != null){
						frame.addItemToMenu((Ingredient)ing);
						String speech = npc.getSpeech();
						npc.setSecondSpeech("Ciao piccolo amico, il segreto per raggiungere la saggezza e' essere consapevole "
								+ "che c'e' sempre qualcosa di nuovo da imparare...");
						return speech + "<BR><BR>" + currentPlayer.addObjCalled(ing.getName());
					}
				}
				else if (npc.getName().equals("vecchio impaurito") && 
						(currentPlayer.getToolFromString("dente di demogorgone")!= null)){
					if (frame.BagFull()) {
						return "Il tuo zaino e' pieno, lascia qualcosa e ritorna!";
					}
					if(npc.isFirstTimeMet()){
						npc.setFirstTimeMet(false);
					}
					npc.setSecondSpeech("Acc..aacc...accipicchia! Hai scon.. sco..sconfitto il demogorgone,"
							+ " l'ho capito dal de..de..dente che porti con t..te!<BR>Tie...tie..tieni, prendi questo regalo, "
							+ "non ti saro' mai abbastanza gra...gra...grato.<BR>Ah, dim...dimenticavo, e' passato di qui uno strano essere"
							+ " con le co...co...corna da capra che ti stava cercando e ha detto qualcosa come <b>fiore di belladonna</b>"
							+ " e <b>cuore del re dell'oblivion</b>."
							+ " Non so proprio cosa volesse dire.<BR>Ora de..de...devo andare,buona fortuna!");
					Tool k = npc.getToolFromString("passepartout"); //a key that opens any door
					npc.removeObjCalled("passepartout");
					currentPlayer.addObj(k);
					frame.addItemToMenu(k);
					currentPlayer.getCurrentRoom().removeNpcNamed("vecchio impaurito");
					//add some enemies:
					NpcBad goblin = new NpcBad("goblin","Un fetido goblin rosso",80,150,true,"Arghaaaaaalllhhh",9);
					goblin.addObj(new Tool("mela", "Una mela verde, sembra acerba", 5));
					map.getRoom(8).addnpcs(goblin);
					map.getRoom(14).addnpcs(new NpcBad("lupo","Un enorme lupo grigio con penetranti occhi gialli",90,40,true,"Grrrrrrrrhhhh",5));
					
					return npc.getSpeech() + "<BR><BR>" + k.getName() + " nuova aggiunta all'inventario." + checkNewCommand(k);
				}
				return npc.getSpeech() + beingattacked();
			} else {
				return npc.getSpeech() + beingattacked();
			}
		}
		return "Impossibile parlare con: " + command.getSecondWord() + beingattacked();
	}

	/**
	 * print the help message containing the possible commands and instructions
	 * @param command
	 * @return the string to be returned and placed in the text-box
	 */
	public String printHelp(Command command) {

		return "<b>I comandi utilizzabili sono:</b> <i>" + command.listCommands() + "</i><BR>"
				+ "I comandi possono essere combinati con altre parole o oggetti (di solito oggetti o personaggi nell'area circostante)."
				+ " Alcuni esempi sono: <b>'attacca il goblin', 'equipaggia coltello', 'esamina', ' esamina baule', 'prendi la chiave',"
				+ " 'vai a nord'.</b> <BR>I comandi possono variare durante il corso della storia.<BR>"
		        + " E' possibile salvare cliccando il tasto 'salva' nell'angolo in basso a sinistra.<BR>Se muori, i tuoi progressi andranno persi.";
	}

	/**
	 * print the welcome message
	 * @param dead : if false prints normal welcome message otherwise prints 'YOU DIED',
	 * it it's you have never saved the game or you are dead it also adds the introduction.
	 */
	public void printWelcome(boolean dead) {
		
		String path = MysticalAdventure.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		File fileIn = new File(path.substring(0, path.lastIndexOf("/") + 1) + "lib/savedGame/savedGameDeath.ser");
		File fileOut = new File(path.substring(0, path.lastIndexOf("/") + 1) + "lib/savedGame/savedGame.ser");
		
		String toChange = "<html><body>";
			
		if(dead){
			toChange += "<p style='font-size: 40px'>SEI MORTO!</p>";
		}
		toChange += "<h1>Benvenuto in 'THE MYSTICAL ADVENTURE'</h1>";
		if(!dead){
			toChange += "<p>Questa e' un'avventura testuale, sarai catapultato in un mondo fantasy dove tu controlli la storia.<BR>"
					+ "Questo significa che userai comandi testuali per controllare le azioni del personaggio protagonista e "
					+ " farlo interagire con l'ambiente in cui si trova.<BR>"
					+ "I comandi vanno inseriti nella barra bianca in basso."
					+ "<BR>Se hai bisogno di aiuto con i comandi scrivi 'aiuto'.</p>";
				 }	
		
			try {
			if (FileUtils.contentEquals(fileIn, fileOut)){
				toChange += "<BR><hr><p>Apri gli occhi... <BR>Ti senti confuso e non riesci bene a capire dove ti trovi, "
						+ "non ricordi nemmeno cosa sia successo nel passato, come se avessi perso ogni ricordo...</p>";
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
		// ------------First check that the savedData is in place----------------------
		String path = MysticalAdventure.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		File fileIn = new File(path.substring(0, path.lastIndexOf("/") + 1) + "lib/savedGame/savedGameDeath.ser");
		File fileOut = new File(path.substring(0, path.lastIndexOf("/") + 1) + "lib/savedGame/savedGame.ser");

		if (!fileIn.exists() || !fileOut.exists()) {

			String err = "<html><body><BR><h2>File di salvataggio non trovati, assicurati di aver estratto la cartella 'MysticalAdventureITA'"
					+ " sul desktop e di aver eseguito il file .jar da dentro quella cartella.</h2></body></html>";
			frame.getPane().setText(err);
			return;
		} else {
			frame.getTextBox().addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Game.THESTACK.reset();
					write();
				}
			});
			printWelcome(false); // if we have the save data then we print welcome message
		}
        //-----------------------------------------------------------------------
		
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

		if (currentPlayer.getWeapon().getName().equals("torcia") && currentPlayer.getWeapon().getDamage() == 5) {
			if (currentR.getName().equals("IL BOSCO - Est")) {
				currentR.setDark(false);
				currentR.setDescription("<i>Gli alberi in questa parte del bosco sono molto fitti,"
						+ " la luce della torcia ti aiuta a vedere meglio</i>");
			} else if (currentR.getName().equals("IL TUNNEL")) {
				currentR.setDark(false);
				currentPlayer.getCurrentRoom().setDescription("<i>La luce della torcia illumina il tunnel</i>");
				map.addPassage(7, 10, "est"); //create passage
				map.addPassage(10, 7, "ovest"); //create passage
			}
			else if(currentR.getName().equals("LA GROTTA DIETRO LA CASCATA")){
				currentR.setDark(false);
				currentR.setDescription("<i>Ti trovi in una caverna fredda ed umida con funghi e dei fiori sul pavimento bagnato.</i>");
			}
			else if(currentR.getName().equals("LA GROTTA DIETRO LA CASCATA - Dietro la porta")){
				currentR.setDark(false);
				currentR.setDescription("<i>Il tetto e' cosi' basso che devi strisciare per poter entrare. "
				+ "Il pavimento e' bagnato e un forte odore di sangue attrae la tua attenzione, noti un cadavere"
				+ " in fondo alla stanza...</i>");
			}
		} else {
			if (currentR.getName().equals("IL BOSCO - Est")) {
				currentR.setDark(true);
				currentR.setDescription("<i>Le chiome degli alberi in questa zona del bosco sono cosi' fitte che "
				+ "la luce non riesce ad attraversarle. Non vedi praticamente nulla.</i>");
			} else if (currentR.getName().equals("IL TUNNEL")) {
				currentR.setDark(true);
				currentPlayer.getCurrentRoom().setDescription("<i>Non riesci a vedere altro che l'ingresso" +
						" del tunnel dietro di te. E' davvero buio!</i>");
			}
			else if(currentR.getName().equals("LA GROTTA DIETRO LA CASCATA")){
				currentR.setDark(true);
				currentR.setDescription("<i>La grotta e' piuttosto buia</i>");
			}
			else if(currentR.getName().equals("LA GROTTA DIETRO LA CASCATA - Dietro la porta")){
				currentR.setDark(true);
				currentR.setDescription("<i>Quest'area e' piuttosto buia</i>");
			}
		}

	}
	
	/**
	 * swap the results of the riddle on the doors and change the length of the codeword
	 * in the riddle so that the result changes.
	 * The numbers on the doors are randomized and change every time you enter the room.
	 */
	private void changeDoors() {
		if (currentPlayer.getCurrentRoom().getName().equals("IL BOSCO - Sud")) {
			Random rand = new Random();
			int code = rand.nextInt(4);
			map.setLorwinCode(code);
			String desc = "La porta e' chiusa, qualcuno ha scritto su di essa il numero ";
			currentPlayer.getCurrentRoom().setDescription("<i>Quest'area e' circondata da pareti rocciose. Sette porte sono poste "
				+ "sul lato a sud e sopra di esse un'incisione ben definita recita: 'Nel codice di Lorwin "
				+ "una sequenza numerica valida contiene solamente cifre e deve contenere lo '0' almeno una volta.<BR>"
				+ "Quante sequenze possibili ci sono in un codice lungo "+ map.getLorwinCodeLength() +" cifre?'</i>");

			for (int i = 1; i < 8; i++) { //for each door
				int num = rand.nextInt(100) + 10; //number to add/subtract from the correct result
				boolean b = rand.nextBoolean(); //to randomize whether to add or subtract the random from the result
				if (b) {
					((Fixed) currentPlayer.getCurrentRoom().getItemNamed("porta " + i))
							.setDescription(desc + (map.getLorwinCodeSolution() + num));
				} else {
					((Fixed) currentPlayer.getCurrentRoom().getItemNamed("porta " + i))
							.setDescription(desc + (map.getLorwinCodeSolution() - num));
				}
			}
			int n = rand.nextInt(7) + 1; //set the correct result on a random door
			correctDoor = n;
			((Fixed) currentPlayer.getCurrentRoom().getItemNamed("porta " + n))
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
		
		if(prev.getName().equals("LA RADURA") && next.getName().equals("CASA DEL DRUIDO - Il salotto"))
		return "Camminando verso la casa alzi gli occhi e noti del fumo uscire dal camino."
				+ " Arrivato davanti alla porta principale, decidi di entrare e apri la porta...<BR><BR>";
		
		if(prev.getName().equals("IL BOSCO - Zona sporca") && next.getName().equals("LA PICCOLA CAPANNA"))
			return "Ti muovi verso la capanna in legno ad ovest e una volta di fronte "
					+ "apri la porta ed entri...<BR><BR>";
		
		if(prev.getName().equals("LA VALLE") && next.getName().equals("LA GROTTA DIETRO LA CASCATA"))
			return "Entri nella fessura di fianco alla cascata e una volta dentro senti una voce che dice:"
					+ " 'SOLO UN FIORE PUO' ESSERE PRESO IN QUESTA GROTTA'<BR><BR>";
		
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
		code += "35b"+ (rand.nextInt(4) + 3);
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
		code += "it";
		for(int i=0; i<3; i++){
			code+= (rand.nextInt(5) + 5);
		}
		
		actione(code);
		
	}
	
	public void createFrame(String code){
		JFrame nf = new JFrame(); //new window with congrats and code
		JTextPane pp = new JTextPane();
		pp.setContentType("text/html");
		pp.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		pp.setEditable(false);
		pp.setText("<html><body style='text-align: center; font-size: 18px;'>"
				+ "<BR><BR><h1 style='color: red;font-size: 22px;'>Complimenti!!!</h1>"
				+ "<p>Hai completato 'The Mystical Adventure'</p>"
				+ "<p>Manda il codice qui in basso, il tuo nome e le ultime parole pronunciate dal druido via E-Mail a giacomobenso94@gmail.com</p><p><b>"
				+ code + "</b></p><p>Se sei tra i primi 10 a finire l'avventura il tuo nome verra' aggiunto sulla hall of fame del sito ufficiale di"
						+ " 'The Mystical Adventure' </p></body></html>");
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


