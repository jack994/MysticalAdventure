import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * class representing an area in the game, every Room can contain items, characters and money
 * @author giacomobenso
 *
 */
public class Room implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String description;
	private ArrayList<Item> items; //items contained in the room
	private HashMap<String, Room> directions; //directions from this room to another
	private ArrayList<NPC> npcs; //NPCs contained in the room
	private boolean dark;
	protected int money;

	public Room(String Name, String description, boolean dark) {
		Name = "<b>" + Name + "</b>"; //bold
		this.name = Name;
		this.description = "<i>" + description + "</i>";
		items = new ArrayList<Item>();
		directions = new HashMap<String, Room>();
		npcs = new ArrayList<NPC>();
		this.setDark(dark);
		money = 0;
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
		return name + "<BR><BR>" + description + "<BR><BR>" + getDirectionsString() + 
				 getItemString() + getNPCString();
	}
	
	public ArrayList<NPC> getNPCArray() {
		return npcs;
	}
	
	public void addMoney(int money){
		this.money += money;
	}
	
	public void removeMoney(int money){
		if(this.money >= money){
			this.money -= money;
		}
		else{
			System.err.println("Non hai abbastanza monete");
		}
	}
	
	public int getMoney(){
		return money;
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

	public boolean isDark() {
		return dark;
	}

	public void setDark(boolean dark) {
		this.dark = dark;
	}
	
	public void setDescription(String description){
		this.description = description;
	}

	public void addTool(Tool o) {
		items.add(o);
	}

	public void addFixed(Fixed o) {
		items.add(o);
	}
	
	public void addIngredient(Ingredient o){
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

	/**
	 * get an item from the given string if it is either in the room or in an OPEN fixed within the room
	 * @param Name
	 * @return the item
	 */
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
	
	/**
	 * remove the item corresponding to the given string from the room or from an OPEN fixed
	 * @param Name
	 */
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

	/**
	 * get a string containing the money end the items contained in the room
	 * @return
	 */
	public String getItemString() {
		String toReturn = "<BR>OGGETTI: ";
		if(items.isEmpty() || dark){
			return "";
		}
		if(!dark){
		for (int i = 0; i < items.size(); i++) {
			Item it;
			toReturn += " -" + (it = items.get(i)).getName();
			if(it.getName().equals("baule") && ((Fixed)it).getToolsArrayList().isEmpty() &&
					((Fixed) it).getMoney() == 0){ //check if the item is a chest and if it is empty
				toReturn += " (Pieno)";
				}
		}
		}
		if(money > 0){
			toReturn += "<BR>MONETE: "+ money;
		}
		return toReturn;
	}

	/**
	 * get a string containing the characters contained in the room
	 * @return
	 */
	public String getNPCString() {
		String toReturn = "<BR>PERSONAGGI: ";
		if(npcs.isEmpty() || dark){
			return "";
		}
		if(!dark){
		for (int i = 0; i < npcs.size(); i++) {
			toReturn += " -" + npcs.get(i).getName();
			if(!npcs.get(i).isAlive()){//if the NPC is dead
				toReturn += " (Morto)";
			}
		}
		}
		return toReturn;
	}
	
	/**
	 * given a string get the NPC in the room with that name, if it exists in the room
	 * @param Name
	 * @return the NPC
	 */
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
	
	/**
	 * get the merchant in the room if it exists
	 * @return the merchant
	 */
	public Merchant getMerchant(){
		for(NPC npc : npcs){
			if(npc.getClass() == Merchant.class){
				return (Merchant)npc;
			}
		}
		return null;
	}
	
	/**
	 * create a string containing the possible directions from the this room
	 * @return the string with the directions
	 */
	public String getDirectionsString() {
		String returnString = "POSSIBILI DIREZIONI: ";
		Set<String> keys = directions.keySet();
		if(dark)
			return "";
		else{
		for (Iterator<String> iter = keys.iterator(); iter.hasNext();)
			returnString += " -" + iter.next();
		}
		return returnString;
	}

}
