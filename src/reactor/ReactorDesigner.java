package reactor;

import chemistry.MultiComponentMixture;
import chemistry.ReactionSet;
import chemistry.Specie;
import numericalmethods.SetOfODEs;

public abstract class ReactorDesigner implements SetOfODEs {

    public double returnVForOutputFlow(Specie s, double F,
                                         ReactionSet rxns, MultiComponentMixture input, Reactor reactor){
        return 0.;
    }


}
