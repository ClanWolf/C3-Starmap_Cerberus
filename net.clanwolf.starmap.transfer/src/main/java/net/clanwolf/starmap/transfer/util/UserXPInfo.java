package net.clanwolf.starmap.transfer.util;

public class UserXPInfo {
    private static final double BASE_XP = 300;
    private static final double EXPONENT = 1.04f;
    private final int playerXP;

    private static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            double levelXp = calcXpForLevel(i);
            double max = calculateFullTargetXp(i);
            System.out.printf("Level: %3d, xp for the next level: %10.2f, next level xp: %10.2f%n", i, levelXp, max);
        }
        System.out.println("====================");
        int xp = 14590;
        System.out.println("For " + xp + " xp  calculated level is " + calculateLevel(xp));
    }

    private static double calcXpForLevel(int level) {
        return BASE_XP + (BASE_XP * Math.pow(level, EXPONENT));
    }

    public UserXPInfo(int xp) {
        playerXP = xp;
    }

    public int getPlayerLevel() {
        return calculateLevel(playerXP);
    }

    private static double calculateFullTargetXp(int level) {
        double requiredXP = 0;
        for (int i = 0; i <= level; i++) {
            requiredXP += calcXpForLevel(i);
        }
        return requiredXP;
    }

    public static int calculateLevel(double xp) {
        int level = 0;
        double maxXp = calcXpForLevel(0);
        do {
            maxXp += calcXpForLevel(++level);
        } while (maxXp < xp);
        return level;
    }
}
