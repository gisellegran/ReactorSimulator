import chemistry.*;
import reactor.*;
import reactor.heat_transfer.HeatTransferCondition;
import reactor.heat_transfer.HeatTransferEquation;
import reactor.heat_transfer.Isothermal;
import reactor.pressure_drop.Isobaric;
import reactor.pressure_drop.PressureDropEquation;

public class Test_Case2 {
    public static void main(String[] args) {
        //first create the reaction species
        Specie specieA = new Specie("A", Phase.LIQUID, Element.A.molarMass, Element.A.heatCapacityCoefficients);
        Specie specieB = new Specie("B", Phase.LIQUID, Element.B.molarMass, Element.B.heatCapacityCoefficients);
        Specie specieC = new Specie("C", Phase.LIQUID, Element.C.molarMass, Element.C.heatCapacityCoefficients);

        Specie[] species = {specieA, specieB, specieC};
        double[] stoichCoeffs = {-1.0, -2.0, 1.0};

        RefValue refEnthalpy = new RefValue(0., 0.);

        double a = 100.0;
        double Ea = 0.;
        RateConstant kA = new RateConstant(a, Ea);

        double[] orders = {1., 2., 0.};
        RateLaw rate = new PowerRateLaw(kA, specieA, orders);

        Reaction rxn = new Reaction(rate, species, stoichCoeffs, refEnthalpy);

        PressureDropEquation pDrop = new Isobaric();
        NominalPipeSizes pipeSize = NominalPipeSizes.ONE_INCH; //default
        HeatTransferCondition condition = HeatTransferCondition.ISOTHERMAL;
        HeatTransferEquation heatX = new Isothermal(0, 0, pipeSize);

        ReactionSet rxns = new ReactionSet(rxn);

        double T = 298.;
        double P = 1.;
        double viscocity = 1.;
        double volFlowRate = 120.;
        double[] initialFlowRates = {15., 15., 0}; //mol/min

        Stream stream = StreamBuilder.buildStreamFromMolFlows(species, initialFlowRates, T, P, viscocity, volFlowRate);
        System.out.println("All initial steps are the same as for case 1. See Test_Case 1 for a detailed output of all the steps" +
                "\nTo determine a reactor volume from a given input stream and a desired output flow rate, the reactor designer class is used" +
                "\nIn this test, values from case 1 will be used. The determined output flow rate of A is used as the target and we are testing to see how " +
                "\nthe calculated volume compares to the actual known volue of the reactor in case 1");
        System.out.println("\nWe pass to the PFRDesigner.returnVForTargetFlow() method : species A as our species of interest, the target flow rate of 8.209693911, the known inlet stream, " +
                "\nthe reaction set, the pressure drop and heat trasnfer equations along with the step size and maximum number of iterations ");

        PFRDesigner designer = new PFRDesigner();
        PFR output = designer.returnVForTargetFlow(specieA, 8.209693911, stream, rxns, pDrop, heatX, pipeSize, 2, 50000);

        System.out.println("Rector size found:" + output.getSize());
        System.out.println("Actual reactor size: 600L");
        //System.out.println(TubularReactor.returnConversion(specieA, stream, output));
    }
}
