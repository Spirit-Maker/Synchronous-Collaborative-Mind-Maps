package com.touchgraph.graphlayout.interaction;

import java.util.Vector;

public class TGUIManager {

    Vector userInterfaces;

    public TGUIManager() {
        userInterfaces = new Vector();
    }

    class NamedUI {

        TGUserInterface ui;
        String name;

        NamedUI(TGUserInterface ui, String n) {
            this.ui = ui;
            name = n;
        }

        public TGUserInterface getUI() {
            return this.ui;
        }
    }

    public Object getTGUserInterface(String name) {
        for (int i = 0; i < userInterfaces.size(); i++) {
            if (((NamedUI) userInterfaces.elementAt(i)).name.equals(name)) {
                return userInterfaces.elementAt(i);
            }
        }
        return null;
    }

    public Object getTGUserInterfaceObject(String name) {
        for (int i = 0; i < userInterfaces.size(); i++) {
            if (((NamedUI) userInterfaces.elementAt(i)).name.equals(name)) {
                NamedUI u = (NamedUI) userInterfaces.elementAt(i);
                return u.getUI();
            }
        }
        return null;
    }

    public void addUI(TGUserInterface ui, String name) {
        userInterfaces.addElement(new NamedUI(ui, name));
    }

    public void addUI(TGUserInterface ui) {
        addUI(ui, null);
    }

    public void removeUI(String name) {
        for (int i = 0; i < userInterfaces.size(); i++) {
            if (((NamedUI) userInterfaces.elementAt(i)).name.equals(name)) {
                deactivate(name);
                userInterfaces.removeElementAt(i);
            }
        }

    }

    public void removeUI(TGUserInterface ui) {
        for (int i = 0; i < userInterfaces.size(); i++) {
            if (((NamedUI) userInterfaces.elementAt(i)).ui == ui) {
                deactivate(ui);
                userInterfaces.removeElementAt(i);
            }
        }

    }

    public void activate(String name) {
        for (int i = 0; i < userInterfaces.size(); i++) {
            NamedUI namedInterf = (NamedUI) userInterfaces.elementAt(i);
            TGUserInterface ui = namedInterf.ui;
            if (((NamedUI) userInterfaces.elementAt(i)).name.equals(name)) {
                ui.activate();
            } else {
                ui.deactivate();
            }
        }
    }

    public void activate(TGUserInterface ui) {
        for (int i = 0; i < userInterfaces.size(); i++) {
            if (((NamedUI) userInterfaces.elementAt(i)).ui == ui) {
                ui.activate();
            } else {
                ui.deactivate();
            }
        }
    }

    public void deactivate(String name) {
        for (int i = 0; i < userInterfaces.size(); i++) {
            NamedUI namedInterf = (NamedUI) userInterfaces.elementAt(i);
            TGUserInterface ui = namedInterf.ui;
            if (((NamedUI) userInterfaces.elementAt(i)).name.equals(name)) {
                ui.deactivate();
            }
        }
    }

    public void deactivate(TGUserInterface ui) {
        for (int i = 0; i < userInterfaces.size(); i++) {
            if (((NamedUI) userInterfaces.elementAt(i)).ui == ui) {
                ui.deactivate();
            }
        }
    }
}
