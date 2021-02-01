package testing;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TestStarter {
    private TestStarter(){}

    public static void start(Class aClass) {
        List<Method> testMethods = findMethods(aClass, Test.class);
        testMethods.sort(new Comparator<Method>() {
            @Override
            public int compare(Method o1, Method o2) {
                return o1.getAnnotation(Test.class).order() - o2.getAnnotation(Test.class).order();
            }
        });

        if (testMethods.isEmpty()) {
            System.out.println(String.format("%s has no test methods", aClass.getName()));
            return;
        }

        Object obj = initObject(aClass);

        List<Method> beforeSuiteMethods = findMethods(aClass, BeforeSuite.class);
        if (!beforeSuiteMethods.isEmpty() && beforeSuiteMethods.size() > 1) {
            throw new RuntimeException("BeforeSuite annotation should be only one");
        }
        List<Method> afterSuiteMethods = findMethods(aClass, AfterSuite.class);
        if (!afterSuiteMethods.isEmpty() && afterSuiteMethods.size() > 1) {
            throw new RuntimeException("AfterSuite annotation should be only one");
        }

        if (!beforeSuiteMethods.isEmpty()) {
            executeMethods(beforeSuiteMethods.get(0), obj);
        }

        for (Method testMethod : testMethods) {
            System.out.println(testMethod);
            executeMethods(testMethod, obj);
        }

        if (!afterSuiteMethods.isEmpty()) {
            executeMethods(afterSuiteMethods.get(0), obj);
        }
    }


    private static void executeMethods(Method method, Object obj, Object... args) {
        try {
            method.setAccessible(true);
            method.invoke(obj, args);
            method.setAccessible(false);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }


    private static List<Method> findMethods(Class aClass, Class<? extends Annotation> annotationClass) {
        List<Method> testMethods = new ArrayList<>();
        for (Method method : aClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(annotationClass)) {
                testMethods.add(method);
            }
        }
        return testMethods;
    }

    public static void start(String className) {
        try {
            start(Class.forName(className));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("SWW", e);
        }
    }

    private static Object initObject(Class aClass) {
        try {
            return aClass.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("SWW", e);
        }
    }
}
