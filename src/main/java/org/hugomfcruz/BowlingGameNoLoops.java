package org.hugomfcruz;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.stream.IntStream;

public class BowlingGameNoLoops implements IBowlingGame {
    private Deque<Integer> rolls = new ArrayDeque<>();

    @Override
    public void roll(int pins) {
        rolls.add(pins);
    }

    @Override
    public int getScore() {
        return IntStream.range(0, 10) // 10 rounds
                        .map(i -> readFromQueue())
                        .sum();

    }

    /**
     * Calculates the score for each round.
     * Side-effect: consumes elements from the queue.
     * @return the score for the round on the head of the queue.
     */
    private int readFromQueue() {
        int score1 = rolls.pop();
        int score2 = rolls.pop();

        // Strike
        if(score1 == 10){

            // 10 + 2 next rolls
            int score3 = rolls.element();
            rolls.addFirst(score2); // put back second roll
            return score1 + score2 + score3;

        // Spare
        } else if (score1 + score2 == 10) {

            // 10 + next roll
            int score3 = rolls.element();
            return score1 + score2 + score3;

        //Split
        } else {
            return score1 + score2;

        }
    }
}
