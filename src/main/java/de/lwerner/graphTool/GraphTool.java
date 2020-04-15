package de.lwerner.graphTool;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class GraphTool extends Application {

    private static final int DEFAULT_EDGE_WEIGHT = 1;

    protected static double unitSize;

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

    private Vertex createVertex(int unitsX, int unitsY) {
        return new Vertex(unitsX, unitsY);
    }

    private Line connect(Vertex v1, Vertex v2) {
        Line line = new Line();

        line.startXProperty().bind(v1.getComponents().layoutXProperty().add(1.5 * unitSize));
        line.startYProperty().bind(v1.getComponents().layoutYProperty().add(1.5 * unitSize));

        line.endXProperty().bind(v2.getComponents().layoutXProperty().add(1.5 * unitSize));
        line.endYProperty().bind(v2.getComponents().layoutYProperty().add(1.5 * unitSize));

        line.setStroke(Color.WHITE);
        line.setStrokeWidth(1);
        line.setStrokeLineCap(StrokeLineCap.BUTT);

        return line;
    }

    @Override
    public void start(Stage stage) {
        Group root = new Group();
        Scene scene = new Scene(root, 50 * unitSize, 30 * unitSize, Color.DARKSLATEBLUE);

        // circles
        Vertex redCircle = createVertex(10, 5);
        Vertex blueCircle = createVertex(40, 10);
        Vertex greenCircle = createVertex(25, 20);

        Line line1 = connect(redCircle, blueCircle);
        Line line2 = connect(redCircle, greenCircle);
        Line line3 = connect(greenCircle, blueCircle);

        root.getChildren().addAll(redCircle.getComponents(), blueCircle.getComponents(), greenCircle.getComponents());

        // add the lines
        root.getChildren().add(line1);
        root.getChildren().add(line2);
        root.getChildren().add(line3);

        redCircle.getComponents().toFront();
        blueCircle.getComponents().toFront();
        greenCircle.getComponents().toFront();

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}