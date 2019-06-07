package group14;

public class Resources {

    public static class TestImages {
        public static final String ballMany1 = load("ball_many_1.jpg");
        public static final String ballMany2 = load("ball_many_2.jpg");
        public static final String ballMany3 = load("ball_many_3.jpg");
        public static final String ballMany4 = load("ball_many_4.jpg");

        public static final String ballOne1 = load("ball_one_1.jpg");
        public static final String ballOne2 = load("ball_one_2.jpg");

        public static final String board1 = load("board_1.jpg");
        public static final String board2 = load("board_2.jpg");
        public static final String board3 = load("board_3.jpg");
        public static final String board4 = load("board_4.jpg");

        public static final String cross1 = load("red_cross_1.jpg");
        public static final String cross2 = load("red_cross_2.png");


        private static String load(String file) {
            return Resources.load("test_images/" + file);
        }
    }


    private static String load(String file) {
        var fileUrl = ClassLoader.getSystemResource("group14/" + file);

        if (fileUrl == null) {
            throw new RuntimeException("File does not exist: group14/" + file);
        }

        return fileUrl.getFile();
    }

}
