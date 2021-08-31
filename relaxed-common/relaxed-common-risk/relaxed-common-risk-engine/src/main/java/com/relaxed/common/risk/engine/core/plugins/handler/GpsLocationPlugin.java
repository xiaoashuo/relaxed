package com.relaxed.common.risk.engine.core.plugins.handler;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.relaxed.common.risk.engine.core.plugins.PluginService;
import com.relaxed.common.risk.engine.model.entity.Location;
import com.relaxed.common.risk.engine.model.vo.PreItemVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;

import java.util.Map;

/**
 * @author Yakir
 * @Topic GpsLocationPlugin
 * @Description
 * @date 2021/8/29 14:29
 * @Version 1.0
 */
@Slf4j
public class GpsLocationPlugin implements PluginService {

	@Override
	public Integer getOrder() {
		return 2;
	}

	@Override
	public String desc() {
		return "GPS转换成地址";
	}

	@Override
	public Class<?> getType() {
		return Location.class;
	}

	@Override
	public Object handle(PreItemVO preItemVO, Map<String, Object> jsonInfo, String[] sourceFields) {
		// 参考 http://jwd.funnyapi.com/#/index , 最好是本地库。
		Location location = new Location();
		// 纬度
		String latitude = String.valueOf(jsonInfo.get(sourceFields[0]));
		// 经度
		String longitude = String.valueOf(jsonInfo.get(sourceFields[1]));
		if ("".equals(latitude) || "".equals(longitude)) {
			return null;
		}
		try {
			String url = "http://106.75.35.67:60000/gis?auth_user=freevip&latitude=" + latitude + "&longitude="
					+ longitude;
			String body = HttpUtil.get(url);
			JSONObject parseObject = JSONUtil.parseObj(body);
			// String转map
			String data = parseObject.getStr("data");
			Map stringToMap = JSONUtil.parseObj(data);

			location.setCountry(String.valueOf(stringToMap.get("zh0")));
			location.setRegion(String.valueOf(stringToMap.get("zh3")));
			location.setProvince(String.valueOf(stringToMap.get("zh1")));
			location.setCity(String.valueOf(stringToMap.get("zh2")));

		}
		catch (Exception e) {
			log.error("GPS2LOCATION error", e);
		}
		return location;
	}

}
