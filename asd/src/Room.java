
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class Room {
	private String name;
	private String description;
	private ArrayList<Item> items;
	private HashMap<String, Room> directions;
	private ArrayList<NPC> npcs;

	public Room(String Name, String description) {
		Name = "<b>" + Name + "</b>";
		this.name = Name;
		description = "<i>" + description + "</i>";
		this.description = description;
		items = new ArrayList<Item>();
		directions = new HashMap<String, Room>();
		npcs = new ArrayList<NPC>();
	}

	public String getName() {
		return name.replace("<b>", "").replace("</b>", "");
	}

	public String getDescription() {
		return description.replace("<i>", "").replace("</i>", "");
	}

	public ArrayList<Item> getItemsArray() {
		return items;
	}

	public String getNameAndDescription() {
		return name + "<BR><BR>" + description + "<BR><BR>" + getDirectionsString() + "<BR>" + getItemString() + "<BR>"
				+ getNPCString();

	}

	public void addTool(Tool o) {
		items.add(o);
	}

	public void addFixed(Fixed o) {
		items.add(o);
	}

	public void addnpcs(NPC p) {
		npcs.add(p);
		p.setCurrentRoom(this);
	}

	public void removeNpcNamed(String name) {
		int i = 0;
		while (i < npcs.size())
			if (npcs.get(i).getName().equals(name))
				npcs.remove(i);
		i++;
	}

	public Item getItemNamed(String Name){
		int i = 0;
		Fixed f;
		while (i < items.size()) {
			if (items.get(i).getName().equals(Name))
				return items.get(i);
			if(items.get(i).getClass() == Fixed.class && (f = (Fixed) items.get(i)).hasBeenOpened()){
				if(!f.getToolsArrayList().isEmpty()){
					for(Tool x : f.getToolsArrayList()){
						if(Name.equals(x.getName())){
							return x;
						}
					}
				}
			}
			i++;
		}
		return null;
	}
	
	public void removeItemNamed(String Name) {
		Item it;
		if((it = getItemNamed(Name)) != null)
			if(items.contains(it)){
			items.remove(it);
			}
			else{
				for(Item x : items){
					if(x.getClass() == Fixed.class && ((Fixed) x).hasBeenOpened()){
						if (((Fixed)x).getToolCalled(Name) != null){
							((Fixed)x).removeItemNamed(Name);;
						}
					}
				}
			}
	}

	public String getItemString() {
		String toReturn = "ITEMS: ";
		for (int i = 0; i < items.size(); i++) {
			toReturn += " -" + items.get(i).getName();
		}
		return toReturn;
	}

	public String getNPCString() {
		String toReturn = "CHARACTERS: ";
		for (int i = 0; i < npcs.size(); i++) {
			toReturn += " -" + npcs.get(i).getName();
		}
		return toReturn;
	}
	
	public ArrayList<NPC> getNPCArray() {
		return npcs;
	}
	
	public NPC getNPCNamed(String Name){		
		if(!npcs.isEmpty()){
			for(NPC pl : npcs){
				if(pl.getName().equals(Name)){
					return pl;
				}
			}
		}
		
		return null;
	}
	
	public String getDirectionsString() {
		String returnString = "POSSIBLE DIRECTIONS: ";
		Set<String> keys = directions.keySet();
		for (Iterator<String> iter = keys.iterator(); iter.hasNext();)
			returnString += " -" + iter.next();
		return returnString;
	}

	public void setDirection(String direction, Room neighbor) {
		directions.put(direction, neighbor);
	}

	public Room getDirectionRoom(String direction) {

		return directions.get(direction);
	}

	boolean hasDirection(String o) {
		if (directions.keySet().contains(o))
			return true;
		else
			return false;
	}

}
