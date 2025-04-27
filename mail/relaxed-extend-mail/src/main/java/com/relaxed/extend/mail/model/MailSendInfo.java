package com.relaxed.extend.mail.model;

import java.time.LocalDateTime;

/**
 * 邮件发送结果信息类，记录邮件发送的详细结果。 包含发送时间、是否成功、错误信息等。
 *
 * @author Hccake
 * @since 1.0
 */
public class MailSendInfo {

	/**
	 * 邮件详细信息
	 */
	private final MailDetails mailDetails;

	/**
	 * 邮件发送时间
	 */
	private LocalDateTime sentDate;

	/**
	 * 发送是否成功
	 */
	private Boolean success;

	/**
	 * 发送失败时的错误信息
	 */
	private String errorMsg;

	/**
	 * 将原始异常抛出
	 */
	private Throwable throwable;

	/**
	 * 创建邮件发送结果信息
	 * @param mailDetails 邮件详细信息
	 */
	public MailSendInfo(MailDetails mailDetails) {
		this.mailDetails = mailDetails;
	}

	/**
	 * 获取邮件详细信息
	 * @return 邮件详细信息
	 */
	public MailDetails getMailDetails() {
		return mailDetails;
	}

	/**
	 * 获取邮件发送时间
	 * @return 发送时间
	 */
	public LocalDateTime getSentDate() {
		return sentDate;
	}

	/**
	 * 设置邮件发送时间
	 * @param sentDate 发送时间
	 */
	public void setSentDate(LocalDateTime sentDate) {
		this.sentDate = sentDate;
	}

	/**
	 * 获取发送是否成功
	 * @return 是否成功
	 */
	public Boolean getSuccess() {
		return success;
	}

	/**
	 * 设置发送是否成功
	 * @param success 是否成功
	 */
	public void setSuccess(Boolean success) {
		this.success = success;
	}

	/**
	 * 获取错误信息
	 * @return 错误信息
	 */
	public String getErrorMsg() {
		return errorMsg;
	}

	/**
	 * 设置错误信息
	 * @param errorMsg 错误信息
	 */
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	/**
	 * 获取异常堆栈
	 * @return 异常堆栈
	 */
	public Throwable getThrowable() {
		return throwable;
	}

	/**
	 * 设置异常堆栈
	 * @param throwable 原始异常
	 */
	public void setThrowable(Throwable throwable) {
		this.throwable = throwable;
	}

}
