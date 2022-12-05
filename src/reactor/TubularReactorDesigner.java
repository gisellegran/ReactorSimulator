package reactor;

import chemistry.*;
import numericalmethods.*;


public class TubularReactorDesigner implements NonLinearEquation {

    //max size of a reactor is 500m3

    //instance variables
    private TubularReactor reactor;

    //Global variables
    private  double g_Vmax;
    private Specie g_desiredSpecie;
    private Specie g_undesiredSpecie;
    private double g_targetF;
    private ReactionSet g_rxns;
    private Stream g_input;

    private double g_delX;

    private int g_maxIt;
    private String g_toMaximize;


    public TubularReactorDesigner(TubularReactor reactor){
        this.reactor = reactor.clone();
        resetGlobalVariables();
    }

    public TubularReactorDesigner(TubularReactorDesigner source){
        this.reactor = source.reactor.clone();
        resetGlobalVariables();
    }


    private void setGlobalVariables(Specie desiredSpecie, Specie undesiredSpecie, double targetF, String toMaximize,
                                    ReactionSet rxns, Stream input, double delX, int maxIt){
        this.g_desiredSpecie = desiredSpecie;
        this.g_undesiredSpecie = undesiredSpecie;
        this.g_targetF = targetF;
        this.g_toMaximize = toMaximize;
        this.g_rxns = rxns;
        this.g_input = input;
        this.g_delX = delX;
        this.g_maxIt = maxIt;
    }

    private void resetGlobalVariables(){
        this.g_Vmax = 100;
        this.g_desiredSpecie = null;
        this.g_undesiredSpecie = null;
        this.g_targetF = -1.;
        this.g_toMaximize = null;
        this.g_rxns = null;
        this.g_input = null;
        this.g_delX = -1;
        this.g_maxIt = -1;

    }


    public TubularReactor returnReactorForTargetConversion(Specie targetS, double targetX, Stream input, ReactionSet rxns,
                                                           double delX, int maxIt){
        //convert conversion to target flow rate
        double targetF =  input.returnSpecieFlowRate(targetS)*(1-targetX);
        return returnReactorForTargetFlow( targetS, targetF, input, rxns, delX, maxIt);

    }

    public TubularReactor returnReactorForTargetFlow(Specie s, double targetF, Stream input, ReactionSet rxns, double delX, int maxIt){
        setGlobalVariables(s, null, targetF, null, rxns, input, delX, maxIt);
        TubularReactor result = this.reactor.clone();
        double v = result.getSize();
        while(true) {
            try {
                v = RootFinder.findRoot(0., this.g_Vmax, 0.0005, 500000, this);
                break;
            } catch (RootFindingException e) {
                //todo: handle eerror
            }
        }

        result.setSize(v);
        resetGlobalVariables();

        return result;
    }

    public TubularReactor returnReactorForMaxFlow(Specie s, Stream input, ReactionSet rxns, double delX, int maxIt){
        setGlobalVariables(s, null, -1., "flow", rxns, input, delX, maxIt);
        TubularReactor result = this.reactor.clone();
        double v = result.getSize();
        int count = 0;
        int maxTries = 500000;

        while(true) {
            try {
                v = GoldenSectionSearch.search(0., this.g_Vmax, 0.0005,  this);
                break;
            } catch (GoldenSearchException e) {
                this.g_Vmax = e.getX_i();
                if (++count == maxTries) {;}//todo: throw error ;
            }
        }


        result.setSize(v);
        resetGlobalVariables();

        return result;
    }

    public TubularReactor returnReactorForMaxSelectivity(Specie s_desired, Specie s_undesired, Stream input, ReactionSet rxns, double delX, int maxIt){
        setGlobalVariables(s_desired, s_undesired, -1., "selectivity", rxns, input, delX, maxIt);
        TubularReactor result = this.reactor.clone();
        double v = result.getSize();
        while(true) {
            try {
                v = GoldenSectionSearch.search(0., this.g_Vmax, 0.0005, this);
                break;
            }
            catch (GoldenSearchException e){
                //todo: handle error
            }
        }
        result.setSize(v);
        resetGlobalVariables();

        return result;
    }


    public double returnValue(double x) {
        this.reactor.setSize(x);
        Stream output = this.reactor.returnReactorOutput(g_input, g_rxns, g_delX, g_maxIt);
        return output.returnSpecieFlowRate(g_desiredSpecie) - this.g_targetF;
    }

    public double returnEquationResult(double x){
        double result;
        this.reactor.setSize(x);
        Stream output = this.reactor.returnReactorOutput(g_input, g_rxns, g_delX, g_maxIt);
        //default result is flow rate
        result = -output.returnSpecieFlowRate(g_desiredSpecie); //negative because search minimizes
        if (this.g_toMaximize.equalsIgnoreCase("selectivity")) result /= output.returnSpecieFlowRate(g_undesiredSpecie);
        return result;
    }

}
