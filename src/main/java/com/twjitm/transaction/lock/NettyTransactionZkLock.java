package com.twjitm.transaction.lock;import com.twjitm.transaction.service.zookeeper.NettyTransactionZookeeperService;import com.twjitm.transaction.transaction.enums.NettyTransactionEntityCause;import com.twjitm.transaction.transaction.enums.NettyTransactionLockStateEnum;import com.twjitm.transaction.transaction.exception.NettyTransactionException;import org.slf4j.Logger;import org.slf4j.LoggerFactory;/** * 基于zookeeper分布式事务锁实体,zookeeper 实现分布式锁 * 基于zookeeper实现分布式锁存在的缺点： * 由于zookeeper天生的特性，我们在创建节点的时候最好创建临时节点 * 防止长期占用锁，造成死锁。由于未知原因，可能程序释放锁失败。 * * @author twjitm - [Created on 2018-08-29 14:52] * @company https://github.com/twjitm * @jdk java version "1.8.0_77" */public class NettyTransactionZkLock implements NettyTransactionLockInterface {    private Logger logger = LoggerFactory.getLogger(NettyTransactionZkLock.class);    /**     * 事物锁关键字     */    private String lockKey;    /**     * 事物锁创建需要的zookeeper服务     */    private NettyTransactionZookeeperService zookeeperService;    /**     * 事物锁参数原因     */    private NettyTransactionEntityCause cause;    /**     * 事物锁装填     */    private NettyTransactionLockStateEnum lockStateEnum;    /**     * 分布式读锁内容     */    private String lockContext="";    public NettyTransactionZkLock(String lockKey, NettyTransactionZookeeperService            zookeeperService,                                  NettyTransactionEntityCause cause) {        super();        this.lockKey = lockKey;        this.zookeeperService = zookeeperService;        this.cause = cause;        this.lockContext="";    }    public NettyTransactionZkLock(String lockKey,                                  NettyTransactionZookeeperService zookeeperService,                                  NettyTransactionEntityCause cause,                                  NettyTransactionLockStateEnum lockState) {        super();        this.lockKey = lockKey;        this.zookeeperService = zookeeperService;        this.cause = cause;        this.lockStateEnum = lockState;    }    public NettyTransactionZkLock(String lockKey, NettyTransactionZookeeperService            zookeeperService,                                  NettyTransactionEntityCause cause,                                  NettyTransactionLockStateEnum lockState, String                                          lockContext) {        super();        this.lockKey = lockKey;        this.zookeeperService = zookeeperService;        this.cause = cause;        this.lockStateEnum = lockState;        this.lockContext = lockContext;    }    /**     * 注销一个锁     */    @Override    public void destroy() {        //这两种状态不能注销锁        if (this.lockStateEnum.equals(NettyTransactionLockStateEnum.INIT) ||                this.lockStateEnum.equals(                        NettyTransactionLockStateEnum.CREATE)) {            return;        }        String realLockKey = getLockKey(lockKey, cause);        boolean delete = zookeeperService.deleteNode(realLockKey);        if (!delete) {            logger.info("居然没有删除掉这个key=" + realLockKey);        }    }    /**     * 创建锁节点     *     * @param lockKey     * @param cause     * @return     */    public String getLockKey(String lockKey, NettyTransactionEntityCause cause) {        return lockKey + "_" + cause.getCause();    }    /**     * 创建锁     *     * @param     * @return     * @throws NettyTransactionException     */    @Override    public boolean create(long seconds) throws NettyTransactionException {        this.lockStateEnum = NettyTransactionLockStateEnum.CREATE;        boolean createFlag;        String realKey = getLockKey(lockKey, cause);        //创建节点        createFlag = zookeeperService.createNode(realKey, lockContext);        if (createFlag) {            this.lockStateEnum = NettyTransactionLockStateEnum.SUCCESS;            logger.info("创建锁成功" + this.getInfo());        } else{            logger.info("获得锁失败" + this.getInfo());        }        return createFlag;    }    @Override    public String getInfo() {        return this.lockKey + cause.getCause() + this.lockStateEnum.name() +                this.lockContext;    }    @Override    public void setContent(String lockContent) {        this.lockContext = lockContent;    }}