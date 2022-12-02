package reactor.heat_transfer;

import reactor.NominalPipeSizes;

public class HeatTransferBuilder {

    public HeatTransferEquation createHeatTransferEquation(HeatTransferCondition condition, double U, double Ta, NominalPipeSizes pipeSize){
        switch (condition){
            case ISOTHERMAL:
                return new Isothermal(U, 0., pipeSize);
            case COCURRENT:
                return new HeatTransferEquation(U, Ta, pipeSize);
            case COUNTERCURRENT:
                return new HeatTransferEquation(U, Ta, pipeSize);
            case ADIABATIC:
                U = 0.;
                return new HeatTransferEquation(U, 0., pipeSize);
        }

        return new Isothermal(U, Ta, pipeSize);
    }
}
