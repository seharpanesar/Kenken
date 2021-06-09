import javax.swing.*;
import java.util.ArrayList;

public class InputValidation {
    public boolean isTextValid(String text) {
        if (text.length() == 0) {
            return false;
        }

        //all chars before last char is a digit
        for (int i = 0; i < text.length() - 1; i++) {
            char maybeDigit = text.charAt(i);
            if (!Character.isDigit(maybeDigit)) {
                return false;
            }
        }

        // last char must be an operation or number
        char lastChar = text.charAt(text.length() - 1);

        String operations = "*/+-";
        return Character.isDigit(lastChar) || operations.indexOf(lastChar) != -1;
    }

    public boolean isButtonAdjacent(Point inQuestion, ArrayList<Point> recentlyClickedPoints) {
        if (recentlyClickedPoints.size() == 0) {
            return true;
        }

        for (Point point : recentlyClickedPoints) {
            if (point.equals(inQuestion)) { // if we clicked on a recently clicked button
                return true;
            }

            int distSq = Math.abs(point.getI() - inQuestion.getI()) + Math.abs(point.getJ() - inQuestion.getJ());

            if (distSq == 1) {
                return true;
            }
        }

        return false;
    }

    public boolean recentButtonsSameText(ArrayList<JButton> buttons) {
        String text = null;
        int i = 0;
        for (JButton button: buttons) {
            i++;
            if (i == 1) {
                text = button.getText();
            } else {
                assert text != null;
                if (!text.equals(button.getText())) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean areAllCellsFilled(JButton[][] buttonGrid) {
        for (JButton[] jButtons : buttonGrid) {
            for (JButton jButton : jButtons) {
                if (jButton.isEnabled()) {
                    return false;
                }
            }
        }
        return true;
    }
}
