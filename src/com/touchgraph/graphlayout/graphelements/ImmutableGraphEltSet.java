package com.touchgraph.graphlayout.graphelements;

import com.touchgraph.graphlayout.Edge;
import com.touchgraph.graphlayout.Node;

public interface ImmutableGraphEltSet {

    public void forAllNodePairs(TGForEachNodePair fenp);

    public int nodeCount();

    public int edgeCount();

    public Node findNode(String id);

    public Edge findEdge(Node from, Node to);

    public void forAllNodes(TGForEachNode fen);

    public void forAllEdges(TGForEachEdge fee);

}
