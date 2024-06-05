package com.touchgraph.graphlayout;

import com.touchgraph.graphlayout.interaction.GLNavigateUI;
import com.touchgraph.graphlayout.interaction.HVScroll;
import com.touchgraph.graphlayout.interaction.TGAddEdgeUI;
import com.touchgraph.graphlayout.interaction.TGAddImageUI;
import com.touchgraph.graphlayout.interaction.TGAddNodeUI;
import com.touchgraph.graphlayout.interaction.TGUIManager;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.MenuItem;
import java.awt.Panel;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.Scrollbar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.EventListener;
import javax.swing.JColorChooser;

public class GLPanel extends javax.swing.JPanel {

    private Color defaultBackColor = Color.white; //new Color(240, 240, 240);
    private Color defaultBorderBackColor = new Color(0x02, 0x35, 0x81);
    private Color defaultForeColor = new Color((float) 0.95, (float) 0.85, (float) 0.55);
    public PopupMenu glPopup;

    public HVScroll hvScroll;

    protected TGLensSet tgLensSet;
    protected TGPanel tgPanel;
    protected TGUIManager tgUIManager;

    public GLPanel() {
        this.setBackground(defaultBorderBackColor);
        this.setForeground(defaultForeColor);
        tgLensSet = new TGLensSet();
        tgPanel = new TGPanel();
        tgPanel.setBackColor(defaultBackColor);
        hvScroll = new HVScroll(tgPanel, tgLensSet);

        initialize();
    }

    public void addUIs() {
        tgUIManager = new TGUIManager();
        GLNavigateUI navigateUI = new GLNavigateUI(this);
        tgUIManager.addUI(navigateUI, "Navigate");
        TGAddNodeUI addNodeUI = new TGAddNodeUI(this);
        tgUIManager.addUI(addNodeUI, "addNodeUI");
        TGAddImageUI addImageUI = new TGAddImageUI(this);
        tgUIManager.addUI(addImageUI, "addImageUI");
//        TGAddEdgeUI addEdgeUI = new TGAddEdgeUI(this);
//        tgUIManager.addUI(addEdgeUI, "addEdgeUI");
        tgUIManager.activate("Navigate");

        addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            @Override
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                int rot = evt.getWheelRotation();
                int countTab = tgPanel.getMF().getTabbedPane().getTabCount();
                if (rot < 0 && countTab <= tgPanel.getMF().getEPanelsize() && countTab > 0) {  // negetive rotation
                    int tabNo = tgPanel.getMF().getTabbedPane().getSelectedIndex();
                    if (tabNo - 1 >= 0) {
                        tgPanel.getMF().getTabbedPane().setSelectedIndex(tabNo - 1);
                    }
                }
                if (rot > 0 && countTab <= tgPanel.getMF().getEPanelsize() && countTab > 0) {
                    int tabNo = tgPanel.getMF().getTabbedPane().getSelectedIndex();
                    if (tabNo + 1 < tgPanel.getMF().getTabbedPane().getTabCount()) {
                        tgPanel.getMF().getTabbedPane().setSelectedIndex(tabNo + 1);
                    }
                }
            }
        });
    }

    public void buildLens() {
        tgLensSet.addLens(hvScroll.getLens());
        tgLensSet.addLens(tgPanel.getAdjustOriginLens());
    }

    public void buildPanel() {
        final Scrollbar horizontalSB = hvScroll.getHorizontalSB();
        final Scrollbar verticalSB = hvScroll.getVerticalSB();

        setLayout(new BorderLayout());

        Panel scrollPanel = new Panel();                    // Panel as an Editor and adds tgpanel
        scrollPanel.setBackground(defaultBackColor);
        scrollPanel.setForeground(defaultForeColor);
        scrollPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        Panel modeSelectPanel = new Panel();
        modeSelectPanel.setBackground(defaultBackColor);
        modeSelectPanel.setForeground(defaultForeColor);
        modeSelectPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));

        c.fill = GridBagConstraints.BOTH;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 1;
        c.weighty = 1;
        scrollPanel.add(tgPanel, c);

        c.gridx = 1;
        c.gridy = 1;
        c.weightx = 0;
        c.weighty = 0;

        c.gridx = 0;
        c.gridy = 2;

        add(scrollPanel, BorderLayout.CENTER);

        glPopup = new PopupMenu();
        add(glPopup);

        MenuItem menuItem = new MenuItem("Change Background Color");
        ActionListener toggleControlsAction = new ActionListener() {
            boolean controlsVisible = true;

            @Override
            public void actionPerformed(ActionEvent e) {
                Color colorChoosed = JColorChooser.showDialog(null, "Select Color", null);
                defaultBackColor = colorChoosed;
            }
        };
        menuItem.addActionListener(toggleControlsAction);
        glPopup.add(menuItem);
    }

    // ....
    public PopupMenu getGLPopup() {
        return glPopup;
    }

    public HVScroll getHVScroll() {
        return hvScroll;
    }

    public Point getOffset() {
        return hvScroll.getOffset();
    }

    public void setOffset(Point p) {
        hvScroll.setOffset(p);
    }

    public TGPanel getTGPanel() {
        return tgPanel;
    }

    public TGUIManager getTgUIManager() {
        return tgUIManager;
    }

    public void initialize() {
        buildPanel();
        buildLens();
        tgPanel.setLensSet(tgLensSet);
        addUIs();
        setVisible(true);
    }

}
