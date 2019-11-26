package com.luther.base.service.impl;

import com.luther.base.intf.Id;
import com.luther.base.service.impl.bean.IdType;
import com.luther.base.service.impl.populater.IdPopulator;
import com.luther.base.service.impl.populater.ResetPopulator;
import com.luther.base.service.impl.provider.MachineIdsProvider;
import com.luther.base.util.TimeUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MachineIdsIdServiceImpl extends IdServiceImpl {
    protected long lastTimestamp = -1;

    protected Map<Long, Long> machineIdMap = new ConcurrentHashMap<>();
    public static final String STORE_FILE_NAME = "machineIdInfo.store";
    private String storeFilePath;

    private File storeFile;

    private Lock lock = new ReentrantLock();

    public void init() {
        if (!(this.machineIdProvider instanceof MachineIdsProvider)) {
            String msg = "The machineIdProvider is not a MachineIdsProvider instance so that signgenertor Service refuses to start.";
            log.error(msg);
            throw new RuntimeException(msg);
        }
        super.init();
        initStoreFile();
        initMachineId();
    }

    protected void populateId(Id id) {
        supportChangeMachineId(id);
    }

    private void supportChangeMachineId(Id id) {
        try {
            id.setMachine(this.machineId);
            idPopulator.populateId(id, this.idMeta);
            this.lastTimestamp = id.getTime();
        } catch (IllegalStateException e) {
            String msg = "Clock moved backwards, change MachineId and reset IdPopulator";
            log.warn(msg);
            lock.lock();
            try {
                if (id.getMachine() == this.machineId) {
                    changeMachineId();
                    resetIdPopulator();
                }
            } finally {
                lock.unlock();
            }
            supportChangeMachineId(id);
        }
    }

    private void resetIdPopulator() {
        if (idPopulator instanceof ResetPopulator) {
            ((ResetPopulator) idPopulator).reset();
        } else {
            try {
                IdPopulator newIdPopulator = this.idPopulator.getClass().newInstance();
                this.idPopulator = newIdPopulator;
            } catch (InstantiationException e) {
                throw new RuntimeException("Reset IdPopulator <[" + this.idPopulator.getClass().getCanonicalName() + "]> instance error", e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Reset IdPopulator <[" + this.idPopulator.getClass().getCanonicalName() + "]> instance error", e);
            }
        }
    }

    private void changeMachineId() {
        this.machineIdMap.put(this.machineId, this.lastTimestamp);
        storeFile();
        initMachineId();
    }

    private void storeFile() {
        Writer writer = null;
        try {
            writer = new FileWriter(storeFile, false);
            for (Map.Entry<Long, Long> entry : this.machineIdMap.entrySet()) {
                writer.write(entry.getKey() + ":" + entry.getValue() + "\n");
            }
        } catch (IOException e) {
            String msg = "Write machineId info to File<[" + storeFile.getAbsolutePath() + "]> error";
            log.error(msg);
            throw new RuntimeException(msg);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                }
            }
        }
    }

    private void initStoreFile() {
        if (storeFilePath == null || storeFilePath.length() == 0) {
            storeFilePath = System.getProperty("user.dir") + File.separator + STORE_FILE_NAME;
        }
    }

    private void initMachineId() {
        long startId = this.machineId;
        long newMachineId = this.machineId;
        while (true) {
            if (this.machineIdMap.containsKey(newMachineId)) {
                long timestamp = TimeUtils.genTime(IdType.parse(this.type));
                if (this.machineIdMap.get(newMachineId) < timestamp) {
                    this.machineId = newMachineId;
                    break;
                } else {
                    newMachineId = ((MachineIdsProvider) this.machineIdProvider).getNextMachineId();
                }
                if (newMachineId == startId) {
                    throw new RuntimeException("No machineId is available");
                }
            } else {
                this.machineId = newMachineId;
                break;
            }
        }
    }

    public void setStoreFilePath(String storeFilePath) {
        this.storeFilePath = storeFilePath;
    }
}
