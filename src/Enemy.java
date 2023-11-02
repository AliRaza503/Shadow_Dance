import bagel.Image;
import bagel.util.Point;

public class Enemy {
    private static final int NO_OF_PIXELS_TO_MOVE = 1;
    public boolean markedForRemoval = false;
    private int x;      // x coordinate of the enemy can be changed when the enemy moves
    private final int y;
    private boolean isMovingLeft;
    private final Image enemyImage = new Image("res/enemy.PNG");
    public Enemy(int x, int y, boolean isMovingLeft) {
        this.x = x;
        this.y = y;
        this.isMovingLeft = isMovingLeft;
    }
    // To draw the enemy at (x, y)
    public void drawEnemy() {
        enemyImage.draw(x, y);
        move();
    }

    // To move the enemy in the direction it is moving
    private void move(){
        if (GameFlow.isGamePaused) {
            return;
        }
        if (isMovingLeft) {
            x -= NO_OF_PIXELS_TO_MOVE;
        } else {
            x += NO_OF_PIXELS_TO_MOVE;
        }
    }

    /**
     * getCoordinates()
     *
     * @return Point object containing the x and y coordinates of the enemy
     */
    public Point getCoordinates() {
        return new Point(x, y);
    }

    // Reverse the direction of movement of the enemy
    public void reverseDirection() {
        isMovingLeft = !isMovingLeft;
    }
}
