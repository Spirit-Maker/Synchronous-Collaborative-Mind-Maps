package com.touchgraph.graphlayout.graphelements;

import com.touchgraph.graphlayout.Node;
import java.util.Vector;

public class TGNodeQueue {

    Vector queue;

    public TGNodeQueue() {
        queue = new Vector();
    }

    public void push(Node n) {
        queue.addElement(n);
    }

    public Node pop() {
        Node n = (Node) queue.elementAt(0);
        queue.removeElementAt(0);
        return n;
    }

    public boolean isEmpty() {
        return queue.size() == 0;
    }

    public boolean contains(Node n) {
        return queue.contains(n);
    }

} // end com.touchgraph.graphlayout.graphelements.TGNodeQueue
