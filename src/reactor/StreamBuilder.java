package reactor;

import chemistry.Specie;

import static chemistry.RateConstant.R;

public class StreamBuilder {

    public static  Stream buildStreamFromMolFlows(Specie[] species, double[] molFlows, double T, double P,
                                                  double viscocity, double volFlowRate) {
        //TODO: check for null molFlows array
        double totalMolFlow = StreamBuilder.returnTotalMolFlow(molFlows);

        double[] molComposition = StreamBuilder.returnMolComposition(molFlows);

        return new Stream(species, molComposition, T, P, viscocity, volFlowRate, totalMolFlow);
    }

    public static  Stream buildGasStream(Specie[] species, double[] molFlows,
                                                  double viscocity, double volFlowRate) {
        //TODO: check for null molFlows array
        double totalMolFlow = StreamBuilder.returnTotalMolFlow(molFlows);

        double[] molComposition = StreamBuilder.returnMolComposition(molFlows);

        //default values of T & P are 298 and 101325
        return new Stream(species, molComposition, 298, 101325, viscocity, volFlowRate, totalMolFlow);
    }

    private static double returnTotalMolFlow(double[] molFlows){
        //dont need to check null array because already checked in the public methods
        //which call this method as a helper method
        double totalMolFlow = 0.;
        for (int i = 0; i < molFlows.length; i++) {
            totalMolFlow += molFlows[i];
        }

        return totalMolFlow;
    }

    private static double[] returnMolComposition(double[] molFlows){
        double totalMolFlow = StreamBuilder.returnTotalMolFlow(molFlows);
        double[] molComposition = new double[molFlows.length];
        for (int i = 0; i < molFlows.length; i++) {
            molComposition[i] = molFlows[i]/totalMolFlow;
        }
        return molComposition;
    }


    public static Stream buildGasStreamFromMolFlows(Specie[] species, double[] molFlows, double T, double P, double viscocity) {
        double totalMolFlow = StreamBuilder.returnTotalMolFlow(molFlows);
        double volFlowRate = StreamBuilder.returnGasVolFLow(totalMolFlow, T, P);
        //now that we have the total vol flow rate, we can just use the buildStreamFromMolFlows method instead of repeating the same code
        return StreamBuilder.buildStreamFromMolFlows(species, molFlows, T,  P, viscocity, volFlowRate);
    }

    public static Stream buildGasStream(Specie[] species, double[] composition, double T, double P, double viscocity, double totalMolFlow) {
        double volFlowRate = StreamBuilder.returnGasVolFLow(totalMolFlow, T, P);
        //now that we have the total vol flow rate, we can just use the buildStreamFromMolFlows method instead of repeating the same code
        return new Stream(species, composition, T,  P, viscocity, volFlowRate, totalMolFlow);
    }

    protected static double returnGasVolFLow(double FT, double T, double P) {

        return FT*R*T/P;
    }


}
