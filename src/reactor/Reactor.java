package reactor;
import chemistry.MultiComponentMixture;
import chemistry.ReactionSet;
import numericalmethods.SetOfODEs;
import reactor.heat_transfer.HeatTransferEquation;
import reactor.pressure_drop.PressureDropEquation;

public abstract class Reactor implements SetOfODEs {
    //instance variables
    private double size;//V for PFR, W for PBR
    private PressureDropEquation pDrop;
    private HeatTransferEquation heatX;

    //global variables



    //main constructor
    public Reactor(double size ,PressureDropEquation pDrop, HeatTransferEquation heatX) {
        //check for null
        if (pDrop == null || heatX == null) {
            System.exit(0); //TODO: error handling
        }

        this.size = size;
        this.pDrop = pDrop.clone();
        this.heatX = heatX.clone();
    }

    //copy constructor
    public Reactor(Reactor source) {
        this.size = source.size;
        this.pDrop = pDrop.clone();
        this.heatX = heatX.clone();
    }

    //global variables handler
    //TODO: i removed the global variables from the reactor class and put them in flow reactor but im not sure if that was the right move or not
    /*
    protected void setGlobalVariables(ReactionSet rxns, MultiComponentMixture mix){
        this.reactions = rxns.clone();
        MultiComponentMixture temp = mix.clone();
        temp.addAllSpecies(rxns.returnSpecies());
        this.speciesInReactor = temp.getSpecies();
    }

    protected void resetGlobalVariables(){
        this.reactions = null;
        this.speciesInReactor = null;
    }*/

    //accessors & mutators

    public double getSize() {
        return this.size;
    }

    public boolean setSize(double size) {
        if (size < 0) return false;

        this.size = size;
        return true;
    }

    public PressureDropEquation getpDrop() {
        return pDrop.clone();
    }

    public boolean setpDrop(PressureDropEquation pDrop) {
        if (pDrop == null) return false;

        this.pDrop = pDrop.clone();

        return true;
    }

    public HeatTransferEquation getHeatX() {
        return heatX.clone();
    }

    public boolean setHeatX(HeatTransferEquation heatX) {
        if (heatX == null) return false;

        this.heatX = heatX.clone();

        return true;
    }

    //class methods

    //TODO: maybe delete this
    public abstract MultiComponentMixture returnReactorOutput(MultiComponentMixture input, ReactionSet rxn, double delX, int maxIt);


    public abstract double[] calculateValue(double x, double[] y0);

    //clone
    public abstract Reactor clone();

    //equals

    public boolean equals(Object obj) {
        //check for null

        if (obj == null)  return false;

        //check that classes are equal
        if (this.getClass() != obj.getClass()) return false;

        //convert obj to reactor
        Reactor objReactor = (Reactor)obj;

        //compare all instance variables
        if (this.size != objReactor.size) return false;

        return (this.pDrop).equals(objReactor.pDrop) && (this.heatX).equals(objReactor.heatX);


    }
}
