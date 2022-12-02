package reactor;

public enum NominalPipeSizes {
    HALF_INCH(21.34,1.57), ONE_INCH(33.40,1.52), TWO_INCHES(60.32,2.31), THREE_INCHES(88.90,2.16), FOUR_INCHES(114.30,3.51), FIVE_INCHES(141.30,2.21);
    public final double outerDiameter;
    public final double wallThickness;

    private NominalPipeSizes(double outerDiameter, double wallThickness)
    {
        this.outerDiameter = outerDiameter/1000.; //to convert to m from mm.
        this.wallThickness = wallThickness/1000.; //to convert to m from mm.
    }

    public double returnInnerDiameter(){
        return outerDiameter - 2*wallThickness;
    }
}
