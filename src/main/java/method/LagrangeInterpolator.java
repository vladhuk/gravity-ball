package method;

import java.util.List;

public class LagrangeInterpolator implements Interpolatable{

    private final List<Double> xValues;
    private final List<Double> yValues;

    private LagrangeInterpolator(List<Double> x, List<Double> y) {


        xValues = x;
        yValues = y;

    }

    public static LagrangeInterpolator createLagrangePolinom(List<Double> x, List<Double> y) {
        if (x == null || y == null || x.size() != y.size()) {
            throw new IllegalArgumentException("Error");
        }

        return new LagrangeInterpolator(x, y);
    }

    @Override
    public double interpolate(double x) {
        double lagrangePol = 0;

        for (int i = 0; i < xValues.size(); i++)
        {
            double basicsPol = 1;

            for (int j = 0; j < xValues.size(); j++)
            {
                if (j != i)
                {
                    basicsPol *= (x - xValues.get(j))/(xValues.get(i) - xValues.get(j));
                }
            }
            lagrangePol += basicsPol * yValues.get(i);
        }

        return lagrangePol;
    }
}
