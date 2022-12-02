package reactor;

import chemistry.CompositionMap;
import chemistry.MolarFlowMap;

import static chemistry.RateConstant.R;

public class StreamBuilder {

    public static  Stream buildStream(MolarFlowMap map, double T, double P,
                                         double viscocity, double volFlowRate) {
        return new Stream(new CompositionMap(map), T, P, viscocity, volFlowRate, map.returnTotalMolarFlow());
    }

    public static  Stream buildGasStream(MolarFlowMap map, double T, double P, double viscocity) {
        double volFlowRate = StreamBuilder.returnGasVolFLow(map.returnTotalMolarFlow(), T, P);
        return new Stream(new CompositionMap(map), T, P, viscocity, volFlowRate, map.returnTotalMolarFlow());
    }

    protected static double returnGasVolFLow(double FT, double T, double P) {

        return FT*R*T/P;
    }


}
