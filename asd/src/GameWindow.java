import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

/**
 * class to create the GUI of the entire program
 * @author giacomobenso
 */
public class GameWindow extends JFrame{

	private static final long serialVersionUID = 1L;
	
	public static int greenLabelsCounter = 100;
	
	private static int itemCounter = 10;
	private static int ingrCounter = 0;
	private static final int startItem = 10;
	private static final int endItem = 32;
	private static boolean fullBag = false;
	public static int numOfIngredients = 3;

	Font f = new Font("sansSerif", Font.PLAIN, 13);
	Font f2 = new Font("sansSerif", Font.BOLD, 14);
	private JScrollPane panelPane;
	private JTextPane pane;
	private JTextField input; //textbox
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
	private JLabel ingredients;
	private JLabel[] labels;
	private JLabel[] labels2;
	private JPanel pfittiz;
	private JPanel ingrpan;
	private JLabel[] labss;

	public GameWindow() {
		makeFrame();

	}

	/**
	 * create the GUI
	 */
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
		labels = new JLabel[endItem + 2];
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
				JPanel pan = new JPanel(new GridLayout(1, 100)); //life bar
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
		//all the three loops needed for the items carried in the bag
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
		ingrpan = new JPanel(new GridLayout(numOfIngredients+1, 1));
		ingrpan.add(ingredients);
		labels2 = new JLabel[numOfIngredients];
		for(int i=0; i< numOfIngredients; i++){
			labels2[i] = new JLabel("");
			ingrpan.add(labels2[i]);
		}
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
		//create the save menu
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

	public boolean BagFull() { // check whether the bag is full
		return fullBag;
	}

	/**
	 * get the number of map pieces
	 * @return an integer representing the number of map pieces
	 */
	public int getMapPieces(){
		for(int i = startItem; i < endItem; i+=2){
			if(labels[i].getText().equals("map piece")){
				return Integer.parseInt(labels[i+1].getText());
			}
		}
		return 0;
	}
	
	/**
	 * set all the labels of the bag to ''
	 */
	public void emptyBagLabels(){
		for(int i = startItem; i < endItem; i+=2){
			labels[i].setText("");
			labels[(i)+1].setText("");
		}
	}
	
	/**
	 * set all the labels in the ingredients area to ''
	 */
	public void emptyIngredientsLabels(){
		for(int i = ingrCounter; i < numOfIngredients; i+=2){
			labels2[i].setText("");
		}
	}
	
	/**
	 * set the lifebar to the actual life of the character
	 */
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

	/**
	 * reset the item-counter ant then the labels of the bag
	 */
	public void resetItemsCounter() {
		itemCounter = 10;
		for (int k = itemCounter; k < endItem; k++) {
			labels[k].setText("");
		}
	}

	/**
	 * add a tool to the correct label in the bag
	 * @param tool
	 */
	public void addItemToMenu(Tool tool) {
		if(tool.getClass() == Ingredient.class){ //if we are adding an ingredient
			for(int k = 0; k< numOfIngredients; k++){
				if(labels2[k].getText().equals("")){
					labels2[k].setText(tool.getName());
					return;
				}
			}
			return;	
		}
		if (itemCounter <= endItem + 2) {
			for (int j = startItem; j <= endItem; j = j + 2) {
				if (labels[j].getText().equals(tool.getName())) {
					int tmp = Integer.parseInt(labels[j + 1].getText());
					labels[j + 1].setText((tmp + 1) + "");
					return;
				}
			}
			labels[itemCounter].setText(tool.getName());
			labels[itemCounter + 1].setText(1 + "");
			if (itemCounter == endItem) {
				fullBag = true;
			}
				itemCounter += 2;
		}
	} // TODO if ingredient

	/**
	 * remove from the bag the given string
	 * @param tool
	 */
	public void removeItemFromMenu(String tool) {	
		int qty;
		for (int i = startItem; i < itemCounter; i+=2) {
			if (tool.equals(labels[i].getText())) {
				if ((qty = Integer.parseInt(labels[i + 1].getText())) == 1) {
					while(i<itemCounter){
						if(i == endItem){
							labels[i].setText("");
							labels[i + 1].setText("");
							i+=2;
							fullBag = false;
						}else{
							labels[i].setText(labels[i+2].getText());
							labels[i + 1].setText(labels[i+3].getText());
							i+=2;
						}
					}
					itemCounter -= 2;
				} else {
					labels[i + 1].setText((qty - 1) + "");
				}
				return;
			}
		}
		for(int j=0; j<numOfIngredients; j++){  //we reach this loop only if we are removing an ingredient
			if(labels2[j].getText().equals(tool)){
				labels2[j].setText("");
			}
		}
		
	}

	/**
	 * decrease the life in the life-bar
	 * @param life : life to be removed
	 */
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

	/**
	 * add life to the life-bar
	 * @param life: life to be added
	 */
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
