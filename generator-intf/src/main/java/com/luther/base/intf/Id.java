package com.luther.base.intf;

import java.io.Serializable;

public class Id implements Serializable {

    private static final long serialVersionUID = -5151114801293630746L;

    private long machine;
    private long seq;
    private long time;
    private long genMethod;
    private long type;
    private long version;

    public Id() {
    }

    public Id(long machine, long seq, long time, long genMethod, long type, long version) {
        super();
        this.machine = machine;
        this.seq = seq;
        this.time = time;
        this.genMethod = genMethod;
        this.type = type;
        this.version = version;
    }

    public long getMachine() {
        return machine;
    }

    public void setMachine(long machine) {
        this.machine = machine;
    }

    public long getSeq() {
        return seq;
    }

    public void setSeq(long seq) {
        this.seq = seq;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
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

    @Override
    public String toString() {
        return "Id{" +
                "machine=" + machine +
                ", seq=" + seq +
                ", time=" + time +
                ", genMethod=" + genMethod +
                ", type=" + type +
                ", version=" + version +
                '}';
    }
}
