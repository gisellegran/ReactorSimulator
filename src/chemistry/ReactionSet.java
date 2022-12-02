package chemistry;
import java.util.HashSet;
import java.util.Map;

public class ReactionSet {
    private Reaction[] reactions;

    //constructor
    public ReactionSet(Reaction[] reactions) {
        if(reactions==null) System.exit(0);
        for(int i=0;i<reactions.length;i++)
            if(reactions[i]==null) System.exit(0);
        this.reactions = new Reaction[reactions.length];
        for(int j=0;j<reactions.length;j++)
            this.reactions[j] = reactions[j].clone();
    }

    public ReactionSet(Reaction reaction) {
        if(reaction==null) System.exit(0);
        this.reactions = new Reaction[]{reaction.clone()};
    }


    //copy constructor
    public ReactionSet (ReactionSet source) {
        if(source==null) System.exit(0);
        this.reactions = new Reaction[source.reactions.length];
        for(int i=0;i<source.reactions.length;i++)
            this.reactions[i] = source.reactions[i].clone();
    }

    //accessor
    public Reaction[] getReactions() {
        Reaction[] temp = new Reaction[this.reactions.length];
        for(int i=0;i<this.reactions.length;i++)
            temp[i] = this.reactions[i].clone();
        return temp;
    }

    //mutator
    public boolean setReactions(Reaction[] reactions) {
        if(reactions == null) return false;
        for(int i=0;i<reactions.length;i++)
            if(reactions[i]==null) return false;
        this.reactions = new Reaction[reactions.length];
        for(int j=0;j<reactions.length;j++)
            this.reactions[j] = reactions[j].clone();
        return true;
    }

    public boolean hasSpecie(Specie s){
        //TODO: error handling
        if (s == null) return false;
        for (int i = 0; i < this.reactions.length; i++) {
            if (this.reactions[i].hasSpecie(s)) return true;
        }

        return false;
    }

    public int returnNumberOfSpecies() {
        //TODO: not ideal should change (along with returnSpecies
        return this.returnSpecies().length;

    }

    public Specie[] returnSpecies() {
        //TODO: this is definetly not ideal but this is what it is for now
        HashSet<Specie> specieSet = new HashSet<Specie>();

        //TODO: use something other than set if its not allowed

        for (int i = 0; i < this.reactions.length; i++) {
            Specie[] species = this.reactions[i].returnAllSpecies();
            for(int j = 0; j < species.length; j++){
                specieSet.add(species[j].clone());
            }
        }

        return specieSet.toArray(new Specie[specieSet.size()]);

    }

    public SpecieMap returnNetRxnRates(double T, MultiComponentMixture mix) {
        SpecieMap netReactionRates = new SpecieMap();

        //iterate through the reactions
        for (int i = 0; i < this.reactions.length; i++) {
            SpecieMap rxnRates = this.reactions[i].calcAllReactionRates(T, mix);

            //iterate through the elements of the reaction
            for (Map.Entry<Specie, Double> elem : rxnRates.entrySet()) {
                if (netReactionRates.hasSpecie(elem.getKey())) {
                    netReactionRates.replace(elem.getKey().clone(), netReactionRates.get(elem.getKey())+elem.getValue());
                } else {
                    netReactionRates.put(elem.getKey().clone(), elem.getValue());
                }
            }
        }
         return netReactionRates;

    }

    //netDeltaH = sum(rij*DeltaH_ij)
    public double returnNetDeltaH(double T, MultiComponentMixture mix){
        double deltaH = 0.;

        for (int i = 0; i < this.reactions.length; i++) {
            deltaH += this.reactions[i].returnReactionEnthalpy(T)*this.reactions[i].calcRefReactionRate(T, mix);
        }

        return deltaH;
    }

    //clone
    public ReactionSet clone() {
        return new ReactionSet(this);
    }

    //equals
    public boolean equals(Object comparator)
    {
        if( comparator == null ) return false;
        else if(this.getClass() != comparator.getClass()) return false;
        else if(this.reactions.length != ((ReactionSet) comparator).reactions.length) return false;

        boolean isEquals = true;
        for(int i=0;i<this.reactions.length;i++)
            if( this.reactions[i].equals(((ReactionSet) comparator).reactions[i]) == false ) isEquals = false;
        return isEquals;
    }

}
