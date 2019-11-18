package com.iyin.sign.system.common.utils;

import com.iyin.sign.system.util.MD5Util;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName MacTools
 * MacTools
 * @Author wdf
 * @Date 2019/8/20 12:04
 * @throws
 * @Version 1.0
 **/
@Slf4j
public class MacTools {

    private MacTools(){}


    /***因为一台机器不一定只有一个网卡呀，所以返回的是数组是很合理的***/
    public static List<String> getMacList() {
        ArrayList<String> tmpMacList = new ArrayList<>();
        try {
            //获取当前机器的所有网络接口的列表
            java.util.Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
            StringBuilder sb = new StringBuilder();
            while (en.hasMoreElements()) {
                NetworkInterface networkInterface = en.nextElement();
                List<InterfaceAddress> addressList = networkInterface.getInterfaceAddresses();
                for (InterfaceAddress address : addressList) {
                    InetAddress ip = address.getAddress();
                    NetworkInterface network = NetworkInterface.getByInetAddress(ip);
                    if (network == null) {
                        continue;
                    }
                    byte[] mac = network.getHardwareAddress();
                    if (mac == null) {
                        continue;
                    }
                    sb.delete(0, sb.length());
                    for (int i = 0; i < mac.length; i++) {
                        sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
                    }
                    tmpMacList.add(sb.toString());
                }
            }
            log.info("getMacList:{}",tmpMacList);
            if (tmpMacList.isEmpty()) {
                return tmpMacList;
            }
        } catch (SocketException e) {
            return tmpMacList;
        }
        /***去重，别忘了同一个网卡的ipv4,ipv6得到的mac都是一样的，肯定有重复，下面这段代码是。。流式处理***/
        return tmpMacList.stream().distinct().collect(Collectors.toList());
    }

    public static void main(String[] args) {
        System.out.println(MD5Util.getStringMD5(MD5Util.getStringMD5("00-00-00-00-00-00-00-E0")+"AnYinKeJi").toUpperCase());
    }
}
