package numericalmethods;
//we will not bother with overridden equals or with mutators in this class, but will add the other elements
//of our Golden Rules
public class GoldenSearchException extends Exception {
    private double tol;
    private double err;
    private double x_l;
    private double x_u;
    private double x_i;

    public GoldenSearchException(double x_l, double x_u, double tol, double x_i)
    {
        super("value x = "+x_i+"returns illegal results");
        this.tol=tol;
        this.x_l= x_l;
        this.x_u= x_u;
        this.x_i = x_i;
    }
    public GoldenSearchException(GoldenSearchException source){
        super(source);
        this.tol=source.tol;
        this.err=source.err;
        this.x_l =source.x_l;
        this.x_u =source.x_u;
        this.x_i =source.x_i;
    }
    public GoldenSearchException clone(){
        return new GoldenSearchException(this);
    }
    public double getTol(){
        return this.tol;
    }
    public double getX_i(){
        return this.x_i;
    }
    public double getErr(){
        return this.err;
    }
    public double getX_l(){
        return this.x_l;
    }
    public double getX_u(){
        return this.x_u;
    }
}

