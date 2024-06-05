package com.touchgraph.graphlayout.graphelements;

import com.touchgraph.graphlayout.Edge;
import com.touchgraph.graphlayout.Node;
import com.touchgraph.graphlayout.TGException;

public class VisibleLocality extends Locality {

    public VisibleLocality(GraphEltSet ges) {
        super(ges);
    }

    public synchronized void addNode(Node node) throws TGException {
        super.addNode(node);
        node.setVisible(true);
    }

    public synchronized void addNodeByForce(Node node) throws TGException {
        super.addNodeByForce(node);
        node.setVisible(true);
    }

    public void addEdge(Edge edge) {
        if (!contains(edge)) {
            super.addEdge(edge);
            edge.from.visibleEdgeCnt++;
            edge.to.visibleEdgeCnt++;
        }
    }

    public boolean removeEdge(Edge edge) {
        boolean removed = super.removeEdge(edge);
        if (removed) {
            edge.setVisible(false);
            edge.from.visibleEdgeCnt--;
            edge.to.visibleEdgeCnt--;
        }
        return removed;
    }

    public boolean removeNode(Node node) {
        boolean removed = super.removeNode(node);
        if (removed) {
            node.setVisible(false);
        }
        return removed;
    }

    public synchronized void removeAll() {
        for (int i = 0; i < nodeCount(); i++) {
            nodeAt(i).setVisible(false);
        }
        for (int i = 0; i < edgeCount(); i++) {
            edgeAt(i).setVisible(false);
        }
        super.removeAll();
    }

    public void updateLocalityFromVisibility() throws TGException {

        TGForEachNode fen = new TGForEachNode() {
            public void forEachNode(Node node) {
                try {
                    if (node.isVisible() && !contains(node)) {
                        addNode(node);
                    } else if (!node.isVisible() && contains(node)) {
                        removeNode(node);
                    }
                } catch (TGException ex) {
                    ex.printStackTrace();
                }
            }
        };
        completeEltSet.forAllNodes(fen);

        TGForEachEdge fee = new TGForEachEdge() {
            public void forEachEdge(Edge edge) {
                if (edge.isVisible() && !contains(edge)) {
                    addEdge(edge);
                } else if (!edge.isVisible() && contains(edge)) {
                    removeEdge(edge);
                }
            }
        };
        completeEltSet.forAllEdges(fee);
    }
}
