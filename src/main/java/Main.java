import javafx.animation.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import javafx.util.Duration;


public class Main extends Application {

    private static Circle circle;
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

        circle = new Circle(15, Color.BLUE);
        circle.relocate(200, 200);

        canvas.getChildren().addAll(circle);

        Timeline timeline = new Timeline();
        timeline.setAutoReverse(true);
        timeline.setCycleCount(Timeline.INDEFINITE);


        KeyFrame kf = new KeyFrame(Duration.millis(1), new EventHandler<ActionEvent>() {
            double startTime = System.currentTimeMillis();
            boolean isMovedToBottom = true;
            double speed = 0;

            @Override
            public void handle(ActionEvent event) {
                double currentTime = (System.currentTimeMillis() - startTime) / 1000;
                Bounds bounds = canvas.getBoundsInLocal();

                if (isMovedToBottom) {
                    circle.setLayoutY((2 + 4.6 * currentTime * currentTime) * 100);
                } else {
                    circle.setLayoutY((bounds.getMaxY() / 100 + speed * currentTime - 4.6 * currentTime * currentTime)
                        * 100);
                }

                if (circle.getLayoutY() >= bounds.getMaxY() - circle.getRadius()) {
                    speed = 8 / currentTime;
                    startTime = System.currentTimeMillis();
                    isMovedToBottom = false;
                }
            }
        });

        timeline.getKeyFrames().add(kf);
        timeline.play();

    }
}
