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
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListSet;

public class Peer {
    int id;

    final String ip = "127.0.0.1";
    int port;
    private OutputStream os;
    private InputStream is;
    Socket socket;


    Scanner scanner;
    InputStream in ;
    OutputStream out ;
    ConcurrentHashMap<Integer, Socket> otherPeersSocket;
    ConcurrentHashMap<Integer, Boolean> concurrentHashMapIdsKnown;
    List<String> s;


    public Peer(int id, int port, List<String> s) throws IOException {
        this.id = id;
        this.port = port;
        scanner = new Scanner(System.in);
        otherPeersSocket = new ConcurrentHashMap<>();
        this.s = s;
        this.concurrentHashMapIdsKnown = new ConcurrentHashMap<Integer, Boolean>();


        //ClientPeerThread clientPeerThread = new ClientPeerThread();
        //clientPeerThread.start();



    }
    private void startingPeer() throws IOException, InterruptedException {


        PeerHandshake[] arrayPeerHandshake = new PeerHandshake[s.size()];
        for(int i=0; i<s.size(); i=i+2){
            System.out.println(i);
            try {

                int port_peer = Integer.parseInt((s.get(i+1)));
                this.concurrentHashMapIdsKnown.put(port_peer, false);
                String ip =s.get(i);
                //socket1 = new Socket(, Integer.parseInt());
                //otherPeers.add(socket1);
                ServerPeer serverPeer = new ServerPeer(this.port, concurrentHashMapIdsKnown);
                serverPeer.start();
                arrayPeerHandshake[i] = new PeerHandshake(this.port, this.ip, port_peer, ip, this.concurrentHashMapIdsKnown);

                arrayPeerHandshake[i].start();


            }catch (Exception e){
                System.err.println("Peer "+s.get(i)+ " is not reachable" );
                e.printStackTrace();
            }

        }


        ClientPeerThread clientPeerThread = new ClientPeerThread();
        clientPeerThread.start();
        /*socket = new Socket(this.ip, this.port);
        is = (this.socket
                .getInputStream());
        os = (this.socket.getOutputStream());*/


    }



    public static void main(String[] args) throws IOException, InterruptedException {
        List<String> list = new ArrayList<>();
        if(args.length>2){
            list.addAll(Arrays.asList(args).subList(2, args.length));
        }
        System.out.println(list);
        Peer peer = new Peer(Integer.parseInt(args[0]), Integer.parseInt(args[1]), list);

        peer.startingPeer();
    }
}
