package search;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.lang.reflect.Array;
import java.security.Key;
import java.util.*;

public class Main {
    private static Map<String, List<Integer>> people = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    private static List<String> listOfPeople = new ArrayList<>();
    private static File file;
    private static int lines = 0;
    private static List<Integer> valSetOne;

    public static void setValSetOne(List<Integer> valSetOne) {
        Main.valSetOne = valSetOne;
    }

    public static Map<String, List<Integer>> getPeople() {
        return people;
    }

    public static List<String> getListOfPeople() {
        return listOfPeople;
    }

    public static List<Integer> getValSetOne() {
        return valSetOne;
    }

    public static void main(String[] args) throws IOException {
        if (args[0].equals("--data")) {//
            file = new File(args[1]);//
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String lineFor = line;
                    listOfPeople.add(lineFor);
                    String[] row = lineFor.trim().split(" ");
                    for (String s : row) {
                        if (!people.containsKey(s)) {
                            ArrayList<Integer> ss = new ArrayList<>();
                            ss.add(lines);
                            people.put(s, ss);
                        } else {
                            List<Integer> sd = people.get(s);
                            sd.add(lines);
                        }
                    }
                    lines++;
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.println("=== Menu ===");
            System.out.println("1. Find a person");
            System.out.println("2. Print all people");
            System.out.println("0. Exit");
            int option = Integer.parseInt(bufferedReader.readLine());

            while (option != 0) {
                switch (option) {
                    case 1:
                        Printer printer = new Printer();
                        String choser = bufferedReader.readLine();
                        if (choser.equalsIgnoreCase("ALL")) {
                            printer.setStrategies(new PrintAll());
                            printer.choseStrategy();
                        } else if (choser.equalsIgnoreCase("ANY")) {
                            printer.setStrategies(new PrintAny());
                            printer.choseStrategy();
                        } else if (choser.equalsIgnoreCase("NONE")) {
                            printer.setStrategies(new PrintNone());
                            printer.choseStrategy();
                        }
                        break;
                    case 2:
                        findAllPeople();
                        break;
                    default:
                        System.out.println("Incorrect option! Try again.");
                }
                option = Integer.parseInt(bufferedReader.readLine());
            }
            System.out.println("Bye!");
        }
    }

    public static void findAllPeople() {
        System.out.println("=== List of people ===");
        for (String s : listOfPeople) {
            System.out.println(s);
        }
    }
}

interface Strategies {
    void printPeople();
}

class PrintAll implements Strategies {

    @Override
    public void printPeople() {
        TreeSet<Integer> result = new TreeSet<>();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a name or email to search all suitable people.");
        String find = scanner.next().trim();
        for (String s : Main.getListOfPeople()) {
            if (s.contains(find)) {
                Main.setValSetOne(Main.getPeople().get(s));
                result.addAll(Main.getValSetOne());
            }
        }
        System.out.println(result.size() + " persons found:");
        for (Integer d : result) {
            System.out.println(Main.getListOfPeople().get(d));
        }
    }
}

class PrintAny implements Strategies {

    @Override
    public void printPeople() {
        TreeSet<Integer> result = new TreeSet<>();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a name or email to search all suitable people.");
        String[] find = scanner.nextLine().trim().split(" ");
        for (String s : find) {
            if (Main.getPeople().containsKey(s)) {
                Main.setValSetOne(Main.getPeople().get(s));
                result.addAll(Main.getValSetOne());
            }
        }
        System.out.println(result.size() + " persons found:");
        for (Integer d : result) {
            System.out.println(Main.getListOfPeople().get(d));
        }
    }
}

class PrintNone implements Strategies {

    @Override
    public void printPeople() {
        TreeSet<Integer> result = new TreeSet<>();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a name or email to search all suitable people.");
        String[] find = scanner.nextLine().trim().split(" ");
        for (String s : find) {
            if (Main.getPeople().containsKey(s)) {
                Main.setValSetOne(Main.getPeople().get(s));
                result.addAll(Main.getValSetOne());
            }
        }
        System.out.println(Main.getListOfPeople().size()-result.size() + " persons found:");
        for (String s: Main.getListOfPeople())
        {   boolean flag = false;
            for (Integer d:result) {
                if(s.contains(Main.getListOfPeople().get(d))){
                    flag = true;
                }
            }
            if(!flag){
                System.out.println(s);
            }
        }
    }
}

class Printer {
    Strategies strategies;

    public void setStrategies(Strategies strategies) {
        this.strategies = strategies;
    }

    public void choseStrategy() {
        strategies.printPeople();
    }
}