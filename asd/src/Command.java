
public class Command {

	private String firstWord; //first part of the command
	private String secondWord; //second part of the command

	private static final String[] commandWords = 
		{ "help", "go", "drop", "pick up", "take", "leave", "examine", "attack", "equip",
			"speak", "talk", "talk to", "speak to"};
	
	public Command(String first, String second){
		firstWord = first;
		secondWord = second;
	}
	public Command(){
		// empty constructor
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
	 * @return a string containing all the availabel commands
	 */
	public String listCommands(){
		String ret = "";
		for(int i = 0; i < commandWords.length ; i++){
			if(i == commandWords.length -1){
				ret = ret + commandWords[i] + ". ";
			}
			else{
			ret = ret + commandWords[i] + ", ";
			}
		}
		return ret;
	}
	
	/**
	 * finds the instructions in the String and separeates them.
	 * @param instruction
	 * @return
	 */
	public String[] contanisInstruction(String instruction){
		String[] ret = new String[2];
		for(int i = 0; i < commandWords.length; i++){
		if(instruction.startsWith(commandWords[i])){
			ret[0] = commandWords[i];
			ret[1] = instruction.replace(commandWords[i], "");
		}
		}
		if(ret[0] == null){	
			ret[0] = "";
			ret[1] = "";
		}
		return ret;

	}
}
