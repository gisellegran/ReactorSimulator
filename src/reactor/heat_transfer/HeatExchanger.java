package reactor.heat_transfer;

public abstract class HeatExchanger extends HeatTransferEquation{

    //instance variables
    private double m;
    private double Cp;

    public boolean setM(double m){
        if (m < 0) throw new IllegalArgumentException("flow rate cannot be negative");
        this.m = m;
        return true;
    }

    public boolean setCp(double Cp){
        this.Cp = Cp;
        return true;
    }
    public double getM(){
        return this.m;
    }

    public double getCp(){
        return this.Cp;
    }

    //heat exchange fluid flow rate m
    //heat exchange heat capacity Cp
    public HeatExchanger(double U, double Ta0, double m, double Cp){
        super(U, Ta0);
        if (m < 0) throw new IllegalArgumentException("flow rate cannot be negative");
        this.m = m;
        this.Cp = Cp;
    }
    public HeatExchanger(HeatExchanger source){
        super(source);
        this.m = source.m;
        this.Cp = source.Cp;
    }

    public abstract HeatExchanger clone();

    //calculate dTa/dTV
    public abstract double calculateDelTa(double a, double Ta, double T);

}
