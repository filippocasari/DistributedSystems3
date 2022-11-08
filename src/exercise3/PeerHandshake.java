package exercise3;
import exercise2.MessageHandShake;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class PeerHandshake extends Thread{
    Socket socket;
    int myPort;
    int itsPort;
    String itsIp;
    String myIp;
    int myId;
    private OutputStream os;
    private InputStream is;
    int itsId;
    ConcurrentHashMap<Integer, Boolean> concurrentLinkedQueueIds;
    PeerSender peerSender;
    public PeerHandshake(int myPort, String myIp, int itsPort, String itsIp, ConcurrentHashMap<Integer, Boolean> concurrentLinkedQueueIds) {
        this.itsIp=itsIp;
        this.myIp = myIp;
        this.itsPort = itsPort;
        this.myPort = myPort;
        //this.socket = socket;
        this.concurrentLinkedQueueIds = concurrentLinkedQueueIds;
    }
    public void run() {
        while(concurrentLinkedQueueIds.get(this.itsPort).equals(false)) {

                if (this.isPeerConnected()) {
                    System.out.println("The peer " + this.itsIp + ":" + this.itsPort + " is reachable");
                    break;
                } else {
                    System.out.println("The peer " + this.itsIp + ":" + itsPort + " is not reachable now\nwaiting 2 secs");
                    try {
                        sleep(2000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }


        }
        try {
            if (concurrentLinkedQueueIds.get(this.itsPort).equals(false)) {

                this.socket = new Socket(this.itsIp, this.itsPort);
                is = (this.socket
                        .getInputStream());
                os = (this.socket.getOutputStream());
                MessageHandShake messageHandshake = MessageHandShake.newBuilder().setId(this.myId).setError(false).build();
                messageHandshake.writeDelimitedTo(os);
                os.flush();
                MessageHandShake replyHandShake = MessageHandShake.parseDelimitedFrom(this.is);
                boolean error = replyHandShake.getError();
                if (error) {
                    System.err.println("error while establishing handshake");
                    System.err.println("Probably this id is already used");

                } else {
                    this.itsId = replyHandShake.getId();
                    concurrentLinkedQueueIds.replace(this.itsPort, false, true);
                    System.out.println("Okay Handshake successful with peer: " + this.itsId);

                /*peerSender = new PeerSender(myPort,myIp, itsPort, itsIp,itsId ,myId );
                //peerSender.start();
                try {
                    //peerSender.join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }*/
                }
            }else{
                System.out.println(" I already know this peer");
            }


        } catch (Exception e) {
            System.err.println("error while establish connection" + e);

        }
    }
    private boolean isPeerConnected() {
        try{

            this.socket = new Socket(this.itsIp, this.itsPort);

            System.out.println("Client is trying to connect to " + this.itsIp + ":" + this.itsPort);
            return true;

        }catch (Exception e){
            return false;
        }
    }
    public int getItsId(){
        return this.itsId;
    }
}
