import java.net.*;
import java.io.*;
import java.util.*;
import java.net.Socket;

/** 
 * This class creates a Crawler object and will go through a given website and
 * depth and find all links to that given depth on the webiste and print all
 * the links that it finds. The website and depth are given through the command line
 * in that respective order
 */
public class Crawler {

    public static final String LINE_PREFIX = "<a href=\"http://";
    public static final String URL_PREFIX = "http://";

    // keeps track of to process URLs
    private LinkedList<URLDepthPair> pendingURLs;

    // keeps track of URLs that have already been processed
    private LinkedList<URLDepthPair> processedURLs;

    // keeps track of the max depth given on command line
    private int maximumDepth;

    // keeps track of the first url given on command line
    private String firstUrl;

    /* 
     * constructor for Crawler object, just 
     * instantiates everything 
     */
    public Crawler(int mD, String fU) {

        pendingURLs = new LinkedList<URLDepthPair>();
        processedURLs = new LinkedList<URLDepthPair>();
        maximumDepth = mD;
        firstUrl = fU;
    }

    /*
     * usage statement. used when given an illegal argument on
     * the command line
     */
    public static void usage() {
        System.out.println("usage: java Crawler <URL> <depth>");
        System.exit(1);
    }

    public Socket makeConnection(URLDepthPair nextPair) {

        try {

            // create a socket to connect with 
            Socket sock = new Socket(nextPair.getHost(), 80);
            sock.setSoTimeout(3000);
            OutputStream os = sock.getOutputStream(); 

            PrintWriter writer = new PrintWriter(os, true);
            writer.println("GET " + nextPair.getPath() + " HTTP/1.1\r"); 
            writer.println("Host: " + nextPair.getHost() + "\r");
            writer.println("Connection: close\r");
            writer.println("\r"); 

            return sock;

        } catch (IOException e) {

            System.out.println("Couldn't create socket");
            System.exit(1);
            return null;

        }

    }

    /** 
     * this function is the main function that does the crawling.
     * it connects to multiple links by using a Socket and alters
     * pendingURLs and processedURLs lists as it works
     */
    public void crawl() {

        try {

            // create the first link to check
            URLDepthPair first = new URLDepthPair(firstUrl, 0);
            pendingURLs.add(first); 

            // make sure it is valid
            if (!first.isValidURL()) {
                System.out.println("The first argument must be a valid URL");
                usage();
            }

            // while we have more urls to look at 
            while (!pendingURLs.isEmpty()) {

                // grab the first url
                URLDepthPair nextURLPair = pendingURLs.removeFirst(); 

                Socket sock = makeConnection(nextURLPair);

                InputStream is = sock.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);

                // set the depth 
                int depth = nextURLPair.getDepth();

                // if we're at the maximum depth, stop
                if (depth == maximumDepth) {
                    break;
                }

                // read the page
                while (true) {

                    String line = br.readLine();

                    if (line == null) {
                        break; // Done reading document!
                    }

                    // check this line for LINE_PREFIX
                    if (line.toLowerCase().contains(LINE_PREFIX.toLowerCase())) {

                        // find the start and end of the url
                        int idxStart = line.indexOf(URL_PREFIX);
                        int idxEnd = line.indexOf("\"", idxStart);

                        // create a new URLDepthPair to add to pending URLs
                        URLDepthPair next = new URLDepthPair(line.substring(idxStart, idxEnd), depth+1);
                        pendingURLs.add(next);
                        
                    }

                }

                // add the just processedURL to the processed URL list
                processedURLs.add(nextURLPair);


            }

        } catch (IOException e) {
            System.out.println("Couldn't create Buffer");
            System.exit(1);
        }


    }

    /**
     * This function simply prints all the URLDepthPairs
     * stored in the processedURL list 
     */ 
    public void getSites() {

        for (int i = 0; i < processedURLs.size(); i++) {
            System.out.println(processedURLs.get(i));
        }

    }

    /** 
     * This is the main function. It will make sure all command line 
     * arguments are correct and will then call the crawl and getSites
     * function to make the program work and print all the sites crawled on
     */
    public static void main(String[] args) {

        //// Patch to get crawler working on most sites ========================
        // System.setProperty("line.separator", "\r\n");
        
        // make sure we only had 2 arguments
        if (args.length != 2) {
            usage();
        } 

        String url;
        int maxDepth = 0;

        // make sure the 2nd argument is an integer
        try {
            // Parse the string argument into an integer value.
            maxDepth = Integer.parseInt(args[1]);
        } 
        catch (NumberFormatException nfe) {
            // The second argument isn't a valid integer.  Print
            // an error message, then exit with an error code.
            System.out.println("The second argument must be an integer.");
            usage();
        }

        // make sure it's positive
        if (maxDepth < 0) {
            usage();
        }

        // first argument is url
        url = args[0];

        Crawler crawling = new Crawler(maxDepth, url);

        crawling.crawl();

        crawling.getSites();
        
    }
}

    
