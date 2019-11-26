package com.luther.base.util;

import com.luther.base.service.impl.bean.IdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimeUtils {

    protected static final Logger log = LoggerFactory.getLogger(TimeUtils.class);

    //209-11-24
    public static final Long EPOCH = 1574590645998L;

    private static final String ERROR_MSG_TEMPLATE = "Clock moved backwards, Refusing to generate id for %d second/millisecond.";
    private static final String TILL_INFO_MSG_TEMPLATE = "Ids are used out during %d. Waiting till next second/millisecond.";

    public static void validateTimestamp(long lastTimestamp, long timestamp) {
        if (timestamp < lastTimestamp) {
            if (log.isErrorEnabled()) {
                log.error(String.format(ERROR_MSG_TEMPLATE, lastTimestamp - timestamp));
                throw new IllegalStateException(String.format(ERROR_MSG_TEMPLATE, lastTimestamp - timestamp));
            }
        }
    }

    public static long tillNextTimeUnit(long lastTimestamp, IdType idType) {
        if (log.isInfoEnabled()) {
            log.info(TILL_INFO_MSG_TEMPLATE, lastTimestamp);
        }
        long timestamp = TimeUtils.genTime(idType);
        while (timestamp <= lastTimestamp) {
            timestamp = TimeUtils.genTime(idType);
        }
        if (log.isInfoEnabled()) {
            log.info(String.format("Next second/millisecond %d is up"));
        }
        return timestamp;
    }

    public static long genTime(IdType idType) {
        if (idType == IdType.MIN_GRANULARITY) {
            return System.currentTimeMillis() - TimeUtils.EPOCH;
        } else {
            return (System.currentTimeMillis() - TimeUtils.EPOCH) / 1000;
        }
    }
}
