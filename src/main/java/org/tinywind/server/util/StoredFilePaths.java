package org.tinywind.server.util;

import java.nio.file.Path;
import java.nio.file.Paths;

public final class StoredFilePaths {
    private StoredFilePaths() {
    }

    public static Path resolve(String directory, String fileName) {
        if (fileName == null || fileName.isBlank() || fileName.equals(".") || fileName.equals("..")
                || fileName.indexOf('/') >= 0 || fileName.indexOf('\\') >= 0
                || fileName.chars().anyMatch(Character::isISOControl)) {
            throw new IllegalArgumentException("Invalid stored file name");
        }
        Path root = Paths.get(directory).toAbsolutePath().normalize();
        Path file = root.resolve(fileName).normalize();
        if (!file.startsWith(root) || file.equals(root)) {
            throw new IllegalArgumentException("File must remain in the storage directory");
        }
        return file;
    }
}
