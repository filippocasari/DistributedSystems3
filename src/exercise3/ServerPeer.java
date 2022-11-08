package exercise3;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class ServerPeer extends Thread{
    private ServerSocket server;
    private int port;
    private int myId;
    ConcurrentHashMap<Integer, Boolean> concurrentHashMapIdsKnown;

    public ServerPeer(int port , ConcurrentHashMap<Integer, Boolean> concurrentHashMapIdsKnown) {
        this.port = port;
        this.concurrentHashMapIdsKnown = concurrentHashMapIdsKnown;
    }
    public void run(){
        try {
            server = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        while(true){
            Socket s;
            try {
                s = server.accept();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println("connection Established");
            try {
                handleHandshake(s);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
    }
    private void handleHandshake(Socket s) throws IOException, InterruptedException {

        HandlerHandShake handlerHandShake = new HandlerHandShake(s, this.myId, this.concurrentHashMapIdsKnown);
        handlerHandShake.start();
        //messageHandShake_response= exercise2.MessageHandShake.newBuilder().setError(true).setId(this.id_server).build();

        //messageHandShake_response.writeDelimitedTo(out);
    }
}
