package chemistry;
import java.util.HashSet;

public class ReactionSet {
    private Reaction[] reactions;

    //constructor
    public ReactionSet(Reaction[] reactions) {
        //todo: error if all the reactions dont have the same number of species
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

    public Phase getPhase(){
        if (this.isSinglePhase()) return reactions[0].getPhase();
        return Phase.L_G;
    }

    public boolean isSinglePhase(){
        Phase phase = reactions[0].getPhase();
        for (int i = 1; i < this.reactions.length; i++) {
            if (reactions[i].getPhase() != phase) return false;
        }
        return true;
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
            Specie[] species = this.reactions[i].getSpecies();
            for(int j = 0; j < species.length; j++){
                specieSet.add(species[j].clone());
            }
        }

        return specieSet.toArray(new Specie[specieSet.size()]);

    }

    public double[] returnNetRxnRates(MultiComponentMixture mix) {
        int n = this.returnNumberOfSpecies();//number of species in all the reactions
        double[] netReactionRates = new double[n];

        double T = mix.getT();

        //iterate through the reactions
        for (int i = 0; i < this.reactions.length; i++) {
            double[] rxnRates = this.reactions[i].calcAllReactionRates(mix);

            //iterate through the elements of the reaction
            for (int j = 0; j < n; j++) {
                netReactionRates[j] += rxnRates[j];
            }

        }
         return netReactionRates;

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
