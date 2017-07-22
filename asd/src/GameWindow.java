import java.awt.*;
import java.awt.event.*;
import java.io.Serializable;

import javax.swing.*;

public class GameWindow extends JFrame implements Serializable {

	private static int itemCounter = 10;
	public static int greenLabelsCounter = 100;
	private static final int startItem = 10;
	private static final int endItem = 32;
	private static boolean fullBag = false;

	private static final long serialVersionUID = 1L;
	Font f = new Font("sansSerif", Font.PLAIN, 13);
	Font f2 = new Font("sansSerif", Font.BOLD, 14);
	private JScrollPane panelPane;
	private JTextPane pane;
	private JTextField input;
	private JMenuBar menubar;
	private JMenu menu;
	public JMenuItem save;
	public JMenuItem load;

	private JPanel menuPanel;
	private JPanel first4;
	private JPanel second4;
	private JPanel statsPanel;
	private JPanel items1;
	private JPanel items2;
	private JPanel items3;
	private JPanel items4;
	private JPanel items5;
	private JPanel items6;
	private JPanel allItems1;
	private JPanel allItems2;
	private JPanel allItems3;
	private JPanel ingredients1;
	private JPanel ingredients2;
	private JLabel ingredients;
	private JPanel mysIngredient;
	private JLabel[] labels;
	private JPanel pfittiz;
	private JPanel ingrpan;
	private JLabel[] labss;

	public GameWindow() {
		makeFrame();

	}

	public void makeFrame() {

		System.setProperty("apple.laf.useScreenMenuBar", "true"); //use the native menubar in OSX
		
		panelPane = new JScrollPane();
		pane = new JTextPane();
		panelPane.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		panelPane.setBackground(Color.WHITE);
		panelPane.setViewportView(pane);
		pane.setEditable(false);
		pane.setBackground(Color.WHITE);
		pane.setContentType("text/html");
		input = new JTextField();
		input.setPreferredSize(new Dimension(1000, 50));
		labels = new JLabel[40];
		for (int k = 0; k < labels.length; k++) {
			labels[k] = new JLabel("");
		}
		labels[0].setText(" Life: ");
		labels[2].setText(" Deaths:  ");
		labels[3].setText(0 + "");
		labels[4].setText(" Weapon: ");
		labels[5].setText(" none");
		labels[6].setText(" Money:   ");
		labels[7].setText(100 + "");
		labels[8].setText(" Item:  ");
		labels[8].setFont(f2);
		labels[9].setText("Q.ty:  ");
		labels[9].setFont(f2);
		labels[8].setOpaque(true);
		labels[9].setOpaque(true);
		labels[8].setBackground(Color.WHITE);
		labels[9].setBackground(Color.WHITE);
		menuPanel = new JPanel(new GridLayout(5, 1));
		first4 = new JPanel(new GridLayout(5, 1));
		second4 = new JPanel(new GridLayout(5, 1));
		statsPanel = new JPanel(new BorderLayout());
		items1 = new JPanel(new GridLayout(4, 1));
		items2 = new JPanel(new GridLayout(4, 1));
		items3 = new JPanel(new GridLayout(4, 1));
		items4 = new JPanel(new GridLayout(4, 1));
		items5 = new JPanel(new GridLayout(4, 1));
		items6 = new JPanel(new GridLayout(4, 1));
		allItems1 = new JPanel(new BorderLayout());
		allItems2 = new JPanel(new BorderLayout());
		allItems3 = new JPanel(new BorderLayout());

		int index;
		for (index = 0; index < 5; index++) {
			if (index == 0) {
				first4.add(labels[index * 2]);
				JPanel pan = new JPanel(new GridLayout(1, 100));
				labss = new JLabel[100];
				for (int z = 0; z < labss.length; z++) {
					labss[z] = new JLabel("");
					labss[z].setOpaque(true);
					pan.add(labss[z]);
					if (z > greenLabelsCounter) {
						labss[z].setBackground(Color.RED);
					} else
						labss[z].setBackground(Color.GREEN);
				}
				second4.add(pan);
			} else if (index == 4) {
				JPanel pan1 = new JPanel(new BorderLayout());
				JPanel pan2 = new JPanel(new BorderLayout());
				pan1.add(labels[index * 2], BorderLayout.WEST);
				pan2.add(labels[(index * 2) + 1], BorderLayout.EAST);
				pan1.setBackground(Color.white);
				pan2.setBackground(Color.white);
				first4.add(pan1);
				second4.add(pan2);
			} else {
				first4.add(labels[index * 2]);
				second4.add(labels[(index * 2) + 1]);
			}
		}
		statsPanel.add(first4, BorderLayout.WEST);
		statsPanel.add(second4, BorderLayout.CENTER);
		menuPanel.add(statsPanel);
		while (index < 9) {
			items1.add(labels[index * 2]);
			items2.add(labels[(index * 2) + 1]);
			items2.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
			index++;
		}
		while (index < 13) {
			items3.add(labels[(index * 2)]);
			items4.add(labels[(index * 2) + 1]);
			items4.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
			index++;
		}
		while (index < 17) {
			items5.add(labels[index * 2]);
			items6.add(labels[(index * 2) + 1]);
			items6.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
			index++;
		}
		allItems1.add(items1, BorderLayout.CENTER);
		allItems1.add(items2, BorderLayout.EAST);
		menuPanel.add(allItems1);
		allItems2.add(items3, BorderLayout.CENTER);
		allItems2.add(items4, BorderLayout.EAST);
		menuPanel.add(allItems2);
		allItems3.add(items5, BorderLayout.CENTER);
		allItems3.add(items6, BorderLayout.EAST);
		menuPanel.add(allItems3);
		ingredients = new JLabel("Ingredients :");
		ingredients.setOpaque(true);
		ingredients.setBackground(Color.WHITE);
		ingredients.setFont(f2);
		ingredients1 = new JPanel(new GridLayout(1, 3));
		ingredients2 = new JPanel(new GridLayout(1, 3));
		mysIngredient = new JPanel(new GridLayout(1, 2));
		ingrpan = new JPanel(new GridLayout(4, 1));
		ingrpan.add(ingredients);
		ingrpan.add(ingredients1);
		ingrpan.add(ingredients2);
		ingrpan.add(mysIngredient);
		menuPanel.add(ingrpan);
		menuPanel.setBackground(Color.WHITE);
		menuPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		menuPanel.setPreferredSize(new Dimension(280, 280));
		getContentPane().add(menuPanel, BorderLayout.WEST);
		getContentPane().add(panelPane, BorderLayout.CENTER);
		pfittiz = new JPanel(new BorderLayout());
		pfittiz.add(input, BorderLayout.CENTER);
		pfittiz.setBorder(BorderFactory.createEmptyBorder(0, 260, 0, 0));
		pfittiz.setBackground(Color.WHITE);
		getContentPane().add(pfittiz, BorderLayout.SOUTH);
		setVisible(true);
		setSize(new Dimension(1080, 680));
		input.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent key) {
				int keyCode = key.getKeyCode();

				switch (keyCode) {
				case (KeyEvent.VK_UP): {
					if (!Game.THESTACK.isEmpty())
						input.setText(Game.THESTACK.pop());
					break;
				}
				case (KeyEvent.VK_DOWN): {
					if (!Game.THESTACK.getS2().isEmpty())
						input.setText(Game.THESTACK.pushFromS2());
					break;
				}
				}
			}
		});
		menubar = new JMenuBar();
		menu = new JMenu("menu");
		save = new JMenuItem("save");
		load = new JMenuItem("load");
		menu.add(save);
		menu.add(load);
		menubar.add(menu);
		setJMenuBar(menubar);
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
		return labels[3];
	}

	public JLabel getMoneyLabel() {
		return labels[7];
	}

	public JLabel getWeaponLabel() {
		return labels[5];
	}

	public boolean BagFull() {
		return fullBag;
	}

	public void resetLifelabel() {

		int i = 0;
		for (JLabel l : labss) {
			if (i < greenLabelsCounter) {
				l.setBackground(Color.GREEN);
			} else {
				l.setBackground(Color.RED);
			}
			i++;
		}
	}

	public void resetItemsCounter() {
		itemCounter = 10;
		for (int k = itemCounter; k < endItem; k++) {
			labels[k].setText("");
		}
	}

	public void addItemToMenu(Tool tool) {
		if (itemCounter <= endItem) {
			if (itemCounter == endItem) {
				fullBag = true;
			}
			for (int j = startItem; j < endItem; j = j + 2) {
				if (labels[j].getText().equals(tool.getName())) {
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
		int qty;
		for (int i = 0; i < labels.length; i++) {
			if (tool.equals(labels[i].getText())) {
				if ((qty = Integer.parseInt(labels[i + 1].getText())) == 1) {
					labels[i].setText("");
					labels[i + 1].setText("");
					itemCounter -= 2;
				} else {
					labels[i + 1].setText((qty - 1) + "");
				}

			}
		}
	}

	public void decreaseLife(int life) {
		if (life < (greenLabelsCounter)) {
			greenLabelsCounter -= life;
			int i = 0;
			for (JLabel l : labss) {
				if (i < greenLabelsCounter) {
					l.setBackground(Color.GREEN);
				} else {
					l.setBackground(Color.RED);
				}
				i++;
			}
		}
	}

	public void increaseLife(int life) {
		if ((life + greenLabelsCounter) < 100) {
			greenLabelsCounter += life;
			int i = 0;
			for (JLabel l : labss) {
				if (i < greenLabelsCounter) {
					l.setBackground(Color.GREEN);
				} else {
					l.setBackground(Color.RED);
				}
				i++;
			}
		}
	}
}
