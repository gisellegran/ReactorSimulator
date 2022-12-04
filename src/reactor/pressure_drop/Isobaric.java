package reactor.pressure_drop;

import chemistry.MultiComponentMixture;

public class Isobaric extends PressureDropEquation{

    public static final PressureDropCondition condition = PressureDropCondition.ISOBARIC;
    public Isobaric() {}

    //copy constructor
    public Isobaric(Isobaric source) {
        if(source==null) throw new IllegalArgumentException("source is null");
    }

    public double calculateValue(MultiComponentMixture s) {
        return 0.0;
    }

    public PressureDropCondition getPressureDropCondition(){
        return this.condition;
    }

    //clone
    public Isobaric clone(){
        return new Isobaric(this);
    }

    //equals
    public boolean equals(Object comparator) {
        return super.equals(comparator);
    }
}
