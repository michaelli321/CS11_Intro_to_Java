import javax.swing.JComponent;
import java.awt.image.BufferedImage;
import java.awt.Dimension;
import java.awt.Graphics;

/* 
 * JImageDisplay class is a subclass of JComponent 
 * and contains one attribute which is a BufferedImage. This
 * class gives us an object to be able to draw on and show
 * a user 
 */
public class JImageDisplay extends JComponent {

	/* BufferedImage attribute is what we will draw to/on */
	private BufferedImage image;

	/* 
	 * given a width and height, this function is the constructor and 
	 * creates the BufferedImage with the correct sizes and dimensions
	 */
	public JImageDisplay(int width, int height) {

		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		Dimension dim = new Dimension(width, height);
		super.setPreferredSize(dim);
	}

	/*
	 * given a Graphics object, this function will just display the image 
	 * data 
	 */
	@Override
	protected void paintComponent(Graphics g) {

		super.paintComponent(g);
		g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
	}

	/*
	 * this method simply sets all pixels to black to clear the image 
	 * on the window
	 */
	public void clearImage() {

		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {
				image.setRGB(i, j, 0);
			}
		}
	}

	// this method allows us to change a pixel's color 
	public void drawPixel(int x, int y, int rgbColor) {
		image.setRGB(x, y, rgbColor);
	}

	// accessor method that returns the buffered image 
	public BufferedImage getImage() {
		return image; 
	}

}
