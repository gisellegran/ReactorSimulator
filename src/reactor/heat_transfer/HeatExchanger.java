package reactor.heat_transfer;

public abstract class HeatExchanger extends HeatTransferEquation{

    //instance variables
    private Utility utility;


    //heat exchange fluid flow rate m
    //heat exchange heat capacity Cp
    public HeatExchanger(double U, double Ta0, Utility utility){
        super(U, Ta0);
        if (utility == null) throw new IllegalArgumentException("null utility");
        this.utility = utility;
    }

    public boolean setUtility(Utility utility){
        if (utility == null) throw new IllegalArgumentException("null utility");
        this.utility = utility;
        return true;
    }

    public Utility getUtility(){
        return this.utility;
    }


    public HeatExchanger(HeatExchanger source){
        super(source);
        this.utility = source.utility.clone();
    }

    public abstract HeatExchanger clone();

    //calculate dTa/dTV
    public abstract double calculateDelTa(double a, double Ta, double T);

}
