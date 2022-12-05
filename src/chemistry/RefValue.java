package chemistry;

public class RefValue {
    double refT;
    double value;

    //constructor
    public RefValue(double value, double refT)
    {
        this.refT = refT;
        this.value = value;
    }

    //copy constructor
    public RefValue(RefValue source)
    {
        if(source == null) System.exit(0);
        this.refT = source.refT;
        this.value = source.value;
    }

    //clone
    public RefValue clone()
    {
        return new RefValue(this);
    }

    //accessors
    public double getRefT()
    {
        return this.refT;
    }
    public double getValue()
    {
        return this.value;
    }

    //mutators
    public void setRefT(double refT)
    {
        this.refT = refT;
    }
    public void setValue(double value)
    {
        this.value = value;
    }

    //equals
    public boolean equals(Object comparator)
    {
        if(comparator == null) return false;
        else if(this.getClass() != comparator.getClass()) return false;

        boolean isEquals = true;
        if(this.refT != ((RefValue) comparator).refT) isEquals = false;
        if(this.value != ((RefValue) comparator).value) isEquals = false;
        return isEquals;
    }
}
