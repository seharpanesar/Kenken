import java.util.ArrayList;

public class DFS {
    private int n;
    private Cell[][] cellGrid;
    private int[] prospectiveSoln;
    private int[][] solution;

    public DFS(int n, Cell[][] cellGrid) {
        this.n = n;
        this.cellGrid = cellGrid;
        prospectiveSoln = new int[n*n];
        solution = new int[n][n];
    }

    /* Uses recursive backtracking to find solution. stackNum is int from 0 -> n^2-1 that represents a cell in the
     * cell grid. Ex. stackNum = 0 represents top left cell, stackNum = 3 represents top right box, stackNum = 15
     * represents the bottom right box etc.
     */

    public boolean runBacktracking(int stackNum) {
        if (stackNum == n*n) { // base case: Solution found!
            fillSolution();
            return true;
        }

        //recursive backtracking

        // if 1 is available, place into prospectiveSoln and move on
        if (isAvailable(1, stackNum)) {
            prospectiveSoln[stackNum] = 1;
            if (cageIsClosed(stackNum)) {
                if (isValidArithmetic(stackNum)) {
                    boolean solnFound = runBacktracking(stackNum + 1);
                    if (solnFound) {
                        return true;
                    }
                }
            }
            else {
                boolean solnFound = runBacktracking(stackNum + 1);
                if (solnFound) {
                    return true;
                }
            }
            prospectiveSoln[stackNum] = 0;
        }

        // if 2 is available, place into prospectiveSoln and move on
        if (isAvailable(2, stackNum)) {
            prospectiveSoln[stackNum] = 2;
            if (cageIsClosed(stackNum)) {
                if (isValidArithmetic(stackNum)) {
                    boolean solnFound = runBacktracking(stackNum + 1);
                    if (solnFound) {
                        return true;
                    }
                }
            }
            else {
                boolean solnFound = runBacktracking(stackNum + 1);
                if (solnFound) {
                    return true;
                }
            }
            prospectiveSoln[stackNum] = 0;
        }

        // if 3 is available, place into prospectiveSoln and move on
        if (isAvailable(3, stackNum)) {
            prospectiveSoln[stackNum] = 3;
            if (cageIsClosed(stackNum)) {
                if (isValidArithmetic(stackNum)) {
                    boolean solnFound = runBacktracking(stackNum + 1);
                    if (solnFound) {
                        return true;
                    }
                }
            }
            else {
                boolean solnFound = runBacktracking(stackNum + 1);
                if (solnFound) {
                    return true;
                }
            }
            prospectiveSoln[stackNum] = 0;
        }

        // if 4 is available, place into prospectiveSoln and move on
        if (isAvailable(4, stackNum)) {
            prospectiveSoln[stackNum] = 4;
            if (cageIsClosed(stackNum)) {
                if (isValidArithmetic(stackNum)) {
                    boolean solnFound = runBacktracking(stackNum + 1);
                    if (solnFound) {
                        return true;
                    }
                }
            }
            else {
                boolean solnFound = runBacktracking(stackNum + 1);
                if (solnFound) {
                    return true;
                }
            }
            prospectiveSoln[stackNum] = 0;
        }

        return false;
    }

    private void fillSolution() {
        for (int i = 0; i < n; i++) {
            System.arraycopy(prospectiveSoln, i * n, solution[i], 0, n);
        }
    }

    private boolean isValidArithmetic(int stackNum) {
        int i = stackNum / n;
        int j = stackNum % n;
        Cage cage = cellGrid[i][j].getRespCage();
        ArrayList<Cell> cellsInCage = cage.getCells();

        ArrayList<Integer> candidate = new ArrayList<>();

        for (Cell cell : cellsInCage) {
            candidate.add(prospectiveSoln[cell.getXp()*n + cell.getYp()]);
        }

        char operation = cage.getOperation();
        int target = cage.getTargetVal();

        boolean candValid = switch (operation) {
            case '+' -> Kenken.addValid(target, candidate);
            case '-' -> Kenken.subValid(target, candidate);
            case '*' -> Kenken.multiValid(target, candidate);
            case '/' -> Kenken.divValid(target, candidate);
            case 'f' -> true;
            default -> false;
        };

        return candValid;
    }

    /* This function checks whether the cage is ready to be evaluated. If it is, we need to call ValidArithmetic to
     * verify if the cage arithmetic checks out.
     */

    private boolean cageIsClosed(int stackNum) {
        int i = stackNum / n;
        int j = stackNum % n;
        Cell cell = cellGrid[i][j];
        Cage cage = cell.getRespCage();
        ArrayList<Cell> cellsInCage = cage.getCells();

        int highestStackNumInCage = -1;

        for (Cell cellInCage : cellsInCage) {
            highestStackNumInCage = Math.max(cellInCage.getXp()*n + cellInCage.getYp(),
                    highestStackNumInCage);
        }

        return (highestStackNumInCage == stackNum);
    }

    /*
     *  This method immediately returns true/false if cell is already solved.
     *  Then it will only return true if it meets these conditions:
     *   1) x is one of the exists in candidates of the cage
     *   2) x is not a cannotBe of the cell
     *   3) x is not repeated amongst its row / column neighbors
     */

    private boolean isAvailable(int x, int stackNum) {
        int i = stackNum / n;
        int j = stackNum % n;
        Cell cell = cellGrid[i][j];
        ArrayList<ArrayList<Integer>> candidates = cell.getRespCage().getCandidates();

        //immediate return true/false condition if cell is solved
        if (cell.getSolution() != 0) {
            return cell.getSolution() == x;
        }

        //first condition
        boolean seen = false;
        for (ArrayList<Integer> list : candidates) {
            if (list.contains(x)) {
                seen = true;
                break;
            }
        }
        if (!seen) {
            return false;
        }

        //second condition
        ArrayList<Integer> cannotBe = cell.getCannotBe();
        if (cannotBe.contains(x)) {
            return false;
        }

        //third condition

        //row check
        for (int k = 0; k < n; k++) {
            if (k == j) { // this case refers to cell[i][j] which we don't need to check
                continue;
            }
            if (prospectiveSoln[i*n + k] == x) {
                return false;
            }
        }

        // column check
        for (int k = 0; k < n; k++) {
            if (k == i) { // this case refers to cell[i][j] which we don't need to check
                continue;
            }
            if (prospectiveSoln[k*n + j] == x) {
                return false;
            }
        }

        return true;
    }
}
