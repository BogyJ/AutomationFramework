package helpers.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

public class FileHelper {
    /**
     * @param sourcePath - file that we want to move/rename
     * @param destPath   - file that represent the moved file or a same file just with a new name
     */
    public static void moveFile(String sourcePath, String destPath) {
        if (Files.exists(Path.of(sourcePath)) && Files.exists(Path.of(destPath))) {
            try {
                Files.move(Path.of(sourcePath), Path.of(destPath));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            throw new FileSystemNotFoundException("One or both files provided does not exists!");
        }
    }

    /**
     * @param sourceFile      - Java representation of a file(file, folder or directory, etc.) we want
     *                        to copy
     * @param destinationFile - Java representation of a file(file, folder or directory, etc.)
     *                        location of a file where we want to make a copy
     */
    public static void copyFile(File sourceFile, File destinationFile) {
        if (sourceFile.exists() && destinationFile.exists()) {
            try {
                Files.copy(sourceFile.toPath(), destinationFile.toPath(),
                        StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            throw new FileSystemNotFoundException("One or both files provided does not exists!");
        }
    }

    /**
     * @param sourcePath      - string representation of a file path (location) we want to copy
     * @param destinationPath - string representation of a file path (location) of a file where we
     *                        want to make a copy
     */
    public static void copyFile(String sourcePath, String destinationPath) {
        if (Files.exists(Path.of(sourcePath)) && Files.exists(Path.of(destinationPath))) {
            File sourceFile = createFile(sourcePath);
            File destinationFile = createFile(destinationPath);
            copyFile(sourceFile, destinationFile);
        } else {
            throw new FileSystemNotFoundException("One or both files provided does not exists!");
        }
    }

    /**
     * @param filePath - path to a file
     * @return Java representation of a file
     */
    public static File createFile(String filePath) {
        return createPath(filePath).toFile();
    }

    /**
     * @param filePath - path to a file
     * @return Java (NIO lib) representation of a path(file)
     */
    public static Path createPath(String filePath) {
        Path file = null;
        try {
            file = Files.createFile(Path.of(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * @param filePath - path to a file
     */
    public static void deleteFileQuietly(String filePath) {
        try {
            Files.delete(Path.of(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param filePath - path to a file
     * @return result of deletion
     */
    public static boolean deleteFile(String filePath) {
        boolean isDeleted = false;
        try {
            isDeleted = Files.deleteIfExists(Path.of(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isDeleted;
    }

    public static boolean deleteDirectory(String directoryPath) {
        File directoryToBeDeleted = new File(directoryPath);
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file.getAbsolutePath());
            }
        }
        return directoryToBeDeleted.delete();
    }

    /**
     * @return string representation of temp file path
     */
    public static String createTempDir() {
        try {
            return Files.createTempDirectory("tempDir").toFile().getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String createTempFile(String fileName, String filePath) {
        Path path = Paths.get(filePath);
        try {
            return Files.createTempFile(path, "tempFile", fileName).toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
