package testing_RK;

import numericalmethods.CKRK45;
import numericalmethods.SetOfODEs;

public class SampleProblem1 implements SetOfODEs {

    private double[] coefficients;

    public SampleProblem1(double[] coefficients){
        if(coefficients ==null) throw new IllegalArgumentException("null array");

        this.coefficients = new double[coefficients.length];
        for (int i = 0; i< coefficients.length; i++)
            this.coefficients[i]= coefficients[i];
    }
    public double[][] solveProblem(double[] x, double delX, double x_0, double[] y_0) {

        double[][] solution = new double[x.length][y_0.length];
        double[] y_init = new double[y_0.length];
        //make local deep copy of y_0
        for (int i = 0; i < y_0.length; i++)
            y_init[i] = y_0[i];
        for (int i = 0; i < x.length; i++) {
            solution[i] = CKRK45.integrate(x_0, x[i], y_init, delX, 100, Math.pow(10., -5), 0.9, this);

            //update starting point and end point to integrate to the next position in x
            x_0 = x[i];
            //update y[] starting points for next integration to be y[] endpoints of previous integration interval
            for (int j = 0; j < y_0.length; j++)
                y_init[j] = solution[i][j];
        }
        return solution;


    }


    public double[] calculateValue(double x, double[] y){
        double result[] = new double[3];
        result[0] = coefficients[0]*(y[1]-y[0]);
        result[1] = y[0]*(coefficients[1]-y[2])-y[1];
        result[2] = y[0]*y[1]-coefficients[2]*y[2];
        return result;
    }

}