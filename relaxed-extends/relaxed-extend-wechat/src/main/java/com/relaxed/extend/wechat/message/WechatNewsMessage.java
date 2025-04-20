package com.relaxed.extend.wechat.message;

import com.relaxed.extend.wechat.enums.MessageTypeEnum;
import com.relaxed.extend.wechat.request.WechatParams;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * 企业微信图文消息类。 用于发送图文类型的消息，支持多条图文组合。
 *
 * @author Yakir
 * @since 1.0
 */
@Getter
@Accessors(chain = true)
public class WechatNewsMessage extends AbstractWechatMessage {

	/**
	 * 图文消息最大条数限制
	 */
	private static final Integer MAX_ARTICLE_LIMIT = 8;

	/**
	 * 图文消息列表，一个图文消息支持1到8条图文
	 */
	private List<Article> articles = new ArrayList<>();

	@Override
	public MessageTypeEnum getType() {
		return MessageTypeEnum.NEWS;
	}

	/**
	 * 添加图文消息
	 * @param article 图文消息对象
	 * @return 当前对象
	 * @throws RuntimeException 当图文消息数量超过8条时抛出异常
	 */
	public WechatNewsMessage addArticle(Article article) {
		if (articles.size() + 1 > MAX_ARTICLE_LIMIT) {
			throw new RuntimeException("图文消息最多支持8条");
		}
		articles.add(article);
		return this;
	}

	@Override
	public WechatParams put(WechatParams params) {
		return params.setNews(new WechatParams.News().setArticles(articles));
	}

}
