//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.relaxed.poi.plugins;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.exception.RenderException;
import com.deepoove.poi.policy.RenderPolicy;
import com.deepoove.poi.render.compute.EnvModel;
import com.deepoove.poi.render.compute.RenderDataCompute;
import com.deepoove.poi.render.processor.DocumentProcessor;
import com.deepoove.poi.render.processor.EnvIterator;
import com.deepoove.poi.resolver.TemplateResolver;
import com.deepoove.poi.template.ElementTemplate;
import com.deepoove.poi.template.MetaTemplate;
import com.deepoove.poi.template.run.RunTemplate;
import com.deepoove.poi.util.ReflectionUtils;
import com.deepoove.poi.util.TableTools;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVMerge;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STMerge;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class LoopRowTableRenderByHeaderPolicy implements RenderPolicy {

	private String prefix;

	private String suffix;

	// 数据模板行
	private Integer dataTemplateRowNum;

	public LoopRowTableRenderByHeaderPolicy() {
		this(null);
	}

	public LoopRowTableRenderByHeaderPolicy(Integer dataTemplateRowNum) {
		this("[", "]", dataTemplateRowNum);
	}

	public LoopRowTableRenderByHeaderPolicy(String prefix, String suffix) {
		this(prefix, suffix, null);
	}

	public LoopRowTableRenderByHeaderPolicy(String prefix, String suffix, Integer dataTemplateRowNum) {
		this.prefix = prefix;
		this.suffix = suffix;
		this.dataTemplateRowNum = dataTemplateRowNum;
	}

	@Override
	public void render(ElementTemplate eleTemplate, Object data, XWPFTemplate template) {
		RunTemplate runTemplate = (RunTemplate) eleTemplate;
		XWPFRun run = runTemplate.getRun();

		try {
			if (!TableTools.isInsideTable(run)) {
				throw new IllegalStateException(
						"The template tag " + runTemplate.getSource() + " must be inside a table");
			}
			else {
				XWPFTableCell tagCell = (XWPFTableCell) ((XWPFParagraph) run.getParent()).getBody();
				XWPFTable table = tagCell.getTableRow().getTable();
				run.setText("", 0);
				int templateRowIndex = this.getTemplateRowIndex(tagCell);
				if (null != data && data instanceof Iterable) {
					Iterator<?> iterator = ((Iterable) data).iterator();
					XWPFTableRow templateRow = table.getRow(templateRowIndex);
					TemplateResolver resolver = new TemplateResolver(
							template.getConfig().copy(this.prefix, this.suffix));
					boolean firstFlag = true;
					int index = 0;
					boolean hasNext = iterator.hasNext();

					while (hasNext) {
						Object root = iterator.next();
						hasNext = iterator.hasNext();
						int insertPosition = templateRowIndex++;
						table.insertNewTableRow(insertPosition);
						this.setTableRow(table, templateRow, insertPosition);
						XmlCursor newCursor = templateRow.getCtRow().newCursor();
						newCursor.toPrevSibling();
						XmlObject object = newCursor.getObject();
						XWPFTableRow nextRow = new XWPFTableRow((CTRow) object, table);
						if (!firstFlag) {
							List<XWPFTableCell> tableCells = nextRow.getTableCells();
							Iterator var21 = tableCells.iterator();

							while (var21.hasNext()) {
								XWPFTableCell cell = (XWPFTableCell) var21.next();
								CTTcPr tcPr = TableTools.getTcPr(cell);
								CTVMerge vMerge = tcPr.getVMerge();
								if (null != vMerge && STMerge.RESTART == vMerge.getVal()) {
									vMerge.setVal(STMerge.CONTINUE);
								}
							}
						}
						else {
							firstFlag = false;
						}

						this.setTableRow(table, nextRow, insertPosition);
						RenderDataCompute dataCompute = template.getConfig().getRenderDataComputeFactory()
								.newCompute(EnvModel.of(root, EnvIterator.makeEnv(index++, hasNext)));
						List<XWPFTableCell> cells = nextRow.getTableCells();
						cells.forEach((cellx) -> {
							List<MetaTemplate> templates = resolver.resolveBodyElements(cellx.getBodyElements());
							(new DocumentProcessor(template, resolver, dataCompute)).process(templates);
						});
					}
				}

				table.removeRow(templateRowIndex);
				this.afterloop(table, data);
			}
		}
		catch (Exception var25) {
			throw new RenderException("HackLoopTable for " + eleTemplate + "error: " + var25.getMessage(), var25);
		}
	}

	private int getTemplateRowIndex(XWPFTableCell tagCell) {
		XWPFTableRow tagRow = tagCell.getTableRow();
		return Objects.isNull(this.dataTemplateRowNum) ? this.getRowIndex(tagRow) + 1 : this.dataTemplateRowNum;
	}

	protected void afterloop(XWPFTable table, Object data) {
	}

	private void setTableRow(XWPFTable table, XWPFTableRow templateRow, int pos) {
		List<XWPFTableRow> rows = (List) ReflectionUtils.getValue("tableRows", table);
		rows.set(pos, templateRow);
		table.getCTTbl().setTrArray(pos, templateRow.getCtRow());
	}

	private int getRowIndex(XWPFTableRow row) {
		List<XWPFTableRow> rows = row.getTable().getRows();
		return rows.indexOf(row);
	}

}
