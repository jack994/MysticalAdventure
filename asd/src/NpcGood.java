

public class NpcGood extends Character {

	private String description;
	private String speech;
	private boolean active;
	
	public NpcGood(String name, String description, int HP, boolean active) {
		super(name, HP);
		this.description = description;
		this.active = active;

	}
	
	@Override
	public String die() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String getSpeech(){
		return speech;
	}

	public boolean isActive(){
		return active;
	}
	
	public String getDescription(){
		return description;
	}
	
	public String getItems(){
		String items = "";
		for(int i = 0 ; i < itemsHeld.size() ; i++){
			if(!itemsHeld.get(i).equals(itemsHeld.get(itemsHeld.size() -1)))
				items = items + itemsHeld.get(i).getDescription() + ", " ;
			else items = items + itemsHeld.get(i).getDescription() + ". " ;
		}
		return name + " is carrying: " + items;
		}

}
