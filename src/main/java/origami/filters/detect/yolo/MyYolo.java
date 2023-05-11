package origami.filters.detect.yolo;

import origami.Filter;
import origami.filters.detect.yolo.Yolo;
import origami.filters.detect.yolo.YoloV6;

public class MyYolo {
    public static class V2 extends Yolo {
        public V2() {
            super("networks.yolo:yolov2:1.0.0");
        }
    }
    public static class V2Tiny extends Yolo {
        public V2Tiny() {
            super("networks.yolo:yolov2-tiny:1.0.0");
        }
    }
    public static class V3 extends Yolo {
        public V3() {
            super("networks.yolo:yolov3:1.0.0");
        }
    }

    public static class V3Tiny extends Yolo {
        public V3Tiny() {
            super("networks.yolo:yolov3-tiny:1.0.0");
        }
    }
    public static class V4 extends Yolo {
        public V4() {
            super("networks.yolo:yolov4:1.0.0");
        }
    }

    public static class V6S extends YoloV6 {
        public V6S() {
            super("networks.yolo:yolov6s:3.0");
        }
    }
    public static class V6L extends YoloV6 {
        public V6L() {
            super("networks.yolo:yolov6s:3.0");
        }
    }
    public static class V6M extends YoloV6 {
        public V6M() {
            super("networks.yolo:yolov6s:3.0");
        }
    }
    public static class V6N extends YoloV6 {
        public V6N() {
            super("networks.yolo:yolov6s:3.0");
        }
    }
    public static class V6Star extends YoloV6 {
        public String getNetwork() {
            return network;
        }

        public void setNetwork(String network) {
            this.network = network;
            this.load(network);
        }

        String network = "networks.yolo:yolov6s:3.0";
        public V6Star() {
            super();
        }
    }
}