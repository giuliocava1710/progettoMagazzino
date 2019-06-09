import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * A class derived from JPanel that allows user to draw simple graphs
 * The drawing area is set as follows:
 *
 * upper left corner: (0,0)
 * upper right corner: (width, 0)
 * lower left corner: (0, height)
 * lower right corner: (width, height)
 */
public class JGraph extends JPanel {
    private BufferedImage image = null;
    private Graphics2D graph = null;

    private Color background;
    private Color pencil;
    private int width;
    private int height;
    /**
     * set the background color
     * @param background color
     */
    public void setBackground(Color background) {
        this.background = background;
    }
    /**
     * set the foreground color for active drawing
     * @param pencil color
     */
    public void setPencil(Color pencil) {
        this.pencil = pencil;
    }
    /**
     * Create a JGraph with given dimensions, setting the point at the upper left corner to (0,0).
     * It almost cleans it with a WHITE background color and sets a BLACK pencil
     * @param width
     * @param height
     */
    public JGraph(int width, int height) {
        image = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        background = Color.WHITE;
        pencil = Color.BLACK;
        this.width= width;
        this.height=height;
        clean();
    }

    /**
     * cleans the JGraph with the background color
     */
    public void clean() {
        Graphics2D graph = image.createGraphics();
        graph.setColor(pencil);
        graph.setBackground(background);
        graph.clearRect(0,0,width,height);
        repaint();
    }
    /**
     * draws a line using the pencil color
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     */
    public void drawLine(int x1, int y1, int x2, int y2) {
        Graphics2D graph = image.createGraphics();
        graph.setColor(pencil);
        graph.drawLine(x1, y1, x2, y2);
        repaint();
    }
    /**
     * draws a rectangle using the pencil color
     * @param x1
     * @param y1
     * @param width
     * @param height
     * @param filled: set to true if you want to fill up the rectangle
     */
    public void drawRect(int x1,int y1,int width, int height, boolean filled) {
        Graphics2D graph = image.createGraphics();
        graph.setColor(pencil);
        if (filled) graph.fillRect(x1,y1,width,height);
        else graph.drawRect(x1, y1, width, height);
        repaint();
    }
    /**
     * draws an ellipse using the pencil color
     * @param x1
     * @param y1
     * @param width
     * @param height
     * @param filled: set to true if you want to fill up the ellipse
     */
    public void drawEllipse(int x1,int y1,int width, int height, boolean filled) {
        Graphics2D graph = image.createGraphics();
        graph.setColor(pencil);
        if (filled) graph.fillOval(x1,y1,width,height);
        else graph.drawOval(x1, y1, width, height);
        repaint();
    }
    /**
     * draws a point at the specified position
     * @param x
     * @param y
     */
    public void drawPoint(int x, int y) {
        drawEllipse(x+2, y+2, 4, 4, true);
    }
    /**
     * refresh the drawing area
     * @param g
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.drawImage(image, 0, 0, width,height,null);
    }
}

/**
 * A class derived from JGraph that allows user to draw simple cartesians graph
 * The drawing area is set as follows:
 *
 * upper left corner: (minX, maxY)
 * upper right corner: (maxX, maxY)
 * lower left corner: (minX, minY)
 * lower right corner: (maxX, minY)
 */
class JCartesian extends JGraph {

    static double mx, my, qx, qy;
    float minX, maxX, minY, maxY;

    /**
     * Create a JGraph with given dimensions, setting the point at the upper left corner to (minX, maxY).
     * It almost cleans it with a WHITE background color and sets a BLACK pencil
     * @param width
     * @param height
     * @param minX
     * @param maxX
     * @param minY
     * @param maxY
     */
    public JCartesian(int width, int height, float minX, float maxX, float minY, float maxY) {
        super(width, height);

        mx = (1.0*(0 - width)) /(minX - maxX);
        my = (1.0*(height - 0)) / (minY - maxY);
        qx = -mx * minX;
        qy = -my * maxY;

        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
    }
    /**
     * convert a x value on the cartesian graph to the equivalent position on the JPanel area
     * @param x
     */
    public static int convX(float x) {
        return (int) Math.round(mx * x + qx);
    }
    /**
     * convert a y value on the cartesian graph to the equivalent position on the JPanel area
     * @param y
     */
    public static int convY(float y) {
        return (int) Math.round(my * y + qy);
    }
    /**
     * draw the cartesian axis with the pencil color
     */
    public void drawAxis() {
        drawLineCart(minX, 0, maxX, 0);
        drawLineCart(0, minY, 0, maxY);
    }
    /**
     * draws a rectangle in the cartesian area using the pencil color
     * @param x1
     * @param y1
     * @param width
     * @param height
     * @param filled: set to true if you want to fill up the rectangle
     */
    public void drawRectCart(float x1, float y1, float width, float height, boolean filled) {
        int xg = convX(x1);
        int widthg = convX(width);
        int yg = convY(y1);
        int heightg = convY(height);

        drawRect(xg, yg, widthg, heightg, filled);
    }
    /**
     * draws an ellipse in the cartesian area using the pencil color
     * @param x1
     * @param y1
     * @param width
     * @param height
     * @param filled: set to true if you want to fill up the ellipse
     */
    public void drawEllipseCart(float x1, float y1, float width, float height, boolean filled) {
        int xg = convX(x1);
        int widthg = convX(width);
        int yg = convY(y1);
        int heightg = convY(height);

        drawEllipse(xg, yg, widthg, heightg, filled);
    }
    /**
     * draws a line in the cartesian area using the pencil color
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     */
    public void drawLineCart(float x1, float y1, float x2, float y2) {
        int x1g = convX(x1);
        int y1g = convY(y1);
        int x2g = convX(x2);
        int y2g = convY(y2);
        drawLine(x1g, y1g, x2g, y2g);
    }
    /**
     * draws a point at the specified position in the cartesian area
     * @param x
     * @param y
     */
    public void drawPointCart(float x, float y) {
        int xg = convX(x);
        int yg = convY(y);

        drawPoint(xg, yg);
    }

    public void drawXReferences(){
        for(float i=minX; i<maxX/2; i++){
            drawLineCart(i, -3, i, +3);

        }

    }

    public void drawYReferences(float unit){
        for(float i=minY; i<maxY; i+=unit){
            drawLineCart(-1, i, 1, i);
        }
    }
}
