import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import util.CanvasHandler;


public class Main extends Application {

    private static Shape shape;
    private static Pane canvas;

    private double mouseX;
    private double mouseY;
    private double shapeX;
    private double shapeY;

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

//        shape.setOnMousePressed(mouseEvent -> {
//            mouseX = mouseEvent.getSceneX();
//            mouseY = mouseEvent.getSceneY();
//            shapeX = ((Shape) mouseEvent.getSource()).getLayoutX();
//            shapeY = ((Shape) mouseEvent.getSource()).getLayoutY();
//        });
//
//        shape.setOnMouseDragged(mouseEvent -> {
//            double offsetX = mouseEvent.getSceneX() - mouseX;
//            double offsetY = mouseEvent.getSceneY() - mouseY;
//            double newShapeX = shapeX + offsetX;
//            double newShapeY = shapeY + offsetY;
//
//            Bounds bounds = shape.getBoundsInLocal();
//            double maxX = bounds.getMaxX();
//            double maxY = bounds.getMaxY();
//            if (newShapeX - maxX < 0) {
//                newShapeX = maxX;
//            }
//
//            ((Shape) (mouseEvent.getSource())).setLayoutX(newShapeX);
//            ((Shape) (mouseEvent.getSource())).setLayoutY(newShapeY);
//        });

        Timeline timeline = new Timeline();
        timeline.setAutoReverse(true);
        timeline.setCycleCount(Timeline.INDEFINITE);


        KeyFrame kf = new KeyFrame(Duration.millis(1), new CanvasHandler(shape));

        timeline.getKeyFrames().add(kf);
        timeline.play();
    }
}
