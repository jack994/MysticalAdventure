import java.io.Serializable;

public class Tool extends Item implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private int value;

	public Tool(String name,String description, int value) {
		super(name, description);
		this.value = value;

	}

	public int getValue() {
		return value;
	}
	

}
