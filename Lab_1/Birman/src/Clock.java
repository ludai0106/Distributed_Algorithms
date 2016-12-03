import java.io.Serializable;

public class Clock implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = -8902584971912817060L;
	private int[] vector;

	public Clock(int size){
		vector = new int[size];
	}
	
	public Clock(int[] vector){
		this.vector = vector;
	}
	
	public void increment(int i){
		vector[i]++;
	}
	
	public Clock compareClocks(Clock sender, Clock receiver){
		
		int[] max = new int[sender.vector.length];
		for(int i = 0; i < sender.vector.length;i++){
			max[i] = Math.max(sender.vector[i], receiver.vector[i]);
		}
		
		return new Clock(max);
	}
	
	public boolean Equal(Clock clock){
		for(int i = 0; i < vector.length;i++){
			if(vector[i] != clock.vector[i])
				return false;			
		}
		return true;
	}
	
	public boolean greaterEqual(Clock clock){
		for(int i = 0; i < vector.length;i++){
			if(vector[i] < clock.vector[i])
				return false;			
		}
		return true;
	}
	
	
	public boolean receiveCondition(Clock clock){
		int count = 0;

		for(int i = 0; i < vector.length;i++){
			if(vector[i] >clock.vector[i])
				count++;			
		}
		return (count <= 1);
	}
	
	public String toString(){
		String s = "[";
		for(int i = 0; i < vector.length; i++){
			s = s + vector[i] + ",";
		}
		s = s.substring(0, s.length()-1);
		s = s + "]";
		return s;
	
	}
	

}
