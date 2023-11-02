import bagel.AbstractGame;

/**
 * Skeleton Code for SWEN20003 Project 2, Semester 2, 2023<br>
 * Please enter your name below<br>
 * @author ABC <br>
 */
public abstract class ShadowDance extends AbstractGame  {
    // Dimensions
    private final static int WINDOW_WIDTH = 1024;
    private final static int WINDOW_HEIGHT = 768;
    // Positions
    private final static String GAME_TITLE = "SHADOW DANCE";
    // Misc



    public ShadowDance(){
        super(WINDOW_WIDTH, WINDOW_HEIGHT, GAME_TITLE);
    }

    /**
     * The entry point for the program.
     */
    public static void main(String[] args) {
        ShadowDance game = new GameFlow();
        game.run();
    }

}
