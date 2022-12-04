package reactor;

import chemistry.*;
import numericalmethods.GoldenSectionSearch;
import numericalmethods.NonLinearEquation;
import numericalmethods.RootFinder;
import reactor.heat_transfer.HeatTransferEquation;
import reactor.pressure_drop.PressureDropEquation;


public class PFRDesigner implements NonLinearEquation {

    public static final double vMax = 1000; //max size of a reactor is 500m3

    //Global variables
    private Specie g_desiredSpecie;
    private Specie g_undesiredSpecie;
    private double g_targetF;
    private PFR g_reactor;
    private ReactionSet g_rxns;
    private Stream g_input;

    private double g_delX;

    private int g_maxIt;
    private String g_toMaximize;


    public PFRDesigner(){
        resetGlobalVariables();
    }

    private void setGlobalVariables(Specie desiredSpecie,Specie undesiredSpecie, double targetF, String toMaximize,
                                    ReactionSet rxns, Stream input, PressureDropEquation pDrop, HeatTransferEquation heatEq, NominalPipeSizes pipeSize, double delX, int maxIt){
        this.g_desiredSpecie = desiredSpecie;
        this.g_undesiredSpecie = undesiredSpecie;
        this.g_targetF = targetF;
        this.g_toMaximize = toMaximize;
        this.g_rxns = rxns;
        this.g_input = input;
        this.g_reactor = new PFR(0., pDrop, heatEq, pipeSize);
        this.g_delX = delX;
        this.g_maxIt = maxIt;
    }

    private void resetGlobalVariables(){
        this.g_desiredSpecie = null;
        this.g_undesiredSpecie = null;
        this.g_targetF = -1.;
        this.g_toMaximize = null;
        this.g_rxns = null;
        this.g_input = null;
        this.g_reactor = null;
        this.g_delX = -1;
        this.g_maxIt = -1;

    }


    public PFR returnReactorForTargetConversion(Specie targetS, double targetX, Stream input, ReactionSet rxns,
                                                PressureDropEquation pDrop, HeatTransferEquation heatX, NominalPipeSizes pipeSize, double delX, int maxIt){
        //convert conversion to target flow rate
        double targetF =  input.returnSpecieFlowRate(targetS)*(1-targetX);
        return returnReactorForTargetFlow( targetS, targetF, input, rxns, pDrop, heatX, pipeSize, delX, maxIt);

    }

    public PFR returnReactorForTargetFlow(Specie s, double targetF, Stream input, ReactionSet rxns,
                                          PressureDropEquation pDrop, HeatTransferEquation heatX, NominalPipeSizes pipeSize, double delX, int maxIt){
        setGlobalVariables(s, null, targetF, null, rxns, input, pDrop, heatX, pipeSize, delX, maxIt);
        double v = RootFinder.findRoot(0., this.vMax, 0.0005, 500000, this);
        PFR result =g_reactor.clone();
        result.setSize(v);
        resetGlobalVariables();

        return result;
    }

    public PFR returnReactorForMaxFlow(Specie s, Stream input, ReactionSet rxns,
                                       PressureDropEquation pDrop, HeatTransferEquation heatX, NominalPipeSizes pipeSize, double delX, int maxIt){
        setGlobalVariables(s, null, -1., "flow", rxns, input, pDrop, heatX, pipeSize, delX, maxIt);
        double v = GoldenSectionSearch.search(0., this.vMax, 0.0005,  this);
        PFR result =g_reactor.clone();
        result.setSize(v);
        resetGlobalVariables();

        return result;
    }

    public PFR returnReactorForMaxSelectivity(Specie s_desired, Specie s_undesired, Stream input, ReactionSet rxns,
                                       PressureDropEquation pDrop, HeatTransferEquation heatX, NominalPipeSizes pipeSize, double delX, int maxIt){
        setGlobalVariables(s_desired, null, -1., "selectivity", rxns, input, pDrop, heatX, pipeSize, delX, maxIt);
        double v = GoldenSectionSearch.search(0., this.vMax, 0.0005,  this);
        PFR result =g_reactor.clone();
        result.setSize(v);
        resetGlobalVariables();

        return result;
    }


    public double returnValue(double x){
        g_reactor.setSize(x);
        Stream output = g_reactor.returnReactorOutput(g_input, g_rxns, g_delX, g_maxIt);
        return output.returnSpecieFlowRate(g_desiredSpecie) - this.g_targetF;
    }

    public double returnEquationResult(double x){
        double result;
        g_reactor.setSize(x);
        Stream output = g_reactor.returnReactorOutput(g_input, g_rxns, g_delX, g_maxIt);
        //default result is flow rate
        result = -output.returnSpecieFlowRate(g_desiredSpecie); //negative because search minimizes
        if (this.g_toMaximize.equalsIgnoreCase("selectivity")) result /= output.returnSpecieFlowRate(g_undesiredSpecie);
        return result;
    }

}
