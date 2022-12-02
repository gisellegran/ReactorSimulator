package reactor;

import chemistry.*;
import numericalmethods.Euler;
import numericalmethods.SetOfODEs;
import reactor.heat_transfer.HeatTransferEquation;
import reactor.pressure_drop.PressureDropEquation;


public class PBRDesigner extends TubularReactorDesigner {


    //Global variables
    private Phase phase;
    private ReactionSet rxns; //TODO: not sure about having any of these in the reactor class
    private Specie[] speciesInReactor;
    private Stream input;
    private int tIndex; //index of temperature associated position in array
    private int pIndex; //index of pressure associated position in array

    private HeatTransferEquation heatX;
    private PressureDropEquation pDrop;




    private void setGlobalVariables(ReactionSet rxns, Stream input, PressureDropEquation pDrop, HeatTransferEquation heatX){

        this.phase = input.returnPhase();
        this.rxns = rxns.clone();
        MultiComponentMixture temp = input.clone();
        temp.addAllSpecies(rxns.returnSpecies());
        this.speciesInReactor = temp.getSpecies();
        this.input = input.clone();
        this.pIndex = this.speciesInReactor.length;
        this.tIndex = this.speciesInReactor.length+1;

        this.pDrop = pDrop;
        //this.heatX = heatX;//TODO: implement
    }

    private void resetGlobalVariables(){
        this.phase = null;
        this.rxns = null;
        this.speciesInReactor = null;
        this.input = null;
        this.tIndex = 0;
        this.pIndex = 0;
        this.pDrop = null;
        this.heatX = null;


    }

    protected double returnPDrop() {
        return 0.; //TODO: change this to pressure drop equation
    }

    protected double returnHeatX() {
        return 0.; //TODO: change this to heat transfer equation
    }

    private int getTargetIndex(Specie s) {
        int targetIndex = -1;
        //TODO: error handling
        for (int i = 0; i < this.speciesInReactor.length; i++) {
            if (this.speciesInReactor[i].equals(s)) {
                targetIndex = i;
                break;
            }
        }
        return targetIndex;
    }

    public PBR returnVForTargetFlow(Specie s, double targetF, Stream input, ReactionSet rxns,
                                    PressureDropEquation pDrop, HeatTransferEquation heatX, double delX, int maxIt){
        setGlobalVariables(rxns, input, pDrop, heatX);
        System.out.println("1");
        System.out.println(delX);

        //get total y array length; length = species + T + P
        int n = this.speciesInReactor.length + 2;

        double[] yf = new double[n];
        double[] y0 = new double[n];


        //get initial T & P
        y0[tIndex] = input.getT();
        y0[pIndex] = input.getP();


        double x0 = 0.;
        double xf =0.;

        int iterationCount = 0;
        int multiplyer = 2;
        //get inlet FlowRates
        for (int i = 0; i < this.speciesInReactor.length; i++) {
            if (input.hasSpecie(this.speciesInReactor[i])){
                y0[i] = input.returnSpecieFlowRate(this.speciesInReactor[i]);
            } else {
                // set flow rate of species which are not present in the input stream to 0
                y0[i] = 0;
            }
        }

        int targetIndex = this.getTargetIndex(s);

        do {
            iterationCount ++;
            //update parameters

            x0 = xf;
            xf += delX*multiplyer;
            System.out.println(xf);
            System.out.println(yf[targetIndex]);
            yf = Euler.integrate(x0, xf, y0, delX, maxIt, this);

            if (Math.abs(yf[targetIndex]-y0[targetIndex]) <0.0000001){
                multiplyer *= 2;
            }
            else if (Math.abs(yf[targetIndex]-y0[targetIndex]) >0.0000001){
                multiplyer /= 2;
            }
            //update y0
            for (int i = 0; i < this.speciesInReactor.length; i++) {
                y0[i] = yf[i];
            }



        } while (yf[targetIndex] < targetF && iterationCount<maxIt && xf<100000);

        if (xf>100000){
            //TODO: throw error
            System.out.println("input does not converge");
            System.exit(0);
        }

        NominalPipeSizes pipeSize = NominalPipeSizes.ONE_INCH;
        PBR result = new PBR(xf, pDrop, heatX, pipeSize);
        resetGlobalVariables();

        //generate stream
        return result;
    }

    public double[] calculateValue(double x, double[] y0){
        Stream currentOutput = this.getStreamFromY(y0);
        double[] dely = new double[y0.length];
        dely[this.tIndex] = returnHeatX();
        dely[this.pIndex] = returnPDrop();
        SpecieMap rates = this.rxns.returnNetRxnRates(y0[this.tIndex], currentOutput);
        for (int i = 0; i < this.speciesInReactor.length; i++) {
            dely[i] = rates.get(this.speciesInReactor[i]);
        }

        return dely;
    }

    //todo: fix
    public PBR buildReactor(double size, PressureDropEquation pDrop, HeatTransferEquation heatX, NominalPipeSizes pipeSize){
        return null;
    }

    protected double returnOutputGasVolFlowRate(double FT, double T, double P) {
        double v0, FT0, P0, T0;
        v0 = input.getVolFlowRate();
        FT0 = input.getMolarFlowRate();
        P0 = input.getP();
        T0 = input.getT();
        return v0*(FT/FT0)*(P0/P)*(T/T0);
    }
}
