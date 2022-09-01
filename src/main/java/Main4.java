import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class Main4 {
    private static final String DELIMITER = "-";
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Incorrect number of args");
            return;
        }

        String path = args[0];
        SortedMap<String, String> map = new TreeMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.contains(DELIMITER)) {
                    continue;
                }
                int i = line.indexOf(DELIMITER);
                String key = line.substring(0, i);
                map.put(key, line);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        for (Map.Entry<String, String> e : map.entrySet()) {
            System.out.println(e.getValue());
        }

    }
}
