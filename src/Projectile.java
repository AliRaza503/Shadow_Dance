import bagel.DrawOptions;
import bagel.Image;
import bagel.Window;
import bagel.util.Point;

public class Projectile {
    public boolean markedForRemoval = false;
    private double x, y;
    private final double rotation;
    private final Image ProjectileImage = new Image("res/arrow.PNG");

    public Projectile(int x, int y, Point nearestEnemyPos) {
        this.x = x;
        this.y = y;
        this.rotation = findRotation(nearestEnemyPos);
    }

    private double findRotation(Point nearestEnemyPos) {
        // atan2 returns the angle in radians
        // nearestEnemyPos.y - y is the opposite side of the triangle formed by the projectile and the enemy
        // nearestEnemyPos.x - x is the adjacent side of the triangle formed by the projectile and the enemy
        return Math.atan2(nearestEnemyPos.y - y, nearestEnemyPos.x - x);
    }

    public void drawProjectile() {
        // Draw the projectile at (x, y) with the rotation angle
        ProjectileImage.draw(x, y, new DrawOptions().setRotation(rotation));
        move();
    }

    private void move() {
        if (GameFlow.isGamePaused) {
            return;
        }
        // Check if the projectile is out of bounds of the window's width or height
        if (x < 0 || x > Window.getWidth() || y < 0 || y > Window.getHeight()) {
            System.out.println("Out of Window");
            markedForRemoval = true;
            return;
        }
        // Move the projectile by 6 pixels in the direction of the enemy
        // cos(rotation) gives the x component of the projectile's movement
        x += 6 * Math.cos(rotation);
        // sin(rotation) gives the y component of the projectile's movement
        y += 6 * Math.sin(rotation);
    }

    public boolean detectCollision(Point coordinates) {
        // There is a collision if the distance between the projectile and the enemy is <= 62
        return Math.sqrt(Math.pow(coordinates.x - x, 2) + Math.pow(coordinates.y - y, 2)) <= 62;
    }
}
