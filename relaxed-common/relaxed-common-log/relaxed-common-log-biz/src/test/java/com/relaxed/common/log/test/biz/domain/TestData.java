package com.relaxed.common.log.test.biz.domain;

import com.relaxed.common.log.biz.annotation.LogDiffTag;
import com.relaxed.common.log.biz.extractor.EntityTypeExtractor;
import com.relaxed.common.log.biz.extractor.json.JsonTypeExtractor;
import com.relaxed.common.log.biz.extractor.richtext.RichTextTypeExtractor;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @author Yakir
 * @Topic TestData
 * @Description
 * @date 2023/12/21 11:32
 * @Version 1.0
 */
@Data
public class TestData {

	@LogDiffTag(alias = "主键")
	private Integer id;

	@LogDiffTag(alias = "用户名")
	private String username;

	private String password;

	@LogDiffTag(alias = "用户名", extractor = EntityTypeExtractor.class)
	private InnerData innerData;

	@LogDiffTag(alias = "json参数", extractor = JsonTypeExtractor.class)
	private String jsonParam;

	/**
	 * 富文本
	 */
	@LogDiffTag(alias = "富文本", extractor = RichTextTypeExtractor.class)
	private String richText;

	@Data
	public static class InnerData {

		private String gender;

		private InnerDataText innerDataText;

	}

	@Data
	public static class InnerDataText {

		private String data;

		private List<Teacher> teachers;

	}

	@AllArgsConstructor
	@Data
	public static class Teacher {

		private String name;

	}

}
