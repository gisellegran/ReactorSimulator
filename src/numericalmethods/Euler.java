package numericalmethods;
public class Euler
{
    //this is a bare-bones solver using a VERY slow and unsophisticated algorithm - it is intended for illustrative purposes only
    public static double[] integrate(double x_0, double x_f, double[] y_0, double delx, int maxIt, SetOfODEs f)
    {
        double x=x_0;
        double[] y=new double[y_0.length];
        double[] dely;
        for(int j=0;j<y_0.length;j++)
            y[j]=y_0[j];
        int i=0;//counter for number of iterations during integration loop
        //communicate with the Function object, asking it for the right-hand-side values of the odes, given the current
        //values of x and y[]. This is accomplished through the Function object's calculateValue method
        //we limit the total number of integration steps to something less than maxIt, just in case delx is chosen to be
        //unreasonably small (this would be more important, say, in an adaptive step size routine, but is included here
        //for illustrative purposes)
        while(x<=x_f && i<=maxIt)
        {
            //note that there is no security leak with dely, even though it is assigned the memory address of the
            // array returned by calculateValue, because it is simply a temporary placeholder (local array) for this returned array
            //and because the array it is assigned to in the client's calculateValue is also a local one
            //dely is then used in the subsequent deep copying of y[j] that is later returned, thereby maintaining security
            dely=f.calculateValue(x,y);//copying array memory addresses here
            if(Math.abs(x+delx)>Math.abs(x_f)) delx=x_f-x;//correct final step if necessary
            for(int j=0;j<y.length;j++)//deep copying to solution array, y
                y[j]=y[j]+delx*dely[j];
            x=x+delx;
            i++;
        }
        if(i>maxIt+1) System.out.println("endpoint not reached within maximum specified iterations in Euler's integrate method");

        return y;
    }//end of integrate method
}//end of Euler class
