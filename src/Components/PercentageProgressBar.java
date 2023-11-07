package Components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PercentageProgressBar extends JProgressBar {
    PercentageProgressBar(int min, int max) {
        super(min, max);
        this.setOrientation(JProgressBar.HORIZONTAL);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D gg = (Graphics2D) g.create();
        gg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        gg.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        int x, y, w, h;
        x = this.getInsets().left;
        y = this.getInsets().top;
        w = this.getWidth() - (this.getInsets().left + this.getInsets().right);
        h = this.getHeight() - (this.getInsets().top + this.getInsets().bottom);
        int min, max, val;
        min = this.getMinimum();
        max = this.getMaximum();
        val = this.getValue();
        int per = ((val - min) * 100) / (max - min);
        String s = per + "%";
        int size = 0;
        if (w > h)
            size = h;
        else
            size = w;
        gg.setFont(new Font(Font.SERIF, Font.BOLD, size));
        int sw = gg.getFontMetrics(gg.getFont()).stringWidth(s);
        gg.setColor(Color.CYAN);
        gg.fillRect(x, y, w, h);
        gg.setColor(Color.BLACK);
        int a, d;
        a = gg.getFontMetrics(gg.getFont()).getAscent();
        d = gg.getFontMetrics(gg.getFont()).getDescent();

        gg.drawString(s, w / 2 - sw / 2, h / 2 + a / 2 - d / 2);
        gg.dispose();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Percentable Progress Bar Test");
        frame.setLocation(50, 0);
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JButton button = new JButton("Start the action");
        JPanel panel = new JPanel();
        panel.add(button);
        PercentageProgressBar bar = new PercentageProgressBar(100, 300);
        bar.setPreferredSize(new Dimension(300, 100));
        panel.add(bar);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Runnable runn = new Runnable() {
                    @Override
                    public void run() {
                        int min, max, val;
                        min = bar.getMinimum();
                        max = bar.getMaximum();
                        int diff;
                        diff = max - min;

                        for (int i = min; i <= max; i = i + 10) {
                            bar.setValue(i);
                            try {
                                Thread.currentThread().sleep(300);
                            } catch (Exception ee) {
                                System.out.println("Excepiton thrown : " + ee.getMessage());
                            }
                        }
                    }
                };
                Thread thread = new Thread(runn);
                thread.start();
                System.out.println("Thread started");
            }
        });
        frame.setContentPane(panel);
        frame.setVisible(true);
    }
}
