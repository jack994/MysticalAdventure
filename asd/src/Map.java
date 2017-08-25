import java.io.Serializable;

public class Map implements Serializable{

	private static final long serialVersionUID = 1L;
	private Room[] Rooms;
	int[][] lorwinCode = { {3,672}, {4,4368}, {5,23520}, {6,100800} };
	int currentCode = 1; 

	public Map() {
		Rooms = new Room[50];
	}

	public Room createRoom() {
		Room p = makeRoom();
		setRoomDirections();
		addItemsToRooms();
		return p;
	}

	public Room makeRoom() {

		Rooms[0] = new Room("THE BEDROOM", "you are in a room that looks like a bedroom, some stairs lead "
				+ "back to the Living Room", false);
		Rooms[1] = new Room("THE LIVING ROOM",
				"you are in the living room, all the house seems really tidy and clean. some stairs lead upstairs, "
				+ "south there is the main entrance and east a wooden door", false);
		Rooms[2] = new Room("THE KITCHEN",
				"you are in the kitchen, the light of a lantern shines in the middle of it. west there is "
				+ "the door that leads back to the kitchen.", false);
		Rooms[3] = new Room("THE MEADOW",
				"you are in a misty meadow in the middle of what seems to be a small wood,"
						+ " north, in the middle of the medow you can barely see a house and all around you it's only trees, they are so"
						+ " thick you couldn't even pass through. South one of the trees seems moving.", false);
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
				+ " on the floor. West, a wooden shed",false);
		Rooms[17] = new Room("The Cave beyond the waterfall","The area is quite dark",true); 
		Rooms[18] = new Room("Little Shed","You are in a tiny wooden shed",false);
		Rooms[19] = new Room("Cave beyond the waterfall - behind the door", "the roof is so low you have to crawl to get in. "
				+ "There is a strong smell of moist and the floor is wet.", true);
		Rooms[20] = new Room("THE OBLIVION","The air is thick and warm, you are in a sort of square floating in the middle"
				+ " of nothing, all around this 'square' flames burn like in hell.",false);

		return Rooms[3];
	}

	public void setRoomDirections() {
		Rooms[0].setDirection("down", Rooms[1]);
		Rooms[1].setDirection("east", Rooms[2]);
		Rooms[1].setDirection("south", Rooms[3]);
		Rooms[1].setDirection("up", Rooms[0]);
		Rooms[2].setDirection("west", Rooms[1]);
		Rooms[3].setDirection("north", Rooms[1]);
		Rooms[4].setDirection("north", Rooms[3]);
		Rooms[4].setDirection("east", Rooms[5]);
		Rooms[5].setDirection("west", Rooms[4]);
		Rooms[5].setDirection("north", Rooms[6]);
		Rooms[6].setDirection("south", Rooms[5]);
		Rooms[6].setDirection("north", Rooms[7]);
		Rooms[7].setDirection("south", Rooms[6]);
		Rooms[4].setDirection("west", Rooms[8]);
		Rooms[8].setDirection("east", Rooms[4]);
		Rooms[8].setDirection("west", Rooms[9]);
		Rooms[9].setDirection("east", Rooms[8]);
		Rooms[4].setDirection("south", Rooms[11]);
		Rooms[11].setDirection("north", Rooms[4]);
		Rooms[8].setDirection("south", Rooms[14]);
		Rooms[14].setDirection("north", Rooms[8]);
		Rooms[8].setDirection("north", Rooms[15]);
		Rooms[15].setDirection("south", Rooms[8]);
		Rooms[15].setDirection("west", Rooms[16]);
		Rooms[16].setDirection("east", Rooms[15]);
		Rooms[16].setDirection("west", Rooms[18]);
		Rooms[18].setDirection("east", Rooms[16]);
		Rooms[19].setDirection("west", Rooms[17]);
	}

	public void addItemsToRooms() {

		Fixed wardrobe = new Fixed("wardrobe", null);
		wardrobe.addMoney(100);
		wardrobe.addTool(new Weapon("torch", "a wooden torch, someone has already used it. "
				+ "it should still be possible to light it up with the correct tool", 5, 3, 0.90f));
		Rooms[0].addFixed(wardrobe);
		Fixed bed = new Fixed("bed", "someone has slept here");
		Rooms[0].addFixed(bed);

		NpcGood druid = new NpcGood("druid", "a busy druid with enormous horns", 1000, 150, true, "Aaawww finally you woke up! I was waiting for you");
		druid.setSecondSpeech("Look, the forest is dying, I need your help to "
				+ "find three ingredients to make a potion that will heal it, the creatures of the forest will tell you which ingredients I need."
				+ " You should be able to get Lorwin to give you the first one, he's a friend of mine."
				+ " Sorry if i can't tell you anything more but I have to go, come back to me when you have the three ingredients.<BR><BR>"
				+ " The Druid runs towards the exit door.<BR><BR><b>druid :</b> ah, I almost forgot, treants like riddles and the number 7!");
		Rooms[1].addnpcs(druid);
		Fixed chest1 = new Fixed("chest", null);
		chest1.addMoney(150);
		chest1.addTool(new Weapon("knife", "A shiny knife with some blood on the blade, it's still fresh", 50, 8, 0.98f));
		Rooms[1].addFixed(chest1);
		Rooms[1].addTool(new Tool("salt", "It's just some tablesalt", 5));

		Fixed cupboard = new Fixed("cupboard", null);
		cupboard.addMoney(50);
		cupboard.addTool(new Tool("apple", "A green apple, it seems still ripe", 2));
		Rooms[2].addFixed(cupboard);

		NpcGood treant = new NpcGood("treant", "an enormous alive tree that blocks the passage to the wood", 100, 130,
				false, "Pa ohz illu hyvbuk mvy tpsspvuz vm flhyz, iba pa pz uv tvyl aohu h tvuao vsk. Doha pz pa?");
		Rooms[3].addnpcs(treant);
		
		Merchant merchant1 = new Merchant("merchant", "a little man almost looking like a gnome with a huge bag on his back",
				100, 300, "Greetings traveller, would you like to buy anything?", 3);
		Weapon whip = new Weapon("whip", "a long brown whip", 80, 12, 0.95f);
		Weapon gladius = new Weapon("gladius", "a golden ancient gladius, f****** sharp", 200, 16, 0.98f);
		Tool apple = new Tool("apple", "a green apple, seems quite ripe", 5);
		merchant1.addObj(whip);
		merchant1.addObj(gladius);
		for(int i = 0; i< 5; i++){
			Tool potion = new Tool("potion", "A little flusk with a green liquid inside it", 50);
			merchant1.addObj(potion);
		}
		merchant1.addObj(apple);
		Rooms[4].addnpcs(merchant1);
		
		Fixed chest2 = new Fixed("chest", null);
		chest2.addMoney(100);
		chest2.addTool(new Tool("potion", "A little flusk with a green liquid inside it", 50));
		Rooms[5].addFixed(chest2);
		
		NpcBad goblin = new NpcBad("goblin pyromaniac", "a red goblin with an axe in fire in his"
				+ " hands and some flasks hanging from his belt", 45, 100, true, "Grwaaal", 5);
		Weapon axe = new Weapon("pyromaniac axe", "a dirty axe, it smells like smoke", 50, 6, 0.97f);
		goblin.setWeapon(axe);
		Tool eyeG = new Tool("goblin eye", "a disgusting goblin eye", 8);
		Tool matches = new Tool("matches", "few matches in a box, the goblin might have used this to light up his fires,"
				+ " they can be helpful", 8);
		goblin.addObj(eyeG);
		goblin.addObj(matches);
		Rooms[9].addnpcs(goblin);
		
		Fixed corp = new Fixed("floating corp", "a pale corp floating on the water");
		corp.addTool(new Tool("key", "a golden key",0));
		Rooms[10].addFixed(corp);
		NpcBad imp = new NpcBad("putrid imp","a horrid fetid creature looking like a flying goblin", 30, 25, true, 
				"Grawam-Gragnam ahraaa", 9);
		Rooms[10].addnpcs(imp);
		
		Fixed Door1 = new Fixed("door 1", "The door is locked, on it someone wrote the number " + (lorwinCode[currentCode][1] +35));
		Fixed Door2 = new Fixed("door 2", "The door is locked, on it someone wrote the number " + (lorwinCode[currentCode][1] +47));
		Fixed Door3 = new Fixed("door 3", "The door is locked, on it someone wrote the number " + (lorwinCode[currentCode][1] -20));
		Fixed Door4 = new Fixed("door 4", "The door is locked, on it someone wrote the number " + (lorwinCode[currentCode][1]));
		Fixed Door5 = new Fixed("door 5", "The door is locked, on it someone wrote the number " + (lorwinCode[currentCode][1] +140));
		Fixed Door6 = new Fixed("door 6", "The door is locked, on it someone wrote the number " + (lorwinCode[currentCode][1] +100));
		Fixed Door7 = new Fixed("door 7", "The door is locked, on it someone wrote the number " + (lorwinCode[currentCode][1] -120));
		NpcGood randomGuy = new NpcGood("elder man","an old man with a long white beard",100,100,false,"I think I"
				+ " know the solution but I don't have the key to open the door!");
		Rooms[11].addnpcs(randomGuy);
		Rooms[11].addFixed(Door1);
		Rooms[11].addFixed(Door2);
		Rooms[11].addFixed(Door3);
		Rooms[11].addFixed(Door4);
		Rooms[11].addFixed(Door5);
		Rooms[11].addFixed(Door6);
		Rooms[11].addFixed(Door7);
		
		NpcGood lorwin = new NpcGood("lorwin", "a tall pale elf wearing a green vest", 100, 150, true, "Good job my friend, Good job, "
				+ "I understimated you.<BR>My name is Lowrwin and I am the sorcerer of the wood. I am also a close friend of the druid and he"
				+ " told me you would have came here for the plum, he also informed me about your "
				+ "cleverness so I made that riddle complicated to make shure no one else but you could come to my house,"
				+ " the wood it's filled with evil creatures.");
		lorwin.setSecondSpeech("This one I am giving to you is a plum of the legendary phoenix"
				+ " of the wood, it is really rare, don't lose it I only have one of those.<BR>"
				+ "ah, thre is something you might need in the chest beside me, take it if you want.");
		Ingredient plum = new Ingredient("phoenix plum","a fire red plum once appartaining to a phoenix");
		lorwin.addObj(plum);
		Rooms[13].addnpcs(lorwin);
		Fixed chest3 = new Fixed("chest", null);
		chest3.addMoney(300);
		chest3.addTool(new Weapon("demon-slayer", "an ancient sword used to defeat demons", 500, 10, 0.99f));
		Rooms[13].addFixed(chest3);
		
		NpcBad demogorgon = new NpcBad("demogorgon", "A horrid massive reptilian Demon with two mandrill heads and long"
				+ " tentacles.", 500, 100, false, "Grwaaaaahhhlll", 50);
		Tool demoTooth = new Tool("demogorgon tooth", "A tooth of the legendary Demogorgon",0);
		demogorgon.addObj(demoTooth);
		Rooms[14].addnpcs(demogorgon);
		Fixed chest6 = new Fixed("chest", null);
		chest6.addMoney(350);
		chest6.addTool(new Weapon("long sword", "A long sharp shiny sword", 150, 20, 0.97f));
		chest6.addTool(new Tool("apple", "a green apple, seems quite ripe", 5));
		Rooms[14].addFixed(chest6);
		
		NpcGood helper = new NpcGood("scared man","a man with fear in his eyes", 100, 150, false, "I almost died down"
				+ " there!! that demogorgon is so scary! I would give you a big prize if you defeat it for me!");
		Tool passParTout = new Tool("passepartout","a key that could open any door",1000);
		helper.addObj(passParTout);
		Rooms[15].addnpcs(helper);
		Fixed CDoor = new Fixed("door", "the door is locked");
		Rooms[15].addFixed(CDoor);
		
		NpcBad imp1 = new NpcBad("putrid imp","a horrid fetid creature looking like a flying goblin", 50, 25, true, 
				"Grawam-Gragnam ahraaa", 10);
		Rooms[16].addnpcs(imp1);
		Fixed chest4 = new Fixed("chest", null);
		chest4.addMoney(150);
		Tool scroll = new Tool("scroll","an ancient scroll, on it someone wrote: <BR><i>'where the water"
				+ " roars the trees have ears, their favourite number they want to hear.'</i>", 10);
		chest4.addTool(scroll);
		
		Ingredient belladonna = new Ingredient("belladonna flower", "A beautiful white flower");
		Rooms[17].addIngredient(belladonna);
		Rooms[17].addTool(new Tool("hibiscus flower", "A beautiful red flower", 2));		
		Fixed chest5 = new Fixed("chest", null);
		chest5.addMoney(50);
		chest5.addTool(new Tool("potion","A little flusk with a green liquid inside it", 50));
		Rooms[17].addFixed(chest5);
		Fixed cDoor = new Fixed("door", "a tiny door of the size of a gnome, the door is locked");
		NpcBad ooze = new NpcBad("acidic ooze","a disgusting green gelly ooze", 40, 100, false, "mluuawaaa",5);
		Rooms[17].addnpcs(ooze);
		Rooms[17].addFixed(cDoor);
		
		Merchant merchant2 = new Merchant("merchant", "a little man almost looking like a gnome with a huge bag on his back",
				100, 300, "Greetings traveller, would you like to buy anything?", 2);
		Weapon excalibur = new Weapon("excalibur", "The famous legendary sword of king Arthur", 500, 30, 0.99f);
		for(int i = 0; i< 10; i++){
			Tool potion = new Tool("potion", "A little flusk with a green liquid inside it", 50);
			merchant2.addObj(potion);
		}
		Tool apple1 = new Tool("apple", "a green apple, seems quite ripe", 5);
		Tool apple2 = new Tool("apple", "a green apple, seems quite ripe", 5);
		merchant2.addObj(excalibur);
		merchant2.addObj(apple1);
		merchant2.addObj(apple2);
		Rooms[18].addnpcs(merchant2);
		
		Fixed gnome = new Fixed("dead gnome", "A dead gnome, from the smell it does not look "
				+ "like he was dead a long time ago.");
		Tool message = new Tool("message", "A sort of scroll with something badly written on it, the person who"
				+ " wrote it must have been in a hurry:<BR><i>'The Lord Dremora is coming to kill me, vindicate me, the only way to "
				+ "find him is to pronounce the next number of the numerical succession that a Demon has written on his collar over his dead body.'</i>",0);
		gnome.addMoney(50);
		gnome.addTool(message);
		Rooms[19].addFixed(gnome);
		
		NpcBad dremora = new NpcBad("lord dremora", "a scary dark demon with human-ish shape, small black horns and a huge"
				+ " sword",500,0,true,"How you dare to come to my kingdom, you insolent little human, you will die for your "
						+ " stupidity!",5);
		Ingredient heart = new Ingredient("dremora heart", "the hearth of the king of demons, it's still pulsing");
		dremora.addObj(heart);
		Weapon sw = new Weapon("demoniac sword","the sword of the king of demons, a unique and legendary piece", 1000, 15, 0.98f);
		dremora.setWeapon(sw);
		Rooms[20].addnpcs(dremora);
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
	
	public Room getRoom(int roomNumber){
		return Rooms[roomNumber];
	}
}