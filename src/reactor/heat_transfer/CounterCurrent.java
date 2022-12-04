package reactor.heat_transfer;

public class CounterCurrent extends HeatExchanger {

    private static HeatTransferCondition condition = HeatTransferCondition.COUNTERCURRENT;

    public CounterCurrent(double U, double Ta0, double m, double Cp){
        super(U, Ta0, m, Cp);
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
