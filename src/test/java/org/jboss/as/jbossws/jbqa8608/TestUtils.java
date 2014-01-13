package org.jboss.as.jbossws.jbqa8608;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;

public class TestUtils {

  private static final String BACKUP_ARCHIVE_PATH = "backupArchivePath";
  private static final String BACKUP_ARCHIVE_FOR_DEBUG = "backupArchiveForDebug";
  public static int readIntValueFromSystemProperties(String name, int defaultValue) {
    String value = System.getProperty(name);
    if (!(value == null || value.isEmpty())) {
      try {
        return Integer.parseInt(value);
      } catch (Exception e) {
        System.err.println("Can not parse int value from system property " + name + " with value " + value + " " + e.getMessage());
        e.printStackTrace(System.err);
      }
    }
    return defaultValue;
  }

  public static String readStringValueFromSystemProperties(String name, String defaultValue) {
    String value = System.getProperty(name);
    return value == null ? defaultValue : value;
  }

  public static final String JBOSS_BIND_PORT = "jboss.bind.port";
  public static final String JBOSS_BIND_ADDRESS = "jboss.bind.address";
  public static String getServerBindAddress() {
    return readStringValueFromSystemProperties(JBOSS_BIND_ADDRESS, "localhost");
  }

  public static int getServerBindPort() {
    return readIntValueFromSystemProperties(JBOSS_BIND_PORT, 8080);
  }

  public static <T extends Archive<T>> T backupArchiveForDebug(T archive) {
    if (readIntValueFromSystemProperties(BACKUP_ARCHIVE_FOR_DEBUG, 0) == 1) {
      File backupArchiveDir = new File(readStringValueFromSystemProperties(BACKUP_ARCHIVE_PATH, "target"));
      File file = new File(backupArchiveDir, archive.getName());
      archive.as(ZipExporter.class).exportTo(file, true);
      System.err.println("Backuped archive " + file.getAbsolutePath());
    }
    return archive;
  }

  public static String createNamespaceFromPackageOfClass(Class<?> clazz) {
    String packageName = clazz.getPackage().getName();
    String names [] = packageName.split("\\.");
    List<String> namesInReverseOrder = new ArrayList<String>();
    namesInReverseOrder.addAll(Arrays.asList(names));
    Collections.reverse(namesInReverseOrder);
    StringBuilder name = new StringBuilder();
    name.append("http://");
    boolean first = true;
    for (String part : namesInReverseOrder) {
      if (!first)
        name.append(".");
      else
        first = false;
      name.append(part);
    }
    name.append("/");
    return name.toString();
  }
}
