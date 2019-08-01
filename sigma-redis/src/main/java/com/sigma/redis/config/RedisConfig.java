package com.sigma.redis.config;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

@Configuration
public class RedisConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisConfig.class);
    @Autowired
    private Environment env;
    private JedisPool pool;
    private String maxActive;
    private String maxIdle;
    private String maxWait;
    private String host;
    private String password;
    private String timeout;
    private String database;
    private String port;
    private String enable;
    private String sysName;
    private String nodes;

    @PostConstruct
    public void init() {
        nodes = env.getProperty("redis.cluster.nodes");
        if (StringUtils.isBlank(nodes)) {
            host = env.getProperty("redis.host");
            maxActive = env.getProperty("redis.pool.maxActive");
            maxIdle = env.getProperty("redis.pool.maxIdle");
            maxWait = env.getProperty("redis.pool.maxWait");
            password = env.getProperty("redis.password");
            timeout = env.getProperty("redis.timeout");
            database = env.getProperty("redis.database");
            port = env.getProperty("redis.port");
            sysName = env.getProperty("redis.sysName");
            enable = env.getProperty("redis.enable");
        } else {
            maxActive = env.getProperty("redis.pool.maxActive");
            maxIdle = env.getProperty("redis.pool.maxIdle");
            maxWait = env.getProperty("redis.pool.maxWait");
            password = env.getProperty("redis.password");
            timeout = env.getProperty("redis.timeout");
            database = env.getProperty("redis.database");
            port = env.getProperty("redis.port");
            sysName = env.getProperty("redis.sysName");
            enable = env.getProperty("redis.enable");
            nodes = env.getProperty("redis.cluster.nodes");
        }
    }

    @Bean
    public JedisPoolConfig constructJedisPoolConfig() {
        JedisPoolConfig config = new JedisPoolConfig();
        // 控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；
        // 如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
        config.setMaxTotal(Integer.parseInt(maxActive));
        // 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。
        config.setMaxIdle(Integer.parseInt(maxIdle));
        // 表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；
        config.setMaxWaitMillis(Integer.parseInt(maxWait));
        config.setTestOnBorrow(true);

        return config;
    }

    @Bean(name = "pool")
    public JedisPool constructJedisPool() {
        if (StringUtils.isEmpty(this.host)) {
            return null;
        }
        String ip = this.host;
        int port = Integer.parseInt(this.port);
        String password = this.password;
        int timeout = Integer.parseInt(this.timeout);
        int database = Integer.parseInt(this.database);
        if (null == pool) {
            if (StringUtils.isBlank(password)) {
                pool = new JedisPool(constructJedisPoolConfig(), ip, port, timeout);
            } else {
                pool = new JedisPool(constructJedisPoolConfig(), ip, port, timeout, password, database);
            }
        }
        return pool;
    }

    /**
     * 注意：
     * 这里返回的JedisCluster是单例的，并且可以直接注入到其他类中去使用
     *
     * @return
     */
    @Bean
    public JedisCluster getJedisCluster() {
        if (StringUtils.isEmpty(nodes)) {
            return null;
        }
        //获取服务器数组(这里要相信自己的输入，所以没有考虑空指针问题)
        String[] serverArray = nodes.split(",");
        Set<HostAndPort> nodes = new HashSet<HostAndPort>();
        for (String ipPort : serverArray) {
            String[] ipPortPair = ipPort.split(":");
            nodes.add(new HostAndPort(ipPortPair[0].trim(), Integer.valueOf(ipPortPair[1].trim())));
        }
        LOGGER.info("getJedisCluster: nodes:" + nodes);
        JedisCluster jedisCluster = new JedisCluster(nodes, Integer.parseInt(timeout));
        return jedisCluster;
    }


    public String getMaxActive() {
        return maxActive;
    }

    public void setMaxActive(String maxActive) {
        this.maxActive = maxActive;
    }

    public String getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(String maxIdle) {
        this.maxIdle = maxIdle;
    }

    public String getMaxWait() {
        return maxWait;
    }

    public void setMaxWait(String maxWait) {
        this.maxWait = maxWait;
    }

    public String getNodes() {
        return nodes;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTimeout() {
        return timeout;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getSysName() {
        return sysName;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    public String getEnable() {
        return enable;
    }

    public void setEnable(String enable) {
        this.enable = enable;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
}
