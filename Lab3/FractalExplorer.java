import java.awt.geom.Rectangle2D;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.Color;
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

	private int rowsRemaining;



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

		// create our reset butotn
		JButton reset = new JButton("Reset Display");

		// add our event listeners for the reset button and click
		reset.addActionListener(new ActionHandler());
		frame.addMouseListener(new MouseHandler());

		// put the button and display onto the frame
		frame.setLayout(new BorderLayout());
		frame.add(reset, BorderLayout.SOUTH);
		frame.add(display, BorderLayout.CENTER);

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

		for (int row = 0; row < displaySize; row++) {
			FractalWorker worker = new FractalWorker(row);
			worker.execute();
		}
	}


	// this inner class is an action handler that is used for the reset button
	private class ActionHandler implements ActionListener {

		// resets the view to the original view
		public void actionPerformed(ActionEvent e) {
			fractal.getInitialRange(range);
			drawFractal();
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

	private class FractalWorker extends SwingWorker<Object, Object> {

		private int y;

		private int[] arr;

		public FractalWorker(int y) {

			y = y;
		}

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

		protected void done() {

			for (int i = 0; i < displaySize; i++) {

				display.drawPixel(i, y, arr[i]);
			}

			display.repaint(0, 0, y, displaySize, 1);
		}

	}

	// main function which creates the FractalExplorer window for the user to interact with
	public static void main(String[] args) {

		FractalExplorer window = new FractalExplorer(800);
		window.createAndShowGui();
		window.drawFractal();
	}

}