package apryraz.eworld;

/**
 * This class is used to store the coordinates' matrix of the world
 */
public class EFState {
    /**
     * This class is used to store the coordinates' matrix of the world
     **/

    int wDim;
    String[][] matrix;

    /**
     * EFState constructor
     *
     * @param dim dimension of the matrix
     **/
    public EFState(int dim) {
        wDim = dim;
        matrix = new String[wDim][wDim];
        initializeState();
    }

    /**
     * It initializes the entire matrix with the value ?
     */
    public void initializeState() {
        for (int i = 0; i < wDim; i++) {
            for (int j = 0; j < wDim; j++) {
                matrix[i][j] = "?";
            }
        }
    }

    /**
     * i is the row, j the column
     * we check if i and j are given in the range [1,wDim]
     *
     * @param i   row
     * @param j   column
     * @param val value to be set on the coordinates i and j
     */
    public void set(int i, int j, String val) {

        if (i >= 1 && i <= wDim && j >= 1 && j <= wDim) {
            matrix[i - 1][j - 1] = val;
        } else {
            System.out.println("DEBUG => Error: set() " + i + " " + j);
        }
    }

    /**
     * Modified equals method
     *
     * @param obj object to compare
     **/
    public boolean equals(Object obj) {

        // Check the class type
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        EFState efstate2 = (EFState) obj;
        boolean status = true;

        for (int i = 0; i < wDim; i++) {
            for (int j = 0; j < wDim; j++) {
                if (!matrix[i][j].equals(efstate2.matrix[i][j])) {
                    status = false;
                    break;
                }
            }
        }

        return status;
    }

    /**
     * Prints the matrix
     **/
    public void printState() {
        System.out.println("FINDER => Printing Envelope world matrix");
        for (int i = wDim - 1; i > -1; i--) {
            System.out.print("\t#\t");
            for (int j = 0; j < wDim; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println("\t#");
        }
    }

}
