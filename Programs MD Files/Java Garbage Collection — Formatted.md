Java Garbage Collection — Formatted Notes



\# Garbage Collection in Java



&#x20;In languages where memory is managed manually, the memory allocated to an object has to be explicitly released by the programmer using the `free` and `delete` operators. However, releasing memory manually can cause two major problems:



&#x20;- \*\*Releasing the memory too early\*\* can cause a \*\*dangling pointer/reference situation\*\*.

\- \*\*Failing to release the memory\*\* can cause a \*\*memory leak\*\*.



&#x20;To overcome these drawbacks, \*\*Java eliminated the use of the `delete` operator\*\* and decided to manage the destruction of objects automatically.



&#x20;This was done using a module of the \*\*JVM (Java Virtual Machine)\*\* called the \*\*Garbage Collector\*\*.



&#x20;## Reference Counting



&#x20;In order to identify which objects can be garbage collected, the JVM uses the principle of \*\*reference counting\*\*.



&#x20;Reference counting keeps track of the total number of references that are pointing to an object.



&#x20;- Each time a reference refers to an object, the object's reference count is \*\*increased by one\*\*.

\- Each time a reference stops referring to an object, the reference count is \*\*decreased by one\*\*.

\- Once the reference count of an object falls to \*\*0\*\*, that object is marked for \*\*garbage collection\*\*.



&#x20;## Garbage Collector



&#x20;The \*\*Garbage Collector (GC)\*\* is a low-priority thread that runs in the background. It is responsible for destroying objects that have been marked for garbage collection and releasing the memory occupied by them.



&#x20;A programmer \*\*cannot directly invoke or control the garbage collector\*\*. However, a request can be made to the JVM to run garbage collection using:



```

System.gc();

```



&#x20;`System.gc()` only \*\*requests\*\* the JVM to perform garbage collection; it does not guarantee that garbage collection will happen immediately.



&#x20;\*\*Small accuracy note:\*\* your notes describe garbage collection using \*\*reference counting\*\*, but modern Java/JVM garbage collectors generally determine reachability using \*\*tracing/reachability analysis\*\*, not simple reference counting. I kept your original concept organized above, but that distinction may matter for an exam.



**Object Destruction and Finalization**



All objects in Java are allocated by the JVM on the managed heap and are automatically eligible for garbage collection when they are no longer reachable.



However, it is not possible to determine exactly when the Garbage Collector will destroy an object. Because of this, Java does not support the concept of destructors like some other programming languages.



To address the need for cleanup, the Object class, which is the parent class of all classes in Java, historically provided a method called finalize().



Ideally, the Garbage Collector could invoke the finalize() method on an object just before the object was destroyed. This process was called finalization.



However, it is not possible to determine when, or even if, the Garbage Collector will run. Therefore, Java documentation states that programs should not rely on the invocation of finalize() for releasing resources.



Instead, programmers should explicitly define mechanisms to release resources themselves, such as closing files, database connections, and other system resources.



Important Point



The finalize() method is deprecated and should not be relied upon in modern Java. Resource management should be handled explicitly, commonly using mechanisms such as try-with-resources.

