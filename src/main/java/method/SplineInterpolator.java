package method;

import java.util.List;

public class SplineInterpolator implements Interpolatable {

    private final List<Double> mX;
    private final List<Double> mY;
    private final double[] mM;

    private SplineInterpolator(List<Double> x, List<Double> y, double[] m) {


        mX = x;
        mY = y;
        mM = m;

    }

    public static SplineInterpolator createMonotoneCubicSpline(List<Double> x, List<Double> y) {
        if (x == null || y == null || x.size() != y.size() || x.size() < 2) {
            throw new IllegalArgumentException("Повинно бути щонайменше дві контрольні точки і ці точки повинні мати як x так і y ");
        }

        final int n = x.size();
        double[] d = new double[n - 1];
        double[] m = new double[n];


        for (int i = 0; i < n - 1; i++) {
            double h = x.get(i + 1) - x.get(i);
            if (h <= 0f) {
                throw new IllegalArgumentException("Контрольні точки мають мати строго зростаючу Х-координату");
            }
            d[i] = (y.get(i + 1) - y.get(i)) / h;
        }


        m[0] = d[0];
        for (int i = 1; i < n - 1; i++) {
            m[i] = (d[i - 1] + d[i]) * 0.5f;
        }
        m[n - 1] = d[n - 2];

        // Оновлення дотичних для збереження монотонності.
        for (int i = 0; i < n - 1; i++) {
            if (d[i] == 0f) { // послідовні значення Y рівні
                m[i] = 0f;
                m[i + 1] = 0f;
            } else {
                double a = m[i] / d[i];
                double b = m[i + 1] / d[i];
                float h = (float) Math.hypot(a, b);
                if (h > 9f) {
                    float t = 3f / h;
                    m[i] = t * a * d[i];
                    m[i + 1] = t * b * d[i];
                }
            }
        }
        return new SplineInterpolator(x, y, m);
    }

    @Override
    public double interpolate(double x) {
        // Обробка граничних випадкыв
        final int n = mX.size();
        if (Double.isNaN(x)) {
            return x;
        }
        if (x <= mX.get(0)) {
            return mY.get(0);
        }
        if (x >= mX.get(n - 1)) {
            return mY.get(n - 1);
        }

        // Знайдіть індекса "i" останньої точки з меншим X.
        // Ми знаємо, що це відбуватиметься в межах сплайна за рахунок граничних випробувань.
        int i = 0;
        while (x >= mX.get(i + 1)) {
            i += 1;
            if (x == mX.get(i)) {
                return mY.get(i);
            }
        }

        // Виконується кубічна сплайн-інтерполяція.
        double h = mX.get(i + 1) - mX.get(i);
        double t = (x - mX.get(i)) / h;
        return (mY.get(i) * (1 + 2 * t) + h * mM[i] * t) * (1 - t) * (1 - t)
                + (mY.get(i + 1) * (3 - 2 * t) + h * mM[i + 1] * (t - 1)) * t * t;
    }

}
