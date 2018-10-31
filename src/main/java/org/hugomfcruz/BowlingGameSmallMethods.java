package org.hugomfcruz;

public class BowlingGameSmallMethods implements IBowlingGame {
    private int[] rolls = new int[21];
    private int currentRoll = 0;

    @Override
    public void roll(int pins) {
            rolls[currentRoll++] = pins;
    }

    @Override
    public int getScore() {
        return calculateScores(0, 0);
    }

    private int calculateScores(int score, int rollIndex) {
        for (int frame = 0; frame < 10; frame++) {
            score = calculateFrameScore(score, rollIndex);
            rollIndex = calculateNextRollIndex(rollIndex);
        }

        return score;
    }

    private int calculateFrameScore(int score, int frameIndex) {
        score = strikeScore(score, frameIndex);
        score = spareScore(score, frameIndex);
        return splitScore(score, frameIndex);
    }

    private int strikeScore(int score, int rollIndex) {
        int strikeScore = isStrike(rollIndex) ? 10 + rolls[rollIndex + 1] + rolls[rollIndex + 2]: 0;
        return score + strikeScore;
    }

    private int spareScore(int score, int rollIndex) {
        int spareScore = isSpare(rollIndex) ? 10 + rolls[rollIndex + 2] : 0;
        return score + spareScore;
    }

    private int splitScore(int score, int rollIndex) {
        int splitScore = isSplit(rollIndex) ? rolls[rollIndex] + rolls[rollIndex + 1] : 0;
        return score + splitScore;
    }

    private int calculateNextRollIndex(int rollIndex) {
        if (isStrike(rollIndex)) {
            return rollIndex + 1;
        } else {
            return rollIndex + 2;
        }
    }

    private boolean isStrike(int rollIndex) {
        return rolls[rollIndex] == 10;
    }

    private boolean isSpare(int rollIndex) {
        return !isStrike(rollIndex) && rolls[rollIndex] + rolls[rollIndex + 1] == 10;
    }

    private boolean isSplit(int rollIndex) {
        return !isStrike(rollIndex) && !isSpare(rollIndex);
    }
}
