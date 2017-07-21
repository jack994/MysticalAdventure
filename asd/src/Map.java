import java.io.Serializable;

public class Map implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Room[] Rooms;

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

		Rooms[0] = new Room("THE BEDROOM", "you are in a room that looks like a bedroom");
		Rooms[1] = new Room("THE LIVING ROOM",
				"you are in the living room, all the house seems really tidy and clean.");
		Rooms[2] = new Room("THE KITCHEN",
				"you are in the kitchen, the light of a lantern shines in the middle of it.");
		Rooms[3] = new Room("THE MEADOW",
				"you are in a misty meadow in the middle of what seems to be a small wood,"
						+ " north, in the middle of the medow you can barely see a house and all around you it's only trees, they are so"
						+ " thick you could't even pass through.</i>");
		Rooms[4] = new Room("THE WOOD - Entrance", "you are in the area of the wood close to the meadow, the ligtht "
				+ "is still passing through the leaves and the air is humid");
		Rooms[5] = new Room("THE WOOD - East 1",
				"the trees in this part of the forest are thicker and you struggle to see anything in this " + "area.");
		Rooms[6] = new Room("THE WOOD - Valley",
				"in the middle of this area there is a river and all around flowers grow luxuriant. east you "
						+ "can see a waterfall, beside it, a dark entrance of what seems to be a tunnel.");
		Rooms[7] = new Room("THE TUNNEL - Entrance",
				"You can see nothing but the entrance of the tunnel behind you. it's really dark.");

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
	}

	public void addItemsToRooms() {

		Fixed wardrobe = new Fixed("wardrobe");
		wardrobe.addMoney(100);
		Rooms[0].addFixed(wardrobe);
		Fixed bed = new Fixed("bed");
		bed.addTool(new Tool("key", "A golden key", 0));
		Rooms[0].addFixed(bed);
		NpcBad Demogorgon = new NpcBad("demogorgon", "a horrid shadow with a shaape of a dragon", 50, 0, false,
				"grawallll", 3);
		Weapon demo = new Weapon("sword", "A shiny sword with some blood on the blade, possibly yours", 50, 6, 0.90f);
		Demogorgon.setWeapon(demo);
		Tool demoC = new Tool("claw of demogorgon", "a horrid claw", 8);
		Demogorgon.addObj(demoC);
		Rooms[0].addnpcs(Demogorgon);

		NpcGood druid = new NpcGood("druid", "a busy druid with enormous horns", 1000, 150, true,
				"Look, the forest is dying, I need your help to "
				+ "find three ingredients to make a potion that will heal it, the creatures of the forest will tell you which ingredients i need."
				+ " sorry if i can't tell you anything more but I have to go, come back to me when you have the three ingredients."
				+ " The Druid runs towards the exit door.<BR><BR><b>druid :</b> ah, I almost forgot, treants like riddles and the number 7!");
		druid.setFirstSpeech("Aaawww finally you woke up! I was waiting for you");
		Rooms[1].addnpcs(druid);
		Fixed chest = new Fixed("chest");
		chest.addMoney(150);
		chest.addTool(
				new Weapon("knife", "A shiny knife with some blood on the blade, it's still fresh", 50, 10, 0.99f));
		Rooms[1].addFixed(chest);
		Rooms[1].addTool(new Tool("salt", "It's just some tablesalt", 5));

		Fixed cupboard = new Fixed("cupboard");
		cupboard.addMoney(50);
		cupboard.addTool(new Tool("apple", "A green apple, it seems still ripe", 2));
		Rooms[2].addFixed(cupboard);
		NpcBad goblin = new NpcBad("goblin", "a red goblin", 45, 45, true, "gnam gnam", 1);
		Weapon axe = new Weapon("axe", "A shiny axe", 50, 6, 0.95f);
		goblin.setWeapon(axe);
		Tool eyeG = new Tool("goblin eye", "a horrid eye", 8);
		goblin.addObj(eyeG);
		Rooms[2].addnpcs(goblin);

		NpcGood treant = new NpcGood("treant", "an enormous alive tree that blocks the passage to the wood", 100, 130,
				false, "Pa ohz illu hyvbuk mvy tpsspvuz vm flhyz, iba pa pz uv tvyl aohu h tvuao vsk. Doha pz pa?");
		Rooms[3].addnpcs(treant);

	}

	public Room getRoomCalled(String name) {
		int i = 0;
		Room toReturn = new Room("", "");
		while (i <= Rooms.length) {
			if (Rooms[i].getName().equals(name)) {
				toReturn = Rooms[i];
				break;
			}
			i++;
		}
		return toReturn;
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
}