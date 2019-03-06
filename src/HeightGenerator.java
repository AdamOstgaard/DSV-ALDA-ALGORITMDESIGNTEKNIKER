import java.util.Random;

public class HeightGenerator {
    public static final int DEFAULT_MAX_HEIGHT = 8;

    private Random rand;

    private int maxHeight;

    private Integer[] ratios;

    public HeightGenerator(long seed) {
        this(seed, DEFAULT_MAX_HEIGHT);
    }

    public HeightGenerator(long seed, int maxHeight) {
        this.rand = new Random(seed);
        this.maxHeight = maxHeight;
    }
    
    public HeightGenerator(int maxHeight) {
        this.rand = new Random();
        this.maxHeight = maxHeight;
    }
    
    /**
     * @return the maxHeight
     */
    public int getMaxHeight() {
        return maxHeight;
    }

    /**
     * Randomizes a height with the probability for each level being half as likely as the previous one.
     * @return an int between 1 and the maxHeight.
     */
    public int getHeight() {
        int r = rand.nextInt((int)Math.pow(2, getMaxHeight()));
        for (int i = 0; i < maxHeight; i++) {
            if (r < Math.pow(2, i)) {
                return getMaxHeight() - i;
            }
        }
        return 1;
    }
}