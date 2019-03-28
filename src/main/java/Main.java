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

        double startTime = System.currentTimeMillis();
        KeyFrame kf = new KeyFrame(Duration.millis(1), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                double currentTime = (System.currentTimeMillis() - startTime) / 1000;
                System.out.println(currentTime);
                circle.setLayoutY(circle.getLayoutY() + 4.6 * currentTime * currentTime);
            }
        });

        timeline.getKeyFrames().add(kf);
        timeline.play();

    }
}
