package reactor.heat_transfer;

public class FreeConvective extends HeatTransferEquation{

    private static final HeatTransferCondition condition = HeatTransferCondition.FREE_CONVECTIVE;

    public FreeConvective(double U, double Ta0){
        super(U, Ta0);
    }
    public FreeConvective(FreeConvective source){
        super(source);
    }

    public HeatTransferCondition getHeatTransferCondition(){
        return FreeConvective.condition;
    };
    public FreeConvective clone(){
        return new FreeConvective(this);
    }
}
