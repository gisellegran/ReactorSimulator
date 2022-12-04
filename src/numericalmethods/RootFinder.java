package numericalmethods;

public class RootFinder
{
    public static double findRoot(double x_0, double x_f, double tol, long maxIt, NonLinearEquation e)
    {
        double x_lower=x_0;
        double x_upper=x_f;
        double x_mid;
        double err;
        double f_lower=e.returnValue(x_lower);
        double f_upper=e.returnValue(x_upper);
        double f_mid;
        long iter=0;
        do
        {
            x_mid=(x_upper+x_lower)/2.;
            f_mid=e.returnValue(x_mid);
            if(f_lower*f_mid<0.)
            {
                f_upper=f_mid;
                x_upper=x_mid;
            }
            else
            {
                f_lower=f_mid;
                x_lower=x_mid;
            }
            err=Math.abs(x_upper-x_lower);
            if(Math.abs(f_mid)<1.e-8)
            {
                err=0.;
                break;
            }
            iter++;
        } while(err>tol && iter<maxIt);
        if(err>tol) {};
        return x_mid;
    }//end of findRoot

}//end of RootFinder

