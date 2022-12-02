package reactor.heat_transfer;

import chemistry.Specie;
import reactor.NominalPipeSizes;
import reactor.Stream;

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

    //TODO: remove
    //calculate deltaH for heat transfer equation
    //deltaH = sum(Fi*(Hi-Hi_0)
    private double returnDeltaH(Stream s0, double T){
        Specie[] species = s0.getSpecies();
        double[] flowRates = s0.getAllFlowRates();
        double T0 = s0.getT();
        double deltaH = 0;
        for (int i = 0; i < flowRates.length; i++) {
            deltaH += flowRates[i]*species[i].returnIntegralHeatCapacity(T0, T);
        }

        return deltaH;
    }


    private double returnTotalFCp(Stream s, double T){
        Specie[] species = s.getSpecies();
        double[] flowRates = s.getAllFlowRates();
        double FCp = 0;
        for (int i = 0; i < flowRates.length; i++) {
            FCp += flowRates[i]*species[i].returnHeatCapacity(T);
        }

        return FCp;
    }

    private double returnHeatRemoved(double U, double a, double T){
        return U*a*(T-this.Ta);
    }

    //heat exchange area per unit volume
    //rdelH = heat generated
    //ua(T-Ta) = heat removed
    public double calculateValue(double a, Stream s, double T, double rdelH)

    {
        return (rdelH-this.returnHeatRemoved(this.U,a, T))/this.returnTotalFCp(s,T);
    }

    //clone
    public HeatTransferEquation clone(){
        return new HeatTransferEquation(this);
    }
}
