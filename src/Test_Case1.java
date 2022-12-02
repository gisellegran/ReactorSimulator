import chemistry.*;
import reactor.*;
import reactor.heat_transfer.HeatTransferCondition;
import reactor.heat_transfer.HeatTransferEquation;
import reactor.pressure_drop.Isobaric;
import reactor.pressure_drop.PressureDropEquation;

import java.text.DecimalFormat;

public class Test_Case1 {/*
    public static void main(String[] args) {

        System.out.println("Creating species objects for A, B, & C..");
        Phase phase = Phase.LIQUID;

        Specie[] species = new Specie[3];
        species[0] = new Specie("A",phase,Element.A.molarMass,Element.A.heatCapacityCoefficients);
        species[1] = new Specie("B",phase,Element.B.molarMass, Element.B.heatCapacityCoefficients);
        species[2] = new Specie("C",phase,Element.C.molarMass,Element.C.heatCapacityCoefficients);
        System.out.println("Species Creation Completed");

        System.out.println("Now the components of the reaction to build the reaction will be created");
        System.out.println("First, we create the stoichiometry map defining the reaction. THe coefficients are as follows: {A:-1, B:-2, C:1}");
        double[] stoichCoeff = new double[] {-1,-2,1};
        StoichiometryMap stoichiometryMap = new StoichiometryMap(species, stoichCoeff);
        System.out.println("Stoichiometry map creation completed");

        System.out.println("Next, we create the rate constant with A = 100 and activation energy = 0. THe reference specie of the rate is specie A.");
        Specie refSpecies = species[0];
        RateConstant rateConstant = new RateConstant(100, 0.);
        System.out.println("Rate Constant Creation Completed");

        System.out.println("Now we create the rate law which is in the power form. Since the reaciton is elementary, the orders are as follows: {A:1, B:2, C:0}");
        double[] orders = new double[] {1,2,0};
        SpecieMap orderMap = new SpecieMap(species, orders);
        RateLaw refReactionRate = new PowerRateLaw(rateConstant, refSpecies, orderMap);
        System.out.println("Rate Law Creation Completed");

        System.out.println("The final component for the creation of the reaction is the reference enthalpy of the reaction" +
                "\nThis reaction is assumed to have no reaction heat and thus the reference enthalpy is set to 0");
        RefValue refEnthalpy = new RefValue(298., 0.);
        System.out.println("Reference Enthalpy Creation Completed");

        System.out.println("Finally, the rate law, stoichiometry map and reaction enthalpy are combined to build a reaction object." );
        Reaction rxn = new Reaction(refReactionRate, stoichiometryMap, refEnthalpy);
        System.out.println("Reaction Creation Completed");

        System.out.println("Now the reactor object will be built. " +
                "\nThe reactor is isothermal and isobaric so the corresponding heat transfer and pressure drop equation objects are created.");
        HeatTransferCondition condition = HeatTransferCondition.ISOTHERMAL;
        HeatTransferEquation heatTransferEquation = new HeatTransferEquation(condition, refEnthalpy);
        PressureDropEquation pDrop = new Isobaric();
        System.out.println("Pressure Drop and Heat Transfer Equation Creation Completed");
        System.out.println("The reactor has a volume of 600L. Combining that with the pressure drop and heat transfer equation we can build the reactor object.");
        PFR pfr = new PFR(600,pDrop,heatTransferEquation);
        System.out.println("Reactor Creation Completed");


        System.out.println("\nTo run a performance problem on a flow reactor a reaction set and an input stream are required." +
                "\n the reaction set holds all the reactions which will be occuring in the reactor. This object is responsible for calculating the net reaction rates" +
                "\n the reaction is loaded into a reaciton set object");
        Reaction[] rxns = new Reaction[1];
        rxns[0] = rxn;
        ReactionSet reactionSet = new ReactionSet(rxns);
        System.out.println("Reaction Set Creation Completed");

        System.out.println("\nThe input stream holds all the input conditions required for solving for the reactor  (Ex: temperature pressure, viscocity, molar flow rate..)");
        System.out.println("The input stream for the reactor is created with following conditions: " +
                "\nvol flow rate : 120L/min, Flow rates: {A:15, B: 15, C:0}, temperature : 298K, Pressure : 1atm (default), Viscocity 1.85E-5");
        double T = 298.0;
        double P = 1.0;
        double inletVolFlowrate = 120;
        double viscosity = 1.8E-5;
        double[] inletFlowRates = new double[3];
        inletFlowRates[0] = 15;
        inletFlowRates[1] = 15;
        inletFlowRates[2] = 0;

        //TODO: change to array
        //MolarFlowMap molarFlowMap = new MolarFlowMap(species, inletFlowRates);
        Stream inletStream = StreamBuilder.buildStream(molarFlowMap, T, P, viscosity, inletVolFlowrate);
        System.out.println("Inlet Stream Creation Completed");

        System.out.println("Now the PFR can solve the performance problem by calling the returnReactorOutput() method. We pass the inlet stream, reaction set " +
                "\nalong with step size (0.01) and maximum number of iterations.");
        Stream outputStream = pfr.returnReactorOutput(inletStream, reactionSet, 0.01, 1000000);
        System.out.println("We obtain the reactor outlet stream. The results of the test are as follows: ");
        DecimalFormat df = new DecimalFormat("0.00");
        System.out.println("Expected output concentration for the PFR (testFlowReactor()): { A: 8.209693911, B: 1.419387822, C: 6.790306089}");
        System.out.println("Expected conversion of A: 0.452687073");
        System.out.println("Expected output flow rates for the PFR (testFlowReactor()): {A:0.068414116, B: 0.011828232, C: 0.056585884 }");
        System.out.println("Calculated Molar Flowrates of all the species: " +outputStream.getAllFlowRates());
        System.out.println("Calculated Fractional conversion of A: " +df.format(PFR.returnConversion(species[0],inletStream,outputStream)));
        System.out.println("Calculated output concentration for the PFR"+outputStream.returnAllMolConcentrations());
        System.out.println("As a percentage, the % conversion of A is: "+df.format(PFR.returnConversion(species[0],inletStream,outputStream)*100)+"%");
    }
*/}
