package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import method.Interpolatable;
import method.LagrangeInterpolator;
import method.SplineInterpolator;

import java.net.URL;
import java.util.*;
import java.util.function.UnaryOperator;

public class ChartController implements Initializable {

    private static final double START = 0;
    private static final double END = 10;
    private static final double INTERVAL = 2;

    @FXML
    private LineChart<Number, Number> lineChart;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        UnaryOperator<Double> mainFunction = Math::sin;

        Map<Double, Double> controlPoints = getControlPoints(mainFunction);
        List<Double> xPoints = new ArrayList<>(controlPoints.keySet());
        List<Double> yPoints = new ArrayList<>(controlPoints.values());

        drawMainFunction(mainFunction, "sin(x)");

        Interpolatable interpolator = SplineInterpolator.createMonotoneCubicSpline(xPoints, yPoints);
        drawInterpolation(interpolator, "Cubic spline");

        interpolator = LagrangeInterpolator.createLagrangePolinom(xPoints, yPoints);
        drawInterpolation(interpolator, "Lagrange polynomial");
    }

    private void drawMainFunction(UnaryOperator<Double> function, String name) {
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName(name);

        double i = START;
        while (Double.compare(i, END) <= 0) {
            series.getData().add(new XYChart.Data<>(i, function.apply(i)));
            i += 0.1;
        }

        lineChart.getData().add(series);
    }

    private Map<Double, Double> getControlPoints(UnaryOperator<Double> function) {
        Map<Double, Double> points = new LinkedHashMap<>();

        double i = START;
        while (Double.compare(i, END) <= 0) {
            points.put(i, function.apply(i));
            i += INTERVAL;
        }

        return points;
    }

    private void drawInterpolation(Interpolatable method, String name) {
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName(name);

        double i = START;
        while (Double.compare(i, END) <= 0) {
            series.getData().add(new XYChart.Data<>(i, method.interpolate(i)));
            i += 0.1;
        }

        lineChart.getData().add(series);
    }

}
