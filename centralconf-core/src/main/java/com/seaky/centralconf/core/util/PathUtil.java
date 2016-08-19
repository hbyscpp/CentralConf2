package com.seaky.centralconf.core.util;

public class PathUtil {


  public static String lastPath(String path) {
    if (path == null)
      return null;

    int pathIndex = path.lastIndexOf("/");

    if (pathIndex == -1)
      return path;

    return path.substring(pathIndex + 1);
  }

  public static boolean isContainPathSeparator(String path) {

    return path.indexOf("/") != -1 || path.indexOf("\\") != -1;
  }
}
