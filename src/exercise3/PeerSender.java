package exercise3;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

import exercise1.Message;
import exercise2.MessageHandShake;

public class PeerSender extends Thread{
    public int itsPort;
    Scanner scanner;
    String inputString = "";
    Message message;
    int myId;
    int itsId;

    String itsIp;
    Socket peer;
    int to;
    int myPort;
    String myIp;

    private OutputStream os;
    private InputStream is;


    public PeerSender(int myPort, String myIp, int itsPort, String itsIp,int itsId , int myId) {
        this.itsId = itsId;
        this.itsIp = itsIp;
        this.itsPort = itsPort;
        this.myId = myId;
        this.myIp = myIp;
        this.myPort = myPort;

    }
    public void run(){
        scanner = new Scanner(System.in);
        int to;
        String msg;

        System.out.println("Hey client " + this.myId + " insert [receiver][msg]");
        inputString = scanner.nextLine();
        to = Integer.parseInt(inputString.substring(0, 1));
        msg = inputString.substring(1);


        while (!inputString.equals("end")) {

            message = createMessage(to, msg);
            try {
                message.writeDelimitedTo(os);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                os.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Hey client " + this.myId + " insert [receiver][msg]");
            inputString = scanner.nextLine();
            to = Integer.parseInt(inputString.substring(0, 1));
            msg = inputString.substring(1);
        }
    }



    private Message createMessage(int to, String msg){

        return Message.newBuilder().setFr(this.myId).setTo(to).setMsg(msg).build();
    }
}
