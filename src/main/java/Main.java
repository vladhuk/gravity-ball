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

        circle = new Circle(15, Color.RED);
        circle.relocate(200, 200);

        canvas.getChildren().addAll(circle);

        Timeline timeline = new Timeline();
        timeline.setAutoReverse(true);
        timeline.setCycleCount(Timeline.INDEFINITE);


        KeyFrame kf = new KeyFrame(Duration.millis(1), new EventHandler<ActionEvent>() {
            final double eps = 1e-5;
            final double g = 9.8;
            double startTime = System.currentTimeMillis();
            double startSpeed = 0;
            Bounds bounds = canvas.getBoundsInLocal();
            double height = (bounds.getMaxY() - circle.getRadius()) / 100;
            double y0 = getY();

            private double getY() {
                return circle.getLayoutY() / 100;
            }

            @Override
            public void handle(ActionEvent event) {
                double currentTime = (System.currentTimeMillis() - startTime) / 1000;

                circle.setLayoutY((y0 + startSpeed * currentTime + g / 2 * Math.pow(currentTime, 2)) * 100);

                if (getY() >= height) {
                    y0 = height - eps;
                    startSpeed = -(startSpeed + g * currentTime);
                    startTime = System.currentTimeMillis();
                }
            }
        });

        timeline.getKeyFrames().add(kf);
        timeline.play();

    }
}
