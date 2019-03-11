import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        SkipList<Integer> sl = new SkipList<>(8);
        
        ArrayList<Integer> l = new ArrayList<>();

        for (int i = 100; i > 0; i--) {
            l.add(i);
        }

        sl.addAll(l);

        sl.remove(l.get(5));

        System.out.println(sl.toString());
    }
}