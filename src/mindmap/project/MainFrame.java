/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mindmap.project;

import Network.Client.Client;
import Network.Client.ConnectGUI;
import Network.Config;
import Network.ConfigurationGUI;
import Network.Server.Server;
import audio.Sound;
import audio.Speech;
import com.touchgraph.graphlayout.Node;
import com.touchgraph.graphlayout.TGPanel;
import com.touchgraph.graphlayout.graphelements.TGForEachNode;
import com.touchgraph.graphlayout.graphelements.VisibleLocality;
import com.touchgraph.graphlayout.interaction.TGUIManager;
import java.awt.Component;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.util.Enumeration;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

/**
 *
 * @author Shani Bhai
 */
public class MainFrame extends javax.swing.JFrame {

    // personal variables
    protected int selectedTabNo;
    protected TGUIManager tgUIManager;
    Vector<EditorPanel> EPanel;
    Speech speech;
    public boolean serverCreated = false;

    private static ImageIcon iconWhite = new ImageIcon("src/resources/images/JtreeNode.png");

    private static ImageIcon iconBlack = new ImageIcon("black.jpg");

    /**
     * Creates new form MainFrame
     *
     * @param title
     */
    public MainFrame(String title) {
        initComponents();
        this.setTitle(title);
        EPanel = new Vector();
        this.MainPanel.setVisible(false);
        this.HorizontalToolBar.setVisible(false);
        speech = new Speech(null);

        // JTree
        final CustomTreeCellRenderer renderer = new CustomTreeCellRenderer();
        renderer.setRendererIcon(iconWhite);
        this.mindMapTree.setCellRenderer(renderer);
        // For Window type display....
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        SwingUtilities.updateComponentTreeUI(this);
        this.setVisible(true);
    }

    public int getEPanelsize() {
        return EPanel.size();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        HorizontalToolBar = new javax.swing.JToolBar();
        addNodeToolButton = new javax.swing.JButton();
        AddImageToolButton = new javax.swing.JButton();
        MainPanel = new javax.swing.JPanel(){
            /*
            public void paintComponent(Graphics g){
                super.paintComponent(g);
                BufferedImage image = new BufferedImage(this.getWidth(),this.getHeight(),BufferedImage.TYPE_4BYTE_ABGR);
                try{
                    image = ImageIO.read(new File("src/resources/images/MainBackground.jpg"));
                }catch(IOException e){}
                g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
            }*/
        };
        PExplorer = new javax.swing.JPanel();
        JTreeScrollPane = new javax.swing.JScrollPane();
        mindMapTree = new javax.swing.JTree();
        TabbedPane = new javax.swing.JTabbedPane();
        jLabel1 = new javax.swing.JLabel();
        Menubar = new javax.swing.JMenuBar();
        FileMenu = new javax.swing.JMenu();
        FileNewMenuItem = new javax.swing.JMenuItem();
        FileOpenMenuItem = new javax.swing.JMenuItem();
        FileSaveMenuItem = new javax.swing.JMenuItem();
        FIleExitMenuItem = new javax.swing.JMenuItem();
        ViewMenu = new javax.swing.JMenu();
        VibrateDiagramMenuItem = new javax.swing.JMenuItem();
        NetworkkMenu = new javax.swing.JMenu();
        StartStopServerMenuItem = new javax.swing.JMenuItem();
        ShareDiagramMenuItem = new javax.swing.JMenuItem();
        ServerConfugurationMenuItem = new javax.swing.JMenuItem();
        ConnectServerMenuItem = new javax.swing.JMenuItem();
        OverviewMenu = new javax.swing.JMenu();
        OverviewMenuItem = new javax.swing.JMenuItem();
        NodeInformationMenuItem = new javax.swing.JMenuItem();
        SelectSoundCheckMenuItem = new javax.swing.JCheckBoxMenuItem();
        EnableDynamicSOundCheckMenuItem = new javax.swing.JCheckBoxMenuItem();
        DFSSpeechMenuItem = new javax.swing.JMenuItem();
        BFSSpeechMenuItem = new javax.swing.JMenuItem();
        DFSSoundMenuItem = new javax.swing.JMenuItem();
        BFSSoundMenuItem = new javax.swing.JMenuItem();
        HelpMenu = new javax.swing.JMenu();
        AboutMenuItem = new javax.swing.JMenuItem();
        DynamiccSoundMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Synchronous Collaborative Mind Maps");
        setBackground(new java.awt.Color(153, 153, 153));

        HorizontalToolBar.setRollover(true);
        HorizontalToolBar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        addNodeToolButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/SimpleOvalNode.png"))); // NOI18N
        addNodeToolButton.setToolTipText("Add new Node");
        addNodeToolButton.setFocusable(false);
        addNodeToolButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        addNodeToolButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        addNodeToolButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addNodeToolButtonActionPerformed(evt);
            }
        });
        HorizontalToolBar.add(addNodeToolButton);

        AddImageToolButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/AddImageIcon.png"))); // NOI18N
        AddImageToolButton.setToolTipText("Add Image as a Node");
        AddImageToolButton.setFocusable(false);
        AddImageToolButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        AddImageToolButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        AddImageToolButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddImageToolButtonActionPerformed(evt);
            }
        });
        HorizontalToolBar.add(AddImageToolButton);

        MainPanel.setBackground(null);

        mindMapTree.setFocusable(false);
        mindMapTree.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                mindMapTreeMousePressed(evt);
            }
        });
        JTreeScrollPane.setViewportView(mindMapTree);

        javax.swing.GroupLayout PExplorerLayout = new javax.swing.GroupLayout(PExplorer);
        PExplorer.setLayout(PExplorerLayout);
        PExplorerLayout.setHorizontalGroup(
            PExplorerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(JTreeScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );
        PExplorerLayout.setVerticalGroup(
            PExplorerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(JTreeScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        TabbedPane.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        TabbedPane.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        TabbedPane.setAutoscrolls(true);
        TabbedPane.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        TabbedPane.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        TabbedPane.setName(""); // NOI18N
        TabbedPane.setOpaque(true);
        TabbedPane.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                TabbedPaneStateChanged(evt);
            }
        });
        TabbedPane.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                TabbedPaneMouseWheelMoved(evt);
            }
        });
        TabbedPane.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TabbedPaneKeyPressed(evt);
            }
        });

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/Logoa.png"))); // NOI18N

        javax.swing.GroupLayout MainPanelLayout = new javax.swing.GroupLayout(MainPanel);
        MainPanel.setLayout(MainPanelLayout);
        MainPanelLayout.setHorizontalGroup(
            MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MainPanelLayout.createSequentialGroup()
                .addGroup(MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(PExplorer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(MainPanelLayout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addComponent(jLabel1)))
                .addComponent(TabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 714, Short.MAX_VALUE))
        );
        MainPanelLayout.setVerticalGroup(
            MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(TabbedPane, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(MainPanelLayout.createSequentialGroup()
                .addComponent(PExplorer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 52, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(40, 40, 40))
        );

        FileMenu.setText("File");

        FileNewMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        FileNewMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/NewFileIcon.png"))); // NOI18N
        FileNewMenuItem.setText("New");
        FileNewMenuItem.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        FileNewMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FileNewMenuItemActionPerformed(evt);
            }
        });
        FileMenu.add(FileNewMenuItem);

        FileOpenMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        FileOpenMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/OpenFileIcon.png"))); // NOI18N
        FileOpenMenuItem.setText("Open");
        FileOpenMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FileOpenMenuItemActionPerformed(evt);
            }
        });
        FileMenu.add(FileOpenMenuItem);

        FileSaveMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        FileSaveMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/SaveFileIcon.png"))); // NOI18N
        FileSaveMenuItem.setText("Save");
        FileSaveMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FileSaveMenuItemActionPerformed(evt);
            }
        });
        FileMenu.add(FileSaveMenuItem);

        FIleExitMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK));
        FIleExitMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/ExitIcon.png"))); // NOI18N
        FIleExitMenuItem.setText("Exit");
        FIleExitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FIleExitMenuItemActionPerformed(evt);
            }
        });
        FileMenu.add(FIleExitMenuItem);

        Menubar.add(FileMenu);

        ViewMenu.setText("View");

        VibrateDiagramMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.ALT_MASK));
        VibrateDiagramMenuItem.setText("Vibrate Diagram");
        VibrateDiagramMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                VibrateDiagramMenuItemActionPerformed(evt);
            }
        });
        ViewMenu.add(VibrateDiagramMenuItem);

        Menubar.add(ViewMenu);

        NetworkkMenu.setText("Network");

        StartStopServerMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/player_play.png"))); // NOI18N
        StartStopServerMenuItem.setText("Start Server");
        StartStopServerMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StartStopServerMenuItemActionPerformed(evt);
            }
        });
        NetworkkMenu.add(StartStopServerMenuItem);

        ShareDiagramMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/share.png"))); // NOI18N
        ShareDiagramMenuItem.setText("Share Diagram");
        ShareDiagramMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ShareDiagramMenuItemActionPerformed(evt);
            }
        });
        NetworkkMenu.add(ShareDiagramMenuItem);

        ServerConfugurationMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/configure.png"))); // NOI18N
        ServerConfugurationMenuItem.setText("Server Conguration");
        ServerConfugurationMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ServerConfugurationMenuItemActionPerformed(evt);
            }
        });
        NetworkkMenu.add(ServerConfugurationMenuItem);

        ConnectServerMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/images/connect_creating.png"))); // NOI18N
        ConnectServerMenuItem.setText("Connect to Server");
        ConnectServerMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ConnectServerMenuItemActionPerformed(evt);
            }
        });
        NetworkkMenu.add(ConnectServerMenuItem);

        Menubar.add(NetworkkMenu);

        OverviewMenu.setText("Audio");

        OverviewMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.ALT_MASK));
        OverviewMenuItem.setText("Overview");
        OverviewMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OverviewMenuItemActionPerformed(evt);
            }
        });
        OverviewMenu.add(OverviewMenuItem);

        NodeInformationMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.ALT_MASK));
        NodeInformationMenuItem.setText("Node Information");
        NodeInformationMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NodeInformationMenuItemActionPerformed(evt);
            }
        });
        OverviewMenu.add(NodeInformationMenuItem);

        SelectSoundCheckMenuItem.setSelected(true);
        SelectSoundCheckMenuItem.setText("Enable Speech");
        SelectSoundCheckMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SelectSoundCheckMenuItemActionPerformed(evt);
            }
        });
        OverviewMenu.add(SelectSoundCheckMenuItem);

        EnableDynamicSOundCheckMenuItem.setSelected(true);
        EnableDynamicSOundCheckMenuItem.setText("Enable Dynamic Sound");
        EnableDynamicSOundCheckMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EnableDynamicSOundCheckMenuItemActionPerformed(evt);
            }
        });
        OverviewMenu.add(EnableDynamicSOundCheckMenuItem);

        DFSSpeechMenuItem.setText("DFS Speech");
        DFSSpeechMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DFSSpeechMenuItemActionPerformed(evt);
            }
        });
        OverviewMenu.add(DFSSpeechMenuItem);

        BFSSpeechMenuItem.setText("BFS Speech");
        BFSSpeechMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BFSSpeechMenuItemActionPerformed(evt);
            }
        });
        OverviewMenu.add(BFSSpeechMenuItem);

        DFSSoundMenuItem.setText("DFS Sound");
        DFSSoundMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DFSSoundMenuItemActionPerformed(evt);
            }
        });
        OverviewMenu.add(DFSSoundMenuItem);

        BFSSoundMenuItem.setText("BFS Sound");
        BFSSoundMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BFSSoundMenuItemActionPerformed(evt);
            }
        });
        OverviewMenu.add(BFSSoundMenuItem);

        Menubar.add(OverviewMenu);

        HelpMenu.setText("Help");

        AboutMenuItem.setText("About");
        AboutMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AboutMenuItemActionPerformed(evt);
            }
        });
        HelpMenu.add(AboutMenuItem);

        DynamiccSoundMenuItem.setText("Dynamic Sound");
        DynamiccSoundMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DynamiccSoundMenuItemActionPerformed(evt);
            }
        });
        HelpMenu.add(DynamiccSoundMenuItem);

        Menubar.add(HelpMenu);

        setJMenuBar(Menubar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 294, Short.MAX_VALUE)
                .addComponent(HorizontalToolBar, javax.swing.GroupLayout.DEFAULT_SIZE, 720, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(MainPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(HorizontalToolBar, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 563, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addGap(33, 33, 33)
                    .addComponent(MainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    // Adding new tab of mindmaps
    public void addNewMindMaps(String name) {

        int tabNo = TabbedPane.getTabCount();
        JScrollPane scrollpane = new JScrollPane();

        scrollpane.setMinimumSize(TabbedPane.getMinimumSize());
        scrollpane.setMaximumSize(TabbedPane.getMaximumSize());

        EditorPanel EP = new EditorPanel(scrollpane.getMinimumSize(), scrollpane.getMaximumSize(), this);
        EPanel.add(EP);
        EP.setMF(this);

        scrollpane.setViewportView(EP);     // add EP to scrollpane.

        this.TabbedPane.addTab("Tab " + (tabNo + 1), scrollpane);
        this.TabbedPane.setSelectedIndex(tabNo);

        if (name == null) {
            name = "Enter Main Title";
        }
        if (!name.isEmpty()) {
            TabbedPane.setTitleAt(tabNo, name);
            EP.addMainNode(name);
        } else {
            TabbedPane.setTitleAt(tabNo, "Enter Main Title");
            EP.addMainNode("Enter Main Title");
        }
    }

    public boolean removeTab() {
        int opTab = TabbedPane.getSelectedIndex();
        if (opTab < EPanel.size() && opTab >= 0) {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete Mind Map.", "Warning", JOptionPane.YES_NO_OPTION);
            if (confirm != 0) {
                return false;
            }
            EPanel.remove(this.getCurrentEPanel());
            TabbedPane.removeTabAt(opTab);

            if (TabbedPane.getTabCount() == 0) {
                this.MainPanel.setVisible(false);
                this.HorizontalToolBar.setVisible(false);
            }
            return true;
        }
        return false;
    }

    public void updateTree(Node node) {
        DefaultTreeModel model = (DefaultTreeModel) this.mindMapTree.getModel();

        if (node == null) {
            Node root = findRoot();
            if (root != null) {
                DefaultMutableTreeNode treeRoot = new DefaultMutableTreeNode(root.getLabel());

                model.setRoot(treeRoot);
                model.reload();
                updateTree(root);
            }
        } else {
            Vector children = node.getChildren();
            for (int i = 0; i < children.size(); i++) {
                Node child = (Node) children.elementAt(i);
                DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
                Enumeration<?> en = root.depthFirstEnumeration();  // Use wildcard for Enumeration

                while (en.hasMoreElements()) {
                    DefaultMutableTreeNode n = (DefaultMutableTreeNode) en.nextElement();
                    if (n.toString().equalsIgnoreCase(node.getLabel())) {
                        DefaultMutableTreeNode ch = new DefaultMutableTreeNode(child.getLabel());
                        model.insertNodeInto(ch, n, i);
                    }
                }

                model.reload();
                updateTree(child);
            }

        }
        if (this.mindMapTree.getRowCount() < 7) {
            for (int i = 0; i < mindMapTree.getRowCount(); i++) {
                this.mindMapTree.expandRow(i);
            }
        }
    }

    public Node findRoot() {
        if (getCurrentEPanel() != null) {
            Vector node = this.getCurrentEPanel().getTGPanel().getVisibleLocality().getNodes();
            Node n;
            for (int i = 0; i < node.size(); i++) {
                n = (Node) node.elementAt(i);
                if (n.getDepth() == 0) {
                    return n;
                }
            }
        }

        return null;
    }

    public EditorPanel getCurrentEPanel() {
        int opTab = TabbedPane.getSelectedIndex();
        if (opTab < EPanel.size() && opTab >= 0) {
            return EPanel.elementAt(opTab);
        }
        return null;
    }

    // System Generated
    private void FileOpenMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FileOpenMenuItemActionPerformed
        // TODO add your handling code here:
        VisibleLocality vloc = null;
        File file = FileChooser(1);
        if (file != null) {
            try (FileInputStream fis = new FileInputStream(file.getAbsolutePath())) {
                ObjectInputStream ois = new ObjectInputStream(fis);
//                Node.MyImage.readImage(ois);
                vloc = (VisibleLocality) ois.readObject();

                if (vloc == null) {
                    return;
                }
//                Node.MyImage.restoreImages(vloc);
                int tabNo = TabbedPane.getTabCount();
                JScrollPane scrollpane = new JScrollPane();

                scrollpane.setMinimumSize(TabbedPane.getMinimumSize());
                scrollpane.setMaximumSize(TabbedPane.getMaximumSize());

                EditorPanel EP = new EditorPanel(scrollpane.getMinimumSize(), scrollpane.getMaximumSize(), this);
                EPanel.add(EP);
                EP.setMF(this);

                scrollpane.setViewportView(EP);

                this.TabbedPane.addTab(vloc.findRoot().getLabel(), scrollpane);
                this.TabbedPane.setSelectedIndex(tabNo);

                TGPanel tgp = EPanel.elementAt(tabNo).getTGPanel();

                tgp.setVisibleLocality(vloc);
                tgp.paint(tgp.getGraphics());

            } catch (FileNotFoundException ex) {
                JOptionPane.showConfirmDialog(this, "File Not Found.\n" + ex.getMessage(), "Error", JOptionPane.PLAIN_MESSAGE);
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showConfirmDialog(this, "IO Error or Class not Found.\n" + ex.getMessage(), "Error", JOptionPane.PLAIN_MESSAGE);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            updateTree(null);
            if (!this.MainPanel.isVisible()) {
                this.MainPanel.setVisible(true);
            }
            if (!this.HorizontalToolBar.isVisible()) {
                this.HorizontalToolBar.setVisible(true);
            }
        } else {
            // file not found
        }
//        this.RedrawSystem();
    }//GEN-LAST:event_FileOpenMenuItemActionPerformed

    private void FIleExitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FIleExitMenuItemActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_FIleExitMenuItemActionPerformed

    private void FileNewMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FileNewMenuItemActionPerformed
        // TODO add your handling code here:
        String name = JOptionPane.showInputDialog(this, "Enter Main Node Statement", "Enter Statement", JOptionPane.OK_CANCEL_OPTION);
        if (name == null) {
            return;
        }
        addNewMindMaps(name);
        updateTree(null);
        if (!this.MainPanel.isVisible()) {
            this.MainPanel.setVisible(true);
        }
        if (!this.HorizontalToolBar.isVisible()) {
            this.HorizontalToolBar.setVisible(true);
        }
    }//GEN-LAST:event_FileNewMenuItemActionPerformed

    private void addNodeToolButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addNodeToolButtonActionPerformed
        // TODO add your handling code here:
        if (this.getCurrentEPanel() != null) {
            EditorPanel panel = this.getCurrentEPanel();
            tgUIManager = panel.getTGUIManager();
            tgUIManager.deactivate("Navigate");
            tgUIManager.activate("addNodeUI");
        }
    }//GEN-LAST:event_addNodeToolButtonActionPerformed

    private void FileSaveMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FileSaveMenuItemActionPerformed
        // TODO add your handling code here:
        if (this.getCurrentEPanel() != null) {

            VisibleLocality vloc = this.getCurrentEPanel().getTGPanel().getVisibleLocality();
            File file = FileChooser(2);
            String filePath;
            if (file.getAbsolutePath().contains(".[a-zA-Z0-9]")) {
                filePath = file.getAbsolutePath();
            } else {
                filePath = file.getAbsolutePath() + ".ser";
            }
            if (file != null) {
                try {
                    try ( //System.out.println(file.getAbsolutePath());
                            FileOutputStream fos = new FileOutputStream(filePath)) {
                        ObjectOutputStream oos = new ObjectOutputStream(fos);
//                        TGForEachNode fen = new TGForEachNode() {
//                            @Override
//                            public void forEachNode(Node n) {
//                                Image img = n.getImage();
//                                if (img != null) {
//                                    Node.MyImage.addImage(img, n.getID());
//                                    n.setImage(null);
//                                }
//                            }
//                        };
//                        vloc.forAllNodes(fen);
//                        Node.MyImage.writeImage(oos);
                        oos.writeObject(vloc);
//                        Node.MyImage.restoreImages(vloc);
                        oos.close();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showConfirmDialog(this, "IO Error.\n" + ex.getMessage(), "Error", JOptionPane.PLAIN_MESSAGE);
                }
            } else {
                System.out.print("error");
            }
        }
    }//GEN-LAST:event_FileSaveMenuItemActionPerformed

    private void VibrateDiagramMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_VibrateDiagramMenuItemActionPerformed
        // TODO add your handling code here:
        if (this.getCurrentEPanel() != null) {
            final EditorPanel panel = this.getCurrentEPanel();
            Thread thread = new Thread() {
                public void run() {
                    panel.getTGPanel().vibrateGraph();
                }
            };
            thread.start();
        }
    }//GEN-LAST:event_VibrateDiagramMenuItemActionPerformed

    private void TabbedPaneMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_TabbedPaneMouseWheelMoved
        // TODO add your handling code here:
        TabbedPane.grabFocus();
        int rot = evt.getWheelRotation();
        int countTab = TabbedPane.getTabCount();
        if (rot < 0 && countTab <= EPanel.size() && countTab > 0) {  // negetive rotation
            int tabNo = this.TabbedPane.getSelectedIndex();
            if (tabNo - 1 >= 0) {
                TabbedPane.setSelectedIndex(tabNo - 1);
            }
        }
        if (rot > 0 && countTab <= EPanel.size() && countTab > 0) {
            int tabNo = this.TabbedPane.getSelectedIndex();
            if (tabNo + 1 < TabbedPane.getTabCount()) {
                TabbedPane.setSelectedIndex(tabNo + 1);
            }
        }
    }//GEN-LAST:event_TabbedPaneMouseWheelMoved

    private void DFSSpeechMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DFSSpeechMenuItemActionPerformed
        // TODO add your handling code here:
        Thread thread = new Thread() {
            public void run() {
                if (getCurrentEPanel() != null) {
                    Vector node = getCurrentEPanel().getTGPanel().getVisibleLocality().getNodes();
                    Node n;
                    for (int i = 0; i < node.size(); i++) {
                        n = (Node) node.elementAt(i);
                        if (n.getDepth() == 0) {
                            DFS(n, true);
                        }

                    }
                }
            }
        };
        thread.start();
    }//GEN-LAST:event_DFSSpeechMenuItemActionPerformed

    protected void SoundSelect(Node n) {
        if (!this.isDynamicSoundEnableed()) {
            if (n.getDepth() == 0) {
                Sound sound = new Sound("src/resources/sound/bubble.wav", "wav", 380);
                sound.run();

            } else if (n.getDepth() == 1) {
                Sound sound = new Sound("src/resources/sound/level1bubble.wav", "wav", 380);
                sound.run();

            } else if (n.getDepth() == 2) {
                Sound sound = new Sound("src/resources/sound/level2bubble.wav", "wav", 380);
                sound.run();

            } else {
                Sound sound = new Sound("src/resources/sound/level3bubble.wav", "wav", 580);
                sound.run();

            }

        } else {
            Sound sound = new Sound(null, "csound", 5, (n.getDepth() + 1) * 2);
            sound.run();
        }
    }

    public void setTabTitle(int index, String title) {
        if (TabbedPane.getTabCount() >= index) {
            TabbedPane.setTitleAt(index, title);
        }
    }

    protected void DFS(Node n, boolean Speech) {
        Vector child = n.getChildren();
        if (Speech) {
            speech.speak(n.getLabel());
        } else {
            SoundSelect(n);
        }
        for (int i = 0; i < child.size(); i++) {
            DFS((Node) child.elementAt(i), Speech);
        }
    }

    private void BFSSoundMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BFSSoundMenuItemActionPerformed
        // TODO add your handling code here:
        // BFS depth finding 
        Thread thread = new Thread() {

            public void run() {

                int maxDepth = 0;
                int tempDepth = 0;
                if (getCurrentEPanel() != null) {
                    Vector node = getCurrentEPanel().getTGPanel().getVisibleLocality().getNodes();

                    for (int i = 0; i < node.size(); i++) {
                        Node n = (Node) node.elementAt(i);
                        if (maxDepth < n.getDepth()) {
                            maxDepth = n.getDepth();
                        }

                    }
                    for (int j = 0; j <= maxDepth; j++) {
                        for (int i = 0; i < node.size(); i++) {
                            Node n = (Node) node.elementAt(i);
                            if (tempDepth == n.getDepth()) {
                                SoundSelect(n);
                            }
                        }
                        tempDepth++;
                    }
                }
            }
        };
        thread.start();
    }//GEN-LAST:event_BFSSoundMenuItemActionPerformed

    private void DFSSoundMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DFSSoundMenuItemActionPerformed
        // TODO add your handling code here:
        Thread thread = new Thread() {

            public void run() {
                if (getCurrentEPanel() != null) {
                    Vector node = getCurrentEPanel().getTGPanel().getVisibleLocality().getNodes();
                    Node n;
                    for (int i = 0; i < node.size(); i++) {
                        n = (Node) node.elementAt(i);
                        if (n.getDepth() == 0) {
                            DFS(n, false);
                        }
                    }
                }
            }
        };
        thread.start();
    }//GEN-LAST:event_DFSSoundMenuItemActionPerformed

    private void BFSSpeechMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BFSSpeechMenuItemActionPerformed
        // TODO add your handling code here:
        Thread thread = new Thread() {

            public void run() {
                if (getCurrentEPanel() != null) {
                    int maxDepth = 0;
                    int tempDepth = 0;
                    EditorPanel panel = getCurrentEPanel();
                    Vector node = panel.getTGPanel().getVisibleLocality().getNodes();

                    for (int i = 0; i < node.size(); i++) {
                        Node n = (Node) node.elementAt(i);
                        if (maxDepth < n.getDepth()) {
                            maxDepth = n.getDepth();
                        }

                    }
                    for (int j = 0; j <= maxDepth; j++) {
                        for (int i = 0; i < node.size(); i++) {
                            Node n = (Node) node.elementAt(i);
                            if (tempDepth == n.getDepth()) {
                                Speech.running = true;
                                speech.speak(n.getLabel());
                            }
                        }
                        tempDepth++;
                    }
                }
                Speech.running = false;
            }
        };
        thread.start();
    }//GEN-LAST:event_BFSSpeechMenuItemActionPerformed

    private void TabbedPaneKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TabbedPaneKeyPressed
        // TODO add your handling code here:
        if (evt.isControlDown() && evt.getKeyCode() == KeyEvent.VK_W) {
            removeTab();
            this.RedrawSystem();
            evt.consume();
        }

        if (evt.getKeyCode() >= 37 && evt.getKeyCode() <= 40) {
            this.getCurrentEPanel().getTGPanel().grabFocus();
        }
    }//GEN-LAST:event_TabbedPaneKeyPressed

    private void mindMapTreeMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mindMapTreeMousePressed
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            TreePath tp = this.mindMapTree.getPathForLocation(evt.getX(), evt.getY());
            if (tp != null) {
                EditorPanel panel = this.getCurrentEPanel();
                TGPanel tgPanel = panel.getTGPanel();
                Vector nodes = tgPanel.getVisibleLocality().getNodes();
                for (int i = 0; i < nodes.size(); i++) {
                    Node n = (Node) nodes.elementAt(i);
                    if (tp.toString().contains(n.getLabel())) {
                        tgPanel.setSelect(n);
                        //this.RedrawSystem();
                    }
                }
            }
        }
    }//GEN-LAST:event_mindMapTreeMousePressed

    private void TabbedPaneStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_TabbedPaneStateChanged
        // TODO add your handling code here:
        if (getCurrentEPanel() != null) {
            this.getCurrentEPanel().getTGPanel().grabFocus();
            this.updateTree(null);
        }

    }//GEN-LAST:event_TabbedPaneStateChanged

    public void changeClientConnectText(String text) {
        this.ConnectServerMenuItem.setText(text);
    }

    private void ConnectServerMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ConnectServerMenuItemActionPerformed
        // TODO add your handling code here:
        if (!Client.isConnected()) {
            if (getCurrentEPanel() != null) {
                if (JOptionPane.showConfirmDialog(this, "Do you want to change current tab.", "Question", JOptionPane.YES_NO_OPTION) == 0) {
                    if (getCurrentEPanel().getGLPanel() == Server.getGlPanel() && Server.getGlPanel() != null) {
                        JOptionPane.showConfirmDialog(this, "Error: This tab is of Server yo cannot make it client", "Error", JOptionPane.PLAIN_MESSAGE);
                    } else {
                        ConnectGUI connectGUI = new ConnectGUI(this.getCurrentEPanel().getGLPanel());
                    }
                } else {
                    this.addNewMindMaps(null);
                    ConnectGUI connectGUI = new ConnectGUI(this.getCurrentEPanel().getGLPanel());

                }
            } else {
                JOptionPane.showConfirmDialog(this, "Please open a tab first.", "Information", JOptionPane.PLAIN_MESSAGE);
            }
        } else {
            changeClientConnectText("Connect to Server");
            Client.disconnect();
            Client.setGLPanel(null);
        }
    }//GEN-LAST:event_ConnectServerMenuItemActionPerformed

    private void StartStopServerMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_StartStopServerMenuItemActionPerformed
        // TODO add your handling code here:
        if (!this.serverCreated) {
            this.serverCreated = true;
            this.StartStopServerMenuItem.setIcon(new ImageIcon(getClass().getResource("/resources/images/player_stop.png")));
            this.StartStopServerMenuItem.setText("Stop Server");
            Server.startServer();
            JOptionPane.showConfirmDialog(this, "Server is started.\n IP: "
                    + Config.serverAddress + "\n port: " + Config.serverPort + "\nUsername: "
                    + Config.username + "\npassword: " + Config.password, "Server Information", JOptionPane.PLAIN_MESSAGE);
        } else {
            this.serverCreated = false;
            this.StartStopServerMenuItem.setIcon(new ImageIcon(getClass().getResource("/resources/images/player_play.png")));
            this.StartStopServerMenuItem.setText("Start Server");
            Server.stopServer();
        }
    }//GEN-LAST:event_StartStopServerMenuItemActionPerformed

    private void OverviewMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OverviewMenuItemActionPerformed
        if (getCurrentEPanel() != null) {
            Thread thread;
            thread = new Thread() {
                @Override
                public void run() {
                    TGPanel tgp = getCurrentEPanel().getTGPanel();
                    if (tgp.getSelect() != null) {
                        speech.speak("The selected Node is " + tgp.getSelect().getLabel()
                                //+ ". Has number of children " + tgp.findRoot().getChildren().size()
                                + ".", (float) 1.2);

                        Vector node = getCurrentEPanel().getTGPanel().getVisibleLocality().getNodes();

                        for (int i = 0; i < tgp.getSelect().getChildren().size(); i++) {
                            Node n = (Node) tgp.getSelect().getChildren().elementAt(i);
                            speech.speak(n.getLabel());
                            BFS(n.getChildren(), false);
                        }
                    } else {
                        speech.speak("This is overview of " + tgp.findRoot().getLabel()
                                //+ ". Has number of children " + tgp.findRoot().getChildren().size()
                                + ".", (float) 1.2);

                        Vector node = getCurrentEPanel().getTGPanel().getVisibleLocality().getNodes();

                        for (int i = 0; i < tgp.findRoot().getChildren().size(); i++) {
                            Node n = (Node) tgp.findRoot().getChildren().elementAt(i);
                            Speech.running = true;
                            speech.speak(n.getLabel());
                            Speech.running = false;
                            BFS(n.getChildren(), false);
                        }
                    }
                }
            };
            while (Speech.running) {
            }
            thread.start();
        }
    }//GEN-LAST:event_OverviewMenuItemActionPerformed

    private void ServerConfugurationMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ServerConfugurationMenuItemActionPerformed
        // TODO add your handling code here:
        ConfigurationGUI configGUI = new ConfigurationGUI();
    }//GEN-LAST:event_ServerConfugurationMenuItemActionPerformed

    private void AboutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AboutMenuItemActionPerformed
        // TODO add your handling code here:
        AboutGUI about = new AboutGUI();
    }//GEN-LAST:event_AboutMenuItemActionPerformed

    private void ShareDiagramMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ShareDiagramMenuItemActionPerformed
        // TODO add your handling code here:
        if (Server.isRunning()) {
            int opt = JOptionPane.showConfirmDialog(this, "Your Current opened tab will be shared.", "Share Diagram", JOptionPane.OK_CANCEL_OPTION);
            if (opt == 0) {
                if (getCurrentEPanel() != null) {
                    if (Server.getGlPanel() == Client.getGLPanel() && Client.getGLPanel() != null) {
                        JOptionPane.showConfirmDialog(this, "Error: This Tab is Client.", "Error", JOptionPane.PLAIN_MESSAGE);
                    } else {
                        Server.setGlPanel(getCurrentEPanel().getGLPanel());
                        int opTab = this.TabbedPane.getSelectedIndex();
                        String title = TabbedPane.getTitleAt(opTab);
                        TabbedPane.setTitleAt(opTab, title);
                    }
                } else {
                    JOptionPane.showConfirmDialog(this, "Error: No Opened Tab", "Error", JOptionPane.PLAIN_MESSAGE);
                }
            }
        } else {
            JOptionPane.showConfirmDialog(this, "Error: Server not started yet.", "Error", JOptionPane.PLAIN_MESSAGE);
        }
    }//GEN-LAST:event_ShareDiagramMenuItemActionPerformed

    private void NodeInformationMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NodeInformationMenuItemActionPerformed
        // TODO add your handling code here:
        if (getCurrentEPanel() != null) {
            if (getCurrentEPanel().getTGPanel().getSelect() != null) {
                Node n = getCurrentEPanel().getTGPanel().getSelect();
                Thread thread = new Thread() {
                    public void run() {
                        Speech.running = true;
                        speech.speak("this node is " + n.getLabel(), (float) 1.2);
                        Speech.running = false;
                    }
                };
                while (Speech.running) {
                }
                thread.start();
            }
        }
    }//GEN-LAST:event_NodeInformationMenuItemActionPerformed

    private void SelectSoundCheckMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SelectSoundCheckMenuItemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_SelectSoundCheckMenuItemActionPerformed

    private void AddImageToolButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddImageToolButtonActionPerformed
        // TODO add your handling code here:
        if (this.getCurrentEPanel() != null) {
            EditorPanel panel = this.getCurrentEPanel();
            tgUIManager = panel.getTGUIManager();
            tgUIManager.deactivate("Navigate");
            tgUIManager.activate("addImageUI");
        }
    }//GEN-LAST:event_AddImageToolButtonActionPerformed

    private void EnableDynamicSOundCheckMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EnableDynamicSOundCheckMenuItemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_EnableDynamicSOundCheckMenuItemActionPerformed

    private void DynamiccSoundMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DynamiccSoundMenuItemActionPerformed
        // TODO add your handling code here:
        JOptionPane.showConfirmDialog(this, "Csound is Dynamic and will create sound differnt in "
                + "change of depth of the node. But it is slow.", "Dynamic cSound Information", JOptionPane.PLAIN_MESSAGE);
    }//GEN-LAST:event_DynamiccSoundMenuItemActionPerformed

    public boolean isSelecetSoundEnabled() {
        if (this.SelectSoundCheckMenuItem.isSelected()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isDynamicSoundEnableed() {
        if (this.EnableDynamicSOundCheckMenuItem.isSelected()) {
            return true;
        } else {
            return false;
        }
    }

    public void BFS(Vector Child, boolean speech) {
        Vector subchild = new Vector();
        for (Object node : Child) {
            Node n = (Node) node;
            subchild.addAll(n.getChildren());
            if (!speech) {
                SoundSelect(n);
            } else {
                Speech.running = true;
                this.speech.speak(n.getLabel());
                Speech.running = false;
            }
        }
        if (Child.isEmpty()) {
            return;
        }
        BFS(subchild, speech);
    }

    public File FileChooser(int type) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        if (type == 1) {       // for Open a file
            chooser.setFileFilter(new FileNameExtensionFilter("Serialize MindMap (.ser)", "ser"));
            int ret = chooser.showOpenDialog(this);
            if (ret == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                return file;
            } else {
                return null;
            }
        } else {    // for save a file
            chooser.setFileFilter(new FileNameExtensionFilter("Serialize MindMap (.ser)", "ser"));
            int ret = chooser.showSaveDialog(this);
            if (ret == JFileChooser.APPROVE_OPTION) {
                return chooser.getSelectedFile();
            } else {
                return null;
            }
        }
    }

    public JTabbedPane getTabbedPane() {
        return TabbedPane;
    }

    public JToolBar getHorizontalToolBar() {
        return HorizontalToolBar;
    }

    public JPanel getMainPanel() {
        return MainPanel;
    }

    public void RedrawSystem() {
        this.revalidate();
        this.repaint();

    }

    @SuppressWarnings("serial")
    private static class CustomTreeCellRenderer extends DefaultTreeCellRenderer {

        ImageIcon rendererIcon;

        public void setRendererIcon(ImageIcon myIcon) {
            this.rendererIcon = myIcon;
        }

        ;


        public Component getTreeCellRendererComponent(JTree tree,
                Object value, boolean selected, boolean expanded,
                boolean leaf, int row, boolean hasFocus) {

            Component ret = super.getTreeCellRendererComponent(tree, value,
                    selected, expanded, leaf, row, hasFocus);

            JLabel label = (JLabel) ret;

            label.setIcon(rendererIcon);

            return ret;
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem AboutMenuItem;
    private javax.swing.JButton AddImageToolButton;
    private javax.swing.JMenuItem BFSSoundMenuItem;
    private javax.swing.JMenuItem BFSSpeechMenuItem;
    private javax.swing.JMenuItem ConnectServerMenuItem;
    private javax.swing.JMenuItem DFSSoundMenuItem;
    private javax.swing.JMenuItem DFSSpeechMenuItem;
    private javax.swing.JMenuItem DynamiccSoundMenuItem;
    private javax.swing.JCheckBoxMenuItem EnableDynamicSOundCheckMenuItem;
    private javax.swing.JMenuItem FIleExitMenuItem;
    private javax.swing.JMenu FileMenu;
    private javax.swing.JMenuItem FileNewMenuItem;
    private javax.swing.JMenuItem FileOpenMenuItem;
    private javax.swing.JMenuItem FileSaveMenuItem;
    private javax.swing.JMenu HelpMenu;
    private javax.swing.JToolBar HorizontalToolBar;
    private javax.swing.JScrollPane JTreeScrollPane;
    private javax.swing.JPanel MainPanel;
    private javax.swing.JMenuBar Menubar;
    private javax.swing.JMenu NetworkkMenu;
    private javax.swing.JMenuItem NodeInformationMenuItem;
    private javax.swing.JMenu OverviewMenu;
    private javax.swing.JMenuItem OverviewMenuItem;
    private javax.swing.JPanel PExplorer;
    private javax.swing.JCheckBoxMenuItem SelectSoundCheckMenuItem;
    private javax.swing.JMenuItem ServerConfugurationMenuItem;
    private javax.swing.JMenuItem ShareDiagramMenuItem;
    private javax.swing.JMenuItem StartStopServerMenuItem;
    private javax.swing.JTabbedPane TabbedPane;
    private javax.swing.JMenuItem VibrateDiagramMenuItem;
    private javax.swing.JMenu ViewMenu;
    private javax.swing.JButton addNodeToolButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JTree mindMapTree;
    // End of variables declaration//GEN-END:variables

}
