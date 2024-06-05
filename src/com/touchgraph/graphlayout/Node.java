package com.touchgraph.graphlayout;

import audio.Sound;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class Node implements java.io.Serializable {

    public static int DEFAULT_TYPE = 1;
    public static final Font SMALL_TAG_FONT = new Font("Courier", Font.PLAIN, 9);
    public static Font TEXT_FONT = new Font("Verdana", Font.PLAIN, 12);

    public static final int TYPE_CIRCLE = 4;

    public final static int TYPE_ELLIPSE = 1;

    public final static int TYPE_RECTANGLE = 3;

    public final static int TYPE_ROUNDRECT = 2;

    public final static int TYPE_Image = 5;

    public Color BACK_DEFAULT_COLOR = Color.decode("#4080A0");
    // Variables that store default values for colors + fonts + node type
    public Color BACK_FIXED_COLOR = new Color(244, 102, 20);
    public Color BACK_FIXED_MAIN_COLOR = Color.WHITE;

    public Color BACK_HILIGHT_COLOR = new Color(205, 192, 166);
    public Color BACK_JML_COLOR = new Color(58, 176, 255);

    public Color BACK_MAIN_COLOR = Color.decode("#FF0000"); //new Color(255, 32, 20);

    public Color BACK_SELECT_COLOR = Color.decode("#009999");//new Color(225, 64, 200);

    public Color BORDER_DRAG_COLOR = Color.decode("#CC0465");//new Color(130, 130, 180);
    public Color BORDER_INACTIVE_COLOR = new Color(30, 50, 160);
    public Color BORDER_MOUSE_OVER_COLOR = Color.decode("#CC5400");// Color(160, 160, 180);

    public Color TEXT_COLOR = Color.black;
    protected Color backColor = BACK_DEFAULT_COLOR;
    // For tree Implementation....
    private Vector children;
    private int depth;
    public double drawx;

    public double drawy;
    protected double dx; //Used by layout
    protected double dy; //Used by layout
    private Vector edges;
    protected boolean fixed;
    protected Font font;
    protected FontMetrics fontMetrics;
    private String id;

    public boolean justMadeLocal = false;
    protected String lbl;
    protected String permlbl;
    public boolean markedForRemoval = false;
    public double massfade = 1; //Used by layout
    private Node parent;
    protected int repulsion; //Used by layout
    // ............
    // Modification by Lutz
    private String strUrl;
    protected Color textColor = TEXT_COLOR;
    boolean onCreateSound = true;

    protected int typ = TYPE_ELLIPSE;
    protected boolean visible;

    public int visibleEdgeCnt; //Should only be modified by graphelements.VisibleLocality
    public double x;

    public double y;

    protected File file = null;
    private byte[] image;

    protected Hashtable childData;

    public Node() {
        initialize(null);
        lbl = id;
        permlbl = id;
    }

    public Node(String id) {
        initialize(id);
        lbl = id;
        permlbl = id;
    }

    public Node(String id, String label) {
        initialize(id);
        if (label == null) {
            lbl = id;
            permlbl = id;
        } else {
            lbl = label;
            permlbl = id;
        }
    }

    public Node(String id, String label, int type, Color color) {
        initialize(id);
        typ = type;
        backColor = color;
        if (label == null) {
//            lbl = id;
            permlbl = id;
        } else {
            lbl = label;
            permlbl = id;
        }
    }

    @SuppressWarnings("unchecked")
    public synchronized void addChild(Node node) {
        if (node == null) {
            return;
        }
        node.setDepth(depth + 1);

        node.setPermlbl(getApropriateNodepermLabel(children));
//        if (this.getDepth() != 0) {
//            node.setPermlbl(this.permlbl + "." + (children.size() + 1));
////            node.setLabel(this.permlbl + "." + (children.size() + 1));
//        } else {
//            node.setPermlbl("" + (children.size() + 1));
////            node.setLabel("" + (children.size() + 1));
//        }
        children.addElement(node);
        childData.put(node.getPermlbl(), node);
    }

    String getApropriateNodepermLabel(Vector Children) {
        String plbl = null;
        if (this.depth == 0) {
            if (children.isEmpty()) {
                return "1";
            } else {
                int i = 1;
                while (true) {
                    String childPermLabel = String.valueOf(i);
                    if (childData.containsKey(childPermLabel)) {
                        i++;
                    } else {
                        plbl = childPermLabel;
                        break;
                    }
                }
            }
            return plbl;
        } else {
            if (children.isEmpty()) {
                return this.getPermlbl() + ".1";
            } else {
                int i = 1;
                while (true) {
                    String childPermLabel = this.getPermlbl() + "." + i;
                    if (childData.containsKey(childPermLabel)) {
                        i++;
                    } else {
                        plbl = childPermLabel;
                        break;
                    }
                }
            }
            return plbl;
        }
    }

    public void deleteChild(Node node) {
        if (children.contains(node)) {
            children.remove(node);
            childData.remove(node.getPermlbl());
        }
    }

    public String getPermlbl() {
        return permlbl;
    }

    public File getFile() {
        return file;
    }

    public BufferedImage getImage() {
        return convertBytesToImage(image);
    }

    public void setImage(BufferedImage image) {
        this.image = convertImageToBytes(image);
    }

    public void setFile(File file) {
        try {
            this.file = file;
            BufferedImage img = ImageIO.read(file);
            img = scaleImage(img, 300);
            setImage(img);
        } catch (IOException ex) {
            Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public byte[] convertImageToBytes(BufferedImage image) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ImageIO.write((RenderedImage) image, "png", bos);
            return bos.toByteArray();
        } catch (IOException ex) {
            Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public BufferedImage convertBytesToImage(byte[] img) {
        try {
            ByteArrayInputStream bin = new ByteArrayInputStream(img);
            return ImageIO.read(bin);
        } catch (IOException ex) {
            Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private BufferedImage scaleImage(BufferedImage bufferedImage, int size) {
        double boundSize = size;
        int origWidth = bufferedImage.getWidth();
        int origHeight = bufferedImage.getHeight();
        double scale;
        if (origHeight > origWidth) {
            scale = boundSize / origHeight;
        } else {
            scale = boundSize / origWidth;
        }
        //* Don't scale up small images.
        if (scale > 1.0) {
            return (bufferedImage);
        }
        int scaledWidth = (int) (scale * origWidth);
        int scaledHeight = (int) (scale * origHeight);
        Image scaledImage = bufferedImage.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
        // new ImageIcon(image); // load image
        // scaledWidth = scaledImage.getWidth(null);
        // scaledHeight = scaledImage.getHeight(null);
        BufferedImage scaledBI = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = scaledBI.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(scaledImage, 0, 0, null);
        g.dispose();
        return (scaledBI);
    }

    public void setPermlbl(String permlbl) {
        this.permlbl = permlbl;
    }

    public void addEdge(Edge edge) {
        if (edge == null) {
            return;
        }
        edges.addElement(edge);
    }

    public void setParent(Node node) {
        if (node == null) {
            return;
        }
        if (depth == 0) {
            return;
        }

        node.setDepth(depth - 1);
        parent = node;
    }

    public Node childAt(int index) {
        return (Node) children.elementAt(index);
    }

    public int childrenNum() {
        return children.size();
    }

    public boolean containsPoint(double px, double py) {
        return ((px > drawx - getWidth() / 2) && (px < drawx + getWidth() / 2)
                && (py > drawy - getHeight() / 2) && (py < drawy + getHeight() / 2));
    }

    public boolean containsPoint(Point p) {
        return ((p.x > drawx - getWidth() / 2) && (p.x < drawx + getWidth() / 2)
                && (p.y > drawy - getHeight() / 2) && (p.y < drawy + getHeight() / 2));
    }

    public Edge edgeAt(int index) {
        return (Edge) edges.elementAt(index);
    }

    /**
     * Return the number of Edges in the cumulative Vector.
     *
     * @return
     */
    public int edgeCount() {
        return edges.size();
    }

    public int edgeNum() {
        return edges.size();
    }

    public Color getBackColor() {
        return backColor;
    }

    public void setBackColor(Color bgColor) {
        backColor = bgColor;
    }

    public Vector getChildren() {
        return children;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int d) {
        this.depth = d;
    }

    public Vector getEdges() {
        return edges;
    }

    public boolean getFixed() {
        return fixed;
    }

    public void setFixed(boolean fixed) {
        this.fixed = fixed;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public int getHeight() {
        if (fontMetrics != null) {
            return fontMetrics.getHeight() + 25;
        } else {
            return 45;
        }
    }

    public String getID() {
        return id;
    }

    public void setID(String id) {
        this.id = id;
    }

    public String getLabel() {
        return lbl;
    }

    public void setLabel(String label) {
        lbl = label;
    }

    public Point getLocation() {
        return new Point((int) x, (int) y);
    }

    public void setLocation(Point p) {
        this.x = p.x;
        this.y = p.y;
    }

    public void setNodeBackDefaultColor(Color color) {
        BACK_DEFAULT_COLOR = color;
    }

    public void setNodeBackFixedColor(Color color) {
        BACK_FIXED_COLOR = color;
    }

    public void setNodeBackHilightColor(Color color) {
        BACK_HILIGHT_COLOR = color;
    }

    public void setNodeBackSelectColor(Color color) {
        BACK_SELECT_COLOR = color;
    }

    public void setNodeBorderDragColor(Color color) {
        BORDER_DRAG_COLOR = color;
    }

    public void setNodeBorderInactiveColor(Color color) {
        BORDER_INACTIVE_COLOR = color;
    }

    public void setNodeBorderMouseOverColor(Color color) {
        BORDER_MOUSE_OVER_COLOR = color;
    }

    public void setNodeTextColor(Color color) {
        TEXT_COLOR = color;
    }

    public void setNodeTextFont(Font font) {
        TEXT_FONT = font;
    }

    public void setNodeType(int type) {
        DEFAULT_TYPE = type;
    }

    public Color getPaintBackColor(TGPanel tgPanel) {
        if (this == tgPanel.getSelect()) {
            if (this.typ == 1) {
                return BACK_SELECT_COLOR;
            } else if (this.typ == 2) {
                return BACK_MAIN_COLOR;
            } else {
                return BACK_DEFAULT_COLOR;
            }
        } else {
            if (fixed && this.typ == 2) {
                return BACK_FIXED_MAIN_COLOR;
            }
            if (fixed) {
                return BACK_FIXED_COLOR;
            }
            if (!fixed && typ == 2) {
                return BACK_MAIN_COLOR;
            }
            if (!fixed) {
                return BACK_DEFAULT_COLOR;
            }
            return backColor;
        }
    }

    public Color getBackFixedColor() {
        return BACK_FIXED_MAIN_COLOR;
    }

    public void setBACK_FIXED_MAIN_COLOR(Color BACK_FIXED_MAIN_COLOR) {
        this.BACK_FIXED_MAIN_COLOR = BACK_FIXED_MAIN_COLOR;
    }

    public Color getPaintBorderColor(TGPanel tgPanel) {
        if (this == tgPanel.getDragNode()) {
            return BORDER_DRAG_COLOR;
        } else if (this == tgPanel.getMouseOverN()) {
            return BORDER_MOUSE_OVER_COLOR;
        } else {
            return BORDER_INACTIVE_COLOR;
        }
    }

    public Color getPaintTextColor(TGPanel tgPanel) {
        return textColor;
    }

    public Node getParent() {
        return parent;
    }

    public Point getRelLocation() {
        return new Point((int) this.drawx, (int) this.drawy);
    }

    public void setRelLocation(Point p) {
        this.drawx = p.x;
        this.drawy = p.y;
    }

    public Color getTextColor() {
        return textColor;
    }

    public void setTextColor(Color txtColor) {
        textColor = txtColor;
    }

    public int getType() {
        return typ;
    }

    public void setType(int type) {
        typ = type;
    }

    public int getWidth() {
        if (fontMetrics != null && lbl != null) {
            return fontMetrics.stringWidth(lbl) + 70;
        } else {
            return 80;
        }
    }

    private void initialize(String identifier) {
        this.id = identifier;
        edges = new Vector();
        children = new Vector();
        x = Math.random() * 2 - 1; // If multiple nodes are added without repositioning,
        y = Math.random() * 2 - 1; // randomizing starting location causes them to spread out nicely.
        repulsion = 60;         // Gives gaps between nodes
        font = TEXT_FONT;
        fixed = true;
        typ = DEFAULT_TYPE;
        visibleEdgeCnt = 0;
        visible = false;
        depth = 0;
        childData = new Hashtable();
    }

    public boolean intersects(Dimension d) {
        return (drawx > 0 && drawx < d.width && drawy > 0 && drawy < d.height);
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean v) {
        visible = v;
    }

    public void paint(Graphics g, TGPanel tgPanel) {
        if (!intersects(tgPanel.getSize())) {
            return;
        }
        paintNodeBody(g, tgPanel);

        if (visibleEdgeCount() < edgeCount()) {
            int ix = (int) drawx;
            int iy = (int) drawy;
            int h = getHeight();
            int w = getWidth();
            int tagX = ix + (w - 7) / 2 - 2 + w % 2;
            int tagY = iy - h / 2 - 2;
            char character;
            int hiddenEdgeCount = edgeCount() - visibleEdgeCount();
            character = (hiddenEdgeCount < 9) ? (char) ('0' + hiddenEdgeCount) : '*';
            paintSmallTag(g, tgPanel, tagX, tagY, Color.red, Color.white, character);
        }
    }

    public void paintNodeBody(Graphics g, TGPanel tgPanel) {
        g.setFont(font);
        fontMetrics = g.getFontMetrics();

        int ix = (int) drawx;
        int iy = (int) drawy;
        int h = getHeight();
        int w = getWidth();
        int r = h / 2 + 1; // arc radius

        Color borderCol = getPaintBorderColor(tgPanel);
        g.setColor(borderCol);

        if (typ == TYPE_ROUNDRECT) {
            g.fillRoundRect(ix - w / 2, iy - h / 2, w, h, r, r);

        } else if (typ == TYPE_ELLIPSE) {
            g.fillOval(ix - w / 2, iy - h / 2, w, h);
        } else if (typ == TYPE_CIRCLE) { // just use width for both dimensions
            g.fillOval(ix - w / 2, iy - w / 2, w, w);
        } else if (typ == TYPE_Image && file != null) {
            g.setColor(Color.WHITE);
            g.drawImage(convertBytesToImage(image), ix - w / 2, iy - h / 2, w, h, tgPanel);
        } else { // TYPE_RECTANGLE
            g.fillRect(ix - w / 2, iy - h / 2, w, h);
        }

        Color backCol = getPaintBackColor(tgPanel);
        g.setColor(backCol);

        if (typ == TYPE_ROUNDRECT) {
            g.fillRoundRect(ix - w / 2 + 2, iy - h / 2 + 2, w - 4, h - 4, r, r);
        } else if (typ == TYPE_ELLIPSE) {
            g.fillOval(ix - w / 2 + 2, iy - h / 2 + 2, w - 4, h - 4);
        } else if (typ == TYPE_CIRCLE) {
            g.fillOval(ix - w / 2 + 2, iy - w / 2 + 2, w - 4, w - 4);
        } else if (typ == TYPE_Image && file != null) {
        } else { // TYPE_RECTANGLE
            g.fillRect(ix - w / 2 + 2, iy - h / 2 + 2, w - 4, h - 4);
        }

        Color textCol = getPaintTextColor(tgPanel);
        g.setColor(textCol);
        if (typ != TYPE_Image) {
            if (lbl != null) {
                g.drawString(lbl, ix - fontMetrics.stringWidth(lbl) / 2, iy + fontMetrics.getDescent() + 1);
            }
            if (permlbl != null) {
                g.drawString(permlbl, ix - w / 2 - fontMetrics.stringWidth(permlbl) / 2, iy - 25);
            }
        } else if (lbl != null) {
            g.drawString(lbl, ix - fontMetrics.stringWidth(lbl) / 2, iy + fontMetrics.getDescent() + 30);
        }

        if (this.onCreateSound) {
            if (depth == 0) {
                Runnable sound = new Sound("bubble.wav", "wav");
                new Thread(sound).start();
            } else if (depth == 1) {
                Runnable sound = new Sound("level1bubble.wav", "wav");
                new Thread(sound).start();
            } else if (depth == 2) {
                Runnable sound = new Sound("level2bubble.wav", "wav");
                new Thread(sound).start();
            } else // for depth greater than or equal to 3
            {
                Runnable sound = new Sound("level3bubble.wav", "wav");
                new Thread(sound).start();
            }

            this.onCreateSound = false;
        }
    }

    public void paintSmallTag(Graphics g, TGPanel tgPanel, int tagX, int tagY, Color backCol, Color textCol, char character) {
        g.setColor(backCol);
        g.fillRect(tagX, tagY, 8, 8);
        g.setColor(textCol);
        g.setFont(SMALL_TAG_FONT);
        g.drawString("" + character, tagX + 2, tagY + 7);
    }

    public void removeEdge(Edge edge) {
        edges.removeElement(edge);
    }

    public int visibleEdgeCount() {
        return visibleEdgeCnt;
    }

//    public static class MyImage implements java.io.Serializable { // for writing image
//
//        private transient static final ArrayList<Image> images = new ArrayList();
//        private transient static final ArrayList<String> nodeId = new ArrayList();
//        private transient static final Hashtable imageTable = new Hashtable();
//
//        public static void addImage(Image img, String id) {
//            images.add(img);
//            nodeId.add(id);
//            imageTable.put(id, img);
//        }
//
//        public static void removeImage(String id) {
//            Image img = (Image) imageTable.get(id);
//            if (img != null) {
//                images.remove(img);
//                nodeId.remove(id);
//                imageTable.remove(id);
//            }
//        }
//
//        public static void writeImage(ObjectOutputStream oos) throws IOException {
//            oos.writeInt(images.size()); // how many images are serialized?
//            for (int i = 0; i < images.size(); i++) {
//                oos.writeUTF(nodeId.get(i));
//                ImageIO.write((RenderedImage) images.get(i), "png", oos); // png is lossless
//            }
//        }
//
//        public static void readImage(ObjectInputStream ois) throws IOException {
//            final int imageCount = ois.readInt();
//            for (int i = 0; i < imageCount; i++) {
//                String nodeid = ois.readUTF();
//                addImage(ImageIO.read(ois), nodeid);
//            }
//        }
//
//        public static void restoreImages(VisibleLocality vloc) {
//            TGForEachNode fen = new TGForEachNode() {
//                @Override
//                public void forEachNode(Node n) {
//                    for (int i = 0; i < nodeId.size(); i++) {
//                        Image image = (Image) imageTable.get(n.getID());
//                        if (image != null) {
//                            n.setImage(image);
//                        }
//                    }
//                }
//            };
//            vloc.forAllNodes(fen);
//        }
//    }
}
