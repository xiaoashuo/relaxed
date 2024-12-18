package com.relaxed.poi;

import com.deepoove.poi.data.AttachmentType;
import com.deepoove.poi.data.HyperlinkTextRenderData;
import com.deepoove.poi.data.NumberingFormat;
import com.deepoove.poi.data.RowRenderData;
import com.deepoove.poi.data.Rows;
import com.deepoove.poi.data.TextRenderData;

import com.relaxed.poi.domain.AttachmentContentData;
import com.relaxed.poi.domain.HtmlContentData;
import com.relaxed.poi.domain.ListContentData;
import com.relaxed.poi.domain.LoopRowTableContentData;
import com.relaxed.poi.domain.PicContentData;
import com.relaxed.poi.domain.TableContentData;
import com.relaxed.poi.domain.TextContentData;
import com.relaxed.poi.enums.ContentTypeEnum;
import com.relaxed.poi.exception.WordException;
import org.apache.commons.lang3.StringUtils;
import org.ddr.poi.html.HtmlRenderConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Yakir
 * @Topic PoiDataHelper
 * @Description
 * @date 2024/3/27 11:33
 * @Version 1.0
 */
public class PoiDataHelper {

	public static class Text {

		/**
		 * 字段名
		 */
		private String labelName;

		/**
		 * 纯文本内容/超链接内容
		 */
		private String content;

		/**
		 * 超链接 url
		 */
		private String url;

		/**
		 * 自定义渲染文本
		 */
		private TextRenderData renderData;

		public static Text builder() {
			return new Text();
		}

		public Text labelName(String labelName) {
			this.labelName = labelName;
			return this;
		}

		public Text content(String content) {
			this.content = content;
			return this;
		}

		public Text url(String url) {
			this.url = url;
			return this;
		}

		public Text custom(TextRenderData renderData) {
			this.renderData = renderData;
			return this;
		}

		public TextContentData build() {
			// 优先级 自定义->超链接->普通文本
			TextContentData textContentData = new TextContentData();
			textContentData.setContentType(ContentTypeEnum.TEXT.name());
			textContentData.setLabelName(this.labelName);
			if (this.renderData != null) {
				textContentData.setRenderData(this.renderData);
				return textContentData;
			}
			if (StringUtils.isBlank(this.url)) {
				textContentData.setContent(this.content);
			}
			else {
				textContentData.setLinkData(new HyperlinkTextRenderData(this.content, this.url));
			}

			return textContentData;
		}

	}

	public static class PICTURE {

		/**
		 * 字段名
		 */
		private String labelName;

		/**
		 * 图片宽度
		 */
		private Integer width;

		/**
		 * 图片高度
		 */
		private Integer height;

		/**
		 * 图片地址（网络图片插入时使用）
		 */
		private String picUrl;

		/**
		 * 本地图片文件
		 */
		private File localFile;

		public static PICTURE builder() {
			return new PICTURE();
		}

		public PICTURE labelName(String labelName) {
			this.labelName = labelName;
			return this;
		}

		public PICTURE width(Integer width) {
			this.width = width;
			return this;
		}

		public PICTURE height(Integer height) {
			this.height = height;
			return this;
		}

		public PICTURE picUrl(String picUrl) {
			this.picUrl = picUrl;
			return this;
		}

		public PICTURE file(File localFile) {
			this.localFile = localFile;
			return this;
		}

		public PicContentData build() {
			// 效验基本参数
			if (StringUtils.isBlank(this.picUrl) && this.localFile == null) {
				throw new WordException("图片地址不能为空");
			}
			// 优先级 远程图片->本地图片
			PicContentData picContentData = new PicContentData();
			picContentData.setContentType(ContentTypeEnum.PICTURE.name());
			picContentData.setLabelName(this.labelName);
			picContentData.setWidth(this.width);
			picContentData.setHeight(this.height);
			picContentData.setPicUrl(this.picUrl);
			picContentData.setLocalFile(this.localFile);
			return picContentData;
		}

	}

	public static class TABLE {

		/**
		 * 字段名
		 */
		private String labelName;

		/**
		 * 表头列
		 */
		private List<String> headers;

		/**
		 * 头样式
		 */
		private HeaderStyle headerStyle;

		/**
		 * 表行内容
		 */
		private List<List<String>> contents = new ArrayList<>();

		public static TABLE builder() {
			return new TABLE();
		}

		public TABLE labelName(String labelName) {
			this.labelName = labelName;
			return this;
		}

		public TABLE headers(List<String> headers) {
			this.headers = headers;
			return this;
		}

		public HeaderStyle headerStyle() {
			this.headerStyle = new HeaderStyle(this);
			return this.headerStyle;
		}

		public TABLE addContent(List<String> rowData) {
			this.contents.add(rowData);
			return this;
		}

		public static class HeaderStyle {

			private TABLE _parent;

			private String textColor;

			private String bgColor;

			private Boolean isBold;

			private Boolean center;

			private HeaderStyle(TABLE table) {
				this._parent = table;
			}

			public HeaderStyle textColor(String textColor) {
				this.textColor = textColor;
				return this;
			}

			public HeaderStyle bgColor(String bgColor) {
				this.bgColor = bgColor;
				return this;
			}

			public HeaderStyle isBold() {
				this.isBold = true;
				return this;
			}

			public HeaderStyle center() {
				this.center = true;
				return this;
			}

			public TABLE build() {
				return _parent;
			}

			public String getBgColor() {
				return bgColor;
			}

			public String getTextColor() {
				return textColor;
			}

			public Boolean getBold() {
				return isBold;
			}

			public Boolean getCenter() {
				return center;
			}

		}

		public TableContentData build() {
			// 效验数据合法性
			if (headers == null) {
				throw new WordException("表头参数暂未设置,请填写.");
			}
			int headerSize = headers.size();
			this.contents.forEach(items -> {
				if (headerSize != items.size()) {
					throw new WordException("表头长度与数据长度不匹配,若数据为空请留空值");
				}
			});
			// 生成渲染表格数据
			TableContentData tableContentData = new TableContentData();
			tableContentData.setLabelName(this.labelName);
			tableContentData.setContentType(ContentTypeEnum.TABLE.name());
			Rows.RowBuilder rowBuilder = Rows.of(this.headers.toArray(new String[0]));
			// 填充头样式
			fillHeaderStyle(rowBuilder);
			tableContentData.setHeader(rowBuilder.create());
			// 设置内容
			List<RowRenderData> datas = new ArrayList<>();
			this.contents.forEach(items -> datas.add(Rows.create(items.toArray(new String[0]))));
			tableContentData.setContents(datas);
			return tableContentData;
		}

		private void fillHeaderStyle(Rows.RowBuilder rowBuilder) {
			if (this.headerStyle != null) {
				if (StringUtils.isNotBlank(headerStyle.getBgColor())) {
					rowBuilder.bgColor(headerStyle.getBgColor());
				}
				if (StringUtils.isNotBlank(headerStyle.getTextColor())) {
					rowBuilder.textColor(headerStyle.getTextColor());
				}
				if (headerStyle.getBold() != null) {
					if (Boolean.TRUE.equals(headerStyle.getBold())) {
						rowBuilder.textBold();
					}
				}
				if (headerStyle.center != null) {
					if (Boolean.TRUE.equals(headerStyle.getCenter())) {
						rowBuilder.center();
					}
				}
			}
		}

	}

	public static class LOOP_ROW_TABLE<T> {

		/**
		 * 字段名
		 */
		private String labelName;

		/**
		 * 数据模板前缀
		 */
		private String prefix = "[";

		/**
		 * 数据模板后缀
		 */
		private String suffix = "]";

		/**
		 * 数据起始行从0开始
		 */
		private Integer dataStartRow = 1;

		private List<T> dataList;

		public static <T> LOOP_ROW_TABLE<T> builder() {
			return new LOOP_ROW_TABLE<>();
		}

		public static <T> LOOP_ROW_TABLE<T> builder(Class<T> entity) {
			return new LOOP_ROW_TABLE<>();
		}

		public LOOP_ROW_TABLE<T> labelName(String labelName) {
			this.labelName = labelName;
			return this;
		}

		public LOOP_ROW_TABLE<T> prefix(String prefix) {
			this.prefix = prefix;
			return this;
		}

		public LOOP_ROW_TABLE<T> suffix(String suffix) {
			this.suffix = suffix;
			return this;
		}

		public LOOP_ROW_TABLE<T> dataStartRow(Integer dataStartRow) {
			this.dataStartRow = dataStartRow;
			return this;
		}

		public LOOP_ROW_TABLE<T> dataList(List<T> dataList) {
			this.dataList = dataList;
			return this;
		}

		public LoopRowTableContentData build() {
			LoopRowTableContentData<T> loopRowTableContentData = new LoopRowTableContentData<>();
			loopRowTableContentData.setPrefix(this.prefix);
			loopRowTableContentData.setSuffix(this.suffix);
			loopRowTableContentData.setDataStartRowNum(this.dataStartRow);
			loopRowTableContentData.setLabelName(this.labelName);
			loopRowTableContentData.setContentType(ContentTypeEnum.LOOP_ROW_TABLE.name());
			loopRowTableContentData.setDataList(this.dataList);
			return loopRowTableContentData;
		}

	}

	public static class LIST {

		/**
		 * 字段名
		 */
		private String labelName;

		/**
		 * 纯文本内容/超链接内容
		 */
		private List<String> contents = new ArrayList<>();

		/**
		 * 列表样式,支持罗马字符、有序无序等,默认为点
		 */
		private NumberingFormat numberingFormat = NumberingFormat.LOWER_LETTER;

		public static LIST builder() {
			return new LIST();
		}

		public LIST labelName(String labelName) {
			this.labelName = labelName;
			return this;
		}

		public LIST addContent(String content) {
			this.contents.add(content);
			return this;
		}

		public LIST numberingFormat(NumberingFormat numberingFormat) {
			this.numberingFormat = numberingFormat;
			return this;
		}

		public ListContentData build() {
			ListContentData listContentData = new ListContentData();
			listContentData.setLabelName(this.labelName);
			listContentData.setContentType(ContentTypeEnum.LIST.name());
			listContentData.setNumberingFormat(this.numberingFormat);
			List<TextRenderData> listData = this.contents.stream().map(TextRenderData::new)
					.collect(Collectors.toList());
			listContentData.setList(listData);
			return listContentData;
		}

	}

	public static class ATTACHMENT {

		/**
		 * 字段名
		 */
		private String labelName;

		/**
		 * 本地文件
		 */
		private File localFile;

		/**
		 * 附件类型
		 */

		private AttachmentType attachmentType;

		public static ATTACHMENT builder() {
			return new ATTACHMENT();
		}

		public ATTACHMENT labelName(String labelName) {
			this.labelName = labelName;
			return this;
		}

		public ATTACHMENT file(File localFile) {
			this.localFile = localFile;
			return this;
		}

		public ATTACHMENT attachmentType(AttachmentType attachmentType) {
			this.attachmentType = attachmentType;
			return this;
		}

		public AttachmentContentData build() {
			// 效验基本参数
			if (this.localFile == null) {
				throw new WordException("图片文件不能为空");
			}
			if (this.attachmentType == null) {
				throw new WordException("文件类型不能为空");
			}
			AttachmentContentData attachmentContentData = new AttachmentContentData();
			attachmentContentData.setAttachmentType(attachmentType);
			attachmentContentData.setLocalFile(this.localFile);
			attachmentContentData.setLabelName(this.labelName);
			attachmentContentData.setContentType(ContentTypeEnum.ATTACHMENT.name());
			return attachmentContentData;
		}

	}

	public static class HTML {

		/**
		 * 字段名
		 */
		private String labelName;

		/**
		 * html内容
		 */
		private String content;

		/**
		 * html渲染配置
		 */
		private HtmlRenderConfig htmlRenderConfig;

		public static HTML builder() {
			return new HTML();
		}

		public HTML labelName(String labelName) {
			this.labelName = labelName;
			return this;
		}

		public HTML content(String content) {
			this.content = content;
			return this;
		}

		public HTML htmlRenderConfig(HtmlRenderConfig htmlRenderConfig) {
			this.htmlRenderConfig = htmlRenderConfig;
			return this;
		}

		public HtmlContentData build() {
			HtmlContentData htmlContentData = new HtmlContentData();
			htmlContentData.setContentType(ContentTypeEnum.HTML.name());
			htmlContentData.setLabelName(this.labelName);
			htmlContentData.setContent(this.content);
			if (this.htmlRenderConfig != null) {
				htmlContentData.setHtmlRenderConfig(this.htmlRenderConfig);
			}

			return htmlContentData;
		}

	}

}
