package reactor.heat_transfer;

import reactor.NominalPipeSizes;

public class CoCurrent extends NonAdiabatic{

    private static HeatTransferCondition condition = HeatTransferCondition.COCURRENT;

    public CoCurrent(double U, double Ta0, NominalPipeSizes pipeSize, double m, double Cp){
        super(U, Ta0, pipeSize, m, Cp);
    }
    public CoCurrent(CoCurrent source){
        super(source);
    }

    public CoCurrent clone(){
        return new CoCurrent(this);
    }

    public double calculateDelTa(double a, double Ta, double T){
        return (this.getU()*a*(T-Ta))/(this.getM()*this.getCp());
    }



}
