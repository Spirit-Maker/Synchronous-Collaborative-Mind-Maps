package com.touchgraph.graphlayout;

import java.util.EventListener;

public interface GraphListener extends EventListener {

    void graphMoved();

    void graphReset();

}
