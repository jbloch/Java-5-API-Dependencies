package us.bloch.apidependencies;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This program computes the API dependencies of the Java Programming Language, as defined in
 * "The Java Language Specification, Third Edition" (Addison Wesley, 2005), which specifies the
 * Java programming language as of version Java SE 5. The JLS section and page references in
 * the comments of this program refer to that edition.
 *
 * <p>Note that the results of running this program will be inaccurate if you run it against the
 * wrong version of the JRE: more recent versions will report additional dependencies, as the
 * libraries have grown. The program will not run against earlier versions, as some classes
 * mentioned in the third edition of the JLS were not present in Java 4.
 *
 * @author Josh Bloch
 */
public class JavaApiDependencies {
    /**
     * This array contains the fully qualified names of the classes explicitly required by
     * normative text in the JLS. The section and page where the JLS requires each of these
     * classes is noted in a comment, like this: [JLS 4.3.3, p. 47]. Most of these classes are
     * referenced repeatedly in JLS; only one reference for each class is shown below.
     * This list was compiled by hand, so it may contain omissions, which could result in a
     * conservative listing of the API dependencies. In other words, the Java programming
     * language might depend on more classes than are printed by this program, but not fewer.
     */
    private static final String[] jlsClassNames = {
        // The root of the class hierarchy [JLS 4.3.2, p. 47]
        "java.lang.Object",

        // A character string [JLS 4.3.3, p. 48]
        "java.lang.String", 

        // The wrapper classes, required for boxing conversion [JLS 5.1.7, p. 87]
        "java.lang.Boolean", "java.lang.Byte", "java.lang.Character", "java.lang.Short",
        "java.lang.Integer", "java.lang.Long", "java.lang.Float", "java.lang.Double",

        // The type of void.class is Class<Void> [15.8.2, p. 421]
        "java.lang.Void",

        // A thread of execution [JLS 17, p. 553]
        "java.lang.Thread",

	    // A group of threads [JLS 11.3, p. 303]
        "java.lang.ThreadGroup",

        // The runtime representation of a class [JLS 4.3.2, p. 48]
        "java.lang.Class",

        // The entity responsible for loading classes into the VM [JLS 12.2, p. 312]
        "java.lang.ClassLoader",

        // An instantiable class that allows access the VM [JLS 12.8; p. 331]
        "java.lang.Runtime",

        // A non-instantiable class that allows access the VM [JLS 17.5.4, p. 578]
        "java.lang.System",

	    // A non-instantiable class that provides various mathematical operations [JLS 3.10.2, p. 26]
	    "java.lang.Math",

        // Interface implemented by objects that can be duplicated [JLS 10.7, p. 292]
        "java.lang.Cloneable",

        // Interface implemented by objects that can be emitted to a byte stream [JLS 10.7, p. 292]
        "java.io.Serializable",

        // An object that can be iterated over with a for-each loop [JLS 14.14.2, p. 387]
        "java.lang.Iterable",

        // The superclass of all enum types [JLS 8.9, p. 251]
        "java.lang.Enum",

        // The superinterface of all annotation types [JLS 9.6, p. 272]
        "java.lang.annotation.Annotation",

        // Annotation type to indicate where an annotation is allowed [JLS 9.6.1.1, p. 278]
        "java.lang.annotation.Target", "java.lang.annotation.ElementType", 

        // Annotation type to indicate how long an annotation is retained [JLS 9.6.1.2, p. 278]
        "java.lang.annotation.Retention", "java.lang.annotation.RetentionPolicy",

        // Annotation to indicate that an annotation applies to subclasses [JLS 9.6.1.3, p. 279]
        "java.lang.annotation.Inherited", 

        // Annotation to indicate that a method declaration overrides another [JLS 9.6.1.4, p. 279]
        "java.lang.Override",

        // Annotation to indicate that compiler warnings should be suppressed [JLS 9.6.1.5, p. 280]
        "java.lang.SuppressWarnings",

        // Annotation type used to indicate that an API element is obsolete [JLS 9.6.1.6, p. 280]
        "java.lang.Deprecated",

        // The root of the exception and error hierarchies [JLS 11.5, p. 306]
        "java.lang.Throwable",

        // The root of the exception hierarchy [JLS 11.2.3, p. 301]
        "java.lang.Exception",

        // The root of the unchecked exception hierarchy [JLS 11.2.5, p. 301]
        "java.lang.RuntimeException",

        // The root of the error hierarchy  [JLS 11.2.4, p. 301]
        "java.lang.Error",
 
        /*
         * Exception types defined in the JLS (most are thrown by the VM)
         */
        "java.lang.ArithmeticException",              // [JLS 4.2.3, p. 37]
        "java.lang.IllegalArgumentException",         // [JLS 8.9, p. 252]
        "java.lang.ArrayIndexOutOfBoundsException",   // [JLS 10.4, p. 290]
        "java.lang.ArrayStoreException",              // [JLS 10.10, p. 294]
        "java.lang.ClassCastException",               // [JLS 15.5, p. 412]
        "java.lang.CloneNotSupportedException",       // [JLS 10.7, p. 292]
        "java.lang.IllegalMonitorStateException",     // [JLS 17.8, p. 580]
        "java.lang.InterruptedException",             // [JLS 17.8, p. 580]
        "java.lang.NegativeArraySizeException",       // [JLS 15.10.1, p. 432]
        "java.lang.NullPointerException",             // [JLS 15.12.4.4, p. 476]
        "java.lang.AbstractMethodError",              // [JLS 13.4.16, p. 352]
        "java.lang.AssertionError",                   // [JLS 14.10, p. 376]
        "java.lang.ClassCircularityError",            // [JLS 12.2.1, p. 313]
        "java.lang.ClassFormatError",                 // [JLS 12.2.1, p. 313]
        "java.lang.ExceptionInInitializerError",      // [JLS 12.4.2, p. 321]
        "java.lang.IncompatibleClassChangeError",     // [JLS 13.4.10, p. 349]
        "java.lang.InstantiationError",               // [JLS 12.3.3, p. 316]
        "java.lang.InternalError",                    // [JLS 11.4, p. 304]
        "java.lang.LinkageError",                     // [JLS 12.2.1, p. 313]
        "java.lang.NoClassDefFoundError",             // [JLS 12.2.1, p. 313]
        "java.lang.IllegalAccessError",               // [JLS 12.3.3, p. 315]
        "java.lang.NoSuchFieldError",                 // [JLS 12.3.3, p. 316]
        "java.lang.NoSuchMethodError",                // [JLS 12.3.3, p. 316]
        "java.lang.OutOfMemoryError",                 // [JLS 12.5, p. 313]
        "java.lang.InstantiationException",           // [JLS 13.4.1, p. 340]
        "java.lang.StackOverflowError",               // [JLS 15.12.4.5, p. 477]
        "java.lang.VerifyError",                      // [JLS 12.3.1, p. 314]
        "java.lang.UnsatisfiedLinkError",             // [JLS 12.3.3, p. 316]
        "java.lang.VirtualMachineError"               // [JLS 11.5.2, p. 307]
    };

    public static void main(String[] args) throws ClassNotFoundException {
        Set<Class<?>> jlsClasses = new HashSet<Class<?>>();
        for (String className : jlsClassNames)
            jlsClasses.add(Class.forName(className));
        Api api = new Api(jlsClasses);

        System.out.println(jlsClasses.size() + " classes are mentioned directly in JLS 3e.");
        System.out.printf("With dependencies, %d classes in %d packages are required, totalling %d members%n%n",
                api.classesAndInterfaces().size(), api.packages().size(), api.members().size());

        // Sort visited package names alphabetically and print them
        System.out.printf("Packages%n%n");
        List<String> packageNames = new ArrayList<String>();
        for (Package p : api.packages())
            packageNames.add(p.getName());
        Collections.sort(packageNames);
        for (String packageName : packageNames)
            System.out.println(packageName);

        // Sort visited class names alphabetically and print them
        System.out.printf("%nClasses%n%n");
        List<String> classNames = new ArrayList<String>();
        for (Class<?> c : api.classesAndInterfaces())
            classNames.add(c.getName());
        Collections.sort(classNames);
        for (String className : classNames)
            System.out.println(className);
    }
}
