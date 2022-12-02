package chemistry;

import java.util.Map;

import static chemistry.RateConstant.R;

public class Reaction {
    private RateLaw refReactionRate;
    private StoichiometryMap rxnStoichiometry;
    private RefValue refEnthalpy;

    private Specie refSpecie;

    //constructor
    public Reaction (RateLaw refReactionRate, StoichiometryMap rxnStoichiometry, RefValue refEnthalpy)
    {
        if(refReactionRate==null) System.exit(0);
        if(rxnStoichiometry==null) System.exit(0);
        if(refEnthalpy==null) System.exit(0);
        this.refReactionRate = refReactionRate.clone();
        this.rxnStoichiometry = rxnStoichiometry.clone();


        this.refEnthalpy = refEnthalpy.clone();
    }

    //copy constructor
    public Reaction (Reaction source)
    {
        if(source==null) System.exit(0);
        this.refReactionRate = source.refReactionRate.clone();

        this.rxnStoichiometry = source.rxnStoichiometry.clone();

        this.refEnthalpy = source.refEnthalpy.clone();
    }

    //accessors
    public RateLaw getRefReactionRate()
    {
        return this.refReactionRate.clone();
    }

    public StoichiometryMap getRxnStoichiometry() {
        return this.rxnStoichiometry.clone();
    }

    public RefValue getRefEnthalpy()
    {
        return this.refEnthalpy.clone();
    }

    //mutators
    public boolean setRefReactionRate(RateLaw refReactionRate) {
        if (refReactionRate == null) return false;
        this.refReactionRate = refReactionRate.clone();
        return true;
    }

    public boolean setRxnStoichiometry(StoichiometryMap rxnStoichiometry) {
        if(rxnStoichiometry==null) return false;
        this.rxnStoichiometry = rxnStoichiometry.clone();
        return true;
    }

    public boolean setRefEnthalpy(RefValue refEnthalpy) {
        if (refEnthalpy == null) return false;
        this.refEnthalpy = refEnthalpy.clone();
        return true;
    }

    public int returnNumberOfSpecies() {
        return rxnStoichiometry.size();
    }

    public Specie[] returnAllSpecies(){
        return this.rxnStoichiometry.returnAllSpecies();
    }

    public double returnSpecieStoichCoeff(Specie s) { return this.rxnStoichiometry.returnSpecieStoichCoeff(s);}
    public boolean hasSpecie(Specie s){
        return rxnStoichiometry.hasSpecie(s);
    }

    //to return net reaction rates for each species in a given mixture
    public SpecieMap calcAllReactionRates(double T, MultiComponentMixture mix) {
        if (mix == null) {
            //TODO: error handling
        }

        //absolute value of the reference species stoichiometric coefficient, for normalizing the reaction rate
        double refV = Math.abs(this.rxnStoichiometry.returnSpecieStoichCoeff(this.refReactionRate.getRefSpecie()));

        double normalizedRefRate;
        if (mix.returnPhase() == Phase.IDEALGAS) {
            SpecieMap concentrations= mix.returnAllMolConcentrations();
            SpecieMap partialPs = new SpecieMap();

            concentrations.forEach((specie, v)
                    -> partialPs.put(specie.clone(), v*R*mix.getT()));

            normalizedRefRate = this.refReactionRate.returnRate(T, partialPs)/refV;
        } else {
            normalizedRefRate = this.refReactionRate.returnRate(T, mix.returnAllMolConcentrations())/refV;
        }


        SpecieMap reactionRates = new SpecieMap();

        Specie[] species = mix.getSpecies();

        for (int i = 0; i < species.length; i++) {
            if (this.rxnStoichiometry.hasSpecie(species[i])){
                reactionRates.put(species[i].clone(), normalizedRefRate*this.rxnStoichiometry.returnSpecieStoichCoeff(species[i]));
            } else {
                reactionRates.put(species[i].clone(), 0.);
            }

        }

        return reactionRates;
    }

    public double calcRefReactionRate(double T, MultiComponentMixture mix){
        return this.refReactionRate.returnRate(T, mix.returnAllMolConcentrations());
    }


    //used in returnReactionEnthalpy
    private double returnDeltaC(double T){
        double deltaC = 0.;
        //get the absolute value of the reference specie in order to obtain the normalized & generalized stoichiometric coefficients
        double absRefStoich = Math.abs(this.rxnStoichiometry.get(this.refReactionRate.getRefSpecie())); //stoichiometric coefficient of the refference specie of the reaction

        //iterate through the map to calculate the
        for (Map.Entry<Specie, Double> entry : this.rxnStoichiometry.entrySet()) {
            double Ci = entry.getKey().returnHeatCapacity(T); //species i heat capacity at T
            double stoichCoeff = entry.getValue(); //stoichiometric coefficient of species i
            deltaC += (stoichCoeff/absRefStoich)*Ci; // add Cp_i * v_i/|v_ref|
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
        if(this.rxnStoichiometry.equals(((Reaction) comparator).rxnStoichiometry) == false) isEquals = false;
        if(this.refEnthalpy.equals(((Reaction) comparator).refEnthalpy) == false) isEquals = false;
        return isEquals;
    }




}
