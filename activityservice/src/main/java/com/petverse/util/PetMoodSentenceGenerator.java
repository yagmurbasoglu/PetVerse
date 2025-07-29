package com.petverse.util;

import com.petverse.dto.PetDailyStats;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PetMoodSentenceGenerator {

    public static String toSentence(PetDailyStats stats) {
        int feed = stats.getFoodCount();
        int walk = stats.getWalkCount();
        int play = stats.getPlayCount();
        int run = stats.getRunCount();
        int sleep = stats.getSleepCount();
        int bath = stats.getBathCount();

        StringBuilder sb = new StringBuilder("Today, my pet ");

        List<String> phrases = new ArrayList<>();

        if (feed >= 2) {
            phrases.add("was well fed");
        } else if (feed == 1) {
            phrases.add("ate a bit but is still hungry");
        } else {
            phrases.add("is hungry and starving");
        }

        if (walk >= 1) {
            phrases.add("went outside for a walk");
        } else {
            phrases.add("stayed inside all day without fresh air");
        }

        if (play >= 1) {
            phrases.add("played and had some fun");
        } else {
            phrases.add("was lonely and bored");
        }

        if (run >= 1) {
            phrases.add("was energetic and active");
        } else {
            phrases.add("was slow and inactive");
        }

        if (sleep >= 1) {
            phrases.add("got enough rest");
        } else {
            phrases.add("was tired and restless");
        }

        if (bath >= 1) {
            phrases.add("was clean and fresh");
        } else {
            phrases.add("was dirty and smelly");
        }

        sb.append(String.join(", ", phrases)).append(".");

        return sb.toString();
    }
}
