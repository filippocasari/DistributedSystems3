package exercise1;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;


public class Client2 {

    private final String ip;
    private final int port;
    private OutputStream os;
    private InputStream is;
    Socket socket;
    Message message;
    String message_string;
    int id;
    Scanner scanner = new Scanner(System.in);
    String inputString = "";
    int id_client_to_send;

    public Client2(String ip, int port, int id,int id_client_to_send, String msg) {
        this.port = port;
        this.id = id;
        this.ip = String.valueOf(ip);
        this.message_string = msg;
        this.id_client_to_send = id_client_to_send;

    }

    public void readServerResponse() throws InterruptedException {
        HandlerMessagesClient handlerMessagesClient = null;
        try {
            socket = new Socket(this.ip, this.port);
            is = (this.socket
                    .getInputStream());
            os = (this.socket.getOutputStream());
            System.out.println("Client is trying to connect to " + this.ip + ":" + this.port);

            // Handshake process
            try{
                exercise1.MessageHandShake messageHandshake = exercise1.MessageHandShake.newBuilder().setId(this.id).setError(false).build();
                messageHandshake.writeDelimitedTo(os);
                os.flush();
                exercise1.MessageHandShake reply_HandShake = exercise1.MessageHandShake.parseDelimitedFrom(this.is);
                boolean error = reply_HandShake.getError();
                if(error){
                    System.err.println("error while establishing handshake");
                    System.err.println("Probably this id is already used");
                    return ;
                }
                else {
                    System.out.println("ACK from server:\n"+reply_HandShake);
                }
            }catch (Exception e){
                System.err.println("error while establish connection"+ e);
                return ;

            }



            handlerMessagesClient= new HandlerMessagesClient(this.socket);
            handlerMessagesClient.start();
            message = createMessage(this.id_client_to_send, this.message_string);
            message.writeDelimitedTo(os);
            os.flush();
            while (!inputString.equals("end")) {

                System.out.println("Hey client "+this.id+" insert the receiver");
                int to = Integer.parseInt(scanner.nextLine());
                System.out.println("Hey client "+this.id+" insert the message");
                inputString = scanner.nextLine();
                message = createMessage(to, inputString);
                message.writeDelimitedTo(os);
                os.flush();

            }
            handlerMessagesClient.join();




        } catch (Exception e) {
            System.err.println("something went wrong");
        }
        finally {
            if(handlerMessagesClient!=null){
                handlerMessagesClient.join();
            }

        }

    }
    private Message createMessage(int to, String msg){

        return Message.newBuilder().setFr(this.id).setTo(to).setMsg(msg).build();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        if (args.length < 5) {
            System.out.println("Usage: server [ip][port][ownID][id][msg]");
            System.exit(0);
        }
        String message;
        StringBuilder sb = new StringBuilder();
        for(int i=4; i< args.length; i++)
        {
            sb.append(args[i]).append(" ");
        }
        message = sb.toString();
        Client2 client = new Client2(args[0], Integer.parseInt(args[1]),Integer.parseInt(args[2]) , Integer.parseInt(args[3]) , message);
        client.readServerResponse();

        System.out.println("Server shutting down...");


    }
}
