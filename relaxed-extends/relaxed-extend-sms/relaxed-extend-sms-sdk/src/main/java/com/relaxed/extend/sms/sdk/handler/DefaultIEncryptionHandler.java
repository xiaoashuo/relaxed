package com.relaxed.extend.sms.sdk.handler;

import cn.hutool.crypto.digest.MD5;

/**
 * @author Yakir
 * @Topic DefaultIEncryptionHandler
 * @Description 私钥计算方法
 * @date 2021/8/26 15:00
 * @Version 1.0
 */
public class DefaultIEncryptionHandler implements IEncryptionHandler {

	@Override
	public String encode(String timestamp, String accessKeyId, String accessKeySecret) {
		byte[] timestamps = timestamp.getBytes();
		byte[] cakis = new byte[timestamps.length];
		byte[] cakss = new byte[timestamps.length];
		for (int i = 0; i < timestamps.length; i++) {
			int ci = (int) timestamps[i] - (int) ('0');
			byte caki = (byte) accessKeyId.charAt(ci);
			byte caks = (byte) accessKeySecret.charAt(ci);
			cakis[i] = (byte) (caki >>> (i > 6 ? i / 2 : i) & i);
			cakss[i] = (byte) (caks | i);
		}
		byte[] accessKeyIds = accessKeyId.getBytes();
		byte[] accessKeySecrets = accessKeySecret.getBytes();

		byte[] md5Bytes = new byte[cakis.length + cakss.length + accessKeyIds.length + accessKeySecrets.length];

		System.arraycopy(cakis, 0, md5Bytes, 0, cakis.length);
		System.arraycopy(accessKeyIds, 0, md5Bytes, cakis.length, accessKeyIds.length);
		System.arraycopy(cakss, 0, md5Bytes, accessKeyIds.length, cakss.length);
		System.arraycopy(accessKeySecrets, 0, md5Bytes, cakss.length, accessKeySecrets.length);
		MD5 md5 = MD5.create();
		return md5.digestHex(md5.digestHex(md5Bytes).getBytes());
	}

}
