package origami;


import org.junit.Test;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class RunAllFilters {
    @Test
    public void sayHello() throws ClassNotFoundException, IllegalAccessException, InstantiationException, IOException {
        Origami.init();
        List<String> filters = FindFilters.findFilters();
        Collections.sort(filters);
        FileWriter fw = new FileWriter("output/filters.md");
        BufferedWriter bw = new BufferedWriter(fw);

        String matPath = this.getClass().getClassLoader().getResource("marcel.jpg").getPath();
        Mat m = Imgcodecs.imread(matPath, Imgcodecs.IMREAD_REDUCED_COLOR_8);

        Imgcodecs.imwrite("output/original.png", m);
        bw.write("# Original");
        bw.newLine();
        bw.write("![](original.png"+")");
        bw.newLine();

        for(String f : filters) {
            try {
                Class<?> _f = Class.forName(f);
                Filter __f = (Filter) _f.newInstance();

                Mat c = m.clone();
                Mat d = __f.apply(c);

                bw.write("# "+_f.getSimpleName());
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
                bw.write("![]("+_f.getSimpleName()+".png"+")");
                bw.newLine();

                bw.flush();

                Imgcodecs.imwrite("output/"+_f.getSimpleName()+".png", d);

            } catch(Exception e) {
                e.printStackTrace();
                System.out.println("> Check:"+f);
            }


        }

        bw.close();
    }
    
}
