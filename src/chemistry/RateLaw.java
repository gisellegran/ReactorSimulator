package chemistry;

//TODO: could add case where k is cst
public abstract class RateLaw {
    private RateConstant k;
    private Specie refSpecie;//todo: maybe move this to reaction

    //constructor
    public RateLaw (RateConstant k, Specie refSpecies)
    {
        if(k == null) System.exit(0);
        if(refSpecies == null) System.exit(0);
        this.k = k.clone();
        this.refSpecie = refSpecies;
    }

    //copy constructor
    public RateLaw(RateLaw source)
    {
        if (source == null) {System.exit(0);}
        this.k = source.k.clone();
        this.refSpecie = source.refSpecie;
    }

    //accessors
    public RateConstant getK()
    {
        return this.k.clone();
    }


    public Specie getRefSpecie()
    {
        return this.refSpecie.clone();
    }

    //mutators
    public boolean setK(RateConstant k)
    {
        if (k == null) return false;
        this.k = k.clone();
        return true;
    }

    public boolean setRefSpecie(Specie refSpecie)
    {
        if(refSpecie == null) return false;
        this.refSpecie = refSpecie.clone();
        return true;
    }

    public abstract double returnRate(double T, double[] concentrations);

    //clone
    public abstract RateLaw clone();

    //equals
    public boolean equals(Object comparator)
    {
        if(comparator == null) return false;
        else if(this.getClass() != comparator.getClass()) return false;

        boolean isEquals = true;
        if(this.k.equals(((RateLaw) comparator).k) == false) isEquals = false;
        if(this.refSpecie.equals(((RateLaw) comparator).refSpecie) == false) isEquals = false;
        return isEquals;
    }

}