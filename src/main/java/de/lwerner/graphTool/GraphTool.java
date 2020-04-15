package de.lwerner.graphTool;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class GraphTool extends Application {

    private static final int UNITS_WIDTH = 50;
    private static final int UNITS_HEIGHT = 30;

    protected static enum ApplicationState {
        IDLE,
        NEW_VERTEX
    }

    protected static double unitSize;

    private static ApplicationState state = ApplicationState.IDLE;
    private static Vertex newVertex;
    private static Group root;
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

    private void drawGrid() {
        // TODO: Update grid on window resize

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
        root = new Group();
        Scene scene = new Scene(root, UNITS_WIDTH * unitSize, UNITS_HEIGHT * unitSize, Color.DARKSLATEBLUE);

        drawGrid();

        // Click helper => Better way of doing that?
        Pane pane = new Pane();
        pane.setLayoutX(0);
        pane.setLayoutY(0);
        pane.setPrefWidth(UNITS_WIDTH * unitSize);
        pane.setPrefHeight(UNITS_WIDTH * unitSize);
        pane.setOnMousePressed((t) -> {
            if (t.getButton() == MouseButton.SECONDARY && state == ApplicationState.IDLE) {
                // New vertex
                // TODO: Dashed line while creating new vertex
                state = ApplicationState.NEW_VERTEX;
                newVertex = new Vertex(
                        t.getSceneX() / unitSize - Vertex.CIRCLE_RADIUS,
                        t.getSceneY() / unitSize - Vertex.CIRCLE_RADIUS
                );
                newVertex.setDashed(true);
                newVertex.setSelected(true);
                root.getChildren().add(newVertex.getUi());
            } else if (t.getButton() == MouseButton.PRIMARY && state == ApplicationState.IDLE) {
                // Clear selection
                System.out.println("Clear selection");
            } else if (t.getButton() == MouseButton.SECONDARY && state == ApplicationState.NEW_VERTEX) {
                // Remove new vertex
                root.getChildren().remove(newVertex.getUi());
                Vertex.decrement();
                newVertex = null;

                state = ApplicationState.IDLE;
            }
        });
        root.getChildren().add(pane);

        rootVertex = new Vertex(
                UNITS_WIDTH / 2.0 - Vertex.CIRCLE_RADIUS,
                UNITS_HEIGHT / 2.0 - Vertex.CIRCLE_RADIUS
        );
        root.getChildren().add(rootVertex.getUi());

        stage.setScene(scene);
        stage.setTitle("Graph Tool v0.1");
        stage.show();
    }

    public static ApplicationState getState() {
        return state;
    }

    public static Vertex getNewVertex() {
        return newVertex;
    }

    public static void connectNewVertex(Vertex vertex) {
        Edge edge = new Edge(vertex, newVertex);
        root.getChildren().add(edge.getUi());
        state = ApplicationState.IDLE;
        vertex.getUi().toFront();
        newVertex.getUi().toFront();
        newVertex.setDashed(false);
        newVertex.setSelected(false);
    }

    public static void main(String[] args) {
        launch();
    }

}