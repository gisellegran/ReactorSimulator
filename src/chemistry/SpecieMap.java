package chemistry;

import java.util.*;

public class SpecieMap extends HashMap<Specie,Double> {

    public SpecieMap() {
        super();
    }

    public SpecieMap( int initialCapacity ) {
        super(initialCapacity);
    }

    public SpecieMap( int initialCapacity, float loadFactor ) {
        super(initialCapacity, loadFactor);
    }

    public SpecieMap ( Map t) {
        super(t);
    }

    //arrays to SpecieMap constructor
    public SpecieMap (Specie[] species, double[] values ){
        //TODO check error handling
        //check null array
        if( species == null || values == null ) System.exit(0);

        //check compatibility of arrays
        if(values.length != species.length) System.exit(0);

        //check for null elements
        for(int i=0;i<species.length;i++) {
            //TODO: error handling
            if (species[i] == null ) System.exit(0);
        }
        //deep copy arrays into the map
        for(int i=0;i<species.length;i++)
            this.put(species[i].clone(), values[i]);

    }

    //class methods
    public Collection<Double> values() {
        return super.values();
    }

    public Specie[] returnAllSpecies(){
        Specie[] temp = this.keySet().toArray(new Specie[this.size()]);

        //create deep copy by replacing species in the array with a copy
        for (int i = 0; i < temp.length; i++) {
            temp[i] = temp[i].clone();
        }
        return temp;
    }

    public boolean hasSpecie(Specie s){
        if (s == null) {
            //TODO: error handling
        }

        return this.containsKey(s.clone());
    }

    //this clone method deep copies the map
    //(HashMap clone method only shallow copies)
    public SpecieMap clone(){
        SpecieMap temp = new SpecieMap(this.size());
        this.forEach((specie, v)
                -> temp.put(specie.clone(), v));
       return temp;
    }


}
