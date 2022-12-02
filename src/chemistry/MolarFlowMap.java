package chemistry;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class MolarFlowMap extends SpecieMap {

    //TODO: can these unused constructors be deleted
    public MolarFlowMap() {
        super();
    }

    public MolarFlowMap( int initialCapacity ) {
        super(initialCapacity);
    }

    public MolarFlowMap( int initialCapacity, float loadFactor ) {
        super(initialCapacity, loadFactor);
    }

    //copy constructor i think?
    public MolarFlowMap ( Map t) {
        super(t);
    }

    //arrays to map constructor
    public MolarFlowMap (Specie[] species, double[] molarFlows ){ super(species, molarFlows); }

    //TODO: explain list in report
    public Collection<Double> values() {
        return super.values();
    }

    //TODO: explain list in report
    public double returnTotalMolarFlow(){

        Double[] flows = this.values().toArray(new Double[this.size()]);
        double totalFlow = 0.;
        for (int i = 0; i < flows.length; i++) {
            totalFlow += flows[i];
        }

        return totalFlow;
    }

    //clone
    public MolarFlowMap clone() {
        return (MolarFlowMap) this.clone();
    }
}
