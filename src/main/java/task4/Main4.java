package task4;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class Main4 {
    private static final String DELIMITER = "-";
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Incorrect number of arguments");
            return;
        }

        String path = args[0];
        Map<String, String> sortedMap = new TreeMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.contains(DELIMITER)) {
                    continue;
                }
                int i = line.indexOf(DELIMITER);
                String key = line.substring(0, i);
                sortedMap.put(key, line);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        for (Map.Entry<String, String> e : sortedMap.entrySet()) {
            System.out.println(e.getValue());
        }
    }
}
