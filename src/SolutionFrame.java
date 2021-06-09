import javax.swing.*;
import java.awt.*;

public class SolutionFrame extends JFrame {
    SolutionFrame(int[][] solution) {
        int n = solution.length; // grid size

        //standard frame constraints
        this.setTitle("Solution");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(700,700);
        this.setLayout(new GridLayout(n, n));

        //center the JFrame
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);

        InputValidation inputValidation = new InputValidation(); // to invoke the fontSize() method
        int fontSize = inputValidation.fontSize(n, "1");

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                JPanel panel = new JPanel();
                JLabel button = new JLabel(Integer.toString(solution[i][j]));
                button.setFont(new Font("Consolas", Font.BOLD, fontSize));
                button.setForeground(Color.BLACK);
                button.setBackground(MyFrame.colorGrid[i][j]);
                button.setOpaque(true);
                button.setEnabled(false);
                panel.add(button);
                this.add(panel);
            }
        }

        this.setVisible(true);
    }
}
