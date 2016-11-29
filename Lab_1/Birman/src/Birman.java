import java.util.Random;

public class Birman implements Runnable{
	private Process currentProcess;
	private double startTime;
	private int index;
	private int messageNum;
	public Birman(Process process, double time, int index){
		this.currentProcess=process;
		this.startTime=time;
		this.index=index;
	}


	@Override
	public void run() {
		try{
		
			while( (System.currentTimeMillis() - startTime) < 4000){
				System.out.println(currentProcess.getClock().toString());
				currentProcess.getClock().increase(index);
				System.out.println("index: " + index + " " + currentProcess.getClock().toString());
				Clock currentClock = currentProcess.getClock();
				messageNum++;
				
				String currentId = Integer.toString(currentProcess.getProcessId());
				String message = messageNum + " Lam Gao Sou ";
				Message m = new Message(currentId, message, currentClock);
				
				currentProcess.broadcastMessage(m);
				
				Thread.sleep(randomNumber(5,500));
			}
			
		}catch (Exception e) {
			System.out.println("Exception in broadCast " + e);
		}		
	}
	
	public int randomNumber(int low, int high){
		Random r = new Random();
		return r.nextInt(high-low) + low;
	}

	

}
