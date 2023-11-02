import bagel.Image;
import bagel.Window;

public class StartEndScreen {
    // Static Messages
    private static final String START_MESSAGE1 = "SELECT LEVELS WITH";
    private static final String START_MESSAGE2 = "NUMBER KEYS";
    private static final String START_MESSAGE3 = "    1       2       3";
    private final static String GAME_TITLE = "SHADOW DANCE";
    private static final String END_MESSAGE = "PRESS SPACE TO RETURN TO LEVEL SELECTION";
    private final Image BACKGROUND_IMAGE = new Image("res/background.png");
    public void updateInitial() {
        BACKGROUND_IMAGE.draw(Window.getWidth() / 2.0, Window.getHeight() / 2.0);
        Font.FONT64.drawString(GAME_TITLE, 220, 250);
        Font.FONT24.drawString(START_MESSAGE1, 220 + 100, 250 + 190);
        Font.FONT24.drawString(START_MESSAGE2, 220 + 100, 250 + 190 + 24);
        Font.FONT24.drawString(START_MESSAGE3, 220 + 100, 250 + 190 + 24 + 48);
    }
    public void drawClear() {
        String clear = "CLEAR!";
        drawMessage(clear);
    }

    private void drawMessage(String message) {
        double widthFont;
        double x;
        double y;
        widthFont = Font.FONT64.getWidth(message);
        x = Window.getWidth() / 2.0 - widthFont / 2.0;
//        y = Window.getHeight() / 2.0 + 40 / 2.0;
        y = 300;
        BACKGROUND_IMAGE.draw(Window.getWidth() / 2.0, Window.getHeight() / 2.0);
        Font.FONT64.drawString(message, x, y);
        widthFont = Font.FONT24.getWidth(END_MESSAGE);
        x = Window.getWidth() / 2.0 - widthFont / 2.0;
        Font.FONT24.drawString(END_MESSAGE, x, 500);
    }

    public void drawLose() {
        String tryAgain = "TRY AGAIN!";
        drawMessage(tryAgain);
    }
}
