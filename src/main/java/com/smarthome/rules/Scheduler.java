package com.smarthome.rules;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class Scheduler {
    private List<TimeRule> rules = new ArrayList<>();

    public Scheduler() {
    }
    public void addRule(TimeRule rule){
        rules.add(rule);
    }

    public void removeRule(TimeRule rule){
        rules.remove(rule);
    }

    public List<TimeRule> getRules(){return rules;}


    public void checkSchedule(String currentTime){
        for(TimeRule rule : rules){

            rule.evaluate(currentTime);
        }
    }
}
