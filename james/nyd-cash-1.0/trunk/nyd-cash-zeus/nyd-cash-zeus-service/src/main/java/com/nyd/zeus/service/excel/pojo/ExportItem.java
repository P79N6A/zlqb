package com.nyd.zeus.service.excel.pojo;

public class ExportItem {

	private String field; // 属性名
	private String display; // 显示名
	private short width; // 宽度
	private String convert;
	private short color;
	private String replace;
	private String  range;//数据有效性 下拉框

	public String getField() {
		return field;
	}

	public ExportItem setField(String field) {
		this.field = field;
		return this;
	}

	public String getDisplay() {
		return display;
	}

	public ExportItem setDisplay(String display) {
		this.display = display;
		return this;
	}

	public short getWidth() {
		return width;
	}

	public ExportItem setWidth(short width) {
		this.width = width;
		return this;
	}

	public String getConvert() {
		return convert;
	}

	public ExportItem setConvert(String convert) {
		this.convert = convert;
		return this;
	}

	public short getColor() {
		return color;
	}

	public ExportItem setColor(short color) {
		this.color = color;
		return this;
	}

	public String getReplace() {
		return replace;
	}

	public ExportItem setReplace(String replace) {
		this.replace = replace;
		return this;
	}

	public String getRange() {
		return range;
	}

	public ExportItem setRange(String range) {
		this.range = range;
		return this;
	}
}
