/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mindmap.project;

import com.touchgraph.graphlayout.GLPanel;
import com.touchgraph.graphlayout.Node;
import com.touchgraph.graphlayout.TGException;
import com.touchgraph.graphlayout.TGPanel;
import com.touchgraph.graphlayout.interaction.TGUIManager;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Zahoor Ali
 */
public class EditorPanel extends javax.swing.JPanel {

    protected GLPanel GLP;
    protected TGPanel TGP;
    protected TGUIManager tgUIManager;
    protected MainFrame MF;

    /**
     * Creates new form EditorPanel
     */
    public EditorPanel(Dimension minimumSize, Dimension maximumSize, MainFrame m) {
        initComponents();
        GLP = new GLPanel();
        TGP = GLP.getTGPanel();
        tgUIManager = GLP.getTgUIManager();
        this.setGLPanel(minimumSize, maximumSize);
        MF = m;
        TGP.setMF(m);
    }

    public MainFrame getMF() {
        return MF;
    }

    public void setMF(MainFrame MF) {
        this.MF = MF;
        TGP.setMF(this.MF);
    }

    public GLPanel getGLPanel() {
        return GLP;
    }

    public TGPanel getTGPanel() {
        return TGP;
    }

    public void setTGP(TGPanel TGP) {
        this.TGP = TGP;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    protected final void setGLPanel(Dimension minimumSize, Dimension maximumSize) {
        this.setMinMaxSize(minimumSize, maximumSize);
        this.setLayout(new BorderLayout());
        this.add(GLP, BorderLayout.CENTER);
        this.validate();
        this.repaint();
    }

    public TGUIManager getTGUIManager() {
        return tgUIManager;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    private void setMinMaxSize(Dimension minimumSize, Dimension maximumSize) {

        GLP.setMinimumSize(minimumSize);   // prefered size setting
        GLP.setMaximumSize(maximumSize);
    }

    public Node addMainNode(String name) {
        Node n = null;
        try {
            if (name.contains("(+)")) {
                name = name.substring(0, name.length() - 3);
            }
            n = TGP.addNode(name, 2);
            TGP.addNode(n);
            //GLP.getHVScroll().slowScrollToCenter(n);
        } catch (TGException ex) {
            Logger.getLogger(EditorPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return n;
    }
}
