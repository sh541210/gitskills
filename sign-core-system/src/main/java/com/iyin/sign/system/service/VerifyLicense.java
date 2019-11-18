package com.iyin.sign.system.service;

import com.iyin.sign.system.common.enums.LicenceEnum;
import com.iyin.sign.system.common.exception.ErrorCode;
import com.iyin.sign.system.common.utils.LicenseManagerHolder;
import com.iyin.sign.system.common.utils.MacTools;
import com.iyin.sign.system.common.utils.UUIDUtil;
import com.iyin.sign.system.mapper.FileResourceMapper;
import com.iyin.sign.system.mapper.ISignSysCompactInfoMapper;
import com.iyin.sign.system.mapper.ISysSignatureLogMapper;
import com.iyin.sign.system.mapper.SignSysAdminUserMapper;
import com.iyin.sign.system.model.IyinResult;
import com.iyin.sign.system.model.code.FileResponseCode;
import com.iyin.sign.system.model.exception.BusinessException;
import com.iyin.sign.system.util.*;
import de.schlichtherle.license.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.prefs.Preferences;

/**
	* @Description VerifyLicense
	* @Author: wdf
    * @CreateDate: 2019/2/22 10:37
	* @UpdateUser: wdf
    * @UpdateDate: 2019/2/22 10:37
	* @Version: 0.0.1
    * @param
    * @return
    */
@Service
@Slf4j
public class VerifyLicense {

	private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
	/**
	 * 常用字符串
	 */
	private static final String VERSION_INFO="versionInfo";
	private static final String IS_EFFECTIVE="isEffective";
	private static final String SERIALNUMBER="serialNumber";
	private static final String MACS="macs";

	private static final String VSTR="永久";
	private static final String DATETIME="dateTime";
	private static final String SYS_TYPE="sysType";
	private static final String REG="正式版";
	private static final String TEST="试用版";

	/**
	 * lic证书需要的数据
	 */
	private static String privateAlias = "publiccert";
	private static String storeProduct = "anyin123";
	private static String subject = "license";
	private static String pubPath = "/licence/publicCerts.store";
	public static final String LIC_NAME = "license.lic";
	public static final String LIC_TYPE_TXT = "licence.txt";
	@Resource
	private ISysSignatureLogMapper sysSignatureLogMapper;
	@Resource
	FileResourceMapper fileResourceMapper;
	@Resource
	private SignSysAdminUserMapper signSysAdminUserMapper;

	@Value("${localFilePath.auth}")
	public String authUrl;
	@Value("${localFilePath.userMac}")
	public String userMac;
	@Value("${localFilePath.userName}")
	public String userName;
	@Value("${localFilePath.userEn}")
	public String userEn;
	@Value("${localFilePath.signKey}")
	private String signKey;

	/**
	 * @Description 注册证书并校验证书
	 * @Author: wdf
	 * @CreateDate: 2019/2/23 18:49
	 * @UpdateUser: wdf
	 * @UpdateDate: 2019/2/23 18:49
	 * @Version: 0.0.1
	 * @param file
	 * @return Map<String, Object>
	 */
	public Map<String, Object> verifyAuthLicence(MultipartFile file)  {
		Map<String, Object> result = new HashMap<>(DEFAULT_INITIAL_CAPACITY);
		//临时目录
        String fileTempPath =authUrl+"temp/"+LIC_NAME;
		//安装目录
		String filePath =authUrl+LIC_NAME;
		//templic文件保存到本地
		File licTempFile=new File(fileTempPath);
        FileUtil.deleteFile(licTempFile);
        try(OutputStream os= new FileOutputStream(fileTempPath);
            InputStream inputStream = file.getInputStream()){
            FileUtils.getFilePath(inputStream,os);
        }catch (Exception e){
            log.error("verifyAuthLicence e:"+e.getMessage());
            throw new BusinessException(ErrorCode.SERVER_50300);
        }
		Map<String, Object> ckMap=ckLicence(fileTempPath);

        //检验成功替换文件
        File licFile=new File(filePath);
        FileUtil.deleteFile(licFile);
        try(OutputStream os= new FileOutputStream(filePath);
            InputStream inputStream = file.getInputStream()){
            FileUtils.getFilePath(inputStream,os);
        }catch (Exception e){
            log.error("verifyAuthLicence e:"+e.getMessage());
            throw new BusinessException(ErrorCode.SERVER_50300);
        }

		result.put(SERIALNUMBER,ckMap.get(SERIALNUMBER));
		result.put(VERSION_INFO,ckMap.get(VERSION_INFO));
		return result;
	}

	/**
		* @Description 验证签章次数
		* @Author: wdf
	    * @CreateDate: 2019/2/26 16:03
		* @UpdateUser: wdf
	    * @UpdateDate: 2019/2/26 16:03
		* @Version: 0.0.1
	    * @param extra, count
	    * @return void
	    */
	private void verifySignCount(Map<String, Object> extra, int count) {
		int sCount= (int) extra.get("signCount");
		if(count>=sCount){
			throw new BusinessException(ErrorCode.SERVER_50305);
		}
	}

	/**
		* @Description 验证有效时间
		* @Author: wdf
	    * @CreateDate: 2019/2/26 16:03
		* @UpdateUser: wdf
	    * @UpdateDate: 2019/2/26 16:03
		* @Version: 0.0.1
	    * @param lc(获取过期时间), date(系统时间), server50305
	    * @return void
	    */
	private void verifyDate(LicenseContent lc, Date date, ErrorCode server) {
		if (lc.getNotAfter().getTime() < date.getTime()) {
			throw new BusinessException(server);
		}else{
			//获取签章日志时间样本  为空转到下一级，不为空获取最大的时间 判断时间是否正常
			Object signDate=sysSignatureLogMapper.getAddDate();
			//获取文件记录时间样本  为空转到下一级，不为空获取最大的时间 判断时间是否正常
			Object relaDate=fileResourceMapper.getAddDate();
			//获取用户最后登录时间样本  为空转到下一级，不为空获取最大的时间 判断时间是否正常
			Object userDate=signSysAdminUserMapper.getAddDate();
			if(null==signDate&&null==relaDate&&null==userDate){
				throw new BusinessException(server);
			}else{
				log.info("date:{}",DateUtil.formatDate(date, DateFormatType.DATETIMESTR));
				ckDate(date, server, signDate, relaDate, userDate);
			}
		}

	}

	private void ckDate(Date date, ErrorCode server, Object signDate, Object relaDate, Object userDate) {
		long sign=0L;
		long rela=0L;
		long user=0L;
		log.info("ckDate signDate:{} , relaDate:{} , userDate:{}",signDate,relaDate,userDate);
		if(null!=signDate){
			sign=((Date)signDate).getTime();
		}
		if(null!=relaDate){
			rela=((Date)relaDate).getTime();
		}
		if(null!=userDate){
			user=((Date)userDate).getTime();
		}
		log.info("ckDate signDate:{} , relaDate:{} , userDate:{}",signDate,relaDate,userDate);
		Long max=getMaxDate(sign,rela,user);
		log.info("max:{}",max);
		if(max>date.getTime()){
			throw new BusinessException(server);
		}
	}

	/**
		* @Description 获取最大的时间
		* @Author: wdf
	    * @CreateDate: 2019/2/28 17:56
		* @UpdateUser: wdf
	    * @UpdateDate: 2019/2/28 17:56
		* @Version: 0.0.1
	    * @param sign, rela, user
	    * @return java.lang.Long
	    */
	private Long getMaxDate(Long sign,Long rela,Long user){
		Long temp;
		temp=sign>rela?sign:rela;
		temp=temp>user?temp:user;
		return temp;
	}

	/**
	 * @Description 获取机器码
	 * @Author: wdf
	 * @CreateDate: 2019/2/25 19:03
	 * @UpdateUser: wdf
	 * @UpdateDate: 2019/2/25 19:03
	 * @Version: 0.0.1
	 * @param
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 */
	public Map<String, Object> getMachineCode() {
		String uuid= UUIDUtil.getFormatUUID();
		String filePath =authUrl+LIC_TYPE_TXT;
		File file=new File(filePath);
		if(file.exists()){
			try(FileInputStream fis=new FileInputStream(file)){
				byte[] buf = new byte[1024];
				StringBuilder sb=new StringBuilder();
				while((fis.read(buf))!=-1){
					sb.append(new String(buf));
					//重新生成，避免和上次读取的数据重复
					buf=new byte[1024];
				}
				uuid=sb.toString().trim();
			} catch (IOException e) {
				log.error("生成文件异常" + e.getMessage());
				throw new BusinessException(FileResponseCode.DATA_IO_EXCEPTION);
			}
		}else {
			//lic文件保存到本地
			try (OutputStream os = new FileOutputStream(filePath)) {
				PrintStream ps = new PrintStream(os);
				ps.println(uuid);
			} catch (IOException e) {
				log.error("生成文件异常" + e.getMessage());
				throw new BusinessException(FileResponseCode.DATA_IO_EXCEPTION);
			}
		}
		Map<String, Object> map=new HashMap<>(DEFAULT_INITIAL_CAPACITY);
		map.put("machineCode", uuid);
		return map;
	}

	/**
	 * @Description 校验lic文件
	 * @Author: wdf
	 * @CreateDate: 2019/2/25 12:00
	 * @UpdateUser: wdf
	 * @UpdateDate: 2019/2/25 12:00
	 * @Version: 0.0.1
	 * @param filePath
	 * @return de.schlichtherle.license.LicenseContent
	 */
	public Map<String, Object> ckLicence(String filePath) {
		LicenseManager licenseManager = LicenseManagerHolder
				.getLicenseManager(initLicenseParams());
		LicenseContent lc=null;
		Map<String,Object> map=new HashMap(DEFAULT_INITIAL_CAPACITY);
		// 安装证书
		try {
			licenseManager.install(new File(filePath));
			//LicenseParam licenseParam
			licenseManager.getLicenseParam();
			log.info("客户端安装证书成功!success");
		} catch (Exception e) {
			log.info("客户端证书安装失败!"+e.getMessage());
			throw new BusinessException(ErrorCode.SERVER_50301);
		}
		// 验证证书
		try {
			//api校验
			lc=licenseManager.verify();
			map= (Map<String, Object>) lc.getExtra();

			log.info("客户端验证证书成功!success");
		} catch (Exception e) {
			log.info("客户端证书验证失效!"+e.getMessage());
			//如果为永久有效，不抛出异常
			if(null!=map){
				Integer status=Integer.parseInt(map.get(IS_EFFECTIVE)+"");
				switch (status) {
					case 0:
						map.put(SERIALNUMBER,"**** **** **** **** ****");
						throw new BusinessException(ErrorCode.SERVER_50306);
					case 2:
					case 3:
						throw new BusinessException(ErrorCode.SERVER_50306);
					case 4:
						exceptionVerifySignCount(map);
						break;
					default:
						if (status != LicenceEnum.EFFECTIVEONE.getCode()) {
							throw new BusinessException(ErrorCode.SERVER_50302);
						}
				}
			}else{
				throw new BusinessException(ErrorCode.SERVER_50300);
			}
		}
		log.info("ckLicence lc");
		if(null!=lc){
			map=ckVerifyResult(lc);
			if(map.isEmpty()){
				throw new BusinessException(ErrorCode.SERVER_50300);
			}
			Integer status=Integer.parseInt(map.get(IS_EFFECTIVE)+"");
			if(status==LicenceEnum.EFFECTIVEONE.getCode()){
				map.put(DATETIME,VSTR);
			}else{
				map.put(DATETIME, DateUtil.formatDate(lc.getNotAfter(), DateFormatType.DATETIMESTR));
			}
			if(status==LicenceEnum.EFFECTIVEZERO.getCode()){
				map.put(SYS_TYPE,TEST);
			}else{
				map.put(SYS_TYPE,REG);
			}
		}
		map.put("userMac",userMac);
		map.put("userName",userName);
		map.put("userEn",userEn);
		log.info("ckLicence map:{}",map);
		return map;
	}

	/**
		* @Description 校验文件验证后的数据
		* @Author: wdf
	    * @CreateDate: 2019/2/28 15:39
		* @UpdateUser: wdf
	    * @UpdateDate: 2019/2/28 15:39
		* @Version: 0.0.1
	    * @param lc
	    * @return java.util.Map<java.lang.String,java.lang.Object>
	    */
	private Map<String, Object> ckVerifyResult(LicenseContent lc) {
		log.info("ckVerifyResult lc");
		Map<String, Object> extra = (Map<String, Object>) lc.getExtra();
		log.info("ckVerifyResult start extra:"+extra.toString());
		//业务逻辑校验
		Integer status=Integer.parseInt(extra.get(IS_EFFECTIVE)+"");
		Date date= new Date();
		log.info("ckVerifyResult getSignCount status:"+status);
		int count=sysSignatureLogMapper.getSignCount();
		log.info("ckVerifyResult count："+count);
		ckMac(extra);
		switch (status) {
			case 0:
				extra.put(SERIALNUMBER,"**** **** **** **** ****");
				//判断是否过期 获取证书有效期 获取当前日期
				verifyDate(lc, date, ErrorCode.SERVER_50304);
				break;
			case 1:
				//永久有效
				break;
			case 2:
				//时间和次数
				verifyDate(lc, date, ErrorCode.SERVER_50305);
				verifySignCount(extra, count);
				break;
			case 3:
				//有效时间
				verifyDate(lc, date, ErrorCode.SERVER_50305);
				break;
			case 4:
				//签章次数 获取次数
				verifySignCount(extra, count);
				break;
			default:
				if (status != LicenceEnum.EFFECTIVEONE.getCode()&&status != LicenceEnum.EFFECTIVEFOUR.getCode()) {
					throw new BusinessException(ErrorCode.SERVER_50302);
				}
		}
		log.info("ckVerifyResult end extra:"+extra.toString());
		return extra;
	}

	/**
		* @Description 校验mac
		* @Author: wdf 
	    * @CreateDate: 2019/9/24 11:17
		* @UpdateUser: wdf
	    * @UpdateDate: 2019/9/24 11:17
		* @Version: 0.0.1
	    * @param extra
	    * @return void
	    */
	private void ckMac(Map<String, Object> extra){
		List<String> macList= MacTools.getMacList();
		if(null==extra||macList.isEmpty()){
			throw new BusinessException(ErrorCode.SERVER_50300);
		}
		String macs=extra.get(MACS)+"";
		String[] macArray=macs.split(",");
		int flag=1;
		for (int i=0;i<macArray.length;i++){
			String mac=macArray[i].toUpperCase();
			String licKey= MD5Util.getStringMD5(MD5Util.getStringMD5(mac)+signKey).toUpperCase();
			flag = getFlag(macList, licKey);
			if(flag==0){
				break;
			}
		}
		if(1==flag){
			throw new BusinessException(ErrorCode.SERVER_50300);
		}

	}

	/**
	 * @Description mac校验
	 * @Author: wdf
	 * @CreateDate: 2019/8/20 17:32
	 * @UpdateUser: wdf
	 * @UpdateDate: 2019/8/20 17:32
	 * @Version: 0.0.1
	 * @param macList, licKey
	 * @return int
	 */
	private int getFlag(List<String> macList, String licKey) {
		int flag=1;
		log.debug("VerifyLicense getFlag start");
		for (int i=0;i<macList.size();i++){
			String userMac=macList.get(i);
			String str="AnYinKeJi";
			String macMd=MD5Util.getStringMD5(MD5Util.getStringMD5(userMac)+str).toUpperCase();
			String userKey= MD5Util.getStringMD5(MD5Util.getStringMD5(macMd)+signKey).toUpperCase();
			if(userKey.equals(licKey)){
				flag=0;
				break;
			}
		}
		return flag;
	}


	/**
		* @Description lic文件异常情况下验证签章次数
		* @Author: wdf
	    * @CreateDate: 2019/2/26 16:26
		* @UpdateUser: wdf
	    * @UpdateDate: 2019/2/26 16:26
		* @Version: 0.0.1
	    * @param map
	    * @return void
	    */
	private void exceptionVerifySignCount(Map<String, Object> map) {
		int count=sysSignatureLogMapper.getSignCount();
		int sCount= (int) map.get("signCount");
		if(count>=sCount){
			throw new BusinessException(ErrorCode.SERVER_50305);
		}
	}

	/**
	 * @Description 组装验证证书需要的参数
	 * @Author: wdf
	 * @CreateDate: 2019/2/23 18:48
	 * @UpdateUser: wdf
	 * @UpdateDate: 2019/2/23 18:48
	 * @Version: 0.0.1
	 * @return de.schlichtherle.license.LicenseParam
	 */
	private static LicenseParam initLicenseParams() {
		Preferences preference = Preferences
				.userNodeForPackage(VerifyLicense.class);
		CipherParam cipherParam = new DefaultCipherParam(storeProduct);
		KeyStoreParam privateStoreParam = new DefaultKeyStoreParam(
				VerifyLicense.class, pubPath, privateAlias, storeProduct, null);
		return new DefaultLicenseParam(subject,
				preference, privateStoreParam, cipherParam);
	}

	public static void main(String[] args) {
//		VerifyLicense verifyLicense=new VerifyLicense();
//		verifyLicense.ckLicence("D:\\license.lic");
	}
}