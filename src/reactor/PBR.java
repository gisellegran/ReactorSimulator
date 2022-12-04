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

    public Catalyst getCatalyst() {
        return this.catalyst.clone();
    }

    //mutators

    public boolean setCatalyst(Catalyst catalyst) {
        if (catalyst==null) return false;
        this.catalyst = catalyst.clone();
        return true;
    }

    //class methods
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
    @Override
    public boolean equals(Object comparator) {
        if (!super.equals(comparator)) return false;
        PBR objPBR = (PBR)comparator;
        if (!(this.catalyst.equals(objPBR.catalyst))) return false;
    return true;
    }



}
