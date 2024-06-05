/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Network.Client;

import com.touchgraph.graphlayout.Node;
import com.touchgraph.graphlayout.graphelements.VisibleLocality;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Zahoor Ali
 */
public interface RMIClientInterface extends Remote {

    public void addServerToClientChildNode(Node parent, Node node) throws RemoteException;

    public void deleteServerToClientNode(Node node) throws RemoteException;

    public void updateServerToClientNodeColor(Node node) throws RemoteException;

    public void updateServerToClientNodeText(Node node) throws RemoteException;

    public void updateVisibleLocality(VisibleLocality vloc) throws RemoteException;

    public void DisconnectByServer() throws RemoteException;
}
