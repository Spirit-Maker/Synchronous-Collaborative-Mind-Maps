/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Network.Server;

import Network.Client.RMIClientInterface;
import Network.Config;
import com.touchgraph.graphlayout.GLPanel;
import com.touchgraph.graphlayout.Node;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Zahoor Ali
 */
public class Server extends Thread implements ServerInterface {

    protected static boolean isRunning = false;
    protected static Registry registry;
    protected static RMIServer RMIServer;
    protected static Vector Clients = new Vector();
    protected static GLPanel glPanel = null;

    public static void startServer() {
        if (!isRunning) {
            isRunning = true;

            try {
                System.setProperty("java.rmi.server.hostname", Config.serverAddress);
                if (registry == null) {
                    registry = LocateRegistry.createRegistry(Config.serverPort);
                } else {
                    registry = LocateRegistry.getRegistry(Config.serverPort);
                }
                RMIServer = new RMIServer();
                registry.rebind(Config.serverAddress, RMIServer);
            } catch (RemoteException ex) {
                ex.printStackTrace();
                JOptionPane.showConfirmDialog(null, "Unable to start Server.\n" + ex.getMessage(), "Error", JOptionPane.PLAIN_MESSAGE);
            }
        } else {
            JOptionPane.showConfirmDialog(null, "Server is already Running.", null, JOptionPane.OK_OPTION);
            return;
        }
    }

    public static void stopServer() {
        if (isRunning) {
            isRunning = false;

            if (registry != null) {
                try {
                    UnicastRemoteObject.unexportObject(RMIServer, true);
                    registry.unbind(Config.serverAddress);
                    Server.glPanel = null;
                    Server.disconnectAllClients();
                } catch (NoSuchObjectException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                } catch (RemoteException | NotBoundException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public static void disconnectAllClients() {
        for (Object c : Server.Clients) {
            String ip = (String) c;
            try {
                Registry ClientRegistry = LocateRegistry.getRegistry(ip, 6667);
                RMIClientInterface RMIClientInterface = (RMIClientInterface) ClientRegistry.lookup(ip);
                Server.disconnectClient(ip);
                RMIClientInterface.DisconnectByServer();
            } catch (RemoteException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NotBoundException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static boolean isRunning() {
        return isRunning;
    }

    public static boolean addClient(String client) {
        if (Clients.contains(client)) {
            return false;
        } else {
            Clients.addElement(client);
            return true;
        }
    }

    public static boolean disconnectClient(String client) {
        if (Clients.contains(client)) {
            Clients.remove(client);
            return true;
        } else {
            return false;
        }
    }

    public static void removeAllClients() {
        Clients.removeAllElements();
    }

    public static Vector getClients() {
        return Clients;
    }

    public static GLPanel getGlPanel() {
        return glPanel;
    }

    public static void setGlPanel(GLPanel glPanel) {
        Server.glPanel = glPanel;
    }

    public static boolean isMindMapAvailable() {
        if (Server.glPanel == null) {
            return false;
        } else {
            return true;
        }
    }

    // RMI server Update Notification
    public static void addServerToClientChildNode(Node parent, Node node) {
        for (Object c : Server.Clients) {
            String ip = (String) c;
            try {
                Registry ClientRegistry = LocateRegistry.getRegistry(ip, 6667);
                RMIClientInterface RMIClientInterface = (RMIClientInterface) ClientRegistry.lookup(ip);
                RMIClientInterface.addServerToClientChildNode(parent, node);
            } catch (RemoteException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NotBoundException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void deleteServerToClientNode(Node node) {
        for (Object c : Server.Clients) {
            String ip = (String) c;
            try {
                Registry ClientRegistry = LocateRegistry.getRegistry(ip, 6667);
                RMIClientInterface RMIClientInterface = (RMIClientInterface) ClientRegistry.lookup(ip);
                RMIClientInterface.deleteServerToClientNode(node);
            } catch (RemoteException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NotBoundException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    static void updateClientDataStructure() {
        for (Object c : Server.Clients) {
            String ip = (String) c;
            try {
                Registry ClientRegistry = LocateRegistry.getRegistry(ip, 6667);
                RMIClientInterface RMIClientInterface = (RMIClientInterface) ClientRegistry.lookup(ip);
                RMIClientInterface.updateVisibleLocality(Server.getGlPanel().getTGPanel().getVisibleLocality());
            } catch (RemoteException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NotBoundException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void updateServerToClientNodeColor(Node node) {
        for (Object c : Server.Clients) {
            String ip = (String) c;
            try {
                Registry ClientRegistry = LocateRegistry.getRegistry(ip, 6667);
                RMIClientInterface RMIClientInterface = (RMIClientInterface) ClientRegistry.lookup(ip);
                RMIClientInterface.updateServerToClientNodeColor(node);
            } catch (RemoteException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NotBoundException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    public static void updateServerToClientNodeText(Node node) {
        for (Object c : Server.Clients) {
            String ip = (String) c;
//            System.out.println("RMI Server to client text" + node.getID() + " " + ip);
            try {
                Registry ClientRegistry = LocateRegistry.getRegistry(ip, 6667);
                RMIClientInterface RMIClientInterface = (RMIClientInterface) ClientRegistry.lookup(ip);
                RMIClientInterface.updateServerToClientNodeText(node);

            } catch (RemoteException | NotBoundException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }
}
