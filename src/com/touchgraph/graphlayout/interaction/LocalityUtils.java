package com.touchgraph.graphlayout.interaction;

import com.touchgraph.graphlayout.Node;
import com.touchgraph.graphlayout.TGException;
import com.touchgraph.graphlayout.TGPanel;
import com.touchgraph.graphlayout.graphelements.GESUtils;
import com.touchgraph.graphlayout.graphelements.Locality;
import com.touchgraph.graphlayout.graphelements.TGForEachNode;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class LocalityUtils {

    public static final int INFINITE_LOCALITY_RADIUS = Integer.MAX_VALUE;
    boolean fastFinishShift = false; // If finish fast is true, quickly wrap up animation
    Locality locality;

    ShiftLocaleThread shiftLocaleThread;
    TGPanel tgPanel;

    public LocalityUtils(Locality loc, TGPanel tgp) {
        locality = loc;
        tgPanel = tgp;
    }

    public synchronized void addAllGraphElts() throws TGException {
        locality.addAll();
    }

    private synchronized void addNearNodes(Hashtable distHash, int radius) throws TGException {
        for (int r = 0; r < radius + 1; r++) {
            Enumeration localNodes = distHash.keys();
            while (localNodes.hasMoreElements()) {
                Node n = (Node) localNodes.nextElement();
                if (!locality.contains(n) && ((Integer) distHash.get(n)).intValue() <= r) {
                    n.massfade = 1;
                    n.justMadeLocal = true;
                    locality.addNodeWithEdges(n);
                    if (!fastFinishShift) {
                        try {
                            if (radius == 1) {
                                Thread.currentThread().sleep(50);
                            } else {
                                Thread.currentThread().sleep(50);
                            }
                        } catch (InterruptedException ex) {
                        }
                    }
                }
            }
        }
    }

    public synchronized void collapseNode(final Node collapseNode) {
        if (collapseNode == null) {
            return;
        }
        new Thread() {
            public void run() {
                synchronized (LocalityUtils.this) {
                    if (!locality.getCompleteEltSet().contains(collapseNode)) {
                        return;
                    }

                    locality.removeNode(collapseNode);

                    Hashtable subgraph = GESUtils.getLargestConnectedSubgraph(locality);
                    markDistantNodes(subgraph);
                    try {
                        locality.addNodeWithEdges(collapseNode); // Add the collapsed node back in.
                    } catch (TGException tge) {
                        tge.printStackTrace();
                    }
                    tgPanel.repaint();
                    tgPanel.resetDamper();
                    try {
                        Thread.currentThread().sleep(600);
                    } catch (InterruptedException ex) {
                    }
                    removeMarkedNodes();

                    tgPanel.resetDamper();
                }
            }
        }.start();
    }

    public void expandNode(final Node n) {
        new Thread() {
            public void run() {
                synchronized (LocalityUtils.this) {
                    if (!locality.getCompleteEltSet().contains(n)) {
                        return;
                    }
                    tgPanel.stopDamper();
                    for (int i = 0; i < n.edgeCount(); i++) {
                        Node newNode = n.edgeAt(i).getOtherEndpt(n);
                        if (!locality.contains(newNode)) {
                            newNode.justMadeLocal = true;
                            try {
                                locality.addNodeWithEdges(newNode);
                                Thread.currentThread().sleep(50);
                            } catch (TGException tge) {
                                System.err.println("TGException: " + tge.getMessage());
                            } catch (InterruptedException ex) {
                            }
                        } else if (!locality.contains(n.edgeAt(i))) {
                            locality.addEdge(n.edgeAt(i));
                        }
                    }
                    try {
                        Thread.currentThread().sleep(200);
                    } catch (InterruptedException ex) {
                    }
                    unmarkNewAdditions();
                    tgPanel.resetDamper();
                }
            }
        }.start();
    }

    public void fastFinishAnimation() {
        fastFinishShift = true;
    }

    private synchronized boolean markDistantNodes(final Hashtable subgraph) {//Collection subgraph) {
        final boolean[] someNodeWasMarked = new boolean[1];
        someNodeWasMarked[0] = false;
//        Boolean x;
        TGForEachNode fen = new TGForEachNode() {
            public void forEachNode(Node n) {
//							if(!subgraph.contains(n)) {
                if (!subgraph.containsKey(n)) {
                    n.markedForRemoval = true;
                    someNodeWasMarked[0] = true;
                }
            }
        };

        locality.forAllNodes(fen);
        return someNodeWasMarked[0];
    }

    private synchronized void removeMarkedNodes() {
        final Vector nodesToRemove = new Vector();

        TGForEachNode fen = new TGForEachNode() {
            public void forEachNode(Node n) {
                if (n.markedForRemoval) {
                    nodesToRemove.addElement(n);
                    n.markedForRemoval = false;
                    n.massfade = 1;
                }
            }
        };
        synchronized (locality) {
            locality.forAllNodes(fen);
            locality.removeNodes(nodesToRemove);
        }
    }

    public void setLocale(Node n, final int radius, final int maxAddEdgeCount, final int maxExpandEdgeCount, final boolean unidirectional) throws TGException {
        if (n == null || radius < 0) {
            return;
        }
        if (shiftLocaleThread != null && shiftLocaleThread.isAlive()) {
            fastFinishShift = true; //This should cause last locale shift to finish quickly
            while (shiftLocaleThread.isAlive()) {
                try {
                    Thread.currentThread().sleep(100);
                } catch (InterruptedException ex) {
                }
            }
        }
        if (radius == INFINITE_LOCALITY_RADIUS || n == null) {
            addAllGraphElts();
            tgPanel.resetDamper();
            return;
        }

        fastFinishShift = false;
        shiftLocaleThread = new ShiftLocaleThread(n, radius, maxAddEdgeCount, maxExpandEdgeCount, unidirectional);
    }

    public void setLocale(Node n, final int radius) throws TGException {
        setLocale(n, radius, 1000, 1000, false);
    }

    private synchronized void unmarkNewAdditions() {
        TGForEachNode fen = new TGForEachNode() {
            public void forEachNode(Node n) {
                n.justMadeLocal = false;
            }
        };
        locality.forAllNodes(fen);
    }

    class ShiftLocaleThread extends Thread {

        Hashtable distHash;
        Node focusNode;
        int radius;
        int maxAddEdgeCount;
        int maxExpandEdgeCount;
        boolean unidirectional;

        ShiftLocaleThread(Node n, int r, int maec, int meec, boolean unid) {
            focusNode = n;
            radius = r;
            maxAddEdgeCount = maec;
            maxExpandEdgeCount = meec;
            unidirectional = unid;
            start();
        }

        public void run() {
            synchronized (LocalityUtils.this) {
                if (!locality.getCompleteEltSet().contains(focusNode)) {
                    return;
                }
                tgPanel.stopDamper();
                distHash = GESUtils.calculateDistances(
                        locality.getCompleteEltSet(), focusNode, radius, maxAddEdgeCount, maxExpandEdgeCount, unidirectional);
                try {
                    if (radius == 1) {
                        addNearNodes(distHash, radius);
                        for (int i = 0; i < 4 && !fastFinishShift; i++) {
                            Thread.currentThread().sleep(100);
                        }
                        unmarkNewAdditions();
                        for (int i = 0; i < 4 && !fastFinishShift; i++) {
                            Thread.currentThread().sleep(100);
                        }
                    }
                    if (markDistantNodes(distHash)) {//.keySet())) {// markDistantNodes will use a Collection..
                        for (int i = 0; i < 8 && !fastFinishShift; i++) {
                            Thread.currentThread().sleep(100);
                        }
                    }
                    removeMarkedNodes();
                    for (int i = 0; i < 1 && !fastFinishShift; i++) {
                        if (radius > 1) {
                            Thread.currentThread().sleep(100);
                        }
                    }
                    if (radius != 1) {
                        addNearNodes(distHash, radius);
                        for (int i = 0; i < 4 && !fastFinishShift; i++) {
                            Thread.currentThread().sleep(100);
                        }
                        unmarkNewAdditions();
                    }

                } catch (TGException tge) {
                    System.err.println("TGException: " + tge.getMessage());
                } catch (InterruptedException ex) {
                }
                tgPanel.resetDamper();
            }
        }
    }
}
