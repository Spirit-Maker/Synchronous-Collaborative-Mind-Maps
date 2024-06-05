/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.touchgraph.graphlayout.interaction;

import Network.Client.Client;
import Network.Server.Server;
import com.touchgraph.graphlayout.Edge;
import com.touchgraph.graphlayout.GLPanel;
import com.touchgraph.graphlayout.Node;
import com.touchgraph.graphlayout.TGException;
import com.touchgraph.graphlayout.TGPanel;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Zahoor Ali
 */
public class TGAddImageUI extends TGUserInterface {
    
    protected GLPanel glPanel;
    protected TGPanel tgPanel;
    protected addNodeListener anL;
    protected Node addNode;
    protected int nodeType = 5;
    protected boolean activated;
    
    public TGAddImageUI(GLPanel GLP) {
        glPanel = GLP;
        tgPanel = glPanel.getTGPanel();
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
    
    public void setNodeType(int type) {
        nodeType = type;
    }
    
    public class addNodeListener extends MouseAdapter {
        
        public void mousePressed(MouseEvent e) {
            Point p = e.getPoint();
            Node mouseOverN = tgPanel.getMouseOverN();
            if (mouseOverN != null) {
                try {
                    addNode = tgPanel.addNode(nodeType);
//                    System.out.println(nodeType);
                    if (addNode.getType() == 5) {
                        JFileChooser chooser = new JFileChooser();
                        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                        int ret = chooser.showOpenDialog(glPanel);
                        if (ret == JFileChooser.APPROVE_OPTION) {
                            addNode.setFile(chooser.getSelectedFile());
                            tgPanel.addNode(addNode);
                            tgPanel.addEdge(mouseOverN, addNode, Edge.DEFAULT_LENGTH);
                            tgPanel.getMF().updateTree(null);
                            tgPanel.repaint();
                            TGUIManager ui = glPanel.getTgUIManager();
                            ui.deactivate("addImageUI");
                            ui.activate("Navigate");
                        }
                        if (Server.isRunning() && Server.getGlPanel().getTGPanel() == tgPanel && Server.getGlPanel().getTGPanel() != null) {
                            Server.addServerToClientChildNode(mouseOverN, addNode);
                        }
                        if (Client.isConnected() && Client.getGLPanel().getTGPanel() == tgPanel && Client.getGLPanel().getTGPanel() != null) {
                            Client.addClientToServerChildNode(mouseOverN, addNode);
                        }
                    }
                } catch (TGException ex) {
                    Logger.getLogger(TGAddNodeUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        public void mouseReleased(MouseEvent e) {
            TGUIManager ui = glPanel.getTgUIManager();
            ui.deactivate("addImageUI");
            ui.activate("Navigate");
        }
        
    }
    
    public void setNode(Node node) {
        addNode = node;
    }
    
}
