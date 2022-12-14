package reactor;

import chemistry.*;
import numericalmethods.Euler;
import reactor.heat_transfer.FreeConvective;
import reactor.heat_transfer.HeatTransferCondition;
import reactor.heat_transfer.HeatTransferEquation;
import reactor.heat_transfer.HeatExchanger;
import reactor.pressure_drop.PressureDropEquation;

public abstract class TubularReactor extends Reactor {

    //instance variables
    NominalPipeSizes pipeSize; //pipe size of the reactor

    //global variables
    private Phase g_phase;
    private ReactionSet g_reactions; //TODO: not sure about having any of these in the reactor class

    private Specie[] g_speciesInReactor; //TODO try to remove
    private Stream g_inputStream;


    private int g_Pindex; //index of pressure  in array
    private int g_Tindex; //index of temperature  in array
    private int g_TaIndex; //index of ambient temperature in array (temperature outside reactor)
    private double g_a; //heat transfer area per unit volume
    private HeatTransferCondition g_heatCondition;

    private double g_Ta; //ambiant temperature

    //main constructor
    public TubularReactor(double size, PressureDropEquation pDrop, HeatTransferEquation heatX, NominalPipeSizes pipeSize) {
        super(size, pDrop, heatX);
        //TODO: check if the enum is null?
        this.pipeSize = pipeSize;
        resetGlobalVariables();
    }

    //copy constructor
    public TubularReactor(TubularReactor source){
        super(source);
        resetGlobalVariables();
    }

    //global variables handlers
    protected void setGlobalVariables(ReactionSet rxns, Stream input) {
        this.g_phase = input.returnPhase();
        this.g_reactions = rxns.clone();
        this.g_speciesInReactor = input.getSpecies();
        this.g_inputStream = input.clone();
        this.g_Pindex = this.g_speciesInReactor.length;
        this.g_Tindex = this.g_speciesInReactor.length+1;
        //we only need to consider Ta when we are in non adiabatic non isothermal conditions
        if (this.getHeatX() instanceof HeatExchanger || this.getHeatX() instanceof FreeConvective) {
            this.g_TaIndex = this.g_speciesInReactor.length+2;//index of Ta
            this.g_Ta = this.getHeatX().getTa0(); //ambiant temperature
        }
        this.g_a = this.returnA();

    }

    protected void resetGlobalVariables() {
        this.g_phase = null;
        this.g_reactions = null;
        this.g_speciesInReactor = null;
        this.g_inputStream = null;
        this.g_Tindex = -1;
        this.g_Pindex = -1;
        this.g_TaIndex = -1;
        this.g_a = -1;
    }

    //accessors
    //mutators
    //class methods

    //TODO: maybe fix this to just take stream and remove it form the reactor class
    public Stream returnReactorOutput(MultiComponentMixture input, ReactionSet rxn, double delX, int maxIt) {
        if (input.getClass()  != Stream.class) {
           // TODO: throw errror
        }

        return this.returnReactorOutputAtPoint(this.getSize(), (Stream)input, rxn, delX, maxIt);
    }

    //point is either weight or volume depending if its g_a PFR of g_a PBR
    public Stream returnReactorOutputAtPoint(double point, Stream input, ReactionSet rxn,
                                             double delX, int maxIt){
        //TODO; implement check that all species in the reactions ar ein the input stream and vice versa
        //checks to make first
        //check for null objects
        if (input == null || rxn == null) {
            //TODO: throw error
        }
        //is point adequate
        if (point < 0 || point > super.getSize()) {
            //TODO: throw error
        }

        if (input.getClass()  != Stream.class) {
            // TODO: throw errror
        }

        setGlobalVariables(rxn, (Stream)input);

        //get total y array length; length = species + T + P
        int n = this.g_speciesInReactor.length + 2;

        //we only need to consider Ta when we are in non adiabatic non isothermal conditions
        //this is checked when we set the global variables; if we are not in these conditions, g_TaIndex remains -1
        //otherwise, we need a spot in the array to store this value
        if (g_TaIndex > 0) n++;

        double[] y_f = new double[n];
        double[] y_0 = new double[n];

        //get initial T & P
        y_0[g_Tindex] = input.getT();
        y_0[g_Pindex] = input.getP();
        if (g_TaIndex > 0) y_0[g_TaIndex] = this.getHeatX().getTa0(); //set Ta0 if we are in non adiabatic/non isothermal conditions

        //get inlet FlowRates
        for (int i = 0; i < this.g_speciesInReactor.length; i++) {
            if (input.hasSpecie(this.g_speciesInReactor[i])){
                y_0[i] = input.returnSpecieFlowRate(this.g_speciesInReactor[i]);
            } else {
                // set flow rate of species which are not present in the input stream to 0
                y_0[i] = 0;
            }
        }

        y_f = Euler.integrate(0, point, y_0, delX, maxIt, this);

        Stream result = this.getStreamFromY(y_f);
        resetGlobalVariables();

        //generate stream
        return result;
    }

    public abstract double returnA();

    //takes numerical methods output and converts it to g_a stream
    protected Stream getStreamFromY(double[] y) {
        //TODO: error handling
        if (y == null) {}

        double T, P, viscocity;

        //make local deep copy of y
        double[] tempY = new double[y.length];

        for (int i = 0; i < tempY.length; i++) {
            tempY[i] = y[i];
        }

        //get T and P
        T = y[this.g_Tindex];
        P = y[this.g_Pindex];

        //viscocity stays constant in our case
        viscocity = g_inputStream.getViscosity();

        //put flow rates in an array
        double[] flowRates = new double[this.g_speciesInReactor.length];

        for (int i = 0; i < flowRates.length; i++) { flowRates[i] = y[i]; }

        Stream result = null;
        if (this.g_phase == Phase.IDEALGAS) {
            //gas is compressible
            result = StreamBuilder.buildGasStreamFromMolFlows(this.g_speciesInReactor, flowRates, T, P, viscocity);
        }
        else if (this.g_phase == Phase.LIQUID) {
            //assume constant density => constant flow rate
            double volFlow = g_inputStream.getVolFlowRate();
            result = StreamBuilder.buildStreamFromMolFlows(this.g_speciesInReactor, flowRates, T, P, viscocity, volFlow);}
        else {
            //TODO: throw error
        }

        return result;

    }

    public double[] calculateValue(double x, double[] y){
        Stream currentOutput = this.getStreamFromY(y);
        double[] dely = new double[y.length];
        dely[this.g_Pindex] = this.getpDrop().calculateValue(currentOutput);
        dely[this.g_Tindex] = this.getHeatX().calculateDelT(g_a, currentOutput, g_reactions);

        if (!(g_TaIndex<0)) dely[this.g_TaIndex] = ((HeatExchanger)this.getHeatX()).calculateDelTa(this.g_a, y[g_TaIndex], currentOutput.getT());//calculate delTa if in heat transfer conditions

        double[] rates = this.g_reactions.returnNetRxnRates(currentOutput);
        for (int i = 0; i < this.g_speciesInReactor.length; i++) {
            dely[i] = rates[i];
        }

        return dely;
    }

    public static double returnConversion(Specie s, Stream input, Stream output){
        if (s == null || input == null || output == null) {
            //TODO: throw error
        }
        double inFlow = input.returnSpecieFlowRate(s);
        double outFlow = output.returnSpecieFlowRate(s);

        if (inFlow < 0.){
            //TODO: throw error
        } else if (inFlow<outFlow) {
            //TODO: throw error
        }

        return (inFlow-outFlow)/inFlow;

    };

    //clone
    public abstract TubularReactor clone();
    //equals
}
