package handler;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.shape.Shape;
import util.Coordinate;

public class CanvasHandler implements EventHandler<ActionEvent> {

    public static final double EPS = 1e-5;
    public static final double G = 9.8;

    private double top;
    private double bottom;
    private double right;
    private double left;

    private double scale = 0.01;

    private Shape shape;

    private Coordinate x;
    private Coordinate y;

    public CanvasHandler(Shape shape) {
        this.shape = shape;

        initCoordinates();
        initBorders();
    }

    private void initCoordinates() {
        x = new Coordinate(shape.getLayoutX() * scale, 5, 1);
        x.updateStopwatch();

        y = new Coordinate(shape.getLayoutY() * scale, -10, G);
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
        x.checkStopwatch();
        y.checkStopwatch();

        double xPosition = x.getCoordinate() / scale;
        double yPosition = y.getCoordinate() / scale;

        shape.setLayoutX(xPosition);
        shape.setLayoutY(yPosition);

        checkBorders();
    }

    private void checkBorders() {
        if (isBottom()) {
            y.updateOrStopStopwatch();
            y.setStartValue(bottom - EPS);
            y.setStartSpeed(-y.getSpeed() * 0.8);
        } else if (isTop()) {
            y.updateOrStopStopwatch();
            y.setStartValue(top + EPS);
            y.setStartSpeed(-y.getSpeed() * 0.8);
        }

        if (isLeft()) {
            x.updateOrStopStopwatch();
            x.setStartValue(left + EPS);
            x.setStartSpeed(-x.getSpeed() * 0.8);
        } else if (isRight()) {
            x.updateOrStopStopwatch();
            x.updateStopwatch();
            x.setStartValue(right - EPS);
            x.setStartSpeed(-x.getSpeed() * 0.8);
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

}
