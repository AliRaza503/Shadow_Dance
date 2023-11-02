public enum ScoreType {
    PERFECT(10),
    GOOD(5),
    BAD(-1),
    MISS(-5),
    SPECIAL_HIT(15);
    private final int scores;

    ScoreType(int i) {
        this.scores = i;
    }

    public int getScores() {
        return scores;
    }

    public static String getScore(ScoreType type) {
        switch (type) {
            case PERFECT:
                return "PERFECT";
            case GOOD:
                return "GOOD";
            case BAD:
                return "BAD";
            case SPECIAL_HIT:
                return "SPECIAL HIT";
            case MISS:
            default:
                return "MISS";
        }
    }
}
