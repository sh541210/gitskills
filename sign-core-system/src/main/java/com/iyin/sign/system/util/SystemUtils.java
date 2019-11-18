package com.iyin.sign.system.util;


import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName: SystemUtils
 * @Description: 获取系统信息
 * @Author: yml
 * @CreateDate: 2019/8/30
 * @UpdateUser: yml
 * @UpdateDate: 2019/8/30
 * @Version: 1.0.0
 */
@Slf4j
public final class SystemUtils {

    private static final char CH = ',';

    /** Don't let anyone instantiate this class. */
    private SystemUtils(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    /**
     * 获取访问者IP
     * 在一般情况下使用Request.getRemoteAddr()即可，但是经过nginx等反向代理软件后，这个方法会失效。
     * <p>
     * 本方法先从Header中获取X-Real-IP，如果不存在再从X-Forwarded-For获得第一个IP(用,分割)，
     * 如果还不存在则调用Request .getRemoteAddr()。
     *
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Real-IP");
        String unknown = "unknown";
        if (ip != null && !"".equals(ip) && !unknown.equalsIgnoreCase(ip)) {
            return ip;
        }
        ip = request.getHeader("X-Forwarded-For");
        if (ip != null && !"".equals(ip) && !unknown.equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个IP值，第一个为真实IP。
            int index = ip.indexOf(CH);
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        } else {
            return request.getRemoteAddr();
        }
    }

    /**
     * 获取来访者的主机名称
     *
     * @param ip
     * @return
     */
    public static String getHostName(String ip) {
        InetAddress inet;
        try {
            inet = InetAddress.getByName(ip);
            return inet.getHostName();
        } catch (UnknownHostException e) {
            log.error("getHostName,exception:{}",e.getLocalizedMessage());
        }
        return "";
    }

    /**
     * 调用命令
     *
     * @param cmd
     * @return
     */
    private static String callCmd(String[] cmd) {
        StringBuilder result = new StringBuilder();
        String line = "";
        try {
            Process proc = Runtime.getRuntime().exec(cmd);
            InputStreamReader is = new InputStreamReader(proc.getInputStream());
            BufferedReader br = new BufferedReader(is);
            while ((line = br.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            log.error("callCmd,linux,exception:{}",e.getLocalizedMessage());
        }
        return result.toString();
    }

    /**
     * @param cmd 第一个命令
     * @param another 第二个命令
     * @return 第二个命令的执行结果
     */
    private static String callCmd(String[] cmd, String[] another) {
        StringBuilder result = new StringBuilder();
        String line = "";
        try {
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec(cmd);
            proc.waitFor(); // 已经执行完第一个命令，准备执行第二个命令
            proc = rt.exec(another);
            InputStreamReader is = new InputStreamReader(proc.getInputStream());
            BufferedReader br = new BufferedReader(is);
            while ((line = br.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            log.error("callCmd,windows,exception:{}",e.getLocalizedMessage());
        }
        return result.toString();
    }

    /**
     * @param ip 目标ip,一般在局域网内
     * @param sourceString 命令处理的结果字符串
     * @param macSeparator mac分隔符号
     * @return mac地址，用上面的分隔符号表示
     */
    private static String filterMacAddress(final String ip, String sourceString, final String macSeparator) {
        String result = "";
        int index = sourceString.indexOf(ip);
        if (index == -1) {
            index = 0;
        }
        int i = sourceString.length() - 1;
        sourceString = sourceString.substring(index, Math.max(i, 0));
        String regExp = "((([0-9,A-F,a-f]{1,2}" + macSeparator + "){1,5})[0-9,A-F,a-f]{1,2})";
        Pattern pattern = Pattern.compile(regExp);
        Matcher matcher = pattern.matcher(sourceString);
        while (matcher.find()) {
            result = matcher.group(1);
            if (sourceString.indexOf(ip) <= sourceString.lastIndexOf(matcher.group(1))) {
                break; // 如果有多个IP,只匹配本IP对应的Mac.
            }
        }
        return result;
    }

    /**
     * @param ip 目标ip
     * @return Mac Address
     */
    private static String getMacInWindows(final String ip) {
        String result = "";
        String[] cmd = {"cmd", "/c", "ping " + ip};
        String[] another = {"cmd", "/c", "arp -a"};
        String cmdResult = callCmd(cmd, another);
        result = filterMacAddress(ip, cmdResult, "-");
        return result;
    }

    /**
     * @param ip 目标ip
     * @return Mac Address
     */
    private static String getMacInLinux(final String ip) {
        String result = "";
        String[] cmd = {"/bin/sh", "-c", "ping " + ip + " -c 2 && arp -a"};
        String cmdResult = callCmd(cmd);
        result = filterMacAddress(ip, cmdResult, ":");
        return result;
    }

    /**
     * 获取MAC地址
     *
     * @return 返回MAC地址
     */
    public static String getMacAddress(String ip) {
        String macAddress = "";
//        macAddress = getMacInWindows(ip).trim();
//        log.info("com.iyin.sign.system.util.SystemUtils.getMacAddress.windows,macAddress:{}", macAddress);
//        if ("".equals(macAddress)) {
//            macAddress = getMacInLinux(ip).trim();
//        }
        return macAddress;
    }

}