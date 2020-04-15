package de.lwerner.graphTool;

import javafx.scene.Cursor;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import static de.lwerner.graphTool.GraphTool.unitSize;

public class Vertex {

    private static int vertexCount;

    private final StackPane components;
    private final Circle circle;
    private final Text text;

    private int unitsX;
    private int unitsY;

    private double orgSceneX;
    private double orgSceneY;

    public Vertex(int unitsX, int unitsY) {
        this.unitsX = unitsX;
        this.unitsY = unitsY;

        this.circle = new Circle(1.5 * unitSize, Color.DARKSLATEBLUE);
        this.text = new Text("" + ++vertexCount);
        this.components = new StackPane();

        buildComponents();
    }

    private void buildComponents() {
        components.setLayoutX(unitsX * unitSize);
        components.setLayoutY(unitsY * unitSize);

        circle.setStroke(Color.WHITE);
        circle.setStrokeWidth(1);

        text.setFont(Font.font(unitSize));
        text.setFill(Color.WHITE);

        circle.setCursor(Cursor.HAND);
        text.setCursor(Cursor.HAND);

        circle.setOnMousePressed((t) -> {
            orgSceneX = t.getSceneX();
            orgSceneY = t.getSceneY();
        });

        text.setOnMousePressed((t) -> {
            orgSceneX = t.getSceneX();
            orgSceneY = t.getSceneY();

            components.toFront();
        });

        circle.setOnMouseDragged((t) -> {
            double offsetX = t.getSceneX() - orgSceneX;
            double offsetY = t.getSceneY() - orgSceneY;

            components.relocate(components.getLayoutX() + offsetX, components.getLayoutY() + offsetY);

            orgSceneX = t.getSceneX();
            orgSceneY = t.getSceneY();

            components.toFront();
        });

        text.setOnMouseDragged((t) -> {
            double offsetX = t.getSceneX() - orgSceneX;
            double offsetY = t.getSceneY() - orgSceneY;

            components.relocate(components.getLayoutX() + offsetX, components.getLayoutY() + offsetY);

            orgSceneX = t.getSceneX();
            orgSceneY = t.getSceneY();

            components.toFront();
        });

        components.getChildren().addAll(circle, text);
    }

    public Circle getCircle() {
        return circle;
    }

    public Text getText() {
        return text;
    }

    public StackPane getComponents() {
        return components;
    }

}
