package reactor.heat_transfer;

import chemistry.ReactionSet;
import reactor.Stream;

public class Isothermal extends HeatTransferEquation{

    private static HeatTransferCondition condition = HeatTransferCondition.ISOTHERMAL;
    //default pipe size if one inch
    public Isothermal(){
        super(0., 0.);
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
