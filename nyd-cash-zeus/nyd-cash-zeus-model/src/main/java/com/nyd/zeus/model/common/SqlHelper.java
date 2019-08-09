package com.nyd.zeus.model.common;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;

import javax.persistence.Table;

import com.nyd.zeus.model.helibao.util.chanpay.ChkUtil;
import com.nyd.zeus.model.helibao.util.chanpay.DateUtils;

public class SqlHelper {

	public static String getInsertSqlByBean(Object obj) {
		Class<? extends Object> clazz = obj.getClass();
		Table table = clazz.getAnnotation(Table.class);
		String tableName = table.name();
		if (ChkUtil.isEmpty(tableName))
			return null;

		StringBuffer sb1 = new StringBuffer();
		sb1.append("insert into ").append(tableName).append("(");
		StringBuffer sb2 = new StringBuffer();
		sb2.append(") values (");
		StringBuffer sb3 = new StringBuffer();
		sb3.append(")");

		Field[] fieldArry = clazz.getDeclaredFields();
		for (Field field : fieldArry) {
			try {
				field.setAccessible(true);
				String fieldName = field.getName();
				if ("id".equals(fieldName.toLowerCase()))
					continue;
				sb1.append(getColumnName(fieldName)).append(", ");

				Object oVal = field.get(obj);
				if (ChkUtil.isEmpty(oVal)) {
					sb2.append("null").append(", ");
				} else {
					String type = field.getType().getName();
					if (type.endsWith("String")) {
						sb2.append("'").append(oVal).append("', ");
					} else if (type.endsWith("ecimal")
							|| type.toLowerCase().endsWith("long")
							|| type.endsWith("Integer")
							|| type.toLowerCase().endsWith("int")
							|| type.toLowerCase().endsWith("double")) {
						sb2.append(oVal).append(", ");
					} else if (type.endsWith("Date")) {
						Method m = (Method) obj.getClass().getMethod(
								getMethodName(fieldName));
						Date val = (Date) m.invoke(obj);
						sb2.append("'")
								.append(DateUtils
										.format(val, DateUtils.STYLE_1))
								.append("', ");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return sb1.substring(0, sb1.length() - 2)
				+ sb2.substring(0, sb2.length() - 2) + sb3.toString();
	}

	public static String getUpdateSqlByBean(Object obj) {
		Class<? extends Object> clazz = obj.getClass();
		Table table = clazz.getAnnotation(Table.class);
		String tableName = table.name();
		if (ChkUtil.isEmpty(tableName))
			return null;

		StringBuffer sb1 = new StringBuffer();
		sb1.append("update ").append(tableName).append(" set ");
		StringBuffer sb2 = new StringBuffer();
		sb2.append(" where ");

		Field[] fieldArry = clazz.getDeclaredFields();
		boolean hasId = false;
		for (Field field : fieldArry) {
			try {
				field.setAccessible(true);
				String fieldName = field.getName();
				Object oVal = field.get(obj);
				if ("id".equals(fieldName.toLowerCase())) {
					sb2.append("id = ").append(oVal);
					hasId = true;
					continue;
				}

				if (ChkUtil.isEmpty(oVal)) {
					sb1.append(fieldName).append(" = null").append(", ");
				} else {
					String type = field.getType().getName();
					if (type.endsWith("String")) {
						sb1.append(fieldName).append(" = '").append(oVal)
								.append("', ");
					} else if (type.endsWith("ecimal")
							|| type.toLowerCase().endsWith("long")
							|| type.endsWith("Integer")
							|| type.toLowerCase().endsWith("int")
							|| type.toLowerCase().endsWith("double")) {
						sb1.append(fieldName).append(" = ").append(oVal)
								.append(", ");
					} else if (type.endsWith("Date")) {
						Method m = (Method) obj.getClass().getMethod(
								getMethodName(fieldName));
						Date val = (Date) m.invoke(obj);
						sb1.append(fieldName)
								.append(" = '")
								.append(DateUtils
										.format(val, DateUtils.STYLE_1))
								.append("', ");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (!hasId)
			return null;
		return sb1.substring(0, sb1.length() - 2) + sb2.toString();
	}

	private static String upperCaseFirstCharacter(String str) {
		byte[] items = str.getBytes();
		items[0] = (byte) ((char) items[0] - 'a' + 'A');
		return new String(items);
	}

	private static String getMethodName(String fildeName) {
		return "get" + upperCaseFirstCharacter(fildeName);
	}

	private static String lowerCaseFirstCharacter(String str) {
		byte[] items = str.getBytes();
		items[0] = (byte) ((char) items[0] + 'a' - 'A');
		return new String(items);
	}

	private static String getColumnName(String fildeName) {
		String columnName = fildeName;
		int index = 0;
		for (int i = 0; i < fildeName.length(); i++) {
			if (Character.isUpperCase(fildeName.charAt(i))) {
				columnName = columnName.substring(0, i + index)
						+ "_"
						+ lowerCaseFirstCharacter(columnName.substring(i
								+ index, columnName.length()));
				index++;
			}
		}
		return columnName;
	}

	public static void main(String[] args) {
		System.out.println(getMethodName("aaa"));
		System.err.println(getColumnName("aBcDeF"));
	}

}
