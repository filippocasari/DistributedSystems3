package exercise1;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    private ServerSocket server;

    public Socket s;
    ConcurrentHashMap<Integer, Socket> clientsSocketsIds;

    public Server(int port) throws IOException{

        try {

            server = new ServerSocket(port);
            System.out.println("Server started");
            OperatorThread operator = new OperatorThread();
            clientsSocketsIds = new ConcurrentHashMap<>();
            operator.start();
            while (true) {

                s = server.accept();

                //message = Message.newBuilder().build();
                /*
                int id;
                id = Integer.parseInt(in.readLine());;

                while (list_clients.contains(id)) {
                    out.println("Insert a new id, this one is already present");
                    id = Integer.parseInt(in.readLine());;
                }
                list_clients.add(id);*/
                System.out.println("connection Established");
                //System.out.println("Just connected with the id client " + id);
                HandlingClientThread r = new HandlingClientThread(s, operator, clientsSocketsIds);
                r.start();

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        server.close();
    }


    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: server [port]");
            return;

        }
        Server server = new Server(Integer.parseInt(args[0]));

        System.out.println("Server shutting down...");

    }

}
