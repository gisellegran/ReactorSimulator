package reactor;

import chemistry.*;
import numericalmethods.Euler;
import reactor.heat_transfer.HeatTransferEquation;
import reactor.pressure_drop.PressureDropEquation;

public abstract class TubularReactor extends Reactor {

    //instance variables
    NominalPipeSizes pipeSize; //pipe size of the reactor

    //global variables
    private Phase phase;
    private ReactionSet reactions; //TODO: not sure about having any of these in the reactor class

    private Specie[] speciesInReactor;
    private Stream inputStream;
    private int tIndex; //index of temperature associated position in array
    private int pIndex; //index of pressure associated position in array

    //main constructor
    public TubularReactor(double size, PressureDropEquation pDrop, HeatTransferEquation heatX, NominalPipeSizes pipeSize) {
        super(size, pDrop, heatX);

        if (pipeSize==null) throw new IllegalArgumentException("Pipe size of the reactor is null");
        this.pipeSize = pipeSize;
        resetGlobalVariables();
    }

    //copy constructor
    public TubularReactor(TubularReactor source){
        super(source);
        this.pipeSize = source.pipeSize;
        resetGlobalVariables();
    }

    //global variables handlers
    protected void setGlobalVariables(ReactionSet rxns, Stream input) {
        this.phase = input.returnPhase();
        this.reactions = rxns.clone();
        MultiComponentMixture temp = input.clone();
        temp.addAllSpecies(rxns.returnSpecies());
        this.speciesInReactor = temp.getSpecies();
        this.inputStream = input.clone();
        this.pIndex = this.speciesInReactor.length;
        this.tIndex = this.speciesInReactor.length+1;

    }

    protected void resetGlobalVariables() {
        this.phase = null;
        this.reactions = null;
        this.speciesInReactor = null;
        this.inputStream = null;
        this.tIndex = 0;
        this.pIndex = 0;
    }

    //accessors

    public NominalPipeSizes getPipeSize() {
        return this.pipeSize;
    }

    //mutators

    public boolean setPipeSize(NominalPipeSizes pipeSize) {
        if (pipeSize==null) return false;
        this.pipeSize = pipeSize;
        return true;
    }

    //class methods

    //TODO: maybe fix this to just take stream and remove it form the reactor class
    public Stream returnReactorOutput(MultiComponentMixture input, ReactionSet rxn, double delX, int maxIt) {
        if (input.getClass()  != Stream.class) {
            {throw new IllegalArgumentException("Not the same class");}
        }

        return this.returnReactorOutputAtPoint(this.getSize(), (Stream)input, rxn, delX, maxIt);
    }

    //point is either weight or volume depending if its a PFR of a PBR
    public Stream returnReactorOutputAtPoint(double point, Stream input, ReactionSet rxn,
                                             double delX, int maxIt){

        //checks to make first
        //check for null objects
        if (input == null || rxn == null) {
            {throw new IllegalArgumentException("The reaction and input are null");}
        }
        //is point adequate
        if (point < 0 || point > super.getSize()) {
            {throw new IllegalArgumentException("Volume or Weight is negative");}
        }

        if (input.getClass()  != Stream.class) {
            {throw new IllegalArgumentException("not the same class");}
        }

        setGlobalVariables(rxn, (Stream)input);

        //get total y array length; length = species + T + P
        int n = this.speciesInReactor.length + 2;

        double[] y_f = new double[n];
        double[] y_0 = new double[n];

        //get initial T & P
        y_0[tIndex] = input.getT();
        y_0[pIndex] = input.getP();

//      //get inlet FlowRates
        for (int i = 0; i < this.speciesInReactor.length; i++) {
            if (input.hasSpecie(this.speciesInReactor[i])){
                y_0[i] = input.returnSpecieFlowRate(this.speciesInReactor[i]);
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

    protected double returnPDrop(Stream s) {
        return super.getpDrop().calculateValue(s); //TODO: change this to pressure drop equation
    }
    protected double returnHeatX() {
        return 0.; //TODO: change this to heat transfer equation
    }

    public double returnA(){
        return 4/pipeSize.returnInnerDiameter();
    }
    //takes numerical methods output and converts it to a stream
    protected Stream getStreamFromY(double[] y) {

        if (y == null) {throw new IllegalArgumentException("y is null");}
        double T, P, viscocity;

        //make local deep copy of y
        double[] tempY = new double[y.length];

        for (int i = 0; i < tempY.length; i++) {
            tempY[i] = y[i];
        }

        //get T and P
        T = y[this.tIndex];
        P = y[this.pIndex];

        //viscocity stays constant in our case
        viscocity = inputStream.getViscosity();

        //put flow rates in an array
        double[] flowRates = new double[tempY.length-2];

        for (int i = 0; i < flowRates.length; i++) { flowRates[i] = y[i]; }

        Stream result = null;
        if (this.phase == Phase.IDEALGAS) {
            //gas is compressible
            result = StreamBuilder.buildGasStreamFromMolFlows(this.speciesInReactor, flowRates, T, P, viscocity);
        }
        else if (this.phase == Phase.LIQUID) {
            //assume constant density => constant flow rate
            double volFlow = inputStream.getVolFlowRate();
            result = StreamBuilder.buildStreamFromMolFlows(this.speciesInReactor, flowRates, T, P, viscocity, volFlow);}
        else {
            {throw new IllegalArgumentException("Flow rate is assumed to be constant");}
        }

        return result;

    }

    public double[] calculateValue(double x, double[] y0){
        Stream currentOutput = this.getStreamFromY(y0);
        double[] dely = new double[y0.length];
        dely[this.tIndex] = returnHeatX();
        dely[this.pIndex] = returnPDrop(currentOutput);
        double[] rates = this.reactions.returnNetRxnRates(y0[this.tIndex], currentOutput);
        for (int i = 0; i < this.speciesInReactor.length; i++) {
            dely[i] = rates[i];
        }

        return dely;
    }

    public static double returnConversion(Specie s, Stream input, Stream output){
        if (s == null || input == null || output == null) {
            {throw new IllegalArgumentException("Specie, inlet stream  or outlet stream is null");}
        }
        double inFlow = input.returnSpecieFlowRate(s);
        double outFlow = output.returnSpecieFlowRate(s);

        if (inFlow < 0.){
            {throw new IllegalArgumentException("inlet flow is negative");}
        } else if (inFlow<outFlow) {
            {throw new IllegalArgumentException("inlet flow is less than outlet flow");}
        }

        return (inFlow-outFlow)/inFlow;

    }


    //clone
    public abstract TubularReactor clone();
    //equals


    public boolean equals(Object comparator) {
        boolean isEquals=true;

        if (comparator == null) return false;
        else if (this.getClass() != comparator.getClass()) return false;
        else
        if (!(((TubularReactor)comparator).pipeSize ==(this.pipeSize)))isEquals = false;

        return isEquals;
    }
}
