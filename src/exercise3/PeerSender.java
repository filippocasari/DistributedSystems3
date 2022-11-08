package exercise3;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

import exercise1.Message;
import exercise2.MessageHandShake;

public class PeerSender extends Thread{
    Scanner scanner;
    String inputString = "";
    Message message;
    int id;
    Socket peer;
    int to;
    int port;
    String ip;
    private OutputStream os;
    private InputStream is;


    public PeerSender(int id, String ip, int port) {
        this.id = id;
        this.port = port;
        this.ip = ip;

    }
    public void run(){
        scanner = new Scanner(System.in);
        int to;
        String msg;
        do {
            System.out.println("Hey client " + this.id + " insert [receiver][msg]");
            inputString = scanner.nextLine();
            to = Integer.parseInt(inputString.substring(0, 1));
            msg = inputString.substring(1);
            if(isPeerConnected()){
                System.out.println("The peer "+to+" is reachable");
                break;
            }else{
                System.out.println("The peer "+to+" is not reachable now\nwaiting 3 secs");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }while(true);

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
            System.out.println("Hey client " + this.id + " insert [receiver][msg]");
            inputString = scanner.nextLine();
            to = Integer.parseInt(inputString.substring(0, 1));
            msg = inputString.substring(1);
        }
    }

    private boolean isPeerConnected() {
        try{
            peer = new Socket(this.ip, this.port);

            System.out.println("Client is trying to connect to " + this.ip + ":" + this.port);
            return true;

        }catch (Exception e){
            return false;
        }
    }

    private Message createMessage(int to, String msg){

        return Message.newBuilder().setFr(this.id).setTo(to).setMsg(msg).build();
    }
}
