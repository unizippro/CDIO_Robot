package group14.gui.components;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point2D;
import javafx.geometry.Side;
import javafx.scene.chart.NumberAxis;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;


public class CoordinateSystem extends Pane {
    private NumberAxis xAxis = new NumberAxis(0, 0, 1);
    private NumberAxis yAxis = new NumberAxis(0, 0, 1);

    private Pane content = new Pane();

    private DoubleProperty xBound = new SimpleDoubleProperty(5);
    private DoubleProperty yBound = new SimpleDoubleProperty(5);

    private Circle robot = new Circle();
    private Line cross1 = new Line();
    private Line cross2 = new Line();


    public CoordinateSystem() {
        DoubleProperty axisZeroProperty = new SimpleDoubleProperty(0);

        this.xAxis.setSide(Side.TOP);
        this.xAxis.setMinorTickVisible(false);

        this.xAxis.setLayoutX(0);
        this.xAxis.layoutYProperty().bind(axisZeroProperty.subtract(this.xAxis.heightProperty()));
        this.xAxis.prefWidthProperty().bind(this.widthProperty());

        this.xAxis.setLowerBound(0);
        this.xAxis.upperBoundProperty().bind(this.xBound);


        this.yAxis.setSide(Side.LEFT);
        this.yAxis.setMinorTickVisible(false);

        this.yAxis.setLayoutY(0);
        this.yAxis.layoutXProperty().bind(axisZeroProperty.subtract(this.yAxis.widthProperty()));
        this.yAxis.prefHeightProperty().bind(this.heightProperty());

        this.yAxis.setUpperBound(0);
        this.yAxis.lowerBoundProperty().bind(this.yBound.multiply(-1));


        this.content.prefWidthProperty().bindBidirectional(this.prefWidthProperty());
        this.content.prefHeightProperty().bindBidirectional(this.prefHeightProperty());

        this.getChildren().setAll(this.xAxis, this.yAxis, this.content);
    }


    public void setPoint(Point2D point) {
        Circle circle = new Circle();
        circle.setRadius(3);
        circle.setFill(Color.WHITE);

        circle.centerXProperty().bind(this.mapX(point.getX()));
        circle.centerYProperty().bind(this.mapY(point.getY()));

        this.content.getChildren().add(circle);
    }

    public void setRobot(Point2D robotPosition) {
        if (! this.content.getChildren().contains(this.robot)) {
            this.robot.setRadius(5);
            this.robot.setFill(Color.STEELBLUE);

            this.content.getChildren().add(this.robot);
        }

        this.robot.centerXProperty().bind(this.mapX(robotPosition.getX()));
        this.robot.centerYProperty().bind(this.mapY(robotPosition.getY()));
    }


    public void setCross(Point2D crossCenter) {
        if (! this.content.getChildren().contains(this.cross1)) {
            this.cross1.setStroke(Color.RED);
            this.cross1.setStrokeWidth(5);
            this.cross2.setStroke(Color.RED);
            this.cross2.setStrokeWidth(5);

            this.content.getChildren().add(this.cross1);
            this.content.getChildren().add(this.cross2);
        }

        this.cross1.startXProperty().bind(this.mapX(crossCenter.getX() - 1));
        this.cross1.endXProperty().bind(this.mapX(crossCenter.getX() + 1));
        this.cross1.startYProperty().bind(this.mapY(crossCenter.getY()));
        this.cross1.endYProperty().bind(this.mapY(crossCenter.getY()));

        this.cross2.startXProperty().bind(this.mapX(crossCenter.getX()));
        this.cross2.endXProperty().bind(this.mapX(crossCenter.getX()));
        this.cross2.startYProperty().bind(this.mapY(crossCenter.getY() - 1));
        this.cross2.endYProperty().bind(this.mapY(crossCenter.getY() + 1));
    }


    public void clearPoints() {
        this.content.getChildren().clear();
    }


    /**
     * XML Properties
     */

    public void setXBound(double value) {
        this.xBound.set(value);
    }

    public DoubleProperty getXBoundProperty() {
        return this.xBound;
    }

    public double getXBound() {
        return this.xBound.get();
    }

    public void setYBound(double value) {
        this.yBound.set(value);
    }

    public DoubleProperty getYBoundProperty() {
        return this.yBound;
    }

    public double getYBound() {
        return this.yBound.get();
    }


    /**
     * Private helpers
     */

    private DoubleBinding mapX(double x) {
        return new SimpleDoubleProperty(x)
                .multiply(this.widthProperty().divide(this.xBound));
    }


    private DoubleBinding mapY(double y) {
        return new SimpleDoubleProperty(y)
                .multiply(this.heightProperty().divide(this.yBound));
    }
}
