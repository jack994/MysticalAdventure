import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GameWindow extends JFrame {
	
	private int itemCounter = 8;
	private static int greenLabelsCounter = 100;
	private static int startItem = 8;
	private static int endItem = 36;
	private static boolean fullBag = false;

	private static final long serialVersionUID = 1L;
	Font f = new Font("sansSerif", Font.PLAIN, 13);
	Font f2 = new Font("sansSerif", Font.BOLD, 14);
	private static JScrollPane panelPane;
	private static JTextPane pane;
	private static JTextField input;

	private static JPanel menuPanel;
	private static JPanel first4;
	private static JPanel second4;
	private static JPanel statsPanel;
	private static JPanel items1;
	private static JPanel items2;
	private static JPanel items3;
	private static JPanel items4;
	private static JPanel items5;
	private static JPanel items6;
	private static JPanel allItems1;
	private static JPanel allItems2;
	private static JPanel allItems3;
	private static JPanel ingredients1;
	private static JPanel ingredients2;
	private static JLabel ingredients;
	private static JPanel mysIngredient;
	private static JLabel[] labels;
	private static JPanel pfittiz;
	private static JPanel ingrpan;
	private static JLabel[] labss;

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
		input.setPreferredSize(new Dimension(1000, 50));
		labels = new JLabel[38];
		for (int k = 0; k < labels.length; k++) {
			labels[k] = new JLabel("");
		}
		labels[0].setText("Life: ");
		labels[1].setText(0 + "");  ////////////////////////////////
		labels[2].setText("Deaths: ");
		labels[3].setText(0 + "");		
		labels[4].setText("Weapon: ");
		labels[5].setText("none");
		labels[6].setText("Item:  ");
		labels[6].setFont(f2);
		labels[7].setText("Q.ty:  ");
		labels[7].setFont(f2);
		labels[6].setOpaque(true);
		labels[7].setOpaque(true);
		labels[6].setBackground(Color.WHITE);
		labels[7].setBackground(Color.WHITE);
		menuPanel = new JPanel(new GridLayout(5,1));
		first4 = new JPanel(new GridLayout(3, 1));
		second4 = new JPanel(new GridLayout(3, 1));
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
		for(index = 0; index < 3; index++) {
			if(index==0){				
				first4.add(labels[index * 2]);
				JPanel pann = new JPanel(new GridLayout(1,100));
				labss = new JLabel[100];
				for(int z=0; z<labss.length; z++){
					labss[z] = new JLabel("");
					labss[z].setOpaque(true);
					pann.add(labss[z]);
					labss[z].setBackground(Color.GREEN);
				}
				second4.add(pann);
			}
			else{
			first4.add(labels[index * 2]);
			second4.add(labels[(index * 2) + 1]);
			}
		}
		statsPanel.add(first4, BorderLayout.WEST);
		statsPanel.add(second4, BorderLayout.CENTER);
		menuPanel.add(statsPanel);
		while(index < 7) {
			items1.add(labels[index * 2]);
			items2.add(labels[(index * 2) + 1]);
			index++;
		}
		while(index < 11) {
			items3.add(labels[(index * 2)]);
			items4.add(labels[(index * 2) + 1]);
			index++;
		}
		while(index < 15) {
			items5.add(labels[index * 2]);
			items6.add(labels[(index * 2) + 1]);
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
		ingrpan = new JPanel(new GridLayout(4,1));
		ingrpan.add(ingredients);
		ingrpan.add(ingredients1);
		ingrpan.add(ingredients2);
		ingrpan.add(mysIngredient);
		menuPanel.add(ingrpan);
		menuPanel.setBackground(Color.WHITE);
		menuPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
		menuPanel.setPreferredSize(new Dimension(300, 300));
		getContentPane().add(menuPanel, BorderLayout.WEST);
		getContentPane().add(panelPane, BorderLayout.CENTER);
		pfittiz = new JPanel(new BorderLayout());
		pfittiz.add(input, BorderLayout.CENTER);
		pfittiz.setBorder(BorderFactory.createEmptyBorder(0, 300, 0, 0));
		pfittiz.setBackground(Color.WHITE);
		getContentPane().add(pfittiz, BorderLayout.SOUTH);
		setVisible(true);
		setSize(new Dimension(1100, 680));
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

	public void addItemToMenu(Tool tool) {
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
	public static void decreaseLife(int life){
		if(life < (greenLabelsCounter)){
			greenLabelsCounter -= life;
			int i = 0;
			for(JLabel l : labss){
				if(i<greenLabelsCounter){
					l.setBackground(Color.GREEN);
				}
				else{
					l.setBackground(Color.RED);
				}
				i++;
			}
		}
	}
	public void increaseLife(int life){
		if((life + greenLabelsCounter) < 100){
			greenLabelsCounter += life;
			int i = 0;
			for(JLabel l : labss){
				if(i<greenLabelsCounter){
					l.setBackground(Color.GREEN);
				}
				else{
					l.setBackground(Color.RED);
				}
				i++;
			}
		}
	}
}
