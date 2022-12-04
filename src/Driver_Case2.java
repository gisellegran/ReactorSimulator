import chemistry.*;
import reactor.*;

public class Driver_Case2 {/*
    public static void main(String[] args) {

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

        PBRDesigner designer = new PBRDesigner();
        PBR pbr = designer.returnReactorForTargetFlow(species[3],10,inletStream,reactionSet,pDrop, heatX, 1,10000);
        double initialSize = pbr.getSize();

        //analyze P effect

        do {
            initialSize = pbr.getSize();
            P = P + 0.5;
            inletStream = StreamBuilder.buildGasStreamFromMolFlows(molarFlowMap,T,P,viscosity);
            pbr = designer.returnReactorForTargetFlow(species[3],10,inletStream,reactionSet,pDrop, heatX, 1,10000);
        } while(P <= 12.5 && pbr.getSize() <= initialSize);

        //analyze T effect
        do{
            initialSize = pbr.getSize();
            T = T + 10;
            inletStream = StreamBuilder.buildGasStreamFromMolFlows(molarFlowMap,T,P,viscosity);
            pbr = designer.returnReactorForTargetFlow(species[3],10,inletStream,reactionSet,pDrop, heatX, 1,10000);
        } while(T <= 520 && pbr.getSize() < initialSize);

        //analyze inlet flowrates
        do {
            initialSize = pbr.getSize();
            molarFlows[0] = molarFlows[0] + 5;
            molarFlowMap = new MolarFlowMap(species,molarFlows);
            inletStream = StreamBuilder.buildGasStreamFromMolFlows(molarFlowMap,T,P,viscosity);
            pbr = designer.returnReactorForTargetFlow(species[3],10,inletStream,reactionSet,pDrop, heatX, 1,10000);
        } while(pbr.getSize() < initialSize);

        do {
            initialSize = pbr.getSize();
            molarFlows[1] = molarFlows[1] + 5;
            molarFlowMap = new MolarFlowMap(species,molarFlows);
            inletStream = StreamBuilder.buildGasStreamFromMolFlows(molarFlowMap,T,P,viscosity);
            pbr = designer.returnReactorForTargetFlow(species[3],10,inletStream,reactionSet,pDrop, heatX, 1,10000);
        } while(pbr.getSize() < initialSize);

        //TODO: fix this

        do {
            initialSize = pfr.getSize();
            molarFlows[2] = molarFlows[2] + 5;
            molarFlowMap = new MolarFlowMap(species,molarFlows);
            inletStream = StreamBuilder.buildGasStreamFromMolFlows(molarFlowMap,T,P,viscosity,volFlow);
            pfr = designer.returnReactorForTargetFlow(species[3],10,inletStream,reactionSet,pDrop, heatX, 1,10000);
        } while(inletStream.returnSpecieMolConcentration(species[2]) <= 0.2*inletStream.returnTotalConcentration() && pfr.returnReactorOutput(species[2]) <= 0.2*inletStream.returnTotalConcentration() && size < );



        do {
            initialSize = pbr.getSize();
            molarFlows[6] = molarFlows[6] + 5;
            molarFlowMap = new MolarFlowMap(species,molarFlows);
            inletStream = StreamBuilder.buildGasStreamFromMolFlows(molarFlowMap,T,P,viscosity);
            pbr = designer.returnReactorForTargetFlow(species[3],10,inletStream,reactionSet,pDrop, heatX, 1,10000);
        } while(pbr.getSize() < initialSize);

        System.out.println("The optimum initial flowrates are: "+inletStream.getAllFlowRates());
        System.out.println("The total initial flowrate is: "+inletStream.getMolarFlowRate());
        System.out.println("The optimal reaction temperature is:  "+T);
        System.out.println("The optimal initial pressure is:" +P);
        System.out.println("The weight of catalyst used is: ");
        System.out.println("The reactor volume needed is: "+pbr.getSize());

    }
*/}
