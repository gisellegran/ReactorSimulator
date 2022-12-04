package reactor.pressure_drop;

import chemistry.MultiComponentMixture;
import reactor.Stream;

public class Isobaric extends PressureDropEquation{

    public Isobaric() {}

    //copy constructor
    public Isobaric(Isobaric source) {
        if(source==null) throw new IllegalArgumentException("source is null");;
    }

    public double calculateValue(MultiComponentMixture s) {
        return 0.0;
    };

    //clone
    public Isobaric clone(){
        return new Isobaric(this);
    }

    //equals
    public boolean equals(Object comparator) {
        if(comparator == null) return false;
        else if(this.getClass() != comparator.getClass()) return false;

        return true;
    }
}
