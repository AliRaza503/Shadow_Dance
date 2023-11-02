import bagel.Image;
import bagel.util.Point;

public class Guardian {
    public final int x = 800, y = 600;
    // Guardian image
    private final Image guardianImage = new Image("res/guardian.PNG");
    public Projectile shoot(Point nearestEnemyPos) {
        return new Projectile(x, y, nearestEnemyPos);
    }

    public void drawGuardian() {
        guardianImage.draw(x, y);
    }

}
