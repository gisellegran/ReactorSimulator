package chemistry;

//todo: should i extend power rate law instead of rate law

public class ReversibleRateLaw extends RateLaw {

    public static double R = 8.3145;

    //TODO: maybe make elementary rate law class - this would be related to the reversible
    //TODO: maybe make rate law builder to handle elementary rate law
    private double Keq; //equilibrium constant
    private double[] forwardOrders;
    private double[] backwardOrders;

    //constructor
    public ReversibleRateLaw(RateConstant k, Specie refSpecie, double Keq, double[] forwardOrders, double[] backwardOrders) {
        super(k, refSpecie);
        //TODO: error handling
        if(forwardOrders == null || backwardOrders == null) throw new IllegalArgumentException("null arrays passed as parameters");
        if ((forwardOrders.length != backwardOrders.length)) throw new IllegalArgumentException("array length mismatch between order arrays");

        this.Keq = Keq;
        //set forward and backwards array in same for loop since hey have the same length
        for (int i = 0; i < forwardOrders.length; i++) {
            this.forwardOrders[i] = forwardOrders[i];
            this.backwardOrders[i] = backwardOrders[i];

        }
    }

    //copy constructor
    public ReversibleRateLaw(ReversibleRateLaw source)
    {
        super(source);

        this.Keq = source.Keq;
        //set forward and backwards array in same for loop since hey have the same length
        for (int i = 0; i < source.forwardOrders.length; i++) {
            this.forwardOrders[i] = source.forwardOrders[i];
            this.backwardOrders[i] = source.backwardOrders[i];

        }
    }


    //accessor
    public double[] getForwardOrders() {

        double[] temp = new double[this.forwardOrders.length];
        for (int i = 0; i < this.forwardOrders.length; i++) {
            temp[i] = this.forwardOrders[i];
        }

        return temp;
    }
    public double[] getBackwardOrdersOrders() {

        double[] temp = new double[this.backwardOrders.length];
        for (int i = 0; i < this.backwardOrders.length; i++) {
            temp[i] = this.backwardOrders[i];
        }

        return temp;
    }


    //mutator
    public boolean setForwardOrders(double[] forwardOrders) {
        if(forwardOrders==null) return false;
        if(forwardOrders.length != this.forwardOrders.length) return false;
        for (int i = 0; i < forwardOrders.length; i++) {
            this.forwardOrders[i] = forwardOrders[i];
        }
        return true;
    }
    public boolean setBackwardOrders(double[] backwardOrders) {
        if(backwardOrders==null) return false;
        if(backwardOrders.length != this.backwardOrders.length) return false;
        for (int i = 0; i < backwardOrders.length; i++) {
            this.backwardOrders[i] = backwardOrders[i];
        }
        return true;
    }

    //calculate rate of reaction, returns rate without accounting if it is a consumption or formation rate
    public double returnRate(MultiComponentMixture mix) {
        //TOOD: error handling null mix

        //only need to check on one of the order array since they have the same length
        if (this.forwardOrders.length != mix.returnNumberOfSpecies()) throw new IllegalArgumentException("mismatch between orders and number of spsecies");
        double[] concentrations = mix.returnAllMolConcentrations();
        double k = super.getK().returnRateConstant(mix.getT());
        double forwardRate = k;
        double backwardRate = k/this.Keq;

        //one loop looping through forward and backwards reactions since they are the same legnth
        for (int i = 0; i < this.forwardOrders.length; i++) {
            forwardRate *= Math.pow(concentrations[i],this.forwardOrders[i]);
            backwardRate *= Math.pow(concentrations[i],this.forwardOrders[i]);
        }

        return forwardRate-backwardRate;

    }

    //clone
    public ReversibleRateLaw clone()
    {
        return new ReversibleRateLaw(this);
    }


    //equals
    public boolean equals(Object comparator) {
        if (!super.equals(comparator)) return false;
        if (!this.forwardOrders.equals(((ReversibleRateLaw)comparator).forwardOrders)) return false;
        if (!this.backwardOrders.equals(((ReversibleRateLaw)comparator).backwardOrders)) return false;

        return true;
    }

}
