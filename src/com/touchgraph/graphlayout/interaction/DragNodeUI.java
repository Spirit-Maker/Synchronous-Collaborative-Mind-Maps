package com.touchgraph.graphlayout.interaction;

import Network.Client.Client;
import Network.Server.Server;
import com.touchgraph.graphlayout.Node;
import com.touchgraph.graphlayout.TGPanel;
import java.awt.Point;
import java.awt.event.MouseEvent;

public class DragNodeUI extends TGAbstractDragUI {

    public Point dragOffs;

    public DragNodeUI(TGPanel tgp) {
        super(tgp);
    }

    public synchronized void mouseDragged(MouseEvent e) {
        Node dragNode = tgPanel.getDragNode();
        dragNode.drawx = e.getX() + dragOffs.x;
        dragNode.drawy = e.getY() + dragOffs.y;
        tgPanel.updatePosFromDraw(dragNode);
        tgPanel.repaintAfterMove();
        tgPanel.stopDamper(); //Keep the graph alive while dragging.
//        Runnable s = new Sound("error.wav", "wav");
//        new Thread(s).start();
        e.consume();

    }

    public void mousePressed(MouseEvent e) {
        Node mouseOverN = tgPanel.getMouseOverN();
        Point mousePos;

        if (e != null) {
            mousePos = e.getPoint(); //e can be null if the wrong activate() method was used
        } else {
            mousePos = new Point((int) mouseOverN.drawx, (int) mouseOverN.drawy);
        }

        if (mouseOverN != null) { //Should never be null if TGUIManager works properly
            tgPanel.setDragNode(mouseOverN);

            dragOffs.setLocation((int) (mouseOverN.drawx - mousePos.x), //For when you click to the side of
                    (int) (mouseOverN.drawy - mousePos.y));//the node, but you still want to drag it
        }

    }

    public void mouseReleased(MouseEvent e) {
        tgPanel.setDragNode(null);
        tgPanel.repaintAfterMove();
        tgPanel.startDamper();
    }

    public void preActivate() {
        if (dragOffs == null) {
            dragOffs = new Point(0, 0);
        }
    }

    public void preDeactivate() {
    }

}
