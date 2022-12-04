package reactor.pressure_drop;
import chemistry.MultiComponentMixture;
import reactor.Catalyst;
import reactor.NominalPipeSizes;
import reactor.Stream;

public class ErgunPDrop extends PressureDropEquation{

    public static final PressureDropCondition condition = PressureDropCondition.ERGUN_CORRELATION;

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

        if(source == null) throw new IllegalArgumentException("source is null");
        this.catalyst = source.catalyst.clone();
        this.pipeSize = source.pipeSize;
    }

    //class methods
    public double calculateValue(MultiComponentMixture s) {
        if (s.getClass() != Stream.class){
            throw new IllegalArgumentException("input must be a stream object");
        }

        Stream stream = (Stream)s;

        double rhsODE;
        rhsODE = (-this.catalyst.calculateErgunParameter(stream,this.pipeSize)/(2.));
        return rhsODE;
    }

    public PressureDropCondition getPressureDropCondition(){
        return this.condition;
    }

    //clone
    public ErgunPDrop clone() {return new ErgunPDrop(this);}

    //equals
    public boolean equals(Object comparator) {
        if (!super.equals(comparator)) return false;

        boolean isEquals = true;
        if(!this.catalyst.equals(((ErgunPDrop) comparator).catalyst)) isEquals = false;
        return isEquals;
    }
}

