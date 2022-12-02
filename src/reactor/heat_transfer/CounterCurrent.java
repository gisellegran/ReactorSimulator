package reactor.heat_transfer;

import reactor.NominalPipeSizes;

public class CounterCurrent extends NonAdiabatic{

    private static HeatTransferCondition condition = HeatTransferCondition.COUNTERCURRENT;

    public CounterCurrent(double U, double Ta0, NominalPipeSizes pipeSize, double m, double Cp){
        super(U, Ta0, pipeSize, m, Cp);
    }
    public CounterCurrent(CounterCurrent source){
        super(source);
    }

    public CounterCurrent clone(){
        return new CounterCurrent(this);
    }

    public double calculateDelTa(double a, double Ta, double T){
        return (this.getU()*a*(Ta-T))/(this.getM()*this.getCp());
    }



}
