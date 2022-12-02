package reactor.pressure_drop;

import chemistry.MultiComponentMixture;


public abstract class PressureDropEquation{


    //instance variables

    //constructor
    public PressureDropEquation() {}

    //copy constructor
    public PressureDropEquation(PressureDropEquation source)
    {
        //TODO: error handling
        if(source==null) System.exit(0);
    }


    //TODO: maybe fix this input
    public abstract double calculateValue(MultiComponentMixture mix);


    //clone
    public abstract PressureDropEquation clone();

    //equals
    public boolean equals(Object comparator) {
        if(comparator == null) return false;
        else if(this.getClass() != comparator.getClass()) return false;

        return true;
    }
}
