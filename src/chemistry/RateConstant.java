package chemistry;

public class RateConstant {
    //instance variables
    private double A;
    private double activationE;
    //public static final double R = 0.082057338; //L.atm.K-1.mol-1
    public static final double R = 8.3145; //L.atm.K-1.mol-1

    //constructor
    public RateConstant(double A, double activationE)
    {
        this.A = A;
        this.activationE = activationE;
    }

    //sets activation energy to zero by default
    public RateConstant(double A) {
        this.A = A;
        this.activationE = 0;
    }

    //copy constructor
    public RateConstant(RateConstant source)
    {
        this.A = source.A;
        this.activationE = source.activationE;
    }

    //clone
    public RateConstant clone()
    {
        return new RateConstant(this);
    }

    //accessors
    public double getA()
    {
        return this.A;
    }

    public double getActivationE()
    {
        return this.activationE;
    }

    //mutators
    public void setA(double A)
    {
        this.A = A;
    }

    public void setActivationE(double activationE)
    {
        this.activationE = activationE;
    }

    //equals
    public boolean equals(Object comparator) {
        if(comparator == null) return false;
        else if(this.getClass() != comparator.getClass()) return false;

        boolean isEquals = true;
        if(this.A != ((RateConstant) comparator).A) isEquals = false;
        if(this.activationE != ((RateConstant) comparator).activationE) isEquals = false;
        return isEquals;
    }

    public double returnRateConstant (double T)
    {
        double k = this.A*Math.exp(-this.activationE/(R*T));
        return k;
    }
}
