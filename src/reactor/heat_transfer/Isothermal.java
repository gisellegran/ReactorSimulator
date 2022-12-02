package reactor.heat_transfer;

import chemistry.ReactionSet;
import reactor.NominalPipeSizes;
import reactor.Stream;

public class Isothermal extends HeatTransferEquation{

    private static HeatTransferCondition condition = HeatTransferCondition.ISOTHERMAL;
    public Isothermal(NominalPipeSizes pipeSize){
        super(0., 0., pipeSize);
    }
    public Isothermal(Isothermal source){
        super(source);
    }

    public Isothermal clone(){
        return new Isothermal(this);
    }

    //rhs of the heat transfer equation
    @Override
    public double calculateDelT(double a, Stream s, ReactionSet rxnSet) {
        return 0.;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
