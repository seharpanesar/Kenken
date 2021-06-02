import java.io.*;
import java.util.ArrayList;

public class Kenken {
    int n; // length of height/width of grid
    ArrayList<Cage> cages = new ArrayList<>();
    Cell[][] cellGrid = new Cell[0][];


    public Kenken(String filePath) throws IOException {
        long timeS = System.currentTimeMillis();
        parseInput(filePath);
        fillAnswerGrid();
        long timeE = System.currentTimeMillis();
        System.out.printf("Time: %d ms\n", timeE-timeS);
    }

    private void printCurrentSolution() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.printf("%d ", cellGrid[i][j].getSolution());
            }
            System.out.println();
        }
        System.out.println();
    }

    private void fillAnswerGrid() {
        // setting candidates and find freebies
        for (Cage cage : cages) {
            cage.setCandidates(candidatesAndFreebies(cage));
        }

        int i = 0;

        while (true) {
            // narrowing candidates strategies
            removeCandidateStrat1(); // see comment above function header to understand the strategy
            printCurrentSolution();
            removeCandidateStrat2();
            printCurrentSolution();

            // identifying solution strategies
            identifySolutionStrat1();
            printCurrentSolution();
            identifySolutionStrat2();
            printCurrentSolution();

            if (i == 3) {
                break;
            }
            i++;
        }
    }

    /* This function iterates through all cages. If all cells in a particular cage have the same value in the cannotBe list
     * AND a candidate in the cage uses that cannotBe value, remove that candidate.
     */

    private void removeCandidateStrat1() {
        for (Cage cage : cages) {
            ArrayList<Integer> cannotBe = cage.getCells().get(0).getCannotBe();
            ArrayList<Cell> cells = cage.getCells();

            ArrayList<ArrayList<Integer>> candidates = cage.getCandidates();

            ArrayList<ArrayList<Integer>> candidatesToRemove = new ArrayList<>(); // every int placed in this CANNOT exist within the cage

            for (Integer integer : cannotBe) {
                boolean continueFlag = true;
                for (Cell cell : cells) {
                    if (!cell.getCannotBe().contains(integer)) {
                        continueFlag = false;
                        break;
                    }
                }

                // if some cells do not have "integer" in cannotBe, we skip to the next integer.
                if (!continueFlag) {
                    continue;
                }

                for (ArrayList<Integer> candidate : candidates) {
                    if (candidate.contains(integer)) {
                        candidatesToRemove.add(candidate);
                    }
                }
            }

            // nothing to remove
            if (candidatesToRemove.size() == 0) {
                continue;
            }

            for (ArrayList<Integer> toRemove : candidatesToRemove) {
                cage.removeCandidate(toRemove);
            }
        }
    }

    /* If a cell indicates that it cannot be x and it cannot be y and (x,y) is a candidate for its cage, remove that
     * candidate.
     */

    private void removeCandidateStrat2() {
        // lots of nested for loops, but it isn't too inefficient because the size of the arrays aren't big
        for (Cage cage: cages) {
            ArrayList<ArrayList<Integer>> candidates = cage.getCandidates();
            ArrayList<Cell> cells = cage.getCells();

            ArrayList<ArrayList<Integer>> candidatesToRemove = new ArrayList<>();

            for (Cell cell : cells) {
                for (ArrayList<Integer> candidateArray : candidates) {
                    boolean removeCandidate = true;
                    for (Integer candidateElement : candidateArray) {
                        if (!cell.getCannotBe().contains(candidateElement)) {
                            removeCandidate = false;
                            break;
                        }
                    }

                    /* if all candidates elements within a candidate array exist in the cannotBe of a cell, we
                     * must remove that candidate from the cage.
                     */

                    if (removeCandidate) {
                        candidatesToRemove.add(candidateArray);
                    }
                }
            }

            /* actually removing that candidate. If we removed if in the above loop, there would have been a
               concurrent mod exception thrown
             */
            for (ArrayList<Integer> toRemove : candidatesToRemove) {
                cage.removeCandidate(toRemove);
            }
        }
    }

    /* For every row and column in cellGrid, this function will iterate from i = 1 -> n and scan how many times i shows
     * up in the candidates of each cell within the row/column. If i only appears once in all cell candidates, i must be
     * the solution for the cell it appears in
     */

    private void identifySolutionStrat1() {
        // scanning row by row
        for (int i = 0; i < n; i++) {
            for (int maybeOnlyCand = 1; maybeOnlyCand <= n; maybeOnlyCand++) {
                boolean alreadySolvedFlag = false;
                int count = 0;
                Cell lastSeenIn = null;
                for (int j = 0; j < n; j++) {
                    if (cellGrid[i][j].getSolution() == maybeOnlyCand) { // if maybeOnlyCand is solved for this row, skip to next maybeOnlyCan
                        alreadySolvedFlag = true;
                        break;
                    }
                    ArrayList<ArrayList<Integer>> cellCandidates = cellGrid[i][j].getRespCage().getCandidates();
                    for (ArrayList<Integer> candidate : cellCandidates) {
                        if (candidate.contains(maybeOnlyCand)) {
                            count++;
                            lastSeenIn = cellGrid[i][j];
                            break;
                        }
                    }
                    if (count > 1) { // optimization to save some iterations
                        break;
                    }
                }
                if (alreadySolvedFlag) { // no need to iterate through row for this maybeOnlyCandidate, so skip
                    continue;
                }
                if (count == 1 && lastSeenIn.getSolution() == 0) { // solution found
                    setSoln(maybeOnlyCand, lastSeenIn);
                }
            }
        }

        //scanning column by column
        for (int i = 0; i < n; i++) {
            for (int maybeOnlyCand = 1; maybeOnlyCand <= n; maybeOnlyCand++) {
                boolean alreadySolvedFlag = false;
                int count = 0;
                Cell lastSeenIn = null;
                for (int j = 0; j < n; j++) {
                    if (cellGrid[j][i].getSolution() == maybeOnlyCand) { // if maybeOnlyCand is solved for this row, skip to next maybeOnlyCan
                        alreadySolvedFlag = true;
                        break;
                    }
                    // condition is DIFFERENT FROM ABOVE: [j][i] instead of [i][j]. this allows column traversal instead of row traversal.
                    ArrayList<ArrayList<Integer>> cellCandidates = cellGrid[j][i].getRespCage().getCandidates();
                    for (ArrayList<Integer> candidate : cellCandidates) {
                        if (candidate.contains(maybeOnlyCand)) {
                            count++;
                            lastSeenIn = cellGrid[j][i];
                            break; // dont want to count a a candidate twice. Ex. if a cell has (1,2) and (2,4) as candidates, 2 should only be counted once.
                        }
                    }
                    if (count > 1) {
                        break;
                    }
                }
                if (alreadySolvedFlag) {
                    continue;
                }
                if (count == 1 && lastSeenIn.getSolution() == 0) {
                    setSoln(maybeOnlyCand, lastSeenIn);
                }
            }
        }
    }

    /* Process of elimination:
     *  1) If a cell has n-1 distinct cannotBe's, the solution must be the missing value.
     *  2) If a cell has a set of candidates and all but 1 of the candidate elements are included in the cannotBe,
     *     the solution must be the missing value.
     */

    private void identifySolutionStrat2() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                Cell cell = cellGrid[i][j];
                if (cell.getSolution() != 0) { // solution is already found, so skip
                    continue;
                }

                ArrayList<Integer> cannotBe = cell.getCannotBe();

                if (cell.getCannotBe().size() == n - 1) { // there are n-1 cannotBes. if true, we need to find the missing value
                    // finding missing value by totalSum - cannotBe sum
                    int totalSum = n*(n+1)/2;
                    int cannotBeSum = 0;
                    for (Integer integer : cannotBe) {
                        cannotBeSum += integer;
                    }
                    int solution = totalSum - cannotBeSum;
                    setSoln(solution, cell);
                }
            }
        }
    }

    private ArrayList<ArrayList<Integer>> candidatesAndFreebies(Cage cage) {
        ArrayList<ArrayList<Integer>> candidates = new ArrayList<>();

        int target = cage.getTargetVal();
        char operation = cage.getOperation();
        int numCells = cage.getCells().size();

        // freebie case
        if (operation == 'f') {
            Cell solvedCell = cage.getCells().get(0);
            setSoln(target, solvedCell);
            return candidates;
        }

        // this places C(n, cage.getCells.size()) arraylists in generateAllCandidates

        if (numCells == 2 || isStraight(cage)) {
            generate2CellCandidates(candidates, new int[numCells], 1, n, 0);
        } else {
            generateManyCellCandidates(candidates, n, numCells);
        }


        ArrayList<ArrayList<Integer>> toRemove = new ArrayList<>();

        // loop identifies which candidates to remove using operation. Removing them in this step throws concurrentMod Exception
        for (ArrayList<Integer> candidate : candidates) {
            boolean candValid = switch (operation) {
                case '+' -> addValid(target, candidate);
                case '-' -> subValid(target, candidate);
                case '*' -> multiValid(target, candidate);
                case '/' -> divValid(target, candidate);
                default -> false;
            };
            if (!candValid) {
                toRemove.add(candidate);
            }
        }

        // loop identifies candidates with all same numbers. rare case, but needs to be weeded out
        for (ArrayList<Integer> candidate: candidates) {
            if (allSame(candidate)) {
                toRemove.add(candidate);
            }
        }

        // loop actually takes out invalid candidates.
        for (ArrayList<Integer> invalidCand : toRemove) {
            candidates.remove(invalidCand);
        }

        return candidates;
    }

    private boolean isStraight(Cage cage) {
        ArrayList<Cell> cells = cage.getCells();
        boolean xChanges = false;
        boolean yChanges = false;

        assert cells.size() > 2;

        int initX = cells.get(0).getXp();
        int initY = cells.get(1).getYp();

        for (int i = 1; i < cells.size(); i++) {
            Cell cell = cells.get(i);
            if (initX - cell.getXp() != 0) {
                xChanges = true;
            }
            if (initY - cell.getYp() != 0) {
                yChanges = true;
            }
        }

        return !xChanges || !yChanges;
    }

    private boolean allSame(ArrayList<Integer> candidate) {
        int num = candidate.get(0);
        for (int i = 1; i < candidate.size(); i++) {
            if (num != candidate.get(i)) {
                return false;
            }
        }
        return true;
    }

    private void CombinationRepetitionUtil(ArrayList<ArrayList<Integer>> toStoreIn, int[] chosen, int[] arr,
                                          int index, int r, int start, int end) {
        // Since index has become r, current combination is
        // ready to be printed, print
        if (index == r) {
            ArrayList<Integer> candidate = new ArrayList<>();
            for (int i = 0; i < r; i++) {
                candidate.add(arr[chosen[i]]);
            }
            toStoreIn.add(candidate);
            return;
        }

        // One by one choose all elements (without considering
        // the fact whether element is already chosen or not)
        // and recur
        for (int i = start; i <= end; i++) {
            chosen[index] = i;
            CombinationRepetitionUtil(toStoreIn, chosen, arr, index + 1,
                    r, i, end);
        }
    }

    // The main function that stores all combinations of size r
    // in arr[] of size n with repetitions. This function mainly
    // uses CombinationRepetitionUtil()
    private void generateManyCellCandidates(ArrayList<ArrayList<Integer>> toStoreIn, int n, int r) {
        // Allocate memory
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = i + 1;
        }
        int[] chosen = new int[r + 1];


        // Call the recursive function
        CombinationRepetitionUtil(toStoreIn, chosen, arr, 0, r, 0, n - 1);
    }

    /*
      Generated this algorithm with the help of baeldung.com/java-combinations-algorithm.
      This combinations algorithm is quite elegant, but the recursion takes a while to understand.
     */

    private void generate2CellCandidates(ArrayList<ArrayList<Integer>> candidates, int[] data,
                                       int start, int end, int index) {
        if (index == data.length) {
            candidates.add(toArrList(data));
        } else if (start <= end) {
            data[index] = start;
            generate2CellCandidates(candidates, data, start + 1, end, index + 1);
            generate2CellCandidates(candidates, data, start + 1, end, index);
        }
    }

    /* This function makes target the solution of solvedCell. It also updates the cannotBe's of neighboring cells and
     * remove candidates that do not use target in this cage.
     */


    private void setSoln(int target, Cell solvedCell) {
        solvedCell.setSolution(target);

        //updating cannotBe of neighbor cells

        int xOfSolved = solvedCell.getXp();
        int yOfSolved = solvedCell.getYp();

        for (int i = 0; i < n; i++) {
            if (i != yOfSolved && !cellGrid[xOfSolved][i].getCannotBe().contains(target)) {
                cellGrid[xOfSolved][i].addCannotBe(target);
            }
            if (i != xOfSolved && !cellGrid[i][yOfSolved].getCannotBe().contains(target)) {
                cellGrid[i][yOfSolved].addCannotBe(target);
            }
        }

        // removing candidates within cage that do not use target

        ArrayList<ArrayList<Integer>> candidatesToRemove = new ArrayList<>();

        Cage cage = solvedCell.getRespCage();
        ArrayList<ArrayList<Integer>> candidatesOfCage = cage.getCandidates();
        for (ArrayList<Integer> candidate : candidatesOfCage) {
            if (!candidate.contains(target)) {
                candidatesToRemove.add(candidate);
            }
        }

        for (ArrayList<Integer> toRemove : candidatesToRemove) {
            cage.removeCandidate(toRemove);
        }
    }

    private boolean addValid(int target, ArrayList<Integer> candidate) {
        int sum = 0;
        for (Integer integer : candidate) {
            sum += integer;
        }
        return sum == target;
    }

    private boolean subValid(int target, ArrayList<Integer> candidate) {
        /* for subtraction there must be ONLY 2 ints in candidate. Therefore we can assume that the 0th and 1st index
          are not empty
         */
        return (Math.abs(candidate.get(0) - candidate.get(1)) == target);
    }

    private boolean multiValid(int target, ArrayList<Integer> candidate) {
        int prod = 1;
        for (Integer integer : candidate) {
            prod *= integer;
        }
        return prod == target;
    }

    private boolean divValid(int target, ArrayList<Integer> candidate) {
        /* for subtraction there must be ONLY 2 ints in candidate. therefore we can assume that the 0th and 1st index
          are not empty. Also, must be aware of integer division, which is why they are casted to floats
         */
        float num1 = candidate.get(0);
        float num2 = candidate.get(1);

        return (num1/num2 == target) || (num2/num1 == target);
    }

    private ArrayList<Integer> toArrList(int[] data) {
        ArrayList<Integer> list = new ArrayList<>();
        for(int integer : data) {
            list.add(integer);
        }
        return list;
    }

    private void parseInput(String s) throws IOException {
        File file = new File(s);
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

        char[][] representationArray = new char[0][];
        int i = 0;

        // loop will retrieve the representational Array of characters
        do {
            String line = bufferedReader.readLine();
            String[] splitLineStr = line.split(" ");
            if (i == 0) {
                n = splitLineStr.length;
                representationArray = new char[n][n];
                cellGrid = new Cell[n][n];
            }
            for (int j = 0; j < splitLineStr.length; j++) {
                representationArray[i][j] = splitLineStr[j].charAt(0);
            }
            i++;
        } while (i < n);

        bufferedReader.readLine(); // get past empty line

        // instantiate the necessary cages in "cages" field
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            String[] splitLine = line.split(" ");
            char rep = splitLine[0].charAt(0);

            if (rep == 'c') {
                System.out.println();
            }

            cages.add(new Cage(rep,
                    Integer.parseInt(splitLine[1].substring(0, splitLine[1].length() - 1)),
                    splitLine[1].charAt(splitLine[1].length() - 1)));
        }

        // loop will fill "cages" and "cellGrid" fields
        for (int j = 0; j < representationArray.length; j++) {
            for (int k = 0; k < representationArray[j].length; k++) {
                cellGrid[j][k] = new Cell(j, k, representationArray[j][k]);
                placeInCage(cellGrid[j][k]);
            }
        }
    }

    private void placeInCage(Cell cell) {
        for (Cage cage : cages) {
            if (cell.getIdentifier() == cage.getIdentifier()) {
                cage.addCell(cell);
                cell.setRespCage(cage);
                return;
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new Kenken("C:/Users/sehar/IdeaProjects/Kenken/tests/Test2");
    }
}
