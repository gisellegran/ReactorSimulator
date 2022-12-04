package reactor;

import reactor.heat_transfer.HeatTransferEquation;
import reactor.pressure_drop.PressureDropEquation;

public class PBR extends TubularReactor {

    //instance variables
    private Catalyst catalyst;

    //main constructor
    public PBR(double weight, PressureDropEquation pDrop, HeatTransferEquation heatX, NominalPipeSizes pipeSize) {
        super(weight, pDrop, heatX, pipeSize);
        if (catalyst==null) throw new IllegalArgumentException("catalyst parameter is null");
        this.catalyst = catalyst.clone();

    }

    //copy constructor
    public PBR(PBR source){
        super(source);
        if (catalyst==null) throw new IllegalArgumentException("source is null");
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

    public PBR clone(){return new PBR(this);}


    public double returnA(){
        return (4/this.pipeSize.returnInnerDiameter())/this.catalyst.returnBulkDensity();
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
