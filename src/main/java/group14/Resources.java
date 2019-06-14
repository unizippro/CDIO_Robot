package group14;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
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
        public static final String board5 = load("board_5.jpg");
        public static final String board6 = load("board_6.jpg");
        public static final String board7 = load("board_7.jpg");

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


    public static class OpenCVSamples {
        public static final String left1 = load("left01.jpg");
        public static final String left2 = load("left02.jpg");
        public static final String left3 = load("left03.jpg");
        public static final String left4 = load("left04.jpg");
        public static final String left5 = load("left05.jpg");
        public static final String left6 = load("left06.jpg");
        public static final String left7 = load("left07.jpg");
        public static final String left8 = load("left08.jpg");
        public static final String left9 = load("left09.jpg");
        public static final String left11 = load("left11.jpg");
        public static final String left12 = load("left12.jpg");
        public static final String left13 = load("left13.jpg");
        public static final String left14 = load("left14.jpg");


        public static List<String> getAllFiles() {
            return Resources.getAllFiles("opencv_samples", false);
        }

        public static List<String> getAllChessboardFiles() {
            return Resources.getAllFilesMatching("**/left*.jpg", "opencv_samples");
        }

        private static String load(String file) {
            return Resources.load("opencv_samples/" + file);
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


    private static List<String> getAllFilesMatching(String glob, String dir) {
        var filePath = ClassLoader.getSystemResource("group14/" + dir).getFile();

        final PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher("glob:" + glob);

        try {
            var matchedPaths = new ArrayList<String>();

            Files.walkFileTree(Paths.get(filePath), new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) {
                    if (pathMatcher.matches(path)) {
                        matchedPaths.add(path.toString());
                    }

                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) {
                    return FileVisitResult.CONTINUE;
                }
            });

            return matchedPaths;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
