package org.hugomfcruz;

public class BowlingGameExponentialIndentation implements IBowlingGame {
    private int[] rolls = new int[21];
    private int currentRoll = 0;

    @Override
    public void roll(int pins) {
            rolls[currentRoll++] = pins;
    }

    @Override
    public int getScore() {
            int score = 0;
            int frameIndex = 0;

            for (int frame = 0; frame < 10; frame++) {
                            if (rolls[frameIndex] == 10) {
                                                            score += 10 + rolls[frameIndex + 1] + rolls[frameIndex + 2];
                                                            frameIndex++;
                            } else if (rolls[frameIndex] + rolls[frameIndex + 1] == 10) {
                                                            score += 10 + rolls[frameIndex + 2];
                                                            frameIndex += 2;
                            } else {
                                                            score += rolls[frameIndex] + rolls[frameIndex + 1];
                                                            frameIndex += 2;
                            }
            }

            return score;
    }

}
