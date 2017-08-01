import java.util.Stack;

/**
 * this class allows the use of the history in the textbox.
 * @author giacomobenso
 */
public class EStack extends Stack<String>{

	private static final long serialVersionUID = 1L;
	int size;
	Stack<String> s2;
	
	public EStack(int size){
		super();
		this.size = size;
		s2 = new Stack<>();
	}
	
	public String pop(){
		if(!this.isEmpty()){		
			return s2.push(super.pop());
		}
		return null;
	}
	public String push(String s){
		if (this.size() > size){
			this.clean();
		}
		return super.push(s);
	}
	
	public Stack<String> getS2(){
		return s2;
	}
	
	public String pushFromS2(){
		if(s2.isEmpty()){
			return null;
		}
		return push(s2.pop());
	}
	
	public void reset(){
		while(!s2.isEmpty()){
			push(s2.pop());
		}

	}
	
	public void clean(){
		if(this.size() < size){
			return;
		}
		while(!s2.isEmpty()){
			push(s2.pop());
		}
		for(int i = 0; i< size; i++){
			s2.push(super.pop());
		}
		this.clear();
		while(!s2.isEmpty()){
			push(s2.pop());
		}
	}
	
}
