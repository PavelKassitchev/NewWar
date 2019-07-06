package com.pavka;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;


public class Battle {
    public static final int FIRE_ON_UNIT = 40;
    public static final double CASUALITY_INTO_MORALE = 3.3;
    public static final int CHARGE_ON_ENEMY = 30;
    public static final int PURSUIT_CHARGE = 45;
    public static final int MIN_SOLDIERS = 6;
    public static final double MIN_MORALE = 0.2;
    public static final double MORALE_PENALTY = -0.03;
    public static final double MORALE_BONUS = 0.03;
    public static final double SMALL_MORALE_BONUS = 0.02;
    public static final double VICTORY_BONUS = 0.5;
    public static final double SMALL_VICTORY_BONUS = 0.2;
    public static final double LONG_DISTANCE_FIRE = 0.65;


    Force attacker;
    Force defender;
    int attackerInit;
    int defenderInit;
    double defenderBonus;
    int winner = 0;
    HashSet<Unit> rootedDef = new HashSet<Unit>();
    HashSet<Unit> rootedAtt = new HashSet<Unit>();

    int attackerKilled;
    int defenderKilled;
    int attackerImprisoned;
    int defenderImprisoned;

    public Battle(Force force1, Force force2) {

        if (force2.order.seekBattle && (!force1.order.seekBattle || force2.strength > force1.strength)) {

            attacker = force2;
            defender = force1;
        } else {
            attacker = force1;
            defender = force2;
        }

        attackerInit = attacker.strength;
        defenderInit = defender.strength;
        //This simply to make chances more equal
        defenderBonus = getBonus(defender);
    }

    public double getBonus(Force force) {
        int units = force.battalions.size() + force.batteries.size() + force.squadrons.size();
        if (units == 0) units = 1;
        if (units > 20) return 1;
        return 1.0045 - 0.1253 / units;

    }

    public int pursuit(int rooted) {
        int prisoners = 0;
        double prisonnedShare;
        if (winner == 1) {
            if (PURSUIT_CHARGE * attacker.charge > rooted) {
                prisonnedShare = 0.9;
            } else {
                prisonnedShare = 0.9 * PURSUIT_CHARGE * attacker.charge / rooted;
            }
            for (Unit unit : rootedDef) {
                prisoners += unit.strength * prisonnedShare * unit.imprisoned;
                unit.bearLoss(prisonnedShare * unit.imprisoned);
            }
        }
        if (winner == -1) {
            if (PURSUIT_CHARGE * defender.charge > rooted) {
                prisonnedShare = 0.9;
            } else {
                prisonnedShare = 0.9 * PURSUIT_CHARGE * defender.charge / rooted;
            }
            for (Unit unit : rootedAtt) {
                prisoners += unit.strength * prisonnedShare * unit.imprisoned;
                unit.bearLoss(prisonnedShare * unit.imprisoned);
            }
        }
        return prisoners;
    }

    public int resolve() {

        System.out.println("The battle begins!");
        //Test.list(attacker);
        //Test.list(defender);

        if (!attacker.order.seekBattle && !defender.order.seekBattle) return 0;
        if (!attacker.order.seekBattle || !defender.order.seekBattle) {
            Random random = new Random();
            if (random.nextBoolean()) return 0;
        }
        String s;
        int count = 0;
        longDistanceBombing();
        System.out.println("Bombing");
        while (winner == 0) {
            s = resolveStage();
            count++;
        }

        int attackerRooted = 0;
        int defenderRooted = 0;


        if (winner == 1) {
            for (Unit unit : rootedDef) {
                defenderRooted += unit.strength;
                unit.retreat();
            }

            //System.out.println("Attacker took prisoners: " + pursuit(defenderRooted));
            defenderImprisoned = pursuit(defenderRooted);
            if (attacker.isUnit) {
                ((Unit) attacker).changeMorale(VICTORY_BONUS);
            } else {
                for (Battalion unit : attacker.battalions) unit.changeMorale(VICTORY_BONUS);
                for (Squadron unit : attacker.squadrons) unit.changeMorale(VICTORY_BONUS);
                for (Battery unit : attacker.batteries) unit.changeMorale(VICTORY_BONUS);
            }
            for (Unit unit : rootedAtt) {
                unit.changeMorale(SMALL_VICTORY_BONUS);
                if (unit.morale > 0) {
                    //TODO what if the unit retreated?
                    attacker.attach(unit);

                } else {
                    attackerRooted += unit.strength;
                    //unit.retreat();
                }
            }

        } else {
            for (Unit unit : rootedAtt) {
                attackerRooted += unit.strength;
                unit.retreat();;
            }
            //System.out.println("Defender took prisoners: " + pursuit(attackerRooted));
            attackerImprisoned = pursuit(attackerRooted);
            if (defender.isUnit) {
                ((Unit) defender).changeMorale(VICTORY_BONUS);
            } else {
                for (Battalion unit : defender.battalions) unit.changeMorale(VICTORY_BONUS);
                for (Squadron unit : defender.squadrons) unit.changeMorale(VICTORY_BONUS);
                for (Battery unit : defender.batteries) unit.changeMorale(VICTORY_BONUS);
            }
            for (Unit unit : rootedDef) {
                unit.changeMorale(SMALL_VICTORY_BONUS);
                if (unit.morale > 0) {
                    //TODO what if the unit retreated
                    defender.attach(unit);

                } else {
                    defenderRooted += unit.strength;
                    //unit.retreat();
                }
            }
        }

        System.out.println("The battle ends!");
        System.out.println("WINNER = " + winner);
        System.out.println("Attacker: imprisoned - " + attackerImprisoned + " , killed and wounded - " + attackerKilled);
        System.out.println("Defender: imprisoned - " + defenderImprisoned + " , killed and wounded - " + defenderKilled);
        System.out.println("Attacker left: " + attacker.strength + " routed: " + (attackerRooted - attackerImprisoned));
        System.out.println("Defender left: " + defender.strength + " routed: " + (defenderRooted - defenderImprisoned));

        Test.list(attacker);
        Test.list(defender);;
        System.out.println("Number of rounds: " + count);

        return winner;
    }

    public int hitUnit(Unit unit, double fire, double charge) {
        int in = unit.strength;
        Force opponent = (unit.nation == attacker.nation) ? defender : attacker;
        HashSet<Unit> rooted = (unit.nation == attacker.nation) ? rootedAtt : rootedDef;

        unit.bearLoss(fire);
        int out = unit.strength;
        unit.changeMorale(charge);
        if (unit.strength <= MIN_SOLDIERS || unit.morale <= MIN_MORALE) {

            rooted.add(unit);
            opponent.selectRandomUnit().changeMorale(MORALE_BONUS);

            if (unit.isSub) {
                for (Force force : unit.superForce.forces) {
                    if (force.isUnit && force != unit) {
                        ((Unit) force).changeMorale(MORALE_PENALTY);
                        if (force.morale <= MIN_MORALE) rooted.add((Unit) force);
                        opponent.selectRandomUnit().changeMorale(SMALL_MORALE_BONUS);
                    }
                }
            }
        }
        return in - out;

    }

    public int longDistanceBombing() {
        int casualities = 0;
        Random random = new Random();

        ArrayList<Unit> attackerUnits = new ArrayList<Unit>();
        if (!attacker.isUnit) {

            attackerUnits.addAll(attacker.battalions);
            attackerUnits.addAll(attacker.batteries);
            attackerUnits.addAll(attacker.squadrons);
        } else {
            attackerUnits.add((Unit) attacker);
        }


        for (Unit unit : attackerUnits) {
            int initStrength = unit.strength;
            double ratio = (double) initStrength / attackerInit;

            if (defender.isUnit && ((Unit) defender).type == Unit.ARTILLERY) {
                Battery b = (Battery) defender;
                b.fire(ratio);
                double fireEffect = LONG_DISTANCE_FIRE * ((0.7 + 0.6 * random.nextDouble() * b.fire * FIRE_ON_UNIT) / attackerInit);
                attackerKilled += hitUnit(unit, fireEffect, (-CASUALITY_INTO_MORALE * fireEffect));
            } else {
                for (Battery b : defender.batteries) {
                    b.fire(ratio);
                    System.out.println("Bombing!!!");
                    double fireEffect = LONG_DISTANCE_FIRE * ((0.7 + 0.6 * random.nextDouble() * b.fire * FIRE_ON_UNIT) / attackerInit);
                    attackerKilled += hitUnit(unit, fireEffect, (-CASUALITY_INTO_MORALE * fireEffect));
                }
            }
        }
        if (rootedAtt.size() > 0) {

            for (Unit unit : rootedAtt) {
                if (unit.isSub) unit.superForce.detach(unit);
                unit.order.retreatLevel = 0.95;
                unit.retreat();
                if (unit.strength <= MIN_SOLDIERS) unit.disappear();

            }
            if (attacker.strength <= attackerInit * attacker.order.retreatLevel) {
                winner = -1;
                attacker.retreat();
                if (attacker.strength <= MIN_SOLDIERS) attacker.disappear();

            }
        }
        casualities = attackerKilled;
        System.out.println("LONG BOMBING CASUALITIES: " + casualities);
        return casualities;
    }


    public String resolveStage() {

        double fireOnDefender = FIRE_ON_UNIT * defenderBonus * attacker.fire / defender.strength;

        double fireOnAttacker = FIRE_ON_UNIT * defender.fire / attacker.strength;

        double chargeOnDefender = -(CASUALITY_INTO_MORALE * fireOnDefender + CHARGE_ON_ENEMY * attacker.charge / defender.strength);

        double chargeOnAttacker = -(CASUALITY_INTO_MORALE * fireOnAttacker + CHARGE_ON_ENEMY * defender.charge / attacker.strength);

        int initAtt = attacker.strength;
        int initDef = defender.strength;


        Random random = new Random();

        StringBuilder result = new StringBuilder("Victory of ");


        ArrayList<Unit> attackerUnits = new ArrayList<Unit>();
        if (!attacker.isUnit && attacker.forces.size() > 0) {
            attackerUnits.addAll(attacker.battalions);
            attackerUnits.addAll(attacker.batteries);
            attackerUnits.addAll(attacker.squadrons);
        } else {
            attackerUnits.add((Unit) attacker);
        }

        ArrayList<Unit> defenderUnits = new ArrayList<Unit>();
        if (!defender.isUnit && defender.forces.size() > 0) {
            defenderUnits.addAll(defender.battalions);
            defenderUnits.addAll(defender.batteries);
            defenderUnits.addAll(defender.squadrons);
        } else {
            defenderUnits.add((Unit) defender);
        }

        //TODO what if min = 0?

        int min = Math.min(attackerUnits.size(), defenderUnits.size());
        if (min == 0) min = 1;

        int defenderStep = defenderUnits.size() / min;

        int attackerStep = attackerUnits.size() / min;


        Iterator<Unit> defIterator = defenderUnits.iterator();
        Iterator<Unit> attIterator = attackerUnits.iterator();


        while (defIterator.hasNext() || attIterator.hasNext()) {

            for (int i = 0; i < defenderStep; i++) {
                if (defIterator.hasNext()) {
                    Unit b = defIterator.next();
                    double ratio = (double) b.strength / initDef;
                    double fluke = 0.7 + 0.6 * random.nextDouble();
                    //fluke = 1;
                    defenderKilled += hitUnit(b, fluke * fireOnDefender, fluke * chargeOnDefender);
                    for (Unit unit : attackerUnits) unit.fire(ratio);
                }
            }
            if (rootedDef.size() > 0) {

                for (Unit unit : rootedDef) {
                    if (unit.isSub) unit.superForce.detach(unit);
                    unit.order.retreatLevel = 0.95;
                    //new
                    unit.retreat();
                    if (unit.strength <= MIN_SOLDIERS) unit.disappear();

                }
                if (defender.strength <= defenderInit * defender.order.retreatLevel) {
                    winner = 1;
                    //new
                    defender.retreat();
                    if (defender.strength <= MIN_SOLDIERS) defender.disappear();

                    return result.toString();
                }
            }
            for (int i = 0; i < attackerStep; i++) {
                if (attIterator.hasNext()) {
                    Unit b = attIterator.next();
                    double ratio = (double) b.strength / initAtt;
                    double fluke = 0.7 + 0.6 * random.nextDouble();
                    //fluke = 1;
                    attackerKilled += hitUnit(b, fluke * fireOnAttacker, fluke * chargeOnAttacker);
                    for (Unit unit : defenderUnits) unit.fire(ratio);
                }

            }

            if (rootedAtt.size() > 0) {

                for (Unit unit : rootedAtt) {
                    if (unit.isSub) {
                        System.out.println(unit.superForce.name + " lost a unit" + "play = " + unit.superForce.play);
                        unit.superForce.detach(unit);

                    }
                    unit.order.retreatLevel = 0.95;
                    //new
                    unit.retreat();
                    if (unit.strength <= MIN_SOLDIERS) unit.disappear();

                }
                if (attacker.strength <= attackerInit * attacker.order.retreatLevel) {
                    winner = -1;
                    //new
                    attacker.retreat();
                    if (attacker.strength <= MIN_SOLDIERS) attacker.disappear();

                }
            }

            if (winner != 0) break;

        }
        System.out.println("Attacker: " + attacker.name);
        Test.list(attacker);
        System.out.println("Defender: " + defender.name);
        Test.list(defender);


        return result.toString();
    }

}


