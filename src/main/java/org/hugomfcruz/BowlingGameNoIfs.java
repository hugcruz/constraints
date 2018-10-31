package org.hugomfcruz;

import java.util.HashMap;
import java.util.function.Function;

public class BowlingGameNoIfs implements IBowlingGame {
    private int[] rolls = new int[21];
    private int currentRoll = 0;

    private HashMap<Boolean, Function<Integer, Integer>> strikeCalculator;
    private HashMap<Boolean, Function<Integer, Integer>> spareCalculator;

    private HashMap<Boolean, Integer> advanceCalculator;

    BowlingGameNoIfs(){
        spareCalculator = new HashMap<>();
        spareCalculator.put(false, index -> rolls[index] + rolls[index + 1]);
        spareCalculator.put(true, index -> 10 + rolls[index + 2]);

        strikeCalculator = new HashMap<>();
        strikeCalculator.put(false, index -> spareCalculator.get(rolls[index] + rolls[index + 1] == 10).apply(index));
        strikeCalculator.put(true, index -> 10 + rolls[index + 1] + rolls[index + 2]);

        advanceCalculator = new HashMap<>();
        advanceCalculator.put(false, 2);
        advanceCalculator.put(true, 1);
    }



    @Override
    public void roll(int pins) {
            rolls[currentRoll++] = pins;
    }

    @Override
    public int getScore() {
        int score = 0;
        int frameIndex = 0;

        for (int frame = 0; frame < 10; frame++) {
            score += strikeCalculator.get(rolls[frameIndex] == 10).apply(frameIndex);
            frameIndex += advanceCalculator.get(rolls[frameIndex] == 10);
        }

        return score;
    }

}
