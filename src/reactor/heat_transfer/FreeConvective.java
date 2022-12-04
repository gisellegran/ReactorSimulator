package reactor.heat_transfer;

import reactor.NominalPipeSizes;

public class FreeConvective extends HeatTransferEquation{

    private static HeatTransferCondition condition = HeatTransferCondition.FREE_CONVECTIVE;

    public FreeConvective(double U, double Ta0, NominalPipeSizes pipeSize){
        super(U, Ta0);
    }
    public FreeConvective(FreeConvective source){
        super(source);
    }
    public FreeConvective clone(){
        return new FreeConvective(this);
    }
}
