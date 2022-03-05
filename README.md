How to reproduce:

File `Unzipper.java` is a simple unzipper that decompresses the zip file into the specified directory.

Here is a jar known to reproduce the problem:
[lwjgl-glfw-3.2.2-natives-windows.jar](https://repo1.maven.org/maven2/org/lwjgl/lwjgl-glfw/3.2.2/lwjgl-glfw-3.2.2-natives-windows.jar)

You should reuse `Unzipper` like this, and you will see the exception printed on the second eval:

```
java Unzipper.java lwjgl-glfw-3.2.2-natives-windows.jar <samba-shared-folder-path>
```

The samba shared folder path should be a folder shared through samba, which can be a UNC path or mounted as a drive.

It seems that not all devices can reproduce the problem with this demo, I am looking for a stable way to reproduce it.