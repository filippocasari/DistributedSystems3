package exercise1;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class HandlingClientThread extends Thread {

    public final int id_server = 1;
    String line = null;
    InputStream in ;
    OutputStream out ;
    OutputStream out_tmp;
    Socket s;
    OperatorThread operatorThread;
    Message message;
    ConcurrentHashMap<Integer, Socket> clientsSocketsIds;

    /**
     * Set the client thread.
     * <p>
     *
     * 2022-10-17
     */
    public HandlingClientThread(Socket socket, OperatorThread operatorThread, ConcurrentHashMap<Integer, Socket> cHM) {
        this.s = socket;

        //this.id = id;
        this.operatorThread = operatorThread;
        this.clientsSocketsIds = cHM;

    }

    /**
     * Run thread.
     * <p>
     *
     * 2022-10-17
     */
    public void run() {

        System.out.println("starting thread to handle client");
        try {

            this.in = this.s.getInputStream();
            this.out = this.s.getOutputStream();

        } catch (IOException e) {
            System.out.println("IO error in server thread");
        }
        MessageHandShake messageHandShake;
        try {
            messageHandShake = MessageHandShake.parseDelimitedFrom(this.in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        int id_client = messageHandShake.getId();
        try {

            MessageHandShake messageHandShake_response;
            boolean isContainedAlready = operatorThread.oneMoreClient(id_client);
            if(isContainedAlready){
                messageHandShake_response= MessageHandShake.newBuilder().setError(true).setId(this.id_server).build();

                messageHandShake_response.writeDelimitedTo(out);
                System.err.println("this id is already used");
                System.out.println(operatorThread.getListClient());
                return;
            }else{

                System.out.println("this id is not already used");

                messageHandShake_response= MessageHandShake.newBuilder().setError(false).setId(id_client).build();
                messageHandShake_response.writeDelimitedTo(out);
                if(this.clientsSocketsIds !=null){
                    this.clientsSocketsIds.put(id_client, this.s);
                }
                System.out.println(this.clientsSocketsIds);

            }
            System.out.println("waiting message...");
            message = Message.parseDelimitedFrom(this.in);

            int from = message.getFr();
            int to = message.getTo();
            String msg = message.getMsg();

            Message msgForwarded;
            while (msg.compareTo("end") != 0) {

                System.out.println("client " + from+ " replied to client: " +to+ ": "+ msg);
                Socket idClienteleForward = this.clientsSocketsIds.get(to);
                if(idClienteleForward!=null){
                    msgForwarded = message;

                    out_tmp = idClienteleForward.getOutputStream();
                    msgForwarded.writeDelimitedTo(out_tmp);
                    out_tmp.flush();
                }


                message = Message.parseDelimitedFrom(this.in);
                if(message==null){
                    System.err.print("Message received is null!!\n");
                    break;
                }else{
                    from = message.getFr();
                    to = message.getTo();
                    msg = message.getMsg();
                }


            }
            operatorThread.oneLessClient(id_client);

            clientsSocketsIds.remove(id_client);
            //this.list_clients.remove(id_client);
        } catch (IOException e) {

            line = this.getName(); //reused String line for getting thread name
            System.out.println("IO Error/ Client " + line + " terminated abruptly");
            operatorThread.oneLessClient(id_client);

            if(clientsSocketsIds!=null){
                clientsSocketsIds.remove(id_client);
            }
        } catch (NullPointerException e) {
            e.printStackTrace(System.err);
            line = this.getName(); //reused String line for getting thread name
            System.out.println("Client " + line + " Closed");
            operatorThread.oneLessClient(id_client);
            if(clientsSocketsIds!=null){
                clientsSocketsIds.remove(id_client);
            }


        } finally {

            System.out.println("Connection Closing..");
        }//end finally
    }



}
