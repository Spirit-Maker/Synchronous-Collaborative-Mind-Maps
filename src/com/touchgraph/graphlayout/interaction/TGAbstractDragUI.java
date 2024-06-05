package com.touchgraph.graphlayout.interaction;

import com.touchgraph.graphlayout.TGPanel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public abstract class TGAbstractDragUI extends TGSelfDeactivatingUI {

    public TGPanel tgPanel;

    private ADUIMouseListener ml;
    private ADUIMouseMotionListener mml;
    public boolean mouseWasDragged;

    public TGAbstractDragUI(TGPanel tgp) {
        tgPanel = tgp;
        ml = new ADUIMouseListener();
        mml = new ADUIMouseMotionListener();
    }

    public final void activate() {
        preActivate();
        tgPanel.addMouseListener(ml);
        tgPanel.addMouseMotionListener(mml);
        mouseWasDragged = false;
    }

    public final void activate(MouseEvent e) {
        activate();
        mousePressed(e);
    }

    public final void deactivate() {
        preDeactivate();
        tgPanel.removeMouseListener(ml);
        tgPanel.removeMouseMotionListener(mml);
        super.deactivate();
    }

    public abstract void preActivate();

    public abstract void preDeactivate();

    public abstract void mousePressed(MouseEvent e);

    public abstract void mouseDragged(MouseEvent e);

    public abstract void mouseReleased(MouseEvent e);

    private class ADUIMouseListener extends MouseAdapter {

        public void mousePressed(MouseEvent e) {
            TGAbstractDragUI.this.mousePressed(e);
        }

        public void mouseReleased(MouseEvent e) {
            TGAbstractDragUI.this.mouseReleased(e);
            if (selfDeactivate) {
                deactivate();
            }
        }
    }

    private class ADUIMouseMotionListener extends MouseMotionAdapter {

        public void mouseDragged(MouseEvent e) {
            mouseWasDragged = true;
            TGAbstractDragUI.this.mouseDragged(e);
        }
    }

}
