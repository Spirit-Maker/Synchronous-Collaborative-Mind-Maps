package com.touchgraph.graphlayout.interaction;

import Network.Client.Client;
import Network.Server.Server;
import com.touchgraph.graphlayout.Edge;
import com.touchgraph.graphlayout.GLPanel;
import com.touchgraph.graphlayout.Node;
import com.touchgraph.graphlayout.TGException;
import com.touchgraph.graphlayout.TGPanel;
import java.awt.Color;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JColorChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

public class GLNavigateUI extends TGUserInterface {

    GLPanel glPanel;
    TGPanel tgPanel;

    GLNavigateMouseListener ml;

    TGAbstractDragUI hvDragUI;

    DragNodeUI dragNodeUI;
    PopupMenu nodePopup;
    PopupMenu edgePopup;
    Node popupNode;
    Edge popupEdge;

    JTextField text;
    Node textNode;

    public GLNavigateUI(GLPanel glp) {
        glPanel = glp;
        tgPanel = glPanel.getTGPanel();

        dragNodeUI = new DragNodeUI(tgPanel);
        hvDragUI = glPanel.getHVScroll().getHVDragUI();
        ml = new GLNavigateMouseListener();
        setUpNodePopup(glp);
        setUpEdgePopup(glp);

        text = new JTextField();
        text.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (textNode.getType() == 2) {
                        int x = JOptionPane.showConfirmDialog(tgPanel, "Are you Sure to change Main Topic");
                        if (x == 0) {
                            int y = tgPanel.getMF().getTabbedPane().getSelectedIndex();
                            tgPanel.getMF().getTabbedPane().setTitleAt(y, text.getText());
                            textNode.setLabel(text.getText());
                            tgPanel.getMF().updateTree(null);
                        } else {
                            return;
                        }
                    } else {
                        textNode.setLabel(text.getText());
                        tgPanel.getMF().updateTree(null);
                    }
                    if (Server.isRunning() && Server.getGlPanel() != null) {
                        if (Server.getGlPanel().getTGPanel() == tgPanel) {
                            Server.updateServerToClientNodeText(textNode);
                        }
                    }
                    if (Client.isConnected() && Client.getGLPanel() != null) {
                        if (Client.getGLPanel().getTGPanel() == tgPanel) {
                            Client.updateClientToServerNodeText(textNode);
                        }
                    }
                    removeText();
                }
                if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    Document doc = text.getDocument();
                    int pos = doc.getLength() - 1;
                    try {
                        doc.remove(pos, 1);
                    } catch (BadLocationException ex) {
//                        Logger.getLogger(GLNavigateUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                e.consume();
            }
        });
    }

    @Override
    public void activate() {
        tgPanel.addMouseListener(ml);
    }

    @Override
    public void deactivate() {
        tgPanel.removeMouseListener(ml);
    }

    class GLNavigateKeyListener extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                updateTextField();
            }
        }
    }

    class GLNavigateMouseListener extends MouseAdapter {

        public void mousePressed(MouseEvent e) {
            removeText();
            Node mouseOverN = tgPanel.getMouseOverN();

            if (e.getModifiers() == MouseEvent.BUTTON1_MASK) {
                if (mouseOverN == null) {
                    hvDragUI.activate(e);
                } else {
                    dragNodeUI.activate(e);
                }
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            //System.out.println("Hello");
            if (e.getClickCount() == 2) {
                updateTextField();
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.isPopupTrigger()) {
                popupNode = tgPanel.getMouseOverN();
                popupEdge = tgPanel.getMouseOverE();
                if (popupNode != null) {
                    tgPanel.setMaintainMouseOver(true);
//									nodePopup.show(e.getComponent(), e.getX(), e.getY());
                    nodePopup.show(tgPanel, e.getX(), e.getY());
                } else if (popupEdge != null) {
                    tgPanel.setMaintainMouseOver(true);
//									edgePopup.show(e.getComponent(), e.getX(), e.getY());
                    edgePopup.show(tgPanel, e.getX(), e.getY());
                } else {
//									glPanel.glPopup.show(e.getComponent(), e.getX(), e.getY());
//                    glPanel.glPopup.show(tgPanel, e.getX(), e.getY());
                }
            } else {
                tgPanel.setMaintainMouseOver(false);
            }
        }

    }

    private void updateTextField() {
        removeText();
        textNode = tgPanel.getMouseOverN();
        if (textNode != null) {
            Point p = textNode.getRelLocation();
            text.setBounds(p.x - (textNode.getWidth() / 2) + 5, p.y - 15 + 5, textNode.getWidth() - 15, 25);
            tgPanel.add(text);
            text.requestFocus();
            tgPanel.revalidate();
            tgPanel.repaint();
        }
    }

    private void removeText() {
        tgPanel.remove(text);
        text.setText(null);
        tgPanel.repaint();
    }

    private void setUpNodePopup(GLPanel glp) {
        nodePopup = new PopupMenu();
        glp.add(nodePopup);
        MenuItem menuItem;

        menuItem = new MenuItem("Edit Text");
        ActionListener EditTextAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (popupNode != null) {
                    updateTextField();
                }

                tgPanel.setMaintainMouseOver(false);
                tgPanel.setMouseOverN(null);
                tgPanel.repaint();
            }
        };

        menuItem.addActionListener(EditTextAction);
        nodePopup.add(menuItem);

        menuItem = new MenuItem("Add Child");
        ActionListener AddChildAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (popupNode != null) {
                    try {
                        Node addNode = tgPanel.addNode(1);
                        if (addNode.getType() != 2) {
                            tgPanel.addEdge(popupNode, addNode, Edge.DEFAULT_LENGTH);
                            tgPanel.addNode(addNode);
                            tgPanel.getMF().updateTree(null);

                            if (Server.isRunning() && Server.isMindMapAvailable()) {
                                if (Server.getGlPanel().getTGPanel() == tgPanel) {
                                    Server.addServerToClientChildNode(popupNode, addNode);
                                }
                            }
                            if (Client.isConnected() && Client.getGLPanel() != null) {
                                if (Client.getGLPanel().getTGPanel() == tgPanel) {
                                    Client.addClientToServerChildNode(popupNode, addNode);
                                }
                            }
                        }

                    } catch (TGException ex) {
                        Logger.getLogger(TGAddNodeUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                // JDK11 Change .. because of MenuBecomesInvisible
                tgPanel.setMaintainMouseOver(false);
                tgPanel.setMouseOverN(null);
                tgPanel.repaint();
                // JDK11 Change .. because of MenuBecomesInvisible
            }
        };

        menuItem.addActionListener(AddChildAction);
        nodePopup.add(menuItem);

        menuItem = new MenuItem("Delete Node");
        ActionListener deleteAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (popupNode != null) {
                    if (popupNode.getType() == 2) {
                        boolean removeTab = tgPanel.getMF().removeTab();
                        tgPanel.getMF().updateTree(null);
                        if (!removeTab) {
                            return;
                        }
                    }
                    tgPanel.deleteNode(popupNode);
                    tgPanel.getMF().updateTree(null);

                    if (Server.isRunning() && Server.getGlPanel() != null) {
                        if (Server.getGlPanel().getTGPanel() == tgPanel) {
                            Server.deleteServerToClientNode(popupNode);
                        }
                    }
                    if (Client.isConnected() && Client.getGLPanel() != null) {
                        if (Client.getGLPanel().getTGPanel() == tgPanel) {
                            Client.deleteClientToServerNode(popupNode);
                        }
                    }
                }
                // JDK11 Change .. because of MenuBecomesInvisible
                tgPanel.setMaintainMouseOver(false);
                tgPanel.setMouseOverN(null);
                tgPanel.repaint();
                // JDK11 Change .. because of MenuBecomesInvisible
            }
        };

        menuItem.addActionListener(deleteAction);
        nodePopup.add(menuItem);

        menuItem = new MenuItem("Center Node");
        ActionListener centerAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (popupNode != null) {
                    glPanel.getHVScroll().slowScrollToCenter(popupNode);
                }

                tgPanel.setMaintainMouseOver(false);
                tgPanel.setMouseOverN(null);
                tgPanel.repaint();

            }
        };
        menuItem.addActionListener(centerAction);
        nodePopup.add(menuItem);

        menuItem = new MenuItem("Change Color");
        ActionListener ColorAction;
        ColorAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (popupNode != null) {
                    Color colorChoosed = JColorChooser.showDialog(glPanel, "Select Color", null);
                    if (popupNode.getFixed()) {
                        if (popupNode.getType() == 2) {
                            popupNode.setBACK_FIXED_MAIN_COLOR(colorChoosed);
                        } else {
                            popupNode.setNodeBackFixedColor(colorChoosed);
                        }
                    } else {
                        popupNode.setBackColor(colorChoosed);
                    }
                    if (Server.isRunning() && Server.getGlPanel() != null) {
                        if (Server.getGlPanel().getTGPanel() == tgPanel) {
                            Server.updateServerToClientNodeColor(popupNode);
                        }
                    }
                    if (Client.isConnected() && Client.getGLPanel() != null) {
                        if (Client.getGLPanel().getTGPanel() == tgPanel) {
                            Client.updateClientToServerNodeColor(popupNode);
                        }
                    }
                }

                tgPanel.setMaintainMouseOver(false);
                tgPanel.setMouseOverN(null);
                tgPanel.repaint();

            }
        };

        menuItem.addActionListener(ColorAction);
        nodePopup.add(menuItem);

        menuItem = new MenuItem("Change Text Color");
        ActionListener TextColorAction;
        TextColorAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (popupNode != null) {
                    Color colorChoosed = JColorChooser.showDialog(glPanel, "Select Color", null);
                    popupNode.setTextColor(colorChoosed);

                }

                tgPanel.setMaintainMouseOver(false);
                tgPanel.setMouseOverN(null);
                tgPanel.repaint();

            }
        };

        menuItem.addActionListener(TextColorAction);
        nodePopup.add(menuItem);

//        menuItem = new MenuItem("Expand Node");
//        ActionListener expandAction = new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                if (popupNode != null) {
//                    tgPanel.expandNode(popupNode);
//                }
//
//                tgPanel.setMaintainMouseOver(false);
//                tgPanel.setMouseOverN(null);
//                tgPanel.repaint();
//
//            }
//        };
//
//        menuItem.addActionListener(expandAction);
//        nodePopup.add(menuItem);
//
//        menuItem = new MenuItem("Collapse Node");
//        ActionListener collapseAction = new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                if (popupNode != null) {
//                    tgPanel.collapseNode(popupNode);
//                }
//                tgPanel.setMaintainMouseOver(false);
//                tgPanel.setMouseOverN(null);
//                tgPanel.repaint();
//            }
//        };
//
//        menuItem.addActionListener(collapseAction);
//        nodePopup.add(menuItem);

        /*       nodePopup.addPopupMenuListener(new PopupMenuListener() {
         public void popupMenuCanceled(PopupMenuEvent e) {}
         public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
         tgPanel.setMaintainMouseOver(false);
         tgPanel.setMouseOverN(null);
         tgPanel.repaint();
         }
         public void popupMenuWillBecomeVisible(PopupMenuEvent e) {}
         });*/
    }

    private void setUpEdgePopup(GLPanel glp) {
        edgePopup = new PopupMenu();
        glp.add(edgePopup);
        MenuItem menuItem;

        menuItem = new MenuItem("Change Color");
        ActionListener ChangeColorAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (popupEdge != null) {
                    Color colorChoosed = JColorChooser.showDialog(glPanel, "Select Color", null);
                    popupEdge.setColor(colorChoosed);
                }
// JDK11 Change .. because of MenuBecomesInvisible
                tgPanel.setMaintainMouseOver(false);
                tgPanel.setMouseOverN(null);
                tgPanel.repaint();
// JDK11 Change .. because of MenuBecomesInvisible
            }
        };

        menuItem.addActionListener(ChangeColorAction);
        edgePopup.add(menuItem);

//        menuItem = new MenuItem("Delete Edge");
//        ActionListener deleteAction = new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                if (popupEdge != null) {
//                    tgPanel.deleteEdge(popupEdge);
//                }
//// JDK11 Change .. because of MenuBecomesInvisible
//                tgPanel.setMaintainMouseOver(false);
//                tgPanel.setMouseOverN(null);
//                tgPanel.repaint();
//// JDK11 Change .. because of MenuBecomesInvisible
//            }
//        };
//
//        menuItem.addActionListener(deleteAction);
//        edgePopup.add(menuItem);

        /*        edgePopup.addPopupMenuListener(new PopupMenuListener() {
         public void popupMenuCanceled(PopupMenuEvent e) {}
         public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
         tgPanel.setMaintainMouseOver(false);
         tgPanel.setMouseOverE(null);
         tgPanel.repaint();
         }
         public void popupMenuWillBecomeVisible(PopupMenuEvent e) {}
         });*/
    }

} // end com.touchgraph.graphlayout.interaction.GLNavigateUI
