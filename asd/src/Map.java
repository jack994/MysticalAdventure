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

		Rooms[0] = new Room("THE BEDROOM", "you are in a room that looks like a bedroom, some stairs lead "
				+ "back to the Living Room", false);
		Rooms[1] = new Room("THE LIVING ROOM",
				"you are in the living room, all the house seems really tidy and clean. some stairs lead upstairs, "
				+ "south there is the main entrance and east a wooden door", false);
		Rooms[2] = new Room("THE KITCHEN",
				"you are in the kitchen, the light of a lantern shines in the middle of it. west there is "
				+ "the door thet leads back to the kitchen.", false);
		Rooms[3] = new Room("THE MEADOW",
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
		
	}

	public void addItemsToRooms() {

		Fixed wardrobe = new Fixed("wardrobe", null);
		wardrobe.addMoney(100);
		wardrobe.addTool(new Weapon("torch", "a wooden torch, someone has already used it. "
				+ "it should still be possible to light it up", 5, 3, 0.90f));
		Rooms[0].addFixed(wardrobe);
		Fixed bed = new Fixed("bed", "someone has slept here");
		Rooms[0].addFixed(bed);

		NpcGood druid = new NpcGood("druid", "a busy druid with enormous horns", 1000, 150, true, "Aaawww finally you woke up! I was waiting for you");
		druid.setSecondSpeech("Look, the forest is dying, I need your help to "
				+ "find three ingredients to make a potion that will heal it, the creatures of the forest will tell you which ingredients i need."
				+ " sorry if i can't tell you anything more but I have to go, come back to me when you have the three ingredients."
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
		Weapon whip = new Weapon("whip", "a long brown whip", 80, 12, 0.96f);
		Tool potion1 = new Tool("potion", "a little flusk with a green liquid inside it", 50);
		Tool potion2 = new Tool("potion", "a little flusk with a green liquid inside it", 50);
		Tool apple = new Tool("apple", "a green apple, seems quite ripe", 5);
		merchant1.addObj(whip);
		merchant1.addObj(potion1);
		merchant1.addObj(potion2);
		merchant1.addObj(apple);
		Rooms[4].addnpcs(merchant1);
		
		Fixed chest2 = new Fixed("chest", null);
		chest2.addMoney(100);
		chest2.addTool(new Tool("potion", "a little flusk with a green liquid inside it", 50));
		Rooms[5].addFixed(chest2);
		
		NpcBad goblin = new NpcBad("goblin pyromaniac", "a red goblin with an axe in fire in his"
				+ " hands and some flasks hanging from his belt", 45, 45, true, "Grwaaal", 1);
		Weapon axe = new Weapon("pyromaniac axe", "a dirty axe, it smells like smoke", 50, 6, 0.97f);
		goblin.setWeapon(axe);
		Tool eyeG = new Tool("goblin eye", "a disgusting goblin eye", 8);
		Tool matches = new Tool("matches", "few matches in a box, the goblin might have used this to light up his fires", 8);
		goblin.addObj(eyeG);
		goblin.addObj(matches);
		Rooms[9].addnpcs(goblin);
		
		Fixed corp = new Fixed("floating corp", "a pale corp floating on the water");
		corp.addTool(new Tool("key", "a golden key",0));
		Rooms[10].addFixed(corp);	
		
		Fixed Door1 = new Fixed("door 1", "The door is locked, on it someone wrote the number " + (lorwinCode[currentCode][1] +35));
		Fixed Door2 = new Fixed("door 2", "The door is locked, on it someone wrote the number " + (lorwinCode[currentCode][1] +47));
		Fixed Door3 = new Fixed("door 3", "The door is locked, on it someone wrote the number " + (lorwinCode[currentCode][1] -20));
		Fixed Door4 = new Fixed("door 4", "The door is locked, on it someone wrote the number " + (lorwinCode[currentCode][1]));
		Fixed Door5 = new Fixed("door 5", "The door is locked, on it someone wrote the number " + (lorwinCode[currentCode][1] +140));
		Fixed Door6 = new Fixed("door 6", "The door is locked, on it someone wrote the number " + (lorwinCode[currentCode][1] +100));
		Fixed Door7 = new Fixed("door 7", "The door is locked, on it someone wrote the number " + (lorwinCode[currentCode][1] -120));
		Rooms[11].addFixed(Door1);
		Rooms[11].addFixed(Door2);
		Rooms[11].addFixed(Door3);
		Rooms[11].addFixed(Door4);
		Rooms[11].addFixed(Door5);
		Rooms[11].addFixed(Door6);
		Rooms[11].addFixed(Door7);
		
		NpcGood lorwin = new NpcGood("lorwin", "a tall pale elf wearing a green vest", 100, 150, true, "Good job my friend, Good job, "
				+ "I understimated you.<BR>My name is Lowrwin and I am the sorcerer of the wood. The druid informed me about your "
				+ "cleverness and I made that riddle complicated to make shure no one else but you could come to my house,"
				+ " the wood it's filled with evil creatures.");
		lorwin.setSecondSpeech("This one I am giving to you is a plum of the legendary phoenix"
				+ "of the wood, it is really rare, don't lose it I only have one of those.");
		Ingredient plum = new Ingredient("phoenix plum","a fire red plum once appartaining to a phoenix");
		lorwin.addIngredient(plum);
		Rooms[13].addnpcs(lorwin);
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