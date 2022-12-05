package reactor.heat_transfer;

public class CounterCurrent extends HeatExchanger {

    private static final HeatTransferCondition condition = HeatTransferCondition.COUNTERCURRENT;

    public CounterCurrent(double U, double Ta0, Utility utility){
        super(U, Ta0, utility);
    }
    public CounterCurrent(CounterCurrent source){
        super(source);
    }

    public HeatTransferCondition getHeatTransferCondition(){
        return CounterCurrent.condition;
    };

    public CounterCurrent clone(){
        return new CounterCurrent(this);
    }

    public double calculateDelTa(double a, double Ta, double T){
        return (this.getU()*a*(Ta-T))/(this.getUtility().getM()*this.getUtility().returnHeatCapacity(Ta));
    }



}
