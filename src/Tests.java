import chemistry.*;
import reactor.*;
import reactor.pressure_drop.*;
import reactor.heat_transfer.*;

public class Tests {/*

    //method to print a double result and the expected double value to see
    public static void testPrint(double value, double expectedVal){
        System.out.println("Expected Value: "+expectedVal);
        System.out.println("result: "+value+" Pass: "+(value == expectedVal)+"\n");
    }

    //method to print an object and the expected double value to see
    public static void testPrint(Object value, double expectedVal){
        System.out.println("Expected Value: "+expectedVal);
        System.out.println("result: "+value+"\n");
    }
    //method to print an object and then the expected value to see
    public static void testPrint(boolean value, boolean expectedVal){
        System.out.println("Expected Value: "+expectedVal);
        System.out.println("result: "+ (value==expectedVal)+"\n");
    }

    //series of tests on liquid species
    public static void testPowerRateLawLiquid(){
        //for 1st iteration of test case 1
        // A + 2B --> C

        //first create the reaction species
        Specie specieA = new Specie("A", Phase.LIQUID, Element.A.molarMass, Element.A.heatCapacityCoefficients);
        Specie specieB = new Specie("B", Phase.LIQUID, Element.B.molarMass, Element.B.heatCapacityCoefficients);
        Specie specieC = new Specie("C", Phase.LIQUID, Element.C.molarMass, Element.C.heatCapacityCoefficients);


        //create rateConstant object
        //kATest = 100.0; dm6/mol2/min
        RateConstant kATest = new RateConstant(100);

        //then we can create the OrdersMap
        //the reaction is elementary so the orders are equivalent to the stoich coeff of the reactants
        //species array for map
        Specie[] species = {specieA,specieB};
        int coefStochATest = 1;
        int coefStochBTest = 2;
        double [] ordersTest = {coefStochATest,coefStochBTest};
        SpecieMap orders = new SpecieMap(species, ordersTest);

        //then we can create the rate law object
        PowerRateLaw testRate = new PowerRateLaw(kATest,specieA,orders);

        //TODO: maybe find a way to make variables have default
        //for operating conditions we can default to room temperature and pressure
        double T = 298;
        double P = 1;
        double viscocity = 1;
        double volFlowRate = 120; //dm3/min

        //generate stream from molar flow rates
        //species in inlet are only A & B
        double[] initialFlowRates = {15, 15}; //mol/min

        MolarFlowMap flowMap = new MolarFlowMap(species, initialFlowRates);

        System.out.println("MolarFlowMap Test: totalMolarFlow()");
        double expectedVal1 = 30;
        testPrint(flowMap.returnTotalMolarFlow(), expectedVal1);

        CompositionMap composition = new CompositionMap(flowMap);
        System.out.println("CompositionMap Test 1: CompositionMap(MolarFlowMap)");
        double expectedVal2 = 0.5;
        testPrint(composition, expectedVal2);

        System.out.println("CompositionMap Test 2: containsKey" );
        testPrint(composition.containsKey(specieA), true);

        Stream stream = StreamBuilder.buildStream(flowMap, T, P, viscocity, volFlowRate);


        System.out.println("Stream Test 2: returnSpecieFlowRate()");
        double expectedVal3 = 15;
        testPrint(stream.returnSpecieFlowRate(specieA), expectedVal3);

        System.out.println("Stream Test 3: returnAllMolConcentrations");
        double expectedVal4 = 0.125;
        testPrint(stream.returnAllMolConcentrations(), expectedVal4);

        System.out.println("Rate Constant Test 1: returnRateConstant()");
        testPrint(kATest.returnRateConstant(298), 100);

        System.out.println("PowerRateLaw Test: returnRate");
        //first test without using a stream by directly creating concentration map
        double cAoTest = 0.125; //mol/dm3
        double cBoTest = 0.125; //mol/dm3
        double [] concentrationsTest = {cAoTest,cBoTest};
        //second test using a stream
        //species are only A & B so we use species array declared earlier
        SpecieMap concentrationMap = new SpecieMap(species, concentrationsTest);

        //calculate reaction rate for concentration taken the given stream
        double streamSolution = testRate.returnRate(T, stream.returnAllMolConcentrations());
        testPrint(streamSolution, 0.1953125);
    }

    //use case 1 to test the flow reactor code
    public static void testFlowReactor(){

        //first create the reaction species
        Specie specieA = new Specie("A", Phase.LIQUID, Element.A.molarMass, Element.A.heatCapacityCoefficients);
        Specie specieB = new Specie("B", Phase.LIQUID, Element.B.molarMass, Element.B.heatCapacityCoefficients);
        Specie specieC = new Specie("C", Phase.LIQUID, Element.C.molarMass, Element.C.heatCapacityCoefficients);

        Specie[] species = {specieA, specieB, specieC};
        double[] stoichCoeffs = {-1.0,-2.0,1.0};

        StoichiometryMap rxnStoich = new StoichiometryMap(species, stoichCoeffs);

        RefValue refEnthalpy = new RefValue(0,0);

        double a = 100.0;
        double Ea = 0;
        RateConstant kA = new RateConstant(a, Ea);

        double[] orders = {1.,2.,0.};
        SpecieMap rxn_orders = new SpecieMap(species, orders);
        RateLaw rate = new PowerRateLaw(kA,specieA, rxn_orders);

        Reaction rxn = new Reaction(rate, rxnStoich, refEnthalpy);

        PressureDropEquation pDrop = new Isobaric();
        HeatTransferEquation heatX = new HeatTransferEquation(HeatTransferCondition.ISOTHERMAL, refEnthalpy);

        double volume = 600.;
        PFR pfr = new PFR(volume, pDrop, heatX);

        ReactionSet rxns = new ReactionSet(rxn);

        double T = 298.;
        double P = 1.;
        double viscocity = 1.;
        double volFlowRate = 120.;
        double[] initialFlowRates = {15., 15., 0.}; //mol/min
        MolarFlowMap flowMap = new MolarFlowMap(species, initialFlowRates);
        Stream stream = StreamBuilder.buildStream(flowMap, T, P, viscocity, volFlowRate);
        Stream output = pfr.returnReactorOutput(stream, rxns, 0.01, 50000);
        System.out.println("Calculated output flow rates for the PFR"+output.getAllFlowRates());
        System.out.println("Calculated output concentration for the PFR"+output.returnAllMolConcentrations());
        System.out.println(TubularReactor.returnConversion(specieB, stream, output));

    }

    //test the PFRDesigner class with case 2
    public static void testReactorDesigner(){

        //first create the reaction species
        Specie specieA = new Specie("A", Phase.LIQUID, Element.A.molarMass, Element.A.heatCapacityCoefficients);
        Specie specieB = new Specie("B", Phase.LIQUID, Element.B.molarMass, Element.B.heatCapacityCoefficients);
        Specie specieC = new Specie("C", Phase.LIQUID, Element.C.molarMass, Element.C.heatCapacityCoefficients);

        Specie[] species = {specieA, specieB, specieC};
        double[] stoichCoeffs = {-1.0,-2.0,1.0};

        StoichiometryMap rxnStoich = new StoichiometryMap(species, stoichCoeffs);

        RefValue refEnthalpy = new RefValue(0.,0.);

        double a = 100.0;
        double Ea = 0.;
        RateConstant kA = new RateConstant(a, Ea);

        double[] orders = {1.,2.,0.};
        SpecieMap rxn_orders = new SpecieMap(species, orders);
        RateLaw rate = new PowerRateLaw(kA,specieA, rxn_orders);

        Reaction rxn = new Reaction(rate, rxnStoich, refEnthalpy);

        PressureDropEquation pDrop = new Isobaric();
        HeatTransferEquation heatX = new HeatTransferEquation(HeatTransferCondition.ISOTHERMAL, refEnthalpy);

        ReactionSet rxns = new ReactionSet(rxn);

        double T = 298.;
        double P = 1.;
        double viscocity = 1.;
        double volFlowRate = 120.;
        double[] initialFlowRates = {15., 15., 0}; //mol/min
        MolarFlowMap flowMap = new MolarFlowMap(species, initialFlowRates);
        Stream stream = StreamBuilder.buildStream(flowMap, T, P, viscocity, volFlowRate);
        PFRDesigner designer = new PFRDesigner();
        PFR output = designer.returnVForTargetFlow(specieA, 8.183165641,stream, rxns, pDrop, heatX, 2, 50000);
        System.out.println("Rector size found:"+output.getSize());
        //System.out.println(TubularReactor.returnConversion(specieA, stream, output));
    }

    public static void testPressure(){
        //ask for what the species are
        Specie[] species = new Specie[7];
        species[0] = new Specie("A",Phase.IDEALGAS,Element.A.molarMass,Element.A.heatCapacityCoefficients);
        species[1] = new Specie("B",Phase.IDEALGAS,Element.B.molarMass,Element.B.heatCapacityCoefficients);
        species[2] = new Specie("C",Phase.IDEALGAS,Element.C.molarMass,Element.C.heatCapacityCoefficients);
        species[3] = new Specie("D",Phase.IDEALGAS,Element.D.molarMass,Element.D.heatCapacityCoefficients);
        species[4] = new Specie("E",Phase.IDEALGAS,Element.E.molarMass,Element.E.heatCapacityCoefficients);
        species[5] = new Specie("F",Phase.IDEALGAS,Element.F.molarMass,Element.F.heatCapacityCoefficients);
        species[6] = new Specie("I",Phase.IDEALGAS,Element.I.molarMass,Element.I.heatCapacityCoefficients);

        //set initial guesses for inlet molar flow rates
        double[] molarFlows = new double[7];
        molarFlows[0] = 100.;
        molarFlows[1] = 100.;
        molarFlows[2] = 50.;
        molarFlows[3] = 0.;
        molarFlows[4] = 0.;
        molarFlows[5] = 0.;
        molarFlows[6] = 10.;
        MolarFlowMap molarFlowMap = new MolarFlowMap(species,molarFlows);

        //set initial guesses for inlet T, inlet P, volflow
        double T = 400.; //K
        double P = 12; //atm
        double viscosity = 1.8E-5;

        Stream inletStream = StreamBuilder.buildGasStreamFromMolFlows(molarFlowMap,T,P,viscosity);

        double[] orders_1 = {0.35,0,0.2,0,0,0,0};
        double[] orders_2 = {-0.27,0,0.88,0,0,0,0};
        SpecieMap orderMap_1 = new SpecieMap(species, orders_1);
        SpecieMap orderMap_2 = new SpecieMap(species, orders_2);
        double A_1 = 9.7;
        double A_2 = 5.13;
        double activationE_1 = 15000;
        double activationE_2 = 17000;
        RateConstant rateConstant_1 = new RateConstant(A_1, activationE_1);
        RateConstant rateConstant_2 = new RateConstant(A_2, activationE_2);
        Specie refSpecies_1 = species[3].clone();
        Specie refSpecies_2 = species[0].clone();
        RateLaw refReactionRate_1 = new PowerRateLaw(rateConstant_1, refSpecies_1, orderMap_1);
        RateLaw refReactionRate_2 = new PowerRateLaw(rateConstant_2, refSpecies_2, orderMap_2);
        double[] stoichCoeff_1 = {-1,-1,-0.5,1,1,0,0};
        double[] stoichCoeff_2 = {-1,0,-3,0,2,2,0};
        StoichiometryMap stoichiometryMap_1 = new StoichiometryMap(species, stoichCoeff_1);
        StoichiometryMap stoichiometryMap_2 = new StoichiometryMap(species, stoichCoeff_2);
        HeatTransferCondition condition = HeatTransferCondition.ISOTHERMAL;
        RefValue refEnthalpy = new RefValue(298., 0.);
        Reaction rxn_1 = new Reaction(refReactionRate_1, stoichiometryMap_1, refEnthalpy);
        Reaction rxn_2 = new Reaction(refReactionRate_2, stoichiometryMap_2, refEnthalpy);
        Reaction[] rxns = new Reaction[2];
        rxns[0] = rxn_1.clone();
        rxns[1] = rxn_2.clone();
        ReactionSet reactionSet = new ReactionSet(rxns);
        HeatTransferEquation heatX = new HeatTransferEquation(condition, refEnthalpy);

        NominalPipeSizes pipeSize = NominalPipeSizes.ONE_INCH;

        Catalyst catalyst = new Catalyst(0.005,0.45,833);
        ErgunPDrop pDrop = new ErgunPDrop(catalyst, pipeSize);
        System.out.println(pDrop.calculateDelT(inletStream));


    }

    public static void testPBR(){
        //first create the reaction species
        Specie specieA = new Specie("A", Phase.IDEALGAS, Element.A.molarMass, Element.A.heatCapacityCoefficients);
        Specie specieB = new Specie("B", Phase.IDEALGAS, Element.B.molarMass, Element.B.heatCapacityCoefficients);
        Specie specieC = new Specie("C", Phase.IDEALGAS, Element.C.molarMass, Element.C.heatCapacityCoefficients);

        Specie[] species = {specieA, specieB, specieC};
        double[] stoichCoeffs = {-1.0,-2.0,1.0};

        StoichiometryMap rxnStoich = new StoichiometryMap(species, stoichCoeffs);

        RefValue refEnthalpy = new RefValue(0,0);

        double a = 100.0;
        double Ea = 0;
        RateConstant kA = new RateConstant(a, Ea);

        double[] orders = {1,2,0};
        SpecieMap rxn_orders = new SpecieMap(species, orders);
        RateLaw rate = new PowerRateLaw(kA,specieA, rxn_orders);

        Reaction rxn = new Reaction(rate, rxnStoich, refEnthalpy);

        PressureDropEquation pDrop = new Isobaric();
        HeatTransferEquation heatX = new HeatTransferEquation(HeatTransferCondition.ISOTHERMAL, refEnthalpy);

        ReactionSet rxns = new ReactionSet(rxn);

        double T = 298;
        double P = 1;
        double viscocity = 1;
        double volFlowRate = 120;
        double[] initialFlowRates = {15, 15, 0}; //mol/min
        MolarFlowMap flowMap = new MolarFlowMap(species, initialFlowRates);
        Stream stream = StreamBuilder.buildStream(flowMap, T, P, viscocity, volFlowRate);
        PFRDesigner designer = new PFRDesigner();
        PFR output = designer.returnVForTargetFlow(specieA, 8.183165641,stream, rxns, pDrop, heatX, 10, 5000);
        System.out.println(output.getSize());
        //System.out.println(TubularReactor.returnConversion(specieA, stream, output));

    }


    public static void main(String[] args) {
        //Tests.testPowerRateLawLiquid();
        System.out.println("Expected output concentration for the PFR (testFlowReactor()): { A: 8.209693911, B: 1.419387822, C: 6.790306089}");
        System.out.println("Expected conversion of B: 0.905374145");
        System.out.println("Expected output flow rates for the PFR (testFlowReactor()): {A:0.068414116, B: 0.011828232, C: 0.056585884 }");
        Tests.testFlowReactor();
        Tests.testReactorDesigner();
        //Tests.testPressure();
    }


*/}
