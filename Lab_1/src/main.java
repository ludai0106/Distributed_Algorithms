import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Main {

    public static void main(String[] args) throws RemoteException {
       if(args!=null){}
       else {
           int processNum = 1;
           int rounds = 1;
           int Id = 0;
           if (System.getSecurityManager() == null) {
               System.setSecurityManager(new SecurityManager());
           }

           Registry registry;
           if (Id==0)registry = LocateRegistry.createRegistry(40001);

           String[] remotes = new String[processNum];
           for (int i=0;i<processNum;i++){
               remotes[i] = String.format("rmi://%s:40001/%s", "localhost", i);
           }

           Thread threads = null;

           Process process = new Process(Id,rounds,remotes);

           try {
               InOrderEndpoint endpoint = new InOrderEndpoint(new TrollEndpoint(ownId, ROUNDS, remotes), ROUNDS, remotes.length);
               //RandomDelaySenderEndpoint endpoint = new RandomDelaySenderEndpoint(i, remotes, ROUNDS);

               System.out.println("Binding node " + ownId);
               Naming.bind(remotes[ownId], new DefaultEndpointBuffer(endpoint));
               endpointThread = new Thread(endpoint);
           } catch (RemoteException | AlreadyBoundException | MalformedURLException e) {
               e.printStackTrace();
           }




       }
    }
}
