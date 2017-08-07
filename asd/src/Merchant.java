import java.io.Serializable;
import java.util.HashMap;

public class Merchant extends NPC implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private int priceModifier;
	String saleTable;
	HashMap<String,Integer> saleMap;
	
	public Merchant(String name, String description, int HP, int money, String speech, int priceModifier) {
		super(name, description, HP, money, false, speech);		
		this.priceModifier = priceModifier;
		saleTable = "<BR><BR><table>"
				  + "<tr><th>Name</th><th>Damage</th><th>Precision</th><th>Price</th><th>Q.ty</th></tr>"+
					"</table>";
		saleMap = new HashMap<String, Integer>();
	}
	
	public String displayForSale(){ 
		return saleTable;
	}

	public String interact(Character pl) {
		return "";
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
	
	public String removeObjCalled(String o) {
		
		Tool b = null;
		for (int i = 0; i < itemsHeld.size(); i++) {
			if (itemsHeld.get(i).getName().equals(o)) {
				b = itemsHeld.get(i);
				itemsHeld.remove(i);
				break;
			}
		}
		if (b == null) {
			return null;
		} else {
			String rep = createStringForTable(b);
			int num;
			if((num = saleMap.get(o).intValue()) > 1){
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
	
	public void addObj(Tool t) {
		
		boolean b = true;
		if(!saleMap.containsKey(t.getName())){	
			b = false;
			saleMap.put(t.getName(), 1);
		}
		
		String rep = createStringForTable(t);
		
		itemsHeld.add(t);
		
		if(!b){				
			saleTable = saleTable.replace("</table>", (rep + "</table>"));
		}
		else{
			saleMap.replace(t.getName(), (Integer)(saleMap.get(t.getName()).intValue()) +1);
			saleTable = saleTable.replace(rep, (rep.replaceAll("<td>" + ((Integer)(saleMap.get(t.getName()).intValue()) -1) +
					"</td>", "<td>" + saleMap.get(t.getName()) + "</td>")));
		}
	}
	
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
