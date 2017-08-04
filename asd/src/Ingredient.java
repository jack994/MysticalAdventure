import java.io.Serializable;

public class Ingredient extends Item implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public Ingredient(String name, String description) {
		super(name, description);
	}

}
