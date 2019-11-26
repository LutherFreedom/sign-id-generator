package com.luther.base.service.factory;

import com.alibaba.druid.pool.DruidDataSource;
import com.luther.base.intf.IdService;
import com.luther.base.service.impl.IdServiceImpl;
import com.luther.base.service.impl.provider.DbMachineIdProvider;
import com.luther.base.service.impl.provider.IpConfigurableMachineIdProvider;
import com.luther.base.service.impl.provider.MachineIdProvider;
import com.luther.base.service.impl.provider.PropertyMachineIdProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

public class IdServiceFactoryBean {

    protected final Logger log = LoggerFactory.getLogger(IdServiceFactoryBean.class);

    public enum Type {
        PROPERTY, IP_CONFIGURABLE, DB;
    }

    private Type providerType;
    private long machineId;
    private String ips;
    private String dbUrl;
    private String dbName;
    private String dbUser;
    private String dbPassword;
    private long genMethod = -1;
    private long type = -1;
    private long version = -1;

    private IdService idService;

    public void init() {
        if (providerType == null) {
            String errorMsg = "The type of Id service is mandatory";
            log.error(errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }

        switch (providerType) {
            case PROPERTY:
                idService = constructPropertyIdService(machineId);
                break;
            case IP_CONFIGURABLE:
                idService = constructIpConfigurableIdService(ips);
                break;
            case DB:
                idService = constructDbIdService(dbUrl, dbName, dbUser, dbPassword);
                break;
        }
    }

    public IdService getObject() {
        return idService;
    }

    private IdService constructDbIdService(String dbUrl, String dbName, String dbUser, String dbPassword) {
        log.info("Construct Db IdService dbUrl {} dbName {} dbUser {} dbPassword {}", dbUrl, dbName, dbUser, dbPassword);
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        String jdbcUrl = String.format("jdbc:mysql://%s/%s?useUnicode=yes&characterEncoding=UTF8&useSSL=false&serverTimezone=Asia/Shanghai", dbUrl, dbName);
        druidDataSource.setUrl(jdbcUrl);
        druidDataSource.setUsername(dbUser);
        druidDataSource.setPassword(dbPassword);
        druidDataSource.setMaxActive(30);
        druidDataSource.setMinIdle(5);
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setLazyInit(Boolean.FALSE);
        jdbcTemplate.setDataSource(druidDataSource);

        DbMachineIdProvider dbMachineIdProvider = new DbMachineIdProvider();
        dbMachineIdProvider.setJdbcTemplate(jdbcTemplate);
        dbMachineIdProvider.init();
        return initIdServiceImpl(dbMachineIdProvider);
    }

    private IdService constructIpConfigurableIdService(String ips) {
        log.info("Construct Ip Configurable IdService ips {}", ips);
        IpConfigurableMachineIdProvider ipConfigurableMachineIdProvider = new IpConfigurableMachineIdProvider(ips);
        return initIdServiceImpl(ipConfigurableMachineIdProvider);
    }

    private IdService constructPropertyIdService(long machineId) {
        log.info("Construct Property IdService machineId {}", machineId);
        PropertyMachineIdProvider propertyMachineIdProvider = new PropertyMachineIdProvider();
        propertyMachineIdProvider.setMachineId(machineId);
        return initIdServiceImpl(propertyMachineIdProvider);
    }

    private IdServiceImpl initIdServiceImpl(MachineIdProvider machineIdProvider) {
        IdServiceImpl idServiceImpl = new IdServiceImpl();
        idServiceImpl.setMachineIdProvider(machineIdProvider);
        if (genMethod != -1) {
            idServiceImpl.setGenMethod(genMethod);
        }
        if (type != -1) {
            idServiceImpl.setType(type);
        }
        if (version != -1) {
            idServiceImpl.setVersion(version);
        }
        idServiceImpl.init();
        return idServiceImpl;
    }

    public Class<?> getObjectType() {
        return IdService.class;
    }

    public boolean isSingleton() {
        return true;
    }

    public Type getProviderType() {
        return providerType;
    }

    public void setProviderType(Type providerType) {
        this.providerType = providerType;
    }

    public long getMachineId() {
        return machineId;
    }

    public void setMachineId(long machineId) {
        this.machineId = machineId;
    }

    public String getIps() {
        return ips;
    }

    public void setIps(String ips) {
        this.ips = ips;
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getDbUser() {
        return dbUser;
    }

    public void setDbUser(String dbUser) {
        this.dbUser = dbUser;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    public long getGenMethod() {
        return genMethod;
    }

    public void setGenMethod(long genMethod) {
        this.genMethod = genMethod;
    }

    public long getType() {
        return type;
    }

    public void setType(long type) {
        this.type = type;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }
}
