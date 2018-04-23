package com.company.compilers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Grammar {
    List<Character> nonTerminals;
    List<Character> terminals;
    List<Rule> rules;
    Character startSymbol;
    String grammarFile;

    public Grammar(String grammarFile) throws IOException {
        initGrammar(grammarFile);
    }

    private void initGrammar(String grammarFile) throws IOException {
        this.grammarFile = grammarFile;
        Scanner scanner = new Scanner(new File(grammarFile));
        Integer countNonTerminals = scanner.nextInt();
        nonTerminals = new ArrayList<>(countNonTerminals);
        for (Integer i = 0; i < countNonTerminals; i++) {
            nonTerminals.add(scanner.next().charAt(0));
        }

        Integer countTerminals =  scanner.nextInt();
        terminals = new ArrayList<>(countTerminals);

        for (Integer i = 0; i < countTerminals; i++) {
            terminals.add(scanner.next().charAt(0));
        }

        Integer countRules = scanner.nextInt();
        rules = new ArrayList<>(countRules);
        for (Integer i = 0; i < countRules; i++) {
            String data = scanner.next();
            String[] parsedData = data.split("->");
            String[] terminals = parsedData[1].split("");
            ArrayList<Character> characters = new ArrayList<>();
            for (String term: terminals) {
                characters.add(term.charAt(0));
            }
            rules.add(new Rule(parsedData[0].charAt(0), characters));
        }
        startSymbol = scanner.next().charAt(0);
        System.out.print(rules);
    }

    public void saveGrammar() {

    }

    public Boolean isNonTerminal(Character term) {
        return nonTerminals.contains(term);
    }

    public void deleteInfertile() {
        List<Character> attainables = new ArrayList<>();
        attainables.add(startSymbol);
//        Rule startRule =

        for (Rule rule : rules) {
            for (Character character: rule.getTerms()) {
                if (isNonTerminal(character)) {
//                    attainables.
                }
            }
            //characters.forEach(character -> isNonTerminal(character));
        }
    }

    public void deleteUnattainable() {

    }
}
