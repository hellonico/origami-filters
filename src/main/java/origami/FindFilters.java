package origami;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class FindFilters {
    public static List<String> find(String _interface) {
        try (
//                ScanResult scanResult = new ClassGraph().enableAllInfo().whitelistPackages("origami")
//                .scan())
                ScanResult scanResult = new ClassGraph().enableAllInfo().scan()) {
            ClassInfoList widgetClasses = scanResult.getClassesImplementing(_interface);
            return widgetClasses.getNames().stream().filter(k -> {
                try {
                    return Class.forName(k).getConstructor() != null;
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

    public static void generateEDNWithAllFilters(String _path) {
        String path = _path == null ? System.getProperty("user.home") + "/Desktop/filters.edn" : _path;
        try {
            List<String> filters = FindFilters.findFilters();
            Collections.sort(filters);
            FileWriter fw = new FileWriter(path);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("[");
            for (String f : filters) {
                try {
                    Class<?> _f = Class.forName(f);
                    Filter __f = (Filter) _f.newInstance();

                    bw.newLine();
                    bw.append(";");
                    bw.write(Origami.FilterToString(__f));
                } catch (Exception e) {
                    System.out.println("Cannot load:" + f);
                }
            }
            bw.newLine();
            bw.write("]");
            bw.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void generateFilterDoc(String outputDir, String matPath) {
        try {
            List<String> filters = FindFilters.findFilters();
            Collections.sort(filters);
            FileWriter fw = new FileWriter(outputDir + "/filters.md");
            BufferedWriter bw = new BufferedWriter(fw);


            Mat m = Imgcodecs.imread(matPath, Imgcodecs.IMREAD_REDUCED_COLOR_8);

            Imgcodecs.imwrite(outputDir + "/original.png", m);
            bw.write("# Original");
            bw.newLine();
            bw.write("![](original.png" + ")");
            bw.newLine();

            for (String f : filters) {
                try {
                    Class<?> _f = Class.forName(f);
                    Filter __f = (Filter) _f.newInstance();
                    System.out.println("Loading:" + _f.getSimpleName());

                    Mat c = m.clone();
                    Mat d = __f.apply(c);

                    bw.write("# " + _f.getSimpleName());
                    bw.newLine();
                    bw.write("*Load with:*");
                    bw.newLine();
                    bw.write("```");
                    bw.newLine();
                    bw.write(Origami.FilterToString(__f));
                    bw.newLine();
                    bw.write("```");
                    bw.newLine();
                    bw.write("*Result:*");
                    bw.newLine();
                    bw.write("![](" + _f.getSimpleName() + ".png" + ")");
                    bw.newLine();

                    bw.flush();

                    Imgcodecs.imwrite(outputDir + "/" + _f.getSimpleName() + ".png", d);

                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("> Check:" + f);
                }

            }

            bw.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void generateFilterDoc() {
        String matPath = this.getClass().getClassLoader().getResource("marcel.jpg").getPath();
        generateFilterDoc("output", matPath);
    }

    public static void main(String... args) {
        String output = args.length >= 1 ? args[0] : "output";
        String matPath = args.length >= 2 ? args[1] : FindFilters.class.getClassLoader().getResource("marcel.jpg").getPath();
        generateFilterDoc(output,matPath);
        generateEDNWithAllFilters(output + "/filters.edn");
    }
}
