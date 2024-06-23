package ca.uwindsor.appliedcomputing.final_project.util;

import com.sun.tools.javac.Main;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Resource {
    /**
     * This method is used to walk through the resources directory and return a list of absolute paths of all regular files.
     *
     * @return A list of absolute paths of all regular files in the resources directory. If an exception occurs, it returns an empty list.
     */
    public static List<Path> walkResources() {
        try {
            Path resourcePath = Paths.get(Main.class.getClassLoader().getResource("data").toURI());
            try (Stream<Path> paths = Files.walk(resourcePath)) {
                return paths.filter(Files::isRegularFile)
                        .map(Path::toAbsolutePath)
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
