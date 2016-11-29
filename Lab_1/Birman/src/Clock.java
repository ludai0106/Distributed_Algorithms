import java.io.Serializable;
public class Clock implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2018730575680054787L;
	private int[] vector;

	public Clock(int size){
		vector = new int[size];
	}
		
	public Clock(int[] clock){
		this.vector = clock;
	}
		
	public  void increase(int i){
		vector[i]++;
	}
	
	public synchronized void update(Clock clock2){
	    for(int i=0;i<vector.length;i++){
	        if (clock2.vector[i]>vector[i])vector[i]=clock2.vector[i];
	    }
	}
	 
	public boolean greaterEqual(Clock senderClock){
		for(int i = 0; i < vector.length;i++){
			if(vector[i]<senderClock.vector[i])
				return false;			
		}
		return true;
	}
		
	public boolean sendCondition(Clock receiverClock){
		int count = 0;
		for(int i = 0; i < vector.length;i++){
			if(vector[i] > receiverClock.vector[i])
				count++;			
		}
		return (count <= 1);
	}
	
	public String toString(){
		String s = "[";
		for(int i:vector){
			s = s + i + ",";
		}
		s = s.substring(0, s.length()-1) + "]";
		return s;
	
	}
		

}
