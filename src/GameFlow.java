import bagel.Input;
import bagel.Keys;
import bagel.Window;

import javax.sound.sampled.Clip;

public class GameFlow extends ShadowDance {
    public static int frame_no = 0;
    // Screens
    private final StartEndScreen screen = new StartEndScreen();
    private static ILevel level = null;      // Single Instance of Current Level
    public static GameState gameState = GameState.START;
    public static boolean isGamePaused = false;

    // Static references
    public static int STEP_SIZE = 2;

    // Loading and playing sounds
    private Clip musicClip;
    private void loadMusicClip() {

    }


    /**
     * Performs a state update.
     * Allows the game to exit when the escape key is pressed.
     */
    @Override
    protected void update(Input input) {
        // Key listeners
        if (input.wasPressed(Keys.ESCAPE)) {
            Window.close();
        }
        if (input.wasPressed(Keys.NUM_1) && gameState == GameState.START) {
            gameState = GameState.LEVEL1;
        } else if (input.wasPressed(Keys.NUM_2) && gameState == GameState.START) {
            gameState = GameState.LEVEL2;
        } else if (input.wasPressed(Keys.NUM_3) && gameState == GameState.START) {
            gameState = GameState.LEVEL3;
        } else if (input.wasPressed(Keys.TAB)) {
            if (isGamePaused) {
                isGamePaused = false;
                STEP_SIZE = 2;
            } else {
                isGamePaused = true;
                STEP_SIZE = 0;
            }
        } else if (input.wasPressed(Keys.SPACE)) {
            if (gameState == GameState.END || gameState == GameState.LOSE) {
                restart();
            }
        }
        switch (gameState) {
            case START:
                level = null;
                screen.updateInitial();
                break;
            case LEVEL1:
            case LEVEL2:
                if (level == null || level.getClass() != Level1_2.class) {
                    level = new Level1_2();
                }
                if (!isGamePaused) frame_no++;
                break;
            case LEVEL3:
                if (level == null || level.getClass() != Level3.class) {
                    level = new Level3();
                }
                if (!isGamePaused) frame_no++;
                break;
            case END:
                level = null;
                screen.drawClear();
                break;
            case LOSE:
                level = null;
                screen.drawLose();
                break;
        }
        if (level != null) {
            level.update(input);
        }
    }

    private void restart() {
        frame_no = 0;
        gameState = GameState.START;
        isGamePaused = false;
        STEP_SIZE = 2;
    }

}
