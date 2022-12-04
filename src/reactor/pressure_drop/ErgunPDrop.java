package reactor.pressure_drop;
import chemistry.MultiComponentMixture;
import reactor.Catalyst;
import reactor.NominalPipeSizes;
import reactor.Stream;

import java.io.PipedInputStream;

public class ErgunPDrop extends PressureDropEquation{

    public static final PressureDropCondition condition = PressureDropCondition.ERGUN_CORRELATION;

    //global variables
    private Stream s0;
    private Stream s;
    private NominalPipeSizes pipeSize;

    //instance variable
    private Catalyst catalyst;

    // main constructor
    public ErgunPDrop(Catalyst catalyst, NominalPipeSizes pipeSize) {
        super();
        //TODO: error handling
        if(catalyst==null || pipeSize == null) System.exit(0);
        this.catalyst = catalyst.clone();
        this.pipeSize = pipeSize;
    }

    //copy constructor
    public ErgunPDrop(ErgunPDrop source) {
        super(source);
        //TODO: error handling
        if(source==null) System.exit(0);
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
        if(this.catalyst.equals(((ErgunPDrop) comparator).catalyst) == false) isEquals = false;
        return isEquals;
    }
}

