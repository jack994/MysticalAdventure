import java.util.ArrayList;

public class Command{

	private String firstWord; //first part of the command
	private String secondWord; //second part of the command

	private static ArrayList<String> commandWords = new ArrayList<String>(){
		private static final long serialVersionUID = 1L;
	{
	    add("help"); add("go"); add("drop"); add("take"); add("pick up"); add("leave"); add("examine"); add("attack");
	    add("equip"); add("speak"); add("talk"); add("talk to"); add("speak to"); add("say"); add("use"); add("buy");
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
		if(toRem.equalsIgnoreCase("key") || toRem.equals("passepartout"))
			com = "open";
		else if(toRem.equals("torch"))
			com = "light up";
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
		}
		}
		if(ret[0] == null){	
			ret[0] = "";
			ret[1] = "";
		}
		return ret;

	}
}
