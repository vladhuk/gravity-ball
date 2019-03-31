package handler;

import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Shape;

public class MouseHandler {

    private Shape shape;

    private double mouseX;
    private double mouseY;
    private double shapeX;
    private double shapeY;

    public MouseHandler(Shape shape) {
        this.shape = shape;
    }

    private class OnMousePressedEvent implements EventHandler<MouseEvent> {

        @Override
        public void handle(MouseEvent mouseEvent) {
            mouseX = mouseEvent.getSceneX();
            mouseY = mouseEvent.getSceneY();
            shapeX = ((Shape) mouseEvent.getSource()).getLayoutX();
            shapeY = ((Shape) mouseEvent.getSource()).getLayoutY();
        }
    }

    public OnMousePressedEvent getOnMousePressedEvent() {
        return new OnMousePressedEvent();
    }

    private class OnMouseDraggedEvent implements EventHandler<MouseEvent> {

        double newShapeX;
        double newShapeY;

        @Override
        public void handle(MouseEvent mouseEvent) {
            double offsetX = mouseEvent.getSceneX() - mouseX;
            double offsetY = mouseEvent.getSceneY() - mouseY;
            newShapeX = shapeX + offsetX;
            newShapeY = shapeY + offsetY;

            limitBorders();

            ((Shape) (mouseEvent.getSource())).setLayoutX(newShapeX);
            ((Shape) (mouseEvent.getSource())).setLayoutY(newShapeY);
        }

        private void limitBorders() {
            Bounds paneBounds = shape.getParent().getBoundsInLocal();
            Bounds shapeBounds = shape.getBoundsInLocal();

            if (newShapeX - shapeBounds.getMaxX() <= 0) {
                newShapeX = shapeBounds.getMaxX();
            } else if (newShapeX + shapeBounds.getMaxX() >= paneBounds.getMaxX()) {
                newShapeX = paneBounds.getMaxX() - shapeBounds.getMaxX();
            }

            if (newShapeY - shapeBounds.getMaxY() <= 0) {
                newShapeY = shapeBounds.getMaxY();
            } else if (newShapeY + shapeBounds.getMaxY() >= paneBounds.getMaxY()) {
                newShapeY = paneBounds.getMaxY() - shapeBounds.getMaxY();
            }
        }
    }

    public OnMouseDraggedEvent getOnMouseDraggedEvent() {
        return new OnMouseDraggedEvent();
    }
}
