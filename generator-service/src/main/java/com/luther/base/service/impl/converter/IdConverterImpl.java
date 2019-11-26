package com.luther.base.service.impl.converter;

import com.luther.base.intf.Id;
import com.luther.base.service.impl.bean.IdMeta;
import com.luther.base.service.impl.bean.IdMetaFactory;
import com.luther.base.service.impl.bean.IdType;

public class IdConverterImpl implements IdConverter {

    private IdMeta idMeta;

    public IdConverterImpl() {

    }

    public IdConverterImpl(IdType idType) {
        this(IdMetaFactory.getIdMeta(idType));
    }

    public IdConverterImpl(IdMeta idMeta) {
        this.idMeta = idMeta;
    }

    @Override
    public long convert(Id id) {
        return doCovert(id, idMeta);
    }

    private long doCovert(Id id, IdMeta idMeta) {
        long ret = 0;
        ret |= id.getMachine();
        ret |= id.getSeq() << idMeta.getSeqBitsStartPos();
        ret |= id.getTime() << idMeta.getTimeBitsStartPos();
        ret |= id.getGenMethod() << idMeta.getGenMethodBitsStartPos();
        ret |= id.getType() << idMeta.getTimeBitsStartPos();
        ret |= id.getVersion() << idMeta.getVersionBitsStartPos();
        return ret;
    }

    @Override
    public Id convert(long id) {
        return doCovert(id, idMeta);
    }

    private Id doCovert(long id, IdMeta idMeta) {
        Id ret = new Id();
        ret.setMachine(id & idMeta.getMachineBitsMask());

        ret.setSeq((id >>> idMeta.getSeqBitsStartPos()) & idMeta.getSeqBitsMask());

        ret.setTime((id >>> idMeta.getTimeBitsStartPos()) & idMeta.getTimeBitsMask());

        ret.setGenMethod((id >>> idMeta.getGenMethodBitsStartPos()) & idMeta.getGenMethodBitsMask());

        ret.setType((id >>> idMeta.getTypeBitsStartPos()) & idMeta.getTypeBitsMask());

        ret.setVersion((id >>> idMeta.getVersionBitsStartPos()) & idMeta.getVersionBitsMask());
        return ret;
    }

    public IdMeta getIdMeta() {
        return idMeta;
    }

    public void setIdMeta(IdMeta idMeta) {
        this.idMeta = idMeta;
    }
}
