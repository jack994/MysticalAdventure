import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.commons.io.FileUtils;

/**
 * class containing the main method and the methods needed to save and load the game
 * @author giacomobenso
 *
 */
public class MysticalAdventure {
	
	public static Game GAME;
	
	/**
	 * save the game serializing an object 
	 * @param obj: object to serialize
	 */
	public static void serializer(SavedObj obj) {
		try {
			FileOutputStream fileOut = new FileOutputStream("./lib/savedGame/savedGame.ser");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(obj);
			out.close();
			fileOut.close();
			System.out.printf("Serialized data is saved in ./lib/savedGame/savedGame.ser");
		} catch (IOException i) {
			i.printStackTrace();
		}
	}

	/**
	 * when the player dies restart the game from the beginning, the saved game is erased
	 */
	public static void die(){
		File fileIn = new File("./lib/savedGame/savedGameDeath.ser");
		File fileOut = new File("./lib/savedGame/savedGame.ser");
		try {
			FileUtils.copyFile(fileIn, fileOut);
		} catch (IOException e) {
			e.printStackTrace();
		}
			GAME.setCurrentPlayer(new Player("Eldor"));
			Map m = new Map();
			GAME.setMap(m);
			GAME.getCurrentPlayer().setCurrentRoom(m.createRoom());
			GAME.frame.resetItemsCounter();
			GameWindow.greenLabelsCounter = GAME.getCurrentPlayer().getLifeRemaining(); // reset correct life in life-bar
			GAME.frame.resetLifelabel();
			GAME.frame.emptyBagLabels();
			GAME.frame.getMoneyLabel().setText(GAME.getCurrentPlayer().getMoneyAmount() + ""); // reset money in JFrame
			GAME.frame.getWeaponLabel().setText(GAME.getCurrentPlayer().getWeapon().getName()); //reset weapon in JFrame
			GAME.frame.resetItemsCounter();
			GAME.printWelcome(true);
	}
	
	/**
	 * load the saved game deserializing the object
	 * @param obj: object to be deserialized
	 * @param g: the game
	 */
	public static void deserializer(SavedObj obj, Game g) {
		try {
			FileInputStream fileIn = new FileInputStream("./lib/savedGame/savedGame.ser");
			ObjectInputStream in = new ObjectInputStream(fileIn);
			obj = (SavedObj) in.readObject();
			in.close();
			fileIn.close();
			g.setCurrentPlayer(obj.getCurrentPlayer());
			g.setMap(obj.getMap());
			Command.commandWords = obj.getCommands();
			GameWindow.greenLabelsCounter = obj.getCurrentPlayer().getLifeRemaining(); // reset correct life in life-bar
			GAME.frame.resetLifelabel();
			GAME.frame.getMoneyLabel().setText(obj.getCurrentPlayer().getMoneyAmount() + ""); // reset money in JFrame
			GAME.frame.getWeaponLabel().setText(obj.getCurrentPlayer().getWeapon().getName()); //reset weapon in JFrame
			GAME.frame.resetItemsCounter();
			for (Tool t : obj.getCurrentPlayer().getItemsHeldArray()) {
				GAME.frame.addItemToMenu(t);
			}
			g.start();
		} catch (IOException i) {
			i.printStackTrace();
		} catch (ClassNotFoundException c) {
			System.out.println("class not found");
			c.printStackTrace();
			return;
		}
	}

//   ***********************************  MAIN METHOD ************************************
	
	public static void main(String[] args) {

		GAME = new Game();
		SavedObj ob = new SavedObj();
		deserializer(ob, GAME);
		GAME.start();

		GAME.frame.save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ob.setCurrentPlayer(GAME.getCurrentPlayer());
				ob.setMap(GAME.getMap());
				ob.setCommands(Command.commandWords);
				serializer(ob);
			}
		});

		GAME.frame.load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deserializer(ob, GAME);
				GAME.frame.getTextBox().addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						Game.THESTACK.reset();
						GAME.write();
					}
				});
			}
		});
	}

//	*******************************************************************************************
}
