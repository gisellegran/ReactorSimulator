package reactor;

public class Catalyst {
    private double particleDiameter;
    private double voidFraction;
    private double particleDensity;

    //constructor
    //particle diameter should be in m
    public Catalyst(double particleDiameter, double voidFraction, double particleDensity)
    {
        this.particleDiameter = particleDiameter;
        this.voidFraction = voidFraction;
        this.particleDensity = particleDensity;
    }

    //copy constructor
    public Catalyst(Catalyst source)
    {
        if(source==null) {throw new IllegalArgumentException("source is null");}
        this.particleDiameter = source.particleDiameter;
        this.voidFraction = source.voidFraction;
        this.particleDensity = source.particleDensity;
    }

    //clone
    public Catalyst clone()
    {
        return new Catalyst(this);
    }

    //accessors
    public double getParticleDiameter()
    {
        return this.particleDiameter;
    }
    public double getVoidFraction()
    {
        return this.voidFraction;
    }
    public double getParticleDensity()
    {
        return this.particleDensity;
    }

    //mutators
    public void setParticleDiameter(double particleDiameter)
    {
        this.particleDiameter = particleDiameter;
    }
    public void setVoidFraction(double voidFraction)
    {
        this.voidFraction = voidFraction;
    }
    public void setParticleDensity(double particleDensity)
    {
        this.particleDensity = particleDensity;
    }

    public double returnBulkDensity(){
        return (1-this.voidFraction)*this.particleDensity;
    }
    //equals
    public boolean equals(Object comparator)
    {
        if(comparator == null) return false;
        else if(this.getClass() != comparator.getClass()) return false;

        boolean isEquals = true;
        if(this.particleDiameter != ((Catalyst) comparator).particleDiameter) isEquals = false;
        if(this.voidFraction != ((Catalyst) comparator).voidFraction) isEquals = false;
        if(this.particleDensity != ((Catalyst) comparator).particleDensity) isEquals = false;
        return isEquals;
    }

    private double calculateArea (NominalPipeSizes pipeSize)
    {
        double innerDiameter = pipeSize.outerDiameter - 2.* pipeSize.wallThickness;
        double area = Math.PI*Math.pow(innerDiameter, 2.)/4.;
        return area;
    }

    public double calculateErgunParameter(Stream s, NominalPipeSizes pipeSize)
    {   double density = s.returnDensity()/1000; //put into kg/m3
        double velocity = s.getVolFlowRate()/calculateArea(pipeSize);
        double massVelocity = velocity*density;
        double beta_0 = ((massVelocity*(1.-this.voidFraction))/(density*1.*this.particleDiameter*Math.pow(this.voidFraction,3.)))*
                ((150.*(1-this.voidFraction)*s.getViscosity())/this.particleDiameter + 1.75*massVelocity);
        double ergunParameter = (2.*beta_0)/(calculateArea(pipeSize)*1.*this.particleDensity*(1.-this.voidFraction));
        return ergunParameter;
    }
}
