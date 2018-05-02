package com.company.compilers;

import java.util.List;

public class Rule {
    private Character nonTerm;
    private List<Character> terms;

    Rule(Character nonTerm, List<Character> terms) {
        this.nonTerm = nonTerm;
        this.terms = terms;
    }

    public Character getNonTerm() {
        return nonTerm;
    }

    public void setNonTerm(Character nonTerm) {
        this.nonTerm = nonTerm;
    }

    public List<Character> getTerms() {
        return terms;
    }

    public void setTerms(List<Character> terms) {
        this.terms = terms;
    }

    @Override
    public String  toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(nonTerm).append("->");//.append(terms.toString());
        terms.forEach(stringBuilder::append);
        return stringBuilder.toString();
    }
}
