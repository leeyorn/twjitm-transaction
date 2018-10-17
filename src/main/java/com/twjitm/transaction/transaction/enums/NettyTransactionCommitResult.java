package com.twjitm.transaction.transaction.enums;/** * 事物提交返回结果 * * @author twjitm - [Created on 2018-08-27 10:17] * @company https://github.com/twjitm * @jdk java version "1.8.0_77" */public class NettyTransactionCommitResult {    /**     * 成功     */    public static final NettyTransactionCommitResult SUCCESS =            new NettyTransactionCommitResult("success");    /**     * 失败     */    public static final NettyTransactionCommitResult COMMON_ERROR =            new NettyTransactionCommitResult("common_error");    /**     * 失败     */    public static final NettyTransactionCommitResult LOCK_ERROR =            new NettyTransactionCommitResult("lock_error");    /**     * 事务执行结果     */    private String result;    public NettyTransactionCommitResult(String result) {        this.result = result;    }    public String getResult() {        return result;    }    public void setResult(String result) {        this.result = result;    }}