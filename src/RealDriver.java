import chemistry.*;
import reactor.*;
import reactor.heat_transfer.*;
import reactor.pressure_drop.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.Scanner;

public class RealDriver {
    //takes comma delimited line and converts to array of doubles

    private static double delX = 0.01;
    private static int maxIt = 500000;
    private static double[] stringToDoubleArray(String s){
        String[] strArr = s.split(",");
        double[] doubleArr = new double[strArr.length];
        for (int i = 0; i < strArr.length; i++) {
            //convert string number into double
            doubleArr[i] = Double.parseDouble(strArr[i]);
        }
        return doubleArr;
    }

    public static String doubleArrayToString(Specie[] s, double[] array){
        DecimalFormat df = new DecimalFormat("0.0000");
        String str = "";
        for (int i = 0; i < array.length; i++) {
            str += (s[i].getName()+": "+df.format(array[i]));
            if (i < array.length-1) str += ", ";
        }
        return "{ "+str+" }";
    }

    private static Specie specieBuilder(String name, String phase){
        if (name == null ) throw new IllegalArgumentException("null specie name");
        if (phase == null ) throw new IllegalArgumentException("null phase");

        Element element = Element.findByName(name.strip());
        Phase phase1 = Phase.findByName(phase);
        return new Specie(element.name, phase1, element.molarMass, element.heatCapacityCoefficients);
    }

    //takes string which is the line in input where it should be indicated if reaction is elementary
    //and converts to a boolean
    private static boolean isElementary(String line){
        if (line == null) return false;
        return line.strip().equalsIgnoreCase("elementary");
    }

    //takes string which is the line in input where it should be indicated if reaction is reversible
    //and converts to a boolean
    private static boolean isReversible(String line){
        if (line == null) return false;
        return line.strip().equalsIgnoreCase("reversible");
    }

    private static Specie[] getSpeciesArray(String[] specieNames, String[] phases){
        if (specieNames == null) throw new IllegalArgumentException( "species names are null");
        if (phases == null) throw new IllegalArgumentException( "phases are null");

        if (phases.length != 1 && specieNames.length != phases.length)  throw new IllegalArgumentException( "array length mismatch between phases and species");

        Specie[] species = new Specie[specieNames.length];
        for (int i = 0; i < specieNames.length; i++) {
            if (phases.length == 1) species[i] = specieBuilder(specieNames[i], phases[0]); //if a single phase is entered, apply it to all the species
            else species[i] = specieBuilder(specieNames[i], phases[i]);
        }

        return species;
    }

    private static boolean isSinglePhase(Specie[] species){
        if (species == null) throw new IllegalArgumentException("species is null");
        Phase phase = species[0].getPhase();
        for (int i = 1; i < species.length; i++) {
            if(species[i].getPhase() != phase) return false;
        }
        return true;
    }

    //build RefValue object for a reaction reference enthalpy
    //from a string having format double,double corresponding to delH,T
    //where delH is the enthalpy value and T is the reference temperature of the enthalpy
    private static RefValue getRefEnthalpyFromLine(String line){
        if (line == null) throw new IllegalArgumentException("line is null");
        double refDelH = 0.; //default value of 0
        double refT = 0.; //default value of 0
        Scanner lineScan = new Scanner(line);
        lineScan.useDelimiter(",|\n");
        if (lineScan.hasNextDouble()) {
            refDelH = lineScan.nextDouble();
        }
        if (lineScan.hasNextDouble()) {
            refT  = lineScan.nextDouble();
        }
        lineScan.close();

        return new RefValue(refDelH, refT);
    }

    //build RateConstant for a reaction rate constant object
    //from a string having format double,double corresponding to A,E
    //where A is the pre-exponential constant and E is the activation energy of the reaction
    private static RateConstant getRateConstantFromLine(String line){
        if (line == null) throw new IllegalArgumentException("line is null");
        //rate constant
        Scanner lineScan = new Scanner(line);
        lineScan.useDelimiter(",|\n");
        double A = lineScan.nextDouble();
        double E = 0.;
        if (lineScan.hasNextDouble()) E = lineScan.nextDouble();
        lineScan.close();
        return new RateConstant(A, E);
    }

    private static HeatTransferEquation getHeatEqFromLine(String line){
        if (line == null) throw new IllegalArgumentException("line is null");
        Scanner lineScan = new Scanner(line);
        lineScan.useDelimiter(",|\n");

        HeatTransferCondition condition = HeatTransferCondition.findByName(lineScan.next());
        //TODO: throw error or just say that isothermal will be assumed?
        if (condition == null) { throw new IllegalArgumentException("heat transfer condition is not provided");}

        //todo: maybe fix this
        switch (condition) {
            case ISOTHERMAL -> {
                lineScan.close();
                return new Isothermal();
            }
            case ADIABATIC -> {
                lineScan.close();
                return new Adiabatic();
            }
            case COCURRENT, COUNTERCURRENT -> {
                double U, Ta0, m, Cp;
                if (!lineScan.hasNextDouble()) throw new IllegalArgumentException("missing heat transfer data");
                U = lineScan.nextDouble();
                if (!lineScan.hasNextDouble()) throw new IllegalArgumentException("missing heat transfer data");
                Ta0 = lineScan.nextDouble();
                if (!lineScan.hasNextDouble()) throw new IllegalArgumentException("missing heat transfer data");
                m = lineScan.nextDouble();
                if (!lineScan.hasNextDouble()) throw new IllegalArgumentException("missing heat transfer data");
                Cp = lineScan.nextDouble();
                lineScan.close();
                switch (condition) {
                    case COCURRENT:
                        return new CoCurrent(U, Ta0, m, Cp);
                    case COUNTERCURRENT:
                        return new CounterCurrent(U, Ta0, m, Cp);
                }
            }
            default -> //TODO: throw error?
                    System.out.println("Default isothermal is created");
        }

        lineScan.close();
        //default
        return new Isothermal();
    }

    private static Catalyst getCatalystFromLine(String line){
        if (line == null) throw new IllegalArgumentException("line is null");
        Scanner lineScan = new Scanner(line);
        double pDiameter, voidFrac, pDensity;
        if( !lineScan.hasNextDouble() ) throw new IllegalArgumentException("missing catalyst data");
        pDiameter = lineScan.nextDouble();
        if( !lineScan.hasNextDouble() ) throw new IllegalArgumentException("missing catalyst data");
        voidFrac = lineScan.nextDouble();
        if( !lineScan.hasNextDouble() ) throw new IllegalArgumentException("missing catalyst data");
        pDensity = lineScan.nextDouble();
        lineScan.close();
        return new Catalyst(pDiameter, voidFrac, pDensity);



    }

    //return specie object stored in a specie array corresponding to the name
    private static Specie getSpecieFromName(String name, Specie[] species){
        for (int i = 0; i < species.length; i++) {
            if(species[i].getName().equalsIgnoreCase(name)) {
                return species[i];
            }
        }

        return null;
    }

    public static void writeData(FileOutputStream destination, Reactor reactor, Stream outlet){
        PrintWriter outputStream = new PrintWriter(destination);//PrintWriter is buffered, which is more efficient
        outputStream.println(reactor.toString());
        outputStream.println("Oulet stream molar flows:"+ outlet.molarFlowRatesToString());
        outputStream.close();

    }

    public static void main(String[] args) throws IOException {

        try {

            FileInputStream input = new FileInputStream("input.csv");
            Scanner in = new Scanner(input);

            in.nextLine(); //skip line that says species

            //species
            String[] speciesStr = (in.nextLine()).split(",");

            String[] phases = (in.nextLine()).split(",");

            Specie[] species = getSpeciesArray(speciesStr, phases);

            //check that we are dealing with a single phase
            //TODO; change error type?
            if (!isSinglePhase(species)) throw new IllegalArgumentException("program can only handle single phase problems");

            in.nextLine(); //skip empty line
            in.nextLine(); //skip line that says reactions

            int nRxns = in.nextInt();
            in.nextLine();//advance cursor to next line

            Reaction[] rxns = new Reaction[nRxns];
            int r = 0; // index for parsing reaction information from the text file

            //reactions
            //pattern  matches a word
            while ( r < nRxns) {
                if (!in.hasNext("[a-zA-Z]+") || !in.next("[a-zA-Z]+").strip().equalsIgnoreCase("reaction")){
                    throw new IllegalArgumentException("not all reactions are defined");//TODO:change to different error type?
                }

                in.nextLine(); // advance cursor to next line

                //reaction phase
                Phase phase = species[0].getPhase(); // all species should have the same phase

                //stoichiometry
                double[] stoich = stringToDoubleArray(in.nextLine());


                //reference specie
                String refName = in.nextLine();
                Specie refSpecie = getSpecieFromName(refName, species);


                //reference enthalpy
                RefValue refEnthalpy = getRefEnthalpyFromLine(in.nextLine());

                //rate constant
                RateConstant k = getRateConstantFromLine(in.nextLine());

                //determine reaction type
                boolean isElementary = isElementary(in.nextLine());
                boolean isReversible = isReversible(in.nextLine());
                RateLawType rateType = RateLawType.getTypeOfRate(isElementary, isReversible);

                //rate law
                double[] orders;
                double[] backwardsOrders; // for reversible reactions
                double Keq; // for reversible reactions
                RateLaw rateLaw = null;

                //build rate law based on type
                switch (rateType) {

                    case ELEMENTARY:
                        orders = new double[species.length];
                        for (int i = 0; i < orders.length; i++) {
                            if(stoich[i] < 0) orders[i] = Math.abs(stoich[i]);//orders are equals to the reactants stoich coeffs
                            else orders[i] = 0.;
                        }
                        rateLaw = new PowerRateLaw(k, refSpecie, orders);
                        break;
                    case ELEMENTARY_REVERSIBLE:
                        orders = new double[species.length];
                        backwardsOrders = new double[species.length];
                        for (int i = 0; i < orders.length; i++) {
                            if(stoich[i] < 0) {
                                orders[i] = Math.abs(stoich[i]);//orders are equals to the reactants stoich coeffs
                                backwardsOrders[i] = 0.;
                            }
                            else {
                                backwardsOrders[i] = Math.abs(stoich[i]);
                                orders[i] = 0.;
                            }
                        }
                        Keq = in.nextDouble();
                        rateLaw = new ReversibleRateLaw(k, refSpecie, Keq, orders, backwardsOrders);
                        break;

                    case POWER:
                        orders = stringToDoubleArray(in.nextLine());

                        if (orders.length != species.length) throw new IllegalArgumentException("invalid number of orders provided");
                        rateLaw = new PowerRateLaw(k, refSpecie, orders);
                        break;
                    case POWER_REVERSIBLE:
                        orders = stringToDoubleArray(in.nextLine());
                        backwardsOrders = stringToDoubleArray(in.nextLine());
                        if (orders.length != species.length || backwardsOrders.length != species.length)
                            throw new IllegalArgumentException("invalid number of orders provided");

                        Keq = in.nextDouble();
                        rateLaw = new ReversibleRateLaw(k, refSpecie, Keq, orders, backwardsOrders);
                        break;

                }

                //create reaction from all the components and place in reactions array
                rxns[r] = new Reaction(species, stoich, rateLaw, refEnthalpy, phase);

                r++; //update counter r
            }

            //create Reaction Set
            ReactionSet rxnSet = new ReactionSet(rxns);
            //check if single phase otherwise throw error
            if (!rxnSet.isSinglePhase()) throw new IllegalArgumentException("program can only handle single phase problems");


            in.nextLine(); //skip empty line
            in.nextLine(); //skip line that says reactor

            //inlet stream
            in.nextLine(); //skip empty line
            in.nextLine(); //skip line that says inlet

            Stream inlet = null;
            double T, P, viscosity;
            double volFlowRate = -1.;
            Scanner lineScan = new Scanner(in.nextLine());
            lineScan.useDelimiter(",|\n");
            if (lineScan.hasNextDouble()) T = lineScan.nextDouble();
            else T = 298.;//default value
            if (lineScan.hasNextDouble()) P = lineScan.nextDouble();
            else P = 101325.;
            if (lineScan.hasNextDouble()) viscosity = lineScan.nextDouble();
            else viscosity = 1.8E-5; //default value
            lineScan.close();

            Phase phase = species[0].getPhase(); //all species were checked to have the same phase

            if (phase == Phase.LIQUID ) {
                if (in.hasNextDouble()) {
                    volFlowRate = in.nextDouble();
                    in.nextLine(); //advance cursor
                }
                else throw new IllegalArgumentException("volumetric flow rate must be provided for liquid streams");
            }

            String compositionFormat = in.nextLine();
            double[] composition = stringToDoubleArray(in.nextLine()); // mol fractions or mol flow depending on composition format

            if (compositionFormat.equalsIgnoreCase("mol flows")) {

                switch (phase) {
                    case IDEALGAS -> {
                        inlet = StreamBuilder.buildGasStreamFromMolFlows(species, composition, T, P, viscosity);
                    }
                    case LIQUID -> {
                        inlet = StreamBuilder.buildStreamFromMolFlows(species, composition, T, P, viscosity, volFlowRate);
                    }
                }
            } else {
                double totalMolFlow = in.nextDouble();
                in.nextLine(); //advance cursor to next line

                switch (phase) {
                    case IDEALGAS -> {
                        inlet = StreamBuilder.buildGasStream(species, composition, T, P, viscosity, totalMolFlow);
                    }
                    case LIQUID -> {
                        inlet = new Stream(species, composition, T, P, viscosity, volFlowRate, totalMolFlow);
                    }
                }

            }

            //reactor type
            String reactorType = in.nextLine();

            //pipe size
            NominalPipeSizes pipeSize = NominalPipeSizes.findByName(in.nextLine().strip());
            if (pipeSize == null) throw new IllegalArgumentException("pipe size not found");

            //heat transfer
            HeatTransferEquation heatEq = getHeatEqFromLine(in.nextLine());

            //pressure drop
            lineScan = new Scanner(in.nextLine());
            lineScan.useDelimiter(",");
            PressureDropCondition pDropCondition = PressureDropCondition.findByName(lineScan.next());
            lineScan.reset(); //reset delimiter

            PressureDropEquation pDrop;
            Catalyst catalyst = null;
            //if we have ergun, then get catalyst object form the rest of the line
            if( pDropCondition == PressureDropCondition.ERGUN_CORRELATION) {
                //if ergun pressure drop is selected we must have a pbr
                // TODO:change to different error type?
                if (reactorType.equals("pfr")) throw new IllegalArgumentException("can't use ergun correlation in pfr");

                catalyst = getCatalystFromLine(lineScan.nextLine());
                pDrop = new ErgunPDrop(catalyst, pipeSize);

            } else { pDrop = new Isobaric(); }

            lineScan.close();

            //problem type
            String problem = in.nextLine();//

            if(!problem.equalsIgnoreCase("performance") && !problem.equalsIgnoreCase("design")){
                throw new IllegalArgumentException("invalid problem type");
            }



            double size = -1;
            Specie desiredS = null;
            Specie undesiredS = null;
            TubularReactor reactor = null;

            if (problem.equalsIgnoreCase("performance")) {
                //reactor size
                in.reset(); //rest delimiter
                size = in.nextDouble();//volume or weight
            }

            //reactor

            if (reactorType.equals("pfr")) {
                reactor = new PFR(size, pDrop, heatEq, pipeSize);
            }
            else if (reactorType.equals("pbr")) {
                reactor = new PBR(size, pDrop, heatEq, pipeSize, catalyst);
            }

            if (problem.equalsIgnoreCase("design")) {
                String toMaximize = in.next();
                String desiredName = in.next(); //get name of desired specie
                desiredS = getSpecieFromName(desiredName, species);
                if (toMaximize.equalsIgnoreCase("flow")) {
                    PFRDesigner designer = new PFRDesigner(reactor);
                    reactor = designer.returnReactorForMaxFlow(desiredS, inlet, rxnSet, delX, maxIt);
                } else if (toMaximize.equalsIgnoreCase("selectivity")) {
                    String undesiredName = in.next();
                    undesiredS = getSpecieFromName(undesiredName, species);
                    PFRDesigner designer = new PFRDesigner(reactor);
                    reactor = designer.returnReactorForMaxSelectivity(desiredS, undesiredS, inlet, rxnSet, delX, maxIt);
                } else {
                    //todo: throw error
                }
            }

            in.close();//advance cursor to next line

            Stream outlet = reactor.returnReactorOutput(inlet, rxnSet, 0.01, 1000000 );

            FileOutputStream output = new FileOutputStream("output.txt");
            writeData(output, reactor, outlet);

            //running the problem


        }
        finally{
            System.out.println("no input found");
        }




    }
}
