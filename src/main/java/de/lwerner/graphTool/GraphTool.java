package de.lwerner.graphTool;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class GraphTool extends Application {

    private static final int UNITS_WIDTH = 50;
    private static final int UNITS_HEIGHT = 30;

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

    @Override
    public void start(Stage stage) {
        Group root = new Group();
        Scene scene = new Scene(root, UNITS_WIDTH * unitSize, UNITS_HEIGHT * unitSize, Color.DARKSLATEBLUE);

        Vertex v1 = new Vertex(10, 5);
        Vertex v2 = new Vertex(40, 10);
        Vertex v3 = new Vertex(25, 20);

        root.getChildren().addAll(v1.getUi(), v2.getUi(), v3.getUi());

        Edge v1v2 = new Edge(v1, v2);
        Edge v1v3 = new Edge(v1, v3);
        Edge v3v2 = new Edge(v3, v2);

        root.getChildren().add(v1v2.getUi());
        root.getChildren().add(v1v3.getUi());
        root.getChildren().add(v3v2.getUi());

        v1.getUi().toFront();
        v2.getUi().toFront();
        v3.getUi().toFront();

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}