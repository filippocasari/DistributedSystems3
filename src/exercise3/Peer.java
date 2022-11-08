package exercise3;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

public class Peer {
    String id;

    final String ip = "127.0.0.1";
    int port;
    private OutputStream os;
    private InputStream is;
    Socket socket;
    private ServerSocket server;
    Scanner scanner;
    ConcurrentHashMap<Integer, Socket> otherPeersSocket;
    ConcurrentSkipListSet<Socket> otherPeers;
    Socket socket1;

    public Peer(String id, int port, List<String> s) throws IOException {
        this.id = id;
        this.port = port;
        scanner = new Scanner(System.in);
        otherPeersSocket = new ConcurrentHashMap<>();
        for(int i=0; i<s.size(); i=i+2){
            System.out.println(i);
            try {

                int port_peer = Integer.parseInt((s.get(i+1)));

                String ip =s.get(i);
                //socket1 = new Socket(, Integer.parseInt());
                //otherPeers.add(socket1);
                PeerSender peerSender = new PeerSender(Integer.parseInt(this.id), ip, port_peer);
                peerSender.start();

            }catch (Exception e){
                System.err.println("Peer "+s.get(i)+ " is not reachable" );
                e.printStackTrace();
            }
        }
        ClientPeerThread clientPeerThread = new ClientPeerThread();
        clientPeerThread.start();



    }
    private void startingPeer() throws IOException {
        ClientPeerThread clientPeerThread = new ClientPeerThread();
        clientPeerThread.start();
        /*socket = new Socket(this.ip, this.port);
        is = (this.socket
                .getInputStream());
        os = (this.socket.getOutputStream());*/
        server = new ServerSocket(port);
        while(true){
            Socket s = server.accept();
            System.out.println("connection Established");

        }

    }

    public static void main(String[] args) throws IOException {
        List<String> list = new ArrayList<>();
        if(args.length>2){
            list.addAll(Arrays.asList(args).subList(2, args.length));
        }
        System.out.println(list);
        Peer peer = new Peer(args[0], Integer.parseInt(args[1]), list);

        peer.startingPeer();
    }
}
