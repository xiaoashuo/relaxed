package com.relaxed.common.risk.engine.manage.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.relaxed.common.risk.engine.config.EngineProperties;
import com.relaxed.common.risk.engine.manage.MobileInfoManageService;
import com.relaxed.common.risk.engine.model.converter.MobileInfoConverter;
import com.relaxed.common.risk.engine.model.vo.MobileInfoVO;
import com.relaxed.common.risk.engine.service.MobileInfoService;
import com.relaxed.common.risk.engine.utils.MobileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Yakir
 * @Topic MobileInfoServiceImpl
 * @Description
 * @date 2021/9/1 13:47
 * @Version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MobileInfoManageServiceImpl implements MobileInfoManageService {

	private final MobileInfoService mobileInfoService;

	private static Map<String, MobileInfoVO> mobileMap = new HashMap<>();

	@PostConstruct
	public void initAllMobileInfo() {
		log.info("mobile info loading...");
		Long begin = System.currentTimeMillis();
		int count = 0;
		try {
			List<String> lines = FileUtil.readLines(EngineProperties.getMobilePath(), Charset.forName("UTF-8"));

			List<MobileInfoVO> mobileInfoVOList = lines.stream().map(line -> {
				String[] info = line.split(",");
				MobileInfoVO mobile = new MobileInfoVO();
				if (info.length == 5) {
					mobile.setMobile(info[0]);
					mobile.setProvince(info[1]);
					mobile.setCity(info[2]);
					mobile.setSupplier(info[3]);
					mobile.setRegionCode(info[4]);
				}
				return mobile;
			}).collect(Collectors.toList());
			for (MobileInfoVO mobileInfoVO : mobileInfoVOList) {
				mobileMap.put(mobileInfoVO.getMobile(), mobileInfoVO);
			}
			count = mobileInfoVOList.size();
		}
		catch (Exception e) {
			throw e;
		}
		finally {
			long cost = System.currentTimeMillis() - begin;
			log.info("{}, mobile info cached, costs:{}", count, cost);
		}

	}

	@Override
	public MobileInfoVO getByMobile(String mobile) {
		MobileInfoVO vo = mobileMap.get(mobile);
		if (vo != null) {
			return vo;
		}
		String result = MobileUtils.getLocation(mobile);
		JSONObject json = JSONUtil.parseObj(result);
		String retMsg = json.getStr("retMsg");
		String province = json.getStr("province");
		String city = json.getStr("city");
		if (retMsg.equals("success")) {
			MobileInfoVO mobileInfoVO = mobileInfoService.selectOneLimit1(province, city);
			if (mobileInfoVO != null) {
				MobileInfoVO info = new MobileInfoVO();
				info.setMobile(mobile.substring(0, 7));
				info.setProvince(mobileInfoVO.getProvince());
				info.setCity(mobileInfoVO.getCity());
				info.setSupplier(mobileInfoVO.getSupplier());
				info.setRegionCode(mobileInfoVO.getRegionCode());
				mobileInfoService.save(MobileInfoConverter.INSTANCE.voToPo(info));
				mobileMap.put(info.getMobile(), info);
				return mobileInfoVO;
			}
		}
		return null;
	}

}
