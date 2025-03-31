package com.relaxed.test.poi;

import cn.hutool.core.io.FileUtil;
import com.deepoove.poi.data.AttachmentType;
import com.deepoove.poi.data.Texts;
import com.relaxed.poi.PoiDataHelper;
import com.relaxed.poi.PoiTemplate;
import com.relaxed.poi.PoiUtil;
import com.relaxed.poi.domain.AttachmentContentData;
import com.relaxed.poi.domain.ElementMeta;
import com.relaxed.poi.domain.HtmlContentData;
import com.relaxed.poi.domain.LabelData;
import com.relaxed.poi.domain.ListContentData;
import com.relaxed.poi.domain.LoopRowTableContentData;
import com.relaxed.poi.domain.PicContentData;
import com.relaxed.poi.domain.TableContentData;
import com.relaxed.poi.domain.TextContentData;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomUtils;
import org.ddr.poi.html.ElementRenderer;
import org.ddr.poi.html.HtmlRenderConfig;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Yakir
 * @Topic PoiTest
 * @Description http://deepoove.com/poi-tl
 * @date 2024/3/25 14:38
 * @Version 1.0
 */
@Slf4j
public class PoiTest {

	public static void main(String[] args) throws IOException {
		// 模板文件
		File file = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "template\\poi\\Poi测试模板.docx");
		// 附件
		File attentmentFile = ResourceUtils
				.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "template\\poi\\attentment.docx");
		// 输出文件
		File destDir = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "template\\poi");

		// 图片文件
		File imgFile = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "template\\poi\\test1.jpg");

		String templatePath = file.getAbsolutePath();

		File parentFileDir = FileUtil.mkdir(destDir + "\\dest");
		String destPath = FileUtil.file(parentFileDir, "test_" + RandomUtils.nextInt() + ".docx").getAbsolutePath();

		PoiTemplate poiTemplate = PoiTemplate.INSTANCE;
		InputStream inputStream = FileUtils.openInputStream(file);
		List<ElementMeta> elementMetas = PoiUtil.templateElement(inputStream);
		System.out.println("模板字段数据\n" + elementMetas);

		// 普通文本
		TextContentData textContentData = PoiDataHelper.Text.builder().labelName("username").content("张三").build();
		// 超链接
		TextContentData genderTextContentData = PoiDataHelper.Text.builder().labelName("gender").content("女")
				.url("https://www.baidu.com").build();
		// 自定义样式
		TextContentData ageTextContentData = PoiDataHelper.Text.builder().labelName("age")
				.custom(Texts.of("Sayi").color("00FF00").create()).build();
		// 图片
		PicContentData picContentData = PoiDataHelper.PICTURE.builder().labelName("image").width(100).height(100)
				.file(imgFile).build();
		// 表格
		TableContentData tableContentData = PoiDataHelper.TABLE.builder().labelName("table").headerStyle()
				.bgColor("4472C4").textColor("FFFFFF").center().build().addContent(Arrays.asList("科教1班", "1"))
				.addContent(Arrays.asList("幼儿3班", "6")).addContent(Arrays.asList("科教1班", "1"))
				.headers(Arrays.asList("班级", "排名")).build();
		// 列表
		ListContentData listContentData = PoiDataHelper.LIST.builder().labelName("list").addContent("排序1")
				.addContent("排序2").addContent("排序3").addContent("排序4").build();
		// 模板列表
		List<TestData> loopDatas = new ArrayList<>();
		loopDatas.add(mockTestLoopData());
		loopDatas.add(mockTestLoopData());
		LoopRowTableContentData loopRowTableContentData = PoiDataHelper.LOOP_ROW_TABLE.<TestData>builder()
				.labelName("policyDetails").prefix("[").suffix("]").dataStartRow(2).dataList(loopDatas).build();
		// 附件
		AttachmentContentData attachmentContentData = PoiDataHelper.ATTACHMENT.builder().labelName("attentment")
				.attachmentType(AttachmentType.DOCX).file(attentmentFile).build();

		HtmlContentData htmlContent = PoiDataHelper.HTML.builder().labelName("htmlContent")
				.content("<p style='color:red'>测试</p>").build();
		// 自定义标题样式
		// HtmlRenderConfig htmlRenderConfig = extHtmlConfig();
		// HtmlContentData htmlContent =
		// PoiDataHelper.HTML.builder().labelName("htmlContent")
		// .content("<p
		// style='color:red'>测试</p>").htmlRenderConfig(htmlRenderConfig).build();
		List<LabelData> contents = new ArrayList<>();
		contents.add(textContentData);
		contents.add(genderTextContentData);
		contents.add(ageTextContentData);
		contents.add(picContentData);
		contents.add(tableContentData);
		contents.add(listContentData);
		contents.add(loopRowTableContentData);
		contents.add(attachmentContentData);
		contents.add(htmlContent);
		// PoiGlobalConfig.setConfigureSupplier(new ConfigureSupplier() {
		// @Override
		// public Configure apply() {
		// ConfigureBuilder builder = Configure.builder();
		// builder.buildGramer("{{","}}");
		// return builder.build();
		// }
		// });

		PoiUtil.renderWord(new File(templatePath), new File(destPath), contents);
		log.info("渲染模板路径:{} \n输出路径:{}", templatePath, destPath);

	}

	public static HtmlRenderConfig extHtmlConfig() {
		HtmlRenderConfig config = new HtmlRenderConfig();
		List<ElementRenderer> customRenderers = config.getCustomRenderers();

		List<ElementRenderer> ext = new ArrayList<>();
		ext.add(new CustomHeaderRender());
		if (customRenderers != null) {
			ext.addAll(customRenderers);
		}

		config.setCustomRenderers(ext);
		return config;
	}

	private static TestData mockTestLoopData() {
		TestData testData = new TestData();
		testData.setSerialNumber("test_" + RandomUtils.nextInt() + "");
		testData.setInsCompany("112");
		testData.setLicensePlateNo("111");
		testData.setVehicleIdNo("111");
		testData.setPolicyholder("11");
		testData.setInsuredName("哈克楼");
		testData.setInsValidityDate("1");
		testData.setInsExpiryDate("1");
		testData.setBusInsAmt("11");
		testData.setClivtaInsAmtAndvehicleTaxAmt("11");
		return testData;
	}

}
