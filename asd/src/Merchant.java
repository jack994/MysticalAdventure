import java.io.Serializable;
import java.util.HashMap;

/**
 * the NPC who sells tools and wapons
 * @author giacomobenso
 */
public class Merchant extends NPC implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private int priceModifier;
	String saleTable; //html table containing all the items to sell
	HashMap<String,Integer> saleMap; //containing item name and quantity
	
	public Merchant(String name, String description, int HP, int money, String speech, int priceModifier) {
		super(name, description, HP, money, false, speech);		
		this.priceModifier = priceModifier;
		saleTable = "<BR><BR><table>"
				  + "<tr><th>Name</th><th>Damage</th><th>Precision</th><th>Price</th><th>Q.ty</th></tr>"+
					"</table>";
		saleMap = new HashMap<String, Integer>();
	}
	
	public String interact(Character pl) {
		return speech;
	}
	
	public String displayForSale(){ //getSaleTable
		return saleTable;
	}
	
	public String getSpeech(){
			return speech + displayForSale();
	}

	public int getPriceModifier() {
		return priceModifier;
	}

	public void setPriceModifier(int priceModifier) {
		this.priceModifier = priceModifier;
	}
	
	/**
	 * remove the item from the merchant table and from the hashmap
	 */
	public String removeObjCalled(String o) {
		
		Tool b = null;
		for (int i = 0; i < itemsHeld.size(); i++) {
			if (itemsHeld.get(i).getName().equals(o)) {
				b = itemsHeld.get(i);
				itemsHeld.remove(i);
				break;
			}
		}
		if (b == null) { // the item does not exist
			return null;
		} else { 
			String rep = createStringForTable(b);
			int num;
			if((num = saleMap.get(o).intValue()) > 1){ //quantity > 1
				saleTable = saleTable.replace(rep, (rep.replaceAll("<td>" + saleMap.get(o) +
						"</td>", "<td>" + ((Integer)(saleMap.get(o).intValue()) -1) + "</td>")));
				saleMap.replace(o, num - 1);
			}
			else{ 
				saleTable = saleTable.replace(rep, ""); 
				saleMap.remove(o);
			}			
			return this.getName() + " dropped " + b;
		}
	}
	
	/**
	 * add an item to the merchant table and to the hashmap
	 */
	public void addObj(Tool t) {
		
		boolean b = true;
		if(!saleMap.containsKey(t.getName())){	
			b = false;
			saleMap.put(t.getName(), 1);
		}
		
		String rep = createStringForTable(t);
		
		itemsHeld.add(t);
		
		if(!b){				
			saleTable = saleTable.replace("</table>", (rep + "</table>")); // add it to the table if there wasn't yet
		}
		else{ // change the quantity 
			saleMap.replace(t.getName(), (Integer)(saleMap.get(t.getName()).intValue()) +1);
			saleTable = saleTable.replace(rep, (rep.replaceAll("<td>" + ((Integer)(saleMap.get(t.getName()).intValue()) -1) +
					"</td>", "<td>" + saleMap.get(t.getName()) + "</td>")));
		}
	}
	
	/**
	 * create the html string for the table of the given tool
	 * @param t: the tool
	 * @return the html string
	 */
	public String createStringForTable(Tool t){
		
		String rep = "<tr><td>" + t.getName() + "</td>";	
		
		if(t.getClass() == Weapon.class)
			rep += "<td>" + ((Weapon) t).getDamage() + "</td><td>" + ((Weapon)t).getPrecision() + "</td>";
		else 
			rep += "<td>n/a</td><td>n/a</td>";
		
		rep += "<td>" + (t.getValue() * priceModifier) + "</td>" +
		"<td>" + saleMap.get(t.getName()) + "</td></tr>";
		
		return rep;
	}

}
