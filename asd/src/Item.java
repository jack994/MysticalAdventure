import java.io.Serializable;

/**
 * abstract class used as superclass for the classes Fixed and Tool
 * 
 * @author giacomobenso
 */
public abstract class Item implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String name;

	public Item(String name) {

		this.name = name;

	}

	public String getName() {
		return name;
	}

}