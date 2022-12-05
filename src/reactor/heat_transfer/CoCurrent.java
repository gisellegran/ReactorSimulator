package reactor.heat_transfer;

public class CoCurrent extends HeatExchanger {

    private static final HeatTransferCondition condition = HeatTransferCondition.COCURRENT;

    public CoCurrent(double U, double Ta0, Utility utility){
        super(U, Ta0, utility);
    }
    public CoCurrent(CoCurrent source){
        super(source);
    }

    public CoCurrent clone(){
        return new CoCurrent(this);
    }

    public HeatTransferCondition getHeatTransferCondition(){
        return CoCurrent.condition;
    };
    public double calculateDelTa(double a, double Ta, double T){
        return (this.getU()*a*(T-Ta))/(this.getUtility().getM()*this.getUtility().returnHeatCapacity(Ta));
    }



}
