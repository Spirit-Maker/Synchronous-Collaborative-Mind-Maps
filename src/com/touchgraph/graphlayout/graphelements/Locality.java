package com.touchgraph.graphlayout.graphelements;

import audio.Sound;
import com.touchgraph.graphlayout.Edge;
import com.touchgraph.graphlayout.Node;
import com.touchgraph.graphlayout.TGException;
import java.util.Vector;

public class Locality extends GraphEltSet {

    protected GraphEltSet completeEltSet;

    public Locality(GraphEltSet ges) {
        super();
        completeEltSet = ges;
    }

    public GraphEltSet getCompleteEltSet() {
        return completeEltSet;
    }

    @Override
    public synchronized void addNode(Node n) throws TGException {
        if (!contains(n)) {
            super.addNode(n);

            if (!completeEltSet.contains(n)) {
                completeEltSet.addNode(n);
            }
        }
    }

    @Override
    public synchronized void addNodeByForce(Node n) throws TGException {
        if (!contains(n)) {
            super.addNodeByForce(n);

            if (!completeEltSet.contains(n)) {
                completeEltSet.addNodeByForce(n);
            }
        }
    }

    @Override
    public void addEdge(Edge e) {
        if (!contains(e)) {
            edges.addElement(e);

            if (!completeEltSet.contains(e)) {
                completeEltSet.addEdge(e);
            }
        }
    }

    public synchronized void addNodeWithEdges(Node n) throws TGException {
        addNode(n);
        for (int i = 0; i < n.edgeCount(); i++) {
            Edge e = n.edgeAt(i);
            if (contains(e.getOtherEndpt(n))) {
                addEdge(e);
            }
        }

    }

    public synchronized void addAll() throws TGException {
        synchronized (completeEltSet) {
            for (int i = 0; i < completeEltSet.nodeCount(); i++) {
                addNode(completeEltSet.nodeAt(i));
            }
            for (int i = 0; i < completeEltSet.edgeCount(); i++) {
                addEdge(completeEltSet.edgeAt(i));
            }
        }
    }

    @Override
    public Edge findEdge(Node from, Node to) {
        Edge foundEdge = super.findEdge(from, to);
        if (foundEdge != null && edges.contains(foundEdge)) {
            return foundEdge;
        } else {
            return null;
        }
    }

    @Override
    public boolean deleteEdge(Edge e) {
        if (e == null) {
            return false;
        } else {
            removeEdge(e);
            return completeEltSet.deleteEdge(e);
        }
    }

    @Override
    public synchronized void deleteEdges(Vector edgesToDelete) {
        removeEdges(edgesToDelete);
        completeEltSet.deleteEdges(edgesToDelete);
    }

    public boolean removeEdge(Edge e) {
        if (e == null) {
            return false;
        } else {
            return edges.removeElement(e);
        }
    }

    public synchronized void removeEdges(Vector edgesToRemove) {
        for (int i = 0; i < edgesToRemove.size(); i++) {
            removeEdge((Edge) edgesToRemove.elementAt(i));
        }
    }

    @Override
    public boolean deleteNode(Node node) {
        if (node == null) {
            return false;
        } else {
            if (node.getChildren().size() > 0) {
                deleteNodes(node.getChildren());
            }
            if (node.getParent() != null) {
                node.getParent().deleteChild(node);
            }
            removeNode(node);
            Runnable delSound = new Sound("src/resources/sound/nodeDeleted.wav", "wav");
            new Thread(delSound).start();
            super.deleteNode(node);
            return completeEltSet.deleteNode(node);
        }
    }

    @Override
    public synchronized void deleteNodes(Vector nodesToDelete) {
        if (nodesToDelete.size() <= 0) {
            return;
        }
        Vector subChild = new Vector();
        for (int i = 0; i < nodesToDelete.size(); i++) {
            subChild.addAll(((Node) nodesToDelete.elementAt(i)).getChildren());
        }
        deleteNodes(subChild);
        removeNodes(nodesToDelete);
        completeEltSet.deleteNodes(nodesToDelete);
    }

    public boolean removeNode(Node node) {
        if (node == null) {
            return false;
        }
//        System.out.println(nodes.contains(node) + " " + node.getID());
        if (!nodes.removeElement(node)) {
            System.out.println(node.getID());
            return false;
        }

        String id = node.getID();
        if (id != null) {
            nodeIDRegistry.remove(id); // remove from registry
        }
        for (int i = 0; i < node.edgeCount(); i++) {
            removeEdge(node.edgeAt(i));
        }

        return true;
    }

    public synchronized void removeNodes(Vector nodesToRemove) {
        for (int i = 0; i < nodesToRemove.size(); i++) {
            removeNode((Node) nodesToRemove.elementAt(i));
        }
    }

    public synchronized void removeAll() {
        super.clearAll();
    }

    @Override
    public synchronized void clearAll() {
        removeAll();
        completeEltSet.clearAll();
    }

}
