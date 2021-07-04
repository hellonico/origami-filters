package origami;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;

import java.util.List;
import java.util.stream.Collectors;

public class FindFilters {
    public static List<String> find(String _interface) {
        try (
//                ScanResult scanResult = new ClassGraph().enableAllInfo().whitelistPackages("origami")
//                .scan())
                ScanResult scanResult = new ClassGraph().enableAllInfo().scan())
        {
            ClassInfoList widgetClasses = scanResult.getClassesImplementing(_interface);
            return widgetClasses.getNames().stream().filter(k -> {
                try {
                    return Class.forName(k).getConstructor()!=null;
                } catch (Exception e) {
                    return false;
                }
            }).collect(Collectors.toList());
        }
    }
    public static List<String> findFilters() {
       return find("origami.Filter");
    }
    public static List<String> findDetectors() {
        return find("origami.Detect");
    }

    public static void main(String... args) {
        System.out.println("Filters");
        for(String s : findFilters())  System.out.println(s);
        System.out.println("Detectors");
        for(String s : findDetectors())  System.out.println(s);
    }
}
