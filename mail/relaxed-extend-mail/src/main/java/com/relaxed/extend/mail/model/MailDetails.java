package com.relaxed.extend.mail.model;

import lombok.Data;

import java.io.File;

/**
 * 邮件详细信息类，包含邮件的所有必要信息。 包括发件人、收件人、主题、内容、附件等。
 *
 * @author Hccake
 * @since 1.0
 */
@Data
public class MailDetails {

	/**
	 * 发件人邮箱地址
	 */
	private String from;

	/**
	 * 收件人邮箱地址数组
	 */
	private String[] to;

	/**
	 * 邮件主题
	 */
	private String subject;

	/**
	 * 是否将内容渲染为HTML格式
	 */
	private Boolean showHtml;

	/**
	 * 邮件正文内容
	 */
	private String content;

	/**
	 * 抄送人邮箱地址数组
	 */
	private String[] cc;

	/**
	 * 密送人邮箱地址数组
	 */
	private String[] bcc;

	/**
	 * 邮件附件文件数组
	 */
	private File[] files;

}
