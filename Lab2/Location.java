/**
 * This class represents a specific location in a 2D map.  Coordinates are
 * integer values.
 **/
public class Location
{
    /** X coordinate of this location. **/
    public int xCoord;

    /** Y coordinate of this location. **/
    public int yCoord;


    /** Creates a new location with the specified integer coordinates. **/
    public Location(int x, int y)
    {
        xCoord = x;
        yCoord = y;
    }

    /** Creates a new location with coordinates (0, 0). **/
    public Location()
    {
        this(0, 0);
    }

    /** equals method to test two Point3ds for value-equality */
    @Override
    public boolean equals(Object obj) {
        // Is obj a Point3d?
        if (obj instanceof Location) {
            // Cast other object to Point3d type,
            // then compare.
            Location other = (Location) obj;
            if (xCoord == other.xCoord && yCoord == other.yCoord) {
                return true;
            }
        }

        // If we got here then they're not equal
        return false;
    }

    /** computes a hashcode based on object's values */
    @Override
    public int hashCode() {

        int result = 17; // some prime value

        // use another prime value to combine
        result = 37 * result + xCoord;
        result = 37 * result + yCoord;

        return result;

    }
}
