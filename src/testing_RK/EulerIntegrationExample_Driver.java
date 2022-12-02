package testing_RK;

import java.text.DecimalFormat;
public class EulerIntegrationExample_Driver {

    public static void main(String[] args)
    {
        double[] x= {0.1,0.2,0.3,0.4,0.5,0.6,0.7,0.8,0.9,1.};

        double x_0=0.;
        double[] y_0={0.5,1.};
        double[] coefficients={2.,1.};

        //euler test

        SampleODEProblemEuler sample=new SampleODEProblemEuler(coefficients);
        double[][] solution = sample.solveProblem(x,0.00001,x_0,y_0);
        DecimalFormat df = new DecimalFormat("#00.000");
        double y1A;
        double y2A;
        System.out.println("x[i]    y_1[i]   y_1A[i]  y_2[i]   y_2A[i]");
        for(int i=0;i<x.length;i++) {
            //dy1A/dx=coefficients[0]*x/y
            //y1A=Math.sqrt(coefficients[0]*Math.pow(x[i],2)+0.5.*Math.pow(y_0[0],2))
            y1A=Math.sqrt(coefficients[0]*Math.pow(x[i],2)+Math.pow(y_0[0],2));
            //dy2A/dx=coefficients[1]*Math.exp(x[i])/y[1]
            //y2A=Math.sqrt(2.*coefficients[1]*Math.exp(x[i])-2.*coefficients[1]+Math.pow(y_0[1],2));
            y2A=Math.sqrt(2.*coefficients[1]*Math.exp(x[i])-2.*coefficients[1]+Math.pow(y_0[1],2));
            System.out.println(df.format(x[i]) + "  " + df.format(solution[i][0]) + "   " + df.format(y1A) + "   "
                    + df.format(solution[i][1])+"   "+df.format(y2A));
        }

        SampleODEProblemRK sample1=new SampleODEProblemRK(coefficients);
        double[][] solution1 = sample1.solveProblem(x,0.01,x_0,y_0);
        System.out.println("\nx[i]    y_1[i]   y_1A[i]  y_2[i]   y_2A[i]");
        for(int i=0;i<x.length;i++) {
            //dy1A/dx=coefficients[0]*x/y
            //y1A=Math.sqrt(coefficients[0]*Math.pow(x[i],2)+0.5.*Math.pow(y_0[0],2))
            y1A=Math.sqrt(coefficients[0]*Math.pow(x[i],2)+Math.pow(y_0[0],2));
            //dy2A/dx=coefficients[1]*Math.exp(x[i])/y[1]
            //y2A=Math.sqrt(2.*coefficients[1]*Math.exp(x[i])-2.*coefficients[1]+Math.pow(y_0[1],2));
            y2A=Math.sqrt(2.*coefficients[1]*Math.exp(x[i])-2.*coefficients[1]+Math.pow(y_0[1],2));
            System.out.println(df.format(x[i]) + "  " + df.format(solution1[i][0]) + "   " + df.format(y1A) + "   "
                    + df.format(solution1[i][1])+"   "+df.format(y2A));
        }
    }
}
