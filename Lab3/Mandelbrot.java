import java.awt.geom.Rectangle2D;

/* 
 * The Mandelbrot class is a subclass of FractalGenerator and 
 * will maintain the implementation of the Mandelbrot Fractal
 */ 
public class Mandelbrot extends FractalGenerator {

	// define a maximum number of iterations for our algorithm
	public static final int MAX_ITERATIONS = 2000;

	/*
	 * this method just sets what part of the complex
	 * plane is the most interesting for the Mandelbrot 
	 * fractal
	 */
	public void getInitialRange(Rectangle2D.Double range) {

		range.height = 3;
		range.width = 3;
		range.x = -2;
		range.y = -1.5;
	}

	/*
	 * this method returns the number of iterations it takes for 
	 * a specific point to converge to the given specifications
	 * for the mandelbrot algorithm 
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
			double nextIm = y + (2 * re * im);

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

	public String toString() {

		return "Mandelbrot";
	}
}