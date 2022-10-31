package exercise1;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class OperatorThread extends Thread{
    JTextField txt;
    JTextField list;
    int num_clients=0;
    JFrame frame;
    public List<Integer> list_clients = new ArrayList<Integer>();

    /**
     * Generate the operator thread .
     * <p>
     *
     * 2022-10-17
     */
    public OperatorThread() {
        frame = new JFrame("Server Operator Thread");
        JPanel panel = new JPanel();
        Border border = BorderFactory.createLineBorder(Color.BLUE, 5);

        frame.add(panel);
        panel.setOpaque(true); // content panes must be opaque
        panel.setBorder(new TitledBorder(new EtchedBorder(),
                "Clients Monitor"));

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300,300);
        txt = new JTextField("clients connected: "+num_clients+"  ");
        txt.setBorder(border);
        txt.setFont(new Font("verdana", Font.BOLD, 15));
        panel.add(txt);
        list = new JTextField("List of client ids: \t\t\t\t\t\t\t\t");
        list.setBounds(new Rectangle(0,0,100, 200));
        panel.add(list);
        GridBagConstraints c = new GridBagConstraints();
        c.weightx=10.0;
        c.fill=GridBagConstraints.HORIZONTAL;
        panel.add(list,c);

        frame.add(panel, BorderLayout.NORTH);

        frame.pack();
        frame.setVisible(true);

    }

    /**
     * Add one more client
     * <p>
     *
     * 2022-10-17
     */
    public synchronized boolean oneMoreClient(int id_client){

        if(list_clients.contains(id_client)){
            return true;
        }
        this.num_clients++;
        list_clients.add(id_client);
        System.out.println("adding new client ");
        //frame.getContentPane().removeAll();

        txt.setText("clients connected: "+this.num_clients+"  ");
        System.out.println("List of clients"+this.list_clients);
        list.setText("List of client ids: \t"+ this.list_clients);
        //frame.add(txt);
        //SwingUtilities.updateComponentTreeUI(frame);
        //frame.revalidate();

        //frame.revalidate();
        frame.repaint();
        return false;
    }
    public synchronized ArrayList<Integer> getListClient() {
        return (ArrayList<Integer>) list_clients;
    }
    /**
     * Clear one client.
     * <p>
     *
     * 2022-10-17
     */
    public synchronized void oneLessClient(int client_id){

        try{
            list_clients.remove((Integer) client_id);
            this.num_clients--;
            System.out.println("List of clients\t"+this.list_clients);
            System.out.println("removing one client ");

            txt.setText("clients connected: "+this.num_clients+"  ");
            list.setText("List of client ids: \t"+ this.list_clients);
        }
        catch (Exception e){
            System.out.println("id not present in the list");
            e.printStackTrace(System.err);

        }




        //frame.revalidate();
        //frame.add(txt);
        //SwingUtilities.updateComponentTreeUI(frame);
        //frame.revalidate();
        frame.repaint();

    }
}
