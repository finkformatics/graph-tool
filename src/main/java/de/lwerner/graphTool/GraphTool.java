package de.lwerner.graphTool;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class GraphTool extends Application {

    private static final int UNITS_WIDTH = 50;
    private static final int UNITS_HEIGHT = 30;

    protected static double unitSize;

    private Vertex rootVertex;

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

    private void drawGrid(Group root) {
        // Verticals
        for (int x = 0; x < UNITS_WIDTH; x++) {
            Line line = new Line(x * unitSize, 0, x * unitSize, UNITS_HEIGHT * unitSize);
            line.setStroke(Color.MIDNIGHTBLUE);
            line.getStrokeDashArray().addAll(3d);
            root.getChildren().add(line);
        }

        // Horizontals
        for (int y = 0; y < UNITS_HEIGHT; y++) {
            Line line = new Line(0, y * unitSize, UNITS_WIDTH * unitSize, y * unitSize);
            line.setStroke(Color.MIDNIGHTBLUE);
            line.getStrokeDashArray().addAll(3d);
            root.getChildren().add(line);
        }
    }

    @Override
    public void start(Stage stage) {
        Group root = new Group();
        Scene scene = new Scene(root, UNITS_WIDTH * unitSize, UNITS_HEIGHT * unitSize, Color.DARKSLATEBLUE);

        drawGrid(root);

        rootVertex = new Vertex(10, 5);
        Vertex v2 = new Vertex(40, 10);
        Vertex v3 = new Vertex(25, 20);

        root.getChildren().addAll(rootVertex.getUi(), v2.getUi(), v3.getUi());

        Edge v1v2 = new Edge(rootVertex, v2);
        Edge v1v3 = new Edge(rootVertex, v3);
        Edge v3v2 = new Edge(v3, v2);

        root.getChildren().add(v1v2.getUi());
        root.getChildren().add(v1v3.getUi());
        root.getChildren().add(v3v2.getUi());

        rootVertex.getUi().toFront();
        v2.getUi().toFront();
        v3.getUi().toFront();

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}