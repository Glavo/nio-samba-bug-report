import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

@SuppressWarnings("BulkFileAttributesRead")
public class Unzipper {
    public static void main(String[] args) throws IOException {
        Path zipFile = Path.of(args[0]);
        Path targetDir = Path.of(args[1]);

        Files.createDirectories(targetDir);

        try (FileSystem fs = FileSystems.newFileSystem(zipFile, (ClassLoader) null)) {
            Path root = fs.getPath("/");

            Files.walkFileTree(fs.getPath("/"), new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    Path d = targetDir.resolve(root.relativize(dir).toString());
                    try {
                        Files.createDirectories(d);
                    } catch (FileAlreadyExistsException e) {
                        BasicFileAttributes attributes = Files.readAttributes(d, BasicFileAttributes.class);
                        File dirFile = d.toFile();

                        System.err.println("Failed to process directory: " + d);

                        System.err.printf("    Files.exists: %s%n", Files.exists(d));
                        System.err.printf("    Files.isDirectory: %s%n", attributes.isDirectory());
                        System.err.printf("    Files.isRegularFile: %s%n", attributes.isRegularFile());

                        System.err.printf("    File.exists: %s%n", dirFile.exists());
                        System.err.printf("    File.isDirectory: %s%n", dirFile.isDirectory());
                        System.err.printf("    File.isFile: %s%n", dirFile.isFile());


                        throw e;
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Path targetFile = targetDir.resolve(root.relativize(file).toString());
                    try {
                        Files.copy(file, targetFile);
                    } catch (FileAlreadyExistsException e) {
                        System.out.printf("file %s already exists: %s%n", file, e.getMessage());
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        }
    }

}
