package controller;

import handler.MouseHandler;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;
import handler.CanvasHandler;

import java.net.URL;
import java.util.ResourceBundle;

public class WindowController implements Initializable {

    @FXML
    private Pane pane;

    @FXML
    private TextField shapeSpringField;

    @FXML
    private TextField borderSpringField;

    @FXML
    private TextField FrictionField;

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

    private Shape shape;
    private Timeline timeline;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        shape = new Circle(15, Color.RED);
        shape.relocate(300, 150);
        pane.getChildren().add(shape);

        MouseHandler handler = new MouseHandler(shape);
        shape.setOnMousePressed(handler.getOnMousePressedEvent());
        shape.setOnMouseDragged(handler.getOnMouseDraggedEvent());
    }

    @FXML
    void start() {
        stop();
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        KeyFrame kf = new KeyFrame(Duration.millis(1), new CanvasHandler(shape));
        timeline.getKeyFrames().add(kf);
        timeline.play();
    }

    @FXML
    void stop() {
        if (timeline != null) {
            timeline.stop();
        }
    }

}
