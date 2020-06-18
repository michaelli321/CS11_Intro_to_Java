import java.io.*;

/* 
 * This class uses the Point3d class and takes in as input 3 3D coordinates from the 
 * command line. It then computes the area of the trianglular plane given by those
 * 3 points and prints it out. It defines a getDouble function and computeArea 
 * function that help. 
 */
public class Lab1 {

    /** main function. computes the area of the triangle and prints it for the user */
    public static void main(String[] args) {

        // declare Point3d array to hold 3 triangle's points
        Point3d[] pointArr = new Point3d[3];

        // fill the pointArr
        for (int i = 0; i < 3; i++) {

            double x = getDouble();
            double y = getDouble();
            double z = getDouble();

            pointArr[i] = new Point3d(x, y, z);

        }

        // print the area 
        System.out.println("Area of Triangle: " + computeArea(pointArr[0], pointArr[1], pointArr[2]));

    }

    /**
     * Obtains one double-precision floating point number from
     * standard input.
     *
     * @return return the inputted double, or 0 on error.
     */
    public static double getDouble() {

        // There's potential for the input operation to "fail"; hard with a
        // keyboard, though!
        try {
            // Set up a reader tied to standard input.
            BufferedReader br =
                new BufferedReader(new InputStreamReader(System.in));

            // Read in a whole line of text.
            String s = br.readLine();

            // Conversion is more likely to fail, of course, if there's a typo.
            try {
                double d = Double.parseDouble(s);

                //Return the inputted double.
                return d;
            }
            catch (NumberFormatException e) {
                // Bail with a 0.  (Simple solution for now.)
                return 0.0;
            }
        }
        catch (IOException e) {
            // This should never happen with the keyboard, but we'll
            // conform to the other exception case and return 0 here,
            // too.
            return 0.0;
        }
    }

    /** Given three Point3d points, computes the area of the triangle */
    public static double computeArea(Point3d p1, Point3d p2, Point3d p3) {

        // calculate the lengths of the sides
        double s1 = p1.distanceTo(p2);
        double s2 = p2.distanceTo(p3);
        double s3 = p1.distanceTo(p3);

        // calculate the semiperimeter
        double s = (s1 + s2 + s3) / 2;

        return Math.sqrt(s * (s - s1) * (s - s2) * (s - s3));

    }

}
