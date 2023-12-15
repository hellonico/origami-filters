package origami.runtime;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import origami.Filter;
import origami.FindFilters;
import origami.Origami;
import origami.utils.FileWatcher;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class RuntimeFilter implements Filter {
    String rootPath = System.getProperty("user.home") + "/.origami";

    String className = "filters.myfilter";

    Filter effectiveFilter;

    FileWatcher wf;

    boolean watch = false;

    public boolean isWatch() {
        return watch;
    }

    public void setWatch(boolean watch) {
        this.watch = watch;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void preload() {
        try {
            if (wf != null) {
                wf.stopThread();
                wf = null;
            }
            File root = new File(rootPath);

            root.mkdirs();
            File sourceFile = getSourceFileFromClassName(root, className);
            if (!sourceFile.exists()) {
                generateJavaFilter(root, className);
            }
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            compiler.run(null, null, null, sourceFile.getPath());

            // Load and instantiate compiled class.
            URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{root.toURI().toURL()}, Origami.class.getClassLoader());

            Class<?> cls = Class.forName(className, true, classLoader);
            effectiveFilter = (Filter) cls.getDeclaredConstructor().newInstance();

            if (watch) {
                wf = new FileWatcher(root) {
                    @Override
                    public void doOnChange() {
                        preload();
                    }
                };
                wf.start();
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Mat apply(Mat mat) {
        if (effectiveFilter == null) {
            preload();
        }
        return effectiveFilter.apply(mat);
    }

    public static File generateJavaFilter(File root, String className) throws IOException {
        // Prepare source somehow.
        String source = "package " + className.substring(0, className.lastIndexOf(".")) + "; " +
                "import org.opencv.core.Mat; " +
                "import origami.Filter; " +
                "public class " + className.substring(className.lastIndexOf(".") + 1) + " implements origami.Filter { public Mat apply(Mat m) { return m;} }";

        File sourceFile = getSourceFileFromClassName(root, className);

        sourceFile.getParentFile().mkdirs();
        Files.write(sourceFile.toPath(), source.getBytes(StandardCharsets.UTF_8));
        return sourceFile;
    }

    private static File getSourceFileFromClassName(File root, String className) {
        String javaFileName = className.replaceAll("\\.", "/") + ".java";
        File sourceFile = new File(root, javaFileName);
        return sourceFile;
    }

    public static void main(String... args) throws Exception {
        Origami.init();
        RuntimeFilter rf = new RuntimeFilter();
        rf.setClassName("filters.myfilter4");

        String matPath = FindFilters.class.getClassLoader().getResource("marcel.jpg").getPath();
        Mat m = Imgcodecs.imread(matPath);
        Imgcodecs.imwrite("runtime.png", rf.apply(m));
    }

}
