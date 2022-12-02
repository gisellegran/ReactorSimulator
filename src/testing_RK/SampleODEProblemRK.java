package testing_RK;//The numerical procedure used here is contained in a package called "chg4343.numericalmethods".
//The files must therefore be located in the subfolder chg4343/numericalmethods/


import numericalmethods.CKRK45;
import numericalmethods.SetOfODEs;

public class SampleODEProblemRK implements SetOfODEs
{
    //this example integrates a set of equations dy[i]/dx==f_i(x,y[i]):
    // dy[0]/dx=coefficients[0]*x/y[0];
    //dy[1]/dx=coefficients[1]*exp(x)/y[1]
    private double[] coefficients;
    //standard constructors, etc
    public SampleODEProblemRK(double[] coefficients)
    {
        if(coefficients ==null) throw new IllegalArgumentException("null array");
        if(coefficients.length!=2) throw new IllegalArgumentException("invalid array");
        this.coefficients = new double[coefficients.length];
        for (int i = 0; i< coefficients.length; i++)
        this.coefficients[i]= coefficients[i];
    }

    public SampleODEProblemRK(SampleODEProblemRK source)
    {
        if(source==null) throw new IllegalArgumentException("invalid source object");
        this.coefficients = new double[source.coefficients.length];
        for (int i = 0; i< coefficients.length; i++)
            this.coefficients[i]=source.coefficients[i];
    }

    public double[] getCoefficients()

    {
        coefficients = new double[this.coefficients.length];
        for (int i = 0; i< coefficients.length; i++)
            coefficients[i]=this.coefficients[i];
        return coefficients;
    }

    public boolean setCoefficients(double[] coefficients)
    {
        if(coefficients==null) return false;
        if(coefficients.length!=2) return false;
        this.coefficients = new double[coefficients.length];
        for (int i=0;i<coefficients.length;i++)
            this.coefficients[i]=coefficients[i];
        return true;
    }

    public boolean equals(Object comparator)
    {
        if(comparator==null) return false;
        if(this.getClass()!=comparator.getClass()) return false;
        if(this.coefficients.length!=(((SampleODEProblemRK)comparator).coefficients.length)) return false;
        for (int i = 0; i<this.coefficients.length; i++)
            if(this.coefficients[i]!=(((SampleODEProblemRK)comparator).coefficients[i])) return false;
            return true;
    }

   //This is the method associated with the engineering problem (e.g. integrating along the length of a PFR),
   //which itself involves integrating the set of O.D.Es associated with the problem.
   //We will assume that there is a set of values for x, stored in an array by that name, that contains
   //the positions at which we want a solution.
   //We make use of the class Euler that is part of the package chg4343.numerical. This requires that the class
   //implement the Function interface, which in turn requires it to define the right-hand-sides of the O.D.E.s, as
    //described below. Because Euler only integrates between x_0 and x_f (the designated "final point"),
   //after each integration step we update x_0 to equal the previous endpoint, x[i], and set y_init[j] (initially y_0)
   //to the current solution values, solution[i][j]. (Counter j represents the number of equations in the O.D.E. set)
   //We then return solution, which contains the y[j] values associated with the x[i] values.
    public double[][] solveProblem(double[] x, double delX,double x_0, double[] y_0) {

        double[][] solution = new double[x.length][y_0.length];
        double[] y_init=new double[y_0.length];
        //make local deep copy of y_0
        for(int i=0;i<y_0.length;i++)
            y_init[i]=y_0[i];
        for (int i = 0; i < x.length; i++) {
            solution[i] = CKRK45.integrate(x_0, x[i], y_init, delX, 50000, Math.pow(10., -9), 0.9, this);

            //update starting point and end point to integrate to the next position in x
            x_0=x[i];
            //update y[] starting points for next integration to be y[] endpoints of previous integration interval
            for(int j=0;j<y_0.length;j++)
            y_init[j]=solution[i][j];
        }
        return solution;
    }
    //Euler requires that the class implement the Function interface, which in turn requires it to define
    //the right-hand-side of the O.D.E.s(Note here that x in Function corresponds to the current value of
    //x and the array y represents the current value of the dependent variables.)
    @Override
    public double[] calculateValue(double x, double[] y)
    {
        if(y.length!=this.coefficients.length)
            throw new IllegalArgumentException("calculateDelT array y length must match number of slopes entries");
        double[] solution = new double[y.length];
        //assume functionality mentioned at top of class, with two entries
            solution[0]=this.coefficients[0]*x/y[0];
            solution[1]=this.coefficients[1]*Math.exp(x)/(y[1]);
        return solution;
    }
}
