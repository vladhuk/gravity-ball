import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

public class CanvasHandler implements EventHandler<ActionEvent> {

    public static final double EPS = 1e-5;
    public static final double G = 9.8;

    private final double top;
    private final double bottom;
    private final double right;
    private final double left;

    private Circle circle;

    private Stopwatch yTime = new Stopwatch();
    private double currentYTime;
    private double y0;
    private double Vy0;

    private Stopwatch xTime = new Stopwatch();
    private double currentXTime;
    private double x0;
    private double Vx0;
    private double a;

    public CanvasHandler(Pane canvas, Circle circle) {
        this.circle = circle;

        xTime.start();
        yTime.start();

        x0 = getX();
        Vx0 = 15;
        a = 0;

        y0 = getY();
        Vy0 = 15;

        Bounds bounds = canvas.getBoundsInLocal();
        top = circle.getRadius() / 100;
        bottom = (bounds.getMaxY() - circle.getRadius()) / 100;
        right = (bounds.getMaxX() - circle.getRadius()) / 100;
        left = top;
    }

    @Override
    public void handle(ActionEvent event) {
        currentYTime = yTime.getCurrentTimeSeconds();
        currentXTime = xTime.getCurrentTimeSeconds();

        circle.setLayoutY(countCoordinate(y0, Vy0, G, currentYTime));
        circle.setLayoutX(countCoordinate(x0, Vx0, a, currentXTime));

        if (isOnBottom()) {
            if (isSpeedLow(getVy())) {
                yTime.turn(false);
            }
            setY0(bottom - EPS);
            setVy0(-getVy() * 0.8);
            yTime.start();
        }

        if (isOnTop()) {
            setY0(top + EPS);
            setVy0(-getVy() * 0.8);
            yTime.start();
        }

        if (isLeft()) {
            setX0(left + EPS);
            setVx0(-getVx() * 0.8);
            xTime.start();
        }

        if (isRight()) {
            setX0(right - EPS);
            setVx0(-getVx() * 0.8);
            xTime.start();
        }
    }

    private boolean isOnBottom() {
        return getY() >= bottom;
    }

    private boolean isOnTop() {
        return getY() <= top;
    }

    private boolean isLeft() {
        return getX() <= left;
    }

    private boolean isRight() {
        return getX() >= right;
    }

    private boolean isSpeedLow(double speed) {
        return Math.abs(speed) <= 1;
    }

    private double countCoordinate(double coord0, double speed0, double acceleration, double time) {
        return (coord0 + speed0 * time + acceleration / 2 * time * time) * 100;
    }

    private double getX() {
        return circle.getLayoutX() / 100;
    }

    private double getY() {
        return circle.getLayoutY() / 100;
    }

    private double getVy() {
        return Vy0 + G * currentYTime;
    }

    private double getVx() {
        return Vx0 + a * currentXTime;
    }

    private void setY0(double y0) {
        this.y0 = y0;
    }

    private void setVy0(double Vy0) {
        this.Vy0 = Vy0;
    }

    private void setX0(double x0) {
        this.x0 = x0;
    }

    private void setVx0(double Vx0) {
        this.Vx0 = Vx0;
    }
}
