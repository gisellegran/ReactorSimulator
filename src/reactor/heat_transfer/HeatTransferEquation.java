package reactor.heat_transfer;

import chemistry.MultiComponentMixture;
import chemistry.Reaction;
import chemistry.ReactionSet;
import chemistry.Specie;
import reactor.Stream;

//maybe rename to tubular
public abstract class HeatTransferEquation{

    public static HeatTransferCondition condition;
    private double Ta0; //ambiant temperature

    private double U;//overall heat transfer coefficient

    //main constructor
    public HeatTransferEquation(double U, double Ta){
        //todo limits on values

        this.U = U;//W/m2K
        this.Ta0 = Ta;//k
    }
    //copy constructor
    public HeatTransferEquation(HeatTransferEquation source){
        if (source == null) throw new IllegalArgumentException("source is null");
        this.U = source.U;
        this.Ta0 = source.Ta0;
    }

    public boolean setTa(double Ta) {
        if (Ta <0) throw new IllegalArgumentException("negative absolute temperature not possible. T is in Kelvins ");
        this.Ta0 = Ta;
        return false;
    }

    public double getTa0(){
        return this.Ta0;
    }

    public double getU(){
        return this.U;
    }

    //calculate netDeltaH fpr the conditions of the mixture
    //rDeltaH = sum(rij*DeltaH_ij)
    //mix has temperature at which to calculate the deltaH
    public double returnHeatGenerated(ReactionSet rxnSet, MultiComponentMixture mix){
        Reaction[] rxns = rxnSet.getReactions();
        double deltaH = 0.;
        double T = mix.getT();
        for (int i = 0; i < rxns.length; i++) {
            deltaH += rxns[i].returnReactionEnthalpy(T)*rxns[i].calcNormalizedReactionRate(mix);
        }

        return -deltaH;//positive if heat is generate negative if heat is lost
    }

    private double returnTotalFCp(Stream s){
        if (s == null) throw new IllegalArgumentException("stream is null");

        Specie[] species = s.getSpecies();
        double[] flowRates = s.getAllFlowRates();
        double T = s.getT();
        double FCp = 0;
        for (int i = 0; i < flowRates.length; i++) {
            FCp += flowRates[i]*species[i].returnHeatCapacity(T);
        }

        return FCp;
    }

    private double returnHeatRemoved(double a, double T){
        return this.U*a*(T-this.Ta0);
    }

    //a = heat exchange area per unit volume
    //rdelH = heat generated
    //ua(T-Ta0) = heat removed
    public double calculateDelT(double a, Stream s, ReactionSet rxns) {
        double T = s.getT();
        return (this.returnHeatGenerated(rxns, s)-this.returnHeatRemoved(a, T))/this.returnTotalFCp(s);
    }

    public abstract HeatTransferCondition getHeatTransferCondition();
    //clone
    public abstract HeatTransferEquation clone();

    public boolean equals(Object comparator)
    {
        if(comparator == null) return false;
        else if(this.getClass() != comparator.getClass()) return false;

        boolean isEquals = true;
        if(this.Ta0 != ((HeatTransferEquation) comparator).Ta0) isEquals = false;
        if(this.U!= ((HeatTransferEquation) comparator).U) isEquals = false;
        if(this.getHeatTransferCondition() != ((HeatTransferEquation) comparator).getHeatTransferCondition()) isEquals = false;
        return isEquals;
    }
}

