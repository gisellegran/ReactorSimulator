package chemistry;

//TODO: maybe make elementary rate law class
public class PowerRateLaw extends RateLaw {
    private static final double R = 8.2057E-5; //m3 atm/mol K) reaction partial pressures should always be in atm
    private double[] orders;

    //constructor
    public PowerRateLaw(RateConstant k, Specie refSpecie, double[] orders)
    {
        super(k, refSpecie);
        //TODO: error handling
        if(orders == null) throw new IllegalArgumentException("orders array is null");
        this.orders = new double[orders.length];
        for (int i = 0; i < orders.length; i++) {
            this.orders[i] = orders[i];
        }
    }

    //copy constructor
    public PowerRateLaw(PowerRateLaw source)
    {
        super(source);
        this.orders = new double[source.orders.length];
        for (int i = 0; i < source.orders.length; i++) {
            this.orders[i] = source.orders[i];
        }
    }

    //accessor
    public double[] getOrders() {
        double[] temp = new double[this.orders.length];
        for (int i = 0; i < this.orders.length; i++) {
            temp[i] = this.orders[i];
        }
        return temp;
    }

    //mutator
    public boolean setOrders(double[] orders) {
        if(orders==null) return false;
        if(orders.length != this.orders.length) return false;
        for (int i = 0; i < orders.length; i++) {
            this.orders[i] = orders[i];
        }
        return true;
    }

    //calculate rate of reaction, returns rate without accounting if it is a consumption or formation rate
    public double returnRate(MultiComponentMixture mix) {

        if (this.orders.length != mix.returnNumberOfSpecies()) throw new IllegalArgumentException("mismatch between number of species and number of orders");
        double[] concentrations;
        double T = mix.getT();
        double rate = super.getK().returnRateConstant(T);

        // we use the partial pressure instead of mo concentration for gas
        if (mix.returnPhase() == Phase.IDEALGAS) {
            double[] conc = mix.returnAllMolConcentrations();
            concentrations = new double[conc.length];

            double mixT = mix.getT();

            for (int i = 0; i < conc.length; i++) {
                concentrations[i] = conc[i] * this.R * mixT;
            }
        } else concentrations = mix.returnAllMolConcentrations();

        for (int i = 0; i < this.orders.length; i++) {
            rate *= Math.pow(concentrations[i],this.orders[i]);
        }

        return rate;

    }

    //clone
    public PowerRateLaw clone()
    {
        return new PowerRateLaw(this);
    }

    //equals
    public boolean equals(Object obj) {
        if (!super.equals(obj)) return false;
        PowerRateLaw objPRL = (PowerRateLaw)obj;

        for (int i = 0; i < this.orders.length; i++) {
            if (this.orders[i] != objPRL.orders[i]) return false;
        }

        return true;
    }
}