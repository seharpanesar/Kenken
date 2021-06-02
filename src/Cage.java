/*
 I defining a cage to be a set of cells that together, using arithmetic, will result in a target number. An example
 of this is the 3 12* cells in the pseudocode example.
 */

import java.util.ArrayList;

public class Cage {
    private ArrayList<Cell> cells = new ArrayList<>();
    private ArrayList<ArrayList<Integer>> candidates = new ArrayList<>();
    private int targetVal; // Arithmetic will result in this number
    private char operation;
    private char identifier;

    public Cage(char identifier, int targetVal, char operation) {
        this.identifier = identifier;
        this.targetVal = targetVal;
        this.operation = operation;
    }

    public void addCell(Cell cell) {
        cells.add(cell);
    }

    public ArrayList<Cell> getCells() {
        return cells;
    }

    public int getTargetVal() {
        return targetVal;
    }

    public char getOperation() {
        return operation;
    }

    public char getIdentifier() {
        return identifier;
    }

    public ArrayList<ArrayList<Integer>> getCandidates() {
        return candidates;
    }

    public void removeCandidate(ArrayList<Integer> candidate) {
        candidates.remove(candidate);
    }

    public void setCandidates(ArrayList<ArrayList<Integer>> candidates) {
        this.candidates = candidates;
    }
}
