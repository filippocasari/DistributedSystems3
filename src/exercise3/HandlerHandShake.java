package exercise3;

import exercise2.MessageHandShake;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class HandlerHandShake extends Thread{

    Socket socket;
    private OutputStream os;
    private InputStream is;
    int myId;
    MessageHandShake messageHandShake;
    ConcurrentHashMap<Integer, Boolean> concurrentLinkedQueueIds;

    public HandlerHandShake(Socket socket, int myId, ConcurrentHashMap<Integer, Boolean> concurrentLinkedQueueIds) throws IOException {
        this.socket = socket;
        this.is = this.socket.getInputStream();
        this.os= this.socket.getOutputStream();
        this.myId = myId;
        this.concurrentLinkedQueueIds = concurrentLinkedQueueIds;
    }
    public void run() {
        try {
            messageHandShake = MessageHandShake.parseDelimitedFrom(this.is);
            if(messageHandShake==null){
                this.join();
            }
            int id_peer = messageHandShake.getId();
            MessageHandShake messageHandShake_response = MessageHandShake.newBuilder().setError(false).setId(this.myId).build();

            messageHandShake_response.writeDelimitedTo(os);
            this.concurrentLinkedQueueIds.put(this.socket.getPort(), true);
            this.join();
    }
        catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


    }
}
