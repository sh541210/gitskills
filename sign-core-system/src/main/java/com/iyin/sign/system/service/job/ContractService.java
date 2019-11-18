package com.iyin.sign.system.service.job;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.iyin.sign.system.common.enums.FileManageEnum;
import com.iyin.sign.system.common.utils.Slf4jUtil;
import com.iyin.sign.system.entity.SignSysCompactInfo;
import com.iyin.sign.system.entity.SignSysCompactSignatory;
import com.iyin.sign.system.entity.SignSysUserInfo;
import com.iyin.sign.system.mapper.ISignSysCompactInfoMapper;
import com.iyin.sign.system.mapper.ISignSysCompactSignatoryMapper;
import com.iyin.sign.system.mapper.SignSysUserInfoMapper;
import com.iyin.sign.system.service.ShortLinkService;
import com.iyin.sign.system.service.impl.CompactInfoServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @ClassName: ContractService
 * @Description: 合同相关定时任务
 * @Author: yml
 * @CreateDate: 2019/8/23
 * @UpdateUser: yml
 * @UpdateDate: 2019/8/23
 * @Version: 1.0.0
 */
@Component
@Slf4j
public class ContractService {

    @Resource
    private ISignSysCompactInfoMapper signSysCompactInfoMapper;
    @Resource
    private ISignSysCompactSignatoryMapper signSysCompactSignatoryMapper;
    @Resource
    private SignSysUserInfoMapper signSysUserInfoMapper;
    @Autowired
    private CompactInfoServiceImpl compactInfoService;
    @Autowired
    private ShortLinkService shortLinkService;

    @Value("${contract.coming_to_sign_time}")
    private String comingToSignTime;
    @Value("${contract.coming_to_sign_deadline}")
    private String comingToSignDeadline;
    @Value("${contract.coming_to_validity_period}")
    private String comingToValidityPeriod;
    @Value("${contract.h5_link}")
    private String h5Link;


    /**
     * 阈值
     */
    private int threshold = 1;

    /**
     * 即将签约截止自动催签
     *
     * @return : void
     * @Author: yml
     * @CreateDate: 2019/8/23
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/23
     * @Version: 0.0.1
     */
    @Scheduled(cron = "0 0 9 * * ?")
    @Async
    public void comingToSign() {
        log.info("com.iyin.sign.system.service.job.ContractService.comingToSign start");
        List<SignSysCompactInfo> signSysCompactInfos = signSysCompactInfoMapper.comingToSign(threshold);
        for (SignSysCompactInfo signSysCompactInfo : signSysCompactInfos) {
            List<SignSysCompactSignatory> signSysCompactSignatories = signSysCompactSignatoryMapper.selectList(
                    Wrappers.<SignSysCompactSignatory>lambdaQuery()
                            .eq(SignSysCompactSignatory::getCompactId, signSysCompactInfo.getId())
                            .eq(SignSysCompactSignatory::getSignStatus, FileManageEnum.TODO.getCode()));
            for (SignSysCompactSignatory signSysCompactSignatory : signSysCompactSignatories) {
                SignSysUserInfo signSysUserInfoSign = signSysUserInfoMapper
                        .selectById(signSysCompactSignatory.getSignatoryId());
                String userName = signSysCompactSignatory.getSignContact();
                if (null != signSysUserInfoSign) {
                    userName = signSysUserInfoSign.getUserName();
                }
                SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
                String h5LinkStr = Slf4jUtil.format(h5Link,signSysCompactInfo.getId(),signSysCompactInfo.getCompactTheme());
                String shortUrl = shortLinkService.getShortUrl(h5LinkStr);
                String content = Slf4jUtil.format(comingToSignTime, signSysCompactInfo.getCompactTheme(),
                        format.format(signSysCompactInfo.getSignDeadline()), shortUrl);
                compactInfoService.doSendNotice(userName, content);
            }
        }
    }

    /**
     * 即将签约截止自动通知发起人
     *
     * @return : void
     * @Author: yml
     * @CreateDate: 2019/8/23
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/23
     * @Version: 0.0.1
     */
    @Scheduled(cron = "0 0 9 * * ?")
    @Async
    public void comingToSignDeadline() {
        log.info("com.iyin.sign.system.service.job.ContractService.comingToSignDeadline start");
        List<SignSysCompactInfo> signSysCompactInfos = signSysCompactInfoMapper.comingToSign(threshold);
        for (SignSysCompactInfo signSysCompactInfo : signSysCompactInfos) {
            SignSysUserInfo signSysUserInfo = signSysUserInfoMapper.selectById(signSysCompactInfo.getUserId());
            String userName = signSysUserInfo.getUserName();
            SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
            String content = Slf4jUtil.format(comingToSignDeadline, signSysCompactInfo.getCompactTheme(),
                    format.format(signSysCompactInfo.getSignDeadline()));
            compactInfoService.doSendNotice(userName, content);
        }
    }

    /**
     * 合同即将到期
     *
     * @return : void
     * @Author: yml
     * @CreateDate: 2019/8/23
     * @UpdateUser: yml
     * @UpdateDate: 2019/8/23
     * @Version: 0.0.1
     */
    @Scheduled(cron = "0 0 9 * * ?")
    @Async
    public void comingToValidityPeriod() {
        log.info("com.iyin.sign.system.service.job.ContractService.comingToValidityPeriod start");
        List<SignSysCompactInfo> signSysCompactInfos = signSysCompactInfoMapper.comingToValidityPeriod(threshold);
        for (SignSysCompactInfo signSysCompactInfo : signSysCompactInfos) {
            SignSysUserInfo signSysUserInfo = signSysUserInfoMapper.selectById(signSysCompactInfo.getUserId());
            String userName = signSysUserInfo.getUserName();
            String content = Slf4jUtil.format(comingToValidityPeriod, signSysCompactInfo.getCompactTheme());
            compactInfoService.doSendNotice(userName, content);
        }
    }
}
