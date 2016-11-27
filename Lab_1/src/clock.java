/**
 * Created by ludai on 16/11/27.
 */
public class Clock {
    private int[] timeVector;
    public Clock(int size){
        timeVector = new int[size];
    }

    public void increase(int i){
        timeVector[i]++;
    }

    public void decrease(int i){
        timeVector[i]--;
    }

    public void update(int[] receivedVector){
        for(int i=0;i<receivedVector.length;i++){
            if (receivedVector[i]>timeVector[i])timeVector[i]=receivedVector[i];
        }
    }

    public void compare(int[] receivedVector){
    }
}
