package chemistry;

public class GasDensityCalculator {
    private static double R = 8.3145; //TODO: find units this is for

    public static double returnValue(MultiComponentMixture mix){

        double density = -1.0;

        if (mix.returnPhase() != Phase.IDEALGAS) {
            // TODO: throw error?
        }

        if (mix.returnPhase() == Phase.IDEALGAS) {
            double P, M, T;
            P = mix.getP();
            T = mix.getT();
            M = mix.returnMixtureMolarMass();

            density = P*M/(R*T);
        }


        return density;
    };

}
