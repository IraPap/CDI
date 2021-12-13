package org.ipap.cdi.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class ClassUtils {

    private ClassUtils() {

    }


    /**
     * Gets all classes under the specified package
     *
     * @param packageName the package to search for classes
     * @return a {@link Set} of {@link Class}
     * @throws ClassNotFoundException any of the classes is not found
     */
    public static Set<Class<?>> getAllClasses(String packageName) throws ClassNotFoundException {

        InputStream inputStream = ClassLoader.getSystemClassLoader()
                .getResourceAsStream(packageName.replaceAll("[.]", "/"));

        assert inputStream != null;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        List<String> list = bufferedReader.lines().collect(Collectors.toList());
        Set<Class<?>> classes = new HashSet<>();
        for (String s : list) {

            if (s.endsWith(".class")) {

                Class<?> clazz = getClass(s, packageName);
                classes.add(clazz);
            }
        }

        if (classes.stream().anyMatch(Objects::isNull)) {
            throw new ClassNotFoundException();
        }

        return classes;
    }


    /**
     * Returns the {@code Class} object associated with the class or
     * interface with the given string name in the given package
     *
     * @param className the name of the class or interface
     * @param packageName the name of the package to search
     * @return a {@code Class} object or null if not found
     */
    private static Class<?> getClass(String className, String packageName) {

        try {
            return Class.forName(packageName + "."
                    + className.substring(0, className.lastIndexOf('.')));
        } catch (ClassNotFoundException e) {

            return null;
        }

    }
}
