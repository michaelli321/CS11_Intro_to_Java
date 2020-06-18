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
import javax.swing.SwingWorker;


/* 
 * the FractalExplorer class creates the GUI that allows us to see
 * and interact with the Fractal we are observing
 */
public class FractalExplorer {

	// keeps track of the length of the side of our display
	private int displaySize;

	// the actual display 
	private JImageDisplay display;

	// the fractal we are trying to show
	private FractalGenerator fractal;

	// the range of the complex plane we are observing
	private Rectangle2D.Double range;

	// combo box to select what fractal we are looking at 
	private JComboBox<FractalGenerator> comboBox;

	// keeps track of the number of rows still needing to be loaded
	private int rowsRemaining;

	// save button to save the fractal image
	private JButton save;

	// reset button to reset the fractal image
	private JButton reset;


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
		JFrame frame = new JFrame("Fractal Explorer Window");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// instantiate the JImageDisplay
		display = new JImageDisplay(displaySize, displaySize);

		// create combo box panel that holds the label and combo box 
		JPanel comboPanel = new JPanel();
		JLabel comboLabel = new JLabel("Fractal:");

		// create the list of objects in the combo box
		FractalGenerator[] fracArr = new FractalGenerator[3];
		fracArr[0] = new Mandelbrot();
		fracArr[1] = new Tricorn();
		fracArr[2] = new BurningShip();
		comboBox = new JComboBox<FractalGenerator>(fracArr);

		// add the componenets to the panel
		comboPanel.add(comboLabel);
		comboPanel.add(comboBox);

		// create button panel that holds the save and reset buttons
		JPanel buttonPanel = new JPanel();

		// create the save button 
		save = new JButton("Save Image");
		save.setActionCommand("save");

		// create our reset button
		reset = new JButton("Reset Display");
		reset.setActionCommand("reset");

		// add the components to the panel
		buttonPanel.add(save);
		buttonPanel.add(reset);

		// add our event listeners for the reset button and click
		reset.addActionListener(new ActionHandler());
		frame.addMouseListener(new MouseHandler());
		comboBox.addActionListener(new ActionHandler());
		save.addActionListener(new ActionHandler());

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

		enableUI(false);

		rowsRemaining = displaySize;

		// loop through all the rows and load them
		for (int row = 0; row < displaySize; row++) {
			FractalWorker worker = new FractalWorker(row);
			worker.execute();
		}


	}

	// this function enables or disables the interface's buttons and combo-box 
	public void enableUI(boolean val) {
		comboBox.setEnabled(val);
		save.setEnabled(val);
		reset.setEnabled(val);

	}

	// this inner class is an action handler that is used for the actions done in the GUI
	private class ActionHandler implements ActionListener {

		// does an action based on the action performed 
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
				int returnVal = chooser.showOpenDialog(display);
				if (returnVal == JFileChooser.APPROVE_OPTION) {

					// then do it
					try {
						javax.imageio.ImageIO.write(display.getImage(), "png", chooser.getSelectedFile());

					// if there was an error, print a message
					} catch (IOException error) {

						javax.swing.JOptionPane.showMessageDialog(display, error.getMessage(), 
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

			if (rowsRemaining != 0) {
				return;
			}

			// x is the pixel-coordinate; xCoord is the coordinate in the fractal's space
			double xCoord = FractalGenerator.getCoord(range.x, range.x + range.width, displaySize, e.getX());

			// y is the pixel-coordinate; yCoord is the coordinate in the fractal's space
			double yCoord = FractalGenerator.getCoord(range.y, range.y + range.width, displaySize, e.getY());

			// do the zoom
			fractal.recenterAndZoomRange(range, xCoord, yCoord, .5);

			drawFractal();
		}

	}

	// this inner class is responsible for computing the color values for a single row of the fractal
	private class FractalWorker extends SwingWorker<Object, Object> {

		// the y coordinate of the row that will be computed 
		private int y;

		// the color values for each pixel in that row 
		private int[] arr;

		// constructor
		public FractalWorker(int y) {
			y = y;
		}

		// this method loops through the row and stores each RGB value into the corresponding 
		// element of the integer array
		protected Object doInBackground() {

			arr = new int[displaySize];

			// y is the pixel-coordinate; yCoord is the coordinate in the fractal's space
			double yCoord = FractalGenerator.getCoord(range.y, range.y + range.width, displaySize, y);

			// loop through all the pixels of the display
			for (int i = 0; i < displaySize; i++) {

				// x is the pixel-coordinate; xCoord is the coordinate in the fractal's space
				double xCoord = FractalGenerator.getCoord(range.x, range.x + range.width, displaySize, i);

				// calculate the number of iterations
				int numIters = fractal.numIterations(xCoord, yCoord);

				// draw the corresponding color at that pixel location
				if (numIters == -1) {
					arr[i] = 0;
				} else {
					float hue = 0.7f + (float) numIters / 200f;
  					int rgbColor = Color.HSBtoRGB(hue, 1f, 1f);
  					arr[i] = rgbColor;
				}
			}

			return null;
		}

		// this method will iterate over the array of row-data and draw the pixel colors
		protected void done() {

			for (int i = 0; i < displaySize; i++) {
				display.drawPixel(i, y, arr[i]);
			}

			display.repaint(0, 0, y, displaySize, 1);

			rowsRemaining--;
			if (rowsRemaining == 0) {
				enableUI(true);
			}


		}
	}

	// main function which creates the FractalExplorer window for the user to interact with
	public static void main(String[] args) {

		FractalExplorer window = new FractalExplorer(800);
		window.createAndShowGui();
		window.drawFractal();
	}

}