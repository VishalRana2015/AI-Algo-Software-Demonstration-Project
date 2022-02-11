import javax.swing.*;
import java.awt.*;
import javax.swing.border.Border;
import javax.swing.event.*;
import java.awt.event.*;
import java.util.*;

public class SearchDialog {
    public static void main(String[] args) {
        final2();
    }
    public static JPanel  SearchDialog(){
        JPanel mainpanel =new JPanel();
        mainpanel.setLayout( new CardLayout(5,5));
        JPanel searchPanel = new JPanel();
        JPanel addPanel = new JPanel();
        searchPanel.setLayout( new BoxLayout(searchPanel, BoxLayout.Y_AXIS));
        JTextField userID =new JTextField();
        userID.setColumns(20);
        userID.setMinimumSize(userID.getPreferredSize());
        userID.setMaximumSize(userID.getPreferredSize());
        JPanel tempPanel = new JPanel();
        tempPanel.add(new JLabel("UserID :"));
        tempPanel.add(userID);
        tempPanel.setPreferredSize(new Dimension( (int)tempPanel.getPreferredSize().getWidth(),110));
        tempPanel.setMinimumSize(new Dimension((int)tempPanel.getPreferredSize().getWidth(), 110));
        tempPanel.setMaximumSize(tempPanel.getMaximumSize());
        searchPanel.add(tempPanel);

        searchPanel.add( new JSeparator(JSeparator.HORIZONTAL));
        JPanel temppanel2 = new JPanel();
        temppanel2.setLayout( new FlowLayout(FlowLayout.RIGHT));
        JButton search = new JButton("Search");
        JButton cancel = new JButton("Cancel");
        temppanel2.add(search);
        temppanel2.add(cancel);
        temppanel2.setMinimumSize(tempPanel.getPreferredSize());
        temppanel2.setMaximumSize(tempPanel.getMaximumSize());
        searchPanel.add(temppanel2);
        mainpanel.add(searchPanel, "searchpanel");
        searchPanel.setBorder( BorderFactory.createLineBorder(Color.red));
        mainpanel.setBorder(BorderFactory.createLineBorder(Color.CYAN));

        addPanel.setLayout( new BoxLayout(addPanel,BoxLayout.Y_AXIS));
        JLabel icon = new JLabel(  );
        icon.setPreferredSize(new Dimension(100,100));
        icon.setMinimumSize(new Dimension(100,100));
        icon.setMaximumSize(new Dimension(100,100));
        icon.setBorder( BorderFactory.createLineBorder( Color.BLACK,2));
        JPanel panel2temp1 = new JPanel();
        JPanel panel2temp2= new JPanel();
        panel2temp1.add(icon);
        panel2temp1.add( new JLabel("set Name :"));
        JTextField username = new JTextField();
        username.setColumns(20);
        panel2temp1.add(username);
        panel2temp1.setMinimumSize(panel2temp1.getPreferredSize());
        panel2temp1.setMaximumSize(panel2temp1.getPreferredSize());
        addPanel.add(panel2temp1);
        addPanel.add( new JSeparator(JSeparator.HORIZONTAL));
        panel2temp2.setLayout( new FlowLayout(FlowLayout.RIGHT));
        JButton add = new JButton("Add");
        JButton cancel2 = new JButton("Cancel");
        panel2temp2.add(add);
        panel2temp2.add(cancel2);
        panel2temp2.setMinimumSize(panel2temp2.getPreferredSize());
        panel2temp2.setMaximumSize( panel2temp2.getPreferredSize());
        addPanel.add(panel2temp2);
        addPanel.setBorder( BorderFactory.createLineBorder( Color.RED));
        mainpanel.add(addPanel, "addpanel");
        return mainpanel;

    }
    public static void final1(){
        JFrame frame = new JFrame("Search Dialog Demo");
        frame.setSize( 500, 500);
        frame.setLocation(50,0);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel= SearchDialog();
        JPanel framepanel = new JPanel();
        framepanel.add(panel);
        JButton toggle = new JButton("toggle");
        toggle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout layout  = (CardLayout)panel.getLayout();
                layout.next(panel);
            }
        });
        framepanel.add(toggle);
        frame.setContentPane(framepanel);

        frame.setVisible(true);
    }
    public static void final2(){
        JFrame frame = new JFrame("Search Dialog Demo");
        frame.setSize( 500, 500);
        frame.setLocation(50,0);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        JButton button = new JButton("Add a new Friend");
        panel.add(button);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPanel panel = SearchDialog();
                JDialog dialog = new JDialog(frame);
                dialog.setModal(true);
                dialog.setContentPane(panel);
                dialog.setLocation(0,0);
                dialog.setLocationRelativeTo(frame);
                dialog.pack();
                dialog.setResizable(false);
                dialog.setVisible(true);

            }
        });
        frame.setContentPane(panel);
        frame.setVisible(true);
    }
}
