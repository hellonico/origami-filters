package origami.filters;

import clojure.java.api.Clojure;
import clojure.lang.IFn;
import clojure.lang.Symbol;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import static clojure.java.api.Clojure.var;

public class DynamicAnnotate extends Annotate {
    static IFn EDN_READ_STRING;

    static {
        Symbol edn = (Symbol) var("clojure.core", "symbol").invoke("clojure.edn");
        var("clojure.core", "require").invoke(edn);
        EDN_READ_STRING = var("clojure.edn", "read-string");
    }

    IFn eval = var("clojure.core", "eval");
    public DynamicAnnotate() {
        super();
    }

    @Override
    public Mat apply(Mat mat) {
        String text2 = eval.invoke(EDN_READ_STRING.invoke(text)).toString();
        Imgproc.putText(mat, text2, point, fontFace, fontSize, color, thickness);
        return mat;
    }
}
