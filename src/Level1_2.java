import bagel.Image;
import bagel.Input;
import bagel.Keys;
import bagel.Window;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Objects;

public class Level1_2 implements ILevel {
    protected Lane[] lanes = null;
    protected ArrayList<Note> notes = new ArrayList<>();
    protected ArrayList<Note> notesDrawn = new ArrayList<>();
    protected int scores = 0;
    private boolean doubleScores = false;
    private int doubleScoreEndFrame = 0;
    private int textFrameNo = 0;
    private LaneType keyPressed = null;
    private static String displayedText = "";
    private static final String SCORE_MESSAGE = "SCORE ";
    private final Image BACKGROUND_IMAGE = new Image("res/background.png");

    private boolean holdFlag = false;

    @Override
    public void drawLanes() {
        for (Lane lane : lanes) {
            lane.draw();
        }
    }

    @Override
    public void drawScores() {
        Font.FONT30.drawString(SCORE_MESSAGE + scores, 35, 35);
    }

    @Override
    public void addScore(int score) {
        // Doubles the score if doubleScores is true. This also doubles the penalty of a BAD hit
        if (doubleScores) {
            score *= 2;
            // If the doubleScores is true and the frame number is greater than the doubleScoreEndFrame, set doubleScores to false
            if (GameFlow.frame_no >= doubleScoreEndFrame) {
                doubleScores = false;
            }
        }
        scores += score;
    }

    @Override
    public void update(Input input) {
        if (lanes == null) {
            lanes = new Lane[4];
            readCSV();
        }
        decideGameState();
        drawGameElems(input);
    }

    protected void decideGameState() {
        if (notes.isEmpty()) {
            if (GameFlow.gameState == GameState.LEVEL1 && scores >= 150) {
                GameFlow.gameState = GameState.END;
            } else if (GameFlow.gameState == GameState.LEVEL2 && scores >= 400) {
                GameFlow.gameState = GameState.END;
            } else if (GameFlow.gameState == GameState.LEVEL3 && scores >= 350) {
                GameFlow.gameState = GameState.END;
            } else {
                GameFlow.gameState = GameState.LOSE;
            }
        }
    }

    protected void drawGameElems(Input input) {
        BACKGROUND_IMAGE.draw(Window.getWidth() / 2.0, Window.getHeight() / 2.0);
        drawLanes();
        drawScores();
        drawFrame(input);
        if (!Objects.equals(displayedText, "") && textFrameNo < 30) {
            drawText();
        } else {
            displayedText = "";
        }
    }

    @Override
    public void drawFrame(Input input) {
        for (Note note : notes) {
            if (note.getFrameNo() == GameFlow.frame_no && !notesDrawn.contains(note)) {
                notesDrawn.add(note);
            }
        }
        notesDrawn.forEach(e -> {
            if (!e.draw(GameFlow.STEP_SIZE)) {
                e.markRemoval();
                addScore(ScoreType.MISS.getScores());
                drawText(ScoreType.getScore(ScoreType.MISS));
            }
//            e.printNote();
        });
        detectPress(input);
        // Break the loop to avoid the following bug: two notes of the same type appearing at short intervals, the second note will be removed immediately after the first note is hit.
        for (Note note : notesDrawn) {
            if (keyPressed != null) {
                // if the key is pressed and the note is in the same lane and a normal note
                if (note.getType() != NoteType.HOLD && note.getLaneType() == keyPressed) {
                    ScoreType scoreType = note.isHit(null, note.getType());
                    // If the note is a special note
                    if (note.getType() != NoteType.NORMAL && scoreType == ScoreType.SPECIAL_HIT) {
                        System.out.println("Special Note");
                        applyEffect(note.getType(), note.getLaneType());
                    } else if (scoreType != null) {
                        addScore(scoreType.getScores());
                        note.markRemoval();
                        drawText(ScoreType.getScore(scoreType));
                        break;
                    }
                } else if (note.getType() == NoteType.HOLD && note.getLaneType() == keyPressed) {
                    // if it is a hold note and the key is pressed then holdStart
                    ScoreType scoreType = note.isHit(true, note.getType());
                    if (scoreType != null) {
                        System.out.println("Hold Started");
                        addScore(scoreType.getScores());
                        drawText(ScoreType.getScore(scoreType));
                        holdFlag = true;
                        break;
                    }
                }
            }
            // if the key is released and the note is in the same lane and a hold note
            if (holdFlag) {
                if (input.wasReleased(Keys.LEFT) || input.wasReleased(Keys.RIGHT) || input.wasReleased(Keys.DOWN) || input.wasReleased(Keys.UP)) {
                    holdFlag = false;
                    System.out.println("Hold Ended");
                    ScoreType scoreTypeReleased = note.isHit(false, note.getType());
                    // If the hold is ended near the end of the note.
                    if (scoreTypeReleased != null) {
                        addScore(scoreTypeReleased.getScores());
                        note.markRemoval();
                        drawText(ScoreType.getScore(scoreTypeReleased));
                    }
                    // If the hold is ended far away from the end of the note. (MISS)
                    if (scoreTypeReleased == null) {
                        addScore(ScoreType.MISS.getScores());
                        note.markRemoval();
                        drawText(ScoreType.getScore(ScoreType.MISS));
                    }
                    break;
                }
            }
        }
        removeMarkedNotes();
    }

    private void applyEffect(NoteType type, LaneType laneType) {
        System.out.println("EFFECT: " + type + " " + laneType);
        String message = "";
        switch (type) {
            case SPEEDUP:
                GameFlow.STEP_SIZE++;
                message = "Speed Up";
                addScore(ScoreType.SPECIAL_HIT.getScores());
                break;
            case SLOWDOWN:
                GameFlow.STEP_SIZE--;
                message = "Slow Down";
                addScore(ScoreType.SPECIAL_HIT.getScores());
                break;
            case DOUBLE:
                message = "Double Score";
                doubleScores = true;
                // Double the scores for the next 480 frames
                doubleScoreEndFrame = GameFlow.frame_no + 480;
                break;
            case BOMB:
                message = "Lane Clear";
                clearLane(laneType);
                break;
        }
        drawText(message);
    }

    private void clearLane(LaneType type) {
        for (Note note : notesDrawn) {
            if (note.getLaneType() == type) {
                note.markRemoval();
            }
        }
    }

    /**
     * Overloaded function to be called once per frame
     */
    private void drawText() {
        double widthFont = Font.FONT40.getWidth(displayedText);
        double x = Window.getWidth() / 2.0 - widthFont / 2.0;
        double y = Window.getHeight() / 2.0 + 40 / 2.0;
        Font.FONT40.drawString(displayedText, x, y);
        if (!GameFlow.isGamePaused) {
            textFrameNo++;
        }
    }

    /**
     * To be called when the text needs to be changed
     *
     * @param text the text to be displayed
     */
    private void drawText(String text) {
        double widthFont = Font.FONT40.getWidth(text);
        double x = Window.getWidth() / 2.0 - widthFont / 2.0;
        double y = Window.getHeight() / 2.0 + 40 / 2.0;
        Font.FONT40.drawString(text, x, y);
        displayedText = text;
        textFrameNo = 0;
    }

    private void removeMarkedNotes() {
        for (int i = 0; i < notesDrawn.size(); i++) {
            if (notesDrawn.get(i).markedForRemoval) {
                notesDrawn.remove(i);
                notes.remove(i);
            }
        }
    }

    private void detectPress(Input input) {
        if (input.wasPressed(Keys.LEFT)) {
            keyPressed = LaneType.LEFT;
        } else if (input.wasPressed(Keys.RIGHT)) {
            keyPressed = LaneType.RIGHT;
        } else if (input.wasPressed(Keys.DOWN)) {
            keyPressed = LaneType.DOWN;
        } else if (input.wasPressed(Keys.UP)) {
            keyPressed = LaneType.UP;
        } else if (input.wasPressed(Keys.SPACE)) {
            keyPressed = LaneType.SPECIAL;
        } else {
            keyPressed = null;
        }
    }

    @Override
    public void readCSV() {
        String fileName = GameFlow.gameState == GameState.LEVEL1 ? "res/level1.csv" : "res/level2.csv";
        if (GameFlow.gameState == GameState.LEVEL3) {
            fileName = "res/level3.csv";
        }
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            int lanesIndex = 0;
            while ((line = br.readLine()) != null) {
                String[] item = line.split(",");
                String firstItem = item[0];
                String secondItem = item[1];
                double thirdItem = Double.parseDouble(item[2]);
                System.out.println(firstItem + " " + secondItem + " " + thirdItem);
                if (Objects.equals(firstItem, "Lane")) {
                    Lane lane = new Lane(Lane.getType(secondItem), thirdItem);
                    lanes[lanesIndex++] = lane;
                } else {
                    // Create and add Notes
                    // firstItem = LaneType (LEFT, DOWN, UP, RIGHT) or SPECIAL keyword (for special notes)
                    // secondItem = NoteType (NORMAL, HOLD, SPEEDUP, SLOWDOWN, DOUBLE, BOMB)
                    // thirdItem = frameNo
                    Note note;
                    note = new Note(Lane.getType(firstItem), Note.getType(secondItem), (int) thirdItem, lanes);
                    notes.add(note);
                }
            }
//            System.out.println("Notes: " + notes.size());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
