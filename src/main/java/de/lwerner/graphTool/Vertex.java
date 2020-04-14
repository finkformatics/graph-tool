package de.lwerner.graphTool;

import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class Vertex {

    private static int vertexCount;

    private final Circle circle;
    private final Text text;

    public Vertex(Circle circle, Text text) {
        this.circle = circle;
        this.text = text;
        this.text.setText("" + ++vertexCount);
    }

    public Circle getCircle() {
        return circle;
    }

    public Text getText() {
        return text;
    }

}
