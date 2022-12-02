package chemistry;

import java.util.Map;

public class ReversibleRateLaw extends RateLaw {

    public static double R = 8.3145;

    //TODO: maybe make elementary rate law class - this would be related to the reversible
    //TODO: maybe make rate law builder to handle elementary rate law
    private double Keq; //equilibrium constant
    private SpecieMap forwardOrders;
    private SpecieMap backwardOrders;

    //constructor
    public ReversibleRateLaw(RateConstant k, Specie refSpecie, double Keq, SpecieMap forwardOrders, SpecieMap backwardOrders) {
        super(k, refSpecie);
        //TODO: error handling
        if(forwardOrders == null || backwardOrders == null) System.exit(0);
        this.Keq = Keq;
        this.forwardOrders = forwardOrders.clone();
        this.backwardOrders = forwardOrders.clone();
    }

    //copy constructor
    public ReversibleRateLaw(ReversibleRateLaw source)
    {
        super(source);
        this.forwardOrders = forwardOrders.clone();
        this.backwardOrders = forwardOrders.clone();
    }


    //accessor
    public SpecieMap getForwardOrders() {
        return this.forwardOrders.clone();
    }
    public SpecieMap getBackwardOrdersOrders() {
        return this.backwardOrders.clone();
    }


    //mutator
    public boolean setForwardOrders(SpecieMap forwardOrders) {
        if(forwardOrders==null) return false;
        this.forwardOrders = forwardOrders.clone();
        return true;
    }
    public boolean setBackwardOrders(SpecieMap backwardOrders) {
        if(backwardOrders==null) return false;
        this.backwardOrders = backwardOrders.clone();
        return true;
    }

    //calculate rate of reaction, returns rate without accounting if it is a consumption or formation rate
    public double returnRate(double T, double[] concentrations) {

        //TODO: checl that all the species in the reaction are in the concentration map else throw error
        double forwardRate = 0.;
        double backwardRate = 0.;
        double k = super.getK().returnRateConstant(T);
        //iterate through the elements in the map
        for  (Map.Entry<Specie,Double> elem : this.forwardOrders.entrySet()) {
            forwardRate *= Math.pow(concentrations.get(elem.getKey()),elem.getValue());
        }

        for  (Map.Entry<Specie,Double> elem : this.backwardOrders.entrySet()) {
            backwardRate *= Math.pow(concentrations.get(elem.getKey()),elem.getValue());
        }

        //r = k*(forwardRate - backwardRate/Keq)
        return k*(forwardRate - backwardRate/Keq);

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
