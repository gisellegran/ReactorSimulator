package numericalmethods;
//we will not bother with overridden equals or with mutators in this class, but will add the other elements
//of our Golden Rules
public class RootFindingException extends Exception {
    private double tol;
    private long maxIt;
    private double err;
    private double x_lower;
    private double x_upper;

    public RootFindingException(double x_l, double x_u, double tol, long maxIt, double err)
    {
        super("Failure to converge on root to within specifications of tolerance="+tol+" maximum iterations:"+maxIt);
        this.tol=tol;
        this.maxIt=maxIt;
        this.err=err;
        this.x_lower= x_l;
        this.x_upper= x_u;
        //add more code...
    }
    public RootFindingException(RootFindingException source){
        super(source);
        this.tol=source.tol;
        this.maxIt=source.maxIt;
        this.err=source.err;
        this.x_lower=source.x_lower;
        this.x_upper=source.x_upper;
    }
    public RootFindingException clone(){
        return new RootFindingException(this);
    }
    public double getTol(){
        return this.tol;
    }
    public long getMaxIt(){
        return this.maxIt;
    }
    public double getErr(){
        return this.err;
    }
    public double getX_lower(){
        return this.x_lower;
    }
    public double getX_upper(){
        return this.x_upper;
    }
}
