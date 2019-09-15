package com.pavka;

import com.badlogic.gdx.utils.Array;

import java.util.Random;

public class LogisticTest {

    public static Hex hex = new Hex();
    public static void main(String[] args) {
        Force regiment = new Force(new Battalion(Nation.FRANCE, hex));
        regiment.attach(new Battalion(Nation.FRANCE, hex));
        Force regiment1 = new Force(new Battalion(Nation.FRANCE, hex));
        regiment.attach(new Battalion(Nation.FRANCE, hex));
        Force brigade = new Force(regiment, regiment1);
        brigade.attach(new Battery(Nation.FRANCE, hex));
        Force cavRegiment = new Force (new Squadron(Nation.FRANCE, hex));
        cavRegiment.attach(new Squadron(Nation.FRANCE, hex));
        Force division = new Force(brigade, cavRegiment);
        Force corps = new Force(division, new Battery(Nation.FRANCE, hex));
        System.out.println("Food Need = " + corps.foodNeed + " Food Stock = " + corps.foodStock);
        double eatenFood = corps.eat();
        System.out.println("Lunch!");
        System.out.println("Food Need = " + corps.foodNeed + " Food Stock = " + corps.foodStock);

        /*Array<Integer> numbers = new Array<Integer>();
        numbers.add(0);
        numbers.add(1);
        System.out.println(numbers.size);
        System.out.println(numbers.get(0));
        System.out.println(numbers.get(1));
        System.out.println("RANDOM");
        Random r = new Random();
        System.out.println(r.nextDouble());*/

    }
}
