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
		int[] vectorS = sender.vector;
		int[] vectorR = receiver.vector;		
		int[] max = new int[vectorS.length];
		for(int i = 0; i < vectorS.length;i++){
			max[i] = Math.max(vectorS[i], vectorR[i]);
		}
		
		return new Clock(max);
	}
	
	public boolean Equal(Clock clock){
		int[] v1 = this.vector;
		int[] v2 = clock.vector;
		for(int i = 0; i < v1.length;i++){
			if(v1[i] != v2[i])
				return false;			
		}
		return true;
	}
	
	public boolean greaterEqual(Clock clock){
		int[] v1 = this.vector;
		int[] v2 = clock.vector;
		for(int i = 0; i < v1.length;i++){
			if(v1[i] < v2[i])
				return false;			
		}
		return true;
	}
	
	
	public boolean receiveCondition(Clock clock){
		int[] v1 = this.vector;
		int[] v2 = clock.vector;
		int count = 0;
//		for(int i=0;i<v2.length;i++){
//			if(v2[i]==0)count++;
//		}
//		if(count==(v2.length-1))return true;
		
		count=0;
		for(int i = 0; i < v1.length;i++){
			if(v1[i] > v2[i])
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
