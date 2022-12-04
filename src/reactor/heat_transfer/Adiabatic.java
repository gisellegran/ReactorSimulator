package reactor.heat_transfer;

import chemistry.ReactionSet;
import reactor.Stream;

public class Adiabatic extends HeatTransferEquation{

    private static final HeatTransferCondition condition = HeatTransferCondition.ADIABATIC;
    //default pipe size if one inch
    public Adiabatic(){
        super(0., 0.);
    }
    public Adiabatic(Adiabatic source){
        super(source);
    }

    public Adiabatic clone(){
        return new Adiabatic(this);
    }

    public HeatTransferCondition getHeatTransferCondition(){
        return this.condition;
    };

    //rhs of the heat transfer equation
    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
