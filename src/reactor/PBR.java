package reactor;

import reactor.heat_transfer.HeatTransferEquation;
import reactor.pressure_drop.PressureDropEquation;

public class PBR extends TubularReactor {

    //instance variables
    private Catalyst catalyst;

    //main constructor
    public PBR(double weight, PressureDropEquation pDrop, HeatTransferEquation heatX, NominalPipeSizes pipeSize, Catalyst catalyst) {
        super(weight, pDrop, heatX, pipeSize);
        if (catalyst == null) throw new IllegalArgumentException("catalyst is null");
        this.catalyst = catalyst.clone();
    }

    //copy constructor
    public PBR(PBR source){
        super(source);
        this.catalyst = source.catalyst.clone();
    }
    //accessors
    //mutators
    //class methods
    //todo: implement

    public double returnA(){
        return (4/this.pipeSize.returnInnerDiameter())/this.catalyst.returnBulkDensity();
    }

    //toString
    public String toString(){
        String str = "Reactor type: PBR\n";
        str+= super.toString();
        //todo: add catalyst to string
        return str;
    }
    //clone
    public PBR clone() {
        return new PBR(this);
    }

    //equals
    //TODO: implement

}
