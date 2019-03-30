import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.shape.Shape;

public class CanvasHandler implements EventHandler<ActionEvent> {

    public static final double EPS = 1e-5;
    public static final double G = 9.8;

    private double top;
    private double bottom;
    private double right;
    private double left;

    private Shape shape;

    private Coordinate x;
    private Coordinate y;

    public CanvasHandler(Shape shape) {
        this.shape = shape;

        initCoordinates();
        initBorders();
    }

    private void initCoordinates() {
        x = new Coordinate(shape.getLayoutX() / 100, 4, 0);
        x.updateStopwatch();

        y = new Coordinate(shape.getLayoutY() / 100, -5, G);
        y.updateStopwatch();
    }

    private void initBorders() {
        Bounds paneBounds = shape.getParent().getBoundsInLocal();
        Bounds shapeBounds = shape.getBoundsInLocal();

        top = shapeBounds.getMaxY() / 100;
        bottom = (paneBounds.getMaxY() - shapeBounds.getMaxY()) / 100;
        right = (paneBounds.getMaxX() - shapeBounds.getMaxX()) / 100;
        left = shapeBounds.getMaxX() / 100;
    }

    @Override
    public void handle(ActionEvent event) {
        x.checkStopwatch();
        y.checkStopwatch();

        double xPosition = x.getCoordinate() * 100;
        double yPosition = y.getCoordinate() * 100;

        shape.setLayoutX(xPosition);
        shape.setLayoutY(yPosition);

        if (isBottom()) {
            if (y.isSpeedLow()) {
                y.stopStopwatch();
            } else {
                y.updateStopwatch();
            }
            y.setStartValue(bottom - EPS);
            y.setStartSpeed(-y.getSpeed() * 0.8);
        }

        if (isTop()) {
            y.setStartValue(top + EPS);
            y.setStartSpeed(-y.getSpeed() * 0.8);
            y.updateStopwatch();
        }

        if (isLeft()) {
            x.setStartValue(left + EPS);
            x.setStartSpeed(-x.getSpeed() * 0.8);
            x.updateStopwatch();
        }

        if (isRight()) {
            x.setStartValue(right - EPS);
            x.setStartSpeed(-x.getSpeed() * 0.8);
            x.updateStopwatch();
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
