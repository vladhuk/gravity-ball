package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        shape = new Circle(15, Color.RED);
        shape.relocate(200, 200);
    }

    @FXML
    void start() {

    }

    @FXML
    void stop() {

    }

}
