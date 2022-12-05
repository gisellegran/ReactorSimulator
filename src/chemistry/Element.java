package chemistry;

public enum Element {
    //list of all the elements with their associated molar masses and heat capacity coefficients
    //molar masses are in g/mol.
    A("A", 28.05, new double[]{32.083,-1.4831, 24.774, -23.766, 68.274}),
    B("B", 60.05, new double[]{34.85,3.7626,28.311,-30.767, 92.646}),
    C("C",32.00, new double[]{29.526, -0.8900, 3.8083, -3.2629, 8.8607}),
    D("D",86.09, new double[]{27.664,23.366,6.2106,-16.972,57.917}),
    E("E",18.02, new double[]{33.933,-0.8419,2.9906,-1.7825,3.6934}),
    F("F",44.01, new double[]{27.437,4.2315,-1.9555,0.39968,-0.29872}),
    H("H",34.01, new double[]{25.206,8.04,-6,2,-2}),
    J("J",32.04, new double[]{15.04,10.39,-3,-0.7,3}),
    K("K",76.09, new double[]{1.0565,25.85,-20,5,-7}),
    L("L",62.07, new double[]{24.255,13.76,30,-50,200}),
    I("I",28.01, new double[]{29.342,-0.3540,1.0076,-0.4312,0.25935});
    public final String name;
    public final double molarMass;
    public final double[] heatCapacityCoefficients;

    //constructor for initializing all elements
    private Element(String name, double molarMass, double[] heatCapacityCoefficients){
        if (heatCapacityCoefficients == null){
            System.out.println("Null heatCapacityCoefficients");
            System.exit(0);
        }

        if (heatCapacityCoefficients.length != 5) {
            System.out.println("Number of heat capacity coefficients must be 5.");
            System.exit(0);
        }
        this.name = name;
        this.molarMass = molarMass/1000.;//convert to kg/mol
        this.heatCapacityCoefficients = new double[5];
        this.heatCapacityCoefficients[0] = heatCapacityCoefficients[0]*1E0;
        this.heatCapacityCoefficients[1] = heatCapacityCoefficients[1]*1E-2;
        this.heatCapacityCoefficients[2] = heatCapacityCoefficients[2]*1E-5;
        this.heatCapacityCoefficients[3] = heatCapacityCoefficients[3]*1E-8;
        this.heatCapacityCoefficients[4] = heatCapacityCoefficients[4]*1E-12;
    }

    //return the right Element based on the string
    public static Element findByName(String id) {
        Element result = null; //TODO: error handling?
        for (Element elem : values()) {
            if (elem.name().equalsIgnoreCase(id)) {
                result = elem;
                break;
            }
        }
        return result;
    }

}
