import java.io.Serializable;

/**
 * abstract class used as superclass for the classes Fixed and Tool
 * 
 * @author giacomobenso
 */
public abstract class Item implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String name;
	private String description;

	public Item(String name, String description) {

		this.name = name;
		this.description = description;

	}

	public String getName() {
		return name;
	}
	
	public String getDescription(){
		return description;
	}
	
	public void setDescription(String description){
		this.description = description;
	}

}