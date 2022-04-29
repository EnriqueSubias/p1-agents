package apryraz.eworld;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import static java.lang.System.exit;

import org.sat4j.specs.*;

import static org.junit.Assert.assertEquals;

import org.junit.*;

/**
 * Class for testing the TreasureFinder agent
 **/
public class EnvelopeFinderTest {


    /**
     * This function should execute the next step of the agent, and then assert
     * whether the resulting state is equal to the targetState
     *
     * @param eAgent      EnvelopeFinder agent
     * @param targetState the state that should be equal to the resulting state of
     *                    the agent after performing the next step
     * @throws ContradictionException in case of a contradiction adding clauses
     **/
    public void testMakeSimpleStep(EnvelopeFinder eAgent,
                                   EFState targetState) throws
            ContradictionException {
        // Check (assert) whether the resulting state is equal to
        //  the targetState after performing action runNextStep with bAgent

        eAgent.runNextStep();
        EFState currentState = eAgent.getState();

        System.out.println("EXPECTED: targetState:");
        targetState.printState();

        System.out.println("ACTUAL: eAgent.getState():");
        currentState.printState();

        assertEquals(targetState, currentState);

    }


    /**
     * Read a state from the current position of the file through the
     * BufferedReader object
     *
     * @param br   BufferedReader object interface to the opened file of states
     * @param wDim dimension of the world
     * @return the state read from the file
     * @throws IOException in case of error reading the files
     **/
    public EFState readTargetStateFromFile(BufferedReader br, int wDim) throws
            IOException {
        EFState efstate = new EFState(wDim);
        String row;
        String[] rowvalues;

        for (int i = wDim; i >= 1; i--) {
            row = br.readLine();
            rowvalues = row.split(" ");
            for (int j = 1; j <= wDim; j++) {
                efstate.set(i, j, rowvalues[j - 1]);
            }
        }
        return efstate;
    }

    /**
     * Load a sequence of states from a file, and return the list
     *
     * @param wDim       dimension of the world
     * @param numStates  num of states to read from the file
     * @param statesFile file name with sequence of target states, that should
     *                   be the resulting states after each movement in fileSteps
     * @return returns an ArrayList of TFState with the resulting list of states
     **/
    ArrayList<EFState> loadListOfTargetStates(int wDim, int numStates, String statesFile) {

        ArrayList<EFState> listOfStates = new ArrayList<>(numStates);

        try {
            BufferedReader br = new BufferedReader(new FileReader(statesFile));
            System.out.println("Reading states from file: " + statesFile);

            for (int s = 0; s < numStates; s++) {
                listOfStates.add(readTargetStateFromFile(br, wDim));
                // Read a blank line between states
                br.readLine();
            }
            br.close();
        } catch (FileNotFoundException ex) {
            System.out.println("MSG.   => States file not found");
            exit(1);
        } catch (IOException ex) {
            Logger.getLogger(EnvelopeFinderTest.class.getName()).log(Level.SEVERE, null, ex);
            exit(2);
        }

        return listOfStates;
    }


    /**
     * This function should run the sequence of steps stored in the file fileSteps,
     * but only up to numSteps steps.
     *
     * @param wDim          the dimension of world
     * @param numSteps      num of steps to perform
     * @param fileSteps     file name with sequence of steps to perform
     * @param fileStates    file name with sequence of target states, that should
     *                      be the resulting states after each movement in fileSteps
     * @param fileEnvelopes file name with sequence of envelopes
     * @throws ContradictionException in case of a contradiction adding clauses
     **/
    public void testMakeSeqOfSteps(int wDim,
                                   int numSteps, String fileSteps, String fileStates,
                                   String fileEnvelopes)
            throws ContradictionException {
        // You should make TreasureFinder and TreasureWorldEnv objects to  test.
        // Then load sequence of target states, load sequence of steps into the eAgent
        // and then test the sequence calling testMakeSimpleStep once for each step.
        EnvelopeFinder eAgent = new EnvelopeFinder(wDim);
        // load information about the World into the EnvAgent
        EnvelopeWorldEnv envAgent = new EnvelopeWorldEnv(wDim, fileEnvelopes);
        // Load list of states
        ArrayList<EFState> seqOfStates = loadListOfTargetStates(wDim, numSteps, fileStates);


        // Set environment agent and load list of steps into the finder agent
        eAgent.loadListOfSteps(numSteps, fileSteps);
        eAgent.setEnvironment(envAgent);

        // Test here the sequence of steps and check the resulting states with the
        // ones in seqOfStates

        for (int i = 0; i < numSteps; i++) {
            testMakeSimpleStep(eAgent, seqOfStates.get(i));
        }
    }

    /**
     * This is an example test. You must replicate this method for each different
     * test sequence, or use some kind of parametric tests with junit
     *
     * @throws ContradictionException in case of a contradiction adding clauses
     **/
    @Test
    public void TWorldTest0() throws ContradictionException {
        // Test for 5x5 world , Treasure at 3,3 and 5 steps
        testMakeSeqOfSteps(5, 5, "tests/steps1.txt", "tests/states1.txt", "tests/envelopes1.txt");
    }

    /**
     * This is the first test.
     * test1 (states1.txt steps1.txt envelopes1.txt):  5x5 world,  5 steps ,  envelopes at  2,2 4,4
     *
     * @throws ContradictionException in case of a contradiction adding clauses
     **/
    @Test
    public void TWorldTest1() throws ContradictionException {
        testMakeSeqOfSteps(5, 5, "tests/steps1.txt", "tests/states1.txt", "tests/envelopes1.txt");
    }

    /**
     * This is the second test.
     * test2 (states2.txt steps2.txt envelopes2.txt): 5x5 world,  7 steps , envelopes at  3,2 3,4
     *
     * @throws ContradictionException in case of a contradiction adding clauses
     **/
    @Test
    public void TWorldTest2() throws ContradictionException {
        testMakeSeqOfSteps(5, 7, "tests/steps2.txt", "tests/states2.txt", "tests/envelopes2.txt");
    }

    /**
     * This is the third test.
     * test3 (states3.txt steps3.txt envelopes3.txt): 7x7 world,  6 steps,  envelopes at  3,2 4,4 2,6
     *
     * @throws ContradictionException in case of a contradiction adding clauses
     **/
    @Test
    public void TWorldTest3() throws ContradictionException {
        testMakeSeqOfSteps(7, 6, "tests/steps3.txt", "tests/states3.txt", "tests/envelopes3.txt");
    }

    /**
     * This is the fourth test.
     * test4 (states4.txt steps4.txt envelopes4.txt): 7x7 world,  12 steps , envelopes at  6,2 4,4 2,6
     *
     * @throws ContradictionException in case of a contradiction adding clauses
     **/
    @Test
    public void TWorldTest4() throws ContradictionException {
        testMakeSeqOfSteps(7, 12, "tests/steps4.txt", "tests/states4.txt", "tests/envelopes4.txt");
    }
}
