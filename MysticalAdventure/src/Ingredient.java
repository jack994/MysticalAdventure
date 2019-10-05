import java.io.Serializable;

/**
 * The ingredients are the objective of the game, there are three of them which need to be found
 * @author giacomobenso
 */
public class Ingredient extends Tool implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public Ingredient(String name, String description) {
		super(name, description, 0);
	}

}
