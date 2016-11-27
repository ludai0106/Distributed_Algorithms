public class Main {

    public static void main(String[] args) {
       if(args!=null){}
       else {
           int processNum = 3;
           String[] remotes = new String[processNum];
           for (int i=0;i<processNum;i++){
               remotes[i] = String.format("rmi://%s:40001/%s", "localhost", i);
           }
       }
    }
}
