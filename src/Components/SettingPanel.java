package Components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SettingPanel {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Search Dialog Demo");
        frame.setSize(500, 500);
        frame.setLocation(50, 0);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = boxDemo();
        frame.setContentPane(panel);
        frame.setVisible(true);
    }

    public static class MyPanel extends JPanel {
        Color color;
        String name;

        MyPanel(Color color, String name) {
            this.color = color;
            this.name = name;
            this.setBorder(BorderFactory.createLineBorder(Color.GREEN));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D gg = (Graphics2D) g.create();
            gg.setColor(this.color);
            int x, y, w, h;
            x = y = 0;
            w = this.getWidth();
            h = this.getHeight();
            gg.fillRect(x, y, w, h);
            gg.setColor(Color.BLACK);
            gg.drawString(name, w / 2, h / 2);
            gg.dispose();
        }
    }

    public static JPanel boxDemo() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JPanel panel1 = new MyPanel(Color.RED, "Panel1");
        panel.add(panel1);
        panel1.setMinimumSize(new Dimension(100, 100));
        panel1.setPreferredSize(new Dimension(100, 100));
        panel1.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        //panel.add( new JSeparator(JSeparator.HORIZONTAL));
        JPanel panel2 = new MyPanel(Color.BLUE, "Panel2");
        panel.add(panel2);
        return panel;
    }

    public static class RemoveButton extends JButton {
        private static Listener listener;

        static {
            listener = new Listener();
        }

        RemoveButton(String text) {
            super(text);
            this.addActionListener(listener);
        }

        public static class Listener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                // do something useful
            }
        }
    }

    public static JPanel setttingPanel() {
        JPanel panel = new JPanel();
        return panel;
    }
}
