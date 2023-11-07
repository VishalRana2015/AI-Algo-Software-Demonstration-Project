import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.*;

public class MapComp extends JComponent implements Cloneable {
    @Override
    protected Object clone() throws CloneNotSupportedException {
        MapComp comp = new MapComp();
        comp.group = (CellGroup) this.group.clone();
        if (cells != null) {
            comp.cells = new Cell[cells.length][cells[0].length];
            for (int i = 0; i < cells.length; i++) {
                for (int j = 0; j < cells[0].length; j++) {
                    comp.cells[i][j] = (Cell) cells[i][j].clone();
                    comp.cells[i][j].setGroup(group);
                }
            }
        }
        if (src != null)
            comp.src = comp.cells[ this.src.row][this.src.col];

        if (dst != null)
            comp.dst = comp.cells[this.dst.row][this.dst.col];

        comp.obstacles = new LinkedList<Cell>();
        if (obstacles != null) {
            for (int i = 0; i < obstacles.size(); i++) {
                Cell obstacle =obstacles.get(i);
                comp.obstacles.add( comp.cells[obstacle.row][obstacle.col]);
            }
        }
        comp.manager = new MapCompLayoutManager();
        comp.setBackground(Color.BLACK);
        comp.changeListener =new Vector<ChangeListener>();
        comp.setLayout(manager);
        comp.setDim(rows,cols);
        comp.addMouseListener(listener);
        return comp;
    }

    private CellGroup group;
    private Cell[][] cells;
    private int rows, cols;
    private Cell current;
    private Vector<ChangeListener> changeListener;
    public static int SETOBSTACLEMODE = 1;
    public static int SETSRCMODE = 2;
    public static int SETDSTMODE = 3;
    MapCompMouseListener listener = new MapCompMouseListener();
    int mode;
    int delay = 500;
    Cell src;
    Cell dst;
    LinkedList<Cell> obstacles = new LinkedList<Cell>();
    HashSet<Cell> openset = new HashSet<Cell>();
    HashSet<Cell> closedset = new HashSet<Cell>();
    HashSet<Cell> pathset = new HashSet<Cell>();
    private int indent;
    MapCompLayoutManager manager;
    Thread thread;

    public MapComp() {

    }

    public void addToOpen(int crow, int ccol) {
        printThread("Adding to open ");
        printThread( "hashCode : "+ cells[crow][ccol].hashCode() + "comp.hashCode : "+ this.hashCode());
        if ((crow >= 0 && crow < rows) & (ccol >= 0 && ccol < cols)) {
            if (!openset.contains(cells[crow][ccol]))
                openset.add(cells[crow][ccol]);
        }
        repaint();
    }
    public void printThread(String str){
        System.out.println(Thread.currentThread().getName() + str);
    }

    public void setThread(Thread thread){
        this.thread = thread;
    }
    public void removeFromOpen(int crow, int ccol) {
        printThread("removing from the open list");
        printThread("hashCode " + cells[crow][ccol].hashCode()+ "comp.hashCode : "+ this.hashCode());
        if ((crow >= 0 && crow < rows) & (ccol >= 0 && ccol < cols)) {
            if (openset.contains(cells[crow][ccol]))
                openset.remove(cells[crow][ccol]);
        }
        repaint();
    }

    public void addToClose(int crow, int ccol) {
        printThread("addToClose method");
        printThread("hashCode : "+ cells[crow][ccol].hashCode() + " comp.hashCode : "+ this.hashCode());
        if ((crow >= 0 && crow < rows) & (ccol >= 0 && ccol < cols)) {
            if (!closedset.contains(cells[crow][ccol]))
                closedset.add(cells[crow][ccol]);
        }
        repaint();
    }

    public void removeFromClose(int crow, int ccol) {
        if ((crow >= 0 && crow < rows) & (ccol >= 0 && ccol < cols)) {
            if (openset.contains(cells[crow][ccol]))
                openset.remove(cells[crow][ccol]);
        }
        repaint();
    }

    public void addToPath(int crow, int ccol) {
        if ((crow >= 0 && crow < rows) & (ccol >= 0 && ccol < cols)) {
            if (!pathset.contains(cells[crow][ccol]))
                pathset.add(cells[crow][ccol]);
        }
        repaint();
    }

    public void removeFromPath(int crow, int ccol) {
        if ((crow >= 0 && crow < rows) & (ccol >= 0 && ccol < cols)) {
            if (pathset.contains(cells[crow][ccol]))
                pathset.remove(cells[crow][ccol]);
        }
        repaint();
    }

    public void setPath(int[] rowarray, int[] colarray) {
        pathset.clear();
        int nrow, ncol;
        if (!(rowarray.length == colarray.length))
            return;
        for (int index = 0; index < rowarray.length; index++) {
            addToPath(rowarray[index], colarray[index]);
        }
    }

    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private boolean editMode = false;

    MapComp(int rows, int cols) {
        indent = 2;
        group = new CellGroup();
        manager = new MapCompLayoutManager();
        this.setBackground(Color.BLACK);
        changeListener = new Vector<ChangeListener>();
        this.setLayout(manager);
        this.setDim(rows, cols);
        this.addMouseListener(listener);
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public int getDelay() {
        return delay;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
        if (editMode == true)
            this.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
        else
            this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    public void setMode(int mode) {
        // just for readability purpose
        if (mode == SETOBSTACLEMODE)
            this.mode = SETOBSTACLEMODE;
        if (mode == SETSRCMODE)
            this.mode = SETSRCMODE;
        if (mode == SETDSTMODE)
            this.mode = SETDSTMODE;
    }

    protected void addObstacle(Cell cell) {
        if (cell == src)
            src = null;
        if (cell == dst)
            dst = null;
        if (!obstacles.contains(cell))
            obstacles.add(cell);
        repaint();
    }

    public void addChangeListener(ChangeListener listener) {
        if (listener == null)
            return;
        if (changeListener.contains(listener))
            return;
        changeListener.add(listener);
    }

    public void removeChangeListener(ChangeListener listener) {
        if (changeListener.contains(listener))
            changeListener.remove(listener);
    }

    public void setCurrent(Point p) {
        try {
            this.current = cells[(int) p.getX()][(int) p.getY()];
            fireChangeListener();
        } catch (Exception e) {
            System.out.println("Exception  thrown :" + e.getMessage());
        }
    }

    public Point getCurrentPosition() {
        if (this.current != null) {
            int row, col;
            col = current.getPosRow();
            row = current.getPosCol();
            int x, y;
            x = (this.indent * (row + 1) + row * this.group.getW() + this.group.getW() / 2);
            y = this.indent * (col + 1) + col * this.group.getH() + this.group.getH() / 2;
            return new Point(x, y);
        }
        return new Point(0, 0);
    }

    protected void fireChangeListener() {
        Iterator itr = changeListener.listIterator();
        while (itr.hasNext()) {
            ChangeListener listener = (ChangeListener) itr.next();
            ChangeEvent e = new ChangeEvent(this);
            listener.stateChanged(e);
        }
    }

    public void addCellToObstacles(int crow, int ccol) {
        if ((crow < 0 || crow > rows) || (ccol < 0 || ccol > cols)) {
            return;
        }
        if (cells[crow][ccol] == src)
            src = null;
        if (cells[crow][ccol] == dst)
            src = null;
        if (!obstacles.contains(cells[crow][ccol]))
            obstacles.add(cells[crow][ccol]);
        repaint();
    }

    public void clear() {
        openset.clear();
        closedset.clear();
        pathset.clear();
        revalidate();
    }

    public void removeCellFromObstacles(int crow, int ccol) {
        if ((crow < 0 || crow > rows) || (ccol < 0 || ccol > cols)) {
            return;
        }
        if (obstacles.contains(cells[crow][ccol]))
            obstacles.remove(cells[crow][ccol]);
        repaint();
    }

    public void setSrc(int crow, int ccol) {
        if ((crow < 0 || crow > rows) || (ccol < 0 || ccol > cols)) {
            return;
        }
        if (cells[crow][ccol] == dst)
            dst = null;
        if (obstacles.contains(cells[crow][ccol]))
            obstacles.remove(cells[crow][ccol]);

        this.src = cells[crow][ccol];
        repaint();
    }

    public void setDst(int crow, int ccol) {
        if ((crow < 0 || crow > rows) || (ccol < 0 || crow > cols)) {
            return;
        }
        if (src == cells[crow][ccol])
            src = null;
        if (obstacles.contains(cells[crow][ccol]))
            obstacles.remove(cells[crow][ccol]);
        this.dst = cells[crow][ccol];
        repaint();
    }

    public void removeSrc(int crow, int ccol) {
        if ((crow < 0 || crow > rows) || (ccol < 0 || ccol > cols)) {
            return;
        }
        if (this.src == cells[crow][ccol])
            this.src = null;
        repaint();
    }

    public void removeDst(int crow, int ccol) {
        if ((crow < 0 || crow > rows) || (ccol < 0 || ccol > cols)) {
            return;
        }
        if (this.dst == cells[crow][ccol])
            this.dst = null;
        repaint();
    }

    public boolean isObstacleHaveCell(int crow, int ccol) {
        if (crow >= 0 && crow < rows && ccol >= 0 && ccol < cols)
            return obstacles.contains(cells[crow][ccol]);
        return false;
    }

    protected void removeObstacle(Cell cell) {
        if (obstacles.contains(cell))
            obstacles.remove(cell);
        repaint();
    }

    protected void setObstacles(LinkedList<Cell> list) {
        obstacles = list;
        repaint();
    }

    protected LinkedList<Cell> getObstacles() {
        return obstacles;
    }

    public Point getSrcPosition() {
        return new Point(src.getPosRow(), src.getPosCol());
    }

    public Point getDstPosition() {
        return new Point(dst.getPosRow(), dst.getPosRow());
    }

    protected void setSrc(Cell src) {
        if (this.dst == src)
            dst = null;
        if (this.obstacles.contains(src))
            obstacles.remove(src);
        this.src = src;
        repaint();
    }

    protected void setDst(Cell dst) {
        if (this.src == dst)
            src = null;
        if (this.obstacles.contains(dst))
            obstacles.remove(dst);
        this.dst = dst;
        repaint();
    }

    protected Cell getSrc() {
        return src;
    }

    protected Cell getDst() {
        return dst;
    }

    public int getSrcRow() {
        if (src != null)
            return src.getPosRow();
        return -1;
    }

    public int getSrcCol() {
        if (src != null)
            return src.getPosCol();
        return -1;
    }

    public int getDstRow() {
        if (dst != null)
            return dst.getPosRow();
        return -1;
    }

    public int getDstCol() {
        if (dst != null)
            return dst.getPosCol();
        return -1;
    }

    public boolean isEditMode() {
        return editMode;
    }

    public int getMode() {
        return mode;
    }

    @Override
    protected void paintComponent(Graphics g) {
        int x, y, w, h;
        x = this.getInsets().left;
        y = this.getInsets().top;
        w = this.getWidth() - (this.getInsets().left + this.getInsets().right);
        h = this.getHeight() - (this.getInsets().top + this.getInsets().bottom);
        Graphics2D gg = (Graphics2D) g.create();
        gg.setColor(this.getBackground());
        gg.fillRect(x, y, w, h);
        gg.dispose();
    }

    public void setIndent(int indent) {
        this.indent = indent;
        if (this.indent < 2)
            this.indent = 2;
        if (this.indent > 10)
            this.indent = 10;
        // indent is always in between 2 and 10
        revalidate();
    }

    public int getIndent() {
        return indent;
    }

    public void setCellDim(int w, int h) {
        if (w < 10)
            w = 10;
        if (h < 10)
            h = 10;
        // w and h should be positive greater than or equal to 10
        group.setW(w);
        group.setH(h);
        revalidate();
    }

    @Override
    public Dimension getMinimumSize() {
        if (this.getLayout() != null) {
            return this.getLayout().minimumLayoutSize(this);
        }
        return null;
    }

    @Override
    public Dimension getMaximumSize() {
        if (this.getLayout() != null) {
            return this.getLayout().preferredLayoutSize(this);
        }
        return null;
    }

    @Override
    public Dimension getPreferredSize() {
        if (this.getLayout() != null) {
            return this.getLayout().preferredLayoutSize(this);
        }
        return null;
    }

    public void setDim(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        Cell[][] temp = new Cell[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                try {
                    temp[i][j] = this.cells[i][j];
                } catch (Exception e) {
                    temp[i][j] = new Cell(i, j);
                    temp[i][j].setGroup(group);
                }
            }
        }
        this.cells = temp;
        this.removeAll();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (cells[i][j] != null)
                    this.add(cells[i][j]);
            }
        }
        revalidate();
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public int getCellDim() {
        return group.w;
    }

    private void startBestFirstSearch() {
        class Node {
            public int dx, dy;

            int crow, ccol;// stands for cellrow, cell col

            Node(int crow, int ccol) {
                this.crow = crow;
                this.ccol = ccol;
            }

            public int getCcol() {
                return ccol;
            }

            public int getCrow() {
                return crow;
            }
        }
        Node nodes[][] = new Node[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                nodes[i][j] = new Node(i, j);
            }
        }
        // Nodes created and initialized
        HashSet<Node> open, closed;
        open = new HashSet<Node>();
        int sx, sy, dx, dy;


        Runnable runn = new Runnable() {
            @Override
            public void run() {

            }
        };

    }

    private void startBreadthFirstSearch() {
        Runnable runn = new Runnable() {
            @Override
            public void run() {

            }
        };
    }

    private void startDepthFirstSearch() {
        Runnable runn = new Runnable() {
            @Override
            public void run() {

            }
        };
    }

    private class MapCompLayoutManager implements LayoutManager {
        @Override
        public Dimension minimumLayoutSize(Container parent) {
            Dimension dim = null;
            if (parent instanceof MapComp) {
                MapComp comp = (MapComp) parent;
                int indent, rows, cols, cellw, cellh, width, height;
                indent = comp.getIndent();
                cellw = comp.group.getW();
                cellh = comp.group.getH();
                rows = cells.length;
                cols = cells[0].length;
                width = cols * (cellw) + indent * (cols + 1);
                height = rows * (cellh) + indent * (rows + 1);
                dim = new Dimension(width, height);
            }
            return dim;
        }

        @Override
        public Dimension preferredLayoutSize(Container parent) {
            Dimension dim = null;
            if (parent instanceof MapComp) {
                MapComp comp = (MapComp) parent;
                int indent, rows, cols, cellw, cellh, width, height;
                indent = comp.getIndent();
                cellw = comp.group.getW();
                cellh = comp.group.getH();
                rows = cells.length;
                cols = cells[0].length;
                width = cols * (cellw) + indent * (cols + 1);
                height = rows * (cellh) + indent * (rows + 1);
                dim = new Dimension(width, height);
            }
            return dim;
        }

        @Override
        public void layoutContainer(Container parent) {
            synchronized (parent.getTreeLock()) {
                if (!(parent instanceof MapComp))
                    return;
                MapComp comp = (MapComp) parent;
                int indent, x, y, w, h;
                indent = comp.getIndent();
                x = indent;
                y = indent;
                w = comp.group.getW();
                h = comp.group.getH();

                for (int i = 0; i < comp.rows; i++) {
                    y = indent + (indent + h) * i;
                    for (int j = 0; j < comp.cols; j++) {
                        x = indent + (indent + w) * j;
                        comp.cells[i][j].setLocation(x, y);
                        comp.cells[i][j].setSize(w, h);
                    }
                }
            }

        }

        @Override
        public void addLayoutComponent(String name, Component comp) {

        }

        @Override
        public void removeLayoutComponent(Component comp) {

        }
    }

    private static class CellGroup implements Cloneable {
        public Color colorForCellInOpenList;
        public Color colorForCellInClosedList;
        public Color colorForSource;
        public Color colorForDestination;
        public Color colorForObstacles;
        public Color colorForCellInPath;
        private int w, h;
        private Color borderColor;

        CellGroup() {
            w = h = 10;
            borderColor = Color.BLACK;
            colorForCellInClosedList = new Color(130, 0, 0);
            colorForCellInOpenList = new Color(200, 145, 33);
            colorForDestination = new Color(76, 32, 244);
            colorForObstacles = Color.BLACK;
            colorForSource = Color.GREEN;
            colorForCellInPath = new Color(108, 155, 200);
        }

        @Override
        protected Object clone() throws CloneNotSupportedException {
            CellGroup group = new CellGroup();
            if (colorForSource != null)
                group.colorForSource = new Color(this.colorForSource.getRGB());
            if (colorForCellInOpenList != null)
                group.colorForCellInOpenList = new Color(this.colorForCellInOpenList.getRGB());
            if (colorForCellInClosedList != null)
                group.colorForCellInClosedList = new Color(this.colorForCellInClosedList.getRGB());
            if (colorForDestination != null)
                group.colorForDestination = new Color(this.colorForDestination.getRGB());
            if (colorForObstacles != null)
                group.colorForObstacles = new Color(this.colorForObstacles.getRGB());
            if (colorForCellInPath != null)
                group.colorForCellInPath = new Color(this.colorForCellInPath.getRGB());
            group.w = this.w;
            group.h = this.h;
            if (borderColor != null)
                group.borderColor = new Color(this.borderColor.getRGB());
            return group;
        }

        public Color getBorderColor() {
            return borderColor;
        }

        public void setBorderColor(Color borderColor) {
            this.borderColor = borderColor;
        }

        public void setW(int w) {
            this.w = w;
        }

        public void setH(int h) {
            this.h = h;
        }

        public int getH() {
            return h;
        }

        public int getW() {
            return w;
        }
    } // A cell group

    private class Cell extends JComponent implements Cloneable {

        private int row, col; // the grid location
        private CellGroup group;
        private Color bgColor;

        Cell(int row, int col) {
            this.row = row;
            this.col = col;
            this.setBgColor(Color.WHITE);
            this.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // default border color is line
        }

        @Override
        protected Object clone() throws CloneNotSupportedException {
            Cell cell = new Cell(this.row, this.col);
            if (bgColor != null)
                cell.bgColor = new Color(bgColor.getRGB());
            cell.group = null;
            return cell;
        }

        public void setGroup(CellGroup group) {
            this.group = group;
            if (group != null)
                this.setBorder(BorderFactory.createLineBorder(group.borderColor));
        }

        public int getPosRow() {
            return row;
        }

        public int getPosCol() {
            return col;
        }

        public void setBgColor(Color bgColor) {
            this.bgColor = bgColor;
            this.repaint();
        }

        int i = 10;

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            int x, y, w, h;
            x = (int) this.getInsets().left;
            y = this.getInsets().top;
            w = this.getWidth() - (this.getInsets().left + this.getInsets().right);
            h = this.getHeight() - (this.getInsets().top + this.getInsets().bottom);
            Graphics2D gg = (Graphics2D) g.create();
            MapComp comp = (MapComp) this.getParent();
            bgColor = Color.WHITE;
            boolean isSrc, isDst, isInPath;
            isSrc = isDst = isInPath = false;
            if (comp.openset.contains(this))
                bgColor = group.colorForCellInOpenList;
            if (comp.closedset.contains(this))
                bgColor = group.colorForCellInClosedList;
            if (comp.pathset.contains(this)) {
                bgColor = group.colorForCellInPath;
                isInPath = true;
            }
            if (comp.src == this) {
                bgColor = group.colorForSource;
                isSrc = true;
            }
            if (comp.dst == this) {
                bgColor = group.colorForDestination;
                isDst = true;
            }
            if (comp.obstacles.contains(this))
                bgColor = group.colorForObstacles;

            String s = "";

            if (isSrc)
                s = "S";
            if (isDst)
                s = "D";
            int size = this.getWidth();
            gg.setFont(new Font(Font.SERIF, Font.BOLD, size));
            int sw = gg.getFontMetrics(gg.getFont()).stringWidth("M");
            int as, ds; // stands for ascent and descent
            as = gg.getFontMetrics(gg.getFont()).getAscent();
            ds = gg.getFontMetrics(gg.getFont()).getDescent();

            gg.setColor(bgColor);
            gg.fillRect(x, y, w, h);
            gg.setColor(Color.BLACK);

            gg.drawString(s, this.getInsets().left + this.getWidth() / 2 - sw / 2, this.getInsets().top + this.getHeight() / 2 + as / 2 - ds / 2);

            gg.dispose();
        }

        @Override
        public Dimension getPreferredSize() {
            Dimension dim = new Dimension(0, 0);
            if (group != null) {
                dim = new Dimension(group.getW(), group.getH());
            }

            return dim;
        }

        @Override
        public Dimension getMaximumSize() {
            Dimension dim = new Dimension(0, 0);
            if (group != null) {
                dim = new Dimension(group.getW(), group.getH());
            }

            return dim;
        }

        @Override
        public Dimension getMinimumSize() {
            Dimension dim = null;
            if (group != null) {
                dim = new Dimension(group.getW(), group.getH());
            }
            return dim;
        }
    }

    public void showstat() {
        if (src != null)
            System.out.println("SRc :(" + src.getPosRow() + "," + src.getPosCol() + ").");
        if (dst != null)
            System.out.println("dst :(" + dst.getPosRow() + "," + dst.getPosCol() + ").");
        ListIterator itr = obstacles.listIterator();
        while (itr.hasNext()) {
            Cell cell = (Cell) itr.next();
            System.out.println("cell : (" + cell.getPosRow() + "," + cell.getPosCol() + ").");
        }
    }

    private static class MapCompMouseListener implements MouseListener {
        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (!(e.getSource() instanceof MapComp))
                return;

            MapComp comp = (MapComp) e.getSource();
            if (comp.isEditMode()) {
                int indent, cw, ch; // stands for cell  width and cell height
                indent = comp.getIndent();
                cw = comp.group.getW();
                ch = comp.group.getH();
                int rx, ry;
                rx = e.getX();
                ry = e.getY();
                boolean isValidCell = false;
                int x, y;
                x = rx;
                y = ry;
                x = x % (indent + cw);
                y = y % (indent + ch);
                int px, py; // Stands for position x and position y
                px = py = -1;
                if (x > indent && y > indent) {
                    py = rx / (indent + cw);
                    px = ry / (indent + ch);
                    isValidCell = true;
                }
                if (isValidCell) {
                    // Add the cell to specified logic
                    if (comp.getMode() == MapComp.SETOBSTACLEMODE) {
                        if (comp.obstacles.contains(comp.cells[px][py])) {
                            comp.removeCellFromObstacles(px, py);
                        } else {
                            comp.addCellToObstacles(px, py);
                        }
                    }
                    if (comp.getMode() == MapComp.SETSRCMODE) {
                        if (comp.src == comp.cells[px][py])
                            comp.removeSrc(px, py);
                        else
                            comp.setSrc(px, py);
                    }
                    if (comp.getMode() == MapComp.SETDSTMODE)
                        if (comp.dst == comp.cells[px][py])
                            comp.removeDst(px, py);
                        else
                            comp.setDst(px, py);
                }

            }
        }
    }

}
