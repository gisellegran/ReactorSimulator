import chemistry.*;
import reactor.*;
import reactor.heat_transfer.HeatTransferCondition;
import reactor.heat_transfer.HeatTransferEquation;
import reactor.pressure_drop.Isobaric;

import java.util.Scanner;

public class Driver2 {
    public static void main(String[] args) {

        System.out.println("How many reactions are present?");
        Scanner scan20 = new Scanner(System.in);
        int numberofReactions = scan20.nextInt();
        Scanner scan = new Scanner(System.in);
        System.out.println("How many elements are present in the reaction (reactants and products)? ");
        int n = scan.nextInt();
        System.out.println("Enter the elements present in the reaction from the available elements (A,B,C,D,E,F,H,J,K,L,I). Separate each element by a comma: ");
        String allElements = scan.next();
        Scanner scan1 = new Scanner(allElements);
        scan1.useDelimiter(",");
        System.out.println("Is the reaction taking place in the liquid or gas phase? ");
        Scanner scan2 = new Scanner(System.in);
        String phaseName = scan2.next();
        Phase phase = Phase.LIQUID; //default is liquid
        Isobaric pDrop = new Isobaric();
        if (phaseName.equals("gas")) {
            phase = Phase.IDEALGAS;
            //Catalyst catalyst = new Catalyst()
            //pDrop = new PressureDropEquation()
        }
        String[] element = new String[n];
        Specie[] species = new Specie[n];
        double[] heatCapacityCoeffs = new double[5];
        double molarMass = Element.A.molarMass; //default molar mass is that of A
        for (int i = 0; i < n; i++) {
            element[i] = scan1.next();
            if (element[i].equals("A"))
                heatCapacityCoeffs = Element.A.heatCapacityCoefficients;
            if (element[i].equals("B")) {
                heatCapacityCoeffs = Element.B.heatCapacityCoefficients;
                molarMass = Element.B.molarMass;
            }
            if (element[i].equals("C")) {
                heatCapacityCoeffs = Element.C.heatCapacityCoefficients;
                molarMass = Element.C.molarMass;
            }
            //continue for rest. TODO: is there a more efficient way to do this?
            species[i] = new Specie(element[i], phase, molarMass, heatCapacityCoeffs);
        }
        //the above basically created an array of the species

        double T = 298.0;
        System.out.println("Is the inlet temperature of the reactor known? Enter \"yes\" or \"no\" ");
        Scanner scan5 = new Scanner(System.in);
        String isTemp = scan5.next();
        if (isTemp.equals("yes"))
        {
            System.out.println("Please enter the temperature in Kelvin: ");
            T = scan5.nextDouble();
        }
        if (isTemp.equals("no"))
            System.out.println("Ok. Default temperature of 298 K will be used.");
        double P = 1.0;
        System.out.println("Is the inlet pressure of the reactor known? Enter \"yes\" or \"no\" ");
        Scanner scan6 = new Scanner(System.in);
        String isPressure = scan6.next();
        if (isPressure.equals("yes")) {
            System.out.println("Please enter the pressure in atm: ");
            P = scan6.nextDouble();
        }
        if (isPressure.equals("no"))
            System.out.println("Ok. Default pressure of 1.0 atm will be used.");

        double inletVolFlowrate = 0.;
        System.out.println("Is the inlet volumetric flowrate to the reactor known? Enter \"yes\" or \"no\" ");
        Scanner scan7 = new Scanner(System.in);
        String isVolFlow = scan7.next();
        if (isVolFlow.equals("yes")) {
            System.out.println("Please enter the volumetric flowrate: ");
            inletVolFlowrate = scan7.nextDouble();
        }
        double viscosity = 1.8E-5;
        System.out.println("Is the viscosity of the reaction solution known? Enter \"yes\" or \"no\" ");
        Scanner scan8 = new Scanner(System.in);
        String isViscosity = scan8.next();
        if (isViscosity.equals("yes")) {
            System.out.println("Please enter the viscosity in Pa s: ");
            viscosity = scan8.nextDouble();
        }
        if (isViscosity.equals("no"))
            System.out.println("Ok. Default viscosity of 1.8*10^-5 Pa s will be used.");

        //ask for input molar flowrates if available
        System.out.println("Are the inlet molar flowrates of the species known? Enter True if known and False if unknown. ");
        boolean isInletFlowrates = scan.nextBoolean();
        double totalInletFlowrate = 0.;
        double[] moleFractions = new double[n];
        double[] orders = new double[0];
        if (isInletFlowrates == true) {
            double[] inletFlowRates = new double[n];
            System.out.println("Enter the inlet flowrates of the species in the following order: " + allElements + " separated by a comma.");
            Scanner scan3 = new Scanner(System.in);
            String flowrates = scan3.next();
            Scanner scan4 = new Scanner(flowrates);
            scan4.useDelimiter(",");
            for (int i = 0; i < n; i++)
                inletFlowRates[i] = scan4.nextDouble();
            MolarFlowMap molarFlowMap = new MolarFlowMap(species, inletFlowRates);
            Stream inletStream = StreamBuilder.buildStream(molarFlowMap, T, P, viscosity, inletVolFlowrate);

            //rate laws
            orders = new double[n];
            System.out.println("What is the reference species to be used for the rate law? Choose from A,B,C,D,E,F,H,J,K,L,I: ");
            Scanner scan13 = new Scanner(System.in);
            String referenceSpecies = scan13.nextLine();
            double[] refHeatCapacityCoeffs = new double[5];
            double refMolarMass = Element.A.molarMass;
            Specie refSpecies;
            if (referenceSpecies.equals("A"))
                refHeatCapacityCoeffs = Element.A.heatCapacityCoefficients;
            if (referenceSpecies.equals("B")) {
                refHeatCapacityCoeffs = Element.B.heatCapacityCoefficients;
                refMolarMass = Element.B.molarMass;
            }
            if (referenceSpecies.equals("C")) {
                refHeatCapacityCoeffs = Element.C.heatCapacityCoefficients;
                refMolarMass = Element.C.molarMass;
            }
            //continue for rest. TODO: is there a more efficient way to do this?

            refSpecies = new Specie(scan13.next(), phase, refMolarMass, refHeatCapacityCoeffs);
            RateConstant rateConstant;
            System.out.println("Is the rate constant of the reaction known? Enter \"yes\" or \"no\"");
            Scanner scan9 = new Scanner(System.in);
            String isRateConstantKnown = scan9.next();
            if (isRateConstantKnown.equals("yes")) {
                System.out.println("Enter the rate constant: ");
                Scanner scan10 = new Scanner(System.in);
                rateConstant = new RateConstant(scan10.nextDouble(), 0.);
            } else {
                System.out.println("Enter the activation energy of the reaction in J/mol: .");
                Scanner scan11 = new Scanner(System.in);
                double activationE = scan11.nextDouble();
                System.out.println("Enter the pre-exponential factor of the reaction in mol/kg/s: .");
                Scanner scan12 = new Scanner(System.in);
                double A = scan12.nextDouble();
                RateConstant rateConstantCalculated = new RateConstant(A, activationE);
                rateConstant = new RateConstant(A, activationE);
            }

            System.out.println("Enter the stoichiometric coefficients for the reactants and products separated by a comma (negative for reactants, positive for products): ");
            Scanner scan14 = new Scanner(System.in);
            String coefficients = scan14.next();
            Scanner scan15 = new Scanner(coefficients);
            scan15.useDelimiter(",");
            double[] stoichCoeff = new double[n];
            for (int i = 0; i < n; i++)
                stoichCoeff[i] = scan15.nextDouble();

            System.out.println("Is the reaction an elementary reaction? Enter True if elementary, False if not:  ");
            boolean isElementary = scan.nextBoolean();
            if (isElementary == true)
                for (int i = 0; i < n; i++)
                    orders[i] = stoichCoeff[i];

            SpecieMap orderMap = new SpecieMap(species, orders);
            RateLaw refReactionRate = new PowerRateLaw(rateConstant, refSpecies, orderMap);
            StoichiometryMap stoichiometryMap = new StoichiometryMap(species, stoichCoeff);

            Scanner scan17 = new Scanner(System.in);
            System.out.println("Is the reactor isothermal/adiabatic/cocurrent/countercurrent?");
            String heatTransfer = scan17.next();
            HeatTransferCondition condition = HeatTransferCondition.ISOTHERMAL;
            RefValue refEnthalpy = new RefValue(298.,0.);
            if (heatTransfer.equals("adiabatic"))
            {
                condition = HeatTransferCondition.ADIABATIC;
                //refEnthalpy = new RefValue(298.,) //TODO: implement for non-isothermal
            }
            //TODO: implement for the rest. Is there a more efficient way?
            Reaction[] rxns = new Reaction[numberofReactions];
            for (int i = 0; i < numberofReactions; i++) {
                Reaction rxn = new Reaction(refReactionRate, stoichiometryMap,refEnthalpy);
                rxns[i] = rxn.clone();
            }
            ReactionSet reactionSet = new ReactionSet(rxns);
            HeatTransferEquation heatTransferEquation = new HeatTransferEquation(condition, refEnthalpy);
            TubularReactor reactor;
            Scanner scan16 = new Scanner(System.in);
            System.out.println("Is the reactor a PFR or PBR?");
            String reactorType = scan16.next();
            if (reactorType.equals("PFR"))
            {
                System.out.println("Is the volume of the PFR known? Enter \"yes\" or \"no\" ");
                Scanner scan18 = new Scanner(System.in);
                String isVolume = scan18.next();
                if (isVolume.equals("yes")) {
                    System.out.println("Enter the volume of the PFR in L: ");
                    Scanner scan19 = new Scanner(System.in);
                    double volume = scan19.nextDouble();
                    reactor = new PFR(volume, pDrop, heatTransferEquation);
                    System.out.println("Choose the step size for Euler's method: ");
                    Scanner scan21 = new Scanner(System.in);
                    double delV = scan21.nextDouble();
                    System.out.println("Maximum number of iterations will be 10,000");
                    Stream outputStream = reactor.returnReactorOutputAtPoint(volume,inletStream, reactionSet, delV, 10000);
                    System.out.println("Molar Flowrates of all the species: " + outputStream.getAllFlowRates());
                    //TODO: need a method in stream that returns fractional conversion
                }
            }
        }

    }
}