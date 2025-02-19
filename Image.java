import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.AreaAveragingScaleFilter;
import java.awt.Toolkit;

import javax.swing.ImageIcon;

public class Image {
    private boolean isVisible;
    private int xPosition;
    private int yPosition;
    private int xSize;
    private int ySize;
    private java.awt.Image scaledImage;
    private ImageIcon baseImage;
    
    /**
     * Create an image with a default position and size.
     * @param filename the name of the image file to load
     */
    public Image(String filename) {
        baseImage = new javax.swing.ImageIcon(filename);
        scaledImage = baseImage.getImage();
        xSize = baseImage.getIconWidth();
        ySize = baseImage.getIconHeight();

        xPosition = 0;
        yPosition = 0;
        isVisible = false;
    }

    /**
     * Create an image with a specified position and size. Width OR height can be -1 to scale
     * the image to the correct aspect ratio.
     * @param filename the name of the image file to load
     * @param width the width of the image
     * @param height the height of the image
     */
    public Image(String filename, int width, int height) {
        baseImage = new javax.swing.ImageIcon(filename);
        scaleImage(width, height);

        isVisible = false;
        xPosition = 0;
        yPosition = 0;
    }

    /**
     * Create an image with a specified position and a default size.
     * @param filename the name of the image file to load
     * @param x the x position of the image
     * @param y the y position of the image
     * @param visible whether the image should be visible
     */
    public Image(String filename, int x, int y, boolean visible) {
        baseImage = new javax.swing.ImageIcon(filename);
        scaledImage = baseImage.getImage();        
        xSize = baseImage.getIconWidth();
        ySize = baseImage.getIconHeight();

        xPosition = x;
        yPosition = y;
        isVisible = visible;
        if (isVisible) {
            add();
        }
    }

    /**
     * Create an image with a specified position and size. Width OR height can be -1 to scale
     * the image to the correct aspect ratio.
     * @param filename the name of the image file to load
     * @param width the width of the image
     * @param height the height of the image
     * @param x the x position of the image
     * @param y the y position of the image
     * @param visible whether the image should be visible
     */
    public Image(String filename, int x, int y, int width, int height, boolean visible) {
        baseImage = new javax.swing.ImageIcon(filename);
        scaleImage(width, height);
        
        xPosition = x;
        yPosition = y;
        isVisible = visible;
        if (isVisible) {
            add();
        }
    }

    /**
     * Set the X position of the image.
     * @param x the new X position
     */
    public void setX(int x) {
        xPosition = x;
    }

    /**
     * Set the Y position of the image.
     * @param y the new Y position
     */
    public void setY(int y) {
        yPosition = y;
    }

    /**
     * Set the position of the image.
     * @param x the new x position
     * @param y the new y position
     */
    public void setPosition(int x, int y) {
        xPosition = x;
        yPosition = y;
    }

    /**
     * Get the X position of the image.
     * @return the X position of the image
     */
    public int getX() {
        return xPosition;
    }

    /**
     * Get the Y position of the image.
     * @return the Y position of the image
     */
    public int getY() {
        return yPosition;
    }

    /**
     * Get the width of the image.
     * @return the width of the image
     */
    public int getWidth() {
        return xSize;
    }

    /**
     * Get the height of the image.
     * @return the height of the image
     */
    public int getHeight() {
        return ySize;
    }

    /**
     * Set the width of the image. The image will be scaled maintaining the aspect ratio.
     * @param width the new width
     */
    public void setWidth(int width) {
        scaleImage(width, -1);
    }

    /**
     * Set the height of the image. The image will be scaled maintaining the aspect ratio.
     * @param height the new height
     */
    public void setHeight(int height) {
        scaleImage(-1, height);
    }

    /**
     * Set the size of the image. Width OR height can be -1 to scale the image to the correct
     * aspect ratio.
     * @param width the new width
     * @param height the new height
     */
    public void setSize(int width, int height) {
        scaleImage(width, height);
    }

    /**
     * Make this image visible. If it is already visible, do nothing.
     */
    public void makeVisible() {
        if (!isVisible) {
            isVisible = true;
            add();
        }
    }

    /**
     * Make this image invisible. If it is already invisible, do nothing.
     */
    public void makeInvisible() {
        if (isVisible) {
            isVisible = false;
            remove();
        }
    }

    /**
     * Add the Image to the screen.
     */
    private void add()
    {
        if(isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.add(this,(g) -> {
                    g.drawImage(scaledImage, xPosition, yPosition, xSize, ySize, null);
            });
        }
    }

    /**
     * Remove the Image from the screen.
     */
    private void remove()
    {
        if(isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.remove(this);
        }
    }

    /**
     * Scale the image to the specified width and height. If one of the dimensions is -1, the
     * image will be scaled to maintain the aspect ratio.
     * @param width the new width
     * @param height the new height
     */
    // private void scaleImage(int width, int height) {
    //     if (width < 0 && height < 0) {
    //         throw new IllegalArgumentException("Width and height cannot both be less than 0");
    //     }
    //     else if (width < 0) {
    //         width = (int) (height * baseImage.getIconWidth() / (double)baseImage.getIconHeight());
    //     }
    //     else if (height < 0) {
    //         height = (int) (width * baseImage.getIconHeight() / (double)baseImage.getIconWidth());
    //     }

    //     BufferedImage scaled = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    //     Graphics2D g = scaled.createGraphics();
    //     g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    //     g.drawImage(baseImage.getImage(), 0, 0, width, height, null);
    //     g.dispose();
    //     scaledImage = scaled;

    //     xSize = width;
    //     ySize = height;
    // }
    private void scaleImage(int width, int height) {
        ImageFilter filter = new AreaAveragingScaleFilter(width, height);
        FilteredImageSource source = new FilteredImageSource(baseImage.getImage().getSource(), filter);
        scaledImage = Toolkit.getDefaultToolkit().createImage(source);
    }
}
