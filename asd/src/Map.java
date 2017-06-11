
public class Map {
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
				"you are in a misty meadow in the middle of what seems to be a huge rainforest,"
						+ " north, in the middle of the medow you can barely see a house and all oround you it's only trees, it's dark.</i>");
		Rooms[4] = new Room("THE WOOD - Entrance", "you are in the area of the wood close to the meadow, the ligtht "
				+ "is still passing through the leaves and the air is humid");
		
		return Rooms[3];
	}

	public void setRoomDirections() {
		Rooms[0].setDirection("down", Rooms[1]);
		Rooms[1].setDirection("east", Rooms[2]);
		Rooms[1].setDirection("south", Rooms[3]);
		Rooms[1].setDirection("up", Rooms[0]);
		Rooms[2].setDirection("west", Rooms[1]);
		Rooms[3].setDirection("north", Rooms[1]);
		Rooms[3].setDirection("south", Rooms[4]);
		Rooms[4].setDirection("north", Rooms[3]);

	}

	public void addItemsToRooms() {
		Fixed wardrobe = new Fixed("wardrobe");
		wardrobe.addMoney(100);
		Rooms[0].addFixed(wardrobe);
		Fixed bed = new Fixed("bed");
		bed.addTool(new Tools("key","A golden key", 0));
		Rooms[0].addFixed(bed);
		NpcBad Demogorgon = new NpcBad("demogorgon", "a horrid shadow with a shaape of a dragon", 50);
		Weapon demo = new Weapon("sword","A shiny sword with some blood on the blade, possibly yours", 50, 10, 0.78f);
		Demogorgon.setWeapon(demo);
		Tools demoC = new Tools("claw of demogorgon", "a horrid claw",8);
		Demogorgon.addObj(demoC);
		Rooms[0].addnpcs(Demogorgon);
		
		Fixed chest = new Fixed("chest");
		chest.addMoney(150);
		chest.addTool(new Weapon("knife","A shiny knife with some blood on the blade, it's still fresh", 50, 10, 0.85f));
		Rooms[1].addFixed(chest);
		Rooms[1].addTool(new Tools("salt","It's just some tablesalt", 5));
		
		Fixed cupboard = new Fixed("cupboard");
		cupboard.addMoney(50);
		cupboard.addTool(new Tools("apple","A green apple, it seems still ripe", 2));
		Rooms[2].addFixed(cupboard);
		
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
}