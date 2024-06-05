package com.touchgraph.graphlayout.interaction;

import com.touchgraph.graphlayout.TGPanel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public abstract class TGAbstractClickUI extends TGSelfDeactivatingUI {

    private ACUIMouseListener ml;
    private TGPanel tgPanel;

    public TGAbstractClickUI() {
        tgPanel = null;
        ml = null;
    }

    public TGAbstractClickUI(TGPanel tgp) {

        tgPanel = tgp;
        ml = new ACUIMouseListener();
    }

    public final void activate() {
        if (tgPanel != null && ml != null) {
            tgPanel.addMouseListener(ml);
        }
    }

    public final void activate(MouseEvent e) {
        mouseClicked(e);
    }

    public final void deactivate() {
        if (tgPanel != null && ml != null) {
            tgPanel.removeMouseListener(ml);
        }
        super.deactivate();
    }

    public abstract void mouseClicked(MouseEvent e);

    private class ACUIMouseListener extends MouseAdapter {

        public void mouseClicked(MouseEvent e) {
            TGAbstractClickUI.this.mouseClicked(e);
            if (selfDeactivate) {
                deactivate();
            }
        }
    }

} // end com.touchgraph.graphlayout.interaction.TGAbstractClickUI
