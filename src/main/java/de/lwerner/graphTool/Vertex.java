package de.lwerner.graphTool;

import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.HashSet;
import java.util.Set;

import static de.lwerner.graphTool.GraphTool.unitSize;

public class Vertex {

    public static final double CIRCLE_RADIUS = 1.5;

    private static int vertexCount;

    // UI
    private final StackPane ui;
    private final Circle circle;
    private final Text text;

    // Event helpers
    private double orgSceneX;
    private double orgSceneY;
    private double orgLayoutX;
    private double orgLayoutY;

    // Vertex properties
    private final Set<Edge> edges;

    public Vertex(int unitsX, int unitsY) {
        this.circle = new Circle(CIRCLE_RADIUS * unitSize, Color.DARKSLATEBLUE);
        this.text = new Text("" + ++vertexCount);
        this.ui = new StackPane();

        edges = new HashSet<>();

        buildUi(unitsX, unitsY);
    }

    private void buildUi(int unitsX, int unitsY) {
        ui.setLayoutX(unitsX * unitSize);
        ui.setLayoutY(unitsY * unitSize);

        circle.setStroke(Color.WHITE);
        circle.setStrokeWidth(1);

        text.setFont(Font.font(unitSize));
        text.setFill(Color.WHITE);

        circle.setCursor(Cursor.HAND);
        text.setCursor(Cursor.HAND);

        text.setOnMousePressed(this::mousePressed);
        text.setOnMouseDragged(this::mouseDragged);
        circle.setOnMousePressed(this::mousePressed);
        circle.setOnMouseDragged(this::mouseDragged);

        ui.getChildren().addAll(circle, text);
    }

    public StackPane getUi() {
        return ui;
    }

    public void addEdge(Edge edge) {
        edges.add(edge);
    }

    private void mousePressed(MouseEvent event) {
        orgSceneX = event.getSceneX();
        orgSceneY = event.getSceneY();
        orgLayoutX = ui.getLayoutX();
        orgLayoutY = ui.getLayoutY();

        ui.toFront();
    }

    private void mouseDragged(MouseEvent event) {
        double offsetX = event.getSceneX() - orgSceneX;
        double offsetY = event.getSceneY() - orgSceneY;

        double newX = orgLayoutX + offsetX;
        double newY = orgLayoutY + offsetY;

        if (event.isShiftDown()) {
            double newXUnits = Math.round(newX / unitSize);
            double newYUnits = Math.round(newY / unitSize);
            newX = newXUnits * unitSize;
            newY = newYUnits * unitSize;
        }

        ui.relocate(newX, newY);

        ui.toFront();
    }

}
