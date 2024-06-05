package com.touchgraph.graphlayout;

import java.awt.Graphics;
import java.util.EventListener;

public interface TGPaintListener extends EventListener {

    void paintAfterEdges(Graphics g);

    void paintFirst(Graphics g);

    void paintLast(Graphics g);

}
