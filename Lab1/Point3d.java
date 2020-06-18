/**
 * A three-dimensional point class.
 */
public class Point3d {
    
    /** X coordinate of the point */
    private double xCoord;
    
    /** Y coordinate of the point */
    private double yCoord;

    /** Z coordinate of the point */
    private double zCoord;

    /** Constructor to initialize point to (x, y, z) value. */
    public Point3d(double x, double y, double z) {
        xCoord = x;
        yCoord = y;
        zCoord = z;
    }

    /** No-argument constructor:  defaults to a point at the origin. */
    public Point3d() {
        // Call three-argument constructor and specify the origin.
        this(0.0, 0.0, 0.0);
    }

    /** Return the X coordinate of the point. */
    public double getX() {
        return xCoord;
    }

    /** Return the Y coordinate of the point. */
    public double getY() {
        return yCoord;
    }

    /** Return the Z coordinate of the point. */
    public double getZ() {
        return zCoord;
    }

    /** Set the X coordinate of the point. */
    public void setX(double val) {
        xCoord = val;
    }

    /** Set the Y coordinate of the point. */
    public void setY(double val) {
        yCoord = val;
    }

    /** Set the Z coordinate of the point. */
    public void SetZ(double val) {
        zCoord = val;
    }

    /** equals method to test two Point3ds for value-equality */
    @override
    public boolean equals(Object obj) {
        // Is obj a Point3d?
        if (obj instanceof Point3d) {
            // Cast other object to Point3d type,
            // then compare.
            Point3d other = (Point3d) obj;
            if (xCoord == other.getX() && yCoord == other.getY() && zCoord == other.getZ()) {
                return true;
            }
        }

        // If we got here then they're not equal
        return false;
    }

    /** find the straight line distance between two Point3ds */
    public double distanceTo(Point3d p1) {

        // use distance formula to find distance
        double x = Math.pow(xCoord - p1.getX(), 2);
        double y = Math.pow(yCoord - p1.getY(), 2);
        double z = Math.pow(zCoord - p1.getZ(), 2);

        return Math.sqrt(x + y + z);
    }
}

