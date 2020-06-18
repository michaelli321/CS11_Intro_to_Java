import java.net.URL;
import java.net.MalformedURLException;

/**
 * This class holds the architecture of a URLDepthPair that will
 * be stored in a list in a Crawler object. Each URLDepthPair is
 * made up of a URL and depth. This class holds all URL string parsing
 * methods as well 
 */
public class URLDepthPair {

    /* url of the pair */
    private String url;

    /* depth of the pair */
    private int depth;

    /*
     * This is the constructor. Given a url and depth
     * we set those attributes 
     */
    public URLDepthPair(String url, int depth) {

        this.url = url;

        this.depth = depth;

    }

    /*
     * toString method that we override to provide
     * a string representation of our object
     */
    @Override
    public String toString() {

        return "URL: " + url + ", Depth: " + Integer.toString(depth);
    }

    /* 
     * returns the url 
     */
    public String getURL() {
        return url;
    }

    /* 
     * returns the depth 
     */
    public int getDepth() {
        return depth;
    }

    /* 
     * checks to see if the url 
     * is a valid one 
     */
    public boolean isValidURL() {

        try {
            new URL(url);
            return true;
        } 
        catch (MalformedURLException e) {
            return false;
        }
    }

    /* 
     * gets the host of the url
     */
    public String getHost() {

        try {
            URL aURL = new URL(url);
            return aURL.getHost();
        } 
        catch (MalformedURLException e) {
            System.out.println("Not a real URL");
            return "Error";
        }
        
    }

    /* 
     * get the path of the url
     */
    public String getPath() {

        try {
            URL aURL = new URL(url);
            return aURL.getPath();

        } 
        catch (MalformedURLException e) {
            System.out.println("Not a real URL");
            return "Error";

        }
    }

}