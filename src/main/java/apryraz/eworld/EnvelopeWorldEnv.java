

package apryraz.eworld;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.System.exit;

/**
 * Environment used to store and interact with the Envelope World.
 */
public class EnvelopeWorldEnv {
    /**
     * world dimension
     **/
    int WorldDim;

    /**
     * The list of steps to perform
     **/
    ArrayList<Position> listOfEnvelopes;

    /**
     * Class constructor
     *
     * @param dim          dimension of the world
     * @param envelopeFile File with list of envelopes locations
     **/
    public EnvelopeWorldEnv(int dim, String envelopeFile) {

        WorldDim = dim;
        loadEnvelopeLocations(envelopeFile);
    }

    /**
     * Load the list of pirates locations
     * <p>
     * The file should contain a set of envelope locations in a single line
     * With the following format: x1,y1 x2,y2 x3,y3 ... xn,yn
     * where xi,yi are the coordinates of a location with an envelope
     * For example: 3,2 4,4 2,6
     * means that there are 3 locations with envelopes, and they are
     * at positions (3,2), (4,4) and (2,6)
     *
     * @param envelopeFile name of the file that should contain a
     *                     set of envelope locations in a single line.
     **/
    public void loadEnvelopeLocations(String envelopeFile) {

        System.out.println("Loading envelope locations from file: " + envelopeFile);

        String[] envelopeLocationsList;
        String envelopes = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(envelopeFile));
            System.out.println("ENVELOPES FILE OPENED ... " + envelopeFile);
            envelopes = br.readLine();
            br.close();
        } catch (FileNotFoundException ex) {
            System.out.println("MSG.   => Envelopes file not found");
            exit(1);
        } catch (IOException ex) {
            Logger.getLogger(EnvelopeFinder.class.getName()).log(Level.SEVERE, null, ex);
            exit(2);
        }
        envelopeLocationsList = envelopes.split(" ");
        listOfEnvelopes = new ArrayList<>(envelopeLocationsList.length);
        for (String envelopeLocation : envelopeLocationsList) {
            String[] coords = envelopeLocation.split(",");
            listOfEnvelopes.add(new Position(Integer.parseInt(coords[0]), Integer.parseInt(coords[1])));
        }
    }


    /**
     * Process a message received by the EFinder agent,
     * by returning an appropriate answer
     * It should answer to moveto and detectsat messages
     *
     * @param msg message sent by the Agent
     * @return a msg with the answer to return to the agent
     **/
    public AMessage acceptMessage(AMessage msg) {
        AMessage ans;

        msg.showMessage();
        if (msg.getComp(0).equals("moveto")) {
            int nx = Integer.parseInt(msg.getComp(1));
            int ny = Integer.parseInt(msg.getComp(2));

            if (withinLimits(nx, ny)) {


                ans = new AMessage("movedto", msg.getComp(1), msg.getComp(2), "");
            } else
                ans = new AMessage("notmovedto", msg.getComp(1), msg.getComp(2), "");

        } else if (msg.getComp(0).equals("detectsat")) {

            // YOU MUST ANSWER HERE TO THE OTHER MESSAGE TYPE:
            //   ( "detectsat", "x" , "y", "" )

            //  The number detects encodes the following valid readings of the sensor:
            //    0: no reading
            //    1: reading of an envelope in the edge positions (x+1,y),(x−1,y),(x,y−1),(x,y+1)
            //    2: reading of an envelope in the corner positions (x−1,y−1),(x+1,y−1),(x−1,y+1),(x+1,y+1)
            //    3: reading of an envelope in the center position (x,y)
            int nx = Integer.parseInt(msg.getComp(1));
            int ny = Integer.parseInt(msg.getComp(2));
            String reading = "0";

            if (envelopeAtEdge(nx, ny)) {
                reading = "1";
                ans = new AMessage("detectedat", msg.getComp(1), msg.getComp(2), reading);
            } else if (envelopeAtCorner(nx, ny)) {
                reading = "2";
                ans = new AMessage("detectedat", msg.getComp(1), msg.getComp(2), reading);
            } else if (envelopeAtCenter(nx, ny)) {
                reading = "3";
                ans = new AMessage("detectedat", msg.getComp(1), msg.getComp(2), reading);
            } else { // no envelope detected in the 3x3 neighborhood
                ans = new AMessage("notdetectedat", msg.getComp(1), msg.getComp(2), reading);
            }

        } else {
            ans = new AMessage("voidmsg", "", "", "");
        }
        return ans;

    }

    // edge positions (x+1,y),(x−1,y),(x,y−1),(x,y+1)
    // corner positions (x−1,y−1),(x+1,y−1),(x−1,y+1),(x+1,y+1)
    // center position (x,y)

    /**
     * Check if there is an envelope in the edges of
     * the 3x3 neighborhood of the position (nx,ny)
     * check drawing provided in the assignment document
     * <p>
     * edge positions (x+1,y),(x−1,y),(x,y−1),(x,y+1)
     *
     * @param nx x coordinate of agent position
     * @param ny y coordinate of agent position
     * @return true if (nx,ny) has an envelope in some edge of the 3x3 neighborhood
     **/
    public boolean envelopeAtEdge(int nx, int ny) {
        for (Position pos : listOfEnvelopes) {
            if (pos.x == nx + 1 && pos.y == ny) return true;
            if (pos.x == nx - 1 && pos.y == ny) return true;
            if (pos.x == nx && pos.y == ny + 1) return true;
            if (pos.x == nx && pos.y == ny - 1) return true;
        }
        return false;
    }

    /**
     * Check if there is an envelope in the corners of
     * the 3x3 neighborhood of the position (nx,ny)
     * check drawing provided in the assignment document
     * <p>
     * corner positions (x−1,y−1),(x+1,y−1),(x−1,y+1),(x+1,y+1)
     *
     * @param nx x coordinate of agent position
     * @param ny y coordinate of agent position
     * @return true if (nx,ny) has an envelope in some corner of the 3x3 neighborhood
     */
    public boolean envelopeAtCorner(int nx, int ny) {
        for (Position pos : listOfEnvelopes) {
            if (pos.x == nx - 1 && pos.y == ny - 1) return true;
            if (pos.x == nx + 1 && pos.y == ny - 1) return true;
            if (pos.x == nx - 1 && pos.y == ny + 1) return true;
            if (pos.x == nx + 1 && pos.y == ny + 1) return true;
        }
        return false;
    }

    /**
     * Check if there is an envelope in the center of
     * the 3x3 neighborhood of the position (nx,ny)
     * check drawing provided in the assignment document
     * <p>
     * center position (x,y)
     *
     * @param nx x coordinate of agent position
     * @param ny y coordinate of agent position
     * @return true if (nx,ny) has an envelope in the center of the 3x3 neighborhood
     */
    public boolean envelopeAtCenter(int nx, int ny) {
        for (Position pos : listOfEnvelopes) {
            if (pos.x == nx && pos.y == ny) return true;
        }
        return false;
    }

    /**
     * Check if position x,y is within the limits of the
     * WorldDim x WorldDim   world
     *
     * @param x x coordinate of agent position
     * @param y y coordinate of agent position
     * @return true if (x,y) is within the limits of the world
     **/
    public boolean withinLimits(int x, int y) {

        return (x >= 1 && x <= WorldDim && y >= 1 && y <= WorldDim);
    }

}
