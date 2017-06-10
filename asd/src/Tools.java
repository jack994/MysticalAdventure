
public class Tools extends Item {

	private int value;
	private String description;

	public Tools(String name, String description, int value) {
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
