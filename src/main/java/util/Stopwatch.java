package util;

public class Stopwatch {

    private double startTime;
    private boolean isStopped = false;

    public void start() {
        startTime = System.currentTimeMillis();
    }

    public double getCurrentTimeSeconds() {
        if (isStopped) {
            return 0;
        }

        return (System.currentTimeMillis() - startTime) / 1000;
    }

    public void stop() {
        this.isStopped = true;
    }
}
