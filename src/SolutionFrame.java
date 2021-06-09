import javax.swing.*;
import java.awt.*;

public class SolutionFrame extends JFrame {
    SolutionFrame(int[][] solution) {
        int n = solution.length; // grid size

        //standard frame constraints
        this.setTitle("Solution");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(300,300);
        this.setLayout(new GridLayout(n, n));

        //center the JFrame
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);

        for (int[] ints : solution) {
            for (int j = 0; j < n; j++) {
                JPanel panel = new JPanel();
                JLabel label = new JLabel(Integer.toString(ints[j]));
                label.setFont(new Font("Consolas", Font.BOLD, 30));
                panel.add(label);
                this.add(panel);
            }
        }

        this.setVisible(true);
    }
}
