package controller;

import handler.MouseHandler;
import javafx.animation.*;
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
import method.SplineInterpolator;

import java.net.URL;
import java.util.ResourceBundle;

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
    private Timeline timeline;
    private PathTransition pathTransition;
    private CanvasHandler canvasHandler;

    private enum Method {SPLINE}

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        interpolateShape = new Circle(15, Color.BLUE);
        mainShape = new Circle(15, Color.RED);
        mainShape.relocate(300, 150);
        pane.getChildren().add(mainShape);

        methodComboBox.setValue(Method.SPLINE);

        stopButton.setDisable(true);
        interpolateButton.setDisable(true);
        cleanButton.setDisable(true);

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
        startButton.setDisable(true);
        stopButton.setDisable(true);

        pane.getChildren().add(interpolateShape);

        switch (methodComboBox.getValue()) {
            case SPLINE:
                splineInterpolation();
                break;
        }
    }

    private void splineInterpolation() {
        SplineInterpolator spline = SplineInterpolator.createMonotoneCubicSpline(canvasHandler.getXPoints(),
                                                                                 canvasHandler.getYPoints());
        Path path = new Path();

        path.getElements().add(new MoveTo(0, 0));
        for (int i = 1; i < canvasHandler.getXPoints().size(); i++) {
            double xPoint = canvasHandler.getXPoints().get(i);
            double xPointPrev = canvasHandler.getXPoints().get(i - 1);

            for (int j = 0; j < 100; j++) {
                path.getElements().add(new LineTo(xPointPrev + j * 0.01 * (xPoint - xPointPrev),
                                                  spline.interpolate(xPointPrev + j * 0.01 * (xPoint - xPointPrev))));
            }
        }

        pathTransition = new PathTransition();
        pathTransition.setDuration(Duration.millis(canvasHandler.getXPoints().size() / 0.005));
        pathTransition.setNode(interpolateShape);
        pathTransition.setPath(path);
        pathTransition.play();
    }

    @FXML
    void clean() {
        startButton.setDisable(false);

        pathTransition.stop();

        pane.getChildren().remove(interpolateShape);
    }

}
