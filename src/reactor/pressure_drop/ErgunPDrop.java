package reactor.pressure_drop;
import chemistry.MultiComponentMixture;
import reactor.Catalyst;
import reactor.NominalPipeSizes;
import reactor.Stream;

import java.io.PipedInputStream;

public class ErgunPDrop extends PressureDropEquation{

    //global variables
    private Stream s0;
    private Stream s;
    private NominalPipeSizes pipeSize;

    //instance variable
    private Catalyst catalyst;

    // main constructor
    public ErgunPDrop(Catalyst catalyst, NominalPipeSizes pipeSize) {
        super();

        if(catalyst==null || pipeSize == null) throw new IllegalArgumentException("catalyst parameter is null");
        this.catalyst = catalyst.clone();
        this.pipeSize = pipeSize;
    }

    //copy constructor
    public ErgunPDrop(ErgunPDrop source) {
        super(source);

        if(source==null) throw new IllegalArgumentException("source is null");
        this.catalyst = source.catalyst.clone();
        this.pipeSize = source.pipeSize;
    }

    //class methods
    public double calculateValue(MultiComponentMixture s) {
        if (s.getClass() != Stream.class){
            //TODO: is this bad practice
        }

        Stream stream = (Stream)s;

        double rhsODE;
        rhsODE = (-this.catalyst.calculateErgunParameter(stream,this.pipeSize)/(2.));
        return rhsODE;
    }

    //clone
    public ErgunPDrop clone() {return new ErgunPDrop(this);}

    //equals
    public boolean equals(Object comparator) {
        if (!super.equals(comparator)) return false;

        boolean isEquals = true;
        if(!this.catalyst.equals(((ErgunPDrop) comparator).catalyst)) isEquals = false;
        if(!this.s.equals(((ErgunPDrop) comparator).s)) isEquals = false;
        if(!this.s0.equals(((ErgunPDrop) comparator).s0)) isEquals = false;
        return isEquals;
    }
}

