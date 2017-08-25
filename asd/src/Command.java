import java.util.ArrayList;

public class Command{

	private String firstWord; //first part of the command
	private String secondWord; //second part of the command
	
	private final static String[] articoli= {" il "," lo ", " la ", " i ", " gli ", " le "," col "," con "," a " };

	public static ArrayList<String> commandWords = new ArrayList<String>(){
		private static final long serialVersionUID = 1L;
	{
	    add("aiuto"); add("vai"); add("vai a"); add("lascia"); add("butta"); add("prendi"); add("esamina"); add("attacca");
	    add("equipaggia"); add("parla"); add("pronuncia"); add("usa"); add("compra");
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
			ret = ret + com + ",&nbsp;";
		}
		ret = ret.substring(0, ret.length()-7) + ".";
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
	public static void removeCommand(String toRem){
		String com = "";
		if(toRem.equalsIgnoreCase("chiave") || toRem.equals("passepartout"))
			com = "apri";
		else if(toRem.equals("torcia"))
			com = "accendi";
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
		for(int i = 0; i<articoli.length; ++i){
			if(instruction.contains(articoli[i])){
				instruction = instruction.replace(articoli[i], " ");
			}
		}
		for(String s : commandWords){
			
			
		if(instruction.startsWith(s)){
			ret[0] = s;
			ret[1] = instruction.replace(s, "");
			if(!ret[1].equals("")){
				if(!ret[1].startsWith(" ")){
					ret[0] = null;
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
