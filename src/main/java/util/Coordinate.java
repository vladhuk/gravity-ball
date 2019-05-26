package util;

public class  Coordinate {

    private Stopwatch stopwatch = new Stopwatch();
    private double currentTime;
    private double startValue;
    private double startSpeed;
    private double acceleration;

    public Coordinate(double startValue, double startSpeed, double acceleration) {
        this.startValue = startValue;
        this.startSpeed = startSpeed;
        this.acceleration = acceleration;
    }

    public double getStartValue() {
        return startValue;
    }

    public void setStartValue(double startValue) {
        this.startValue = startValue;
    }

    public double getStartSpeed() {
        return startSpeed;
    }

    public void setStartSpeed(double startSpeed) {
        this.startSpeed = startSpeed;
    }

    public double checkStopwatch() {
        currentTime = stopwatch.getCurrentTimeSeconds();
        return currentTime;
    }

    public void updateStopwatch() {
        stopwatch.start();
    }

    public void stopStopwatch() {
        stopwatch.stop();
    }

    public void updateOrStopStopwatch() {
        if (isSpeedLow()) {
            stopStopwatch();
        } else {
            updateStopwatch();
        }
    }

    public boolean isSpeedLow() {
        return Math.abs(getSpeed()) <= 0.7;
    }

    public double getCoordinate() {
        return startValue + startSpeed * currentTime + acceleration / 2 * currentTime * currentTime;
    }

    public double getSpeed() {
        return startSpeed + acceleration * currentTime;
    }

}
