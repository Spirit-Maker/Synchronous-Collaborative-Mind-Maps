package com.touchgraph.graphlayout.interaction;

import com.touchgraph.graphlayout.Edge;
import com.touchgraph.graphlayout.GLPanel;
import com.touchgraph.graphlayout.Node;
import com.touchgraph.graphlayout.TGPanel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 *
 * @author Shani Bhai
 */
public class TGAddEdgeUI extends TGUserInterface {

    protected GLPanel glPanel;
    protected TGPanel tgPanel;
    protected TGUIManager ui;
    protected addNodeListener anL;
    protected Node firstNode;
    protected Node secondNode;
    protected Edge conEdge;
    protected boolean activated;

    public TGAddEdgeUI(GLPanel GLP) {
        glPanel = GLP;
        tgPanel = glPanel.getTGPanel();
        ui = glPanel.getTgUIManager();
        anL = new addNodeListener();
        activated = false;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    @Override
    public void activate() {
        if (!isActivated()) {
            tgPanel.addMouseListener(anL);
            setActivated(true);
        } else {
            deactivate();
        }
    }

    public void deactivate() {
        tgPanel.removeMouseListener(anL);
        setActivated(false);
    }

    public void deactivateUI() {
        firstNode = null;       // reset nodes for unequality...
        secondNode = null;
        ui.deactivate("addEdgeUI");
        ui.activate("Navigate");
        //System.out.println("r");
    }

    public class addNodeListener extends MouseAdapter {

        public void mousePressed(MouseEvent e) {
            if (firstNode == null) {
                firstNode = tgPanel.getMouseOverN();
            } else if (secondNode == null) {
                secondNode = tgPanel.getMouseOverN();
            }

            if (firstNode != null && secondNode != null && firstNode != secondNode) {
                conEdge = new Edge(firstNode, secondNode, Edge.DEFAULT_LENGTH);
                tgPanel.addEdge(conEdge);
            }

            if (e.getButton() == MouseEvent.BUTTON3) {
                deactivateUI();
            }
        }

        public void mouseReleased(MouseEvent e) {
            if (firstNode != null && secondNode != null && firstNode != secondNode) {
                deactivateUI();
            }
        }
    }

}
