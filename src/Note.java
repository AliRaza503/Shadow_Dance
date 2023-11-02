import bagel.Image;
import bagel.Window;
import bagel.util.Point;


public class Note {
    private int frameNo = 0;                  // frame number of appearance of the note
    private double x = 0;                        // x coordinate of the note = x coordinate of the lane
    private double y = 100;                      // y coordinate of the note
    private Image noteIcon;
    private final double height;

    private final LaneType laneType;       // Lane of appearance of the note
    private final NoteType noteType;       // Type of the note (normal, hold, speedup, slowdown, double, bomb)

    public boolean markedForRemoval = false;


    public Note(LaneType laneType, NoteType noteType, int frameNo, Lane[] lanes) {
        this.laneType = laneType;
        this.noteType = noteType;
        this.frameNo = frameNo;
        initIcon();
        updateX(lanes, laneType);
        this.height = noteIcon.getHeight();
    }

    private void updateX(Lane[] lanes, LaneType laneType) {
        for (Lane lane: lanes) {
            if (lane.getType() == laneType) {
                x = lane.getX();
            }
        }
    }

    public Point getCoordinates() {
        return new Point(x, y);
    }

    public int getFrameNo() {
        return frameNo;
    }

    public static NoteType getType(String type) {
        switch (type) {
            case "Normal":
                return NoteType.NORMAL;
            case "Hold":
                return NoteType.HOLD;
            case "SpeedUp":
                return NoteType.SPEEDUP;
            case "SlowDown":
                return NoteType.SLOWDOWN;
            case "DoubleScore":
                return NoteType.DOUBLE;
            case "Bomb":
                return NoteType.BOMB;
            default:
                return null;
        }
    }
    public NoteType getType() {
        return noteType;
    }

    /**
     * Initialize the icon of the note based on the lane type and note type
     */
    public void initIcon() {
        switch (noteType) {
            case NORMAL:
                switch (laneType) {
                    case LEFT:
                        noteIcon = new Image("res/noteLeft.png");
                        break;
                    case DOWN:
                        noteIcon = new Image("res/noteDown.png");
                        break;
                    case UP:
                        noteIcon = new Image("res/noteUp.png");
                        break;
                    case RIGHT:
                        noteIcon = new Image("res/noteRight.png");
                        break;
                }
                break;
            case HOLD:
                switch (laneType) {
                    case LEFT:
                        noteIcon = new Image("res/holdNoteLeft.png");
                        break;
                    case DOWN:
                        noteIcon = new Image("res/holdNoteDown.png");
                        break;
                    case UP:
                        noteIcon = new Image("res/holdNoteUp.png");
                        break;
                    case RIGHT:
                        noteIcon = new Image("res/holdNoteRight.png");
                        break;
                }
                break;
            case SPEEDUP:
                noteIcon = new Image("res/noteSpeedUp.png");
                break;
            case SLOWDOWN:
                noteIcon = new Image("res/noteSlowDown.png");
                break;
            case DOUBLE:
                noteIcon = new Image("res/note2x.png");
                break;
            case BOMB:
                noteIcon = new Image("res/noteBomb.png");
                break;
        }
    }

    public boolean draw(int speed) {
        // check whether `y` is out of window
        double top = y - (this.height / 2);
        if (top < Window.getHeight()) {
            this.noteIcon.draw(x, y);
            // keep `x`, increment y
            y += speed;
            return true;
        } else {
            return false;
        }
    }

    public void printNote() {
        System.out.println("Lane Type " + laneType + " Note Type " + noteType + " Frame " + frameNo + " Current Frame " + GameFlow.frame_no);
    }

    public void markRemoval() {
        markedForRemoval = true;
    }

    public LaneType getLaneType() {
        return laneType;
    }


    /**
     * • If distance <= 15, this is a PERFECT score and receives 10 points <br>
     * • If 15 < distance <= 50, this is a GOOD score and receives 5 points <br>
     * • If 50 < distance <= 100, this is a BAD score and receives -1 points <br>
     * • If 100 < distance <= 200, this is a MISS and receives -5 points.
     *
     * @param isStarted whether the hold is started or ended.
     * @param type     type of the note
     * @return score
     */
    public ScoreType isHit(Boolean isStarted, NoteType type) {
        int pos = 0;
        if (noteType == NoteType.HOLD) {
            if (isStarted) {
                pos = 82;
            } else {
                pos = -82;
            }
        }
        double distance = Math.abs(y - 657 + pos);
        // If it is a special note then only hit
        if (type == NoteType.SPEEDUP || type == NoteType.SLOWDOWN || type == NoteType.DOUBLE || type == NoteType.BOMB) {
            if (distance <= 50) {
                return ScoreType.SPECIAL_HIT;
            }
        }
        if (distance <= 15) {
            return ScoreType.PERFECT;
        } else if (distance <= 50) {
            return ScoreType.GOOD;
        } else if (distance <= 100) {
            return ScoreType.BAD;
        } else if (distance <= 200) {
            return ScoreType.MISS;
        } else {
            return null;
        }
    }
}
