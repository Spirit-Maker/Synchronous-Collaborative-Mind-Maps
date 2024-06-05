/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Network.Server;

import Network.Client.Client;
import Network.Client.RMIClient;
import Network.Client.RMIClientInterface;
import com.touchgraph.graphlayout.Node;
import com.touchgraph.graphlayout.graphelements.VisibleLocality;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Vector;

/**
 *
 * @author Zahoor Ali
 */
public interface RMIServerInterface extends Remote {

    public boolean checkLoginInfo(String username, String Password, String ip, int port) throws RemoteException;

    public boolean addClient(String client) throws RemoteException;

    public boolean disconnectClient(String client) throws RemoteException;

    public Node getRootNode() throws RemoteException;

    public boolean isMindMapAvailable() throws RemoteException;

    public boolean isServerRunning() throws RemoteException;

    public VisibleLocality getVisibleLocality() throws RemoteException;

    public void addClientToServerChildNode(Node parent, Node node) throws RemoteException;

    public void deleteClientToServerNode(Node node) throws RemoteException;

    public void updateClientToServerNodeColor(Node node) throws RemoteException;

    public void updateClientToServerNodeText(Node node) throws RemoteException;

    public void updateClientDataStructure() throws RemoteException;
}
