package com.touchgraph.graphlayout.interaction;

public abstract class TGSelfDeactivatingUI extends TGUserInterface {

    boolean selfDeactivate;

    public TGSelfDeactivatingUI() {
        selfDeactivate = true;
    }

    public void setSelfDeactivate(boolean sd) {
        selfDeactivate = sd;
    }

}
