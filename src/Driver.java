import chemistry.*;
import reactor.*;
import reactor.heat_transfer.HeatTransferCondition;
import reactor.heat_transfer.HeatTransferEquation;
import reactor.pressure_drop.Isobaric;
import reactor.pressure_drop.PressureDropEquation;

import java.util.Scanner;
import java.util.regex.MatchResult;

public class Driver {

    private static Specie specieBuilder(String elem, String phase){
        if (elem == null ) {
            //TODO: throw error
        }
        if (phase == null) {
            phase = "liquid"; // default
        }
        Element element = Element.findByName(elem.strip());
        Phase phase1 = Phase.findByName(phase);
        return new Specie(element.name, phase1, element.molarMass, element.heatCapacityCoefficients);
    }

    private static HeatTransferEquation heatTransferEquationBuilder(String condition, RefValue ref){
        if (condition == null ) {
            condition = "isothermal"; //default
        }

        return new HeatTransferEquation(HeatTransferCondition.findByName(condition.strip()), ref);
    }

    public static void main(String[] args) {

        //intialize scanner
        Scanner scan = new Scanner(System.in);

        //TODO: error handling for wrongly formatted input
            //extra: if the element is not in the available list, prompt to add molar weight and heat transfer coefficients in input file


        //TODO: error handling if element is not in list
        //create species array from the selected elements
        scan.useDelimiter(",|/|\n"); //changed delimiter to fit the specified input format

        //TODO: default value setting

        //TODO: phase is kinda weird maybe put phase in reactions or in reactor instead of species
        System.out.println("Will the reactor be operating at liquid or gas phase");
        String phaseIN = scan.next();//default is liquid

        //REACTIONS
        System.out.println("Input will now be taken for reactions");
        //this can be determined by the number of lines in the reactor section of input file maybe?
        System.out.println("How many reactions are present?");
        int numberofReactions = scan.nextInt();
        scan.nextLine();
        Reaction[] rxns = new Reaction[numberofReactions];

        for (int i = 0; i < numberofReactions; i++) {
            //SPECIES of reaction i
            Specie[] species;
            System.out.println("Enter the elements that will be present in the reaction seperated by a comma");
            String specieString = scan.nextLine();
            String[] speciesIN = specieString.split(",");
            System.out.print("Specie objects are now being created for each of the entered elements");
            species = new Specie[speciesIN.length];
            for (int j = 0; j < speciesIN.length; j++) {
                Element element = Element.findByName(speciesIN[i].strip());
                species[j] = specieBuilder(speciesIN[j], phaseIN);
            }

            double[] stoichIN = new double[speciesIN.length];
            double[] ordersIN = new double[speciesIN.length];

            //STOICIOMETRY & RATE ORDERS of reaction i
            System.out.println("Now enter each elements stoichiometric coefficient and reaction order."+
                    "\nIn the following format : coeff/order"+
                    "\nFor reactant: coefficient is negative, for product coefficient is positive ");
            for (int j = 0; j < speciesIN.length; j++) {
                System.out.print(speciesIN[i]+": ");
                scan.findInLine("(\\d+)/(\\d+)");
                MatchResult result = scan.match();
                for (int k=1; k<=result.groupCount(); k++){
                    Scanner lineScan = new Scanner(result.group(k));
                    lineScan.useDelimiter("/");
                    if (lineScan.hasNextInt()) stoichIN[k-1] = (double)lineScan.nextInt();//is this necessary
                    else if (lineScan.hasNextDouble()) stoichIN[k-1] = lineScan.nextDouble();

                    if (lineScan.hasNextInt()) ordersIN[k-1] = (double)lineScan.nextInt();
                    else if (lineScan.hasNextDouble()) ordersIN[k-1] = lineScan.nextDouble();
                }
            }

            StoichiometryMap stoich = new StoichiometryMap(species, stoichIN);
            SpecieMap orders = new SpecieMap(species, ordersIN);

            //rate laws
            System.out.println("What is the reference species to be used for the rate law? Choose from: "+specieString);
            String refS = scan.next();
            Specie refSpecie = specieBuilder(refS, phaseIN);

            RateConstant rateConstant;
            System.out.println("Is the rate constant temperature independent? Enter \"yes\" or \"no\"");
            String isRateConstantKnown = scan.next();
            if (isRateConstantKnown.equals("yes")) {
                System.out.println("Enter the rate constant: ");
                rateConstant = new RateConstant(scan.nextDouble(), 0.);
            } else {
                System.out.println("Enter the activation energy of the reaction in J/mol: .");
                double activationE = scan.nextDouble();
                System.out.println("Enter the pre-exponential factor of the reaction in mol/kg/s: .");
                double A = scan.nextDouble();
                RateConstant rateConstantCalculated = new RateConstant(A, activationE);
                rateConstant = new RateConstant(A, activationE);
            }


            RateLaw rate = new PowerRateLaw(rateConstant,refSpecie, orders);//TODO: default is just power rate law rn
            RefValue reactionEnthalpy = new RefValue(298., 0.); //TODO: reaction enthalpy input

            rxns[i] = new Reaction(rate, stoich, reactionEnthalpy);

        }

        ReactionSet reactionSet = new ReactionSet(rxns);
        TubularReactor reactor = null;
        System.out.println("Is the reactor a PFR or PBR?");
        String reactorType = scan.next();
        if (reactorType.equals("PFR"))
        {
            PressureDropEquation pDrop = new Isobaric();

            System.out.println("Is the reactor isothermal/adiabatic/cocurrent/countercurrent?");
            String heatTransfer = scan.next();
            RefValue refEnthalpy = new RefValue(298., 0.); //TODO: fix what enthlapy goes here, sum of everything?
            HeatTransferEquation heatX = heatTransferEquationBuilder(heatTransfer, refEnthalpy);

            System.out.println("Is the volume of the PFR known? Enter \"yes\" or \"no\" ");
            String isVolume = scan.next();
            if (isVolume.equals("yes")) {
                System.out.println("Enter the volume of the PFR in L: ");
                double volume = scan.nextDouble();
                reactor = new PFR(volume, pDrop, heatX);

            }
        } else{
            //TODO: implement PBR stuff
            //Catalyst catalyst = new Catalyst()
            //pDrop = new PressureDropEquation()
        }

        //INLET STREAM
        System.out.println("Is the inlet stream of the reactor known? Enter \"yes\" or \"no\" "); //TODO: whats in the case of no

        System.out.println("Is the inlet temperature of the reactor known? Enter \"yes\" or \"no\" ");
        String isTemp = scan.next();
        double T = 298.0;
        if (isTemp.equals("yes"))
        {
            System.out.println("Please enter the temperature in Kelvin: ");
            T = scan.nextDouble();
        }
        if (isTemp.equals("no"))
            System.out.println("Ok. Default temperature of 298 K will be used.");

        double P = 1.0;
        System.out.println("Is the inlet pressure of the reactor known? Enter \"yes\" or \"no\" ");
        String isPressure = scan.next();

        if (isPressure.equals("yes")) {
            System.out.println("Please enter the pressure in atm: ");
            P = scan.nextDouble();
        }
        if (isPressure.equals("no"))
            System.out.println("Ok. Default pressure of 1.0 atm will be used.");

        double inletVolFlowrate = 0.;
        System.out.println("Is the inlet volumetric flowrate to the reactor known? Enter \"yes\" or \"no\" ");
        String isVolFlow = scan.next();
        if (isVolFlow.equals("yes")) {
            System.out.println("Please enter the volumetric flowrate: ");
            inletVolFlowrate = scan.nextDouble();
        }
        double viscosity = 1.8E-5;
        System.out.println("Is the viscosity of the reaction solution known? Enter \"yes\" or \"no\" ");
        String isViscosity = scan.next();
        if (isViscosity.equals("yes")) {
            System.out.println("Please enter the viscosity in Pa s: ");
            viscosity = scan.nextDouble();
        }
        if (isViscosity.equals("no"))
            System.out.println("Ok. Default viscosity of 1.8*10^-5 Pa s will be used.");

        Specie[] allSpecies = reactionSet.returnSpecies();

        System.out.println("Enter corresponding number to make a selection:" +
                "\n1. Total molar flow rate and molar composition of stream is known" +
                "\n2. Species flow rates are known");

        Stream inletStream = null;
        switch(scan.nextInt()){
            case 1:
                System.out.print("Enter total flow rate: ");
                double totalFlow = scan.nextDouble();
                System.out.print("Now enter the molar fraction of each specie as requested");
                double[] composition = new double[allSpecies.length];
                for (int i = 0; i < allSpecies.length; i++) {
                    System.out.println(allSpecies[i].getName()+ ": ");
                    composition[i] = scan.nextDouble();
                }

                //TODO: add stream builder for the gerenal case of having just composition map or just the arrays directly + classifier (composition or molar fllow)
                inletStream = new Stream(new CompositionMap(allSpecies, composition), T, P, viscosity, inletVolFlowrate, totalFlow);
                break;

            case 2:
                System.out.print("Enter the molar fraction of each specie as requested");
                double[] flows = new double[allSpecies.length];
                for (int i = 0; i < allSpecies.length; i++) {
                    System.out.println(allSpecies[i].getName()+ ": ");
                    flows[i] = scan.nextDouble();
                }
                inletStream = StreamBuilder.buildStream(new MolarFlowMap(allSpecies, flows), T, P, viscosity, inletVolFlowrate);
                break;
        }


            //TODO: add this back in
            /*
            System.out.println("Is the reaction an elementary reaction? Enter True if elementary, False if not:  ");
            boolean isElementary = scan.nextBoolean();
            if (isElementary == true)
                for (int i = 0; i < n; i++)
                    orders[i] = stoichCoeff[i];

            */

        //INTEGRATION
        System.out.println("Choose the step size for Euler's method: ");
        double delV = scan.nextDouble();
        System.out.println("Maximum number of iterations will be 10,000");
        Stream outputStream = reactor.returnReactorOutput(inletStream, reactionSet, delV, 10000);
        System.out.println("Molar Flowrates of all the species: " + outputStream.getAllFlowRates());
        //TODO: need a method in stream that returns fractional conversion

        scan.close();

    }
}