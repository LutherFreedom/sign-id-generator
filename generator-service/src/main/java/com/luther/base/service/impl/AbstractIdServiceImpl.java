package com.luther.base.service.impl;

import com.luther.base.intf.Id;
import com.luther.base.intf.IdService;
import com.luther.base.service.impl.bean.IdMeta;
import com.luther.base.service.impl.bean.IdMetaFactory;
import com.luther.base.service.impl.bean.IdType;
import com.luther.base.service.impl.converter.IdConverter;
import com.luther.base.service.impl.converter.IdConverterImpl;
import com.luther.base.service.impl.provider.MachineIdProvider;
import com.luther.base.util.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public abstract class AbstractIdServiceImpl implements IdService {

    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    protected long machineId = -1;
    protected long genMethod = 0;
    protected long type = 0;
    protected long version = 0;

    protected IdType idType;
    protected IdMeta idMeta;

    protected IdConverter idConverter;
    protected MachineIdProvider machineIdProvider;

    public AbstractIdServiceImpl() {
        idType = IdType.MAX_PEAK;
    }

    public AbstractIdServiceImpl(String type) {
        idType = IdType.parse(type);
    }

    public AbstractIdServiceImpl(IdType idType) {
        this.idType = idType;
    }

    public void init() {
        this.machineId = machineIdProvider.getMachineId();

        if (machineId < 0) {
            String msg = "The machine ID is not configured properly so that Signgenertor Service refuses to start.";
            log.error(msg);
            throw new IllegalStateException(msg);
        }

        if (this.idMeta == null) {
            setIdMeta(IdMetaFactory.getIdMeta(idType));
            setType(idType.value());
        } else {
            if (this.idMeta.getTimeBits() == 30) {
                setType(0);
            } else if (this.idMeta.getTimeBits() == 40) {
                setType(1);
            } else {
                String msg = "Init Error. The time bits in IdMeta should be Set to 30 or 40!";
                throw new RuntimeException(msg);
            }
        }
    }

    public long genId() {
        Id id = new Id();
        id.setMachine(machineId);
        id.setGenMethod(genMethod);
        id.setVersion(version);
        id.setType(type);
        populateId(id);
        long ret = idConverter.convert(id);
        if (log.isTraceEnabled()) {
            log.trace(String.format("Id: %s => %d", id, ret));
        }
        return ret;
    }

    protected abstract void populateId(Id id);

    public Date transTime(long time) {
        if (idType == IdType.MAX_PEAK) {
            return new Date(time * 1000 + TimeUtils.EPOCH);
        } else if (idType == IdType.MIN_GRANULARITY) {
            return new Date(time + TimeUtils.EPOCH);
        }
        return null;
    }

    public Id expId(long id) {
        return idConverter.convert(id);
    }

    public long makeId(long time, long seq) {
        return makeId(time, seq, machineId);
    }

    public long makeId(long time, long seq, long machine) {
        return makeId(genMethod, time, seq, machine);
    }

    public long makeId(long genMethod, long time, long seq, long machine) {
        return makeId(type, genMethod, time, seq, machine);
    }

    public long makeId(long type, long genMethod, long time, long seq, long machine) {
        return makeId(version, type, genMethod, time, seq, machine);
    }

    public long makeId(long version, long type, long genMethod, long time, long seq, long machine) {
        IdType idType = IdType.parse(type);
        Id id = new Id(machine, seq, time, genMethod, type, version);
        IdConverter idConverter = new IdConverterImpl(idType);
        return idConverter.convert(id);
    }

    public void setMachineId(long machineId) {
        this.machineId = machineId;
    }


    public void setGenMethod(long genMethod) {
        this.genMethod = genMethod;
    }


    public void setType(long type) {
        this.type = type;
    }


    public void setVersion(long version) {
        this.version = version;
    }


    public void setIdMeta(IdMeta idMeta) {
        this.idMeta = idMeta;
    }

    public void setIdConverter(IdConverter idConverter) {
        this.idConverter = idConverter;
    }

    public void setMachineIdProvider(MachineIdProvider machineIdProvider) {
        this.machineIdProvider = machineIdProvider;
    }
}
