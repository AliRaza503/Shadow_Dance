import bagel.*;
import bagel.util.Point;

public class Lane {
    private LaneType type;
    private Image laneIcon;

    private final double x;          // x coordinate of the lane given by the csv file
    private final double y = 384;    // y coordinate of the lane always 384

    private static final int yIcon = 657;    // y coordinate of the lane icon in the lane always 657

    public Lane(LaneType type, double x) {
        this.type = type;
        this.x = x;
        initImage(type);
    }

    private void initImage(LaneType type) {
        String path = "res/";
        String name = "lane" + getType(type);
        String extension = ".png";
        if (!name.equals("Error")) {
            laneIcon = new Image(path + name + extension);
        } else {
            System.out.println("Error: " + name + " is not a valid lane type.");
        }
    }

    public Point getStaticIconCoordinates() {
        return new Point(x, yIcon);
    }

    public void draw() {
        laneIcon.draw(x, y);
    }
    public LaneType getType(){
        return type;
    }
    public double getX() {
        return x;
    }

    public String getType(LaneType type) {
        switch (type) {
            case LEFT:
                return "Left";
            case DOWN:
                return "Down";
            case UP:
                return "Up";
            case RIGHT:
                return "Right";
            case SPECIAL:
                return "Special";
            default:
                return "Error";
        }
    }

    public static LaneType getType(String type) {
        switch (type) {
            case "Left":
                return LaneType.LEFT;
            case "Down":
                return LaneType.DOWN;
            case "Up":
                return LaneType.UP;
            case "Right":
                return LaneType.RIGHT;
            case "Special":
            default:
                return LaneType.SPECIAL;
        }
    }

}
