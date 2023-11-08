import javax.swing.*;
import java.awt.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.util.jar.JarEntry;

public class mainFrame {
    public static MapComp comp ;
    private static JScrollPane pane = new JScrollPane();
    private static JPanel controlpanel, viewpanel;
    private static JButton play = new JButton("Play");
    private static JFrame frame;
    private static Thread thread;
    private static JComboBox<String> list;

    // static values


    // The graph is made up of squares I call them cell, the below given static value is used to set initial width of those cells.
    //public static int INITIAL_CELL_DIM = 30;
    public static int INITIAL_INSET = 10;

    // Represents scrollPane initial size
    public static int PANE_MINIMUM_SIZE = 300;
    // The left side of the menu is contains several controls to edit the graph, each control is placed in a vertical order and the following value is used to set space between those controls
    public static int VERTICAL_STRUCT_HEIGHT = 10;

    // When user clicks on the play button. A new thread is created that start searching the path to the given destination based on the selected algorithm.
    // It displays cells that have been processed in RED color
    // Cells that are yet to be processed in YELLOW color.
    // You can control how fast or slow you want the thread to perform using the DELAY button.
    // Following static values is used to set constraints and values on Delay button.
    public static int DELAY_MIN = 0;
    public static int DELAY_MAX = 10000;
    public static int DELAY_INITIAL = 5000;
    public static int DELAY_STEP = 50;

    // Below given four properties are used to set constraints on the cell size, that can be set from the UI.
    public static int CELL_SIZE_INITIAL = 40;
    public static int CELL_SIZE_MIN = 10;
    public static int CELL_SIZE_MAX = 40;
    public static int CELL_SIZE_STEP = 1;

    // Below given two properties are used to set initial number of rows and columns in the graph.
    public static int INITIAL_ROWS =  20 ;
    public static int INITIAL_COLS = 20;

    private static void initialize() {
        frame = new JFrame();
        frame.setLocation(0, 0);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        comp = new MapComp(INITIAL_ROWS, INITIAL_COLS);
        controlpanel = cp1();
        frame.setContentPane(controlpanel);
        play.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MapComp comp2 = null;
                try {
                    comp2 = (MapComp) comp.clone();
                } catch (CloneNotSupportedException ee) {
                    System.out.println("CloneNotSupportedException ");
                }
                if ( comp2 == null){
                    throw new RuntimeException("Something went wrong");
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
                stop.setMinimumSize(stop.getPreferredSize());
                viewpanel.add(stop);
                frame.setContentPane(viewpanel);
                MapComp finalComp = comp2;
                stop.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        frame.setContentPane(controlpanel);
                        frame.revalidate();
                        finalComp.algoRunnerRunnable.setExit(true);
                    }
                });

                frame.revalidate();
                int i = list.getSelectedIndex();
                System.out.println("i : " + i);
                if (i == AlgoDemo.BEST_FIRST_SEARCH) {
                    AlgoDemo.runBestFirstSearch(comp2);
                }
                else if (i == AlgoDemo.BREADTH_FIRST_SEARCH) {
                    AlgoDemo.runBreadthFirstSearch(comp2);
                }
                else if(i == AlgoDemo.DEPTH_FIRST_SEARCH) {
                    AlgoDemo.runDepthFirstSearch(comp2);
                }
                else if( i == AlgoDemo.A_STAR){
                    AlgoDemo.runAStarSearch(comp2);
                }
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
        c.insets = new Insets(INITIAL_INSET, INITIAL_INSET, INITIAL_INSET, INITIAL_INSET);
        c.anchor = GridBagConstraints.CENTER;
        p.add(comp, c);
        viewport.setView(p);
        pane.setViewport(viewport);
        comp.setCellDim(CELL_SIZE_INITIAL, CELL_SIZE_INITIAL);
        pane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        pane.setMinimumSize(new Dimension(PANE_MINIMUM_SIZE, PANE_MINIMUM_SIZE));
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
        panel.add(Box.createVerticalStrut(VERTICAL_STRUCT_HEIGHT));
        panel.add(buttonpanel);
        panel.add(Box.createVerticalStrut(VERTICAL_STRUCT_HEIGHT));
        JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
        //separator.setPreferredSize(new Dimension(Integer.MAX_VALUE, (int)separator.getPreferredSize().getHeight()));
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, (int) separator.getPreferredSize().getHeight()));
        panel.add(separator);
        panel.add(Box.createVerticalStrut(VERTICAL_STRUCT_HEIGHT));


        JPanel sizePanel = new JPanel();
        sizePanel.setLayout(new GridBagLayout());
        JLabel rowlabel = new JLabel("Set Rows : ");
        JLabel collabel = new JLabel("Set Columns : ");
        JSpinner rowspinner = new JSpinner(new SpinnerNumberModel(INITIAL_ROWS, 1, 1000, 1));
        JSpinner colspinner = new JSpinner(new SpinnerNumberModel(INITIAL_COLS, 1, 1000, 1));
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
        panel.add(Box.createVerticalStrut(VERTICAL_STRUCT_HEIGHT));
        JSeparator separator2 = new JSeparator(JSeparator.HORIZONTAL);
        //separator.setPreferredSize(new Dimension(Integer.MAX_VALUE, (int)separator.getPreferredSize().getHeight()));
        separator2.setMaximumSize(new Dimension(Integer.MAX_VALUE, (int) separator2.getPreferredSize().getHeight()));
        panel.add(separator2);
        panel.add(Box.createVerticalStrut(VERTICAL_STRUCT_HEIGHT));
        JPanel cellsizepanel = new JPanel();
        JLabel cellwidthlabel = new JLabel("Cell Size : ");
//        JLabel cellheightlabel = new JLabel("Cell height : ");
        JLabel delayLabel = new JLabel("Delay (ms): ");
        JSpinner cellwidthspinner = new JSpinner(new SpinnerNumberModel(CELL_SIZE_INITIAL,CELL_SIZE_MIN, CELL_SIZE_MAX, CELL_SIZE_STEP));
//        JSpinner cellheightspinner = new JSpinner(new SpinnerNumberModel(10, 1, 40, 1));
        JSpinner delaySpinner = new JSpinner(new SpinnerNumberModel(DELAY_INITIAL, DELAY_MIN, DELAY_MAX, DELAY_STEP));
        cellsizepanel.setLayout(new GridBagLayout());
        cont.gridx = 0;
        cont.gridy = 0;
        cont.anchor = GridBagConstraints.EAST;
        cellsizepanel.add(cellwidthlabel, cont);
        cont.gridx = 1;
        cont.anchor = GridBagConstraints.WEST;
        cellsizepanel.add(cellwidthspinner, cont);
//        cont.gridx = 0;
//        cont.gridy = 1;
//        cont.anchor = GridBagConstraints.EAST;
//        cellsizepanel.add(cellheightlabel, cont);
//        cont.gridx = 1;
//        cont.anchor = GridBagConstraints.WEST;
//        cellsizepanel.add(cellheightspinner, cont);
        cellsizepanel.setMaximumSize(cellsizepanel.getPreferredSize());
        panel.add(cellsizepanel);
        panel.add(Box.createVerticalStrut(VERTICAL_STRUCT_HEIGHT));


        JSeparator separator6 = new JSeparator(JSeparator.HORIZONTAL);

        separator6.setMaximumSize(new Dimension(Integer.MAX_VALUE, (int) separator6.getPreferredSize().getHeight()));

        panel.add(separator6);
        JPanel delayPanel = new JPanel();
        delayPanel.setLayout(new GridBagLayout());
        cont.gridx = 0;
        cont.gridy = 0;
        cont.anchor = GridBagConstraints.EAST;
        delayPanel.add(delayLabel, cont);
        cont.gridx = 1;
        cont.anchor = GridBagConstraints.WEST;
        delayPanel.add(delaySpinner, cont);
        panel.add(Box.createVerticalStrut(VERTICAL_STRUCT_HEIGHT));
        delayPanel.setMaximumSize(delayPanel.getPreferredSize());
        panel.add(delayPanel);
        panel.add(Box.createVerticalStrut(VERTICAL_STRUCT_HEIGHT));
        JSeparator separator8 = new JSeparator(JSeparator.HORIZONTAL);
        separator8.setMaximumSize(new Dimension(Integer.MAX_VALUE, (int) separator8.getPreferredSize().getHeight()));
        panel.add(separator8);
        panel.add(Box.createVerticalStrut(VERTICAL_STRUCT_HEIGHT));

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
        panel.add(Box.createVerticalStrut(VERTICAL_STRUCT_HEIGHT));
        JSeparator separator3 = new JSeparator(JSeparator.HORIZONTAL);
        separator3.setMaximumSize(new Dimension(Integer.MAX_VALUE, (int) separator3.getPreferredSize().getHeight()));
        panel.add(separator3);
        panel.add(Box.createVerticalStrut(VERTICAL_STRUCT_HEIGHT));
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
        panel.add(Box.createVerticalStrut(VERTICAL_STRUCT_HEIGHT));
        panel.add(separator5);
        panel.add(Box.createVerticalStrut(VERTICAL_STRUCT_HEIGHT));
        panel.add(play);
        panel.add(Box.createVerticalStrut(VERTICAL_STRUCT_HEIGHT));
        JSeparator separator4 = new JSeparator();
        separator4.setMaximumSize(new Dimension(Integer.MAX_VALUE, (int) separator4.getPreferredSize().getHeight()));
        panel.add(separator4);
        panel.add(Box.createVerticalGlue());
        rowspinner.setValue(comp.getRows());
        colspinner.setValue(comp.getCols());
        delaySpinner.setValue(comp.getDelay());
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

        delaySpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                System.out.println("delaySpinner change event");
                JSpinner sp = (JSpinner) e.getSource();
                int val = (int) sp.getValue();
                comp.setDelay(val);
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

