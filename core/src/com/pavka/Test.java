package com.pavka;


import java.util.Arrays;
import java.util.Comparator;

import static com.pavka.Nation.*;



public class Test {

    static Hex hex = Play.hexGraph.getHex(4, 8);
    static Hex hex2 = Play.hexGraph.getHex(16, 16);
    static Force force1;
    static Force force2;

    static
    {

        Force regiment11 = new Force(new Battalion(FRANCE, hex), new Battalion(FRANCE, hex), new Battalion(FRANCE, hex));
        regiment11.name = "1.Regiment";
        Force regiment12 = new Force(new Battalion(FRANCE, hex), new Battalion(FRANCE, hex), new Battalion(FRANCE, hex));
        regiment12.name = "2.Regiment";
        Force regiment13 = new Force(new Battalion(FRANCE, hex), new Battalion(FRANCE, hex), new Battalion(FRANCE, hex));
        regiment13.name = "3.Regiment";
        Force regiment14 = new Force(new Battalion(FRANCE, hex), new Battalion(FRANCE, hex), new Battalion(FRANCE, hex));
        regiment14.name = "4.Regiment";
        Force brigade11 = new Force(regiment11, regiment12);
        brigade11.name = "1.Brigade";
        Force brigade12 = new Force (regiment13, regiment14);
        brigade12.name = "2.Brigade";
        force1 = new Force(brigade11, brigade12);
        force1.attach(new Squadron(FRANCE, hex));
        force1.attach(new Squadron(FRANCE, hex));
        Force artillery1 = new Force(new Battery(FRANCE, hex), new Battery(FRANCE, hex), new Battery(FRANCE, hex), new Battery(FRANCE, hex));
        artillery1.name = "Artillery Division";
        force1.attach(artillery1);
        force1.attach(new Wagon(FRANCE, hex));
        force1.attach(new Wagon(FRANCE, hex));
        force1.attach(new Wagon(FRANCE, hex));
        force1.name = "French division";
        force1.order.seekBattle = true;
        force1.order.retreatLevel = 0.7;

        Force regiment21 = new Force(new Battalion(AUSTRIA, hex2), new Battalion(AUSTRIA, hex2), new Battalion(AUSTRIA, hex2));
        regiment21.name = "Regiment-1";
        Force regiment22 = new Force(new Battalion(AUSTRIA, hex2), new Battalion(AUSTRIA, hex2), new Battalion(AUSTRIA, hex2));
        regiment22.name = "Regiment-2";
        Force regiment23 = new Force(new Battalion(AUSTRIA, hex2), new Battalion(AUSTRIA, hex2), new Battalion(AUSTRIA, hex2));
        regiment23.name = "Regiment-3";
        Force regiment24 = new Force(new Battalion(AUSTRIA, hex2), new Battalion(AUSTRIA, hex2), new Battalion(AUSTRIA, hex2));
        regiment24.name = "Regiment-4";
        Force brigade21 = new Force(regiment21, regiment22);
        brigade21.name = "Brigade-1";
        Force brigade22 = new Force (regiment23, regiment24);
        brigade22.name = "Brigade-2";
        force2 = new Force(brigade21, brigade22);
        force2.attach(new Squadron(AUSTRIA, hex2));
        force2.attach(new Squadron(AUSTRIA, hex2));
        Force artillery2 = new Force(new Battery(AUSTRIA, hex2), new Battery(AUSTRIA, hex2), new Battery(AUSTRIA, hex2), new Battery(AUSTRIA, hex2));
        artillery2.name = "Austrian Artillery";
        force2.attach(artillery2);
        Force wagons = new Force(new Wagon(AUSTRIA, hex2), new Wagon(AUSTRIA, hex2), new Wagon(AUSTRIA, hex2));
        wagons.name = "Supply Train";
        force2.attach(wagons);
        force2.name = "Austrian Division";
        force2.order.seekBattle = true;
        force2.order.retreatLevel = 0.7;
    }

    public static void main(String[] args) {

        Force france = createForce(FRANCE,12, 2, 4);
        Force austria = createForce(AUSTRIA, 12, 2, 4);
        //Force austria = new Squadron(AUSTRIA, hex);
        france.order = new Order(true, 0.4, 0);
        austria.order = new Order(true, 0.4, 0);
        //Force f = new Force(new Battalion(FRANCE, hex), new Battalion(FRANCE, hex));
        //Force w = new Wagon(FRANCE, hex);
        //france.attach(f);
        /*Force france = new Force(new Battalion(FRANCE, hex, 50), new Battalion(FRANCE, hex, 50),
                new Battalion(FRANCE, hex, 50), new Battalion(FRANCE, hex, 50), new Battalion(FRANCE, hex, 50));*/
        //france.attach(f);
        //france.attach(w);
        //austria.attach(new Battalion(AUSTRIA, hex));
        //austria.attach(new Squadron(AUSTRIA, hex));

        /*System.out.println("Before the battle");
        System.out.println();
        list(france);
        list(austria);
        System.out.println();*/

        //double a = france.unloadAmmo();
        //System.out.println("UNLOADED: " + a);
        //double n = france.loadAmmo(INFANTRY, CAVALRY);
        //System.out.println("LOADED: "+ n);
        //france.distributeAmmo(15.0);

        /*Battle battle = new Battle(france, austria);


        //getStat(france, austria);
        //battle.longDistanceBombing();


        battle.resolve();
        //battle.resolveStage();
        //battle.resolveStage();
        //battle.resolveStage();
        //battle.resolveStage();
        //battle.resolveStage();
        System.out.println("After the battle");
        System.out.println();
        list(france);
        list(austria);*/

        /*Battle battle = new Battle(force1, force2);
        battle.resolve();
        getStat(force1, force2);*/
        //getStat(france, austria);
        //UnitType.combatByAmmo();
        System.out.println(Unit.UNITS_BY_SPEED[0]);
    }

    public static void getStat(Force attacker, Force defender) {
        int a = 0;
        int d = 0;
        int n = 0;
        for (int i = 0; i < 100; i++) {
            Force att = createForce(attacker.nation, attacker.battalions.size(), attacker.squadrons.size(), attacker.batteries.size(), attacker.morale);
            att.order = new Order(attacker.order.seekBattle, attacker.order.retreatLevel, 0);
            Force def = createForce(defender.nation, defender.battalions.size(), defender.squadrons.size(), defender.batteries.size(), defender.morale);
            def.order = new Order(defender.order.seekBattle, defender.order.retreatLevel, 0);


            Battle battle = new Battle(att, def);

            //Battle battle = new Battle(force1, force2);
            int r = battle.resolve();

            if (r == 1) a++;
            else
                if (r == -1) d++;

            else n++;
        }

        System.out.println("Attacker wins = " + a + " Defender wins = " + d + " Without battle " + n);
    }

    static void list(Force force) {
        System.out.println(force.name);
        System.out.println("Totally soldiers: " + force.strength + ", Morale level: " + force.morale + ", Fatigue: " + force.fatigue + " speed: " + force.getForceSpeed() +
                " AMMO: " + force.ammoStock + " FOOD: " + force.foodStock +" foodNeed: "+ force.foodNeed + " foodLimit " + force.foodLimit + " fire : " + force.fire + " charge: " +
                force.charge);
        System.out.println("Including: ");
        System.out.println();
        for (Force f: force.forces) {
            if (f.isUnit) {
                System.out.println("    " + f.name + ": " + f.strength + " soldiers, Morale level: " + f.morale + ". Fatigue: " + f.fatigue + " speed: " + f.speed +
                        " AMMO: " + f.ammoStock + " FOOD: " + f.foodStock + " Food Need " + f.foodNeed + " foodlimit " + f.foodLimit + " fire: " + f.fire + " charge: " + f.charge);
            }
            else {
                list(f);
            }
        }
        System.out.println();
    }

    private static Force createForce(Nation nation, int i, int c, int a) {
        int b = 1;
        int s = 1;
        int y = 1;
        Force force = new Force(nation, hex);
        force.name = "Corps " + nation;
        for (int count = 0; count < i; count++) {
            Unit u = new Battalion(nation, hex);
            u.name = b++ +". Battalion";

            force.attach(u);
        }
        for (int count = 0; count < c; count++) {
            Unit u =  new Squadron(nation, hex);
            u.name = s++ + ". Squadron";

            force.attach(u);
        }
        for (int count = 0; count < a; count++) {
            Unit u = new Battery(nation, hex);
            u.name = y++ + ". Battery";
            force.attach(u);
        }

        return force;
    }
    private static Force createForce(Nation nation, int i, int c, int a, double m) {
        int b = 1;
        int s = 1;
        int y = 1;
        Force force = new Force(nation, hex);
        force.name = "Corps " + nation;
        for (int count = 0; count < i; count++) {
            Unit u = new Battalion(nation, hex);
            u.name = b++ +". Battalion";
            u.morale = m;
            force.attach(u);
        }
        for (int count = 0; count < c; count++) {
            Unit u =  new Squadron(nation, hex);
            u.name = s++ + ". Squadron";
            u.morale = m;
            force.attach(u);
        }
        for (int count = 0; count < a; count++) {
            Unit u = new Battery(nation, hex);
            u.name = y++ + ". Battery";
            u.morale = m;
            force.attach(u);
        }

        return force;
    }
}
