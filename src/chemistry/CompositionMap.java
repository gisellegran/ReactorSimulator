package chemistry;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CompositionMap extends SpecieMap {
    public CompositionMap() {
        super();
    }

    public CompositionMap( int initialCapacity ) {
        super(initialCapacity);
    }

    public CompositionMap( int initialCapacity, float loadFactor ) {
        super(initialCapacity, loadFactor);
    }

    //TODO: how to make copy constructor for hash map
    public CompositionMap( SpecieMap t) {

    }

    //turn molarFlowMap into the composition map
    public CompositionMap(MolarFlowMap map){
        //TODO: error handling
        if (map == null) System.exit(0);

        double totalFlow = map.returnTotalMolarFlow();

        for (Map.Entry<Specie, Double> entry : map.entrySet()) {
            Specie specie = entry.getKey().clone();
            Double concentration = ((double) entry.getValue()) / totalFlow;
            this.put(specie, concentration);
        }
    }

    //arrays to map constructor
    public CompositionMap(Specie[] species, double[] molFractions ){ super(species, molFractions); }


    //class methods
    public Collection<Double> values() {
        return super.values();
    }

    //TODO: check this over
    private boolean fractionsAreNormalized(double[] molFractions){
        double sum = 0.;
        for (int i = 0; i < molFractions.length; i++) {
            sum += molFractions[i];
        }

        return sum == 1.0;
    }

    //TODO: maybe just move to multicoponent mixture
    public boolean addSpecie(Specie s) {
        if (s == null) {
            //TODO: throw error
        }
        this.put(s.clone(), 0.);
        return true;
    }

    //clone
    public CompositionMap clone() {
        CompositionMap temp = new CompositionMap(this.size());
        this.forEach((specie, v)
                -> temp.put(specie.clone(), v));
        return temp;
    }

    //TODO add these methods
    //setSpecieMolFraction

    //getSpecieArray
    /*
        public boolean setMolFractions(double[] molFractions) {

        //check for null array
        if (molFractions == null) {return false;}

        //check array lengths match
        if (molFractions.length != this.molFractions.length) {return false;}

        for (int i = 0; i < molFractions.length; i++) {
            this.molFractions[i] = molFractions[i];
        }

        return true;
    }

    public boolean setSpecies(Specie[] species) {
        //check for null array
        if(species==null) return false;

        //check that number of Species elements passed in array matches number of elements in molFraction instance variable
        if(this.molFractions.length!=species.length) return false;

        //copy values over & simultaneously check for null
        for (int i = 0; i < this.species.length; i++) {
            //check while we loop if species is null
            if (this.species[i] == null) return false;
            this.species[i] = species[i].clone();
        }

        return true;
    }
     */

}
