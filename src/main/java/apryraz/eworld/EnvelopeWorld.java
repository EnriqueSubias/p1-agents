package apryraz.eworld;

import org.sat4j.specs.*;
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
     * @throws ContradictionException if there is a contradiction in the formula
     **/
    public static void runStepsSequence(int wDim, int numSteps, String fileSteps, String fileEnvelopes)
            throws ContradictionException {
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
     * This function should load five arguments from the command line,
     * it accepts the following arguments, that can be passed as 4 separate arguments or as a single string
     * Usage with 4 separate arguments:
     * java EnvelopeWorld dimension numSteps fileSteps fileEnvelopes
     * Usage with a single string:
     * mvn exec:java -Dexec.args="dimension numSteps fileSteps fileEnvelopes"
     *
     * @param args the command line arguments
     *             arg[0] = dimension of the word
     *             arg[1] = num of steps to perform
     *             arg[2] = file name with sequence of steps to perform
     *             arg[3] = file name with list of envelopes positions
     * @throws ParseFormatException   if the arguments are not in the correct format according to the usage description
     * @throws ContradictionException if there is a contradiction in the formula
     **/
    public static void main(String[] args) throws ParseFormatException, ContradictionException {

        // Here I run a concrete example, but you should read parameters from
        // the command line, as described above.
        // runStepsSequence(  4, 5, "tests/steps1.txt", "tests/envelopes1.txt"  );

        // If a string is given as an input, separate it into the 4 arguments
        // and call runStepsSequence()

        System.out.println("args.length = " + args.length);

        if (args.length == 1) {
            // If mvn exec is used, the input is a string
            System.out.println("Input string: " + args[0]);
            String[] arg = args[0].split(" ");
            runStepsSequence(Integer.parseInt(arg[0]), Integer.parseInt(arg[1]), arg[2], arg[3]);
        } else if (args.length != 4) {
            System.out.println("Usage: java EnvelopeWorld <dimension> <numSteps> <fileSteps> <fileEnvelopes>");
            throw new ParseFormatException("Wrong number of arguments, check Usage");
        } else {

            // load 4 arguments from the command line
            int wDim = Integer.parseInt(args[0]);
            int numSteps = Integer.parseInt(args[1]);
            String fileSteps = args[2];
            String fileEnvelopes = args[3];

            System.out.println("wDim = " + wDim);
            System.out.println("numSteps = " + numSteps);
            System.out.println("fileSteps = " + fileSteps);
            System.out.println("fileEnvelopes = " + fileEnvelopes);

            // run the sequence of steps
            runStepsSequence(wDim, numSteps, fileSteps, fileEnvelopes);
        }
    }

}
