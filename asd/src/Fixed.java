import java.util.ArrayList;

public class Fixed extends Item {
	private int money;
	private boolean opened;
	private ArrayList<Tools> t;

	public Fixed(String name) {
		super(name);
		t = new ArrayList<Tools>();
		opened = false;

	}
	
	public void open(){
		opened = true;
	}
	
	public boolean hasBeenOpened(){
		return opened;
	}

	void addTool(Tools d) {
		t.add(d);
	}

	public ArrayList<Tools> getToolsArrayList(){
		return t;
	}
	
	String getToolList() {
		String toreturn = "";
		for (Tools c : t) {
			toreturn += c.getName() + ", ";
		}
		if(toreturn.length()>3){
			toreturn = toreturn.substring(0, toreturn.length()-2);
		}
		return toreturn;
	}

	Tools getToolCalled(String d) {
		
		for (Tools c : t) {
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

	int getMoney() {
		return money;
	}
}
