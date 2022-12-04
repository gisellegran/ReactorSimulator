package reactor.heat_transfer;

import reactor.NominalPipeSizes;

public class CoCurrent extends HeatExchanger {

    private static final HeatTransferCondition condition = HeatTransferCondition.COCURRENT;

    public CoCurrent(double U, double Ta0, double m, double Cp){
        super(U, Ta0, m, Cp);
    }
    public CoCurrent(CoCurrent source){
        super(source);
    }

    public CoCurrent clone(){
        return new CoCurrent(this);
    }

    public HeatTransferCondition getHeatTransferCondition(){
        return this.condition;
    };
    public double calculateDelTa(double a, double Ta, double T){
        return (this.getU()*a*(T-Ta))/(this.getM()*this.getCp());
    }



}
