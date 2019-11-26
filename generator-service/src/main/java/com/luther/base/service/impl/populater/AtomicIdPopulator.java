package com.luther.base.service.impl.populater;

import com.luther.base.intf.Id;
import com.luther.base.service.impl.bean.IdMeta;
import com.luther.base.service.impl.bean.IdType;
import com.luther.base.util.TimeUtils;

import java.util.concurrent.atomic.AtomicReference;

/**
 * All rights Reserved, Designed by Luther
 *
 * @auther: Luther
 * @createdTime: 2019/11/24 18:52
 * @version： 0.0.1
 * @copyRight: @2019
 * TODO:
 */
public class AtomicIdPopulator implements IdPopulator, ResetPopulator {

    class Variant {
        private long sequence = 0;
        private long lastTimestamp = -1;
    }

    private AtomicReference<Variant> variant = new AtomicReference<>(new Variant());

    public AtomicIdPopulator() {
        super();
    }

    @Override
    public void populateId(Id id, IdMeta idMeta) {
        Variant varOld, varNew;
        long timestamp, sequence;
        while (true) {
            varOld = variant.get();

            timestamp = TimeUtils.genTime(IdType.parse(id.getType()));
            TimeUtils.validateTimestamp(varOld.lastTimestamp, timestamp);
            sequence = varOld.sequence;

            if (timestamp == varOld.lastTimestamp) {
                sequence++;
                sequence &= idMeta.getSeqBitsMask();
                if (sequence == 0) {
                    timestamp = TimeUtils.tillNextTimeUnit(varOld.lastTimestamp, IdType.parse(id.getType()));
                }
            } else {
                sequence = 0;
            }

            varNew = new Variant();
            varNew.sequence = sequence;
            varNew.lastTimestamp = timestamp;
            if (variant.compareAndSet(varOld, varNew)) {
                id.setSeq(sequence);
                id.setTime(timestamp);
                break;
            }
        }
    }

    @Override
    public void reset() {
        variant = new AtomicReference<>(new Variant());
    }
}
