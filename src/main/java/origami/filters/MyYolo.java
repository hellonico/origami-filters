package origami.filters;

import origami.filters.detect.Yolo;

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
            super("networks.yolo:yolov3:1.0.0");
        }
    }
    public static class V4 extends Yolo {
        public V4() {
            super("networks.yolo:yolov4:1.0.0");
        }
    }
}