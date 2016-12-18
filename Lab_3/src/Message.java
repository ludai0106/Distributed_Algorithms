import java.io.Serializable;

public class Message implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -4731898095466253355L;
	//0 stands for N message
	//1 stands for P message
	private int type;
	private int w;
	private int round;


	public Message(int type, int w, int round) {

		this.type = type;
		this.w = w;
		this.round = round;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getW() {
		return w;
	}

	public int getRound() {
		return round;
	}

	public void setRound(int round) {
		this.round = round;
	}

	public void setW(int w) {
		this.w = w;
	}

}
