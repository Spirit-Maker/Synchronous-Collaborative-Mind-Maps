package com.touchgraph.graphlayout.graphelements;

import com.touchgraph.graphlayout.Node;

public abstract class TGForEachNodePair {

    public void beforeInnerLoop(Node n1) {
    }

    ;

    public void afterInnerLoop(Node n1) {
    }

    ;

    public abstract void forEachNodePair(Node n1, Node n2);

}
