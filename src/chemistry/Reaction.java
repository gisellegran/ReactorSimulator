package chemistry;

import static chemistry.RateConstant.R;

public class Reaction {
    private RateLaw refReactionRate;
    private Specie[] species;
    private double[] stoichiometry;

    private RefValue refEnthalpy;

    //globabl variables
    int g_refI; // reference species index

    //constructor
    public Reaction (RateLaw refReactionRate, Specie[] species, double[] stoichiometry, RefValue refEnthalpy)
    {
        //TODO; error handling
        if(refReactionRate==null) System.exit(0);
        if(stoichiometry==null) System.exit(0);
        if(refEnthalpy==null) System.exit(0);

        //TODO; throw error if ref species is not in the species list

        if (species.length != stoichiometry.length) {throw new IllegalArgumentException("array length mismatch");}
        for (int i = 0; i < species.length; i++) {
            if (species[i] == null ) {throw new IllegalArgumentException("null element in array");}
        }
        this.species = new Specie[species.length];
        this.stoichiometry = new double[stoichiometry.length];

        //do species and stoichionmetry at the same time since theyre the same legnth
        for (int i = 0; i < stoichiometry.length; i++) {
            this.species[i] = species[i];
            this.stoichiometry[i] = stoichiometry[i];
        }
        this.refReactionRate = refReactionRate.clone();
        this.refEnthalpy = refEnthalpy.clone();
    }

    //copy constructor
    public Reaction (Reaction source)
    {
        if(source==null) System.exit(0);
        this.species = new Specie[source.species.length];
        this.stoichiometry = new double[source.stoichiometry.length];

        //do species and stoichionmetry at the same time since theyre the same legnth
        for (int i = 0; i < source.stoichiometry.length; i++) {
            this.species[i] = source.species[i];
            this.stoichiometry[i] = source.stoichiometry[i];
        }
        this.refReactionRate = source.refReactionRate.clone();

        this.refEnthalpy = source.refEnthalpy.clone();
    }

    //global variable handlers
    private void setGlobalVariables(){

        //set refSpecie index in the specie array
        for (int i = 0; i < this.species.length; i++) {
            if(this.species[i].equals(this.refReactionRate.getRefSpecie())) {this.g_refI = i;}
            break;
        }
    }

    //accessors
    public RateLaw getRefReactionRate()
    {
        return this.refReactionRate.clone();
    }

    public double[] getRxnStoichiometry() {
        double[] temp = new double[this.stoichiometry.length];
        for (int i = 0; i < this.stoichiometry.length; i++) {
            temp[i] = this.stoichiometry[i];
        }
        return temp;
    }

    public RefValue getRefEnthalpy()
    {
        return this.refEnthalpy.clone();
    }

    public boolean setSpecies(){
        // todo: implement
        return true;
    }
    //mutators
    public boolean setRefReactionRate(RateLaw refReactionRate) {
        if (refReactionRate == null) return false;
        this.refReactionRate = refReactionRate.clone();
        return true;
    }

    public boolean setStoichiometry(double[] stoichiometry) {
        //todo: throw error?
        if(stoichiometry==null) return false;

        if (stoichiometry.length != this.stoichiometry.length){return false;}
        for (int i = 0; i < this.stoichiometry.length; i++) {
            this.stoichiometry[i] = stoichiometry[i];
        }

        return true;
    }

    public boolean setRefEnthalpy(RefValue refEnthalpy) {
        if (refEnthalpy == null) return false;
        this.refEnthalpy = refEnthalpy.clone();
        return true;
    }

    public int returnNumberOfSpecies() {
        return species.length;
    }

    public Specie[] getSpecies(){
        Specie[] temp = new Specie[this.species.length];
        for (int i = 0; i < this.species.length; i++) {
            temp[i] = this.species[i];
        }
        return temp;
    }

    public double returnSpecieStoichCoeff(Specie s) {
        //find index of specie
        int index = -1;
        for (int i = 0; i < this.species.length; i++) {
            if (this.species[i].equals(s)){
                index = i;
                break;
            }
        }
        return index;}
    public boolean hasSpecie(Specie s){
        if (s == null) {throw new IllegalArgumentException("specie is null");}
        for (int i = 0; i < this.species.length; i++) {
            if (species[i].equals(s)){
                return true;
            }
        }
        return false;
    }

    //to return net reaction rates for each species in a given mixture
    //TODO check if T gets updated in the mixture
    public double[] calcAllReactionRates(MultiComponentMixture mix) {
        if (mix == null) {
            //TODO: error handling
        }


        //TODO; if mix has species not in reaction

        double[] conc; //values to be passed to reaction rate for calculation
        //concentration for liquid andpartial pressure for gas

        //absolute value of the reference species stoichiometric coefficient, for normalizing the reaction rate
        double refV = Math.abs(this.stoichiometry[g_refI]);

        double normalizedRefRate;
        if (mix.returnPhase() == Phase.IDEALGAS) {
            double[] concentrations = mix.returnAllMolConcentrations();
            conc = new double[this.species.length];

            double mixT = mix.getT();

            for (int i = 0; i < concentrations.length; i++) {
                conc[i] = concentrations[i] * R * mixT;
            }
        } else conc = mix.returnAllMolConcentrations();

        normalizedRefRate = this.refReactionRate.returnRate(mix)/refV;

        double[] reactionRates = new double[this.species.length];

        for (int i = 0; i < this.species.length; i++) {
                reactionRates[i] = normalizedRefRate*this.stoichiometry[i];
        }

        return reactionRates;
    }

    public double calcRefReactionRate(MultiComponentMixture mix){
        return this.refReactionRate.returnRate(mix);
    }


    //used in returnReactionEnthalpy
    private double returnDeltaC(double T){
        double deltaC = 0.;
        //get the absolute value of the reference specie in order to obtain the normalized & generalized stoichiometric coefficients
        double refV = Math.abs(this.stoichiometry[g_refI]);//stoichiometric coefficient of the refference specie of the reaction

        for (int i = 0; i < this.species.length; i++) {
            double Ci = species[i].returnHeatCapacity(T);//species i heat capacity at T
            double stoichCoeff = this.stoichiometry[i]; //stoichiometric coefficient of species i
            deltaC += (stoichCoeff/refV)*Ci;// add Cp_i * v_i/|v_ref|
        }

        return deltaC;
    }

    //return reaction enthalpy at temperature T
     public double returnReactionEnthalpy(double T) {
        if (T <= 0 ) {throw new IllegalArgumentException("T is not positive.");}
         //∆H_rx(T) = ∆H_rx(T_R) + ∆C_p*(T-T_R)
         //this is calculated with respect to the reference specie
        return this.refEnthalpy.getValue() + this.returnDeltaC(T)*(T-this.refEnthalpy.getRefT());
     }

    //clone
    public Reaction clone(){
        return new Reaction(this);
    };

    //equals
    public boolean equals(Object comparator)
    {
        if(comparator == null) return false;
        else if(this.getClass() != comparator.getClass()) return false;

        boolean isEquals = true;
        if(this.refReactionRate.equals(((Reaction) comparator).refReactionRate) == false) isEquals = false;
        if(this.stoichiometry.equals(((Reaction) comparator).stoichiometry) == false) isEquals = false;
        if(this.refEnthalpy.equals(((Reaction) comparator).refEnthalpy) == false) isEquals = false;
        return isEquals;
    }




}
