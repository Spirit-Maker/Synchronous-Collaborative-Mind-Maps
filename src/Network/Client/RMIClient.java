/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Network.Client;

import com.touchgraph.graphlayout.Node;
import com.touchgraph.graphlayout.graphelements.VisibleLocality;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author Zahoor Ali
 */
public class RMIClient extends UnicastRemoteObject implements RMIClientInterface {

    public RMIClient() throws RemoteException {
    }

    @Override
    public void DisconnectByServer() throws RemoteException {
        Client.disconnectByServer();
    }

    @Override
    public void addServerToClientChildNode(Node parent, Node node) throws RemoteException {
        Client.addServerToClientChildNode(parent, node);
    }

    @Override
    public void deleteServerToClientNode(Node node) throws RemoteException {
        Client.deleteServerToClientNode(node);
    }

    @Override
    public void updateServerToClientNodeColor(Node node) throws RemoteException {
        Client.updateServerToClientNodeColor(node);
    }

    @Override
    public void updateServerToClientNodeText(Node node) throws RemoteException {
        Client.updateServerToClientNodeText(node);
    }

    @Override
    public void updateVisibleLocality(VisibleLocality vloc) throws RemoteException {
        Client.updateVisibleLocality(vloc);
    }

}
