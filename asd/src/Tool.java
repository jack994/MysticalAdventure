import java.io.Serializable;

public class Tool extends Item implements Serializable{


	private static final long serialVersionUID = 1L;
	private int value;
	private String description;

	public Tool(String name, String description, int value) {
		super(name);
		this.value = value;
		this.description = description;

	}

	public int getValue() {
		return value;
	}
	
	public String getDescription(){
		return description;
	}

}
