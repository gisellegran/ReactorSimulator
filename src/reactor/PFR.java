package reactor;


import reactor.heat_transfer.HeatTransferEquation;
import reactor.pressure_drop.PressureDropEquation;

public class PFR extends TubularReactor {


    //instance variables

    //main constructor
    public PFR(double volume , PressureDropEquation pDrop, HeatTransferEquation heatX, NominalPipeSizes pipeSize) {
        super(volume, pDrop, heatX, pipeSize);
    }

    //copy constructor
    public PFR(PFR source){
        super(source);
    }
    //accessors
    //mutators
    //class methods
    public double returnA(){
       return 4/this.pipeSize.returnInnerDiameter();}

    public String toString(){
        String str = "Reactor type: PFR\n";
        str+= super.toString();
        return str;
    }
    //clone
    public PFR clone() {
        return new PFR(this);
    }

    //equals
    //TODO: implement
}
