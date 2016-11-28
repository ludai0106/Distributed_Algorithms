/**
 * Created by ludai on 16/11/28.
 */
public class Process {
    private int processId;
    private String[] remotes;
    private Clock clock;
    public Process(int processId, int rounds,  String[] remotes){
        this.processId = processId;
        this.remotes = remotes;
        this.clock = new Clock(remotes.length);
    }

    public int getProcessId(){
        return processId;
    }

    public Clock getClock(){
        return clock;
    }

    public void deliverMessage(Message message){}

    public void broadcast(Object message){}
}
