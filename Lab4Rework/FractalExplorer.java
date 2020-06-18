import java.awt.geom.Rectangle2D;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.Color;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.IOException;

/* 
 * the FractalExplorer class creates the GUI that allows us to see
 * and interact with the Fractal we are observing. Our Fractal Explorer 
 * keeps track of what Fractal we are observing, the size of the display
 * and the range of the complex plane we are observing. Clicking on the display
 * allows us to zoom in. We can choose between three different fractals to 
 * look at and we are able to save the image. We also have a reset button
 * that will give us our default view
 */
public class FractalExplorer {

	/* keeps track of the length of the side of our display */
	private int displaySize;

	/* the actual display */
	private JImageDisplay display;

	/* the fractal we are trying to show */
	private FractalGenerator fractal;

	/* the range of the complex plane we are observing */
	private Rectangle2D.Double range;

	/* combo box to select what fractal we are looking at */
	private JComboBox<FractalGenerator> comboBox;

	/* create the JFrame that will hold our JImageDisplay */
	JFrame frame = new JFrame("Fractal Explorer Window");

	/* 
	 * constructor that just takes a size and initializes everything 
	 * other than the display 
	 */
	public FractalExplorer(int size) {

		displaySize = size; 
		fractal = new Mandelbrot();
		range = new Rectangle2D.Double();
		fractal.getInitialRange(range);
	}

	/*
	 * this method creates the display, frame, and reset button.
	 * it also deals with events and is basically the interactive 
	 * interface that users will see and deal with 
	 */
	public void createAndShowGui() {

		// create the JFrame that will hold our JImageDisplay
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// instantiate the JImageDisplay
		display = new JImageDisplay(displaySize, displaySize);

		// create combo box panel that holds the label and combo box 
		JPanel comboPanel = new JPanel();
		JLabel comboLabel = new JLabel("Fractal:");

		// create the list of objects in the combo box
		comboBox = new JComboBox<FractalGenerator>();
		comboBox.addItem(new Mandelbrot());
		comboBox.addItem(new Tricorn());
		comboBox.addItem(new BurningShip());

		// add the componenets to the panel
		comboPanel.add(comboLabel);
		comboPanel.add(comboBox);

		// create button panel that holds the save and reset buttons
		JPanel buttonPanel = new JPanel();

		// create the save button 
		JButton save = new JButton("Save Image");
		save.setActionCommand("save");

		// create our reset button
		JButton reset = new JButton("Reset Display");
		reset.setActionCommand("reset");

		// add the components to the panel
		buttonPanel.add(save);
		buttonPanel.add(reset);

		// add our event listeners for the reset button and click
		ActionHandler ah = new ActionHandler();
		reset.addActionListener(ah);
		frame.addMouseListener(new MouseHandler());
		comboBox.addActionListener(ah);
		save.addActionListener(ah);

		// put the button and display onto the frame
		frame.setLayout(new BorderLayout());
		frame.add(buttonPanel, BorderLayout.SOUTH);
		frame.add(display, BorderLayout.CENTER);
		frame.add(comboPanel, BorderLayout.NORTH);

		// lay out contents of frame and make it visible and non-resizeable
		frame.pack();
		frame.setVisible(true);
		frame.setResizable(false);


	}

	/*
	 * this method will draw the fractal onto the display by finding 
	 * the number of iterations at a coordinate and then changing 
	 * the color of that spot to its corresponding color 
	 */ 
	public void drawFractal() {
		// loop through all the pixels of the display
		for (int i = 0; i < displaySize; i++) {
			for (int j = 0; j < displaySize; j++) {

				 // x is the pixel-coordinate; xCoord is the coordinate in the fractal's space
				double xCoord = FractalGenerator.getCoord(range.x, range.x + range.width, displaySize, i);

				// y is the pixel-coordinate; yCoord is the coordinate in the fractal's space
				double yCoord = FractalGenerator.getCoord(range.y, range.y + range.width, displaySize, j);

				// calculate the number of iterations
				int numIters = fractal.numIterations(xCoord, yCoord);

				// draw the corresponding color at that pixel location
				if (numIters == -1) {
					display.drawPixel(i, j, 0);
				} else {
					float hue = 0.7f + (float) numIters / 200f;
  					int rgbColor = Color.HSBtoRGB(hue, 1f, 1f);
  					display.drawPixel(i, j, rgbColor);
				}

			}
		}

		display.repaint();
	}

	// this inner class is an action handler that is used for the actions done in the GUI
	private class ActionHandler implements ActionListener {

		/*  
			does an action based on the action performed. There are three cases here
			1. if the rest button was hit, then we get the intial range and redraw 
			the fractal

		    2. if the save button was hit, then we prompt the user with a save 
		    dialog box and proceed to save 
		 
		    3. if the combobox was changed, then we redraw the fractal to show
		    the selected fractal
		*/
		public void actionPerformed(ActionEvent e) {
			// if reset button was hit, reset the view
			if (e.getActionCommand() == "reset") {
				fractal.getInitialRange(range);
				drawFractal();

			// if save button was hit, save the image
			} else if (e.getActionCommand() == "save") {

				// create a file chooser dialog box 
				JFileChooser chooser = new JFileChooser();
				FileFilter filter = new FileNameExtensionFilter("PNG Images", "png");
				chooser.setFileFilter(filter);
				chooser.setAcceptAllFileFilterUsed(false);

				// if we can save the picture 
				int returnVal = chooser.showOpenDialog(frame);
				if (returnVal == JFileChooser.APPROVE_OPTION) {

					// then do it
					try {
						javax.imageio.ImageIO.write(display.getImage(), "png", chooser.getSelectedFile());

					// if there was an error, print a message
					} catch (IOException error) {

						javax.swing.JOptionPane.showMessageDialog(frame, error.getMessage(), 
							"Error: Image Cannot be Saved", JOptionPane.ERROR_MESSAGE);

					}

				// otherwise, just exit
				} else {
					return;
				}

			// if the combo box was changed 
			} else {

				// change the view to the selected Fractal
				if ((comboBox.getSelectedItem().toString()).equals("Mandelbrot")) {
					fractal = new Mandelbrot();

				} else if ((comboBox.getSelectedItem().toString()).equals("Tricorn")) {
					fractal = new Tricorn();

				} else {
					fractal = new BurningShip();
				}

				// refresh the view
				range = new Rectangle2D.Double();
				fractal.getInitialRange(range);
				drawFractal();
			}	
			
		}
	}

	// this inner class is a mouse handler that will zoom when the mouse clicks the display
	private class MouseHandler extends MouseAdapter {

		// zoom in a scale of .5 at the spot the display is clicked
		@Override
		public void mouseClicked(MouseEvent e) {

			// x is the pixel-coordinate; xCoord is the coordinate in the fractal's space
			double xCoord = FractalGenerator.getCoord(range.x, range.x + range.width, displaySize, e.getX());

			// y is the pixel-coordinate; yCoord is the coordinate in the fractal's space
			double yCoord = FractalGenerator.getCoord(range.y, range.y + range.width, displaySize, e.getY());

			// do the zoom
			fractal.recenterAndZoomRange(range, xCoord, yCoord, .5);

			drawFractal();
		}

	}

	// main function which creates the FractalExplorer window for the user to interact with
	public static void main(String[] args) {

		FractalExplorer window = new FractalExplorer(800);
		window.createAndShowGui();
		window.drawFractal();
	}

}