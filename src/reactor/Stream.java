package reactor;

import chemistry.*;

//FIXME: for new map classes


public class Stream extends MultiComponentMixture{
    //instance variables
    private double molarFlowRate;
    private double volFlowRate;



    //main constructor
    public Stream(CompositionMap composition, double T, double P,
                  double viscocity, double volFlowRate, double molarFlowRate ){
        //TODO do we want to take phase as an input? im not sure
        super(, composition, , viscocity, T, P);
        this.volFlowRate = volFlowRate;
        this.molarFlowRate = molarFlowRate;

    }

    //copy constructor
    public Stream(Stream source){

        super(source);

        this.volFlowRate = source.volFlowRate;
        this.molarFlowRate = source.molarFlowRate;

    }

    //accessors & mutators
    public double getMolarFlowRate() {
        return this.molarFlowRate;
    }

    public boolean setMolarFlowRate(double molarFlowRate) {
        if (molarFlowRate < 0) return false;

        this.molarFlowRate = molarFlowRate;
        return true;
    }

    public double getVolFlowRate(){
        return this.volFlowRate;
    }

    public boolean setVolFlowRate(double flowRate){
        //TODO: we cant set flow rate if its a gas?
        if (volFlowRate < 0) return false;
        this.volFlowRate = flowRate;
        return true;
    }

    public double returnMassFlowRate(){
        return this.molarFlowRate*this.returnMixtureMolarMass();
    }
    public double returnDensity(){
        return this.returnMassFlowRate()/this.volFlowRate;
    }

    //class methods
    public double returnSpecieFlowRate(Specie s){
        //TODO: null pointer exception
        if (s == null) {System.exit(0);}
        //TODO: check if this is adequate or if an error should be thrown
        if (!this.hasSpecie(s)) return -1.0;

        return super.returnSpecieMolFraction(s)*molarFlowRate;
    }
    public MolarFlowMap getAllFlowRates(){
        MolarFlowMap flowRates = new MolarFlowMap();
        Specie[] s = super.getSpecies();

        for (int i = 0; i < s.length; i++) {
            flowRates.put(s[i], returnSpecieFlowRate(s[i]));
        }
        return flowRates;
    }

    //TODO: some code to explain in the report
    public double returnTotalConcentration(){
        return getAllFlowRates().values().stream().reduce(0., (a, b) -> a + b);
    }

    public double returnSpecieMolConcentration(Specie s){
        if (s == null) ;//TODO: throw error
        if (!this.hasSpecie(s)) ; //TODO: throw error
        return this.returnSpecieFlowRate(s)/this.volFlowRate;
    }


    public SpecieMap returnAllMolConcentrations(){
        SpecieMap concentrations = new SpecieMap(this.returnNumberOfSpecies());
        this.getComposition().forEach((specie, v) ->
                concentrations.put(specie, returnSpecieFlowRate(specie)/this.volFlowRate));
        return concentrations;
    };

    public boolean setSpecieFlowRate(Specie s, double flowRate){

        if (s == null) {
            //todo: throw error
        }
        MolarFlowMap map = this.getAllFlowRates();

        //update species molar flow
        map.replace(s, flowRate);

        //update composition, volumetric and molar flow
        double FT = map.returnTotalMolarFlow();

        if (this.returnPhase() == Phase.IDEALGAS) {
            double FT0 = this.molarFlowRate;
            this.volFlowRate = this.volFlowRate*FT/FT0;
        }

        super.setComposition(new CompositionMap(map));
        this.molarFlowRate = FT;

        return true;

    }

    //clone
    public Stream clone(){
        return new Stream(this);
    }

    //toString
    public String toString(){
        return super.toString()+"\n Molar flow rate: "+this.molarFlowRate+"\n Volumetric flow rate: "+this.volFlowRate;
    }
    //equals
    public boolean equals(Object obj) {

        super.equals(obj);

        //create stream from obj for easy reference
        Stream objStream = (Stream)obj;

        //compare variables
        if (this.volFlowRate != objStream.volFlowRate) return false;
        if (this.molarFlowRate != objStream.molarFlowRate) return false;

        return true;



    }

}
