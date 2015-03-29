package graphics;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

public class MovePanel extends JPanel {

    private int x;
    private int y;
    private boolean isSet;
    final int UP = 0, RIGHT = 1, DOWN = 2, LEFT = 3;

    public MovePanel() {
        super();
        isSet = false;
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillRect(x, y, 10, 10);
    }

    public void setXY(int x, int y) {
        this.x = x;
        this.y = y;
        isSet = true;
    }

    public boolean isSet() {
        return isSet;
    }

    public void setSet() {
        isSet = false;
    }

    public void move(int direction) {
        switch (direction) {
            case RIGHT:
                x += 2;
                break;
            case DOWN:
                y += 2;
                break;
            case LEFT:
                x -= 2;
                break;
            case UP:
                y -= 2;
                break;
        }
    }
}
