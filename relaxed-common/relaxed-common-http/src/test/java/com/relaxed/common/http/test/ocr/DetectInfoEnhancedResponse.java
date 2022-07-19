package com.relaxed.common.http.test.ocr;

import com.relaxed.common.http.core.response.BaseResponse;
import lombok.Data;

/**
 * @author Yakir
 * @Topic GetDetectInfoEnhancedRequest
 * @Description
 * @date 2022/6/23 16:59
 * @Version 1.0
 */
@Data
public class DetectInfoEnhancedResponse extends BaseResponse {

	/**
	 * 文本类信息。 注意：此字段可能返回 null，表示取不到有效值。
	 */
	private DetectInfoText Text;

	/**
	 * 身份证照片信息。 注意：此字段可能返回 null，表示取不到有效值。
	 */
	private DetectInfoIdCardData IdCardData;

	/**
	 * 最佳帧信息。 注意：此字段可能返回 null，表示取不到有效值。
	 */
	private DetectInfoBestFrame BestFrame;

	/**
	 * 视频信息。 注意：此字段可能返回 null，表示取不到有效值。
	 */
	private DetectInfoVideoData VideoData;

	/**
	 * 敏感数据加密信息。 注意：此字段可能返回 null，表示取不到有效值。
	 */
	private Encryption Encryption;

	/**
	 * 意愿核身相关信息。若未使用意愿核身功能，该字段返回值可以不处理。 注意：此字段可能返回 null，表示取不到有效值。
	 */
	private IntentionVerifyData IntentionVerifyData;

	/**
	 * 唯一请求 ID，每次请求都会返回。定位问题时需要提供该次请求的 RequestId。
	 */
	private String RequestId;

	@Data
	public class Encryption {

		/**
		 * 有加密需求的用户，接入传入kms的CiphertextBlob，关于数据加密可查阅<a href=
		 * "https://cloud.tencent.com/document/product/1007/47180">数据加密</a> 文档。
		 */
		private String CiphertextBlob;

		/**
		 * 在使用加密服务时，填入要被加密的字段。本接口中可填入加密后的一个或多个字段
		 */
		private String[] EncryptList;

		/**
		 * 有加密需求的用户，传入CBC加密的初始向量
		 */
		private String Iv;

	}

	@Data
	public class DetectInfoText {

		/**
		 * 本次流程最终验证结果。0为成功 注意：此字段可能返回 null，表示取不到有效值。
		 */
		private Long ErrCode;

		/**
		 * 本次流程最终验证结果描述。（仅描述用，文案更新时不会通知。） 注意：此字段可能返回 null，表示取不到有效值。
		 */
		private String ErrMsg;

		/**
		 * 本次验证使用的身份证号。 注意：此字段可能返回 null，表示取不到有效值。
		 */
		private String IdCard;

		/**
		 * 本次验证使用的姓名。 注意：此字段可能返回 null，表示取不到有效值。
		 */
		private String Name;

		/**
		 * Ocr识别结果。民族。 注意：此字段可能返回 null，表示取不到有效值。
		 */
		private String OcrNation;

		/**
		 * Ocr识别结果。家庭住址。 注意：此字段可能返回 null，表示取不到有效值。
		 */
		private String OcrAddress;

		/**
		 * Ocr识别结果。生日。 注意：此字段可能返回 null，表示取不到有效值。
		 */
		private String OcrBirth;

		/**
		 * Ocr识别结果。签发机关。 注意：此字段可能返回 null，表示取不到有效值。
		 */
		private String OcrAuthority;

		/**
		 * Ocr识别结果。有效日期。 注意：此字段可能返回 null，表示取不到有效值。
		 */
		private String OcrValidDate;

		/**
		 * Ocr识别结果。姓名。 注意：此字段可能返回 null，表示取不到有效值。
		 */
		private String OcrName;

		/**
		 * Ocr识别结果。身份证号。 注意：此字段可能返回 null，表示取不到有效值。
		 */
		private String OcrIdCard;

		/**
		 * Ocr识别结果。性别。 注意：此字段可能返回 null，表示取不到有效值。
		 */
		private String OcrGender;

		/**
		 * 本次流程最终活体结果。0为成功 注意：此字段可能返回 null，表示取不到有效值。
		 */
		private Long LiveStatus;

		/**
		 * 本次流程最终活体结果描述。（仅描述用，文案更新时不会通知。） 注意：此字段可能返回 null，表示取不到有效值。
		 */
		private String LiveMsg;

		/**
		 * 本次流程最终一比一结果。0为成功 注意：此字段可能返回 null，表示取不到有效值。
		 */
		private Long Comparestatus;

		/**
		 * 本次流程最终一比一结果描述。（仅描述用，文案更新时不会通知。） 注意：此字段可能返回 null，表示取不到有效值。
		 */
		private String Comparemsg;

		/**
		 * 本次流程活体一比一的分数，取值范围 [0.00,
		 * 100.00]。相似度大于等于70时才判断为同一人，也可根据具体场景自行调整阈值（阈值70的误通过率为千分之一，阈值80的误通过率是万分之一）
		 * 注意：此字段可能返回 null，表示取不到有效值。
		 */
		private String Sim;

		/**
		 * 地理位置经纬度。 注意：此字段可能返回 null，表示取不到有效值。
		 */
		private String Location;

		/**
		 * Auth接口带入额外信息。 注意：此字段可能返回 null，表示取不到有效值。
		 */
		private String Extra;

		/**
		 * 本次流程进行的活体一比一流水。 注意：此字段可能返回 null，表示取不到有效值。
		 */
		private DetectDetail[] LivenessDetail;

		/**
		 * 手机号码。 注意：此字段可能返回 null，表示取不到有效值。
		 */
		private String Mobile;

		/**
		 * 本次流程最终比对库源类型。包括： 权威库； 业务方自有库（用户上传照片、客户的混合库、混合部署库）； 二次验证库； 人工审核库； 注意：此字段可能返回
		 * null，表示取不到有效值。
		 */
		private String CompareLibType;

	}

	@Data
	public class DetectDetail {

		/**
		 * 请求时间戳。 注意：此字段可能返回 null，表示取不到有效值。
		 */
		private String ReqTime;

		/**
		 * 本次活体一比一请求的唯一标记。 注意：此字段可能返回 null，表示取不到有效值。
		 */
		private String Seq;

		/**
		 * 参与本次活体一比一的身份证号。 注意：此字段可能返回 null，表示取不到有效值。
		 */
		private String Idcard;

		/**
		 * 参与本次活体一比一的姓名。 注意：此字段可能返回 null，表示取不到有效值。
		 */
		private String Name;

		/**
		 * 本次活体一比一的相似度。 注意：此字段可能返回 null，表示取不到有效值。
		 */
		private String Sim;

		/**
		 * 本次活体一比一是否收费 注意：此字段可能返回 null，表示取不到有效值。
		 */
		private Boolean IsNeedCharge;

		/**
		 * 本次活体一比一最终结果。0为成功 注意：此字段可能返回 null，表示取不到有效值。
		 */
		private Long Errcode;

		/**
		 * 本次活体一比一最终结果描述。（仅描述用，文案更新时不会通知。） 注意：此字段可能返回 null，表示取不到有效值。
		 */
		private String Errmsg;

		/**
		 * 本次活体结果。0为成功 注意：此字段可能返回 null，表示取不到有效值。
		 */
		private Long Livestatus;

		/**
		 * 本次活体结果描述。（仅描述用，文案更新时不会通知。） 注意：此字段可能返回 null，表示取不到有效值。
		 */
		private String Livemsg;

		/**
		 * 本次一比一结果。0为成功 注意：此字段可能返回 null，表示取不到有效值。
		 */
		private Long Comparestatus;

		/**
		 * 本次一比一结果描述。（仅描述用，文案更新时不会通知。） 注意：此字段可能返回 null，表示取不到有效值。
		 */
		private String Comparemsg;

		/**
		 * 比对库源类型。包括： 公安商业库； 业务方自有库（用户上传照片、客户的混合库、混合部署库）； 二次验证库； 人工审核库； 注意：此字段可能返回
		 * null，表示取不到有效值。
		 */
		private String CompareLibType;

	}

	@Data
	public class DetectInfoIdCardData {

		/**
		 * OCR正面照片的base64编码。 注意：此字段可能返回 null，表示取不到有效值。
		 */
		private String OcrFront;

		/**
		 * OCR反面照片的base64编码 注意：此字段可能返回 null，表示取不到有效值。
		 */
		private String OcrBack;

		/**
		 * 旋转裁边后的正面照片base64编码。 注意：此字段可能返回 null，表示取不到有效值。
		 */
		private String ProcessedFrontImage;

		/**
		 * 旋转裁边后的背面照片base64编码。 注意：此字段可能返回 null，表示取不到有效值。
		 */
		private String ProcessedBackImage;

		/**
		 * 身份证正面人像图base64编码。 注意：此字段可能返回 null，表示取不到有效值。
		 */
		private String Avatar;

		/**
		 * 开启身份证防翻拍告警功能后才会返回，返回数组中可能出现的告警码如下： -9102 身份证复印件告警。 -9103 身份证翻拍告警。 -9106 身份证 PS
		 * 告警。 注意：此字段可能返回 null，表示取不到有效值。
		 */
		private Long[] WarnInfos;

	}

	@Data
	public class DetectInfoBestFrame {

		/**
		 * 活体比对最佳帧Base64编码。 注意：此字段可能返回 null，表示取不到有效值。
		 */
		private String BestFrame;

		/**
		 * 自截帧Base64编码数组。 注意：此字段可能返回 null，表示取不到有效值。
		 */
		private String[] BestFrames;

	}

	@Data
	public class DetectInfoVideoData {

		/**
		 * 活体视频的base64编码 注意：此字段可能返回 null，表示取不到有效值。
		 */
		private String LivenessVideo;

	}

	@Data
	public class IntentionVerifyData {

		/**
		 * 意愿确认环节中录制的视频（base64）。若不存在则为空字符串。 注意：此字段可能返回 null，表示取不到有效值。
		 */
		private String IntentionVerifyVideo;

		/**
		 * 意愿确认环节中用户语音转文字的识别结果。若不存在则为空字符串。 注意：此字段可能返回 null，表示取不到有效值。
		 */
		private String AsrResult;

		/**
		 * 意愿确认环节的结果码。当该结果码为0时，语音朗读的视频与语音识别结果才会返回。 注意：此字段可能返回 null，表示取不到有效值。
		 */
		private Long ErrorCode;

		/**
		 * 意愿确认环节的结果信息。 注意：此字段可能返回 null，表示取不到有效值。
		 */
		private String ErrorMessage;

		/**
		 * 意愿确认环节中录制视频的最佳帧（base64）。若不存在则为空字符串。 注意：此字段可能返回 null，表示取不到有效值。
		 */
		private String IntentionVerifyBestFrame;

		/**
		 * 本次流程用户语音与传入文本比对的相似度分值，取值范围 [0.00, 100.00]。只有配置了相似度阈值后才进行语音校验并返回相似度分值。
		 * 注意：此字段可能返回 null，表示取不到有效值。
		 */
		private String AsrResultSimilarity;

	}

}
