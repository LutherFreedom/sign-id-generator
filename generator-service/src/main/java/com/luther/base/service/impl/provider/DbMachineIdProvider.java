package com.luther.base.service.impl.provider;

import com.luther.base.util.IpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;

public class DbMachineIdProvider implements MachineIdProvider {

    private static final Logger log = LoggerFactory.getLogger(DbMachineIdProvider.class);

    private long machineId;

    private JdbcTemplate jdbcTemplate;

    public DbMachineIdProvider() {
        log.debug("DbMachineIdProvider constructed");
    }

    public void init() {
        String ip = IpUtils.getHostIp();
        if (StringUtils.isEmpty(ip)) {
            String msg = "Fail to get host IP address. Stop to initialize the DbMachineIdProvider provider.";

            log.error(msg);
            throw new IllegalStateException(msg);
        }
        Long id = null;
        try {
            id = jdbcTemplate.queryForObject("SELECT ID FROM DB_MACHINE_ID_PROVIDER WHERE IP = ?", new Object[]{ip}, Long.class);
        } catch (EmptyResultDataAccessException e) {
            log.error("No allocation before for ip {}.", ip);
        }
        if (id != null) {
            machineId = id;
            return;
        }
        log.info("Fail to get ID from DB for host IP address {}. Next step try to allocate one.", ip);
        int count = jdbcTemplate.update("UPDATE  DB_MACHINE_ID_PROVIDER SET IP = ? WHERE IP IS NULL limit 1");
        if (count <= 0 || count > 1) {
            String msg = String.format("Fail to allocate ID for host IP address{} after allocation. Stop to initialize the DbMachineIdProvider provider.", ip);
            log.error(msg);
            throw new IllegalStateException(msg);
        }
        try {
            id = jdbcTemplate.queryForObject("SELECT ID FROM DB_MACHINE_ID_PROVIDER WHERE IP = ?", new Object[]{ip}, Long.class);
        } catch (EmptyResultDataAccessException e) {
            log.error("Fail to do allocation for ip {}.", ip);
        }
        if (id == null) {
            String msg = String.format("Fail to get ID from DB for host IP address {} after allocation. Stop to initialize the DbMachineIdProvider provider.",
                            ip);

            log.error(msg);
            throw new IllegalStateException(msg);
        }
        machineId = id;
    }

    @Override
    public long getMachineId() {
        return machineId;
    }

    public void setMachineId(long machineId) {
        this.machineId = machineId;
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
