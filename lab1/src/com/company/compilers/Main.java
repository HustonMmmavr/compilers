package com.company.compilers;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Input file name: ");
            String fName = br.readLine();
            Grammar grammar = new Grammar(fName);
            grammar.deleteUselesTerms();
            grammar.saveGrammar("newGrammar.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
