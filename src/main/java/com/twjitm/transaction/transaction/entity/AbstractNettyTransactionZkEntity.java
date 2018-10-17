package com.twjitm.transaction.transaction.entity;import com.twjitm.transaction.lock.NettyTransactionLockInterface;import com.twjitm.transaction.lock.NettyTransactionZkLock;import com.twjitm.transaction.service.zookeeper.NettyTransactionZookeeperService;import com.twjitm.transaction.transaction.enums.NettyTransactionEntityCause;import com.twjitm.transaction.transaction.enums.NettyTransactionLockType;import com.twjitm.transaction.transaction.exception.NettyTransactionException;import java.util.BitSet;/** * 基于zookeeper 实现的分布式事物锁,、 * <p> * zookeeper 分布式锁仅仅支持独占锁模式 * * @author twjitm - [Created on 2018-08-29 16:18] * @company https://github.com/twjitm * @jdk java version "1.8.0_77" */public abstract class AbstractNettyTransactionZkEntity implements NettyTransactionEntityInterface {    /**     * 进度设置集合 主要用于rollback     */    private BitSet progressBitSet;    /**     * 事务锁     */    private NettyTransactionLockInterface nettyTransactionLock;    /**     * 锁类型     */    private NettyTransactionLockType nettyTransactionLockType;    /**     * 构建一个zookeeper类型的独占锁实体对象     *     * @param cause     * @param key     * @param zookeeperService     */    public AbstractNettyTransactionZkEntity(NettyTransactionEntityCause cause,                                            String key,                                            NettyTransactionZookeeperService                                                    zookeeperService) {        this.progressBitSet = new BitSet();        this.nettyTransactionLock = new NettyTransactionZkLock(key,                zookeeperService, cause);        this.nettyTransactionLockType = NettyTransactionLockType.WRITE;    }    /**     * 创建一个锁     *     * @param seconds     * @return     * @throws NettyTransactionException     */    @Override    public boolean createNettyTransactionLock(long seconds) throws NettyTransactionException {        return this.nettyTransactionLock.create(seconds);    }    /**     * 记录事务提交的进度，用于回滚操作。     * 根据进度进行不同程度的回滚     *     * @param step     */    public void setTransactionCommitProgress(int step) {        if (progressBitSet != null) {            progressBitSet.set(step);        }    }    /**     * 检查事物锁所处于的进度状态     *     * @param step     * @return     */    public boolean checkTransactionCommitProgress(int step) {        return this.progressBitSet.get(step);    }    /**     * 释放一个锁请求     */    @Override    public void releaseNettyTransactionLock() {        this.nettyTransactionLock.destroy();    }    @Override    public void forceReleaseNettyTransactionLock() {        this.nettyTransactionLock.destroy();    }    @Override    public String getInfo() {        return this.nettyTransactionLock.getInfo() + this.nettyTransactionLockType.name();    }    @Override    public boolean needCommit() {        return !this.nettyTransactionLockType.equals(NettyTransactionLockType.READ);    }    @Override    public NettyTransactionLockInterface getNettyTransactionLockInterface() {        return this.nettyTransactionLock;    }}