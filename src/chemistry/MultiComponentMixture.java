package chemistry;

import java.util.Map;

//FIXME make into abstract class
public abstract class MultiComponentMixture {
    private CompositionMap composition;
    private double viscosity;
    private double T;
    private double P;


    //main constructor
    public MultiComponentMixture(CompositionMap composition,
                                 double viscosity, double T, double P) {
        //TODO check null handling
        //check null array
        if( composition == null ) System.exit(0);

        //copy values
        this.viscosity = viscosity;
        this.T = T;
        this.P = P;

        this.composition = composition.clone();
    }

    //copy constructor
    public MultiComponentMixture(MultiComponentMixture source) {

        //TODO check null handling
        //check null source
        if ( source == null ) {System.exit(0);}

        //copy primitive data type values
        this.viscosity = source.viscosity;
        this.T = source.T;
        this.P = source.P;

        this.composition = source.composition.clone();


    }

    //mutators
    public boolean setComposition(CompositionMap composition) {
        if (composition == null){
            //TODO: throw error or false
        }

        this.composition = composition.clone();
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

    public SpecieMap getComposition() {
        //FIXME: adapt for the composition map
        return this.composition.clone();
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
        Specie[] species = this.returnAllSpecies();

        for(int i=1; i<species.length; i++)
            if(species[0].getPhase() != species[i].getPhase()) return false;

        return true;
    }

    public Phase returnPhase(){
        Phase phase;

        if (this.isSinglePhase()) {
            phase = this.returnAllSpecies()[0].getPhase();
        } else {
            phase = Phase.L_G;
        }

        return phase;
    }

    public int returnNumberOfSpecies(){
        return composition.size();
    }

    public Specie[] returnAllSpecies(){
        return this.composition.returnAllSpecies();
    }

    public double returnSpecieMolFraction(Specie specie) {
        //TODO: if value isnt found it return -1, should it throw error instead
        return this.composition.getOrDefault(specie, -1.0);
    }

    public double returnMixtureMolarMass() {
        double molarMass = 0.;

        //iterate through the elements in the map
        for  (Map.Entry<Specie,Double> elem : this.composition.entrySet()) {
            molarMass += elem.getKey().getMolarMass()*elem.getValue();
        }

        return molarMass;
    }

    public boolean hasSpecie(Specie s){
        return this.composition.hasSpecie(s);
    }

    //adds the specie with a composition of 0
    public boolean addSpecie(Specie s) {
        if (s == null) {
            //TODO: throw error
        }
        this.composition.addSpecie(s);
        return true;
    }

    //adds all species which arent already in the mixture with a mol fraction of 0
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
        double heatCapacity = 0.;
        //TODO: check if this is the right formula
        for  (Map.Entry<Specie,Double> elem : this.composition.entrySet()) {
            heatCapacity += elem.getKey().returnHeatCapacity(T)*elem.getValue();
        }
       return heatCapacity;
    }

    public abstract double returnTotalConcentration();

    public abstract double returnSpecieMolConcentration(Specie s);

    public abstract SpecieMap returnAllMolConcentrations();
    //clone
    public abstract MultiComponentMixture clone();


    //toString
    public String toString(){
        return composition.toString()+"\n Viscocity: "+this.viscosity+" | T: "+this.T+" | P: "+this.P;
    }
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

        if (!this.composition.equals(objMix.composition));

        return true;

    }


}
