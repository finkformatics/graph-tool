package de.lwerner.graphTool;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import javafx.stage.Screen;
import javafx.stage.Stage;

import static de.lwerner.graphTool.Vertex.CIRCLE_RADIUS;

public class GraphTool extends Application {

    private static final int UNITS_WIDTH = 50;
    private static final int UNITS_HEIGHT = 30;

    protected static enum ApplicationState {
        IDLE,
        NEW_VERTEX,
        CONNECT_VERTICES,
    }

    protected static double unitSize;

    private static ApplicationState state = ApplicationState.IDLE;
    private static Vertex startVertex;
    private static Vertex newVertex;
    private static Line selectionLine;
    private static Group root;
    private static GraphTool instance;
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

        instance = this;
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
                state = ApplicationState.NEW_VERTEX;
                newVertex = new Vertex(
                        t.getSceneX() / unitSize - Vertex.CIRCLE_RADIUS,
                        t.getSceneY() / unitSize - Vertex.CIRCLE_RADIUS
                );
                newVertex.setDashed(true);
                newVertex.setSelected(true);
                root.getChildren().add(newVertex.getUi());
                createSelectionLine(newVertex, t);
                newVertex.getUi().toFront();
            } else if (t.getButton() == MouseButton.PRIMARY && state == ApplicationState.IDLE) {
                // Clear selection
                System.out.println("Clear selection");
            } else if (t.getButton() == MouseButton.SECONDARY && state == ApplicationState.NEW_VERTEX) {
                // Remove new vertex
                root.getChildren().remove(newVertex.getUi());
                Vertex.decrement();
                newVertex = null;

                removeSelectionLine();

                state = ApplicationState.IDLE;
            } else if (t.getButton() == MouseButton.SECONDARY && state == ApplicationState.CONNECT_VERTICES) {
                removeSelectionLine();
                state = ApplicationState.IDLE;
                startVertex.setSelected(false);
            }
        });
        pane.setOnMouseMoved((t) -> {
            if (state == ApplicationState.NEW_VERTEX || state == ApplicationState.CONNECT_VERTICES) {
                selectionLine.setEndX(t.getSceneX() + 1);
                selectionLine.setEndY(t.getSceneY() + 1);
                newVertex.getUi().toFront();
                for (Vertex v: rootVertex.getAllConnectedVertices()) {
                    v.getUi().toFront();
                }
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

    private static void createSelectionLine(Vertex startVertex, MouseEvent event) {
        selectionLine = new Line();
        selectionLine.setStroke(Color.LIGHTCORAL);
        selectionLine.setStrokeWidth(1);
        selectionLine.setStrokeLineCap(StrokeLineCap.BUTT);
        selectionLine.getStrokeDashArray().addAll(3d);
        selectionLine.startXProperty().bind(startVertex.getUi().layoutXProperty().add(CIRCLE_RADIUS * unitSize));
        selectionLine.startYProperty().bind(startVertex.getUi().layoutYProperty().add(CIRCLE_RADIUS * unitSize));
        selectionLine.setEndX(event.getSceneX());
        selectionLine.setEndY(event.getSceneY());
        root.getChildren().add(selectionLine);
    }

    public static ApplicationState getState() {
        return state;
    }

    public static void startConnection(Vertex vertex, MouseEvent event) {
        state = GraphTool.ApplicationState.CONNECT_VERTICES;
        startVertex = vertex;
        createSelectionLine(vertex, event);
    }

    public static void endConnection(Vertex endVertex) {
        Edge edge = new Edge(startVertex, endVertex);
        root.getChildren().add(edge.getUi());
        state = ApplicationState.IDLE;
        removeSelectionLine();
        startVertex.setSelected(false);
        endVertex.setSelected(false);
        startVertex.getUi().toFront();
        endVertex.getUi().toFront();
        startVertex = null;
    }

    public static Vertex getNewVertex() {
        return newVertex;
    }

    public static Vertex getStartVertex() {
        return startVertex;
    }

    public static Line getSelectionLine() {
        return selectionLine;
    }

    public static void connectNewVertex(Vertex vertex) {
        Edge edge = new Edge(vertex, newVertex);
        root.getChildren().add(edge.getUi());
        state = ApplicationState.IDLE;
        vertex.getUi().toFront();
        newVertex.getUi().toFront();
        newVertex.setDashed(false);
        newVertex.setSelected(false);
        removeSelectionLine();
    }

    private static void removeSelectionLine() {
        root.getChildren().remove(selectionLine);
        selectionLine.startXProperty().unbind();
        selectionLine.endXProperty().unbind();
        selectionLine = null;
    }

    public static void main(String[] args) {
        launch();
    }

}