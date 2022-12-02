package chemistry;

import java.util.Map;

//TODO: maybe make elementary rate law class
public class PowerRateLaw extends RateLaw {
    private double[] orders;

    //constructor
    public PowerRateLaw(RateConstant k, Specie refSpecie, double[] orders)
    {
        super(k, refSpecie);
        //TODO: error handling
        if(orders == null) throw new IllegalArgumentException("orders array is null");
        this.orders = new double[orders.length];
        for (int i = 0; i < orders.length; i++) {
            this.orders[i] = orders[i];
        }
    }

    //copy constructor
    public PowerRateLaw(PowerRateLaw source)
    {
        super(source);
        this.orders = new double[source.orders.length];
        for (int i = 0; i < source.orders.length; i++) {
            this.orders[i] = source.orders[i];
        }
    }

    //accessor
    public double[] getOrders() {
        double[] temp = new double[this.orders.length];
        for (int i = 0; i < this.orders.length; i++) {
            temp[i] = this.orders[i];
        }
        return temp;
    }

    //mutator
    public boolean setOrders(double[] orders) {
        if(orders==null) return false;
        if(orders.length != this.orders.length) return false;
        for (int i = 0; i < orders.length; i++) {
            this.orders[i] = orders[i];
        }
        return true;
    }

    //calculate rate of reaction, returns rate without accounting if it is a consumption or formation rate
    public double returnRate(double T, double[] concentrations) {
        if (this.orders.length != concentrations.length) throw new IllegalArgumentException("array legnth mismatch between orders and concentrations");

        double rate = super.getK().returnRateConstant(T);

        for (int i = 0; i < this.orders.length; i++) {
            rate *= Math.pow(concentrations[i],this.orders[i]);
        }

        return rate;

    }

    //clone
    public PowerRateLaw clone()
    {
        return new PowerRateLaw(this);
    }

    //equals
    public boolean equals(Object obj) {
        if (!super.equals(obj)) return false;
        PowerRateLaw objPRL = (PowerRateLaw)obj;

        for (int i = 0; i < this.orders.length; i++) {
            if (this.orders[i] != objPRL.orders[i]) return false;
        }

        return true;
    }
}