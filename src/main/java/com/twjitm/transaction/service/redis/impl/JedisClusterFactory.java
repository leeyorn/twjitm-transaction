package com.twjitm.transaction.service.redis.impl;import org.apache.commons.pool2.impl.GenericObjectPoolConfig;import org.springframework.beans.factory.FactoryBean;import org.springframework.beans.factory.InitializingBean;import redis.clients.jedis.HostAndPort;import redis.clients.jedis.JedisCluster;import java.text.ParseException;import java.util.HashSet;import java.util.Set;/** * redis 集群服務 * * @author twjitm - [Created on 2018-08-27 16:24] * @company https://github.com/twjitm * @jdk java version "1.8.0_77" */public class JedisClusterFactory implements FactoryBean<JedisCluster>, InitializingBean {    private GenericObjectPoolConfig genericObjectPoolConfig;    private JedisCluster jedisCluster;    private int connectionTimeout = 2000;    private int soTimeout = 3000;    /**     * maxAttempts：出现异常最大重试次数     */    private int maxAttempts = 5;    private Set<String> jedisClusterNodes;    @Override    public void afterPropertiesSet() throws Exception {        if (jedisClusterNodes == null || jedisClusterNodes.size() == 0) {            throw new NullPointerException("jedisClusterNodes is null.");        }        Set<HostAndPort> haps = new HashSet<HostAndPort>();        for (String node : jedisClusterNodes) {            String[] arr = node.split(":");            if (arr.length != 2) {                throw new ParseException("node address error !", node.length() - 1);            }            haps.add(new HostAndPort(arr[0], Integer.valueOf(arr[1])));        }        jedisCluster = new JedisCluster(haps, connectionTimeout, soTimeout, maxAttempts, genericObjectPoolConfig);    }    @Override    public JedisCluster getObject() throws Exception {        return jedisCluster;    }    @Override    public Class<?> getObjectType() {        return (this.jedisCluster != null ? this.jedisCluster.getClass() : JedisCluster.class);    }    @Override    public boolean isSingleton() {        return true;    }    public org.apache.commons.pool2.impl.GenericObjectPoolConfig getGenericObjectPoolConfig() {        return genericObjectPoolConfig;    }    public void setGenericObjectPoolConfig(GenericObjectPoolConfig genericObjectPoolConfig) {        this.genericObjectPoolConfig = genericObjectPoolConfig;    }    public JedisCluster getJedisCluster() {        return jedisCluster;    }    public void setJedisCluster(JedisCluster jedisCluster) {        this.jedisCluster = jedisCluster;    }    public int getConnectionTimeout() {        return connectionTimeout;    }    public void setConnectionTimeout(int connectionTimeout) {        this.connectionTimeout = connectionTimeout;    }    public int getSoTimeout() {        return soTimeout;    }    public void setSoTimeout(int soTimeout) {        this.soTimeout = soTimeout;    }    public int getMaxAttempts() {        return maxAttempts;    }    public void setMaxAttempts(int maxAttempts) {        this.maxAttempts = maxAttempts;    }    public Set<String> getJedisClusterNodes() {        return jedisClusterNodes;    }    public void setJedisClusterNodes(Set<String> jedisClusterNodes) {        this.jedisClusterNodes = jedisClusterNodes;    }}