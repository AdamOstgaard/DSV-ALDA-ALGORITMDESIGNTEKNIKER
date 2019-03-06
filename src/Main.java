import java.util.ArrayList;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        SkipList<Integer> sl = new SkipList<>(8);

        Random rand = new Random();
        
        ArrayList<Integer> l = new ArrayList<>();

        for (int i = 100; i > 0; i--) {
            l.add(i);
        }

        sl.addRange(l);

        sl.remove(l.get(5));

        System.out.println(sl.toString());
    }
}