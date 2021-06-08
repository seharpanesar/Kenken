import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    MyFrame() throws HeadlessException {
        //standard frame constraints
        this.setTitle("Kenken solver!");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500,500);
        this.setLayout(new BorderLayout(10,10));

        //setting header message + panel
        headerPanel = new JPanel();
        headerMessage = new JLabel("Choose grid length (n)");
        headerPanel.add(headerMessage);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10,0,0,0));


        //setting constraint panel and its components
        String [] potentialN = {"3","4","5","6","7","8","9"};
        constraintComboBox = new JComboBox(potentialN);
        constraintComboBox.setEditable(true);

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
            int n = Integer.parseInt(Objects.requireNonNull(constraintComboBox.getSelectedItem()).toString());
            mainPanel = new JPanel();
            mainPanel.setLayout(new GridLayout(n, n, 10,10));

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    mainPanel.add(new Button("?"));
                }
            }

            constraintComboBox.setEnabled(false);

            //submit button for south area of main frame
            computeButton = new JButton("Compute!");
            southPanel = new JPanel();
            southPanel.add(computeButton);


            //buttons and text that allow user to place input in east main frame
            JPanel textPanel = new JPanel();
            JPanel buttonPanel = new JPanel();
            inputText = new JTextArea();
            inputText.setPreferredSize(new Dimension(70,30));
            inputText.setFont(new Font("Consolas", Font.BOLD, 24));
            lockButton = new JButton("Lock");
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

        }
    }
}
