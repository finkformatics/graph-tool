package de.lwerner.graphTool;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;

import static de.lwerner.graphTool.Vertex.CIRCLE_RADIUS;
import static de.lwerner.graphTool.GraphTool.unitSize;

public class Edge {

    public static final int DEFAULT_WEIGHT = 1;

    // UI
    private final Line ui;

    // Edge properties
    private final Vertex v1;
    private final Vertex v2;
    private int weight = DEFAULT_WEIGHT;

    public Edge(Vertex v1, Vertex v2) {
        this.v1 = v1;
        this.v2 = v2;

        this.v1.addEdge(this);
        this.v2.addEdge(this);

        ui = new Line();

        buildUi();
    }

    private void buildUi() {
        ui.startXProperty().bind(v1.getUi().layoutXProperty().add(CIRCLE_RADIUS * unitSize));
        ui.startYProperty().bind(v1.getUi().layoutYProperty().add(CIRCLE_RADIUS * unitSize));

        ui.endXProperty().bind(v2.getUi().layoutXProperty().add(CIRCLE_RADIUS * unitSize));
        ui.endYProperty().bind(v2.getUi().layoutYProperty().add(CIRCLE_RADIUS * unitSize));

        ui.setStroke(Color.WHITE);
        ui.setStrokeWidth(1);
        ui.setStrokeLineCap(StrokeLineCap.BUTT);
    }

    public Line getUi() {
        return ui;
    }

    public Vertex getV1() {
        return v1;
    }

    public Vertex getV2() {
        return v2;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Edge edge = (Edge) o;

        if (!v1.equals(edge.v1)) return false;
        return v2.equals(edge.v2);
    }

    @Override
    public int hashCode() {
        int result = v1.hashCode();
        result = 31 * result + v2.hashCode();
        return result;
    }
}
