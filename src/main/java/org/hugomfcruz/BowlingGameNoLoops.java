package org.hugomfcruz;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BowlingGameNoLoops implements IBowlingGame {
    private int[] rolls = new int[21];
    private int currentRoll = 0;

    @Override
    public void roll(int pins) {
            rolls[currentRoll++] = pins;
    }

    @Override
    public int getScore() {

        List<Score> listOfScores =
                IntStream
                        .range(0, rolls.length)
                        .mapToObj(this::scoreFactory)
                        .collect(Collectors.toList());

        List<Score> filteredScores =
                IntStream
                        .range(0, listOfScores.size())
                        .filter(i -> isValid(i, listOfScores))
                        .mapToObj(listOfScores::get)
                        .collect(Collectors.toList());

        List<Score> filteredScoresWithBonusAdjustments =
                IntStream
                        .range(0, 10)
                        .mapToObj(filteredScores::get)
                        .collect(Collectors.toList());

        return filteredScoresWithBonusAdjustments.stream()
                        .map(Score::getScore)
                        .reduce(0, Integer::sum);
    }

    private boolean isValid(int rollIndex, List<Score> listOfScores) {
        long numberOfCurrentStrikes = listOfScores.subList(0, rollIndex).stream().filter(s -> s instanceof Strike).count();
        long adjustmentForStrikes = numberOfCurrentStrikes % 2;

        return  (rollIndex + adjustmentForStrikes) % 2 == 0 ||      // every other non-strike counts, with adjustment for the strikes previously scored.
                listOfScores.get(rollIndex - 1) instanceof Strike;  // Strikes always count on their own
    }

    private Score scoreFactory(int rollIndex) {
        int rollScore1 = getScoreForRoll(rollIndex);
        int rollScore2 = getScoreForRoll(rollIndex + 1);
        int rollScore3 = getScoreForRoll(rollIndex + 2);

        Score score;
        if(rollScore1 == 10){
            score = new Strike();
        } else if(rollScore1 + rollScore2 == 10){
            score = new Spare();
        } else {
            score = new Split();
        }

        return score.pushResult(rollScore1)
                    .pushResult(rollScore2)
                    .pushResult(rollScore3);
    }

    private int getScoreForRoll(int rollIndex){
        return rollIndex < rolls.length ? rolls[rollIndex] : 0;
    }

    private abstract class Score {
        protected int[] results = new int[3];
        private int numberOfResults;

        public abstract int getScore();

        protected Score pushResult(int result) {
            results[numberOfResults] = result;
            numberOfResults++;
            return this;
        }
    }

    private class Strike extends Score {
        @Override
        public int getScore() {
            return 10 + results[1] + results[2];
        }
    }

    private class Spare extends Score {
        @Override
        public int getScore() {
            return 10 + results[2];
        }
    }

    private class Split extends Score {
        @Override
        public int getScore() {
            return results[0] + results[1];
        }
    }
}
