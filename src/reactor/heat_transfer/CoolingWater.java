package reactor.heat_transfer;


public class CoolingWater extends Utility{
    private static final double R = 8.3145;
    private static final double MW = 18.0152/1000; //molar weight of water kg/mol

    public CoolingWater(double m) {super(m);}
    public CoolingWater(CoolingWater source) {super(source);}
    public CoolingWater clone() {return new CoolingWater(this);}
    public double returnHeatCapacity(double T){
        return (8.712 + (1.25*Math.pow(10,-3)*T) - 0.18*Math.pow(10,-6)*Math.pow(T,2))*CoolingWater.R/CoolingWater.MW;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
