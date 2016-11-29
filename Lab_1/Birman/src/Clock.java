import java.io.Serializable;
public class Clock implements Serializable {
	private static final long serialVersionUID = 1L;
	private int[] clock;

	public Clock(int size){
		clock = new int[size];
	}
		
	public Clock(int[] clock){
		this.clock = clock;
	}
		
	public synchronized void increase(int i){
		clock[i]++;
	}
		
	public synchronized void decrease(int i){
		clock[i]--;
	}
	
	 public synchronized void update(Clock clock2){
	        for(int i=0;i<clock.length;i++){
	            if (clock2.clock[i]>clock[i])clock[i]=clock2.clock[i];
	        }
	    }
	 
	public boolean greaterEqual(Clock senderClock){
		for(int i = 0; i < clock.length;i++){
			if(clock[i]<senderClock.clock[i])
				return false;			
		}
		return true;
	}
		
	public boolean sendCondition(Clock receiverClock){
		int count = 0;
		for(int i = 0; i < clock.length;i++){
			if(clock[i] > receiverClock.clock[i])
				count++;			
		}
		return (count <= 1);
	}
	
	public String toString(){
		String s = "[";
		for(int i:clock){
			s = s + clock[i] + ",";
		}
		s = s.substring(0, s.length()-1) + "]";
		return s;
	
	}
		

}
