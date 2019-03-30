public class Stopwatch {

    private double startTime;
    private boolean turn = true;

    public void start() {
        startTime = System.currentTimeMillis();
    }

    public double getCurrentTimeSeconds() {
        if (!turn) {
            return 0;
        }

        return (System.currentTimeMillis() - startTime) / 1000;
    }

    public void turn(boolean flag) {
        this.turn = flag;
    }
}
