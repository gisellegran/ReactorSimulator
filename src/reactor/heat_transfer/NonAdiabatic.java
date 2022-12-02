package reactor.heat_transfer;

import reactor.NominalPipeSizes;

public abstract class NonAdiabatic extends HeatTransferEquation{

    //instance variables
    private double m;
    private double Cp;

    public double getM(){
        return this.m;
    }

    public double getCp(){
        return this.Cp;
    }

    //heat exchange fluid flow rate m
    //heat exchange heat capacity Cp
    public NonAdiabatic(double U, double Ta0, NominalPipeSizes pipeSize, double m, double Cp){
        super(U, Ta0, pipeSize);
        this.m = m;
        this.Cp = Cp;
    }
    public NonAdiabatic(NonAdiabatic source){
        super(source);
        this.m = source.m;
        this.Cp = source.Cp;
    }

    public abstract NonAdiabatic clone();

    //calculate dTa/dTV
    public abstract double calculateDelTa(double a, double Ta, double T);

}
