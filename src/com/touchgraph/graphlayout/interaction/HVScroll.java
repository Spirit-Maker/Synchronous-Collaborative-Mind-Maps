package com.touchgraph.graphlayout.interaction;

import com.touchgraph.graphlayout.GraphListener;
import com.touchgraph.graphlayout.Node;
import com.touchgraph.graphlayout.TGAbstractLens;
import com.touchgraph.graphlayout.TGLensSet;
import com.touchgraph.graphlayout.TGPanel;
import com.touchgraph.graphlayout.TGPoint2D;
import java.awt.Point;
import java.awt.Scrollbar;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseEvent;

public class HVScroll implements GraphListener {

    private DScrollbar horizontalSB;
    private DScrollbar verticalSB;

    HVLens hvLens;
    HVDragUI hvDragUI;
    HVScrollToCenterUI hvScrollToCenterUI;
    public boolean scrolling;

    private boolean adjustmentIsInternal;
    private TGPanel tgPanel;
    private TGLensSet tgLensSet;

    TGPoint2D offset;

    public HVScroll(TGPanel tgp, TGLensSet tgls) {
        tgPanel = tgp;
        tgLensSet = tgls;

        offset = new TGPoint2D(0, 0);
        scrolling = false;
        adjustmentIsInternal = false;

        horizontalSB = new DScrollbar(Scrollbar.HORIZONTAL, 0, 100, -1000, 1100);
        horizontalSB.setBlockIncrement(100);
        horizontalSB.setUnitIncrement(20);

        horizontalSB.addAdjustmentListener(new horizAdjustmentListener());

        verticalSB = new DScrollbar(Scrollbar.VERTICAL, 0, 100, -1000, 1100);
        verticalSB.setBlockIncrement(100);
        verticalSB.setUnitIncrement(20);

        verticalSB.addAdjustmentListener(new vertAdjustmentListener());

        hvLens = new HVLens();
        hvDragUI = new HVDragUI(); //Hopefully this approach won't eat too much memory
        hvScrollToCenterUI = new HVScrollToCenterUI();

        tgPanel.addGraphListener(this);
    }

    public Scrollbar getHorizontalSB() {
        return horizontalSB;
    }

    public Scrollbar getVerticalSB() {
        return verticalSB;
    }

    public HVDragUI getHVDragUI() {
        return hvDragUI;
    }

    public HVLens getLens() {
        return hvLens;
    }

    public TGAbstractClickUI getHVScrollToCenterUI() {
        return hvScrollToCenterUI;
    }

    public TGPoint2D getTopLeftDraw() {
        TGPoint2D tld = tgPanel.getTopLeftDraw();
        tld.setLocation(tld.x - tgPanel.getSize().width / 4, tld.y - tgPanel.getSize().height / 4);
        return tld;
    }

    public TGPoint2D getBottomRightDraw() {
        TGPoint2D brd = tgPanel.getBottomRightDraw();
        brd.setLocation(brd.x + tgPanel.getSize().width / 4, brd.y + tgPanel.getSize().height / 4);
        return brd;
    }

    public TGPoint2D getDrawCenter() { //Should probably be called from tgPanel
        return new TGPoint2D(tgPanel.getSize().width / 2, tgPanel.getSize().height / 2);
    }

    Thread noRepaintThread;

    public void graphMoved() { //From GraphListener interface
        if (tgPanel.getDragNode() == null && tgPanel.getSize().height > 0) {
            TGPoint2D drawCenter = getDrawCenter();

            TGPoint2D tld = getTopLeftDraw();
            TGPoint2D brd = getBottomRightDraw();

            double newH = (-(tld.x - drawCenter.x) / (brd.x - tld.x) * 2000 - 1000);
            double newV = (-(tld.y - drawCenter.y) / (brd.y - tld.y) * 2000 - 1000);

            boolean beyondBorder;
            beyondBorder = true;

            if (newH < horizontalSB.getMaximum() && newH > horizontalSB.getMinimum()
                    && newV < verticalSB.getMaximum() && newV > verticalSB.getMinimum()) {
                beyondBorder = false;
            }

            adjustmentIsInternal = true;
            horizontalSB.setDValue(newH);
            verticalSB.setDValue(newV);
            adjustmentIsInternal = false;

            if (beyondBorder) {
                adjustHOffset();
                adjustVOffset();
                tgPanel.repaint();
            }
        }

        if (noRepaintThread != null && noRepaintThread.isAlive()) {
            noRepaintThread.interrupt();
        }
        noRepaintThread = new Thread() {
            public void run() {
                try {
                    Thread.currentThread().sleep(40); //Wait 40 milliseconds before repainting
                } catch (InterruptedException ex) {
                }
            }
        };
        noRepaintThread.start();

    }

    public void graphReset() { //From GraphListener interface
        horizontalSB.setDValue(0);
        verticalSB.setDValue(0);

        adjustHOffset();
        adjustVOffset();
    }

    class DScrollbar extends Scrollbar {

        private double doubleValue;

        DScrollbar(int orient, int val, int vis, int min, int max) {
            super(orient, val, vis, min, max);
            doubleValue = val;
        }

        public void setValue(int v) {
            doubleValue = v;
            super.setValue(v);
        }

        public void setIValue(int v) {
            super.setValue(v);
        }

        public void setDValue(double v) {
            doubleValue = Math.max(getMinimum(), Math.min(getMaximum(), v));
            setIValue((int) v);
        }

        public double getDValue() {
            return doubleValue;
        }
    }

    private void adjustHOffset() { //The inverse of the "graphMoved" function.
        //System.out.println(horizontalSB.getDValue());
        for (int iterate = 0; iterate < 3; iterate++) { // Iteration needed to yeild cerrect results depite warping lenses
            TGPoint2D center = tgPanel.getCenter();
            TGPoint2D tld = getTopLeftDraw();
            TGPoint2D brd = getBottomRightDraw();

            double newx = ((horizontalSB.getDValue() + 1000.0) / 2000) * (brd.x - tld.x) + tld.x;
            double newy = tgPanel.getSize().height / 2;
            TGPoint2D newCenter = tgLensSet.convDrawToReal(newx, newy);

            offset.setX(offset.x + (newCenter.x - center.x));
            offset.setY(offset.y + (newCenter.y - center.y));

            tgPanel.processGraphMove();
        }
    }

    private void adjustVOffset() { //The inverse of the "graphMoved" function.
        for (int iterate = 0; iterate < 10; iterate++) { // Iteration needed to yeild cerrect results depite warping lenses
            TGPoint2D center = tgPanel.getCenter();
            TGPoint2D tld = getTopLeftDraw();
            TGPoint2D brd = getBottomRightDraw();

            double newx = tgPanel.getSize().width / 2;
            double newy = ((verticalSB.getDValue() + 1000.0) / 2000) * (brd.y - tld.y) + tld.y;

            TGPoint2D newCenter = tgLensSet.convDrawToReal(newx, newy);

            offset.setX(offset.x + (newCenter.x - center.x));
            offset.setY(offset.y + (newCenter.y - center.y));

            tgPanel.processGraphMove();
        }
    }

    private class horizAdjustmentListener implements AdjustmentListener {

        public void adjustmentValueChanged(AdjustmentEvent e) {
            if (!adjustmentIsInternal) {
                adjustHOffset();
                tgPanel.repaintAfterMove();
            }
        }
    }

    private class vertAdjustmentListener implements AdjustmentListener {

        public void adjustmentValueChanged(AdjustmentEvent e) {
            if (!adjustmentIsInternal) {
                adjustVOffset();
                tgPanel.repaintAfterMove();
            }
        }
    }

    class HVLens extends TGAbstractLens {

        @Override
        protected void applyLens(TGPoint2D p) {
            p.x = p.x - offset.x;
            p.y = p.y - offset.y;
        }

        @Override
        protected void undoLens(TGPoint2D p) {
            p.x = p.x + offset.x;
            p.y = p.y + offset.y;
        }
    }

    public void setOffset(Point p) {
        offset.setLocation(p.x, p.y);
        tgPanel.processGraphMove(); //Adjust draw coordinates to include new offset
        graphMoved(); //adjusts scrollbars to fit draw coordinates
    }

    public Point getOffset() {
        return new Point((int) offset.x, (int) offset.y);
    }

    public void scrollAtoB(TGPoint2D drawFrom, TGPoint2D drawTo) {
        TGPoint2D from = tgLensSet.convDrawToReal(drawFrom);
        TGPoint2D to = tgLensSet.convDrawToReal(drawTo);
        offset.setX(offset.x + (from.x - to.x));
        offset.setY(offset.y + (from.y - to.y));
    }

    Thread scrollThread;

    public void slowScrollToCenter(final Node n) {
        final TGPoint2D drawFrom = new TGPoint2D(n.drawx, n.drawy);
        final TGPoint2D drawTo = getDrawCenter();
        scrolling = true;
        if (scrollThread != null && scrollThread.isAlive()) {
            scrollThread.interrupt();
        }
        scrollThread = new Thread() {
            public void run() {
                double nx = -999;
                double ny = -999;
                double cx;
                double cy;
                double distFromCenter;
                boolean keepScrolling = true;
                int stopScrollingAttempt = 0;
                int scrollSteps = 0;

                while (keepScrolling && scrollSteps++ < 250) {
                    nx = n.drawx;
                    ny = n.drawy;
                    cx = getDrawCenter().x;
                    cy = getDrawCenter().y;

                    distFromCenter = Math.sqrt((nx - cx) * (nx - cx) + (ny - cy) * (ny - cy));

                    double newx, newy;
                    if (distFromCenter > 5) {
                        newx = cx + (nx - cx) * ((distFromCenter - 5) / distFromCenter);
                        newy = cy + (ny - cy) * ((distFromCenter - 5) / distFromCenter);
                    } else {
                        newx = cx;
                        newy = cy;
                    }

                    scrollAtoB(new TGPoint2D(nx, ny), new TGPoint2D(newx, newy));

                    if (noRepaintThread == null || !noRepaintThread.isAlive()) {
                        tgPanel.repaintAfterMove(); //only repaint if 40 milliseconds have not ellapsed since last repaint
                    } else {
                        tgPanel.processGraphMove(); //otherwise, register scroll internally
                    }

                    try {
                        Thread.currentThread().sleep(20);
                    } catch (InterruptedException ex) {
                        keepScrolling = false;
                    }

                    if (distFromCenter < 3) {
                        try {
                            Thread.currentThread().sleep(200); //Wait a little to make sure
                        } catch (InterruptedException ex) {
                            keepScrolling = false;
                        }
                        nx = n.drawx;
                        ny = n.drawy;
                        cx = getDrawCenter().x;
                        cy = getDrawCenter().y;
                        distFromCenter = Math.sqrt((nx - cx) * (nx - cx) + (ny - cy) * (ny - cy));
                        if (distFromCenter < 3) {
                            keepScrolling = false;
                        }

                    }
                }
                scrollAtoB(new TGPoint2D(n.drawx, n.drawy), getDrawCenter()); //for good measure
                tgPanel.repaintAfterMove();
                HVScroll.this.scrolling = false;
            }
        };
        scrollThread.start();
    }

    class HVScrollToCenterUI extends TGAbstractClickUI {

        public void mouseClicked(MouseEvent e) {
            /*            Node mouseOverN=tgPanel.getMouseOverN();
             if(!scrolling && mouseOverN!=null)
             slowScrollToCenter(mouseOverN);
             */        }
    }

    class HVDragUI extends TGAbstractDragUI {

        TGPoint2D lastMousePos;

        HVDragUI() {
            super(HVScroll.this.tgPanel);
        }

        public void preActivate() {
        }

        public void preDeactivate() {
        }

        public void mousePressed(MouseEvent e) {
            lastMousePos = new TGPoint2D(e.getX(), e.getY());
        }

        public void mouseReleased(MouseEvent e) {
        }

        public void mouseDragged(MouseEvent e) {
            if (!scrolling) {
                scrollAtoB(lastMousePos, new TGPoint2D(e.getX(), e.getY()));
            }
            lastMousePos.setLocation(e.getX(), e.getY());
            this.tgPanel.repaintAfterMove();
        }
    }

} // end com.touchgraph.graphlayout.interaction.HVScroll
