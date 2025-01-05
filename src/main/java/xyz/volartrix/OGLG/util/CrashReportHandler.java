package xyz.volartrix.OGLG.util;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.List;
import java.util.StringTokenizer;

public class CrashReportHandler {
    public static String crashReportHandler(Thread thread, Throwable throwable) {
        return "The game has crashed! This is the crash report\n" +
                "OS: " + System.getProperty("os.name") + "\n" +
                "Java Version: " + System.getProperty("java.version") + "\n" +
                "Architecture: " + System.getProperty("os.arch") + "\n" +
                "Thread: " + thread.getName() + "\n" +
                "Error: " + throwable.getMessage() + "\n" +
                getJVMArgs() + "\n" +

                "Stack Trace: " + getStackTraceAsString(throwable) + "\n"+
                getJavaLibraries() + "\n" + getNativeLibraries() + "\n";

    }

    public static String getStackTraceAsString(Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        throwable.printStackTrace(printWriter);
        return stringWriter.toString();
    }

    public static String getJVMArgs() {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();
        List<String> jvmArgs = runtimeMxBean.getInputArguments();
        printWriter.println("JVM Args: ");
        for (String jvmArg : jvmArgs) {
            printWriter.println("\t" + jvmArg);
        }
        return stringWriter.toString();
    }

    public static String getJavaLibraries() {
        String classpath = System.getProperty("java.class.path");
        StringTokenizer tokenizer = new StringTokenizer(classpath, File.pathSeparator);
        StringBuilder result = new StringBuilder();

        result.append("Java Libraries (JARs):\n");
        while (tokenizer.hasMoreTokens()) {
            String path = tokenizer.nextToken();
            if (path.endsWith(".jar")) {
                result.append(path).append("\n");
            }
        }
        return result.toString();
    }

    public static String getNativeLibraries() {
        StringBuilder result = new StringBuilder();
        result.append("Native Libraries:\n");

        String libraryPath = System.getProperty("java.library.path");
        StringTokenizer tokenizer = new StringTokenizer(libraryPath, File.pathSeparator);

        while (tokenizer.hasMoreTokens()) {
            String directory = tokenizer.nextToken();
            File dir = new File(directory);

            if (dir.exists() && dir.isDirectory()) {
                File[] files = dir.listFiles((dir1, name) -> name.endsWith(".dll") || name.endsWith(".so") || name.endsWith(".dylib"));
                if (files != null) {
                    for (File file : files) {
                        result.append(file.getName()).append("\n");
                    }
                }
            }
        }

        return result.toString();
    }
}
