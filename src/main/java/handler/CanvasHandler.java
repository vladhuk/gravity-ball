package handler;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.shape.Shape;
import util.Coordinate;
import util.Stopwatch;

import java.util.ArrayList;
import java.util.List;

public class CanvasHandler implements EventHandler<ActionEvent> {

    public static final double EPS = 1e-5;
    public static final double G = 9.8;

    private double top;
    private double bottom;
    private double right;
    private double left;

    private double scale = 0.01;
    private double spring;

    private Shape shape;

    private Coordinate x;
    private Coordinate y;

    private List<Double> xPoints = new ArrayList<>();
    private List<Double> yPoints = new ArrayList<>();
    private Stopwatch stopwatch = new Stopwatch();
    private double lastCurrentTime = 0;

    public CanvasHandler(Shape shape, double spring, double V0x, double V0y, double acceleration) {
        this.shape = shape;
        this.spring = spring;

        initCoordinates(V0x, V0y, acceleration);
        initBorders();

        stopwatch.start();
    }

    private void initCoordinates(double V0x, double V0y, double acceleration) {
        x = new Coordinate(shape.getLayoutX() * scale, V0x, acceleration);
        x.updateStopwatch();

        y = new Coordinate(shape.getLayoutY() * scale, V0y, G);
        y.updateStopwatch();
    }

    private void initBorders() {
        Bounds paneBounds = shape.getParent().getBoundsInLocal();
        Bounds shapeBounds = shape.getBoundsInLocal();

        top = shapeBounds.getMaxY() * scale;
        bottom = (paneBounds.getMaxY() - shapeBounds.getMaxY()) * scale;
        right = (paneBounds.getMaxX() - shapeBounds.getMaxX()) * scale;
        left = shapeBounds.getMaxX() * scale;
    }

    @Override
    public void handle(ActionEvent event) {
        double currentTime = stopwatch.getCurrentTimeSeconds();
        x.checkStopwatch();
        y.checkStopwatch();

        double xPosition = x.getCoordinate() / scale;
        double yPosition = y.getCoordinate() / scale;

        checkBorders();

        shape.setLayoutX(xPosition);
        shape.setLayoutY(yPosition);

        if (currentTime - lastCurrentTime > 0.2) {
            lastCurrentTime = currentTime;
            xPoints.add(xPosition);
            yPoints.add(yPosition);
        }
    }

    private void checkBorders() {
        if (isBottom()) {
            y.updateOrStopStopwatch();
            y.setStartValue(bottom - EPS);
            y.setStartSpeed(-y.getSpeed() * spring);
        } else if (isTop()) {
            y.updateOrStopStopwatch();
            y.setStartValue(top + EPS);
            y.setStartSpeed(-y.getSpeed() * spring);
        }

        if (isLeft()) {
            x.updateOrStopStopwatch();
            x.setStartValue(left + EPS);
            x.setStartSpeed(-x.getSpeed() * spring);
        } else if (isRight()) {
            x.updateOrStopStopwatch();
            x.setStartValue(right - EPS);
            x.setStartSpeed(-x.getSpeed() * spring);
        }
    }

    private boolean isBottom() {
        return y.getCoordinate() >= bottom;
    }

    private boolean isTop() {
        return y.getCoordinate() <= top;
    }

    private boolean isLeft() {
        return x.getCoordinate() <= left;
    }

    private boolean isRight() {
        return x.getCoordinate() >= right;
    }

    public List<Double> getXPoints() {
        return xPoints;
    }

    public List<Double> getYPoints() {
        return yPoints;
    }

}
