import java.util.ArrayList;

public class Command{

	private String firstWord; //first part of the command
	private String secondWord; //second part of the command
	private static final String[] ARTICLES = {" il "," lo ", " la ", " i ", " gli ", " le "," col "," con "," a ", " ad ",
			" al ", " l'", " un ", " uno ", " una ", " di ", " da ", " in "};

	public static ArrayList<String> commandWords = new ArrayList<String>(){ 
		private static final long serialVersionUID = 1L;
	{
		add("aiuto"); add("attacca"); add("butta"); add("compra"); add("equipaggia"); add("esamina"); add("lascia");
		add("mangia"); add("parla"); add("prendi"); add("raccogli"); add("pronuncia"); add("vai");
	}
	};

	public Command(String first, String second){
		firstWord = first;
		secondWord = second;
	}
	
	public String getFirstWord(){
		return firstWord;
	}
	public String getSecondWord(){
		return secondWord;
	}
	
	public boolean hasFirstWord(){
		if(firstWord != null && !firstWord.equals(""))
			return true;
		return false;
	}
	public boolean hasSecondWord(){
		if(secondWord != null && !secondWord.equals(""))
			return true;
		return false;
	}
	
	/**
	 * makes a list of the commands
	 * @return a string containing all the available commands
	 */
	public String listCommands(){
		String ret = "";
		for(String com : commandWords){
			ret = ret + com + ",&nbsp; ";
		}
		ret = ret.substring(0, ret.length()-8) + ".";
		return ret;
	}
	
	/**
	 * dynamically add a new command to the list of commands
	 * @param toAdd
	 */
	public static void addCommand(String toAdd){
			commandWords.add(toAdd);
	}
	
	/**
	 * removes the String passed as parameter from the command list.
	 * @param toRem
	 */
	public static void removeCommand(String toRem, Player player){
		String com = "";
		if(toRem.equalsIgnoreCase("chiave") || toRem.equals("passepartout")){
			if(player.getToolFromString("passepartout") == null && player.getToolFromString("chiave") == null){
				com = "apri";
			}else{
				return;
			}
		}else if(toRem.equals("torcia") || toRem.equals("fiammiferi")){
			com = "accendi";
		}else if(toRem.equals("pezzo di mappa")){
			if(player.getToolFromString("pezzo di mappa") == null){
				com = "mappa";
			}else{
				return;
			}
		}else if(toRem.equals("pozione")){
			if(player.getToolFromString("pozione") == null){
				com = "bevi";
			}else{
		 		return;
			}
		 }
		for(String s : commandWords){
			if(s.equals(com)){
				commandWords.remove(s);
				return;
			}
		}
	}
	
	/**
	 * finds the instructions in the String and separates them.
	 * @param instruction
	 * @return
	 */
	public static String[] contanisInstruction(String instruction){
		String[] ret = new String[2];
		
		for(String s : commandWords){
		if(instruction.startsWith(s)){
			ret[0] = s;
			ret[1] = instruction.replace(s, "");
			if(!ret[1].equals("")){
				if(!ret[1].startsWith(" ")){
					ret[0] = null;
				}
				else{
					for (int i = 0; i < ARTICLES.length; i++){
						if(ret[1].startsWith(ARTICLES[i])){
							ret[1] = ret[1].replace(ARTICLES[i], " "); //remove articles
							i = -1; //keep checking for articles when removing one
						}
					}
				}
			}
		}
		}
		if(ret[0] == null){	
			ret[0] = "";
			ret[1] = "";
		}
		return ret;

	}
}