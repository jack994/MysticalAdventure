import java.io.Serializable;
import java.util.ArrayList;

/**
 * Big items impossible to carry.
 * @author giacomobenso
 */
public class Fixed extends Item implements Serializable{
	private int money;
	private boolean opened; // has the item been examined?
	private ArrayList<Tool> t; // List of tools within 

	public Fixed(String name) {
		super(name);
		t = new ArrayList<Tool>();
		opened = false;

	}
	
	public void open(){
		opened = true;
	}
	
	public boolean hasBeenOpened(){
		return opened;
	}

	void addTool(Tool d) {
		t.add(d);
	}

	public ArrayList<Tool> getToolsArrayList(){
		return t;
	}
	
	/**
	 * creates a string with all the items in the arraylist.
	 * @return a string with all the items in the arraylist, comma separated.
	 */
	String getToolList() {
		String toreturn = "";
		for (Tool c : t) {
			toreturn += c.getName() + ", ";
		}
		if(toreturn.length()>3){
			toreturn = toreturn.substring(0, toreturn.length()-2);
		}
		return toreturn;
	}

	/**
	 * passing a string to this method wie find the item with that name in the arraylist
	 * @param d String corresponding to the name of the item we want to find in the arraylist
	 * @return the item
	 */
	Tool getToolCalled(String d) {
		
		for (Tool c : t) {
			if (c.getName().equals(d))
			return c;
		}
		return null;
	}
	
	public void removeItemNamed(String name){
		t.remove(getToolCalled(name));
	}

	void addMoney(int m) {
		money += m;
	}
	void removeAllMoney() {
		money = 0;
	}

	int getMoney() {
		return money;
	}
}
