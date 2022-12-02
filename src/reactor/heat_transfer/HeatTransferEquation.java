package reactor.heat_transfer;

import chemistry.MultiComponentMixture;
import chemistry.Reaction;
import chemistry.ReactionSet;
import chemistry.Specie;
import reactor.NominalPipeSizes;
import reactor.Stream;

//maybe rename to tubular
public class HeatTransferEquation{
    private HeatTransferCondition condition;

    private double Ta; //ambiant temperature

    private double U;//overall heat transfer coefficient

    private double pipeSize;//size of reactor pipe

    //main constructor
    public HeatTransferEquation(double U, double Ta0, NominalPipeSizes pipeSize){
        if (condition == null) {
            //TODO: error handling
        }
        this.condition = condition;
    }
    //copy constructor
    public HeatTransferEquation(HeatTransferEquation source){
        if (source == null) {
            //TODO: error handling
        }
        this.condition = source.condition;
    }

    public double calculateEnthalpy()
    {
        double enthalpy = 0.;
        //TODO: implement
        return enthalpy;
    }

    //calculate netDeltaH fpr the conditions of the mixture
    //rDeltaH = sum(rij*DeltaH_ij)
    //mix has temperature at which to calculate the deltaH
    public double returnHeatGenerated(ReactionSet rxnSet, MultiComponentMixture mix){
        Reaction[] rxns = rxnSet.getReactions();
        double deltaH = 0.;
        double T = mix.getT();
        for (int i = 0; i < rxns.length; i++) {
            deltaH += rxns[i].returnReactionEnthalpy(T)*rxns[i].calcRefReactionRate(mix);
        }

        return deltaH;
    }


    private double returnTotalFCp(Stream s){
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
        return this.U*a*(T-this.Ta);
    }

    //a = heat exchange area per unit volume
    //rdelH = heat generated
    //ua(T-Ta) = heat removed
    public double calculateValue(double a, Stream s, ReactionSet rxns) {
        double T = s.getT();
        return (this.returnHeatGenerated(rxns, s)-this.returnHeatRemoved(a, T))/this.returnTotalFCp(s);
    }

    //clone
    public HeatTransferEquation clone(){
        return new HeatTransferEquation(this);
    }
}
