package controller;

import handler.MouseHandler;
import javafx.animation.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.util.Duration;
import handler.CanvasHandler;
import method.Interpolatable;
import method.LagrangeInterpolator;
import method.SplineInterpolator;

import java.net.URL;
import java.util.*;

public class WindowController implements Initializable {

    @FXML
    private Pane pane;

    @FXML
    private TextField springField;

    @FXML
    private TextField startSpeedXField;

    @FXML
    private TextField startSpeedYField;

    @FXML
    private TextField accelerationField;

    @FXML
    private Button startButton;

    @FXML
    private Button stopButton;

    @FXML
    private Button interpolateButton;

    @FXML
    private Button cleanButton;

    @FXML
    private ComboBox<Method> methodComboBox;

    private Shape mainShape;
    private Shape interpolateShape;
    private List<Shape> shapePoints;
    private Timeline timeline;
    private PathTransition pathTransition;
    private CanvasHandler canvasHandler;

    private enum Method { SPLINE, LAGRANGE }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initShapes();
        configureMethodComboBox();
        addMouseHandlers();

        stopButton.setDisable(true);
        interpolateButton.setDisable(true);
        cleanButton.setDisable(true);
    }

    private void initShapes() {
        interpolateShape = new Circle(15, Color.BLUE);
        mainShape = new Circle(15, Color.RED);
        mainShape.relocate(300, 150);
        pane.getChildren().add(mainShape);
    }

    private void configureMethodComboBox() {
        methodComboBox.setValue(Method.SPLINE);
        ObservableList<Method> methods = FXCollections.observableArrayList(Method.values());
        methodComboBox.setItems(methods);
    }

    private void addMouseHandlers() {
        MouseHandler handler = new MouseHandler(mainShape);
        mainShape.setOnMousePressed(handler.getOnMousePressedEvent());
        mainShape.setOnMouseDragged(handler.getOnMouseDraggedEvent());
    }

    @FXML
    void start() {
        if (timeline != null) {
            timeline.stop();
        }

        stopButton.setDisable(false);
        interpolateButton.setDisable(true);
        cleanButton.setDisable(true);

        interpolateShape.relocate(mainShape.getLayoutX(), mainShape.getLayoutY());

        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);

        canvasHandler = buildHandler();
        KeyFrame kf = new KeyFrame(Duration.millis(1), canvasHandler);

        timeline.getKeyFrames().add(kf);
        timeline.play();
    }

    private CanvasHandler buildHandler() {
        double spring = getDoubleFromField(springField);
        double V0x = getDoubleFromField(startSpeedXField);
        double V0y = getDoubleFromField(startSpeedYField);
        double acceleration = getDoubleFromField(accelerationField);

        CanvasHandler handler = new CanvasHandler(mainShape, spring, V0x, V0y, acceleration);

        return handler;
    }

    private double getDoubleFromField(TextField field) {
        try {
            return Double.parseDouble(field.getText());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    @FXML
    void stop() {
        timeline.stop();

        interpolateButton.setDisable(false);
    }

    @FXML
    void interpolate() {
        cleanButton.setDisable(false);
        interpolateButton.setDisable(true);
        startButton.setDisable(true);
        stopButton.setDisable(true);

        pane.getChildren().add(interpolateShape);

        Map<Double, Double> sortedPoints = getSortedPoints();
        List<Double> xPoints = new ArrayList<>(sortedPoints.keySet());
        List<Double> yPoints = new ArrayList<>(sortedPoints.values());

        drawPoints(xPoints, yPoints);

        Interpolatable method = null;
        switch (methodComboBox.getValue()) {
            case SPLINE:
                method = SplineInterpolator.createMonotoneCubicSpline(xPoints, yPoints);
                break;
            case LAGRANGE:
                method = LagrangeInterpolator.createLagrangePolinom(xPoints, yPoints);
        }

        interpolate(method);
    }

    private Map<Double, Double> getSortedPoints() {
        List<Double> xPoints = canvasHandler.getXPoints();
        List<Double> yPoints = canvasHandler.getYPoints();

        if (xPoints.get(1) > xPoints.get(0)) {
            for (int i = 1; i < xPoints.size(); i++) {
                if (xPoints.get(i) < xPoints.get(i - 1)) {
                    xPoints = xPoints.subList(0, i);
                    yPoints = yPoints.subList(0, i);
                    break;
                }
            }
        } else {
            for (int i = 1; i < xPoints.size(); i++) {
                if (xPoints.get(i) > xPoints.get(i - 1)) {
                    xPoints = xPoints.subList(0, i);
                    yPoints = yPoints.subList(0, i);
                    break;
                }
            }
        }

        Map<Double, Double> sortedPoints = new TreeMap<>();
        for (int i = 0; i < xPoints.size(); i++) {
            sortedPoints.put(xPoints.get(i), yPoints.get(i));
        }

        return sortedPoints;
    }

    private void drawPoints(List<Double> xPoints, List<Double> yPoints) {
        shapePoints = new LinkedList<>();

        for (int i = 0; i < xPoints.size(); i++) {
            Circle circle = new Circle(3, Color.BLACK);
            circle.relocate(xPoints.get(i), yPoints.get(i));
            shapePoints.add(circle);
        }
        pane.getChildren().addAll(shapePoints);
    }

    private void interpolate(Interpolatable method) {
        Path path = new Path();

        path.getElements().add(new MoveTo(0, 0));

        final double interval = 0.01;
        List<Double> xPoints = canvasHandler.getXPoints();

        for (int i = 1; i < xPoints.size(); i++) {

            double x = xPoints.get(i);
            double xPrev = xPoints.get(i - 1);

            for (int j = 0; j < 1 / interval; j++) {
                double newX = xPrev + j * interval * (x - xPrev);
                double newY = method.interpolate(newX);
                newX -= interpolateShape.getLayoutX();
                newY -= interpolateShape.getLayoutY();

                path.getElements().add(new LineTo(newX, newY));
            }
        }

        pathTransition = new PathTransition();
        pathTransition.setDuration(Duration.millis(xPoints.size() / 0.005));
        pathTransition.setNode(interpolateShape);
        pathTransition.setPath(path);
        pathTransition.play();
    }

    @FXML
    void clean() {
        startButton.setDisable(false);
        interpolateButton.setDisable(false);

        pathTransition.stop();

        pane.getChildren().remove(interpolateShape);
        pane.getChildren().removeAll(shapePoints);
    }

}
