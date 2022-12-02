package testing_RK;

import java.text.DecimalFormat;

public class EulerIntegrationExample_Driver1 {

    public static void main(String[] args)
    {
        double[] x= new double[20];
        x[0] = 0;
        for (int i = 1; i < x.length; i++) {
            x[i] = x[i-1]+0.01;
        }

        double x_0=0.;
        double[] y_0={1.,1.,1.};
        double[] coefficients={10.,27., 8./3.};


        SampleProblem1 problem = new SampleProblem1(coefficients);
        double[][] solution = problem.solveProblem(x,0.001,x_0,y_0);

        DecimalFormat df = new DecimalFormat("#00.00000");
        double y1A;
        double y2A;
        System.out.println("x[i]    y_1[i]    y_2[i]   y_3[i]");
        for(int i=0;i<x.length;i++) {

            System.out.println(df.format(x[i]) + "  " + df.format(solution[i][0]) + "   " + df.format(solution[i][1]) + "   "
                    + df.format(solution[i][2]));
        }

    }




}

