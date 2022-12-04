package reactor;

import chemistry.*;
import numericalmethods.*;


public class PBRDesigner extends TubularReactorDesigner {

    //instance variables
    //Global variables

    public PBRDesigner(PBR reactor){
        super(reactor);
    }

    public PBRDesigner(PBRDesigner source){
        super(source);
    }

    public PBR returnReactorForTargetConversion(Specie targetS, double targetX, Stream input, ReactionSet rxns,
                                                double delX, int maxIt){
        return (PBR)super.returnReactorForTargetConversion(targetS, targetX, input, rxns, delX, maxIt);

    }

    public PBR returnReactorForTargetFlow(Specie targetS, double targetF, Stream input, ReactionSet rxns, double delX, int maxIt){
        return (PBR)super.returnReactorForTargetFlow(targetS, targetF, input, rxns, delX, maxIt);
    }

    //maximize flow rate of target species targetS
    public PBR returnReactorForMaxFlow(Specie targetS, Stream input, ReactionSet rxns, double delX, int maxIt){
        return (PBR)super.returnReactorForMaxFlow(targetS, input, rxns, delX, maxIt);
    }

    public PBR returnReactorForMaxSelectivity(Specie s_desired, Specie s_undesired, Stream input, ReactionSet rxns, double delX, int maxIt){
        return (PBR)super.returnReactorForMaxSelectivity(s_desired, s_undesired, input, rxns, delX, maxIt);
    }

}
