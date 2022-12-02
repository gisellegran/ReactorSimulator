package chemistry;

import java.util.Map;

//FIXME make into abstract class
public abstract class MultiComponentMixture {

    private Specie[] species;
    private double[] molComposition;
    private double viscosity;
    private double T;
    private double P;


    //main constructor
    public MultiComponentMixture(Specie[] species, double[] molComposition,
                                 double viscosity, double T, double P) {
        //TODO check null handling
        //check null array
        if( species == null) {throw new IllegalArgumentException("species array is null");}
        if( molComposition == null) {throw new IllegalArgumentException("mol composition array is null");}

        if (species.length != molComposition.length){throw new IllegalArgumentException("species and mol composition array are not the same length");}

        for (int i = 0; i < species.length; i++) {
            if (species[i] == null) { throw new IllegalArgumentException("specie in array is null");}
        }

        //check for negative compositions
        for (int i = 0; i < species.length; i++) {
            if (molComposition[i] <0 ) { throw new IllegalArgumentException("negative composition was entered");}
        }

        this.species = new Specie[species.length];
        this.molComposition = new double[species.length];

        //copy values
        for (int i = 0; i < species.length; i++) {
            this.species[i] = species[i];
            this.molComposition[i] = molComposition[i];
        }

        this.viscosity = viscosity;
        this.T = T;
        this.P = P;

    }

    //copy constructor
    public MultiComponentMixture(MultiComponentMixture source) {

        //check null source
        if ( source == null ) {throw new IllegalArgumentException("source is null");}

        this.species = new Specie[species.length];
        this.molComposition = new double[species.length];

        //copy values
        for (int i = 0; i < species.length; i++) {
            this.species[i] = species[i];
            this.molComposition[i] = molComposition[i];
        }

        //copy primitive data type values
        this.viscosity = source.viscosity;
        this.T = source.T;
        this.P = source.P;


    }

    //mutators

    public boolean setMolComposition(double[] molComposition) {

        //TODO: are we just supposed to do true or false here
        if (molComposition == null) { throw new IllegalArgumentException("mol composition array is null");}
        if (molComposition.length != species.length) {throw new IllegalArgumentException("invalid number of compositions");}

        //check for negative compositions
        for (int i = 0; i < species.length; i++) {
            if (molComposition[i] <0 ) { throw new IllegalArgumentException("negative composition was entered");}
        }


        for (int i = 0; i < species.length; i++) {
            this.molComposition[i] = molComposition[i];
        }

        return true;
    }

    public boolean setSpecies(Specie[] species) {
        //FIXME: do we need to put return false orexceptions
        if (species == null) { throw new IllegalArgumentException("species array is null");}
        if (this.species.length != species.length) {throw new IllegalArgumentException("invalid number of species");}

        //check for negative compositions
        for (int i = 0; i < species.length; i++) {
            if (species[i] == null ) { throw new IllegalArgumentException("null element in array");}
        }

        //copy values
        for (int i = 0; i < species.length; i++) {
            this.species[i] = species[i];
        }

        return true;
    }

    public boolean setViscocity(double viscocity) {
        this.viscosity = viscocity;
        return true;
    }

    public boolean setT(double T) {
        this.T = T;
        return true;
    }

    public boolean setP(double p) {
        if (P < 0) return false;
        this.P = p;
        return true;
    }

    //accessors

    public double[] getComposition() {
        //FIXME: adapt for the composition map
        double[] temp = new double[molComposition.length];
        for (int i = 0; i < species.length; i++) {
            temp[i] = this.molComposition[i];
        }
        return temp;
    }


    public double getT() {
        return T;
    }

    public double getP() {
        return P;
    }

    public double getViscosity() {
        return viscosity;
    }

    //class methods
    public boolean isSinglePhase() {
        Specie[] species = this.getSpecies();

        for(int i=1; i<species.length; i++)
            if(species[0].getPhase() != species[i].getPhase()) return false;

        return true;
    }

    public Phase returnPhase(){
        Phase phase;

        if (this.isSinglePhase()) {
            phase = this.getSpecies()[0].getPhase();
        } else {
            phase = Phase.L_G;
        }

        return phase;
    }

    public int returnNumberOfSpecies(){
        return species.length;
    }

    public Specie[] getSpecies(){

        Specie[] temp = new Specie[species.length];
        for (int i = 0; i < species.length; i++) {
            temp[i] = this.species[i];
        }
        return temp;
    }

    public double returnSpecieMolFraction(Specie specie) {
        if (specie == null) {throw new IllegalArgumentException("specie is null");}
        for (int i = 0; i < species.length; i++) {
            if (species[i].equals(specie)){
                return this.molComposition[i];
            }
        }


        //TODO: if value isnt found it return -1, should it throw error instead
        return -1.;
    }

    public double returnMixtureMolarMass() {
        double molarMass = 0.;
        for (int i = 0; i < this.species.length; i++) {
            molarMass += this.species[i].getMolarMass()*molComposition[i];
        }

        return molarMass;
    }

    public boolean hasSpecie(Specie s){
        if (s == null) {throw new IllegalArgumentException("specie is null");}
        for (int i = 0; i < this.species.length; i++) {
            if (species[i].equals(s)){
                return true;
            }
        }
        return false;
    }

    //adds the specie with a composition of 0
    //TODO: maybe take this out if not needed
    public boolean addSpecie(Specie s) {
        if (s == null) {
            //TODO: throw error
        }

        return true;
    }

    //adds all species which arent already in the mixture with a mol fraction of 0
    //TODO: maybe take this out if not needed
    public boolean addAllSpecies(Specie[] s){
        if (s == null) {
            //TODO: throw error or
            return false;
        }

        for (int i = 0; i < s.length; i++) {
            if (s[i] == null) {
                //TODO: throw error or

                return false;
            }
            if (!this.hasSpecie(s[i])){
                this.addSpecie(s[i]);
            }
        }

        return true;

    }

    public double returnMixtureHeatCapacity(double T) {
        if (T < 0 ) {throw new IllegalArgumentException("negative absolute temperature. Temperature is in K");}
        double heatCapacity = 0.;

        //TODO: check if this is the right formula
        for (int i = 0; i < this.species.length; i++) {
            heatCapacity += this.species[i].returnHeatCapacity(T)*this.molComposition[i];
        }

       return heatCapacity;
    }

    public abstract double returnTotalConcentration();

    public abstract double returnSpecieMolConcentration(Specie s);

    public abstract SpecieMap returnAllMolConcentrations();

    //clone
    public abstract MultiComponentMixture clone();


    //toString
    //TODO: probably remove
    /*public String toString(){
        return composition.toString()+"\n Viscocity: "+this.viscosity+" | T: "+this.T+" | P: "+this.P;
    }*/
    //equals
    public boolean equals(Object obj){

        //check that obj isnt null
        if ( obj == null ) { return false;}

        //check that classes are equal
        if (this.getClass() != obj.getClass()){ return false;}

        //convert obj to MultiComponentMixture
        MultiComponentMixture objMix = (MultiComponentMixture)obj;

        //check primitive data values
        if (this.T != objMix.T) return false;
        if (this.P != objMix.P) return false;
        if (this.viscosity != objMix.viscosity) return false;

        if (this.species.length != objMix.species.length) return false;

        //check species and mol composition
        for (int i = 0; i < this.species.length; i++) {
            if(!this.species[i].equals(objMix.species[i])) return false;
            if(this.molComposition[i] != (objMix.molComposition[i])) return false;
        }

        return true;

    }


}
