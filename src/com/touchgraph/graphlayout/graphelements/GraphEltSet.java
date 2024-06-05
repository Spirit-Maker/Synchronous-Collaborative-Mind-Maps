package com.touchgraph.graphlayout.graphelements;

import com.touchgraph.graphlayout.Edge;
import com.touchgraph.graphlayout.Node;
import com.touchgraph.graphlayout.TGException;
import java.util.Hashtable;
import java.util.Vector;

public class GraphEltSet implements ImmutableGraphEltSet, java.io.Serializable {

    protected Vector nodes;
    protected Vector edges;

    protected Hashtable nodeIDRegistry = null;

    public GraphEltSet() {
        nodes = new Vector();
        edges = new Vector();
        nodeIDRegistry = new Hashtable(); // registry of Node IDs
    }

    public void setNodes(Vector n) {
        if (n == null) {
            return;
        }
        nodes = n;
    }

    public void setEdges(Vector e) {
        if (e == null) {
            return;
        }
        edges = e;
    }

    public Node nodeAt(int i) {
        if (nodes.size() == 0) {
            return null;
        }
        return (Node) nodes.elementAt(i);
    }

    public int nodeCount() {
        return nodes.size();
    }

    public synchronized void addNode(Node node) throws TGException {

        String id = node.getID();

        if (id != null) {
            if (findNode(id) == null) { // doesn't already exist
                nodeIDRegistry.put(id, node);
                nodes.addElement(node);
            } else {
                while (true) {
                    if (findNode(id) != null) {
                        int iden = Integer.parseInt(id);
                        iden++;
                        id = String.valueOf(iden);
                    } else {
//                        System.out.println(id);
                        nodeIDRegistry.put(id, node);
                        nodes.addElement(node);
                        node.setID(id);
                        node.setLabel(id);
                        break;
                    }
                }
            }
        } else {
            String label = node.getLabel().trim();
            if (label == null) {
                label = "";
            }
            if (!label.equals("") && findNode(node.getLabel()) == null) {
                id = label;
            } else {
                int i;
                for (i = 1; findNode(label + "-" + i) != null; i++);
                id = label + "-" + i;
            }
            node.setID(id);
            nodeIDRegistry.put(id, node);
            nodes.addElement(node);
        }

    }

    public synchronized void addNodeByForce(Node node) throws TGException {

        String id = node.getID();
        if (id != null) {
            if (findNode(id) == null) { // doesn't already exist
                nodeIDRegistry.put(id, node);
                nodes.addElement(node);
            } else {
                Node n = findNode(node.getID());
                int index = nodes.indexOf(n);
                double x = n.x;
                double y = n.y;
                n = node;
                n.x = x;
                n.y = y;

                nodeIDRegistry.replace(n.getID(), n);
                nodes.remove(index);
                nodes.add(index, n);

            }
        } else {

        }
    }

    public boolean contains(Node node) {
        return nodes.contains(node);
    }

    protected Edge edgeAt(int index) {
        if (edges.size() == 0) {
            return null;
        }
        return (Edge) edges.elementAt(index);
    }

    public int edgeNum() {
        return edges.size();
    }

    public int edgeCount() {
        return edges.size();
    }

    public void addEdge(Edge edge) {
        if (edge == null) {
            return;
        }
        if (!contains(edge)) {
            edges.addElement(edge);
            edge.from.addEdge(edge);
            edge.to.addEdge(edge);
        }
    }

    public Edge addEdge(Node from, Node to, int tension) {
        Edge edge = null;
        if (from != null && to != null) {
            edge = new Edge(from, to, tension);
            addEdge(edge);
        }
        return edge;
    }

    public boolean contains(Edge edge) {
        return edges.contains(edge);
    }

    public Node findNode(String id) {
        if (id == null) {
            return null; // ignore
        }

//        for (Object n : nodes) {
////            Node node = (Node) n;
//            if (((Node) n).getID() == id) {
//                return (Node) n;
//            }
//        }
        return (Node) this.nodeIDRegistry.get(id);
    }

    public Edge findEdge(Node from, Node to) {
        for (int i = 0; i < from.edgeCount(); i++) {
            Edge e = from.edgeAt(i);
            if (e.to == to) {
                return e;
            }
        }
        return null;
    }

    public boolean deleteEdge(Edge edge) {
        synchronized (edges) {
            if (edge == null) {
                return false;
            }
            if (!edges.removeElement(edge)) {
                return false;
            }
            edge.from.removeEdge(edge);
            edge.to.removeEdge(edge);
            return true;
        }
    }

    public void deleteEdges(Vector edgesToDelete) {
        synchronized (edges) {
            for (int i = 0; i < edgesToDelete.size(); i++) {
                deleteEdge((Edge) edgesToDelete.elementAt(i));
            }
        }
    }

    public boolean deleteEdge(Node from, Node to) {
        synchronized (edges) {
            Edge e = findEdge(from, to);
            if (e != null) {
                return deleteEdge(e);
            }
            return false;
        }
    }

    public boolean deleteNode(Node node) {
        synchronized (nodes) {

            if (node == null) {
                return false;
            }
            if (!nodes.removeElement(node)) {
                return false;
            }
            if (node.getParent() != null) {
                node.getParent().deleteChild(node);
            }
            String id = node.getID();
            nodes.removeElement(node);
            if (id != null) {
                nodeIDRegistry.remove(id); // remove from registry
            }
            for (int i = 0; i < node.edgeCount(); i++) {
                Edge e = node.edgeAt(i);
                if (e.from == node) {
                    edges.removeElement(e); // Delete edge not used, because it would change the node's edges
                    e.to.removeEdge(e);     // vector which is being iterated on.
                } else if (e.to == node) {
                    edges.removeElement(e);
                    e.from.removeEdge(e);
                }
            }

        }
        return true;
    }

    /**
     * Delete the Nodes contained within the Vector <tt>nodesToDelete</tt>.
     */
    public void deleteNodes(Vector nodesToDelete) {
        synchronized (nodes) {
            for (int i = 0; i < nodesToDelete.size(); i++) {
                deleteNode((Node) nodesToDelete.elementAt(i));
            }
        }
    }

    public Vector getNodes() {
        return nodes;
    }

    public Vector getEdges() {
        return edges;
    }

    public Hashtable getNodeIDRegistry() {
        return nodeIDRegistry;
    }

    public void clearAll() {
        synchronized (nodes) {
            synchronized (edges) {
                nodes.removeAllElements();
                edges.removeAllElements();
                nodeIDRegistry.clear();
            }
        }
    }

    public void forAllNodes(TGForEachNode fen) {
        synchronized (nodes) {
            for (int i = 0; i < nodeCount(); i++) {
                Node n = nodeAt(i);
                fen.forEachNode(n);
            }
        }
    }

    public void forAllNodePairs(TGForEachNodePair fenp) {
        synchronized (nodes) {
            for (int i = 0; i < nodeCount(); i++) {
                Node n1 = nodeAt(i);
                fenp.beforeInnerLoop(n1);
                for (int j = i + 1; j < nodeCount(); j++) {
                    fenp.forEachNodePair(n1, nodeAt(j));
                }
                fenp.afterInnerLoop(n1);
            }
        }
    }

    /**
     * Iterates through Edges.
     */
    public void forAllEdges(TGForEachEdge fee) {
        synchronized (edges) {
            for (int i = 0; i < edgeCount(); i++) {
                Edge e = edgeAt(i);
                fee.forEachEdge(e);
            }
        }
    }

    public Node findRoot() {
        for (int i = 0; i < nodes.size(); i++) {
            Node node = (Node) nodes.elementAt(i);
            if (node.getDepth() == 0) {
                return node;
            }
        }
        return null;
    }

}
