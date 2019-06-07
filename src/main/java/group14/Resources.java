package group14;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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


        public static List<String> getAllFiles() {
            return Resources.getAllFiles("test_images", false);
        }

        public static List<String> getAllFiles(boolean onlyFileName) {
            return Resources.getAllFiles("test_images", onlyFileName);
        }


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


    private static List<String> getAllFiles(String dir, boolean onlyFileName) {
        var filePath = ClassLoader.getSystemResource("group14/" + dir).getFile();
        try (Stream<Path> walk = Files.walk(Paths.get(filePath))) {

            return walk.filter(Files::isRegularFile)
                    .map(path -> onlyFileName ? path.getFileName() : path)
                    .map(Path::toString)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
