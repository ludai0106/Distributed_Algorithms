/**
 * Created by ludai on 16/11/27.
 */
public class Message {
    private int sender;
    private int receiver;
    private Clock clock;
    private Buffer buffer;

    public Message(int sender, int receiver, Clock clock, Buffer buffer){
        this.sender = sender;
        this.receiver = receiver;
        this.clock = clock;
        this.buffer = buffer;
    }

    public int getSender(){
        return sender;
    }

    public int getReceiver(){
        return receiver;
    }

    public Clock getClock(){
        return clock;
    }
}
