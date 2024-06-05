/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Network.Server;

import Network.Client.Client;
import Network.Client.RMIClientInterface;
import Network.Config;
import audio.Speech;
import com.touchgraph.graphlayout.Edge;
import com.touchgraph.graphlayout.Node;
import com.touchgraph.graphlayout.TGException;
import com.touchgraph.graphlayout.graphelements.VisibleLocality;
import java.rmi.Remote;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

/**
 *
 * @author Zahoor Ali
 */
public class RMIServer extends UnicastRemoteObject implements RMIServerInterface {

    public RMIServer() throws RemoteException {
    }

    @Override
    public boolean addClient(String client) throws RemoteException {

        if (Server.addClient(client)) {
            Thread thread = new Thread() {
                public void run() {
                    JOptionPane.showConfirmDialog(null, "Client " + client + " has Joined.", "Client joined", JOptionPane.PLAIN_MESSAGE);
                }
            };
            thread.start();
            return true;
        }
        return false;
    }

    @Override
    public void deleteClientToServerNode(Node node) throws RemoteException {
        if (node == null) {
            return;
        }
        if (Server.glPanel.getTGPanel().findNode(node.getID()) != null) {
            if (node.getType() == 2) {
                Server.glPanel.getTGPanel().deleteNodeById(node.getID());
                Server.glPanel.getTGPanel().getMF().removeTab();
            } else {
                Server.glPanel.getTGPanel().deleteNodeById(node.getID());
            }
        }
        Server.glPanel.getTGPanel().getMF().updateTree(null);
        Server.glPanel.getTGPanel().repaint();
        updateClientDataStructure();
        Thread thread = new Thread() {
            public void run() {
                Speech speech = new Speech(null);
                speech.speak(node.getLabel() + " is deleted.", (float) 1.2);
            }
        };
        if (Server.glPanel.getTGPanel().getMF().isSelecetSoundEnabled()) {
            while (Speech.running) {
            }
            thread.start();
        }
    }

    @Override
    public boolean disconnectClient(String client) throws RemoteException {
        if (Server.disconnectClient(client)) {
            Thread thread = new Thread() {
                public void run() {
                    JOptionPane.showConfirmDialog(null, "Client " + client + " has disconnected.", "Client disconnect", JOptionPane.PLAIN_MESSAGE);
                }
            };
            thread.start();
            return true;
        }
        return false;
    }

    @Override
    public void addClientToServerChildNode(Node parent, Node node) throws RemoteException {
        if (parent == null) {
            return;
        }

        try {
            Node n;
            if (node != null) {

                n = Server.glPanel.getTGPanel().addNode(node.getID(), node.getLabel(), node.getType());
                n.setLabel(node.getLabel());
                n.setPermlbl(node.getPermlbl());
                n.BACK_FIXED_COLOR = node.BACK_FIXED_COLOR;
                if (node.getType() == Node.TYPE_Image) {
                    n.setImage(node.getImage());
                }
            } else {
                n = Server.glPanel.getTGPanel().addNode(1);
            }
            Server.glPanel.getTGPanel().addNode(n);
            Node par;
            if (Server.glPanel.getTGPanel().findNode(parent.getID()) != null) {
                par = Server.glPanel.getTGPanel().findNode(parent.getID());
            } else {
                par = Server.glPanel.getTGPanel().addNode(parent.getID(), parent.getLabel(), 1);
                this.addClientToServerChildNode(parent.getParent(), par);
            }

            Server.glPanel.getTGPanel().addEdge(par, n, Edge.DEFAULT_LENGTH);
            Server.glPanel.getTGPanel().getMF().updateTree(null);
            Server.glPanel.getTGPanel().repaint();
            Thread thread = new Thread() {
                public void run() {

                    Speech speech = new Speech(null);
                    speech.speak("Child node of " + parent.getLabel() + " is created.", (float) 1.2);
                }
            };
            if (Server.glPanel.getTGPanel().getMF().isSelecetSoundEnabled()) {
                while (Speech.running) {
                }
                thread.start();

            }
        } catch (TGException ex) {
            Logger.getLogger(Client.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        updateClientDataStructure();
    }

    @Override
    public boolean checkLoginInfo(String username, String Password, String ip, int port) {
        return username.equals(Config.username) && Password.equals(Config.password) && ip.equals(Config.serverAddress) && port == Config.serverPort;
    }

    @Override
    public Node getRootNode() throws RemoteException {
        if (Server.getGlPanel() != null) {
            return Server.glPanel.getTGPanel().findRoot();
        }
        return null;
    }

    @Override
    public VisibleLocality getVisibleLocality() throws RemoteException {
        if (Server.getGlPanel() != null) {
            return Server.glPanel.getTGPanel().getVisibleLocality();
        }
        return null;
    }

    public void updateClientDataStructure() throws RemoteException {
        Server.updateClientDataStructure();
    }

    public boolean isServerRunning() {
        return Server.isRunning();
    }

    public boolean isMindMapAvailable() {
        return Server.isMindMapAvailable();
    }

    @Override
    public void updateClientToServerNodeColor(Node node) throws RemoteException {
        if (node == null) {
            return;
        }
        Node n = Server.glPanel.getTGPanel().getVisibleLocality().findNode(node.getID());
//        if (n == null) {
//            return;
////            addClientToServerChildNode(node, null);
//        }
        if (n.getType() == 2) {
            n.BACK_FIXED_MAIN_COLOR = node.BACK_FIXED_MAIN_COLOR;
        } else {
            n.BACK_FIXED_COLOR = node.BACK_FIXED_COLOR;
        }
        Server.glPanel.getTGPanel().repaint();
    }

    @Override
    public void updateClientToServerNodeText(Node node) throws RemoteException {
        if (node == null) {
            return;
        }
//        if (Server.glPanel.getTGPanel().getVisibleLocality().findNode(node.getID()) == null) {
////            addClientToServerChildNode(node, null);
//        } else {
        Node n = Server.glPanel.getTGPanel().getVisibleLocality().findNode(node.getID());
        if (n.getType() == Node.TYPE_ROUNDRECT) {
            int y = Server.glPanel.getTGPanel().getMF().getTabbedPane().getSelectedIndex();
            Server.glPanel.getTGPanel().getMF().getTabbedPane().setTitleAt(y, node.getLabel());
        }
        n.setLabel(node.getLabel());
//        }
        Server.glPanel.getTGPanel().repaint();
    }
}
