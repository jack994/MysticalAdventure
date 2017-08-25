import java.io.Serializable;

public class Map implements Serializable{

	private static final long serialVersionUID = 1L;
	private Room[] Rooms;
	int[][] lorwinCode = { {3,672}, {4,4368}, {5,23520}, {6,100800} };
	int currentCode = 1; 

	public Map() {
		Rooms = new Room[100];
	}

	public Room createRoom() {
		Room p = makeRoom();
		setRoomDirections();
		addItemsToRooms();
		return p;
	}

	public Room makeRoom() {

		Rooms[0] = new Room("CAMERA", "sei in una stanza che sembra una camera da letto, alcune scale portano "
				+ "indietro nel salotto", false);
		Rooms[1] = new Room("SALOTTO",
				"sei nel salotto, tutta la casa sembra pulita e ordinata. alcune scale portano al piano superiore, "
				+ "mentre a sud c'e' l'ingresso e ad est una porta di legno", false);
		Rooms[2] = new Room("CUCINA",
				"sei in cucina, the light of a lantern shines in the middle of it. west there is "
				+ "the door thet leads back to the kitchen.", false);
		Rooms[3] = new Room("LA RADURA",
				"you are in a misty meadow in the middle of what seems to be a small wood,"
						+ " north, in the middle of the medow you can barely see a house and all around you it's only trees, they are so"
						+ " thick you could't even pass through. South one of the trees seems moving.", false);
		Rooms[4] = new Room("THE WOOD - Entrance", "you are in the area of the wood close to the meadow, the ligtht "
				+ "is still passing through the leaves and the air is humid. all around you there are only trees.", false);
		Rooms[5] = new Room("THE WOOD - East",
				"the trees in this part of the wood are thicker and you struggle to see anything in this area.", true);
		Rooms[6] = new Room("THE VALLEY",
				"in the middle of this area there is a river and all around flowers grow luxuriant. east you "
						+ "can see a waterfall. North, a dark entrance looks like a tunnel.", false);
		Rooms[7] = new Room("THE TUNNEL",
				"You can see nothing but the entrance of the tunnel behind you. it's really dark.", true);
		Rooms[8] = new Room("THE WOOD - West", "This part of the wood looks more trashy compared to the others, you "
				+ "notice a burning smell and some trees around look completly burnt.", false);
		Rooms[9] = new Room("THE CAVE", "You are in a small cave, the burning smell here is stronger.", false);
		Rooms[10] = new Room("THE SWAMP","a stinky flooded swamp full of bugs, judging from the stink there could "
				+ "be someone dead here", false); 
		Rooms[11] = new Room("THE WOOD - South", "This area is surrounded by rock walls, five doors are on the south "
				+ "side of the area, above them a well defined engraving says: 'In the Lorwin code a "
				+ "valid codeword does not contain any digit more than once and "
				+ "cannot contain both 0 and 1 in the same codeword.<BR> "
				+ "How many possible strings of "+ lorwinCode[currentCode][0] +" digits are there?'", false);
		Rooms[12] = new Room("INCORRECT", "Die Insect!!!!", false);
		Rooms[13] = new Room("Lorwin's Home", "The house is minimal and tidy, in the middle of the room there is a "
				+ "tiny bed and a small table is placed beside it. A lot of books are piled up on the other"
				+ " side of the room, some of them look like magic tomes", false);
		Rooms[14] = new Room("Demo's Den","The area is quite dark but not enough to impede your sight, there is an unusual"
				+ " smell of death... ",false);
		Rooms[15] = new Room("THE WOOD - Quiet area","This area is extremely quiet and pleasant, some birds sing...",false);
		Rooms[16] = new Room("THE WOOD - Dirty spot","This area is surrounded by trees, a lot of trash is spreaded"
				+ " on the floor.",false);
		Rooms[17] = new Room("TO BE CONTINUED","",true);  ///////// TODO finish here.

		return Rooms[3];
	}

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
		Rooms[4].setDirection("sud", Rooms[11]);
		Rooms[11].setDirection("nord", Rooms[4]);
		Rooms[8].setDirection("sud", Rooms[14]);
		Rooms[14].setDirection("nord", Rooms[8]);
		Rooms[8].setDirection("nord", Rooms[15]);
		Rooms[15].setDirection("sud", Rooms[8]);
		Rooms[15].setDirection("ovest", Rooms[16]);
		Rooms[16].setDirection("est", Rooms[15]);
		
	}

	public void addItemsToRooms() {

		Fixed wardrobe = new Fixed("guardaroba", null);
		wardrobe.addMoney(100);
		wardrobe.addTool(new Weapon("torcia", "una torcia di legno gia' usata da qualcuno. "
				+ "dovrebbe ancora essere possibile accenderla", 5, 3, 0.90f));
		Rooms[0].addFixed(wardrobe);
		Fixed bed = new Fixed("letto", "qualcuno ha dormito qui");
		Rooms[0].addFixed(bed);

		NpcGood druid = new NpcGood("druido", "a busy druid with enormous horns", 1000, 150, true, "Aaawww finalmente ti sei svegliato! Ti stavo aspettando");
		druid.setSecondSpeech("Guarda, la foresta sta morendo, ho bisogno del tuo aiuto! "
				+ "Trova i tre ingredienti necesari a fare una pozione che la possa salvare, le creature della foresta ti indicheranno quali servono."
				+ " Devi convincere il Lorwin a farti dare il primo, e' un mio amico."
				+ " Mi dispiace non poterti essere piu' d'aiuto ma devo andare, torna qui quando avrai i tre ingredienti.<BR><BR>"
				+ " Il Druido corre verso la porta d'uscita.<BR><BR><b>druid :</b> Ah, quasi dimenticavo, il treant ama gli indovinelli e il numero 7!");
		Rooms[1].addnpcs(druid);
		Fixed chest1 = new Fixed("baule", null);
		chest1.addMoney(150);
		chest1.addTool(new Weapon("coltello", "Un coltello lucido con del sangue sulla lama, sembra fresco", 50, 8, 0.98f));
		Rooms[1].addFixed(chest1);
		Rooms[1].addTool(new Tool("sale", "E' solo sale da tavola", 5));

		Fixed cupboard = new Fixed("armadio", null);
		cupboard.addMoney(50);
		cupboard.addTool(new Tool("mela", "Una mela verde, sembra matura", 2));
		Rooms[2].addFixed(cupboard);

		NpcGood treant = new NpcGood("treant", "Un enorme albero vivo che blocca il passaggio verso il bosco", 100, 130,
				false, "Pa ohz illu hyvbuk mvy tpsspvuz vm flhyz, iba pa pz uv tvyl aohu h tvuao vsk. Doha pz pa?");//-----------------------------
		Rooms[3].addnpcs(treant);
		
		Merchant merchant1 = new Merchant("mercante", "Un piccolo uomo simile ad uno gnomo con una grande sacca sulla schiena",
				100, 300, "Salve viaggiatore, vuoi comprare qualcosa?", 3);
		Weapon whip = new Weapon("frusta", "una lunga frusta marrone", 80, 12, 0.96f);
		Tool potion1 = new Tool("pozione", "Una piccola boccetta con del liquido verde dentro", 50);
		Tool potion2 = new Tool("pozione", "Una piccola boccetta con del liquido verde dentro", 50);
		Tool apple = new Tool("mela", "Una mela verde, sembra matura", 5);
		merchant1.addObj(whip);
		merchant1.addObj(potion1);
		merchant1.addObj(potion2);
		merchant1.addObj(apple);
		Rooms[4].addnpcs(merchant1);
		
		Fixed chest2 = new Fixed("baule", null);
		chest2.addMoney(100);
		chest2.addTool(new Tool("pozione", "Una piccola boccetta con del liquido verde dentro", 50));
		Rooms[5].addFixed(chest2);
		
		NpcBad goblin = new NpcBad("goblin piromane", "Un goblin rosso con un'ascia infuocata nelle sue"
				+ " mani e delle boccette appese alla cintura", 45, 45, true, "Grwaaal", 5);
		Weapon axe = new Weapon("ascia infuocata", "Un'ascia sporca chepuzza di fumo", 50, 6, 0.97f);
		goblin.setWeapon(axe);
		Tool eyeG = new Tool("occhio di goblin", "Un disgustoso occhio di goblin", 8);
		Tool matches = new Tool("fiammiferi", "Alcuni fiammiferi in una scatoletta, il goblin deve averli usati per accendere il fuoco", 8);
		goblin.addObj(eyeG);
		goblin.addObj(matches);
		Rooms[9].addnpcs(goblin);
		
		Fixed corp = new Fixed("corpo galleggiante", "Un pallido corpo che galleggia dell'acqua");
		corp.addTool(new Tool("chiave", "una chiave d'oro",0));
		Rooms[10].addFixed(corp);
		NpcBad imp = new NpcBad("putrid imp","a horrid fetid creature looking like a flying goblin", 30, 25, true,//------------------------------- 
				"Grawam-Gragnam ahraaa", 12);
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
		
		NpcGood lorwin = new NpcGood("lorwin", "Un elfo alto e pallido che indossa una veste verde", 100, 150, true, "Bel lavoro amico, complimenti! "
				+ "Ti avevo sottovalutato.<BR>Il mio nome e' Lowrwin e sono lo stregone del bosco. Il druido mi ha detto della tua "
				+ "intelligenza and I made that riddle complicated to make shure no one else but you could come to my house,"//-------------------------
				+ " the wood it's filled with evil creatures.");
		lorwin.setSecondSpeech("Quella che sto per darti e' la leggendaria piuma della fenice"
				+ "del bosco. E' estremamente rara. Mi raccomando non perderla, ho solo questa.<BR>"
				+ "Ah, c'e' qualcosa che potrebbe esserti utile nel baule dietro me, prendi cio' che ti serve.");
		Ingredient plum = new Ingredient("piuma di fenice","una piuma rossa apartenente ad una fenice");
		lorwin.addIngredient(plum);
		Rooms[13].addnpcs(lorwin);
		Fixed chest3 = new Fixed("baule", null);
		chest3.addMoney(100);
		chest3.addTool(new Weapon("uccisore di demoni", "Un'antica spada usata per sconfiggere i demoni", 500, 10, 0.99f));
		Rooms[13].addFixed(chest3);
		
		NpcBad demogorgon = new NpcBad("demogorgone", "A horrid massive reptilian Demon with two mandrill heads and long"//------------------------
				+ " tentacles.", 500, 100, false, "Grwaaaaahhhlll", 50);
		Tool demoTooth = new Tool("dente di demogorgone", "Un dente del leggendario demogorgone",0);
		demogorgon.addObj(demoTooth);
		Rooms[14].addnpcs(demogorgon);
		
		NpcGood helper = new NpcGood("uomo impaurito","Un uomo con la paura negli occhi", 100, 150, false, "Sono quasi morto"
				+ " laggiu'!! Quel demogorgone e' cosi' spaventoso! Ti daro' una grande ricompensa se lo ucciderai per me!");
		Tool passParTout = new Tool("passepartout","Una chiave che puo' aprire qualsiasi porta",1000);
		helper.addObj(passParTout);
		Rooms[15].addnpcs(helper);
		Fixed CDoor = new Fixed("porta", "La porta e' chiusa");
		Rooms[15].addFixed(CDoor);
		
		NpcBad imp1 = new NpcBad("putrid imp","a horrid fetid creature looking like a flying goblin", 30, 25, true,//------------------------------ 
				"Grawam-Gragnam ahraaa", 12);
		Rooms[16].addnpcs(imp1);
		Fixed chest4 = new Fixed("baule", null);
		chest4.addMoney(50);
		Tool scroll = new Tool("pergamena","un'antica pergamena, qualcuno ci ha scritto: <BR>'where the water"
				+ " roars the trees have ears, their favourite number they want to hear.'", 10);//--------------------------------------------------
		chest4.addTool(scroll);
		
		
	}

	public boolean hasRoomCalled(String des) {
		int i = 0;

		while (i <= Rooms.length) {
			if (Rooms[i].getDescription().equals(des)) {
				return true;
			}
			i++;
		}
		return false;
	}

	public void addPassage(int roomNumber, int roomConnection, String name) {
		Rooms[roomNumber].setDirection(name, Rooms[roomConnection]);
	}

	public int getLorwinCodeSolution() {
		return lorwinCode[currentCode][1];
	}
	
	public int getLorwinCodeLength(){
		return lorwinCode[currentCode][0];
	}

	public void setLorwinCode(int n) {
		currentCode = n;
	}
}