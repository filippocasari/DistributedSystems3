package exercise3;


import exercise1.Message;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ClientPeerThread extends Thread{
    JFrame frame;
    TextArea textArea;
    //Socket socket;
    private OutputStream os;
    private InputStream is;
    String msg;
    int from_who;
    public ClientPeerThread() throws IOException {
        //this.socket = socket;
        frame = new JFrame("CHAT");


        JButton b = new JButton();
        b.setPreferredSize(new Dimension(40, 40));
        b.setText("clean chat");
        b.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.setText("");
            }
        });
        textArea = new TextArea();
        Container pane =frame.getContentPane();
        pane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();


        c.gridwidth = 1;
        c.weightx = .01;
        c.weighty = .2;
        c.gridx = 0;
        c.gridy = 1;
        pane.add(b, c);

        c.gridwidth = 2;
        c.weightx = .01;
        c.weighty = .2;
        c.gridx = 0;
        c.gridy = 1;
        pane.add(textArea, c);


        frame.add(b);
        frame.setSize(400,300);

        frame.setVisible(true);
        //is = (this.socket
        //        .getInputStream());
        //os = (this.socket.getOutputStream());

    }
    public void run(){

        while(!Thread.interrupted()){
            Message reply_from_server;
            /*try {
                reply_from_server = Message.parseDelimitedFrom(this.is);
                if(reply_from_server==null){
                    System.err.println("Reply from the server is null!!\n");
                }
                else{
                    from_who = reply_from_server.getFr();
                    if(from_who == 0) {
                        System.out.println("message from the server: " + msg);
                    }

                    msg = reply_from_server.getMsg();
                    appendTextArea(from_who, msg);
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }*/

        }
    }
    private synchronized void appendTextArea(int sender, String message){

        textArea.append("client "+sender+" says: "+message+"\n");
        frame.repaint();
    }
}

