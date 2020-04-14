package de.lwerner.graphTool;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextBoundsType;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class GraphTool extends Application {

    private final int DEFAULT_EDGE_WEIGHT = 1;

    double unitSize;
    double orgSceneX, orgSceneY;

    public GraphTool() {
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        if (screenBounds.getMaxX() >= 3000) {
            unitSize = 50;
        } else if (screenBounds.getMaxX() >= 1800) {
            unitSize = 25;
        } else {
            unitSize = 15;
        }
    }

    private Vertex createVertex(double x, double y, double r) {
        Circle circle = new Circle(x, y, r, Color.WHITE);
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(2);

        final Text vertexName = new Text(x, y, null);
        vertexName.setFont(Font.font(25));
        vertexName.setBoundsType(TextBoundsType.VISUAL);
        vertexName.setTextAlignment(TextAlignment.CENTER);
//        double w = vertexName.getBoundsInLocal().getWidth();
//        double h = vertexName.getBoundsInLocal().getHeight();
//
//        vertexName.relocate(r - w / 2, r - h / 2);

        vertexName.xProperty().bind(circle.centerXProperty());
        vertexName.yProperty().bind(circle.centerYProperty());

        circle.setCursor(Cursor.HAND);

        circle.setOnMousePressed((t) -> {
            orgSceneX = t.getSceneX();
            orgSceneY = t.getSceneY();

            Circle c = (Circle) (t.getSource());
            c.toFront();
            vertexName.toFront();
        });

        circle.setOnMouseDragged((t) -> {
            double offsetX = t.getSceneX() - orgSceneX;
            double offsetY = t.getSceneY() - orgSceneY;

            Circle c = (Circle) (t.getSource());

            c.setCenterX(c.getCenterX() + offsetX);
            c.setCenterY(c.getCenterY() + offsetY);

            orgSceneX = t.getSceneX();
            orgSceneY = t.getSceneY();
        });

        return new Vertex(circle, vertexName);
    }

    private Line connect(Circle c1, Circle c2) {
        Line line = new Line();

        line.startXProperty().bind(c1.centerXProperty());
        line.startYProperty().bind(c1.centerYProperty());

        line.endXProperty().bind(c2.centerXProperty());
        line.endYProperty().bind(c2.centerYProperty());

        line.setStrokeWidth(2);
        line.setStrokeLineCap(StrokeLineCap.BUTT);

        return line;
    }

    @Override
    public void start(Stage stage) {
        Group root = new Group();
        Scene scene = new Scene(root, 50 * unitSize, 30 * unitSize);

        // circles
        Vertex redCircle = createVertex(10 * unitSize, 5 * unitSize, 2 * unitSize);
        Vertex blueCircle = createVertex(40 * unitSize, 10 * unitSize, 1.5 * unitSize);
        Vertex greenCircle = createVertex(25 * unitSize, 20 * unitSize, 4 * unitSize);

        Line line1 = connect(redCircle.getCircle(), blueCircle.getCircle());
        Line line2 = connect(redCircle.getCircle(), greenCircle.getCircle());
        Line line3 = connect(greenCircle.getCircle(), blueCircle.getCircle());

//        StackPane redStack = new StackPane(redCircle.getCircle(), redCircle.getText());
//        StackPane blueStack = new StackPane(blueCircle.getCircle(), blueCircle.getText());
//        StackPane greenStack = new StackPane(greenCircle.getCircle(), greenCircle.getText());
//
//        root.getChildren().addAll(redStack, blueStack, greenStack);

        // add the circles
        root.getChildren().add(redCircle.getCircle());
        root.getChildren().add(blueCircle.getCircle());
        root.getChildren().add(greenCircle.getCircle());

        root.getChildren().add(redCircle.getText());
        root.getChildren().add(blueCircle.getText());
        root.getChildren().add(greenCircle.getText());

        // add the lines
        root.getChildren().add(line1);
        root.getChildren().add(line2);
        root.getChildren().add(line3);

        // bring the circles to the front of the lines
        redCircle.getCircle().toFront();
        blueCircle.getCircle().toFront();
        greenCircle.getCircle().toFront();

        redCircle.getText().toFront();
        blueCircle.getText().toFront();
        greenCircle.getText().toFront();

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}