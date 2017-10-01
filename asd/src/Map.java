import java.io.Serializable;

/**
 * class holding the whole map with all the rooms, items and characters in it
 * @author giacomobenso
 */
public class Map implements Serializable{

	private static final long serialVersionUID = 1L;
	private Room[] Rooms;
	int[][] lorwinCode = { {3,672}, {4,4368}, {5,23520}, {6,100800} };
	int currentCode = 1; 

	public Map() {
		Rooms = new Room[22];
	}
	
	public void setLorwinCode(int n) {
		currentCode = n;
	}
	
	public Room getRoom(int roomNumber){
		return Rooms[roomNumber];
	}

	/**
	 * method needed in the class game, it sets up all the rooms
	 * @return
	 */
	public Room createRoom() {
		Room p = makeRoom();
		setRoomDirections();
		addItemsToRooms();
		return p;
	}

	/**
	 * create all the rooms in the map with roomname, description and a boolean representing the darkness
	 * @return
	 */
	public Room makeRoom() {
		Rooms[0] = new Room("CASA DEL DRUIDO - La camera da letto", "Sei in una camera da letto, al centro un letto disfatto con la testiera in legno."
				+ " vicino c'e' un guardaroba anch'esso in legno. Delle scale portano al piano di sotto", false);
		Rooms[1] = new Room("CASA DEL DRUIDO - Il salotto",
				"Ti trovi nel salotto della casa, una stanza pulita e ordinata. Delle scale portano al piano superiore "
				+ "mentre a sud c'e' l'ingresso e ad est una porta di legno", false);
		Rooms[2] = new Room("CASA DEL DRUIDO - La cucina",
				"Ti trovi in cucina, la luce di una lanterna illumina la stanza. Ad ovest c'e' "
				+ "una porta dalla quale e' possibile ritornare in salotto.", false);
		Rooms[3] = new Room("LA RADURA",
				"Ti trovi in una piccola radura nebbiosa nel bel mezzo di quello che pare un bosco."
						+ " A nord si intravede una casa; gli alberi intorno a te"
						+ " sono molto fitti ed impossibili da oltrepassare. A sud un albero sembra muoversi.", false);
		Rooms[4] = new Room("IL BOSCO - Entrata", "Ti trovi nell'area del bosco vicino alla radura, la luce "
				+ "passa attraverso le foglie e l'aria e' umida. Intorno a te vedi solo alberi ed in lontananza una donna seduta dietro"
				+ " ad un banco pieno di oggetti, probabilmente un mercante.", false);
		Rooms[5] = new Room("IL BOSCO - Est",
				"Le chiome degli alberi in questa zona del bosco sono cosi' fitte che "
				+ "la luce non riesce ad attraversarle. Non vedi praticamente nulla.", true);
		Rooms[6] = new Room("LA VALLE",
				"Ti trovi in una valle, gli alberi sono meno presenti in questa zona che, attraversata da un ruscello,"
					+ " da vita ad una vegetazione ricca di fiori e piccole piante. Ad est si vede una cascata."
					+ "A nord c'e' un ingersso buio, sembra un tunnel.", false);
		Rooms[7] = new Room("IL TUNNEL",
				"Non riesci a vedere altro che l'ingresso del tunnel dietro di te. E' davvero buio!", true);
		Rooms[8] = new Room("IL BOSCO - Ovest", "Questa parte del bosco sembra piu' sporca rispetto alle altre, "
				+ "non puoi fare a meno di notare un forte odore di fumo. Alcuni alberi attorno a te sembrano bruciati.", false);
		Rooms[9] = new Room("LA GROTTA", "Ti trovi in una piccola grotta, l'odore di fumo e' molto forte qui.", false);
		Rooms[10] = new Room("LA PALUDE","Una palude puzzolente e piena di insetti, sull'acqua in lontananza si intravede un uomo, a giudicare pero'"
				+ " dalla puzza che c'e' qui e' probailmente morto", false); 
		Rooms[11] = new Room("IL BOSCO - Sud", "Quest'area e' circondata da pareti rocciose. Sette porte sono poste "
				+ "sul il lato a sud e sopra di esse un'incisione ben definita recita: 'Nel codice di Lorwin "
				+ "una sequenza numerica valida non contiene mai un numero (da 0 a 9) ripetuto piu' di una volta "
				+ "e le cifre 0 e 1 non possono essere presenti contemporaneamente nella sequenza.<BR>"
				+ "Quante sequenze possibili ci sono in un codice lungo "+ lorwinCode[currentCode][0] +" cifre?'", false);
		Rooms[12] = new Room("SBAGLIATO", "Muori insetto!!!!", false);
		Rooms[13] = new Room("CASA DI LORWIN", "La casa e' piccola e ordinata, nel mezzo della stanza c'e' un "
				+ "grosso letto con accanto un piccolo tavolo. Molti libri posti uno sopra l'altro"
				+ " si trovano dall'altra parte della stanza, alcuni di questi assomigliano a dei tomi magici", false);
		Rooms[14] = new Room("IL RIFUGIO DEL DEMOGORGONE","La zona e' buia ma non abbastanza per impedirti di vedere, c'e' anche un insolito"
				+ " odore di morte... ",false);
		Rooms[15] = new Room("IL BOSCO - Area tranquilla","Quest'area e' estremamente tranquilla e piacevole, alcuni uccellini cinguettano felici..."
				+ " Ad ovest un'immensa parete rocciosa con al centro una porta",false);
		Rooms[16] = new Room("IL BOSCO - Zona sporca","Quest'area e' circondata da alberi, c'e' un sacco di sporcizia"
				+ " sparsa per terra. Ad ovest, una piccola capanna di legno",false);
		Rooms[17] = new Room("LA GROTTA DIETRO LA CASCATA","Quest'area e' molto buia, non si vede niente!",true); 
		Rooms[18] = new Room("LA PICCOLA CAPANNA","Sei in una piccola capanna di legno. Di fronte a te, un uomo che sembra un mercante si "
				+ " riscalda di fronte al caminetto.",false);
		Rooms[19] = new Room("LA GROTTA DIETRO LA CASCATA - Dietro la porta", "Il tetto e' cosi' basso che devi strisciare per entrare. "
				+ "C'e' un forte odore di umido e il pavimento e' bagnato.", true);
		Rooms[20] = new Room("L'OBLIVION","L'aria e' calda e pesante, sei su un grande piano di roccia fluttuante"
				+ " nel mezzo del nulla, tutt'intorno a questa roccia le fiamme ardono come all'inferno.",false);
		Rooms[21] = new Room("IL GIARDINO DIETRO CASA DI LORWIN","Un piccolo giardino con erba verde ed un piccolo orto nell'angolo "
				+ "a destra. Nel mezzo e' seduto un enorme cane bianco a tre teste",false);

		return Rooms[3];
	}
	
	/**
	 * connect the rooms among them with directions.
	 */

	public void setRoomDirections() {
		Rooms[0].setDirection("sotto", Rooms[1]);
		Rooms[1].setDirection("est", Rooms[2]);
		Rooms[1].setDirection("sud", Rooms[3]);
		Rooms[1].setDirection("sopra", Rooms[0]);
		Rooms[2].setDirection("ovest", Rooms[1]);
		Rooms[3].setDirection("nord", Rooms[1]);
		Rooms[4].setDirection("nord", Rooms[3]);
		Rooms[4].setDirection("est", Rooms[5]);
		Rooms[5].setDirection("ovest", Rooms[4]);
		Rooms[5].setDirection("nord", Rooms[6]);
		Rooms[6].setDirection("sud", Rooms[5]);
		Rooms[6].setDirection("nord", Rooms[7]);
		Rooms[7].setDirection("sud", Rooms[6]);
		Rooms[4].setDirection("ovest", Rooms[8]);
		Rooms[8].setDirection("est", Rooms[4]);
		Rooms[8].setDirection("ovest", Rooms[9]);
		Rooms[9].setDirection("est", Rooms[8]);
		Rooms[13].setDirection("sud", Rooms[21]);
		Rooms[21].setDirection("nord", Rooms[13]);
		Rooms[4].setDirection("sud", Rooms[11]);
		Rooms[11].setDirection("nord", Rooms[4]);
		Rooms[8].setDirection("sud", Rooms[14]);
		Rooms[14].setDirection("nord", Rooms[8]);
		Rooms[8].setDirection("nord", Rooms[15]);
		Rooms[15].setDirection("sud", Rooms[8]);
		Rooms[15].setDirection("ovest", Rooms[16]);
		Rooms[16].setDirection("est", Rooms[15]);
		Rooms[16].setDirection("ovest", Rooms[18]);
		Rooms[18].setDirection("est", Rooms[16]);
		Rooms[19].setDirection("ovest", Rooms[17]);
	}

	public void addItemsToRooms() {

		Fixed wardrobe = new Fixed("guardaroba", null);
		wardrobe.addMoney(100);
		wardrobe.addTool(new Weapon("torcia", "Una torcia di legno gia' usata da qualcuno. "
				+ "Dovrebbe ancora essere possibile accenderla", 5, 3, 0.90f));
		Rooms[0].addFixed(wardrobe);
		Fixed bed = new Fixed("letto", "Qualcuno ha dormito qui");
		Rooms[0].addFixed(bed);

		NpcGood druid = new NpcGood("druido", "Un misterioso essere alto e imponente, le sue sembianze sono umane ma non puoi fare a meno di notare"
								+ " lesue corna da capra e i suoi penetranti occhi rossi", 1000, 150, true, "Aaahh finalmente ti sei svegliato Eldor! Ti stavo aspettando");
		druid.setSecondSpeech("Ascolta, il bosco sta morendo, ho bisogno del tuo aiuto! "
				+ "Devi trovare i tre ingredienti necesari a creare la pozione da usare per curarlo. Le creature della foresta ti sapranno dire quali sono."
				+ " Lorwin dovrebbe essere in grado di darti il primo, e' un mio amico."
				+ " Mi dispiace non poterti dire di piu' ma devo scappare, torna qui quando avrai i tre ingredienti.<BR><BR>"
				+ " lo strano essere si dirige verso la porta d'uscita.<BR><BR><b>druido :</b> Ah, quasi dimenticavo... I treant sono strani alberi parlanti,"
				+ " amano gli indovinelli, ma soprattutto il numero 7!");
		Rooms[1].addnpcs(druid);
		Fixed chest1 = new Fixed("baule", null);
		chest1.addMoney(150);
		chest1.addTool(new Weapon("coltello", "Un coltello lucido con del sangue sulla lama, sembra fresco", 50, 8, 0.98f));
		Rooms[1].addFixed(chest1);
		Rooms[1].addTool(new Tool("sale", "Del semplice sale da cucina", 5));

		Fixed cupboard = new Fixed("armadio", null);
		cupboard.addMoney(50);
		cupboard.addTool(new Tool("mela", "Una mela verde, sembra matura", 2));
		Rooms[2].addFixed(cupboard);

		NpcGood treant = new NpcGood("treant", "Un enorme albero parlante che blocca il passaggio verso il bosco", 100, 130,
				false, "L PU NPYV KH TPSPVUP KP HUUP, TH UVU L WPB CLJJOPH KP BU TLZL?");
		Rooms[3].addnpcs(treant);
		
		Merchant merchant1 = new Merchant("mercante", "Una bella donna con lunghe trecce bionde",
				100, 300, " Salve viaggiatore, vuoi comprare qualcosa?", 3);
		Weapon whip = new Weapon("frusta", "Una lunga frusta marrone", 80, 12, 0.95f);
		Weapon gladius = new Weapon("gladio", "Un'antico gladio d'oro. C**** e' affilatissimo!", 200, 16, 0.98f);
		Tool apple = new Tool("mela", "Una mela verde, sembra matura", 5);
		merchant1.addObj(whip);
		merchant1.addObj(gladius);
		for(int i = 0; i< 5; i++){
			Tool potion = new Tool("pozione", "Una piccola boccetta con del liquido verde dentro", 50);
			merchant1.addObj(potion);
		}
		merchant1.addObj(apple);
		Rooms[4].addnpcs(merchant1);
		
		Fixed chest2 = new Fixed("baule", null);
		Tool mapPiece1 = new Tool("pezzo di mappa","Un pezzo di una mappa, probabilmente non e' lunico in giro",0);
		chest2.addTool(mapPiece1);
		chest2.addMoney(100);
		chest2.addTool(new Tool("pozione", "Una piccola boccetta con del liquido verde dentro", 50));
		Rooms[5].addFixed(chest2);

		Fixed ccp = new Fixed("cadavere","Un cadavere umano, guardandolo meglio si notano gli occhi azzurri ghiaccio");
		Rooms[8].addFixed(ccp);
		
		NpcBad goblin = new NpcBad("goblin piromane", "Un goblin rosso con un'ascia infuocata tra le"
				+ " mani e delle boccette colorate appese alla cintura", 80, 100, true, "Grwaaal", 5);
		Weapon axe = new Weapon("ascia del piromane", "Un'ascia sporca che puzza di fumo", 50, 6, 0.97f);
		goblin.setWeapon(axe);
		Tool eyeG = new Tool("occhio di goblin", "Un disgustoso occhio di goblin", 8);
		Tool matches = new Tool("fiammiferi", "Alcuni fiammiferi in una scatoletta, il goblin deve averli usati per accendere il fuoco."
				+ " Potrebbero tornare utili", 8);
		goblin.addObj(eyeG);
		goblin.addObj(matches);
		Rooms[9].addnpcs(goblin);
		
		Fixed corp = new Fixed("cadavere galleggiante", "Un cadavere che galleggia sull'acqua, guardando piu' da vicino si notano gli occhi azzurro ghiaccio");
		corp.addTool(new Tool("chiave", "Una chiave d'oro",0));
		Rooms[10].addFixed(corp);
		NpcBad imp = new NpcBad("demonietto putrido","Un'orrida creatura nera simile ad un goblin con le ali", 70, 25, true, 
				"Grawam-Gragnam ahraaa", 9);
		Rooms[10].addnpcs(imp);
		
		Fixed Door1 = new Fixed("porta 1", "La porta e' chiusa, qualcuno ci ha inciso il numero " + (lorwinCode[currentCode][1] +35));
		Fixed Door2 = new Fixed("porta 2", "La porta e' chiusa, qualcuno ci ha inciso il numero " + (lorwinCode[currentCode][1] +47));
		Fixed Door3 = new Fixed("porta 3", "La porta e' chiusa, qualcuno ci ha inciso il numero " + (lorwinCode[currentCode][1] -20));
		Fixed Door4 = new Fixed("porta 4", "La porta e' chiusa, qualcuno ci ha inciso il numero " + (lorwinCode[currentCode][1]));
		Fixed Door5 = new Fixed("porta 5", "La porta e' chiusa, qualcuno ci ha inciso il numero " + (lorwinCode[currentCode][1] +140));
		Fixed Door6 = new Fixed("porta 6", "La porta e' chiusa, qualcuno ci ha inciso il numero " + (lorwinCode[currentCode][1] +100));
		Fixed Door7 = new Fixed("porta 7", "La porta e' chiusa, qualcuno ci ha inciso il numero " + (lorwinCode[currentCode][1] -120));
		NpcGood randomGuy = new NpcGood("uomo anziano","Un uomo anziano con una lunga barba bianca",100,100,false,"Penso"
				+ " di sapere la soluzione ma non ho la chiave per aprire la porta!");
		Rooms[11].addnpcs(randomGuy);
		Rooms[11].addFixed(Door1);
		Rooms[11].addFixed(Door2);
		Rooms[11].addFixed(Door3);
		Rooms[11].addFixed(Door4);
		Rooms[11].addFixed(Door5);
		Rooms[11].addFixed(Door6);
		Rooms[11].addFixed(Door7);

		NpcGood lorwin = new NpcGood("lorwin", "Un elfo alto e pallido che indossa una tunica verde", 100, 150, true, "Bel lavoro amico, complimenti! "
				+ "Ti avevo sottovalutato.<BR>Il mio nome e' Lowrwin e sono lo stregone del bosco. Sono un caro amico del druido e "
				+ " mi ha detto che saresti venuto da me a cercare la piuma; mi ha anche parlato della tua "
				+ "intelligenza, cosi' ho fatto un indovinello abbastanza complicato per essere sicuro che nessun'altro eccetto te sarebbe entrato a casa mia, "
				+ " il bosco e' pieno di creature malvagie.");
		lorwin.setSecondSpeech("Quella che sto per darti e' la leggendaria piuma della fenice"
				+ "del bosco. E' estremamente rara. Mi raccomando non perderla, ho solo questa.<BR>"
				+ "Ah, c'e' qualcosa che potrebbe esserti utile nel baule qua di fianco a me, prendi pure.");
		Ingredient plum = new Ingredient("piuma di fenice","Una piuma rossa apartenente ad una fenice");
		lorwin.addObj(plum);
		Rooms[13].addnpcs(lorwin);
		Fixed chest3 = new Fixed("baule", null);
		Tool mapPiece2 = new Tool("pezzo di mappa","Un pezzo di una mappa, probabilmente non e' lunico in giro",0);
		chest3.addTool(mapPiece2);
		chest3.addMoney(300);
		chest3.addTool(new Weapon("ammazza demoni", "Un'antica spada usata per sconfiggere i demoni", 500, 10, 0.99f));
		Rooms[13].addFixed(chest3);
		
		NpcBad demogorgon = new NpcBad("demogorgone", "Uno spaventoso demone con due teste di mandrillo e dei lunghi"
				+ " tentacoli.", 1000, 100, true, "Grwaaaaahhhlll", 20);
		Tool demoTooth = new Tool("dente di demogorgone", "Un dente del leggendario demogorgone",0);
		demogorgon.addObj(demoTooth);
		Rooms[14].addnpcs(demogorgon);
		Fixed chest6 = new Fixed("baule", null);
		chest6.addMoney(350);
		chest6.addTool(new Weapon("spada lunga", "Una lunga e affilata spada di acciaio", 150, 20, 0.97f));
		chest6.addTool(new Tool("mela", "Una mela verde, sembra acerba", 5));
		Rooms[14].addFixed(chest6);
		
		NpcGood helper = new NpcGood("vecchio impaurito","Un uomo con la paura negli occhi", 100, 150, false, "So...so..sono quasi morto"
				+ " laggiu'!! Quel de...dee..demogorgone e' cosi' spaventoso! Ti da..daro' una grande ricompensa se lo u..uuu..ucciderai per me!");
		Tool passParTout = new Tool("passepartout","Una chiave che puo' aprire qualsiasi porta standard",1000);
		helper.addObj(passParTout);
		Rooms[15].addnpcs(helper);
		Fixed CDoor = new Fixed("porta", "La porta e' chiusa");
		Rooms[15].addFixed(CDoor);
		
		NpcBad imp1 = new NpcBad("demonietto putrido","Un'orrida creatura nera simile ad un goblin volante", 70, 25, true, 
				"Grawam-Gragnam ahraaa", 10);
		Rooms[16].addnpcs(imp1);
		Fixed chest4 = new Fixed("baule", null);
		chest4.addMoney(150);
		Tool scroll = new Tool("pergamena","Un'antica pergamena, su di essa qualcuno ha scritto: <BR><i>'Dove l'acqua"
				+ " scroscia agli alberi piace origliare, il loro numero preferito vogliono sentire.'</i>", 10);//sentire
		chest4.addTool(scroll);
		Rooms[16].addFixed(chest4);
		
		Ingredient belladonna = new Ingredient("fiore di belladonna", "Un grazioso fiore bianco");
		Rooms[17].addIngredient(belladonna);
		Rooms[17].addTool(new Tool("fiore di ibisco", "Un grazioso fiore rosso", 2));		
		Fixed chest5 = new Fixed("baule", null);
		chest5.addMoney(50);
		Tool mapPiece3 = new Tool("pezzo di mappa","Un pezzo di una mappa, probabilmente non e' lunico in giro",0);
		chest5.addTool(mapPiece3);
		chest5.addTool(new Tool("pozione", "Una piccola boccetta con del liquido verde dentro", 50));
		Rooms[17].addFixed(chest5);
		Fixed cDoor = new Fixed("porta", "Una piccolissima porta che sembra fatta apposta per gli gnomi. Oh, e' chiusa!");
		NpcBad ooze = new NpcBad("melma acida","Una disgustosa e gelatinosa melma verde che emana un odore nauseabondo", 100, 500, false, "mluuawaaa",8);
		Rooms[17].addnpcs(ooze);
		Rooms[17].addFixed(cDoor);
		
		Merchant merchant2 = new Merchant("mercante", "Un piccolo uomo con una lunga barba con una grande sacca sulla schiena",
				100, 300, " Salve viaggiatore, vuoi comprare qualcosa?", 2);
		Weapon excalibur = new Weapon("lungartiglio", "La famosa spada del re del nord'", 500, 30, 0.99f);
		for(int i = 0; i< 10; i++){
			Tool potion = new Tool("pozione", "Una piccola boccetta con del liquido verde dentro", 50);
			merchant2.addObj(potion);
		}
		Tool apple1 = new Tool("mela", "Una mela verde, sembra acerba", 5);
		Tool apple2 = new Tool("mela", "Una mela verde, sembra acerba", 5);
		merchant2.addObj(excalibur);
		merchant2.addObj(apple1);
		merchant2.addObj(apple2);
		Rooms[18].addnpcs(merchant2);
		
		Fixed gnome = new Fixed("gnomo morto", "Uno gnomo morto, dal sangue fresco sul pavimento non sembra sia "
				+ "morto da molto.");
		Tool message = new Tool("messaggio", "Una pergamena con qualcosa scritto sopra, a giudicare dalla scrittura la persona che"
				+ " l'ha scritta doveva essere di fretta:<BR><i>'Il Lord Dremora sta venendo ad uccidermi, vendicatemi! L'unico modo "
				+ "per trovarlo e' pronunciare il numero successivo della successione numerica che un Demone ha scritto sul collare dove giace il suo corpo morto.'</i>",0);
		gnome.addMoney(50);
		gnome.addTool(message);
		Rooms[19].addFixed(gnome);
		
		NpcBad dremora = new NpcBad("lord dremora", "Uno spaventoso demone dalle sembianze umane, con delle piccole corna nere e una gigantesca"
				+ " spada rosso scuro",1500,0,true,"Come osi venire nel mio regno, piccolo umano insolente! Morirai per la tua "
						+ " arroganza!",5);
		Ingredient heart = new Ingredient("cuore di dremora", "Il cuore del re dei demoni, sta ancora battendo");
		dremora.addObj(heart);
		Weapon sw = new Weapon("spada demoniaca","La spada del re dei demoni, un pezzo unico e leggendario", 1000, 16, 0.98f);
		dremora.setWeapon(sw);
		Rooms[20].addnpcs(dremora);
		
		NpcBad hound = new NpcBad("segugio di Lorwin","Un imponente cane bianco con tre teste, dev'essere di Lorwin",500,1000,true,
				 				"Grrrrrrrhhhh...",15);
		Fixed vegGarden = new Fixed("orto","Un piccolo orto ben curaato, Lorwin deve avere il pollice verde");
			Tool car = new Tool("carota","Una piccola carota arancione",5);
			Tool rasp = new Tool("lampone", "Un succoso lampone rosso",5);
			vegGarden.addTool(car);
			vegGarden.addTool(rasp);
		Rooms[21].addFixed(vegGarden);
		Rooms[21].addnpcs(hound);
	}

	/**
	 * create a new connection between two rooms
	 * @param roomNumber: from 
	 * @param roomConnection: to
	 * @param name: name of the connection (direction)
	 */
	public void addPassage(int roomNumber, int roomConnection, String name) {
		Rooms[roomNumber].setDirection(name, Rooms[roomConnection]);
	}

	public int getLorwinCodeSolution() {
		return lorwinCode[currentCode][1];
	}
	
	public int getLorwinCodeLength(){
		return lorwinCode[currentCode][0];
	}
}
