package reactor.heat_transfer;

import chemistry.RefValue;
import chemistry.Specie;
import chemistry.SpecieMap;
import reactor.NominalPipeSizes;
import reactor.Stream;

import java.util.Map;

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
        SpecieMap flowRates = s0.getAllFlowRates();
        double T0 = s0.getT();
        double deltaH = 0;
        for (Map.Entry<Specie,Double> s: flowRates.entrySet()){
            deltaH += s.getValue()*s.getKey().returnIntegralHeatCapacity(T0, T);
        }

        return deltaH;
    }


    private double returnTotalFCp(Stream s, double T){
        SpecieMap flowRates = s.getAllFlowRates();
        double FCp = 0;
        for (Map.Entry<Specie,Double> specie: flowRates.entrySet()){
            FCp += specie.getValue()*specie.getKey().returnHeatCapacity(T);
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
