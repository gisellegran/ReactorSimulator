package reactor.heat_transfer;

public abstract class Utility {

    //instance variables
    private double m; //mass flow rate in kg/s

    public Utility(double m){
        if (m<0) throw new IllegalArgumentException("cant have negative flow");
        this.m = m;
    }


    //accessors and mutators
    public Utility(Utility source){ this.m = source.m;}

    public double getM() {return this.m;}

    public boolean setM(double m) {
        if (m < 0) throw new IllegalArgumentException("cant have negative flow");
        this.m = m;
        return true;
    }

    public abstract double returnHeatCapacity(double T);

    public abstract Utility clone();
    //equals
    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj.getClass() != this.getClass()) return false;
        return ((Utility)obj).m == this.m;
    }
}
