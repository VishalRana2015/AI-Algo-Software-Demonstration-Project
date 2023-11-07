import javax.swing.*;
import java.awt.*;
import javax.swing.event.*;
import java.awt.event.*;

public class mainFrame {
    public static MapComp comp = new MapComp(10, 20);
    private static JScrollPane pane = new JScrollPane();
    private static JPanel mainpanel, controlpanel, viewpanel;
    private static JButton play = new JButton("Play");
    private static JFrame frame;
    private static Thread thread;
    private static JComboBox<String> list;

    private static void initialize() {
        frame = new JFrame();
        frame.setLocation(0, 0);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        comp = new MapComp(10, 20);
        controlpanel = cp1();
        frame.setContentPane(controlpanel);
        play.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MapComp comp2 = new MapComp(10, 10);
                try {
                    comp2 = (MapComp) comp.clone();
                } catch (CloneNotSupportedException ee) {
                    System.out.println("CloneNotSupportedException ");
                }
                viewpanel = new JPanel();
                viewpanel.setLayout(new BoxLayout(viewpanel, BoxLayout.Y_AXIS));
                GridBagConstraints c = new GridBagConstraints();
                JPanel insidePanel = new JPanel();
                insidePanel.setLayout(new GridBagLayout());

                insidePanel.add(comp2, c);
                JScrollPane pane = new JScrollPane();
                JViewport viewport = new JViewport();
                viewport.add(insidePanel);
                pane.setViewport(viewport);

                viewpanel.add(pane);
                JLabel status = new JLabel("Status");
                status.setMaximumSize(status.getPreferredSize());
                viewpanel.add(status);
                JButton stop = new JButton("Stop");
                stop.setMaximumSize(stop.getPreferredSize());
                viewpanel.add(stop);
                frame.setContentPane(viewpanel);
                MapComp finalComp = comp2;
                stop.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        frame.setContentPane(controlpanel);
                        System.out.println("stopping thread : "+ finalComp.thread.getName());
                        frame.revalidate();
                    }
                });

                frame.revalidate();
                int i = list.getSelectedIndex();
                System.out.println("i : " + i);
                if (i == 0) {
                    //AlgoDemo.runBSF(comp2);
                    AlgoDemo.runBSF1(comp2);
                    System.out.println("thread : " + thread.getName() + " running...");
                }
                if (i == 1)
                    AlgoDemo.runBFS(comp2);
                if (i == 2)
                    AlgoDemo.runDFS(comp2);
            }
        });
        frame.setVisible(true);
        frame.pack();
    }

    public static void main(String[] args) {
        initialize();
    }
    public static JPanel cp1() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        JViewport viewport = new JViewport();
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(10, 10, 10, 10);
        c.anchor = GridBagConstraints.CENTER;
        p.add(comp, c);
        viewport.setView(p);
        pane.setViewport(viewport);
        comp.setCellDim(40, 40);
        pane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        pane.setMinimumSize(new Dimension(300, 300));
        //pane.setMaximumSize( comp.getMaximumSize());
        panel.add(pane);
        JPanel controlpanel = compPanel();
        controlpanel.setMinimumSize(controlpanel.getPreferredSize());
        controlpanel.setMaximumSize(new Dimension((int) controlpanel.getPreferredSize().getWidth(), Integer.MAX_VALUE));
        controlpanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        panel.add(controlpanel);
        return panel;
    }

    public static JPanel compPanel() {
        JPanel panel = new JPanel();
        JPanel buttonpanel = new JPanel();
        buttonpanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        JButton importbutton = new JButton("Import");
        importbutton.setToolTipText("Import configuration from file.city");
        JButton exportbutton = new JButton("Export");
        exportbutton.setToolTipText("Export configuration to file.city");
        buttonpanel.add(importbutton);
        buttonpanel.add(exportbutton);
        buttonpanel.setMinimumSize(buttonpanel.getPreferredSize());
        buttonpanel.setMaximumSize(buttonpanel.getPreferredSize());
        buttonpanel.setMaximumSize(buttonpanel.getPreferredSize());

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(Box.createVerticalStrut(5));
        panel.add(buttonpanel);
        panel.add(Box.createVerticalStrut(5));
        JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
        //separator.setPreferredSize(new Dimension(Integer.MAX_VALUE, (int)separator.getPreferredSize().getHeight()));
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, (int) separator.getPreferredSize().getHeight()));
        panel.add(separator);
        panel.add(Box.createVerticalStrut(5));


        JPanel sizePanel = new JPanel();
        sizePanel.setLayout(new GridBagLayout());
        JLabel rowlabel = new JLabel("Set Rows : ");
        JLabel collabel = new JLabel("Set Columns : ");
        JSpinner rowspinner = new JSpinner(new SpinnerNumberModel(10, 1, 1000, 1));
        JSpinner colspinner = new JSpinner(new SpinnerNumberModel(10, 1, 1000, 1));
        GridBagConstraints cont = new GridBagConstraints();
        cont.gridx = 0;
        cont.gridy = 0;
        cont.weightx = 1;
        cont.weighty = 1;
        cont.gridheight = 1;
        cont.gridwidth = 1;
        cont.anchor = GridBagConstraints.EAST;
        sizePanel.add(rowlabel, cont);
        cont.gridx = 1;
        cont.anchor = GridBagConstraints.WEST;
        sizePanel.add(rowspinner, cont);
        cont.gridx = 0;
        cont.gridy = 1;
        cont.anchor = GridBagConstraints.EAST;
        sizePanel.add(collabel, cont);
        cont.gridx = 1;
        cont.gridy = 1;
        cont.anchor = GridBagConstraints.WEST;
        sizePanel.add(colspinner, cont);
        sizePanel.setMaximumSize(sizePanel.getPreferredSize());
        //sizePanel.setMinimumSize(sizePanel.getPreferredSize());


        panel.add(sizePanel);
        panel.add(Box.createVerticalStrut(5));
        JSeparator separator2 = new JSeparator(JSeparator.HORIZONTAL);
        //separator.setPreferredSize(new Dimension(Integer.MAX_VALUE, (int)separator.getPreferredSize().getHeight()));
        separator2.setMaximumSize(new Dimension(Integer.MAX_VALUE, (int) separator2.getPreferredSize().getHeight()));
        panel.add(separator2);
        panel.add(Box.createVerticalStrut(5));
        JPanel cellsizepanel = new JPanel();
        JLabel cellwidthlabel = new JLabel("Cell Width : ");
        JLabel cellheightlabel = new JLabel("Cell height : ");
        JSpinner cellwidthspinner = new JSpinner(new SpinnerNumberModel(10, 1, 40, 1));
        JSpinner cellheightspinner = new JSpinner(new SpinnerNumberModel(10, 1, 40, 1));
        cellsizepanel.setLayout(new GridBagLayout());
        cont.gridx = 0;
        cont.gridy = 0;
        cont.anchor = GridBagConstraints.EAST;
        cellsizepanel.add(cellwidthlabel, cont);
        cont.gridx = 1;
        cont.anchor = GridBagConstraints.WEST;
        cellsizepanel.add(cellwidthspinner, cont);
        cont.gridx = 0;
        cont.gridy = 1;
        cont.anchor = GridBagConstraints.EAST;
        cellsizepanel.add(cellheightlabel, cont);
        cont.gridx = 1;
        cont.anchor = GridBagConstraints.WEST;
        cellsizepanel.add(cellheightspinner, cont);
        cellsizepanel.setMaximumSize(cellsizepanel.getPreferredSize());
        panel.add(cellsizepanel);
        panel.add(Box.createVerticalStrut(5));


        JSeparator separator6 = new JSeparator(JSeparator.HORIZONTAL);
        separator6.setMaximumSize(new Dimension(Integer.MAX_VALUE, (int) separator6.getPreferredSize().getHeight()));

        panel.add(separator6);
        panel.add(Box.createVerticalStrut(5));

        list = new JComboBox();
        list.addItem("Best First Search");
        list.addItem("Breadth First Search");
        list.addItem("Depth First Search");
        list.addItem("A* Search");
        list.addItem("Hill Climbing");
        JPanel algoPanel = new JPanel();
        algoPanel.setLayout(new GridBagLayout());
        GridBagConstraints cont2 = new GridBagConstraints();
        cont2.gridx = 0;
        cont2.gridy = 0;
        algoPanel.add(new JLabel("Select Algorithm"), cont2);
        list.setMinimumSize(list.getPreferredSize());
        list.setMaximumSize(list.getPreferredSize());
        cont2.gridy = 1;
        algoPanel.add(list, cont2);
        algoPanel.setMaximumSize(algoPanel.getPreferredSize());
        panel.add(algoPanel);
        algoPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        panel.add(Box.createVerticalStrut(5));
        JSeparator separator3 = new JSeparator(JSeparator.HORIZONTAL);
        //separator.setPreferredSize(new Dimension(Integer.MAX_VALUE, (int)separator.getPreferredSize().getHeight()));
        separator3.setMaximumSize(new Dimension(Integer.MAX_VALUE, (int) separator3.getPreferredSize().getHeight()));
        panel.add(separator3);
        panel.add(Box.createVerticalStrut(5));
        JPanel editpanel = new JPanel();
        editpanel.setLayout(new GridBagLayout());
        JButton editcomp = new JButton("Edit City");
        JCheckBox checkSrc = new JCheckBox("Source");
        JCheckBox checkDst = new JCheckBox("Destination");
        JCheckBox checkObs = new JCheckBox("Obstacle");
        checkDst.setEnabled(false);
        checkSrc.setEnabled(false);
        checkObs.setEnabled(false);
        ButtonGroup group = new ButtonGroup();
        group.add(checkDst);
        group.add(checkSrc);
        group.add(checkObs);
        editcomp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton button = (JButton) e.getSource();
                String text = button.getText();
                if (text.equalsIgnoreCase("edit city")) {
                    checkDst.setEnabled(true);
                    checkSrc.setEnabled(true);
                    checkObs.setEnabled(true);
                    button.setText("Stop Editing");
                    comp.setEditMode(true);
                } else {
                    checkDst.setEnabled(false);
                    checkObs.setEnabled(false);
                    checkSrc.setEnabled(false);
                    button.setText("Edit City");
                    comp.setEditMode(false);
                }
            }
        });

        checkDst.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JCheckBox box = (JCheckBox) e.getSource();
                if (box.isSelected()) {
                    comp.setMode(MapComp.SETDSTMODE);
                }
            }
        });
        checkSrc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JCheckBox box = (JCheckBox) e.getSource();
                if (box.isSelected())
                    comp.setMode(MapComp.SETSRCMODE);
            }
        });
        checkObs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JCheckBox box = (JCheckBox) e.getSource();
                if (box.isSelected())
                    comp.setMode(MapComp.SETOBSTACLEMODE);
            }
        });
        cont.gridwidth = 3;
        cont.anchor = GridBagConstraints.CENTER;
        cont.gridx = 0;
        cont.gridy = 0;
        editpanel.add(editcomp, cont);
        cont.gridwidth = 1;
        cont.gridy = 1;
        editpanel.add(checkSrc, cont);
        cont.gridx = 1;
        editpanel.add(checkObs, cont);
        cont.gridx = 2;
        editpanel.add(checkDst, cont);
        editpanel.setMaximumSize(editpanel.getPreferredSize());
        panel.add(editpanel);
        JSeparator separator5 = new JSeparator();
        separator5.setMaximumSize(new Dimension(Integer.MAX_VALUE, (int) separator5.getPreferredSize().getHeight()));
        panel.add(Box.createVerticalStrut(5));
        panel.add(separator5);
        panel.add(Box.createVerticalStrut(5));
        panel.add(play);
        panel.add(Box.createVerticalStrut(5));
        JSeparator separator4 = new JSeparator();
        separator4.setMaximumSize(new Dimension(Integer.MAX_VALUE, (int) separator4.getPreferredSize().getHeight()));
        panel.add(separator4);
        panel.add(Box.createVerticalGlue());
        rowspinner.setValue(comp.getRows());
        colspinner.setValue(comp.getCols());
        cellwidthspinner.setValue(comp.getCellDim());
// Names of controls in this panel
        // rowspinner, colspinner, play , importbutton, exportbutton, list, checkSrc, checkDst, checkObs, editcomp
        cellwidthspinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSpinner sp = (JSpinner) e.getSource();
                int w = (int) sp.getValue();
                comp.setCellDim(w, w);
            }
        });
        rowspinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSpinner sp = (JSpinner) e.getSource();
                int val = (int) sp.getValue();
                comp.setDim(val, comp.getCols());
            }
        });
        colspinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSpinner sp = (JSpinner) e.getSource();
                int val = (int) sp.getValue();
                comp.setDim(comp.getRows(), val);
            }
        });
        JPanel panel2 = new JPanel();
        //panel2.setLayout(null);
        panel2.setMinimumSize(panel.getPreferredSize());
        panel2.setMaximumSize(new Dimension((int) panel.getPreferredSize().getWidth(), Integer.MAX_VALUE));
        panel2.add(panel);
        return panel;
    }
}

