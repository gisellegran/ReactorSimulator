package reactor;

import chemistry.*;
import numericalmethods.*;


public class PFRDesigner extends TubularReactorDesigner {

    //instance variables
    //Global variables

    public PFRDesigner(PFR reactor){
        super(reactor);
    }

    public PFRDesigner(PFRDesigner source){
        super(source);
    }

    public PFR returnReactorForTargetConversion(Specie targetS, double targetX, Stream input, ReactionSet rxns,
                                                double delX, int maxIt){
        return (PFR)super.returnReactorForTargetConversion(targetS, targetX, input, rxns, delX, maxIt);

    }

    public PFR returnReactorForTargetFlow(Specie targetS, double targetF, Stream input, ReactionSet rxns, double delX, int maxIt){
       return (PFR)super.returnReactorForTargetFlow(targetS, targetF, input, rxns, delX, maxIt);
    }

    //maximize flow rate of target species targetS
    public PFR returnReactorForMaxFlow(Specie targetS, Stream input, ReactionSet rxns, double delX, int maxIt){
        return (PFR)super.returnReactorForMaxFlow(targetS, input, rxns, delX, maxIt);
    }

    public PFR returnReactorForMaxSelectivity(Specie s_desired, Specie s_undesired, Stream input, ReactionSet rxns, double delX, int maxIt){
        return (PFR)super.returnReactorForMaxSelectivity(s_desired, s_undesired, input, rxns, delX, maxIt);
    }

}
