import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Serial {

	static Game g;

	public static void serializer() {
		try {
			FileOutputStream fileOut = new FileOutputStream("./folder/savedGame.ser");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(g);
			out.close();
			fileOut.close();
			System.out.printf("Serialized data is saved in ./folder/savedGame.ser");
		} catch (IOException i) {
			i.printStackTrace();
		}
	}

	public static void deserializer(boolean dispose) {
		try {
			FileInputStream fileIn = new FileInputStream("./folder/savedGame.ser");
			ObjectInputStream in = new ObjectInputStream(fileIn);
			g = (Game) in.readObject();
			in.close();
			fileIn.close();
			GameWindow.greenLabelsCounter = g.currentPlayer.getLifeRemaining(); // reset correct life in life-bar
			if (!dispose) {
				Game.frame = new GameWindow();
			}
			Game.frame.getTextBox().addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Game.THESTACK.reset();
					g.write();
				}
			});
			Game.frame.getMoneyLabel().setText(g.currentPlayer.getMoneyAmount() + ""); // reset money in JFrame
			Game.frame.getWeaponLabel().setText(g.currentPlayer.getWeapon().getName()); //reset weapon in JFrame
			Game.frame.resetItemsCounter();
			for (Tool t : g.currentPlayer.getItemsHeldArray()) {
				Game.frame.addItemToMenu(t);
			}
			g.start();
		} catch (IOException i) {
			i.printStackTrace();
			g = new Game();
			g.start();
		} catch (ClassNotFoundException c) {
			System.out.println("class not found");
			c.printStackTrace();
			return;
		}
	}

	public static void main(String[] args) {

		deserializer(false);

		Game.frame.save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				serializer();
			}
		});

		Game.frame.load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deserializer(true);

			}
		});
	}
}
