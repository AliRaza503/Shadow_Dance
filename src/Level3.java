import bagel.Input;
import bagel.Keys;

import java.util.ArrayList;

public class Level3 extends Level1_2 {

    public ArrayList<Enemy> enemies = new ArrayList<>();
    public ArrayList<Projectile> projectiles = new ArrayList<>();
    public Guardian guardian = new Guardian();

    @Override
    public void update(Input input) {
        if (lanes == null) {
            lanes = new Lane[3];
            readCSV();
        }
        decideGameState();
        drawGameElems(input);

    }

    @Override
    protected void drawGameElems(Input input) {
        super.drawGameElems(input);
        if (GameFlow.frame_no % 600 == 0) {
            addNewEnemy();
        }
        // Draw enemies
        for (Enemy enemy : enemies) {
            enemy.drawEnemy();
            checkHit(enemy);
        }
        // draw guardian
        guardian.drawGuardian();
        // draw projectiles
        for (Projectile projectile : projectiles) {
            projectile.drawProjectile();
            for (Enemy enemy : enemies) {
                if (projectile.detectCollision(enemy.getCoordinates())) {
                    enemy.markedForRemoval = true;
                    projectile.markedForRemoval = true;
                }
            }
        }
        // Check for new projectiles being fired
        if (input.wasPressed(Keys.LEFT_SHIFT) && !enemies.isEmpty()) {
            Enemy enemy = findNearestEnemy(guardian.x, guardian.y);
            if (enemy != null) {
                projectiles.add(guardian.shoot(enemy.getCoordinates()));
            }
        }
        // Remove enemies and projectiles that are marked for removal
        enemies.removeIf(enemy -> enemy.markedForRemoval);
        projectiles.removeIf(projectile -> projectile.markedForRemoval);
    }

    // Find the nearest enemy to the guardian's position
    private Enemy findNearestEnemy(int x, int y) {
        Enemy nearestEnemy = null;
        int minDistance = Integer.MAX_VALUE;
        for (Enemy enemy : enemies) {
            int distance = (int) Math.sqrt(Math.pow(x - enemy.getCoordinates().x, 2) + Math.pow(y - enemy.getCoordinates().y, 2));
            if (distance < minDistance) {
                minDistance = distance;
                nearestEnemy = enemy;
            }
        }
        return nearestEnemy;
    }

    /**
     * is the enemy hitting the lane?<br>
     * Reverses the enemy's movement if it is hitting the lane. <br>
     * If the enemy is stealing the note, then remove the note
     *
     * @param enemy enemy object
     */
    private void checkHit(Enemy enemy) {
        int x = (int) enemy.getCoordinates().x;
        int y = (int) enemy.getCoordinates().y;
        if (x == 100 || x == 900) {
            enemy.reverseDirection();
        }
        checkAndRemoveStolenNotes(x, y);
    }

    // Check if the enemy is stealing the note, if yes, remove the note
    private void checkAndRemoveStolenNotes(int x_coordinates, int y_coordinates) {
        for (Note note : notesDrawn) {
            // if the distance between the centre-coordinates of the enemy image and the centre-coordinates of the note image is <= 104, this is considered as a collision.
            if (note.getType() == NoteType.NORMAL) {
                int distance = (int) Math.sqrt(Math.pow(x_coordinates - note.getCoordinates().x, 2) + Math.pow(y_coordinates - note.getCoordinates().y, 2));
//                System.out.println(distance);
                if (distance <= 104) {
                    note.markedForRemoval = true;
                }
            }
        }
    }

    // Add a new enemy to the list of enemies
    private void addNewEnemy() {
        int enemyX = (int) (Math.random() * 800) + 100;
        // x between 100 and 900 (inclusive), y between 100 and 500 (inclusive)
        int enemyY = (int) (Math.random() * 400) + 100;
        boolean isMovingLeft = Math.random() < 0.5;
        enemies.add(new Enemy(enemyX, enemyY, isMovingLeft));
    }

}
