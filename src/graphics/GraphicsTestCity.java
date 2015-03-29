package graphics;

import javax.swing.JFrame;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxStylesheet;

import java.awt.*;
import java.util.Hashtable;
import com.mxgraph.layout.mxParallelEdgeLayout;
import com.mxgraph.util.mxUtils;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JDialog;
import javax.swing.SwingUtilities;

import mtj.City;
import mtj.Time;

public final class GraphicsTestCity extends JFrame {

    static Time time;
    static City city;
    static Object v[];
    static int first;
    static int last;
    static boolean isClickedTwice = false;
    static boolean isLenNull = false;
    static MovePanel panel;
    final mxGraph graph = new mxGraph();
    final mxParallelEdgeLayout parallelEdgeLayout = new mxParallelEdgeLayout(graph, 22);
    final Object parent = graph.getDefaultParent();
    static int[] len;
    GraphicsTestCity frame;

    public void run() {
        frame = new GraphicsTestCity();
        panel = new MovePanel();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1250, 680);
        frame.setLocationRelativeTo(null);
        frame.setGlassPane(panel);
        panel.setOpaque(false);
        frame.setVisible(true);
    }

    public GraphicsTestCity() {
        super("Graphics test");

        setLogic();

        mxStylesheet stylesheet = graph.getStylesheet();
        Hashtable<String, Object> style = new Hashtable<String, Object>();
        style.put(mxConstants.STYLE_SHAPE, ";");
        style.put(mxConstants.STYLE_OVERFLOW, "fill");
        style.put(mxConstants.STYLE_MOVABLE, "0");
        style.put(mxConstants.STYLE_FONTCOLOR, mxUtils.getHexColorString(Color.BLACK));
        style.put(mxConstants.STYLE_FONTSIZE, 20);
        style.put(mxConstants.STYLE_PERIMETER, mxConstants.PERIMETER_HEXAGON);
        style.put(mxConstants.STYLE_PERIMETER_SPACING, "2");
        style.put(mxConstants.STYLE_EDITABLE, "0");
        stylesheet.setDefaultVertexStyle(style);

        Hashtable<String, Object> edgeStyleTable = new Hashtable<String, Object>(stylesheet.getDefaultEdgeStyle());
        edgeStyleTable.put(mxConstants.STYLE_STROKECOLOR, mxUtils.getHexColorString(Color.GRAY));
        edgeStyleTable.put(mxConstants.STYLE_FONTSIZE, 9);
        edgeStyleTable.put(mxConstants.STYLE_FONTCOLOR, mxUtils.getHexColorString(Color.MAGENTA));
        stylesheet.setDefaultEdgeStyle(edgeStyleTable);

        Hashtable<String, Object> edgeStyleTableRed = new Hashtable<String, Object>(edgeStyleTable);
        edgeStyleTableRed.put(mxConstants.STYLE_STROKECOLOR, mxUtils.getHexColorString(Color.RED));
        edgeStyleTableRed.put(mxConstants.STYLE_FONTCOLOR, mxUtils.getHexColorString(Color.BLACK));
        Hashtable<String, Object> edgeStyleTableYellow = new Hashtable<String, Object>(edgeStyleTable);
        edgeStyleTableYellow.put(mxConstants.STYLE_STROKECOLOR, mxUtils.getHexColorString(Color.ORANGE));
        edgeStyleTableYellow.put(mxConstants.STYLE_FONTCOLOR, mxUtils.getHexColorString(Color.BLACK));
        Hashtable<String, Object> edgeStyleTableGreen = new Hashtable<String, Object>(edgeStyleTable);
        edgeStyleTableGreen.put(mxConstants.STYLE_STROKECOLOR, mxUtils.getHexColorString(Color.GREEN));
        edgeStyleTableGreen.put(mxConstants.STYLE_FONTCOLOR, mxUtils.getHexColorString(Color.BLACK));
        Hashtable<String, Object> edgeStyleTableLeng = new Hashtable<String, Object>(edgeStyleTable);
        edgeStyleTableLeng.put(mxConstants.STYLE_STROKECOLOR, mxUtils.getHexColorString(Color.BLUE));
        edgeStyleTableLeng.put(mxConstants.STYLE_FONTCOLOR, mxUtils.getHexColorString(Color.BLACK));
        Hashtable<String, Object> edgeStyleTableGone = new Hashtable<String, Object>(edgeStyleTable);
        edgeStyleTableGone.put(mxConstants.STYLE_STROKECOLOR, mxUtils.getHexColorString(Color.BLACK));
        edgeStyleTableGone.put(mxConstants.STYLE_FONTCOLOR, mxUtils.getHexColorString(Color.BLACK));
        stylesheet.putCellStyle("red", edgeStyleTableRed);
        stylesheet.putCellStyle("yellow", edgeStyleTableYellow);
        stylesheet.putCellStyle("green", edgeStyleTableGreen);
        stylesheet.putCellStyle("leng", edgeStyleTableLeng);
        stylesheet.putCellStyle("gone", edgeStyleTableGone);

        graphUpdate();

        final mxGraphComponent graphComponent = new mxGraphComponent(graph);
        graphComponent.setEnabled(false);
        getContentPane().add(graphComponent);

        graphComponent.getGraphControl().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                Object cell = graphComponent.getCellAt(e.getX(), e.getY());
                if (cell != null && isVertex(cell)) {
                    if (!isClickedTwice) {
                        if (!panel.isSet()) {
                            panel.setXY(e.getX(), e.getY());
                        }
                        first = Integer.valueOf(graph.getLabel(cell)) - 1;
                        isClickedTwice = true;
                        isLenNull = true;
                    } else {
                        last = Integer.valueOf(graph.getLabel(cell)) - 1;
                        graph.removeCells(v);
                        graphUpdate();
                        isClickedTwice = false;
                        new Thread(new MoveRunnable()).start();
                    }
                }
            }
        });
    }

    public void graphUpdate() {
        graph.getModel().beginUpdate();
        try {
            if (!isClickedTwice && !isLenNull) {
                len = null;
            } else {
                len = time.dijkstraLen(first, last);
            }
            v = new Object[City.COUNT];
            double top = 50, left = 50;
            for (int i = 0; i < City.COUNT; i++, left += 150) {
                v[i] = graph.insertVertex(parent, null, String.valueOf(i + 1), left, top, 80, 30);
                if (left == 1100) {
                    top += 110;
                    left = -100;
                }
            }
            int k = 0;
            for (int i = 0; i < City.COUNT; i++) {
                for (int j = 0; j < City.COUNT; j++) {
                    if (i != j) {
                        if (city.city[i][j] != null) {
                            if ((len != null) && (k + 1) < len.length && isExistLeng(len, i, j)) {
                                for (int l = 0; l + 1 < len.length; l++) {
                                    if (i == len[l] && j == len[l + 1]) {
                                        graph.insertEdge(parent, null, city.getSpeedString(i, j), v[i], v[j], "leng");
                                        k++;
                                    }
                                }
                            } else {
                                int temp = city.getSpeed(i, j);
                                if (temp < 15) {
                                    graph.insertEdge(parent, null, city.getSpeedString(i, j), v[i], v[j], "red");
                                } else if (temp < 40) {
                                    graph.insertEdge(parent, null, city.getSpeedString(i, j), v[i], v[j], "yellow");
                                } else {
                                    graph.insertEdge(parent, null, city.getSpeedString(i, j), v[i], v[j], "green");
                                }
                            }
                            if (j > i) {
                                graph.insertEdge(parent, null, city.getLengthString(i, j), v[i], v[j]);
                            }
                        }
                    }
                }
            }
        } finally {
            parallelEdgeLayout.execute(parent);
//            graph.getModel().beginUpdate();
            graph.getModel().endUpdate();
        }
    }

    public boolean isExistLeng(int[] len, int i, int j) {
        for (int l = 0; l + 1 < len.length; l++) {
            if (i == len[l] && j == len[l + 1]) {
                return true;
            }
        }
        return false;
    }

    public boolean isVertex(Object cell) {
        for (Object object : v) {
            if (cell.equals(object)) {
                return true;
            }
        }
        return false;
    }

    public void setLogic() {
        city = new City().testStatic();
        time = new Time(city.city, city.size);
    }

    public static void updateLogic() {
        city.newSpeedReally();
        time = new Time(city.city, city.size);
    }

    public boolean isEnd(int current) {
        return current == last;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GraphicsTestCity().run();
            }
        });
    }

    class MoveRunnable implements Runnable {

        @Override
        public synchronized void run() {
            float allTime = 0;
            panel.setVisible(true);
            int pixelPath = 0;
            int pixelConst = 55;
            while (!isEnd(len[0])) {
                panel.repaint();
                int step = nextDirection(len[0], len[1]);
                panel.move(step);
                try {
                    Thread.sleep(55);
                } catch (InterruptedException e) {
                }
                pixelPath++;
                if (step == 1 || step == 3) {
                    pixelConst = 75;
                }
                if (step == 0 || step == 2) {
                    pixelConst = 55;
                }
                if (pixelPath == pixelConst) {
                    allTime += time.getTimeEdge(len[0], len[1]);
                    first = len[1];
                    updateLogic();
                    graph.removeCells(v);
                    graphUpdate();
                    pixelPath = 0;
                }
            }
            panel.setSet();
            finishDialog(allTime);
            System.out.println("YOUR TIME IS: " + allTime + " seconds.");
        }
    }

    public void finishDialog(float time) {
        JDialog dialog = new JDialog();
        dialog.add(new Label("YOUR TIME IS: " + String.valueOf(time) + " seconds."));
        dialog.pack();
        dialog.setPreferredSize(new Dimension(100, 80));
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    public static int nextDirection(int s, int f) {
        switch (f - s) {
            case 8:
                return 2;
            case -1:
                return 3;
            case 1:
                return 1;
            case -8:
                return 0;
        }
        return -1;
    }
}
