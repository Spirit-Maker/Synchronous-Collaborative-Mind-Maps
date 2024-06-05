package com.touchgraph.graphlayout;

import Network.Client.Client;
import audio.Speech;
import com.touchgraph.graphlayout.graphelements.GraphEltSet;
import com.touchgraph.graphlayout.graphelements.ImmutableGraphEltSet;
import com.touchgraph.graphlayout.graphelements.TGForEachEdge;
import com.touchgraph.graphlayout.graphelements.TGForEachNode;
import com.touchgraph.graphlayout.graphelements.VisibleLocality;
import com.touchgraph.graphlayout.interaction.LocalityUtils;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import mindmap.project.MainFrame;

public class TGPanel extends javax.swing.JPanel {

    // static variables for use within the package
    public static Color BACK_COLOR = Color.white;
    MainFrame MF;
    AdjustOriginLens adjustOriginLens;
    protected BasicMouseMotionListener basicMML;
    TGPoint2D bottomRightDraw = null;

    // ....
    private GraphEltSet completeEltSet;
    Node dragNode; //Node currently being dragged
    private Vector graphListeners;

    protected KeyboardListener keyBL;
    private LocalityUtils localityUtils;
    protected boolean maintainMouseOver = false; //If true, then don't change mouseOverN or mouseOverE

    protected Edge mouseOverE;  //mouseOverE is the edge the mouse is over
    protected Node mouseOverN;  //mouseOverN is the node the mouse is over
    protected Point mousePos; //Mouse location, updated in the mouseMotionListener
    Graphics offgraphics;
    Image offscreen;
    Dimension offscreensize;
    private Vector paintListeners;

    protected Node select;
    protected int selectedNodeDepth = 0;
    protected int selectedNodeNo = 0;
    protected Node tempNode;
    protected Node tempParNode;
    public TGLayout tgLayout;
    TGLensSet tgLensSet; // Converts between a nodes visual position (drawx, drawy),
    // and its absolute position (x,y).
    TGPoint2D topLeftDraw = null;
    private VisibleLocality visibleLocality;

    /**
     * Default constructor.
     */
    public TGPanel() {
        setLayout(null);

        setGraphEltSet(new GraphEltSet());
        addMouseListener(new BasicMouseListener());
        basicMML = new BasicMouseMotionListener();
        addMouseMotionListener(basicMML);
        keyBL = new KeyboardListener();
        addKeyListener(keyBL);

        graphListeners = new Vector();
        paintListeners = new Vector();

        adjustOriginLens = new AdjustOriginLens();

        TGLayout tgLayout = new TGLayout(this);
        setTGLayout(tgLayout);
        tgLayout.start();
    }

    public void addEdge(Edge e) {
        synchronized (localityUtils) {
            visibleLocality.addEdge(e);
            resetDamper();
        }
    }

    public Edge addEdge(Node f, Node t, int tens) {
        synchronized (localityUtils) {
            return visibleLocality.addEdge(f, t, tens);
        }
    }

    public synchronized void addGraphListener(GraphListener gl) {
        graphListeners.addElement(gl);
    }

    public Node addNode() throws TGException {
        String id = "0";
        return addNode(id, null, Node.DEFAULT_TYPE);
    }

    public Node addNode(int type) throws TGException {
        String id = "0";
        return addNode(id, null, type);
    }

    public Node addNode(String label, int type) throws TGException {
        String id = "0";
        return addNode(id, label, type);
    }

    public Node addNode(String id, String label, int type) throws TGException {
        Node node;
        if (label == null) {
            node = new Node(id);
            node.setType(type);
        } else {
            node = new Node(id, label);
            node.setType(type);
        }

        updateDrawPos(node); // The addNode() call should probably take a position, this just sets it at 0,0
        //addNode(node);
        return node;
    }

    public void addNode(final Node node) throws TGException {
        synchronized (localityUtils) {
            visibleLocality.addNode(node);
            resetDamper();
        }
    }
// For Node to add via Network to maintain the datastructure in one pattern

    public void addNodeByForce(final Node node) throws TGException {
        synchronized (localityUtils) {
            visibleLocality.addNodeByForce(node);
            resetDamper();
        }
    }

    public synchronized void addPaintListener(TGPaintListener pl) {
        paintListeners.addElement(pl);
    }

    public void clearAll() {
        synchronized (localityUtils) {
            visibleLocality.clearAll();
        }
    }

    public void clearSelect() {
        if (select != null) {
            select = null;
            repaint();
        }
    }

    public void collapseNode(Node collapseNode) {
        localityUtils.collapseNode(collapseNode);
    }

    public void deleteEdge(Edge edge) {
        synchronized (localityUtils) {
            visibleLocality.deleteEdge(edge);
            resetDamper();
        }
    }

    public void deleteEdge(Node from, Node to) {
        synchronized (localityUtils) {
            visibleLocality.deleteEdge(from, to);
        }
    }

    public boolean deleteNode(Node node) {
        synchronized (localityUtils) {
            if (visibleLocality.deleteNode(node)) { // delete from visibleLocality, *AND completeEltSet
                if (node == select) {
                    clearSelect();
                }
                resetDamper();
                return true;
            }
            return false;
        }
    }

    public boolean deleteNodeById(String id) {
        if (id == null) {
            return false; // ignore
        }
        Node node = findNode(id);
        if (node == null) {
            return false;
        } else {
            return deleteNode(node);
        }
    }

    public int edgeNum() {
        return visibleLocality.edgeCount();
    }

    public void expandNode(Node node) {
        localityUtils.expandNode(node);
    }

    public void fastFinishAnimation() { //Quickly wraps up the add node animation
        localityUtils.fastFinishAnimation();
    }

    public Edge findEdge(Node f, Node t) {
        return visibleLocality.findEdge(f, t);
    }

    public void setNodes(Vector Nodes) {
        visibleLocality.setNodes(Nodes);
    }

    public void setEdges(Vector edges) {
        visibleLocality.setEdges(edges);
    }

    protected synchronized void findMouseOver() {

        if (mousePos == null) {
            setMouseOverN(null);
            setMouseOverE(null);
            return;
        }

        final int mpx = mousePos.x;
        final int mpy = mousePos.y;

        final Node[] monA = new Node[1];
        final Edge[] moeA = new Edge[1];

        TGForEachNode fen = new TGForEachNode() {

            double minoverdist = 100; //Kind of a hack (see second if statement)
            //Nodes can be as wide as 200 (=2*100)

            public void forEachNode(Node node) {
//                System.out.print(node.getID());
                double x = node.drawx;
                double y = node.drawy;

                double dist = Math.sqrt((mpx - x) * (mpx - x) + (mpy - y) * (mpy - y));

                if ((dist < minoverdist) && node.containsPoint(mpx, mpy)) {
                    minoverdist = dist;
                    monA[0] = node;
                }
            }
        };
        visibleLocality.forAllNodes(fen);

        TGForEachEdge fee = new TGForEachEdge() {

            double minDist = 8; // Tangential distance to the edge
            double minFromDist = 1000; // Distance to the edge's "from" node

            public void forEachEdge(Edge edge) {
                double x = edge.from.drawx;
                double y = edge.from.drawy;
                double dist = edge.distFromPoint(mpx, mpy);
                if (dist < minDist) { // Set the over edge to the edge with the minimun tangential distance
                    minDist = dist;
                    minFromDist = Math.sqrt((mpx - x) * (mpx - x) + (mpy - y) * (mpy - y));
                    moeA[0] = edge;
                } else if (dist == minDist) { // If tangential distances are identical, chose
                    // the edge whose "from" node is closest.
                    double fromDist = Math.sqrt((mpx - x) * (mpx - x) + (mpy - y) * (mpy - y));
                    if (fromDist < minFromDist) {
                        minFromDist = fromDist;
                        moeA[0] = edge;
                    }
                }
            }
        };
        visibleLocality.forAllEdges(fee);

        setMouseOverN(monA[0]);
        if (monA[0] == null) {
            setMouseOverE(moeA[0]);
        } else {
            setMouseOverE(null);
        }
    }

    public Node findNode(String id) {
        if (id == null) {
            return null; // ignore
        }
        return completeEltSet.findNode(id);
    }

    void fireMovedEvent() {
        Vector listeners;

        synchronized (this) {
            listeners = (Vector) graphListeners.clone();
        }

        for (int i = 0; i < listeners.size(); i++) {
            GraphListener gl = (GraphListener) listeners.elementAt(i);
            gl.graphMoved();
        }
    }

    public void fireResetEvent() {
        Vector listeners;

        synchronized (this) {
            listeners = (Vector) graphListeners.clone();
        }

        for (int i = 0; i < listeners.size(); i++) {
            GraphListener gl = (GraphListener) listeners.elementAt(i);
            gl.graphReset();
        }
    }

    public AdjustOriginLens getAdjustOriginLens() {
        return adjustOriginLens;
    }

    // color and font setters ......................
    public void setBackColor(Color color) {
        BACK_COLOR = color;
    }

    public TGPoint2D getBottomRightDraw() {
        return new TGPoint2D(bottomRightDraw);
    }

    public TGPoint2D getCenter() {
        return tgLensSet.convDrawToReal(getSize().width / 2, getSize().height / 2);
    }

    public Node getDragNode() {
        return dragNode;
    }

    public void setDragNode(Node node) {
        dragNode = node;
        tgLayout.setDragNode(node);
    }

    public TGPoint2D getDrawCenter() {
        return new TGPoint2D(getSize().width / 2, getSize().height / 2);
    }

    public int getEdgeCount() {
        return completeEltSet.edgeCount();
    }

    public ImmutableGraphEltSet getGES() {
        return visibleLocality;
    }

    public void setGraphEltSet(GraphEltSet ges) {
        completeEltSet = ges;
        visibleLocality = new VisibleLocality(completeEltSet);
        localityUtils = new LocalityUtils(visibleLocality, this);
    }

    public void setLensSet(TGLensSet lensSet) {
        tgLensSet = lensSet;
    }

    public MainFrame getMF() {
        return MF;
    }

    public void setMF(MainFrame MF) {
        this.MF = MF;
    }

    public void setMaintainMouseOver(boolean maintain) {
        maintainMouseOver = maintain;
    }

    public Edge getMouseOverE() {
        return mouseOverE;
    }

    public synchronized void setMouseOverE(Edge edge) {
        if (dragNode != null || maintainMouseOver) {
            return; // No funny business while dragging
        }
        if (mouseOverE != edge) {
            Edge oldMouseOverE = mouseOverE;
            mouseOverE = edge;
        }
    }

    public Node getMouseOverN() {
        return mouseOverN;
    }

    public synchronized void setMouseOverN(Node node) {
        if (dragNode != null || maintainMouseOver) {
            return;  // So you don't accidentally switch nodes while dragging
        }
        if (mouseOverN != node) {
            Node oldMouseOverN = mouseOverN;
            mouseOverN = node;
        }

        if (mouseOverN == null) {
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        } else {
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
    }

    public Point getMousePos() {
        return mousePos;
    }

    void setMousePos(Point p) {
        mousePos = p;
    }

    public int getNodeCount() {
        return completeEltSet.nodeCount();
    }

    public Node getSelect() {
        return select;
    }

    public void setSelect(Node node) {
        if (node != null) {
            select = node;
            repaint();
        } else if (node == null) {
            clearSelect();
        }
        if (getMF().isSelecetSoundEnabled()) {
            Thread thread = new Thread() {
                public void run() {
                    if (Speech.running) {
                        return;
                    }
                    Speech.running = true;
                    Speech speech = new Speech(null);
                    speech.speak("this node is " + node.getLabel(), (float) 1.2);
                    Speech.running = false;
                }
            };
            thread.start();
        }
    }

    public void setTGLayout(TGLayout tgl) {
        tgLayout = tgl;
    }

    public TGPoint2D getTopLeftDraw() {
        return new TGPoint2D(topLeftDraw);
    }

    public VisibleLocality getVisibleLocality() {
        return visibleLocality;
    }

    public void hideEdge(Edge hideEdge) {
        visibleLocality.removeEdge(hideEdge);
        if (mouseOverE == hideEdge) {
            setMouseOverE(null);
        }
        resetDamper();
    }

    public void multiSelect(TGPoint2D from, TGPoint2D to) {
        final double minX, minY, maxX, maxY;

        if (from.x > to.x) {
            maxX = from.x;
            minX = to.x;
        } else {
            minX = from.x;
            maxX = to.x;
        }
        if (from.y > to.y) {
            maxY = from.y;
            minY = to.y;
        } else {
            minY = from.y;
            maxY = to.y;
        }

        final Vector selectedNodes = new Vector();

        TGForEachNode fen = new TGForEachNode() {
            public void forEachNode(Node node) {
                double x = node.drawx;
                double y = node.drawy;
                if (x > minX && x < maxX && y > minY && y < maxY) {
                    selectedNodes.addElement(node);
                }
            }
        };

        visibleLocality.forAllNodes(fen);

        if (selectedNodes.size() > 0) {
            int r = (int) (Math.random() * selectedNodes.size());
            setSelect((Node) selectedNodes.elementAt(r));
        } else {
            clearSelect();
        }
    }

    Color myBrighter(Color c) {
        int r = c.getRed();
        int g = c.getGreen();
        int b = c.getBlue();

        r = Math.min(r + 96, 255);
        g = Math.min(g + 96, 255);
        b = Math.min(b + 96, 255);

        return new Color(r, g, b);
    }

    public int nodeNum() {
        return visibleLocality.nodeCount();
    }

    public synchronized void paint(Graphics g) {
        update(g);
    }

    public synchronized void processGraphMove() {
        updateDrawPositions();
        updateGraphSize();
    }

    private void redraw() {
        resetDamper();
    }

    public synchronized void removeGraphListener(GraphListener gl) {
        graphListeners.removeElement(gl);
    }

    public synchronized void removePaintListener(TGPaintListener pl) {
        paintListeners.removeElement(pl);
    }

    public synchronized void repaintAfterMove() { // Called by TGLayout + others to indicate that graph has moved
        processGraphMove();
        findMouseOver();
        fireMovedEvent();
        repaint();
    }

    /**
     * Makes the graph mobile, and slowly slows it down.
     */
    public void resetDamper() {
        if (tgLayout != null) {
            tgLayout.resetDamper();
        }
    }

    public void setLocale(Node node, int radius, int maxAddEdgeCount, int maxExpandEdgeCount, boolean unidirectional) throws TGException {
        localityUtils.setLocale(node, radius, maxAddEdgeCount, maxExpandEdgeCount, unidirectional);
    }

    public void setLocale(Node node, int radius) throws TGException {
        localityUtils.setLocale(node, radius);
    }

    /**
     * Start and stop the damper. Should be placed in the TGPanel too.
     */
    public void startDamper() {
        if (tgLayout != null) {
            tgLayout.startDamper();
        }
    }

    public void stopDamper() {
        if (tgLayout != null) {
            tgLayout.stopDamper();
        }
    }

    public void setVisibleLocality(VisibleLocality visibleLocality) {
        if (visibleLocality != null) {
            this.visibleLocality = visibleLocality;
        }
    }

    public void stopMotion() {
        if (tgLayout != null) {
            tgLayout.stopMotion();
        }
    }

    public Node findRoot() {
        Vector n = visibleLocality.getNodes();
        for (int i = 0; i < n.size(); i++) {
            Node node = (Node) n.elementAt(i);
            if (node.getDepth() == 0) {
                return node;
            }
        }
        return null;
    }

    public void vibrateGraph() { // Vibrates Node better run in a thread
        long end = System.currentTimeMillis() + 2000; // 4 seconds from now
        int tempSpeed = 1;

        Vector nodes = visibleLocality.getNodes();

        Vector<Integer> speed = new Vector();    // to manage speed and thier permanent positions
        Vector<Integer> permPosY = new Vector();

        for (int i = 0; i < nodes.size(); i++) {
            speed.addElement(tempSpeed);
            tempSpeed = -tempSpeed;
            permPosY.addElement((int) ((Node) nodes.elementAt(i)).drawy);
        }
        while (System.currentTimeMillis() < end) {
            for (int i = 0; i < speed.size(); i++) {
                Node node = (Node) nodes.elementAt(i);
                if (node.getDepth() == 0) {
                    continue;
                }
                int y = (int) node.drawy;
                if (permPosY.elementAt(i) - y > 10 || permPosY.elementAt(i) - y < -10) {      //invert speed
                    speed.setElementAt((-speed.elementAt(i)), i);
//                    if (permPosY.elementAt(i) - y > 10 || permPosY.elementAt(i) - y < -10) {
//                        if (speed.elementAt(i) < 0) {
//                            speed.set(i, -1);
//                        } else {
//                            speed.set(i, 1);
//                        }
//                    } else {
//                        if (speed.elementAt(i) < 0) {
//                            speed.set(i, -3);
//                        } else {
//                            speed.set(i, 3);
//                        }
//                    }
                }
                node.drawy = y + speed.elementAt(i);
                update(this.getGraphics());
                try {
                    Thread.sleep(10);
                } catch (InterruptedException ex) {
                    Logger.getLogger(TGPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public synchronized void update(Graphics g) {
        Dimension d = getSize();
        if ((offscreen == null)
                || (d.width != offscreensize.width)
                || (d.height != offscreensize.height)) {
            offscreen = createImage(d.width, d.height);
            offscreensize = d;
            offgraphics = offscreen.getGraphics();

            processGraphMove();
            findMouseOver();
            fireMovedEvent();
        }

        offgraphics.setColor(BACK_COLOR);
        offgraphics.fillRect(0, 0, d.width, d.height);

        TGForEachEdge fee = new TGForEachEdge() {
            public void forEachEdge(Edge edge) {
                edge.paint(offgraphics, TGPanel.this);
            }
        };

        visibleLocality.forAllEdges(fee);

        for (int i = 0; i < paintListeners.size(); i++) {
            TGPaintListener pl = (TGPaintListener) paintListeners.elementAt(i);
            pl.paintAfterEdges(offgraphics);
        }

        TGForEachNode fen = new TGForEachNode() {
            public void forEachNode(Node node) {
                node.paint(offgraphics, TGPanel.this);
            }
        };

        visibleLocality.forAllNodes(fen);

        if (mouseOverE != null) { //Make the edge the mouse is over appear on top.
            mouseOverE.paint(offgraphics, this);
            mouseOverE.from.paint(offgraphics, this);
            mouseOverE.to.paint(offgraphics, this);
        }

        if (select != null) { //Make the selected node appear on top.
            select.paint(offgraphics, this);
        }

        if (mouseOverN != null) { //Make the node the mouse is over appear on top.
            mouseOverN.paint(offgraphics, this);
        }

        for (int i = 0; i < paintListeners.size(); i++) {
            TGPaintListener pl = (TGPaintListener) paintListeners.elementAt(i);
            pl.paintLast(offgraphics);
        }

        paintComponents(offgraphics); //Paint any components that have been added to this panel
        g.drawImage(offscreen, 0, 0, null);

    }

    public void updateDrawPos(Node node) { //sets the visual position from the real position
        TGPoint2D p = tgLensSet.convRealToDraw(node.x, node.y);
        node.drawx = p.x;
        node.drawy = p.y;
    }

    public void updateDrawPositions() {
        TGForEachNode fen = new TGForEachNode() {
            @Override
            public void forEachNode(Node node) {
                updateDrawPos(node);
            }
        };
        visibleLocality.forAllNodes(fen);
    }

    public void updateGraphSize() {
        if (topLeftDraw == null) {
            topLeftDraw = new TGPoint2D(0, 0);
        }
        if (bottomRightDraw == null) {
            bottomRightDraw = new TGPoint2D(0, 0);
        }

        TGForEachNode fen = new TGForEachNode() {
            boolean firstNode = true;

            public void forEachNode(Node node) {
                if (firstNode) { //initialize topRight + bottomLeft
                    topLeftDraw.setLocation(node.drawx, node.drawy);
                    bottomRightDraw.setLocation(node.drawx, node.drawy);
                    firstNode = false;
                } else {  //Standard max and min finding
                    topLeftDraw.setLocation(Math.min(node.drawx, topLeftDraw.x),
                            Math.min(node.drawy, topLeftDraw.y));
                    bottomRightDraw.setLocation(Math.max(node.drawx, bottomRightDraw.x),
                            Math.max(node.drawy, bottomRightDraw.y));
                }
            }
        };

        visibleLocality.forAllNodes(fen);
    }

    public void updateLocalityFromVisibility() throws TGException {
        visibleLocality.updateLocalityFromVisibility();
    }

    public void updatePosFromDraw(Node node) { //sets the real position from the visual position
        TGPoint2D p = tgLensSet.convDrawToReal(node.drawx, node.drawy);
        node.x = p.x;
        node.y = p.y;
    }

    /**
     * Return the number of Edges in the Locality.
     */
    public int visibleEdgeCount() {
        return visibleLocality.edgeCount();
    }

    /**
     * Returns the current node count within the VisibleLocality.
     */
    public int visibleNodeCount() {
        return visibleLocality.nodeCount();

    }

    // miscellany ..................................
    protected class AdjustOriginLens extends TGAbstractLens {

        protected void applyLens(TGPoint2D p) {
            p.x = p.x + TGPanel.this.getSize().width / 2;
            p.y = p.y + TGPanel.this.getSize().height / 2;
        }

        protected void undoLens(TGPoint2D p) {
            p.x = p.x - TGPanel.this.getSize().width / 2;
            p.y = p.y - TGPanel.this.getSize().height / 2;
        }
    }

    class BasicMouseListener extends MouseAdapter {

        public void mouseEntered(MouseEvent e) {
            addMouseMotionListener(basicMML);
        }

        public void mouseExited(MouseEvent e) {
            removeMouseMotionListener(basicMML);
            mousePos = null;
            setMouseOverN(null);
            setMouseOverE(null);
            repaint();
        }
    }

    class BasicMouseMotionListener implements MouseMotionListener {

        public void mouseDragged(MouseEvent e) {
            mousePos = e.getPoint();
            findMouseOver();
            try {
                Thread.currentThread().sleep(6); //An attempt to make the cursor flicker less
            } catch (InterruptedException ex) {
                //break;
            }
        }

        public void mouseMoved(MouseEvent e) {
            mousePos = e.getPoint();
            synchronized (this) {
                Edge oldMouseOverE = mouseOverE;
                Node oldMouseOverN = mouseOverN;
                findMouseOver();
                if (oldMouseOverE != mouseOverE || oldMouseOverN != mouseOverN) {
                    repaint();
                }
                // Replace the above lines with the commented portion below to prevent whole graph
                // from being repainted simply to highlight a node On mouseOver.
                // This causes some annoying flickering though.
                /*
                 if(oldMouseOverE!=mouseOverE) {
                 if (oldMouseOverE!=null) {
                 synchronized(oldMouseOverE) {
                 oldMouseOverE.paint(TGPanel.this.getGraphics(),TGPanel.this);
                 oldMouseOverE.from.paint(TGPanel.this.getGraphics(),TGPanel.this);
                 oldMouseOverE.to.paint(TGPanel.this.getGraphics(),TGPanel.this);
                
                 }
                 }
                
                 if (mouseOverE!=null) {
                 synchronized(mouseOverE) {
                 mouseOverE.paint(TGPanel.this.getGraphics(),TGPanel.this);
                 mouseOverE.from.paint(TGPanel.this.getGraphics(),TGPanel.this);
                 mouseOverE.to.paint(TGPanel.this.getGraphics(),TGPanel.this);
                 }
                 }
                 }
                
                 if(oldMouseOverN!=mouseOverN) {
                 if (oldMouseOverN!=null) oldMouseOverN.paint(TGPanel.this.getGraphics(),TGPanel.this);
                 if (mouseOverN!=null) mouseOverN.paint(TGPanel.this.getGraphics(),TGPanel.this);
                 }
                 */
            }
        }
    }

    class KeyboardListener extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) { // backspace
                selectedNodeNo = 0;
                selectedNodeDepth = 0;
                clearSelect();
            }

            if (e.getKeyCode() == 37) {     // Left  Move to previos Brother Node
                if (select == null) {
                    for (int i = 0; i < visibleLocality.getNodes().size(); i++) {
                        Node n = (Node) visibleLocality.getNodes().elementAt(i);
                        if (n.getDepth() == 0) {
                            setSelect(n);
                            tempNode = n;
                            selectedNodeNo = i;
                            break;
                        } else {
                            continue;
                        }
                    }
                } else {
                    OuterLoop:
                    for (int i = 0; i < visibleLocality.getNodes().size(); i++) {
                        selectedNodeNo--;
                        if (selectedNodeNo < 0) {
                            selectedNodeNo = visibleLocality.getNodes().size() - 1;
                        }
                        if (selectedNodeNo >= 0 && selectedNodeNo < visibleLocality.getNodes().size()) {
                            Node node = visibleLocality.nodeAt(selectedNodeNo);

                            if (selectedNodeDepth == 0 && node.getDepth() == selectedNodeDepth) {
                                if (select != node) {
                                    setSelect(node);
                                } else {
                                    continue;
                                }
                                tempNode = node;
                                tempParNode = null;
                                break OuterLoop;

                            } else if (node.getDepth() == selectedNodeDepth && selectedNodeDepth > 0) {

                                if (node.getParent() == tempParNode && tempParNode != null) {

                                    setSelect(node);
                                    tempNode = select;
                                    break OuterLoop;
                                }

                            }

                        } else {
//                             voice error
                            java.awt.Toolkit.getDefaultToolkit().beep();
                        }
                    }
                }
            }

            if (e.getKeyCode() == 38) {  //up  Move to Parent
                if (select == null) {
                    for (int i = 0; i < visibleLocality.getNodes().size(); i++) {
                        Node n = (Node) visibleLocality.getNodes().elementAt(i);
                        if (n.getDepth() == 0) {
                            setSelect(n);
                            tempNode = n;
                            tempParNode = null;
                            selectedNodeNo = i;
                            break;
                        } else {
                            continue;
                        }
                    }
                } else if (select.getParent() != null) {
                    setSelect(select.getParent());
                    tempParNode = select.getParent();
                    tempNode = select;
                    selectedNodeDepth--;
                } else {
                    tempParNode = null;
                    java.awt.Toolkit.getDefaultToolkit().beep();
                }
            }

            if (e.getKeyCode() == 39) {  // Right   Move to Front Brother Noe
                if (select == null) {
                    for (int i = 0; i < visibleLocality.getNodes().size(); i++) {
                        Node n = (Node) visibleLocality.getNodes().elementAt(i);
                        if (n.getDepth() == 0) {
                            setSelect(n);
                            tempNode = n;
                            selectedNodeNo = i;
                            break;
                        } else {
                            continue;
                        }
                    }
                } else {
                    OuterLoop:
                    for (int i = 0; i < visibleLocality.getNodes().size(); i++) {
                        selectedNodeNo++;
                        if (selectedNodeNo >= visibleLocality.getNodes().size()) {
                            selectedNodeNo = 0;
                        }
                        if (selectedNodeNo >= 0 && selectedNodeNo < visibleLocality.getNodes().size()) {
                            Node node = visibleLocality.nodeAt(selectedNodeNo);
                            if (selectedNodeDepth == 0 && node.getDepth() == selectedNodeDepth) {
                                if (select != node) {
                                    setSelect(node);
                                } else {
                                    continue;
                                }
                                tempNode = node;
                                tempParNode = null;
                                break;

                            } else if (node.getDepth() == selectedNodeDepth && selectedNodeDepth > 0) {

                                if (node.getParent() == tempParNode && tempParNode != null) {
                                    setSelect(node);
                                    tempNode = node;
                                    break OuterLoop;
                                }

                            }

                        } else {
                            // voice error
                            java.awt.Toolkit.getDefaultToolkit().beep();
                        }
                    }
                }

            }

            if (e.getKeyCode() == 40) {   //Down   Move to Child Node
                if (select == null) {
                    for (int i = 0; i < visibleLocality.getNodes().size(); i++) {
                        Node n = (Node) visibleLocality.getNodes().elementAt(i);
                        if (n.getDepth() == 0) {
                            setSelect(n);
                            tempNode = n;
                            tempParNode = null;
                            selectedNodeNo = i;
                            break;
                        } else {
                            continue;
                        }
                    }
                } else if (select.childrenNum() != 0) {
                    tempParNode = select;
                    setSelect(select.childAt(0));
                    tempNode = select;
                    selectedNodeDepth++;
                } else {
                    //sound Error
                    java.awt.Toolkit.getDefaultToolkit().beep();
                }
            }
//            if (e.getKeyCode() == KeyEvent.VK_ADD) {
//                TGForEachNode fen = new TGForEachNode() {
//                    @Override
//                    public void forEachNode(Node n) {
//                        
//                    }
//                };
//            }
//            if (e.getKeyCode() == KeyEvent.VK_MINUS) {
//
//            }
        }

    }

//    public class SwitchSelectUI extends TGAbstractClickUI {
//
//        /**
//         *
//         * @param e
//         */
//        @Override
//        public void mouseClicked(MouseEvent e) {
//            if (mouseOverN != null) {
//                if (mouseOverN != select) {
//                    setSelect(mouseOverN);
//                } else {
//                    clearSelect();
//                }
//            }
//        }
//    }
} // end com.touchgraph.graphlayout.TGPanel
