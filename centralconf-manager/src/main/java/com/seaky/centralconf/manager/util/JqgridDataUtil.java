package com.seaky.centralconf.manager.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.seaky.centralconf.core.ConfigItem;
import com.seaky.centralconf.core.ResourceItem;
import com.seaky.centralconf.manager.web.vo.Result;
import com.seaky.centralconf.manager.web.vo.Rows;

public class JqgridDataUtil {


  public static Result getJqgridData(List<String> list) {
    Result r = new Result();

    List<Rows> rows = new ArrayList<Rows>();
    if (list != null) {
      for (String s : list) {
        Rows row = new Rows();
        row.setId(s);
        row.setCell(new String[] {StringUtils.isNotEmpty(s) ? s.trim() : ""});
        rows.add(row);
      }
    }
    r.setRows(rows);
    return r;

  }


  //
  // if (null == list || list.size() < 1) {
  // return r;
  // }
  // StringBuffer sb = new StringBuffer();
  // sb.append("{\"rows\":[");
  // for (String s : list) {
  // sb.append("{").append("\"id\":\"").append(StringUtils.isNotEmpty(s) ? s.trim() : "" + "")
  // .append("\",").append("\"cell\":").append("[").append("\"")
  // .append(StringUtils.isNotEmpty(s) ? s.trim() : "").append("\"").append("]").append("}")
  // .append(",");
  // }
  // String temp = sb.toString();
  // String s = temp.substring(0, temp.length() - 1);
  // s = s + "]}";
  // return s.replace("\\", "\\\\");

  public static Result getJqgridDataByConfItem(List<ConfigItem> items) {
    Result r = new Result();

    List<Rows> rows = new ArrayList<Rows>();
    if (items != null) {
      for (ConfigItem item : items) {
        Rows row = new Rows();
        row.setId(item.getKey());
        row.setCell(new String[] {StringUtils.isNotEmpty(item.getKey()) ? item.getKey().trim() : "",
            StringUtils.isNotEmpty(item.getValue()) ? item.getValue().trim() : "",
            StringUtils.isNotEmpty(item.getDesc()) ? item.getDesc().trim() : ""});
        rows.add(row);
      }
    }
    r.setRows(rows);
    return r;
  }


  // public static String getJqgridDataByConfItem(List<ConfigItem> items) {
  // if (null == items || items.size() < 1) {
  // return "{\"rows\":[]}";
  // }
  // StringBuffer sb = new StringBuffer();
  // sb.append("{\"rows\":[");
  // for (ConfigItem item : items) {
  // sb.append("{").append("\"id\":\"").append(item.getKey() + "").append("\",")
  // .append("\"cell\":").append("[").append("\"")
  // .append(StringUtils.isNotEmpty(item.getKey()) ? item.getKey().trim() : "").append("\",")
  // .append("\"")
  // .append(StringUtils.isNotEmpty(item.getValue()) ? item.getValue().trim() : "")
  // .append("\",").append("\"")
  // .append(StringUtils.isNotEmpty(item.getDesc()) ? item.getDesc().trim() : "").append("\"")
  // .append("]").append("}").append(",");
  // }
  // String temp = sb.toString();
  // String s = temp.substring(0, temp.length() - 1);
  // s = s + "]}";
  // return s.replace("\\", "\\\\");
  // }


  // public static String getJqgridDataByResourceItem(List<ResourceItem> items, boolean isShowName)
  // {
  // if (null == items || items.size() < 1) {
  // return "{\"rows\":[]}";
  // }
  // StringBuffer sb = new StringBuffer();
  // sb.append("{\"rows\":[");
  // for (ResourceItem item : items) {
  // sb.append("{").append("\"id\":\"").append(item.getKey() + "").append("\",")
  // .append("\"cell\":").append("[");
  // if (isShowName) {
  // sb.append("\"").append(StringUtils.isNotEmpty(item.getRsc()) ? item.getRsc().trim() : "")
  // .append("\",");
  // }
  // sb.append("\"").append(StringUtils.isNotEmpty(item.getKey()) ? item.getKey().trim() : "")
  // .append("\",").append("\"")
  // .append(StringUtils.isNotEmpty(item.getValue()) ? item.getValue().trim() : "")
  // .append("\",").append("\"")
  // .append(StringUtils.isNotEmpty(item.getDesc()) ? item.getDesc().trim() : "").append("\"")
  // .append("]").append("}").append(",");
  // }
  // String temp = sb.toString();
  // String s = temp.substring(0, temp.length() - 1);
  // s = s + "]}";
  // return s.replace("\\", "\\\\");
  // }

  public static Result getJqgridDataByResourceItem(List<ResourceItem> items, boolean isShowName) {
    Result r = new Result();

    List<Rows> rows = new ArrayList<Rows>();
    if (items != null) {
      for (ResourceItem item : items) {
        Rows row = new Rows();
        row.setId(item.getKey());
        if (isShowName) {
          row.setCell(
              new String[] {StringUtils.isNotEmpty(item.getRsc()) ? item.getRsc().trim() : "",
                  StringUtils.isNotEmpty(item.getKey()) ? item.getKey().trim() : "",
                  StringUtils.isNotEmpty(item.getValue()) ? item.getValue().trim() : "",
                  StringUtils.isNotEmpty(item.getDesc()) ? item.getDesc().trim() : ""});
        } else {
          row.setCell(
              new String[] {StringUtils.isNotEmpty(item.getKey()) ? item.getKey().trim() : "",
                  StringUtils.isNotEmpty(item.getValue()) ? item.getValue().trim() : "",
                  StringUtils.isNotEmpty(item.getDesc()) ? item.getDesc().trim() : ""});
        }

        rows.add(row);
      }
    }
    r.setRows(rows);
    return r;
  }

  // public static String getJqgridDataByResourceItem(List<ResourceItem> items, boolean isShowName)
  // {
  // if (null == items || items.size() < 1) {
  // return "{\"rows\":[]}";
  // }
  // StringBuffer sb = new StringBuffer();
  // sb.append("{\"rows\":[");
  // for (ResourceItem item : items) {
  // sb.append("{").append("\"id\":\"").append(item.getKey() + "").append("\",")
  // .append("\"cell\":").append("[");
  // if (isShowName) {
  // sb.append("\"").append(StringUtils.isNotEmpty(item.getRsc()) ? item.getRsc().trim() : "")
  // .append("\",");
  // }
  // sb.append("\"").append(StringUtils.isNotEmpty(item.getKey()) ? item.getKey().trim() : "")
  // .append("\",").append("\"")
  // .append(StringUtils.isNotEmpty(item.getValue()) ? item.getValue().trim() : "")
  // .append("\",").append("\"")
  // .append(StringUtils.isNotEmpty(item.getDesc()) ? item.getDesc().trim() : "").append("\"")
  // .append("]").append("}").append(",");
  // }
  // String temp = sb.toString();
  // String s = temp.substring(0, temp.length() - 1);
  // s = s + "]}";
  // return s.replace("\\", "\\\\");
  // }
}
