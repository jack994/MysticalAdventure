import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JOptionPane;

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
			String path = MysticalAdventure.class.getProtectionDomain().getCodeSource().getLocation().getPath();
			path = path.replaceAll("%20", " ");
			FileOutputStream fileOut = new FileOutputStream(path.substring(0, path.lastIndexOf("/") + 1) + "data/savedGame.ser");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(obj);
			out.close();
			fileOut.close();
			System.out.printf("Serialized data is saved in " + path.substring(0, path.lastIndexOf("/") + 1) + "data/savedGame.ser");
		} catch (IOException i) {
			i.printStackTrace();
		}
	}

	/**
	 * when the player dies restart the game from the beginning, the saved game is erased
	 */
	public static void reset(boolean dead){
		String path = MysticalAdventure.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		path = path.replaceAll("%20", " ");
		File fileIn = new File(path.substring(0, path.lastIndexOf("/") + 1) + "data/savedGameDeath.ser");
		File fileOut = new File(path.substring(0, path.lastIndexOf("/") + 1) + "data/savedGame.ser");
		try {
			FileUtils.copyFile(fileIn, fileOut);
		} catch (IOException e) {
			e.printStackTrace();
		}
			int deaths = GAME.getCurrentPlayer().getDeaths() + 1;
			GAME.setCurrentPlayer(new Player("Eldor"));
			GAME.getCurrentPlayer().setDeaths(deaths);
			GAME.frame.getDeathsLabel().setText(deaths + "");
			Map m = new Map();
			GAME.setMap(m);
			GAME.getCurrentPlayer().setCurrentRoom(m.createRoom());
			GameWindow.greenLabelsCounter = (int)((double)GAME.getCurrentPlayer().getLifeRemaning() / 
					GAME.getCurrentPlayer().getHP()*100); // reset correct life in life-bar
			GAME.frame.resetLifelabel();
			GAME.frame.emptyBagLabels();
			GAME.frame.emptyIngredientsLabels();
			GAME.frame.getMoneyLabel().setText(GAME.getCurrentPlayer().getMoneyAmount() + ""); // reset money in JFrame
			GAME.frame.getWeaponLabel().setText(GAME.getCurrentPlayer().getWeapon().getName()); //reset weapon in JFrame
			GAME.printWelcome(dead);
	}
	
	/**
	 * load the saved game deserializing the object
	 * @param obj: object to be deserialized
	 * @param g: the game
	 */
	public static void deserializer(SavedObj obj, Game g) {
		try {
			String path = MysticalAdventure.class.getProtectionDomain().getCodeSource().getLocation().getPath();
			path = path.replaceAll("%20", " ");
			FileInputStream fileIn = new FileInputStream(path.substring(0, path.lastIndexOf("/") + 1) + "data/savedGame.ser");
			ObjectInputStream in = new ObjectInputStream(fileIn);
			obj = (SavedObj) in.readObject();
			in.close();
			fileIn.close();
			g.setCurrentPlayer(obj.getCurrentPlayer());
			g.setMap(obj.getMap());
			Command.commandWords = obj.getCommands();
			GameWindow.greenLabelsCounter = (int)((double)obj.getCurrentPlayer().getLifeRemaning() / 
					obj.getCurrentPlayer().getHP()*100); // reset correct life in life-bar
			GAME.frame.resetLifelabel();
			GAME.frame.getMoneyLabel().setText(obj.getCurrentPlayer().getMoneyAmount() + ""); // reset money in JFrame
			GAME.frame.getWeaponLabel().setText(obj.getCurrentPlayer().getWeapon().getName()); //reset weapon in JFrame
			GAME.frame.getDeathsLabel().setText(obj.getCurrentPlayer().getDeaths() + ""); //update deaths label
			GAME.frame.emptyBagLabels();
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
				int response = JOptionPane.showConfirmDialog(null, "Do you want to save the game state?", "Confirm",
				        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				    if (response == JOptionPane.YES_OPTION) {
				    	ob.setCurrentPlayer(GAME.getCurrentPlayer());
						ob.setMap(GAME.getMap());
						ob.setCommands(Command.commandWords);
						serializer(ob);
				    } 
			}
		});
		

		GAME.frame.load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int response = JOptionPane.showConfirmDialog(null, "Do you want to load the saved data?", "Confirm",
				        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				    if (response == JOptionPane.YES_OPTION) {
				    	deserializer(ob, GAME);
				    } 
			}
		});
		
		GAME.frame.reset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int response = JOptionPane.showConfirmDialog(null, "If you reset the Game all your progress "
						+ "will be lost. \nDo you want to continue?", "Confirm",
				        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				    if (response == JOptionPane.YES_OPTION) {
				    	reset(false);
				    } 
			}
		});
		
	}

//	*******************************************************************************************
}
