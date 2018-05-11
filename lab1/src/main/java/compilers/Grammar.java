package compilers;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Grammar {
    private List<Character> nonTerminals;
    private List<Character> terminals;
    private List<Rule> rules;
    private Character startSymbol;
    private String grammarFile;

    public Grammar(String grammarFile) throws IOException {
        initGrammar(grammarFile);
    }

    public Grammar(List<Character> nonTerminals, List<Character> terminals, List<Rule> rules, Character startSymbol, String grammarFile) {
        this.nonTerminals = nonTerminals;
        this.terminals = terminals;
        this.rules = rules;
        this.startSymbol = startSymbol;
        this.grammarFile = grammarFile;
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

    public void saveGrammar(String newGrammarFile) throws IOException {
        PrintWriter writer = new PrintWriter(newGrammarFile, "UTF-8");
        writer.println(nonTerminals.size());
        for (Character term: nonTerminals) {
            writer.println(term);
        }

        writer.println(terminals.size());
        for (Character term: terminals) {
            writer.println(term);
        }

        writer.println(rules.size());
        for (Rule rule: rules) {
            writer.println(rule);
        }

        writer.println(startSymbol);
        writer.close();
    }

    private Boolean isNonTerminal(Character term) {
        return nonTerminals.contains(term);
    }

    private List<Rule> getAssociatedRules(Character nonTerm) {
        return rules.stream().
                filter(rule -> rule.getNonTerm().equals(nonTerm)).
                collect(Collectors.toList());
    }

    private boolean isTerminalRule(Rule rule) {
        for (Character term: rule.getTerms()) {
            if (isNonTerminal(term)) {
                return false;
            }
        }
        return true;
    }

    private Boolean checkIsGenerating(Character nonTerm) {
        List<Rule> asscoiated = getAssociatedRules(nonTerm);
        for (Rule rule : asscoiated) {
            if (isTerminalRule(rule)) {
                return true;
            }
        }
        return false;
    }

    private List<Rule> getAssociatedRulesRight(Character nonTerm) {
        return rules.stream().filter(rule -> rule.getTerms().contains(nonTerm))
                .collect(Collectors.toList());
    }

    public void deleteUselesTerms() throws Exception {
        deleteUngenerating();
        deleteUnreacheble();
    }

    public Integer getCountNonTerminals(Rule rule) {
        Integer count = 0;
        for (Character term : rule.getTerms()) {
            if (isNonTerminal(term)) {
                count++;
            }
        }
        return count;
    }

//    private List<C>

    private void deleteUngenerating() {
//        List<Pair<Character, Boolean>> generatingNonTerms = new ArrayList<>();
        Hashtable<Character, Boolean> generatingNonTerms = new Hashtable<>();
        for (Character nonTerm: nonTerminals) {
            generatingNonTerms.put(nonTerm, false);
//            generatingNonTerms.add(new Pair<>(nonTerm, false));
        }

        List<Rule> ungeneratingRules = new ArrayList<>(rules);


        List<Pair<Character, List<Character>>> associatedNonTerms = new ArrayList<>();
        List<Pair<Rule, Integer>> countNonTerminals = new ArrayList<>();
        for (Rule rule: ungeneratingRules) {
            //associatedNonTerms.add(new Pair<>(rule.getNonTerm(), ))
            countNonTerminals.add(new Pair<>(rule, getCountNonTerminals(rule)));
        }

        List<Pair<Character, List<Rule>>> asscoiatedRules = new ArrayList<>();
        for (Character nonTerm : nonTerminals) {
            asscoiatedRules.add(new Pair<>(nonTerm, getAssociatedRulesRight(nonTerm)));
        }

        List<Character> queue = new ArrayList<>();

        // Rules - copy and remove, after copied undeleted delete from main

        for (int i = 0; i < countNonTerminals.size(); i++) {
            if (countNonTerminals.get(i).getValue() == 0) {
                Rule generatingRule = ungeneratingRules.get(i);
                generatingNonTerms.put(generatingRule.getNonTerm(), true);
                queue.add(generatingRule.getNonTerm());
                ungeneratingRules.remove(generatingRule);
            }
        }

        while (queue.size() != 0) {
            for (Rule rule : ungeneratingRules) {
            }
        }





//        Hashtable<Character, List<Character>> activeNonTerm = new Hashtable<>();
//        List<Rule> associatedRules = rules.stream().filter(getAssociatedRules(startSymbol).stream())
//        activeNonTerm.put()
//
//        for (int i = 0; i < activeNonTerms.size(); i++) {
//            List<Rule> activeRules = getAssociatedRules(activeNonTerms.get(i));
//
//        }

    }


    private void deleteUnreacheble() throws Exception {
        if (rules.size() == 0) {
            throw new Exception("There is no rules");
        }
        List<Character> activeNonTerms = new ArrayList<>();
        List<Character> activeTerms = new ArrayList<>();
        activeNonTerms.add(startSymbol);

        for (int i = 0; i < activeNonTerms.size(); i++) {
            List<Rule> workRules = getAssociatedRules(activeNonTerms.get(i));
            for (Rule rule : workRules) {
                for (Character term : rule.getTerms()) {
                    if (isNonTerminal(term)) {
                        if (!activeNonTerms.contains(term)) {
                            activeNonTerms.add(term);
                        }
                    } else {
                        if (!activeTerms.contains(term)) {
                            activeTerms.add(term);
                        }
                    }
                }
            }
        }

        List<Rule> newRules = new ArrayList<>();
        activeNonTerms.forEach(nonTerm -> newRules.addAll(getAssociatedRules(nonTerm)));
        this.nonTerminals = activeNonTerms;
        this.terminals = activeTerms;
        this.rules = newRules;
    }
}
