package chemistry;

public class Specie {

    //instance variables
    private String name;
    private double[] heatCapacityCoeffs;

    private double molarMass;
    private Phase phase;

    //main constructor
    public Specie(String name, Phase phase, double molarMass, double[] heatCapacityCoeffs) {

        // TODO check what to do with this
        // TODO do we need to check if a Enum is null
        if ( heatCapacityCoeffs == null) { throw new IllegalArgumentException("null heat capacities"); }
        if ( phase == null) { throw new IllegalArgumentException("phase is null"); }

        else if (heatCapacityCoeffs.length != 5){
            // TODO throw some error
        }
        this.name = name;
        this.phase = phase;
        this.molarMass = molarMass;

        this.heatCapacityCoeffs = new double[heatCapacityCoeffs.length];

        for (int i = 0; i < this.heatCapacityCoeffs.length; i++) {
            this.heatCapacityCoeffs[i] = heatCapacityCoeffs[i];
        }
    }

    //copy constructor
    public Specie(Specie source) {
        // TODO throw null pointer exception?
        if (source == null) {System.exit(0);}
        this.name = source.name;
        this.phase = source.phase;

        this.heatCapacityCoeffs = new double[source.heatCapacityCoeffs.length];

        for (int i = 0; i < source.heatCapacityCoeffs.length; i++) {
            this.heatCapacityCoeffs[i] = source.heatCapacityCoeffs[i];
        }

        this.molarMass = source.molarMass;
    }

    //accessor
    public String getName() {
        return this.name;
    }

    public double[] getHeatCapacityCoeffs() {
        double[] temp = new double[this.heatCapacityCoeffs.length];

        for (int i = 0; i < this.heatCapacityCoeffs.length; i++) {
            temp[i] = this.heatCapacityCoeffs[i];
        }

        return temp;
    }

    public Phase getPhase() {
        return this.phase;
    }

    public double getMolarMass() {return this.molarMass;}

    //mutator
    public boolean setName(String name) {
        // TODO correct exception?
        if (name == null) {
            return false;
        }

        this.name = name;

        return true;
    }

    public boolean setHeatCapacityConstants(double[] heatCapacityCoeffs) {

        if(heatCapacityCoeffs == null) return false;
        if(heatCapacityCoeffs.length != this.heatCapacityCoeffs.length ) return false;

        for (int i = 0; i < heatCapacityCoeffs.length; i++)
            this.heatCapacityCoeffs[i] = heatCapacityCoeffs[i];

        return true;
    }

    public boolean setPhase(Phase phase){

        if ( phase == null) return false;
        this.phase = phase;
        return true;
    }

    public boolean setMolarMass(double molarMass) {
        if (molarMass < 0) return false;

        this.molarMass = molarMass;
        return true;
    }

    //class methods
    public double returnHeatCapacity(double T) {
        double cp = 0.0;

        for (int i = 0; i < this.heatCapacityCoeffs.length; i++) {
            cp += this.heatCapacityCoeffs[i]*Math.pow(T, i);
        }
        return cp;
    }

    //integrate Cp from T0 to T
    public double returnIntegralHeatCapacity(double T0, double T) {
        double cp = 0.0;
        for (int i = 0; i < this.heatCapacityCoeffs.length; i++) {
            cp +=  (this.heatCapacityCoeffs[i]/(i+1.))*(Math.pow(T, i+1.)-Math.pow(T0, i+1.));
        }
        return cp;
    }




    //clone
    public Specie clone(){
        return new Specie(this);
    };


    public boolean equals(Object obj) {
        //check that obj isnt null
        if ( obj == null ) { return false;}

        //check that classes are equal
        if (this.getClass() != obj.getClass()){ return false;}

        //compare name
        if (!(this.name).equals(((Specie)obj).name)){ return false;}

        //compare phase
        if (!(this.phase).equals(((Specie)obj).phase)){ return false;}

        //compare heatCapacityCoeffs
        double[]  objHeatCapacityCoeffs = ((Specie)obj).heatCapacityCoeffs;

        for (int i = 0; i < this.heatCapacityCoeffs.length; i++) {
            if (this.heatCapacityCoeffs[i] != objHeatCapacityCoeffs[i]) {return false;}
        }

        return true;
    }

    @Override
    //TODO: in report mention is necessary for the hashmap class to be able to compare the specie values
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + name.hashCode();
        result = prime * result + phase.hashCode();
        return result;
    }

    public String toString() {return "Specie:"+name;}
}
