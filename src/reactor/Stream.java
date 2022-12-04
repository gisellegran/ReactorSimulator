package reactor;

import chemistry.*;

import java.text.DecimalFormat;

//FIXME: for new map classes


public class Stream extends MultiComponentMixture{
    //instance variables
    private double molarFlowRate;
    private double volFlowRate;



    //main constructor
    public Stream(Specie[] species, double[] molComposition, double T, double P,
                  double viscocity, double volFlowRate, double molarFlowRate ){
        //TODO do we want to take phase as an input? im not sure
        super(species, molComposition, T, P, viscocity);
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
    public double[] getAllFlowRates(){
        int n = this.returnNumberOfSpecies(); //number of species in the stream
        double[] molComp = this.getMolComposition();
        double[] flowRates = new double[n];
        for (int i = 0; i < n; i++) {
            flowRates[i] = molComp[i] * this.molarFlowRate;
        }
        return flowRates;
    }

    //TODO: some code to explain in the report
    public double returnTotalConcentration(){
        return this.molarFlowRate/this.volFlowRate;
    }

    public double returnSpecieMolConcentration(Specie s){
        if (s == null) ;//TODO: throw error
        if (!this.hasSpecie(s)) ; //TODO: throw error
        return this.returnSpecieFlowRate(s)/this.volFlowRate;
    }


    public double[] returnAllMolConcentrations(){

        double[] concentrations = this.getMolComposition();
        for (int i = 0; i < concentrations.length; i++) {
            concentrations[i] = concentrations[i]*this.returnTotalConcentration();
        }
        return concentrations;
    };

    //todo: probably remove if not needed
    /*public boolean setSpecieFlowRate(Specie s, double flowRate){

        if (s == null) {
            //todo: throw error
        }
        //MolarFlowMap map = this.getAllFlowRates();

        //update species molar flow
        map.replace(s, flowRate);

        //update composition, volumetric and molar flow
        double FT = map.returnTotalMolarFlow();

        if (this.returnPhase() == Phase.IDEALGAS) {
            double FT0 = this.molarFlowRate;
            this.volFlowRate = this.volFlowRate*FT/FT0;
        }

        //super.setComposition(new CompositionMap(map));
        this.molarFlowRate = FT;

        return true;

    }*/

    //clone
    public Stream clone(){
        return new Stream(this);
    }

    public static String doubleArrayToString(Specie[] s, double[] array){
        DecimalFormat df = new DecimalFormat("0.000");
        String str = "";
        for (int i = 0; i < array.length; i++) {
            str += (s[i].getName()+": "+df.format(array[i]));
            if (i < array.length-1) str += ", ";
        }
        return "{ "+str+" }";
    }

    //toString
    //todo: maybe remove
    /*
    public String toString(){
        String str = "Total molar flow rate: "+this.molarFlowRate;
        Specie[] species = this.getSpecies();
        str += "Component molar flow rate: "+this.molarFlowRate;

        return super.toString()+"\n Molar flow rate: "+this.molarFlowRate+"\n Volumetric flow rate: "+this.volFlowRate;
    }*/

    public String molarFlowRatesToString(){
        Specie[] species = this.getSpecies();
        String str = "Component molar flow rate: "+this.doubleArrayToString(species, this.getAllFlowRates());
        return str;
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
