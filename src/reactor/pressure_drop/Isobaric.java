package reactor.pressure_drop;

import chemistry.MultiComponentMixture;
import reactor.Stream;

public class Isobaric extends PressureDropEquation{

    public static final PressureDropCondition condition = PressureDropCondition.ISOBARIC;
    public Isobaric() {}

    //copy constructor
    public Isobaric(Isobaric source) {
        //TODO: error handling
        if(source==null) System.exit(0);
    }

    public double calculateValue(MultiComponentMixture s) {
        return 0.0;
    };

    public PressureDropCondition getPressureDropCondition(){
        return this.condition;
    };

    //clone
    public Isobaric clone(){
        return new Isobaric(this);
    };

    //equals
    public boolean equals(Object comparator) {
        if(comparator == null) return false;
        else if(this.getClass() != comparator.getClass()) return false;

        return true;
    }
}
