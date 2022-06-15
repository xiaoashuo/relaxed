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
 * @author Yakir
 * @Topic WechatNewsMessage
 * @Description
 * @date 2022/6/15 14:06
 * @Version 1.0
 */
@Getter
@Accessors(chain = true)
public class WechatNewsMessage extends AbstractWechatMessage {

	private static final Integer MAX_ARTICLE_LIMIT = 8;

	/**
	 * 图文消息，一个图文消息支持1到8条图文 required true
	 */
	private List<Article> articles = new ArrayList<>();

	@Override
	public MessageTypeEnum getType() {
		return MessageTypeEnum.NEWS;
	}

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
