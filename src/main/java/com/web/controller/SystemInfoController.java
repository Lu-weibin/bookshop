package com.web.controller;

import com.base.Result;
import com.base.StatusCode;
import com.web.pojo.SystemInfo;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Properties;

@RestController
@CrossOrigin
@RequestMapping("systemInfo")
public class SystemInfoController {

    @GetMapping
    public Result getSystemInfo() {
        SystemInfo systemInfo = new SystemInfo();
        try {
            Runtime r = Runtime.getRuntime();
            Properties props = System.getProperties();
            InetAddress addr;
            addr = InetAddress.getLocalHost();
            String ip = addr.getHostAddress();
            Map<String, String> map = System.getenv();
            // 获取计算机名
            String computerName = map.get("COMPUTERNAME");
            // 获取计算机域名
            String userDomain = map.get("USERDOMAIN");
            // 内存信息
            Sigar sigar = new Sigar();
            Mem mem = null;
            mem = sigar.getMem();
            systemInfo.setComputerName(computerName);
            systemInfo.setDomain(userDomain);
            systemInfo.setIp(ip);
            systemInfo.setHostName(addr.getHostName());
            systemInfo.setJvmTotalMemory(r.totalMemory() / 1024L / 1024L + "M");
            systemInfo.setJvmFreeMemory(r.freeMemory() / 1024L / 1024L + "M");
            systemInfo.setAvailableProcessors(r.availableProcessors());
            systemInfo.setJavaVersion(props.getProperty("java.version"));
            systemInfo.setOsName(props.getProperty("os.name"));
            systemInfo.setOsArch(props.getProperty("os.arch"));
            systemInfo.setMemTotal(mem.getTotal() / 1024L / 1024L + "M");
            systemInfo.setMemUsed(mem.getUsed() / 1024L / 1024L + "M");
            systemInfo.setMemFree(mem.getFree() / 1024L / 1024L + "M");
        } catch (SigarException | UnknownHostException e) {
            return new Result(false, StatusCode.ERROR, "读取服务器信息失败！");
        }
        return new Result(systemInfo);
    }

}
