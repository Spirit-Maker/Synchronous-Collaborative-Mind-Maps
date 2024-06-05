/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Network.Client;

import Network.Server.RMIServerInterface;
import audio.Speech;
import com.touchgraph.graphlayout.GLPanel;
import com.touchgraph.graphlayout.Node;
import com.touchgraph.graphlayout.graphelements.VisibleLocality;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Zahoor Ali
 */
public class Client extends Thread implements java.io.Serializable {

    private static Registry Serverregistry;
    private static Registry Clientregistry;
    private static RMIServerInterface RMIServerInterface;
    private static RMIClient RMIClient;
    private static GLPanel glPanel = null;
    private static String ip = "127.0.0.1";
    private static int clientPort = 6667;
    protected static boolean connected = false;

    public static void deleteClientToServerNode(Node node) {
        try {
            RMIServerInterface.deleteClientToServerNode(node);
            updateVisibleLocality(RMIServerInterface.getVisibleLocality());
        } catch (RemoteException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void setGLPanel(GLPanel glPanel) {
        Client.glPanel = glPanel;

    }

    static void updateVisibleLocality(VisibleLocality vloc) {
        glPanel.getTGPanel().setVisibleLocality(vloc);
    }

    public static boolean Connect(String username, String pass, String ip, int port) {
        try {
            Serverregistry = LocateRegistry.getRegistry(ip, port);
            RMIServerInterface = (RMIServerInterface) Serverregistry.lookup(ip);
            if (RMIServerInterface.checkLoginInfo(username, pass, ip, port)) {
                if (RMIServerInterface.isMindMapAvailable()) {
                    RMIClient = new RMIClient();
                    try {
                        if (!"127.0.0.1".equals(ip)) {
                            Client.ip = InetAddress.getLocalHost().getHostAddress();
                        }
                    } catch (UnknownHostException ex) {
                        JOptionPane.showConfirmDialog(null, "IPV4 Address not found." + ex.getMessage(), "Error", JOptionPane.PLAIN_MESSAGE);
                    }
                    if (RMIServerInterface.addClient(Client.ip)) {
                        connected = true;
                        updateVisibleLocality(RMIServerInterface.getVisibleLocality());
                        glPanel.getTGPanel().repaint();
                        glPanel.getTGPanel().getMF().updateTree(null);
                        if (!"127.0.0.1".equals(Client.ip)) {
                            System.setProperty("java.rmi.server.hostname", Client.ip);
                        }
                        if (Clientregistry == null) {
                            Clientregistry = LocateRegistry.createRegistry(6667);
                        } else {
                            Clientregistry = LocateRegistry.getRegistry(port);
                        }
                        Clientregistry.rebind(Client.ip, RMIClient);
//                        Client.updateServerToClientNodeText(RMIServerInterface.getVisibleLocality().findRoot());
                        return true;
                    } else {
                        JOptionPane.showConfirmDialog(null, "Already Connected. Disconnecting.", "Error", JOptionPane.PLAIN_MESSAGE);
                        disconnect();
                        //Connect(username, pass, ip, port);
                    }
                } else {
                    JOptionPane.showConfirmDialog(null, "Diagram Not Shared by the Computer.", "Error", JOptionPane.PLAIN_MESSAGE);
                    glPanel.getTGPanel().getMF().getTabbedPane().removeTabAt(glPanel.getTGPanel().getMF().getTabbedPane().getSelectedIndex());
                    return false;
                }
            } else {
                JOptionPane.showConfirmDialog(null, "Error wrong input.", "Error", JOptionPane.PLAIN_MESSAGE);
                return false;
            }
        } catch (RemoteException | NotBoundException ex) {
//            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
//            JOptionPane.showConfirmDialog(null, "Unable to find Server.\n" + ex.getMessage(), "Error", JOptionPane.PLAIN_MESSAGE);
        }
        return false;
    }

    public static void disconnect() {
        if (connected) {
            try {
                if (RMIServerInterface.disconnectClient(Client.ip)) {

                }
                UnicastRemoteObject.unexportObject(RMIClient, true);
                Client.Clientregistry.unbind(Client.ip);
                Client.glPanel = null;
            } catch (RemoteException ex) {
                JOptionPane.showConfirmDialog(null, ex.getMessage(), "Error", JOptionPane.PLAIN_MESSAGE);
            } catch (NotBoundException ex) {
                JOptionPane.showConfirmDialog(null, ex.getMessage(), "Error", JOptionPane.PLAIN_MESSAGE);
//                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
            connected = false;
        }
    }

    public static GLPanel getGLPanel() {
        return Client.glPanel;
    }

    public static String getIPAddress() {
        return ip;
    }

    public static int getClientPort() {
        return Client.clientPort;
    }

    public static boolean isConnected() {
        return connected;
    }

    // functions to be accessed by local to access Server
    public static void addClientToServerChildNode(Node parent, Node node) {
        try {
            RMIServerInterface.addClientToServerChildNode(parent, node);
        } catch (RemoteException ex) {
            JOptionPane.showConfirmDialog(null, ex.getMessage(), "Error", JOptionPane.PLAIN_MESSAGE);
        }
        Client.glPanel.getTGPanel().repaint();
    }

    public static void updateClientToServerNodeColor(Node node) {
        try {
            RMIServerInterface.updateClientToServerNodeColor(node);
        } catch (RemoteException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        Client.glPanel.getTGPanel().repaint();
    }

    public static void updateClientToServerNodeText(Node node) {
        try {
            RMIServerInterface.updateClientToServerNodeText(node);
        } catch (RemoteException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        Client.glPanel.getTGPanel().repaint();
    }

    // Functions to be accessed by Server via RMIClient class
    public static void disconnectByServer() {
        if (connected) {
            try {
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        JOptionPane.showConfirmDialog(null, "Server has Stopped.", "Server Stoped", JOptionPane.PLAIN_MESSAGE);
                    }
                };
                thread.start();
                UnicastRemoteObject.unexportObject(RMIClient, true);
                Client.Clientregistry.unbind(Client.ip);
                Client.glPanel = null;
            } catch (RemoteException ex) {
                JOptionPane.showConfirmDialog(null, ex.getMessage(), "Error", JOptionPane.PLAIN_MESSAGE);
            } catch (NotBoundException ex) {
                JOptionPane.showConfirmDialog(null, ex.getMessage(), "Error", JOptionPane.PLAIN_MESSAGE);
//                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
            connected = false;
        }
    }

    public static void addServerToClientChildNode(Node parent, Node node) {
        try {
            glPanel.getTGPanel().setVisibleLocality(RMIServerInterface.getVisibleLocality());
            glPanel.getTGPanel().repaint();
        } catch (RemoteException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        Client.glPanel.getTGPanel().repaint();
        glPanel.getTGPanel().getMF().updateTree(null);
        Thread thread = new Thread() {
            @Override
            public void run() {

                Speech speech = new Speech(null);
                speech.speak("Child node of " + parent.getLabel() + " is created.", (float) 1.2);
            }
        };
        if (glPanel.getTGPanel().getMF().isSelecetSoundEnabled()) {
            while (Speech.running) {
            }
            thread.start();
        }
    }

    public static void deleteServerToClientNode(Node node) {
        if (node == null) {
            return;
        }
        if (glPanel.getTGPanel().findNode(node.getLabel()) != null) {
            if (node.getType() == 2) {
                glPanel.getTGPanel().deleteNodeById(node.getID());
                glPanel.getTGPanel().getMF().removeTab();
            } else {
                glPanel.getTGPanel().deleteNodeById(node.getID());
            }
        }
        try {
            Client.updateVisibleLocality(RMIServerInterface.getVisibleLocality());
        } catch (RemoteException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        Client.glPanel.getTGPanel().repaint();
        glPanel.getTGPanel().getMF().updateTree(null);
        Thread thread = new Thread() {
            public void run() {
                Speech speech = new Speech(null);
                speech.speak(node.getLabel() + " is deleted.", (float) 1.2);
            }
        };
        if (glPanel.getTGPanel().getMF().isSelecetSoundEnabled()) {
            while (Speech.running) {
            }
            thread.start();
        }
    }

    public static void updateServerToClientNodeColor(Node node) {
        if (node == null) {
//            JOptionPane.showConfirmDialog(null, "Unable to update null node passed .", "Error", JOptionPane.PLAIN_MESSAGE);
        }

        Node n = glPanel.getTGPanel().getVisibleLocality().findNode(node.getID());
        if (n.getType() == 2) {
            n.BACK_FIXED_MAIN_COLOR = node.BACK_FIXED_MAIN_COLOR;
        } else {
            n.BACK_FIXED_COLOR = node.BACK_FIXED_COLOR;
        }
        glPanel.getTGPanel().update(glPanel.getTGPanel().getGraphics());
        Client.glPanel.getTGPanel().repaint();
    }

    public static void updateServerToClientNodeText(Node node) {
        if (node == null) {
            return;
        }

        Node n = glPanel.getTGPanel().getVisibleLocality().findNode(node.getID());
        if (n.getType() == Node.TYPE_ROUNDRECT) {
            int y = glPanel.getTGPanel().getMF().getTabbedPane().getSelectedIndex();
            glPanel.getTGPanel().getMF().getTabbedPane().setTitleAt(y, node.getLabel());
        }
        n.setLabel(node.getLabel());
        Client.glPanel.getTGPanel().repaint();
        glPanel.getTGPanel().update(glPanel.getTGPanel().getGraphics());

    }

}
