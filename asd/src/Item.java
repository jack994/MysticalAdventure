/**
 * abstract class used as superclass for the classes Fixed and Tool
 * @author giacomobenso
 */
public abstract class Item {
	
	private String name;

	public Item(String name) {
		
		this.name = name;
	
	}

	public String getName() {
		return name;
	}




}