package reactor.heat_transfer;

import chemistry.ReactionSet;
import reactor.NominalPipeSizes;
import reactor.Stream;

public class Isothermal extends HeatTransferEquation{
    public Isothermal(double U, double Ta0, NominalPipeSizes pipeSize){
        super(U, Ta0, pipeSize);
    }
    public Isothermal(Isothermal source){
        super(source);
    }

    public Isothermal clone(){
        return new Isothermal(this);
    }

    //rhs of the heat transfer equation
    @Override
    public double calculateValue(double a, Stream s, ReactionSet rxnSet) {
        return 0.;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
