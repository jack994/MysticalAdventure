import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GameWindow extends JFrame {
	
	private int itemCounter = 8;
	private int startItem = 8;
	private int endItem = 36;
	private boolean fullBag = false;

	private static final long serialVersionUID = 1L;
	Font f = new Font("sansSerif", Font.PLAIN, 13);
	Font f2 = new Font("sansSerif", Font.BOLD, 14);
	private static JScrollPane panelPane;
	private static JTextPane pane;
	private static JTextField input;

	private static JPanel menuPanel;
	private static JPanel[] first18;
	private static JPanel ingredients1;
	private static JPanel ingredients2;
	private static JLabel ingredients;
	private static JPanel mysIngredient;
	private static JLabel[] labels;
	private static JPanel pfittiz;

	public GameWindow() {
		makeFrame();

	}

	public void makeFrame() {

		panelPane = new JScrollPane();
		pane = new JTextPane();
		panelPane.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		panelPane.setBackground(Color.WHITE);
		panelPane.setViewportView(pane);
		pane.setEditable(false);
		pane.setBackground(Color.WHITE);
		pane.setContentType("text/html");
		input = new JTextField();
		input.setPreferredSize(new Dimension(770, 50));
		labels = new JLabel[38];
		for (int i = 0; i < labels.length; i++) {
			labels[i] = new JLabel("");
		}
		labels[0].setText("Deaths: ");
		labels[1].setText(0 + "");
		labels[2].setText("Money: ");
		labels[3].setText(0 + "");
		labels[4].setText("Weapon: ");
		labels[5].setText("none");
		labels[6].setText("Item: ");
		labels[6].setFont(f2);
		labels[7].setText("Quantity: ");
		labels[7].setFont(f2);
		menuPanel = new JPanel(new GridLayout(22, 1));
		first18 = new JPanel[18];
		for (int i = 0; i < first18.length; i++) {
			first18[i] = new JPanel(new GridLayout(1, 2));
			first18[i].add(labels[i * 2]);
			first18[i].add(labels[(i * 2) + 1]);
			menuPanel.add(first18[i]);
			if (i == 3) {
				first18[i].setBackground(Color.WHITE);
			}
		}
		ingredients = new JLabel("Ingredients :");
		ingredients.setFont(f2);
		ingredients1 = new JPanel(new GridLayout(1, 3));
		ingredients2 = new JPanel(new GridLayout(1, 3));
		mysIngredient = new JPanel(new GridLayout(1, 2));
		menuPanel.add(ingredients);
		menuPanel.add(ingredients1);
		menuPanel.add(ingredients2);
		menuPanel.add(mysIngredient);
		menuPanel.setBackground(Color.WHITE);
		menuPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
		menuPanel.setPreferredSize(new Dimension(180, 180));
		getContentPane().add(menuPanel, BorderLayout.WEST);
		getContentPane().add(panelPane, BorderLayout.CENTER);
		pfittiz = new JPanel();
		pfittiz.add(input);
		pfittiz.setBorder(BorderFactory.createEmptyBorder(0, 180, 0, 0));
		pfittiz.setBackground(Color.WHITE);
		getContentPane().add(pfittiz, BorderLayout.SOUTH);
		setVisible(true);
		setSize(new Dimension(950, 650));
		input.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent key) {
				int keyCode = key.getKeyCode();
				
				switch (keyCode) {
				case (KeyEvent.VK_UP): {
					if(!Game.THESTACK.isEmpty())
						input.setText(Game.THESTACK.pop());
					break;
				}
				case (KeyEvent.VK_DOWN): {
					if(!Game.THESTACK.getS2().isEmpty())
						input.setText(Game.THESTACK.pushFromS2());
					break;
				}
				}
			}
		});
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}

	public JTextField getTextBox() {
		return input;
	}

	public JTextPane getPane() {
		return pane;
	}

	public JLabel getDeathsLabel() {
		return labels[1];
	}

	public JLabel getMoneyLabel() {
		return labels[3];
	}

	public JLabel getWeaponLabel() {
		return labels[5];
	}

	public boolean BagFull() {
		return fullBag;
	}

	public void addItemToMenu(Tools tool) {
		if (itemCounter * 2 <= endItem) {
			if (itemCounter * 2 == endItem) {
				fullBag = true;
			}
			for (int j = startItem; j < endItem; j = j + 2) {
				if (labels[j].getText().equals(tool.getDescription())) {
					int tmp = Integer.parseInt(labels[j + 1].getText());
					labels[j + 1].setText((tmp + 1) + "");
					return;
				}
			}
			labels[itemCounter].setText(tool.getName());
			labels[itemCounter + 1].setText(1 + "");
			itemCounter += 2;
		}
	}

	public void removeItemFromMenu(String tool) {
		for (int i = 0; i < labels.length; i++) {
			if (tool.equals(labels[i].getText())) {
				labels[i].setText("");
				labels[i + 1].setText("");
				itemCounter -= 2;

			}
		}
	}
}
