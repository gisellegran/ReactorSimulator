package reactor;

import chemistry.*;
import numericalmethods.SetOfODEs;
import reactor.heat_transfer.HeatTransferEquation;
import reactor.pressure_drop.PressureDropEquation;

public abstract class TubularReactorDesigner implements SetOfODEs{

    //instance variables
    private PressureDropEquation pDrop;
    private HeatTransferEquation heatX;

        //Global variables
        private Phase phase;
        private ReactionSet rxns; //TODO: not sure about having any of these in the reactor class
        private Specie[] speciesInReactor;
        private Stream input;
        private int tIndex; //index of temperature associated position in array
        private int pIndex; //index of pressure associated position in array




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
            this.pDrop = pDrop;
            this.heatX = heatX;


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

        //TODO: fix
    /*
        public TubularReactor returnReactorForTargetFlow(Specie s, double targetF, Stream input, ReactionSet rxns,
                                        PressureDropEquation pDrop, HeatTransferEquation heatX, NominalPipeSizes pipeSize, double delX, int maxIt){
            setGlobalVariables(rxns, input, pDrop, heatX);

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
            double multiplyer = 1.;
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

                yf = Euler.integrate(x0, xf, y0, delX, maxIt, this);

                if ((Math.abs(yf[targetIndex]-y0[targetIndex])/y0[targetIndex]) <0.001){
                    multiplyer *= 2.;
                }

            if ((Math.abs(yf[targetIndex]-y0[targetIndex])/y0[targetIndex]) >0.1){
                multiplyer /= 2.;
            }

                //update y0
                for (int i = 0; i < this.speciesInReactor.length; i++) {
                    y0[i] = yf[i];
                }



            } while (yf[targetIndex] > targetF && iterationCount<maxIt && xf<100000);

            if (xf>100000){
                //TODO: throw error
                System.out.println("input does not converge");
                System.exit(0);
            }
            TubularReactor result = this.buildReactor(xf, pDrop, heatX, pipeSize);
            resetGlobalVariables();

            //generate stream
            return result;
        } */

        public abstract TubularReactor buildReactor(double size, PressureDropEquation pDrop, HeatTransferEquation heatX, NominalPipeSizes pipeSize);

        public double[] calculateValue(double x, double[] y0){
            Stream currentOutput = this.getStreamFromY(y0);
            double[] dely = new double[y0.length];
            dely[this.tIndex] = returnHeatX();
            dely[this.pIndex] = returnPDrop();
            double[] rates = this.rxns.returnNetRxnRates(currentOutput);
            for (int i = 0; i < this.speciesInReactor.length; i++) {
                dely[i] = rates[i];
            }

            return dely;
        }

        protected Stream getStreamFromY(double[] y) {
            //TODO: error handling
            if (y == null) {
            }

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
            viscocity = input.getViscosity();

            //put flow rates in an array
            double[] flowRates = new double[tempY.length - 2];

            for (int i = 0; i < flowRates.length; i++) {
                flowRates[i] = y[i];
            }

            Stream result = null;
            if (this.phase == Phase.IDEALGAS) {
                //gas is compressible
                result = StreamBuilder.buildGasStreamFromMolFlows(this.speciesInReactor, flowRates, T, P, viscocity);
            }
            else if (this.phase == Phase.LIQUID) {
                //assume constant density => constant flow rate
                double volFlow = input.getVolFlowRate();
                result = StreamBuilder.buildStreamFromMolFlows(this.speciesInReactor, flowRates, T, P, viscocity, volFlow);
            } else {
                //TODO: throw error
            }

            return result;
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
