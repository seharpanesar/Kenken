import java.util.ArrayList;

public class Cell {
    private int xp; // x position in table. 0-3
    private int yp; // y position in table. 0-3

    private char identifier;

    private ArrayList<Integer> cannotBe = new ArrayList<>();
    private int solution = 0;

    private Cage respCage = null; //will be used to access candidates

    public Cell(int xp, int yp, char identifier) {
        this.xp = xp;
        this.yp = yp;
        this.identifier = identifier;
    }

    public void addCannotBe(int cannotBe) {
        this.cannotBe.add(cannotBe);
    }

    public int getXp() {
        return xp;
    }

    public int getYp() {
        return yp;
    }

    public ArrayList<Integer> getCannotBe() {
        return cannotBe;
    }

    public char getIdentifier() {
        return identifier;
    }

    public int getSolution() {
        return solution;
    }

    public Cage getRespCage() {
        return respCage;
    }

    public void setSolution(int solution) {
        this.solution = solution;
    }

    public void setRespCage(Cage respCage) {
        this.respCage = respCage;
    }
}
