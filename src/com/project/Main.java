package com.project;

import com.project.bdd.BDDNotInitializedException;
import com.project.tester.Tester;

public class Main {
    public static void main(String[] args) throws BDDNotInitializedException {
        Tester tester = new Tester();

        int id = 16;
        for (int i = 16; i <= 30; i++) {
            tester.testBDD(id, false,100, id*100+1);
            System.out.println("========================================= " + (id*100+1) + ". done");
            id++;
        }

    }
}