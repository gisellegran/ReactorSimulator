package numericalmethods;

public class GoldenSectionSearch {
    private static final double r = (3 - Math.sqrt(5))/2;

    public static double search(double x_0, double x_f, double eps, NonLinearEquation f) {
        double c, d, z, yc, yd;
        c = x_0 + r * (x_f - x_0);
        yc = f.returnEquationResult(c);
        d = x_0 + (1 - r) * (x_f - x_0);
        yd = f.returnEquationResult(d);
        while ((d - c) >Math.sqrt(eps) * Math.max(Math.abs(c), Math.abs(d))) {
            if (yc >= yd) {
                z = c + (1 - r) * (x_f - c);
                // [x_0 c d x_f ] <--- [c d z x_f]
                x_0 = c;
                c = d;
                yc = yd;
                d = z;
                yd = f.returnEquationResult(z);
            }
            else {
                z = x_0 + r * (d - x_0);
                // [x_0 c d x_f ] <--- [x_0 z c d]
                x_f = d;
                d = c;
                yd = yc;
                c = z;
                yc = f.returnEquationResult(z);
            }
        }
        double x_min = (c + d) / 2;
        return x_min;
    }
}
