package com.yiyi.auth.shiro;

import com.yiyi.auth.model.User;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

/**
 * 密码工具类
 * @date 2016年1月24日 下午1:47:35
 */
public class PasswordHelper {

	private static RandomNumberGenerator randomNumberGenerator;

	public static RandomNumberGenerator getRandomNumberGenerator(){
		if(randomNumberGenerator == null){
			randomNumberGenerator = new SecureRandomNumberGenerator();
		}
		return randomNumberGenerator;
	}

	//MD5加密
	private static String algorithmName = "md5";

	private static int hashIterations = 2;

	public void setRandomNumberGenerator(RandomNumberGenerator randomNumberGenerator) {
		this.randomNumberGenerator = randomNumberGenerator;
	}

	/**
	 * 对用户的密码进行加密处理
	 * @param user
	 */
	public static void encryptPassword(User user) {

		user.setSalt(getRandomNumberGenerator().nextBytes().toHex());

		String newPassword = new SimpleHash(algorithmName, user.getPassword(),
				ByteSource.Util.bytes(user.getCredentialsSalt()),
				hashIterations).toHex();

		user.setPassword(newPassword);
	}
}
