import java.util.ArrayList;

public class Command{

	private String firstWord; //first part of the command
	private String secondWord; //second part of the command
	private static final String[] ARTICLES = {" to ", " at ", " from ", " the ", " a ", " an ", " with "};

	public static ArrayList<String> commandWords = new ArrayList<String>(){ 
		private static final long serialVersionUID = 1L;
	{   // all the possible commands are stored here
	    add("help"); add("go"); add("pick up"); add("take"); add("drop"); add("leave"); add("examine"); add("attack");
	    add("equip"); add("speak"); add("talk");  add("say"); add("buy"); add("eat");
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
		if(!commandWords.contains(toAdd))
			commandWords.add(toAdd);
	}
	
	/**
	 * removes the String passed as parameter from the command list.
	 * @param toRem
	 */
	public static void removeCommand(String toRem, Player player){
		String com = "";
		if(toRem.equals("key") || toRem.equals("skeleton key")){
			if(player.getToolFromString("skeleton key") == null && 
					player.getToolFromString("key") == null){
				com = "open";
			}
			else{
				return;
			}			
		}
		else if(toRem.equals("matches")){
			com = "light up";
		}
		else if(toRem.equals("map piece")){
			if(player.getToolFromString("map piece") == null){
				com = "map";
			}
			else{
				return;
			}
		}
		else if(toRem.equals("potion")){
			if(player.getToolFromString("potion") == null){
				com = "drink";
			}
			else{
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
				}else{
					for (int i = 0; i < ARTICLES.length; i++){
						if(ret[1].startsWith(ARTICLES[i])){
							ret[1] = ret[1].replaceFirst(ARTICLES[i], " "); //remove articles
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
