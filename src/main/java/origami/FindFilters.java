package origami;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import origami.annotations.Parameter;
import origami.annotations.Usage;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
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
            String version = loadVersion();
            bw.write(";; Origami Filters EDN: "+version);
            bw.newLine();
            bw.write(";; Generated: "+new Date().toString());
            bw.newLine();
            bw.write("[\n");
            for (String f : filters) {
                try {
                    Class<?> _f = Class.forName(f);
                    if (Modifier.isAbstract(_f.getModifiers())) {
                        continue;
                    }

                    Filter __f = (Filter) _f.newInstance();

                    Object[] us = Arrays.stream(_f.getAnnotations()).filter(a -> a.annotationType().equals(Usage.class)).toArray();
                    for (Object u : us) {
                        bw.append(";");
                        bw.append(((Usage) u).description());
                        bw.newLine();
                    }

                    for (Field field : _f.getDeclaredFields()) {
                        field.setAccessible(true);
                        if (field.isAnnotationPresent(Parameter.class)) {
                            String description = ((Parameter) field.getAnnotationsByType(Parameter.class)[0]).description();
                            bw.append(";");
                            bw.append(description);
                            bw.newLine();
                        }
                    }

                    bw.append(";;");
                    bw.write(Origami.FilterToString(__f));
                    bw.newLine();
//                    bw.append(";; Spped")
                    bw.newLine();
                } catch (Exception e) {
                    //e.printStackTrace();
                    System.out.println("FAILED:" + f.getClass() + "{" + f + "}");
                }
            }
            bw.newLine();
            bw.write("]\n");
            bw.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static String outputDate() {
        // Get the current date and time
        LocalDateTime now = LocalDateTime.now();

        // Define a custom date-time formatter
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy 'at' h:mm a");

        // Format the date-time using the formatter
        return now.format(formatter);
    }

    static String getFilterName(Filter f) {
        return getFilterName(f.getClass().getName());
    }
    static String getFilterName(String f) {
        return f.replace("origami.filters.", "").replace("$", ".").toLowerCase();
    }

    public static void generateOverview(String outputDir, String matPath) {
        List<String> filters = FindFilters.findFilters();
        Collections.sort(filters);
        int size = filters.size();

        try {
            FileWriter fw2 = new FileWriter(outputDir + "/overview.md");
            BufferedWriter bw2 = new BufferedWriter(fw2);
            Mat m = Imgcodecs.imread(matPath, Imgcodecs.IMREAD_REDUCED_COLOR_8);
            Size s = m.size();

            int i = 0;
            bw2.write("<html>\n" +
                    "<head>\n" +
                    "  <style>\n" +
                    "    .row {\n" +
                    "  display: flex;\n" +
                    "  flex-wrap: wrap;\n" +
                    "  padding: 0 4px;\n" +
                    "}\n" +
                    "\n" +
                    "/* Create two equal columns that sits next to each other */\n" +
                    ".column {\n" +
                    "  flex: 50%;\n" +
                    "  padding: 0 4px;\n" +
                    "}\n" +
                    "\n" +
                    ".column img {\n" +
                    "  height: 50%;\n" +
                    "  width: 50%;\n" +
                    "  margin-top: 8px;\n" +
                    "  vertical-align: middle;\n" +
                    "}\n" +
                    "img {\n" +
                    "  border-radius: 8px;\n" +
                    "    height: 128px;\n" +
                    "    width: 120px;\n" +
                    "}" +
                    "</style>\n" +
                    "</head>");
            bw2.newLine();
            bw2.write("<div class=\"row\">");
            bw2.newLine();

            bw2.write("<div class=\"column\">");
            bw2.newLine();
            for (String f : filters) {

//                Class<?> _f = Class.forName(f);
//                Filter __f = (Filter) _f.newInstance();
                String filterName = getFilterName(f);
                System.out.printf("Loading: %s\n", filterName);
                String imageFile = filterName+".png";
                if(!new File(outputDir+"/"+imageFile).exists()) {
                    System.out.println("Missing:"+outputDir+"/"+imageFile);
                    imageFile = "original.png";
                }

//                if (i % 7 == 0) {
//                    bw2.write("<div class=\"column\">");
//                    bw2.newLine();
//                }
                bw2.write(String.format("<a href=\"#/filters/filters?id=%s\"><img title=\"%s\" alt=\"%s\" src=\"filters/%s\"></a>", filterName, filterName, filterName, imageFile));
                bw2.newLine();
//                if (i % 7 == 6) {
//                    bw2.write("</div>");
//                    bw2.newLine();
//                }

                i++;
                bw2.flush();
            }
            bw2.write("</div>");
            bw2.newLine();

            bw2.write("</div>");
            bw2.newLine();
            bw2.write("</html>");
            bw2.newLine();
            bw2.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e2) {
            e2.printStackTrace();
        }

    }

    public static void generateFilterDoc(String outputDir, String matPath) {
        System.out.printf("Generating filters documentation: %s\n", outputDate());
        try {
            List<String> filters = FindFilters.findFilters();
            Collections.sort(filters);
            if (!new File(outputDir).exists()) {
                new File(outputDir).mkdirs();
            }
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
                    String filterName = getFilterName(__f);
                    System.out.printf("Loading: %s\n", filterName);

                    Mat c = m.clone();

                    long startTime = System.currentTimeMillis();
                    Mat d = __f.apply(c);
                    long endTime = System.currentTimeMillis();

                    bw.write("# " + filterName);
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
                    bw.newLine();
                    bw.write("![](" + filterName + ".png" + ")");
                    bw.newLine();
                    bw.newLine();
                    bw.write(String.format("Execution Time: %d ms", endTime - startTime));
                    bw.newLine();
                    bw.newLine();

                    bw.flush();

                    Imgcodecs.imwrite(outputDir + "/" + filterName + ".png", d);
                    System.out.printf("Filter %s executed in %d ms\n", filterName, endTime - startTime);

                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("> Check:" + f);
                }

            }

            bw.close();
            System.out.printf("Finished: %s\n", outputDate());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void generateFilterDoc() {
        String matPath = this.getClass().getClassLoader().getResource("marcel.jpg").getPath();
        generateFilterDoc("output", matPath);
    }

    public static String loadVersion() {
        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream("gradle.properties"));
            return (String) prop.get("version");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void main(String... args) {
        System.out.printf("Loading filters version: %s\n", loadVersion());
        Origami.init();
        String output = args.length >= 1 ? args[0] : "output";
        String matPath = args.length >= 2 ? args[1] : FindFilters.class.getClassLoader().getResource("marcel.jpg").getPath();
//        generateFilterDoc(output, matPath);
//        generateOverview(output, matPath);

    }

    public static class GenerateDoc {
        public static void main(String... args) {
            System.out.printf("Loading filters version: %s\n", loadVersion());
            Origami.init();
            String output = args.length >= 1 ? args[0] : "output";
            String matPath = args.length >= 2 ? args[1] : FindFilters.class.getClassLoader().getResource("marcel.jpg").getPath();
//        generateFilterDoc(output, matPath);
        }
    }

    public static class GenerateEdn {
        public static void main(String... args) {
            System.out.printf("Loading filters version: %s\n", loadVersion());
            Origami.init();
            String output = args.length >= 1 ? args[0] : "output";
            generateEDNWithAllFilters(output + "/filters.edn");
        }
    }
}
