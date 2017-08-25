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
	public final Weapon NN = new Weapon("nessuna", "nessun'arma", 0, 1, 0.5f);
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
				if(currentPlayer.getCurrentRoom().getName().equals("SBAGLIATO") || currentPlayer.getLifeRemaining() <=0){
					currentPlayer.die();
				}
		}
	}

	public String processCommand(Command command) {
		String firstWord = command.getFirstWord();

		if (firstWord.equals("vai")) {
			return goRoom(command);
		} else if (firstWord.equals("aiuto")) {
			return printHelp(command);
		} else if (firstWord.equals("prendi")) {
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
		} else if (firstWord.equals("usa")){
			return useItem(command);
		} else if (firstWord.equals("compra")){
			return buyFromMerchant(command);
		} else
			return "Non puoi usare questo comando, o non esiste o non e' lo strumento appropriato"
					+ " da usare.<BR>Puoi vedere i comandi utilizzabili scrivendo 'aiuto'.";
	}

	/**
	 * checks if the tool picked up creates a new commandWord
	 * 
	 * @param t
	 */
	public void checkNewCommand(Tool t) {
		if (t.getName().equals("fiammiferi")) {
			Command.addCommand("accendi");
		}
		if (t.getName().equals("chiave")) {
			Command.addCommand("apri");
		}
	}
	
	public String buyFromMerchant(Command command){
		Merchant merchant;
		if((merchant = currentPlayer.getCurrentRoom().getMerchant()) == null){
			return "in questa zona non e' presente il mercante";
		}
		if(!command.hasSecondWord()){
			return "cosa vorresti comprare? scrivi 'compra -oggetto-'";
		}
		Tool t;
		if((t = currentPlayer.buyToolFromMerchant(merchant, command.getSecondWord())) != null){
			currentPlayer.removeMoney(t.getValue() * merchant.getPriceModifier());
			merchant.removeObjCalled(t.getName());
			currentPlayer.addObj(t);
			frame.addItemToMenu(t);
			return "oggetto comprato: " + command.getSecondWord();
		}
		else{
			return "impossibile comprare: " + command.getSecondWord();
		}
	}
	
	public String useItem(Command command){
		if(!command.hasSecondWord()){
			return "cosa vorresti usare? scrivi 'usa -oggetto-'";
		}
		if(command.getSecondWord().equals("pozione") && currentPlayer.getToolFromString("pozione") != null){
			int life;
			if((currentPlayer.getHP() - currentPlayer.getLifeRemaining()) > 50){
				currentPlayer.setLifeRemaining(life = currentPlayer.getLifeRemaining() + 50);
				GameWindow.greenLabelsCounter = life;
				frame.resetLifelabel();
				currentPlayer.removeObjCalled("pozione");
				frame.removeItemFromMenu("pozione");
				return "50 HP recuperati";
			}	
			else{
				int healed = currentPlayer.getHP() - currentPlayer.getLifeRemaining();
				currentPlayer.setLifeRemaining(life = currentPlayer.getHP());
				GameWindow.greenLabelsCounter = life;
				frame.resetLifelabel();
				currentPlayer.removeObjCalled("pozione");
				frame.removeItemFromMenu("pozione");
				return healed + " HP recuperati";
			}
		}
		return "Non puoi usare questo oggetto in questo momento";
	}

	public String lightUp(Command command) {
		if (currentPlayer.getToolFromString("fiammiferi") == null) {
			return "non puoi accendere niente senza l'oggetto appropriato" + beingattacked();
		}
		if (!command.hasSecondWord()) {
			return "cosa vuoi accendere? <BR>scrivi 'accendi -oggetto-'"+ beingattacked();
		}
		if (currentPlayer.getWeapon().getName().equals("torcia") && currentPlayer.getWeapon().getDamage() == 5) {
			return "la torcia e' gia' stata accesa"+ beingattacked();
		}
		if (command.getSecondWord().equals("torcia") || command.getSecondWord().equals("la torcia")) {
			if (currentPlayer.getToolFromString("torcia") != null) {
				if (currentPlayer.getCurrentRoom().getName().equals("BOSCO - Est")
						|| currentPlayer.getCurrentRoom().getName().equals("TUNNEL")) {
					return "non si vede niente! prova a farlo dove c'e' un po' di luce"+ beingattacked();
				} else {
					if (currentPlayer.getWeapon().getName().equals("torcia")) {
						currentPlayer.getWeapon().setDamage(5); // damage
																// increased by
																// fire (also
																// new
																// functionality)
						currentPlayer.getWeapon().setDescription("la fiamma della torcia brilla al buio");
						return "hai acceso la torcia"+ beingattacked();
					}
					return "la torcia deve essere prima equipaggiata"+ beingattacked();
				}
			}
			return "non stai portando nessuna torcia"+ beingattacked();
		}
		return "impossibile accendere: " + command.getSecondWord()+ beingattacked();
	}

	public String openDoor(Command command) {
		Tool ky;
		if ((ky =currentPlayer.getToolFromString("chiave")) == null) {
			return "non puoi aprire niente senza la chiave appropriata"+ beingattacked();
		}
		if (!command.hasSecondWord()) {
			return "cosa vuoi aprire? <BR>scrivi 'apri -oggetto-'"+ beingattacked();
		}
		if (currentPlayer.getCurrentRoom().getName().equals("BOSCO - Sud")) {
			if (command.getSecondWord().equals("porta") || command.getSecondWord().equals("la porta"))
				return "quale vuoi aprire? scrivi 'apri porta -numero-'";
			String[] tmp = command.getSecondWord().split(" ");
			if (tmp[0].equals("porta") && StringUtils.isNumeric(tmp[1])) {
				if (Integer.parseInt(tmp[1]) < 8 && Integer.parseInt(tmp[1]) > 0) {
					if(Integer.parseInt(tmp[1]) == correctDoor){
						map.addPassage(11, 13, "sud");
						map.addPassage(13, 11, "nord");
					}
					else{
						map.addPassage(11, 12, "sud");
					}
					frame.removeItemFromMenu(ky.getName());    //remove the key
					currentPlayer.getCurrentRoom().getItemNamed("porta " + tmp[1]).setDescription("La porta e' aperta");
					currentPlayer.removeObjCalled(ky.getName());
					return "hai aperto la porta numero " + tmp[1] + ", la chiave e' sparita magicamente."
							+ "<BR>il passaggio e' ora aperto (sud)";
				} else
					return "la porta non esiste";
			}
		}		
			if (command.getSecondWord().equals("porta") || command.getSecondWord().equals("la porta") 
					&& currentPlayer.getToolFromString("passepartout") != null){
				if (currentPlayer.getCurrentRoom().getName().equals("BOSCO - Area tranquilla")) {
					currentPlayer.getCurrentRoom().getItemNamed("porta").setDescription("La porta e' aperta");
					map.addPassage(15, 16, "ovest");
					map.addPassage(16, 15, "est");
					return "hai aperto la porta, il passaggio e' ora aperto (ovest)";
				}
			}
					
		return "impossibile aprire: " + command.getSecondWord()+ beingattacked();
	}

	public String saySomething(Command command) {
		if ((command.getSecondWord().equals("luna") || command.getSecondWord().equals("la luna"))
				&& currentPlayer.getCurrentRoom().getName().equals("LA RADURA")) { // riddle solved//---------------------------------------------
			NPC treant;
			if(!(treant = currentPlayer.getCurrentRoom().getNPCNamed("treant")).getSpeech().equals("Bel lavoro"
					+ " con l'indovinello, ora puoi passare.")){
				map.addPassage(3, 4, "sud");
				treant.setSpeech("Bel lavoro con l'indovinello, ora puoi passare.");
				return "il treant si muove lentamente verso sinistra liberando il passaggio davanti al quale era seduto (sud).";
			}else{
				return "hai gia' aperto il passaggio";
			}
		}
		else if((command.getSecondWord().equals("7") || command.getSecondWord().equals("sette"))
				&& currentPlayer.getCurrentRoom().getName().equals("VALLE")){
			currentPlayer.getCurrentRoom().setDescription("in mezzo a quest'area c'e' un fiume"
					+ " e tutto intorno la vegetazione cresce rigogliosa. Ad est si vede una cascata con una piccola entrata accanto. "
					+ "A nord c'e' un ingersso buio, sembra un tunnel.");
				map.addPassage(6, 17, "est");
				map.addPassage(17, 6, "ovest");
				return "due alberi sopra la cascata si muovono leggermente e il flusso di essa cambia."
						+ " un nuovo passaggio (est) e' stato scoperto";
		}
		else {
			return "non succede niente"+ beingattacked();
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
			toReturn = "non puoi andare in quella direzione!";
		}
		return toReturn;
	}

	public String pickTool(Command command) {
		if (frame.BagFull()) {
			return "Lo zaino e' pieno";
		}
		Item temp;
		if (!command.hasSecondWord()) {
			return "cosa vuoi prendere?";
		} else if (command.getSecondWord().equals("monete")) {
			for (Item f : currentPlayer.getCurrentRoom().getItemsArray()) {
				if (f.getClass() == Fixed.class) {
					if (((Fixed) f).hasBeenOpened() && ((Fixed) f).getMoney() > 0) {
						int mon;
						currentPlayer.addMoney(mon = ((Fixed) f).getMoney());
						((Fixed) f).removeAllMoney();
						return "monete aggiunte: " + mon;
					}
				}
			}
			return "non ci sono monete da prendere";
		} else {
			if ((temp = currentPlayer.getCurrentRoom().getItemNamed(command.getSecondWord())) != null) {
				if (temp.getClass() == Tool.class || temp.getClass() == Weapon.class) {
					currentPlayer.addObjCalled(temp.getName());
					frame.addItemToMenu((Tool) temp);
					currentPlayer.currentRoom.removeItemNamed(command.getSecondWord());
					checkNewCommand((Tool) temp);
					return "hai raccolto: " + temp.getName() + beingattacked();
				} else {
					return "impossibile raccogliere: " + temp.getName() + " prova con il comando 'esamina " + temp.getName() + "'"
							+ beingattacked();
				}
			}
			return "impossibile vedere: " + command.getSecondWord() + beingattacked();
		}
	}

	public String dropTool(Command command) {
		Tool t = currentPlayer.getToolFromString(command.getSecondWord());
		if (!command.hasSecondWord())
			return "cosa vuoi buttare?" + beingattacked();
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
				if((remove.equals("chiave") || remove.equals("torcia"))){
					Command.removeCommand(remove);
				}
				return "hai lasciato cadere: " + t.getName() + beingattacked();
			}
		}
		return "impossibile portare: " + command.getSecondWord() + beingattacked();
	}

	public String examineObj(Command command) {
		if (!command.hasSecondWord() || command.getSecondWord().equals("stanza")) {
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
					ret += "oggetti contenuti: " + t.getToolList();
				}
				if (t.getMoney() != 0) {
					if (t.getDescription() != null && t.getToolList().isEmpty())
						ret += "<BR><BR>";
					else if (!t.getToolList().isEmpty())
						ret += "<BR>";
					ret += "soldi contenuti: " + t.getMoney();
				}
				if(t.getMoney() == 0 && t.getToolList().isEmpty() && t.getDescription() == null){
					ret = t.getName() + " e' pieno";
				}
				return ret + beingattacked();
			}
		} else if ((npc = currentPlayer.currentRoom.getNPCNamed(command.getSecondWord())) != null) {
			return npc.getDescription();
		}
		return command.getSecondWord()   + ": impossibile da esaminare" + beingattacked();
	}

	public String attack(Command command) {
		if (!command.hasSecondWord()) {
			return "chi vuoi attaccare? scrivi 'attacca -obiettivo-'";
		}
		NPC enemy;
		if ((enemy = currentPlayer.currentRoom.getNPCNamed(command.getSecondWord())) != null) {
			if (!enemy.isAlive()) {
				return enemy.getName() + " gia' ucciso";
			} else if (enemy.getClass() == NpcBad.class){
					if(enemy.getName().equals("demogorgone") && currentPlayer.getWeapon().getName().equals("uccisore di demoni")){
						enemy.setLifeRemaining(0);
						return enemy.getName() + " comincia a bruciare dall'interno e muore."+ "<BR>" + enemy.die();
					}
				return currentPlayer.attackTarget(enemy) + "<BR><BR>" + enemy.attackTarget(currentPlayer);
			}
			else
				return "impossibile attaccare: " + command.getSecondWord();
		}
		return command.getSecondWord() + " insesistente";
	}

	public String equip(Command command) {
		if (!command.hasSecondWord()) {
			return "cosa vuoi equipaggiare?";
		}
		Tool t;
		if ((t = currentPlayer.getToolFromString(command.getSecondWord())) != null) {
			if (t.getClass() == Weapon.class) {
				frame.getWeaponLabel().setText(t.getName());
				if (currentPlayer.getWeapon().getName().equals("torcia")) { // put
																			// out
																			// the
																			// torch
					currentPlayer.getWeapon().setDamage(3);
					currentPlayer.getWeapon().setDescription("una torcia di legno, qualcuno l'ha gia' usata "
							+ "ma dovrebbe essere ancora possibile accenderla");
				}
				return currentPlayer.equipWeapon((Weapon) t) + beingattacked();
			} else {
				return "sarebbe inutile" + beingattacked();
			}
		}
		return "There is no " + command.getSecondWord() + beingattacked();//-----------------------------------------------------------------------
	}

	public String speakToChar(Command command) {
		if (!command.hasSecondWord()) {
			return "tu: blablablabla. <BR>se vuoi parlare con qualcuno scrivi 'parla con <soggetto>'"
					+ beingattacked();
		}
		NPC npc;
		if ((npc = currentPlayer.getCurrentRoom().getNPCNamed(command.getSecondWord())) != null) {
			if (npc.getClass() == NpcGood.class) {
				if (npc.getName().equals("druid") && currentPlayer.getCurrentRoom().getName().equals("SOGGIORNO")){
					currentPlayer.getCurrentRoom().removeNpcNamed("druid");
				}
				else if (npc.getName().equals("lorwin")) {
					Ingredient ing;
					if((ing = npc.getIngredient("pium di fenice")) != null){
						npc.removeIngredient(ing);
						frame.addIngredientToMenu(ing);
						String speech = npc.getSpeech();
						npc.setSecondSpeech("Ciao mio piccolo amico, sai, non smetti mai di imparare.");
						return speech + currentPlayer.addIngredient(ing);
					}
				}
				else if (npc.getName().equals("uomo spaventato") && 
						(currentPlayer.getToolFromString("Dente di demogorgon")!= null)) {
					npc.setSecondSpeech("Holy Guacamole hai sconfitto il demogorgon,"
							+ " lo si vede dal dente che stai portando!<BR>Tieni, prendi questo regalo, "
							+ "non sarò mai abbastanza grato.<BR>Ora devo andare,buona fortuna! .");//-------------------see you around------------------------
					Tool k = npc.getToolFromString("passepartout");
					npc.removeObjCalled("passepartout");
					currentPlayer.addObj(k);
					frame.addItemToMenu(k);
					checkNewCommand(k);
					currentPlayer.getCurrentRoom().removeNpcNamed("uomo spaventato");
					return npc.getSpeech() + "<BR><BR>" + k.getName() + " aggiunto all'inventario di  " + 
					currentPlayer.getName();
				}
				return npc.getSpeech() + beingattacked();
			} else {
				return npc.getSpeech() + beingattacked();
			}
		}
		return "you can't speak to " + command.getSecondWord() + beingattacked();//----------------------------------------------------------------
	}

	public String printHelp(Command command) {

		return "<b>I comandi utilizzabili sono:</b> <i>" + command.listCommands() + "</i><BR>"
				+ "i comandi possono essere combinati con altre parole o oggetti (di solito oggetti presi dentro le stanze)."
				+ " Alcuni esempi sono : <b>'vai a nord', 'esamina', ' esamina baule', 'prendi la chiave', 'attacca il goblin',"
				+ " 'equipaggia coltello'.</b> <BR>P.S. : I comandi possono variare durante il corso della storia";

	}

	public void printWelcome(boolean dead) {
		String toChange = "<html><body>";
		if(dead){
			toChange += "<p style='font-size: 40px'>YOU DIED!</p>";
		}
		toChange += "<h1>Benvenuto ne 'L'AVVENTURA MISTICA'</h1>" + "<p>"//--------------------------------------------------------------------------
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

		if (currentPlayer.getWeapon().getName().equals("torcia") && currentPlayer.getWeapon().getDamage() == 5) {
			if (currentR.getName().equals("BOSCO - Est")) {
				currentR.setDark(false);
				currentR.setDescription("gli alberi in questa parte del bosco sono fitti"
						+ " ma la luce della tua torcia ti aiuta a vedere meglio");
			} else if (currentR.getName().equals("TUNNEL")) {
				currentR.setDark(false);
				currentPlayer.getCurrentRoom().setDescription("La luce della torcia illumina il tunnel");
				map.addPassage(7, 10, "est");
				map.addPassage(10, 7, "ovest");
			}
		} else {
			if (currentR.getName().equals("BOSCO - Est")) {
				currentR.setDark(true);
				currentR.setDescription("gli alberi in questa parte del bosco sono fitti e tu devi dimenarti"//----------------------------------------
						+ " per cercare di vedere qualcosa");
			} else if (currentR.getName().equals("TUNNEL")) {
				currentR.setDark(true);
				currentPlayer.getCurrentRoom().setDescription(
						"Non puoi vedere altro che l'ingresso" + " del tunnel dietro di te. E' davvero buio!");
			}
		}

	}

	public void changeDoors() {
		if (currentPlayer.getCurrentRoom().getName().equals("BOSCO - Sud")) {
			Random rand = new Random();
			int code = rand.nextInt(4);
			map.setLorwinCode(code);
			String desc = "La porta e' chiusa, qualcuno ci ha scritto un numero sopra ";
			currentPlayer.getCurrentRoom().setDescription("Quest'area e' circondata da pareti rocciose. Sette porte sono poste "//-----------------
				+ "verso il lato a sud e sopra un'incisione ben definita recita: 'In the Lorwin code a "
				+ "valid codeword does not contain any digit more than once and "
				+ "cannot contain both 0 and 1 in the same codeword.<BR>"
				+ "How many possible strings of "+ map.getLorwinCodeLength() +" digits are there?'");
			

			for (int i = 1; i < 8; i++) {
				int num = rand.nextInt(100) + 10;
				boolean b = rand.nextBoolean();
				if (b) {
					((Fixed) currentPlayer.getCurrentRoom().getItemNamed("porta " + i))
							.setDescription(desc + (map.getLorwinCodeSolution() + num));
				} else {
					((Fixed) currentPlayer.getCurrentRoom().getItemNamed("porta " + i))
							.setDescription(desc + (map.getLorwinCodeSolution() - num));
				}
			}
			int n = rand.nextInt(7) + 1;
			correctDoor = n;
			((Fixed) currentPlayer.getCurrentRoom().getItemNamed("porta " + n))
					.setDescription(desc + map.getLorwinCodeSolution());
		}
	}
}
