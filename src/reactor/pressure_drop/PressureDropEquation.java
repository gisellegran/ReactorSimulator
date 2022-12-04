package reactor.pressure_drop;

import chemistry.MultiComponentMixture;


public abstract class PressureDropEquation{


    //instance variables
    public static PressureDropCondition condition;

    //constructor
    public PressureDropEquation() {}

    //copy constructor
    public PressureDropEquation(PressureDropEquation source)
    {
        if(source==null) throw new IllegalArgumentException("source is null");
    }


    //TODO: maybe fix this input
    public abstract double calculateValue(MultiComponentMixture mix);


    public abstract PressureDropCondition getPressureDropCondition();
    //clone
    public abstract PressureDropEquation clone();

    //equals
    public boolean equals(Object comparator) {
        if(comparator == null) return false;
        else return this.getClass() == comparator.getClass();
    }
}
