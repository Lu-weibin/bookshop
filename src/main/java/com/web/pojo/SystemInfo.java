package com.web.pojo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class SystemInfo {

    /**
     * 计算机名称
     */
    private String computerName;
    /**
     * 域名
     */
    private String domain;
    /**
     * ip
     */
    private String ip;
    /**
     * 主机名
     */
    private String hostName;
    /**
     * JVM可使用总内存
     */
    private String jvmTotalMemory;
    /**
     * JVM可使用的剩余内存
     */
    private String jvmFreeMemory;
    /**
     * JVM可使用的处理器个数
     */
    private Integer availableProcessors;
    /**
     * Java版本
     */
    private String javaVersion;
    /**
     * 操作系统
     */
    private String osName;
    /**
     * 系统构架
     */
    private String osArch;
    /**
     * 内存总量
     */
    private String memTotal;
    /**
     * 当前内存使用量
     */
    private String memUsed;
    /**
     * 当前内存剩余量
     */
    private String memFree;



}