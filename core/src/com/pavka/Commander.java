package com.pavka;

import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

public class Commander extends General {

    public Array<Report> sentReports = new Array<Report>();
    public Array<Report> receivedReports;

    public Commander(Nation nation, Hex hex) {
        super(nation, hex);
    }

    public void sendOrder(Order order, Force force) {
        Array<Path> paths = Play.navigate(hex, force.hex);
        int days = Path.getDaysToGo(paths, General.SPEED);
        order.issued = Play.turn;
        order.turn = Play.turn + days;
        if (force.message == null || force.message.issued < order.issued) force.message = order;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        getReports();
        getViews();
    }

    public void getReports() {

        for (Force force: Play.whiteTroops) {
            System.out.println(force.name);
            Array<Path> paths = Play.navigate(force.hex, hex);
            int days = Path.getDaysToGo(paths, General.SPEED);
            Report report = new Report(force, Play.turn + days);
            report.issued = Play.turn;
            for (Iterator<Report> iterator = sentReports.iterator(); iterator.hasNext();) {
                Report oldReport = iterator.next();
                if(oldReport.force == force && oldReport.turn >= report.turn) iterator.remove();
            }
            sentReports.add(report);
        }

    }
    public void getViews() {
        receivedReports = new Array<Report>();
        for (Iterator<Report> iterator = sentReports.iterator(); iterator.hasNext();) {
            Report report = iterator.next();
            if (report.turn == Play.turn) {
                receivedReports.add(report);
                System.out.println("REPORT ADDED! - " + report.force.name);
                iterator.remove();
            }
        }

    }
}
