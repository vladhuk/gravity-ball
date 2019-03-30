import javafx.animation.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import javafx.util.Duration;


public class Main extends Application {

    private static Shape shape;
    private static Pane canvas;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        canvas = new Pane();
        Scene scene = new Scene(canvas, 800, 600);

        primaryStage.setTitle("Game");
        primaryStage.setScene(scene);
        primaryStage.show();

        shape = new Circle(15, Color.RED);
        shape.relocate(200, 200);

        canvas.getChildren().addAll(shape);

        Timeline timeline = new Timeline();
        timeline.setAutoReverse(true);
        timeline.setCycleCount(Timeline.INDEFINITE);


        KeyFrame kf = new KeyFrame(Duration.millis(1), new CanvasHandler(shape));

        timeline.getKeyFrames().add(kf);
        timeline.play();
    }
}
