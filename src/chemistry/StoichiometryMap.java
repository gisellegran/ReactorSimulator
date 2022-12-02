package chemistry;

import java.util.Map;

//FIXME: to extend SpecieMap
public class StoichiometryMap extends SpecieMap{
    private Map<Specie,Double> rxn;

    //main constructor
    public StoichiometryMap() { super();}

    public StoichiometryMap( int initialCapacity ) {
        super(initialCapacity);
    }

    //copy constructor

    //arrays to map constructor
    public StoichiometryMap(Specie[] species, double[] stoichiometry ){ super(species, stoichiometry); }

    //class methods
    public int returnNumberOfSpecies() {
        return this.size();
    }

    public Specie[] returnAllSpecies() { return super.returnAllSpecies();}


    public double returnSpecieStoichCoeff(Specie specie) {
        return this.get(specie);
    }


    //clone
    public StoichiometryMap clone(){
        StoichiometryMap temp = new StoichiometryMap(this.size());
        this.forEach((specie, v)
                -> temp.put(specie.clone(), v));
        return temp;
    }

    //equals



}
