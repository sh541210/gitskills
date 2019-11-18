package com.iyin.sign.system.common.utils;

import de.schlichtherle.license.LicenseManager;
import de.schlichtherle.license.LicenseParam;

/**
 * @ClassName LicenseManagerHolder
 * @Desscription: LicenseManager容器类
 * @Author wdf
 * @Date 2019/2/23 18:38
 * @Version 1.0
 **/
public class LicenseManagerHolder {

    private LicenseManagerHolder() {}

    private static LicenseManager licenseManager;

    public static synchronized LicenseManager getLicenseManager(LicenseParam licenseParams) {
        if (licenseManager == null) {
            licenseManager = new LicenseManager(licenseParams);
        }
        return licenseManager;
    }

}
