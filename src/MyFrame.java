import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Objects;

public class MyFrame extends JFrame implements ActionListener {
    JPanel headerPanel;
    JLabel headerMessage;

    JPanel constraintPanel;
    JComboBox constraintComboBox; // goes in gridMakerInput panel
    Button constraintButton; // goes in gridMakerInput panel

    JPanel headerAndConstraint;

    JPanel mainPanel;

    JPanel southPanel;
    JButton computeButton;

    JPanel eastPanel;
    JTextArea inputText;
    JButton lockButton;

    InputValidation inputValidation = new InputValidation(); // object to run input validation methods

    ArrayList<JButton> recentlyClickedButtons = new ArrayList<>();
    ArrayList<Point> recentlyClickedPoints = new ArrayList<>();

    JButton[][] buttonGrid;
    int n; // grid length

    Cell[][] finalCellGrid;
    ArrayList<Cage> cages = new ArrayList<>();

    int identifierNum = 0;

    MyFrame() throws HeadlessException {
        //standard frame constraints
        this.setTitle("Kenken solver!");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500,500);
        this.setLayout(new BorderLayout(10,10));

        //center the JFrame
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);

        //setting header message + panel
        headerPanel = new JPanel();
        headerMessage = new JLabel("Choose grid length (n)");
        headerPanel.add(headerMessage);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10,0,0,0));


        //setting constraint panel and its components
        String [] potentialN = {"3","4","5","6","7","8","9"};
        constraintComboBox = new JComboBox(potentialN);
        constraintComboBox.setEditable(false);

        constraintButton = new Button("Create n by n grid");
        constraintButton.addActionListener(this);

        constraintPanel = new JPanel();
        constraintPanel.add(constraintComboBox);
        constraintPanel.add(constraintButton);

        //combining header and constraint for north area of main frame
        headerAndConstraint = new JPanel();
        headerAndConstraint.setLayout(new BorderLayout());
        headerAndConstraint.add(headerPanel, BorderLayout.NORTH);
        headerAndConstraint.add(constraintPanel, BorderLayout.CENTER);

        // adding all components to MyFrame
        this.add(headerAndConstraint, BorderLayout.NORTH);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == constraintButton) {
            // creating grid
            n = Integer.parseInt(Objects.requireNonNull(constraintComboBox.getSelectedItem()).toString());
            buttonGrid = new JButton[n][n];
            finalCellGrid = new Cell[n][n];

            mainPanel = new JPanel();
            mainPanel.setLayout(new GridLayout(n, n, 10,10));
            mainPanel.setBorder(BorderFactory.createEmptyBorder(0,30, 0,0));

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    JButton toAdd = new JButton("?");
                    toAdd.addActionListener(this);
                    toAdd.setFocusable(false);
                    mainPanel.add(toAdd);
                    buttonGrid[i][j] = toAdd;
                }
            }

            constraintComboBox.setEnabled(false);
            constraintButton.setEnabled(false);

            //submit button for south area of main frame
            computeButton = new JButton("Compute!");
            computeButton.addActionListener(this);
            southPanel = new JPanel();
            southPanel.add(computeButton);


            //buttons and text that allow user to place input in east main frame
            JPanel textPanel = new JPanel();
            JPanel buttonPanel = new JPanel();
            inputText = new JTextArea();
            inputText.setPreferredSize(new Dimension(70,30));
            inputText.setFont(new Font("Consolas", Font.BOLD, 24));
            lockButton = new JButton("Lock");
            lockButton.addActionListener(this);
            textPanel.add(inputText);
            buttonPanel.add(lockButton);
            eastPanel = new JPanel();
            eastPanel.setLayout(new BorderLayout());
            eastPanel.add(textPanel, BorderLayout.NORTH);
            eastPanel.add(buttonPanel, BorderLayout.CENTER);

            //placing all panels in frame
            this.add(mainPanel, BorderLayout.CENTER);
            this.add(southPanel, BorderLayout.SOUTH);
            this.add(eastPanel, BorderLayout.EAST);


            mainPanel.revalidate(); // this updates the frame to add more components
        } else if (e.getSource() == lockButton) { // button that sets a cage
            //input validation
            if (!inputValidation.recentButtonsSameText(recentlyClickedButtons)) {
                errorMessage("To lock, recently clicked buttons must have same input");
                return;
            }
            if (recentlyClickedButtons.size() == 0) {
                errorMessage("Click at least 1 button to lock");
            }

            buildCageAndCellGrid(recentlyClickedButtons, recentlyClickedPoints);

            for (JButton button : recentlyClickedButtons) {
                button.setEnabled(false);
            }

            recentlyClickedButtons.clear();
            recentlyClickedPoints.clear();
        } else if (e.getSource() == computeButton){ // will compute grid given correct user input
            if (!inputValidation.areAllCellsFilled(buttonGrid)) {
                errorMessage("Make sure to fill and lock all cells!");
                return;
            }

            Kenken kenken = new Kenken(n, cages, finalCellGrid);
            if (kenken.solutionExists()) {
                new SolutionFrame(kenken.getSolution());
            } else{
                errorMessage("No solution :(");
            }

            /*TODO:
                2) create cell grid and pass to kenken object
                3) create cell grid constructor in kenken that solves dfs
                4) clean up unused kenken code
                5) display solution / no soln found
             */
        } else { // lastly, check for buttonArray clicks
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (e.getSource() == buttonGrid[i][j]) {
                        String text = inputText.getText();
                        text = text.replace(" ", "");
                        if (inputValidation.isTextValid(text)) {
                            Point point = new Point(i, j);
                            if (inputValidation.isButtonAdjacent(point, recentlyClickedPoints)) {
                                buttonGrid[i][j].setText(inputText.getText());
                                if (!recentlyClickedPoints.contains(point)) { // to avoid duplicates in arraylist
                                    recentlyClickedButtons.add(buttonGrid[i][j]);
                                    recentlyClickedPoints.add(new Point(i,j));
                                }
                            } else {
                                errorMessage("You must click an adjacent button!");
                            }
                        } else {
                            errorMessage("Please enter a valid text! Ex. 4* or 3+ or 2/ or 1-");
                        }

                    }
                }
            }
        }
    }

    private void buildCageAndCellGrid(ArrayList<JButton> buttonGrid, ArrayList<Point> recentlyClickedPoints) {
        //Cage building
        char identifier = (char) identifierNum++;
        char operation;
        int targetVal;

        assert buttonGrid.get(0) != null;

        String inputText = buttonGrid.get(0).getText();
        char lastChar = inputText.charAt(inputText.length() - 1);

        if (Character.isDigit(lastChar)) {
            operation = 'f';
            targetVal = Integer.parseInt(inputText);
        } else {
            operation = lastChar;
            targetVal = Integer.parseInt(inputText.substring(0, inputText.length() - 1));
        }

        Cage cage = new Cage(identifier, targetVal, operation);

        //building cells
        for (Point recentlyClickedPoint : recentlyClickedPoints) {
            int cellI = recentlyClickedPoint.getI();
            int cellJ = recentlyClickedPoint.getJ();
            Cell cell = new Cell(cellI, cellJ, identifier);
            cell.setRespCage(cage);
            cage.addCell(cell);
            finalCellGrid[cellI][cellJ] = cell;
        }

        cages.add(cage);

    }

    private void errorMessage(String s) {
        JOptionPane.showMessageDialog(this, s);
    }
}
