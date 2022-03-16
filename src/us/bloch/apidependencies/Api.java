package us.bloch.apidependencies;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * An API containing the classes provided to its constructor and any classes on which these
 * classes depend. In other words, the API consists of the reflexive transitive closure of the
 * classes provided to the constructor under API dependency.
 *
 * @author Josh Bloch
 */
public class Api {
    /** A list of classes and interfaces that we have yet to visit */
    private  final Set<Class<?>> toVisit = new LinkedHashSet<Class<?>>();

    /** A list of classes and interfaces that we've already visited */
    private  final Set<Class<?>> visited = new LinkedHashSet<Class<?>>();

    /** The packages in which we've visited classes and interfaces */
    private  final Set<Package> packages = new HashSet<Package>();

    /** The members we've already visited */
    private  final Set<Member> members = new HashSet<Member>();

    /**
     * Returns an API consisting of the given classes and any classes on which their APIs depend
     * (directly or indirectly).
     *
     * @param classes the classes contained in the API (any duplicates are ignored)
     * @throws NullPointerException if classes or any of its elements are null
     */
    public Api(Collection<Class<?>> classes) {
        for (Class<?> c : classes)
            visit(c);

        // Recursively visit all unvisited classes and their API dependencies
        while (!toVisit.isEmpty())
            visit(removeUnvisitedClass());
    }
    /**
     * Visit a class, which entails adding it to visited, ensuring that we have already
     * visited or will subsequently visit all the classes on which its API depends,
     * and adding this class's members and constructors to members.
     */
    private  void visit(Class<?> c) {
        visited.add(c);
        packages.add(c.getPackage());

        // For each public constructor, ensure that we visit its parameter and exception types
        for (Constructor<?> cons : c.getConstructors())
            visitConstructor(cons);

        // For each protected constructor, ensure that we visit its parameter and exception types
        for (Constructor<?> cons : c.getDeclaredConstructors())
            if (isProtected(cons))
                visitConstructor(cons);

        // For each public method, ensure that we visit its return, parameter and exception types
        for (Method m : c.getMethods())
            visitMethod(m);

        // For each protected method, ensure that we visit its return, parameter and exception types
        for (Method m : c.getDeclaredMethods())
            if (isProtected(m))
                visitMethod(m);

        // For each public field, ensure that we visit its type
        for (Field f : c.getFields())
            visitField(f);

        // For each protected field, ensure that we visit its type
        for (Field f : c.getDeclaredFields())
            if (isProtected(f))
               visitField(f);

        // Ensure that we visit all of this class's public member types
        ensureVisit(c.getClasses());

        // Ensure that we visit all of this class's superClasses
        for (Class<?> sc = c.getSuperclass(); sc != null; sc = sc.getSuperclass())
            ensureVisit(sc);

        // Ensure that we visit all of this type's implemented interfaces
        ensureVisit(c.getInterfaces());

        // Ensure that we visit all of this type's enclosing classes
        for (Class<?> ec = c.getEnclosingClass(); ec != null; ec = ec.getEnclosingClass())
            ensureVisit(ec);
    }

    /** Ensure that we visit the constructor's parameter and exception types */
    private  void visitConstructor(Constructor<?> cons) {
        members.add(cons);
        ensureVisit(cons.getParameterTypes());
        ensureVisit(cons.getExceptionTypes());
    }

    /** Ensure that we visit the method's return, parameter and exception types */
    private  void visitMethod(Method m) {
        members.add(m);
        ensureVisit(m.getReturnType());
        ensureVisit(m.getParameterTypes());
        ensureVisit(m.getExceptionTypes());
    }

    /** Ensure that we visit the field's type */
    private  void visitField(Field f) {
        members.add(f);
        ensureVisit(f.getType());
    }

    /** Ensure that we visit all the types in the specified array. */
    private  void ensureVisit(Class<?>[] classes) {
        for (Class<?> c : classes)
            ensureVisit(c);
    }

    /** Ensure that we visit the specified type. */
    private  void ensureVisit(Class<?> c) {
        // If the type represents an array, get its "ultimate" (non-array) element type
        while (c.isArray())
            c = c.getComponentType();

        // If the types is a reference type that we haven't yet visited, add it to toVisit
        if (!(c.isPrimitive() || visited.contains(c)))
            toVisit.add(c);
    }

    /** Remove and return a type from toVisit */
    private  Class<?> removeUnvisitedClass() {
        Iterator<Class<?>> it = toVisit.iterator();
        Class<?> result = it.next();
        it.remove();
        return result;
    }

    /** Returns true if member m is protected */
    private static boolean isProtected(Member m) {
        return (m.getModifiers() & Modifier.PROTECTED) != 0;
    }

    /**
     * Returns the classes and interfaces in this API (which is the reflexive transitive closure
     * of the classes and interfaces passed to the constructor under API dependency). The returned
     * set is unmodifiable.
     */
    public Set<Class<?>> classesAndInterfaces() {
        return Collections.unmodifiableSet(visited);
    }

    /**
     * Returns the exported (public and protected) members of the classes and interfaces in this
     * API (as returned by {@link #classesAndInterfaces()}). The returned set is unmodifiable.
     */
    public Set<Member> members() {
        return Collections.unmodifiableSet(members);
    }

    /**
     * Returns the packages that contain classes and interfaces in this API (as returned by
     * {@link #classesAndInterfaces()}). The returned set is unmodifiable.
     */
    public Set<Package> packages() {
        return Collections.unmodifiableSet(packages);
    }
}
