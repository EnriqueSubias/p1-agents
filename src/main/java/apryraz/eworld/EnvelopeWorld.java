package apryraz.eworld;


import java.io.IOException;

import org.sat4j.specs.*;
import org.sat4j.minisat.*;
import org.sat4j.reader.*;


/**
 * The class for the main program of the Barcenas World
 **/
public class EnvelopeWorld {


    /**
     * This function should execute the sequence of steps stored in the file fileSteps,
     * but only up to numSteps steps. Each step must be executed with function
     * runNextStep() of the BarcenasFinder agent.
     *
     * @param wDim          the dimension of world
     * @param numSteps      num of steps to perform
     * @param fileSteps     file name with sequence of steps to perform
     * @param fileEnvelopes file name with sequence of steps to perform
     **/
    public static void runStepsSequence(int wDim,
                                        int numSteps, String fileSteps, String fileEnvelopes) throws
            IOException, ContradictionException, TimeoutException {
        // Make instances of TreasureFinder agent and environment object classes
        EnvelopeFinder EAgent;
        EnvelopeWorldEnv EnvAgent;
        EAgent = new EnvelopeFinder(wDim);
        EnvAgent = new EnvelopeWorldEnv(wDim, fileEnvelopes);

        // save environment object into EAgent
        EAgent.setEnvironment(EnvAgent);

        // load list of steps into the Finder Agent
        EAgent.loadListOfSteps(numSteps, fileSteps);

        // Execute sequence of steps with the Agent
        for (int i = 0; i < numSteps; i++) EAgent.runNextStep();

    }

    /**
     * This function should load five arguments from the command line:
     * arg[0] = dimension of the word
     * arg[3] = num of steps to perform
     * arg[4] = file name with sequence of steps to perform
     * arg[5] = file name with list of envelopes positions
     **/
    public static void main(String[] args) throws ParseFormatException,
            IOException, ContradictionException, TimeoutException {

        // Here I run a concrete example, but you should read parameters from
        // the command line, as described above.
        // runStepsSequence(  4, 5, "tests/steps1.txt", "tests/envelopes1.txt"  );

        if (args.length != 6) {
            System.out.println("Usage: java EnvelopeWorld <dimension> <numSteps> <fileSteps> <fileEnvelopes>");
            throw new ParseFormatException("Wrong number of arguments, check Usage");
        }

        // load 4 arguments from the command line
        // NOTE: I don't know what the arguments 1 and 2 are for, so I used only 4 arguments
        int wDim = Integer.parseInt(args[0]);
        int numSteps = Integer.parseInt(args[3]);
        String fileSteps = args[4];
        String fileEnvelopes = args[5];

        System.out.println("wDim = " + wDim);
        System.out.println("numSteps = " + numSteps);
        System.out.println("fileSteps = " + fileSteps);
        System.out.println("fileEnvelopes = " + fileEnvelopes);

        // run the sequence of steps
        runStepsSequence(wDim, numSteps, fileSteps, fileEnvelopes);

    }

}
