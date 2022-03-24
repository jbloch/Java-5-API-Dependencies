# Java 5 API Dependencies
This repo contains a program to compute the *reflexive transitive closure* under *API dependency* of a collection of Java classes (and interfaces). One class's API is said to (directly) depend on another's if the former class's API mentions latter's, e.g., as a parameter type, a return type, a supertype, or an exception type. The reason this closure is important is that it represents the size of the API that is required to support the given collection of Java classes or interfaces: you *cannot* implement the given classes without implementing every other class in the closure.

In the *Oracle v. Google* trials in 2012 and 2016, Oracle contended that 170 declarations from 62 classes were sufficient to implement the Java language. These declarations are the ones that are specifically referred to in The the *Java Language Specification* (3d Ed., Addison-Wesley, 2005), which describes Java SE 5, better known as Java 5.

This contention is wrong on several counts. First of all, the specification directly mentions at least 64 classes, as shown in the table below. More seriously it ignores the fact that you can't implement an API without implementing its reflexive transitive closure under API dependency, as explained above (let's call this the *implicit API*).

Using the `Api` program in this repo, you can compute the implicit API of a given API. The `JavaApiDependencies` program use the `Api` program to compute the implicit API of the API mentioned in the *Java Language Specification* (3d Ed). If you run this program under Java 5, you will find that it requires 2,381 methods, constructors, and fields, spread across 178 classes in 10 packages just to implement the Java language (never mind that that does not get you such basic libraries as `java.io.PrintStream`, which is required even for the basic "Hello World" program).

Here is a table of direct references to classes and interfaces in the *Java Language Specification* (3d Ed., Addison-Wesley, 2005). This table may not be complete, but it demonstrates that at least 64 classes (and interfaces) are mentioned in the JLS:
|Row|Class Name|Description|Section|Page|
| -----------| ----------- | ----------- | ----------- | ----------- |
|1|`java.lang.Object`|The root of the class hierarchy|4.3.2|47|
|2|`java.lang.String`|A character string|4.3.3|48|
|3|`java.lang.Boolean`|Wrapper classes for `boolean`|5.1.7|87|
|4|`java.lang.Byte`|Wrapper classes for `byte`|5.1.7|87|
|5|`java.lang.Character`|Wrapper classes for `char`|5.1.7|87|
|6|`java.lang.Short`|Wrapper classes for `short`|5.1.7|87|
|7|`java.lang.Integer`|Wrapper classes for `int`|5.1.7|87|
|8|`java.lang.Long`|Wrapper classes for `long`|5.1.7|87|
|9|`java.lang.Float`|Wrapper classes for `float`|5.1.7|87|
|10|`java.lang.Double`|Wrapper classes for `double`|5.1.7|87|
|11|`java.lang.Void`|The type of `void.class` |15.8.2|421|
|12|`java.lang.Thread`|A thread of execution|17|553|
|13|`java.lang.ThreadGroup`|A group of threads|11.3|303|
|14|`java.lang.Class`|The runtime representation of a class|4.3.2|48|
|15|`java.lang.ClassLoader`|The entity responsible for loading classes into the VM|12.2|312|
|16|`java.lang.Runtime`|An instantiable class that allows access the VM|12.8|331|
|17|`java.lang.System`|A non-instantiable class that allows access the VM|17.5.4|578|
|18|`java.lang.Math`|A non-instantiable class that provides various mathematical operations|3.10.2|26|
|19|`java.lang.Cloneable`|Interface implemented by objects that can be duplicated|10.7|292|
|20|`java.io.Serializable`|Interface implemented by objects that can be emitted to a byte stream|10.7|292|
|21|`java.lang.Iterable`|An object that can be iterated over with a for-each loop|14.14.2|387|
|22|`java.lang.Enum`|The superclass of all enum types|8.9|251|
|23|`java.lang.annotation.Annotation`|The superinterface of all annotation types|9.6|272|
|24|`java.lang.annotation.Target`|Annotation type to indicate where an annotation is allowed|9.6.1.1|278|
|25|`java.lang.annotation.ElementType`|Enum to indicate where an annotation is allowed|9.6.1.1|278|
|26|`java.lang.annotation.Retention`|Annotation type to indicate how long an annotation is retained|9.6.1.2|278|
|27|`java.lang.annotation.RetentionPolicy`|Enum to indicate how long an annotation is retained|9.6.1.2|278|
|28|`java.lang.annotation.Inherited`|Annotation to indicate that an annotation applies to subclasses|9.6.1.3|279|
|29|`java.lang.Override`|Annotation to indicate that a method declaration overrides another|9.6.1.4|279|
|30|`java.lang.SuppressWarnings`|Annotation to indicate that compiler warnings should be suppressed|9.6.1.5|280|
|31|`java.lang.Deprecated`|Annotation type used to indicate that an API element is obsolete|9.6.1.6|280|
|32|`java.lang.Throwable`|The root of the exception and error hierarchies|11.5|306|
|33|`java.lang.Exception`|The root of the exception hierarchy|11.2.3|301|
|34|`java.lang.RuntimeException`|The root of the unchecked exception hierarchy|11.2.5|301|
|35|`java.lang.Error`|The root of the error hierarchy|11.2.4|301|
|36|`java.lang.ArithmeticException`|(Self explanatory)|4.2.3|37|
|37|`java.lang.IllegalArgumentException`|(Self explanatory)|8.9|252|
|38|`java.lang.ArrayIndexOutOfBoundsException`|(Self explanatory)|10.4|290|
|39|`java.lang.ArrayStoreException`|(Self explanatory)|10.10|294|
|40|`java.lang.ClassCastException`|(Self explanatory)|15.5|412|
|41|`java.lang.CloneNotSupportedException`|(Self explanatory)|10.7|292|
|42|`java.lang.IllegalMonitorStateException`|(Self explanatory)|17.8|580|
|43|`java.lang.InterruptedException`|(Self explanatory)|17.8|580|
|44|`java.lang.NegativeArraySizeException`|(Self explanatory)|15.10.1|432|
|45|`java.lang.NullPointerException`|(Self explanatory)|15.12.4.4|476|
|46|`java.lang.AbstractMethodError`|(Self explanatory)|13.4.16|352|
|47|`java.lang.AssertionError`|(Self explanatory)|14.10|376|
|48|`java.lang.ClassCircularityError`|(Self explanatory)|12.2.1|313|
|49|`java.lang.ClassFormatError`|(Self explanatory)|12.2.1|313|
|50|`java.lang.ExceptionInInitializerError`|(Self explanatory)|12.4.2|321|
|51|`java.lang.IncompatibleClassChangeError`|(Self explanatory)|13.4.10|349|
|52|`java.lang.InstantiationError`|(Self explanatory)|12.3.3|316|
|53|`java.lang.InternalError`|(Self explanatory)|11.4|304|
|54|`java.lang.LinkageError`|(Self explanatory)|12.2.1|313|
|55|`java.lang.NoClassDefFoundError`|(Self explanatory)|12.2.1|313|
|56|`java.lang.IllegalAccessError`|(Self explanatory)|12.3.3|315|
|57|`java.lang.NoSuchFieldError`|(Self explanatory)|12.3.3|316|
|58|`java.lang.NoSuchMethodError`|(Self explanatory)|12.3.3|316|
|59|`java.lang.OutOfMemoryError`|(Self explanatory)|12.5|313|
|60|`java.lang.InstantiationException`|(Self explanatory)|13.4.1|340|
|61|`java.lang.StackOverflowError`|(Self explanatory)|15.12.4.5|477|
|62|`java.lang.VerifyError`|(Self explanatory)|12.3.1|314|
|63|`java.lang.UnsatisfiedLinkError`|(Self explanatory)|12.3.3|316|
|64|`java.lang.VirtualMachineError`|(Self explanatory)|11.5.2|307|
