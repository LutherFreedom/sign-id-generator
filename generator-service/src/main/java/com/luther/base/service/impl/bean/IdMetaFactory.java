package com.luther.base.service.impl.bean;

public class IdMetaFactory {
    private static IdMeta maxPeak = new IdMeta((byte)10, (byte)20, (byte)30, (byte)2, (byte)1, (byte)1);
    private static IdMeta minGranularity = new IdMeta((byte)10, (byte)10, (byte)40, (byte)2, (byte)1, (byte)1);

    public static IdMeta getIdMeta(IdType type){
        if (IdType.MAX_PEAK == type){
            return maxPeak;
        }else if (IdType.MIN_GRANULARITY == type){
            return minGranularity;
        }
        return null;
    }
}
