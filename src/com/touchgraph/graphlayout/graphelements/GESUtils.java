package com.touchgraph.graphlayout.graphelements;

import com.touchgraph.graphlayout.Edge;
import com.touchgraph.graphlayout.Node;
import java.util.Hashtable;
import java.util.Vector;

public class GESUtils {

    public static Hashtable calculateDistances(GraphEltSet ges, Node focusNode, int radius,
            int maxAddEdgeCount, int maxExpandEdgeCount,
            boolean unidirectional) {

        Hashtable distHash = new Hashtable();
        distHash.put(focusNode, 0);

        TGNodeQueue nodeQ = new TGNodeQueue();
        nodeQ.push(focusNode);

        while (!nodeQ.isEmpty()) {
            Node n = nodeQ.pop();
            int currDist = ((Integer) distHash.get(n)).intValue();

            if (currDist >= radius) {
                break;
            }

            for (int i = 0; i < n.edgeCount(); i++) {
                Edge e = n.edgeAt(i);
                if (n != n.edgeAt(i).getFrom() && unidirectional) {
                    continue;
                }
                Node adjNode = e.getOtherEndpt(n);
                if (ges.contains(e) && !distHash.containsKey(adjNode) && adjNode.edgeCount() <= maxAddEdgeCount) {
                    if (adjNode.edgeCount() <= maxExpandEdgeCount) {
                        nodeQ.push(adjNode);
                    }
                    distHash.put(adjNode, Integer.valueOf(currDist + 1));
                }
            }
        }
        return distHash;
    }

    public static Hashtable calculateDistances(GraphEltSet ges, Node focusNode, int radius) {
        return calculateDistances(ges, focusNode, radius, 1000, 1000, false);
    }

    public static Hashtable getLargestConnectedSubgraph(GraphEltSet ges) {
        int nodeCount = ges.nodeCount();
        if (nodeCount == 0) {
            return null;
        }

        Vector subgraphVector = new Vector();
        for (int i = 0; i < nodeCount; i++) {
            Node n = ges.nodeAt(i);
            boolean skipNode = false;
            for (int j = 0; j < subgraphVector.size(); j++) {
                if (((Hashtable) subgraphVector.elementAt(j)).contains(n)) {
                    skipNode = true;
                }
            }
//					Collection subgraph = calculateDistances(ges,n,1000).keySet();
            Hashtable subgraph = calculateDistances(ges, n, 1000);
            if (subgraph.size() > nodeCount / 2) {
                return subgraph; //We are done
            }
            if (!skipNode) {
                subgraphVector.addElement(subgraph);
            }

        }

        int maxSize = 0;
        int maxIndex = 0;
        for (int j = 0; j < subgraphVector.size(); j++) {
            if (((Hashtable) subgraphVector.elementAt(j)).size() > maxSize) {
                maxSize = ((Hashtable) subgraphVector.elementAt(j)).size();
                maxIndex = j;
            }
        }

        return (Hashtable) subgraphVector.elementAt(maxIndex);
    }

} // end com.touchgraph.graphlayout.graphelements.GraphEltSet
