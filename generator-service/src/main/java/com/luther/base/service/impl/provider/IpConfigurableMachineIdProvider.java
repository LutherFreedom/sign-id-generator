package com.luther.base.service.impl.provider;

import com.luther.base.util.IpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class IpConfigurableMachineIdProvider implements MachineIdProvider {

    private static final Logger log = LoggerFactory.getLogger(IpConfigurableMachineIdProvider.class);

    private long machineId;

    private Map<String, Long> ipsMap = new HashMap<>();

    public IpConfigurableMachineIdProvider() {
        log.debug("IpConfigurableMachineIdProvider constructed.");
    }

    public IpConfigurableMachineIdProvider(String ips) {
        setIps(ips);
        init();
    }

    private void init() {
        String ip = IpUtils.getHostIp();
        if (StringUtils.isEmpty(ip)) {
            String msg = "Fail to get host IP address. Stop to initialize the IpConfigurableMachineIdProvider provider.";
            log.error(msg);
            throw new IllegalStateException(msg);
        }
        if (!ipsMap.containsKey(ip)) {
            String msg = String.format("Fail to configure ID for host IP address %s. Stop to initialize the IpConfigurableMachineIdProvider provider.", ip);
            log.error(msg);
            throw new IllegalStateException(msg);
        }
        machineId = ipsMap.get(ip);
        log.info("IpConfigurableMachineIdProvider.init ip {} id {}", ip, machineId);
    }

    private void setIps(String ips) {
        log.debug("IpConfigurableMachineIdProvider ips {}", ips);
        if (!StringUtils.isEmpty(ips)) {
            String[] ipArray = ips.split(",");
            for (int i = 0; i < ipArray.length; i++) {
                ipsMap.put(ipArray[i], (long) i);
            }
        }
    }

    @Override
    public long getMachineId() {
        return machineId;
    }

    public void setMachineId(long machineId) {
        this.machineId = machineId;
    }
}
