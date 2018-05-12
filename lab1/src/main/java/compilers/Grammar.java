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

    public void deleteUselesTerms() throws Exception {
        deleteUngenerating();
        deleteUnreacheble();
    }

    private void deleteUngenerating() {
        Hashtable<Character, Boolean> generatingNonTerms = new Hashtable<>();
        Hashtable<Character, List<List<Character>>> termsAndRules = new Hashtable<>();

        for (Character nonTerm: nonTerminals) {
            generatingNonTerms.put(nonTerm, false);
            termsAndRules.put(nonTerm, new ArrayList<>());
        }

        for (Rule rule: rules) {
            List<List<Character>> associatedNonTerms = termsAndRules.get(rule.getNonTerm());
            List<Character> temp = new ArrayList<>();
            for (Character term : rule.getTerms()) {
                if (isNonTerminal(term)) {
                    temp.add(term);
                }
            }
            associatedNonTerms.add(temp);
        }

        List<Character> queue = new ArrayList<>();
        for (Character key: termsAndRules.keySet()) {
            for (List<Character> temp: termsAndRules.get(key)) {
                if (temp.size() == 0) {
                    generatingNonTerms.put(key, true);
                    if (!queue.contains(key)) {
                        queue.add(key);
                    }
                }
            }
        }


        while (queue.size() != 0) {
            termsAndRules.remove(queue.get(0));
            for (Character key: termsAndRules.keySet()) {
                for (List<Character> temp: termsAndRules.get(key)) {
                    temp.remove(queue.get(0));
                    if (temp.size() == 0 && !queue.contains(key)) {
                        queue.add(key);
                        generatingNonTerms.put(key, true);
                    }
                }
            }

            for (int i = 0; i < rules.size(); i++) {
                Rule rule = rules.get(i);
                if (!generatingNonTerms.get(rule.getNonTerm())) {
                    rules.remove(rule);
                }
            }
            queue.remove(0);
        }

        for (int i = 0; i < rules.size(); i++) {
            for (Character term: rules.get(i).getTerms()) {
                if (isNonTerminal(term)) {
                    if (!generatingNonTerms.get(term)) {
                        rules.remove(i);
                    }
                }
            }
        }
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


//    public Integer getCountNonTerminals(Rule rule) {
//        Integer count = 0;
//        for (Character term : rule.getTerms()) {
//            if (isNonTerminal(term)) {
//                count++;
//            }
//        }
//        return count;
//    }

//
//    private Boolean checkIsGenerating(Character nonTerm) {
//        List<Rule> asscoiated = getAssociatedRules(nonTerm);
//        for (Rule rule : asscoiated) {
//            if (isTerminalRule(rule)) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    private List<Rule> getAssociatedRulesRight(Character nonTerm) {
//        return rules.stream().filter(rule -> rule.getTerms().contains(nonTerm))
//                .collect(Collectors.toList());
//    }


//    private boolean isTerminalRule(Rule rule) {
//        for (Character term: rule.getTerms()) {
//            if (isNonTerminal(term)) {
//                return false;
//            }
//        }
//        return true;
//    }