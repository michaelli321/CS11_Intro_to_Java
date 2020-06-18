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
