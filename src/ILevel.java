import bagel.Input;

public interface ILevel {
    void drawFrame(Input input);
    void drawLanes();
    void drawScores();

    void addScore(int score);

    void update(Input input);

    void readCSV();

}
