package chemistry;

import java.util.Map;

//TODO: maybe make elementary rate law class
public class PowerRateLaw extends RateLaw {
    private SpecieMap orders;

    //constructor
    public PowerRateLaw(RateConstant k, Specie refSpecie, SpecieMap orders)
    {
        super(k, refSpecie);
        //TODO: error handling
        if(orders == null) System.exit(0);

        this.orders = orders.clone();
    }

    //copy constructor
    public PowerRateLaw(PowerRateLaw source)
    {
        super(source);
        this.orders = source.orders.clone();
    }

    //accessor
    public SpecieMap getOrders() {
        return this.orders.clone();
    }

    //mutator
    public boolean setOrders(SpecieMap orders) {
        if(orders==null) return false;
        this.orders = orders.clone();
        return true;
    }

    //calculate rate of reaction, returns rate without accounting if it is a consumption or formation rate
    public double returnRate(double T, SpecieMap concentrations) {

        double rate = super.getK().returnRateConstant(T);

        //iterate through the elements in the map
        for  (Map.Entry<Specie,Double> elem : this.orders.entrySet()) {
            rate *= Math.pow(concentrations.get(elem.getKey()),elem.getValue());
        }
        return rate;

    }

    //clone
    public PowerRateLaw clone()
    {
        return new PowerRateLaw(this);
    }

    //equals
    public boolean equals(Object comparator) {
        return super.equals(comparator) && this.orders.equals(((PowerRateLaw)comparator).orders);
    }
}