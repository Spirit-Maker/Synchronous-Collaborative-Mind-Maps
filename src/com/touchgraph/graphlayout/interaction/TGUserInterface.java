package com.touchgraph.graphlayout.interaction;

public abstract class TGUserInterface {

    private TGUserInterface parentUI;

    public abstract void activate();

    boolean active;

    public boolean isActive() {
        return active;
    }

    public void activate(TGUserInterface parent) {
        parentUI = parent;
        parentUI.deactivate();
        activate();
    }

    public void deactivate() {
        if (parentUI != null) {
            parentUI.activate();
        }
        parentUI = null;
    }

}
