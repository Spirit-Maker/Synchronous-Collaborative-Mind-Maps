package com.touchgraph.graphlayout;

import java.util.Vector;

public class TGLensSet {

    Vector lenses = new Vector();

    public void addLens(TGAbstractLens l) {
        lenses.addElement(l);
    }

    public void applyLens(TGPoint2D p) {
        if (lenses.isEmpty()) {
            return;
        }
        for (int i = 0; i < lenses.size(); i++) {
            ((TGAbstractLens) lenses.elementAt(i)).applyLens(p);
        }
    }

    public TGPoint2D convDrawToReal(TGPoint2D p) {
        TGPoint2D newp = new TGPoint2D(p);
        undoLens(newp);
        return newp;
    }

    /**
     * Convert real position to draw position.
     */
    public TGPoint2D convDrawToReal(double x, double y) {
        TGPoint2D newp = new TGPoint2D(x, y);
        undoLens(newp);
        return newp;
    }

    public TGPoint2D convRealToDraw(TGPoint2D p) {
        TGPoint2D newp = new TGPoint2D(p);
        applyLens(newp);
        return newp;
    }

    public TGPoint2D convRealToDraw(double x, double y) {
        TGPoint2D newp = new TGPoint2D(x, y);
        applyLens(newp);
        return newp;
    }

    public void undoLens(TGPoint2D p) {
        if (lenses.isEmpty()) {
            return;
        }
        //else
        for (int i = lenses.size() - 1; i >= 0; i--) {
            ((TGAbstractLens) lenses.elementAt(i)).undoLens(p);
        }
    }

}
