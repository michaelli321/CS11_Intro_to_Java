import java.awt.geom.Rectangle2D;

/* 
 * The Tricorn class is a subclass of FractalGenerator and 
 * will maintain the implementation of the Tricorn Fractal
 */ 
public class Tricorn extends FractalGenerator {

	// define a maximum number of iterations for our algorithm
	public static final int MAX_ITERATIONS = 2000;

	/*
	 * this method just sets what part of the complex
	 * plane is the most interesting for the Tricorn 
	 * fractal (-2, -2) to (2, 2)
	 */
	public void getInitialRange(Rectangle2D.Double range) {

		range.height = 4;
		range.width = 4;
		range.x = -2;
		range.y = -2;
	}

	/*
	 * this method returns the number of iterations it takes for 
	 * a specific point to converge to the given specifications
	 * for the Tricorn algorithm 
	 */
	public int numIterations(double x, double y) {

		int iters = 0;
		double re = x;
		double im = y;
		double zSquared = 0;

		while ((iters < MAX_ITERATIONS) && (zSquared < 4)) {

			iters++;

			// find the next Re and Im parts for next iteration
			double nextRe = x + ((re * re) - (im * im));
			double nextIm = y - (2 * re * im);

			re = nextRe;
			im = nextIm;

			// calculate zSquared to check exit condition 
			zSquared = re * re + im * im;

		}

		if (iters < MAX_ITERATIONS) {
			return iters;
		} else {
			return -1;
		}

	}

	// returns the object's string representation 
	public String toString() {

		return "Tricorn";
	}
}