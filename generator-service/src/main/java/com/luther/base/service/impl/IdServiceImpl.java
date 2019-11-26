package com.luther.base.service.impl;

import com.luther.base.intf.Id;
import com.luther.base.service.impl.bean.IdType;
import com.luther.base.service.impl.populater.AtomicIdPopulator;
import com.luther.base.service.impl.populater.IdPopulator;
import com.luther.base.service.impl.populater.LockIdPopulator;
import com.luther.base.service.impl.populater.SyncIdPopulator;
import com.luther.base.util.CommonUtils;

public class IdServiceImpl extends AbstractIdServiceImpl {
    public static void main(String[] args) {
        System.out.println(IdServiceImpl.class.getName());
    }

    private static final String SYNC_LOCK_IMPL_KEY = "signgenerator.sync.lock.impl.key";
    private static final String ATOMIC_IMPL_KEY = "signgenerator.atomic.impl.key";

    protected IdPopulator idPopulator;

    public IdServiceImpl() {
        super();
        initPopulator();
    }

    public IdServiceImpl(String type) {
        super(type);
        initPopulator();
    }

    public IdServiceImpl(IdType type) {
        super(type);
        initPopulator();
    }

    private void initPopulator() {
        if (idPopulator != null) {
            log.info("The " + idPopulator.getClass().getCanonicalName() + "is used");
        } else if (CommonUtils.isPropKeyOn(SYNC_LOCK_IMPL_KEY)) {
            log.info("The SyncIdPopulator is used");
            idPopulator = new SyncIdPopulator();
        } else if (CommonUtils.isPropKeyOn(ATOMIC_IMPL_KEY)) {
            log.info("The AtomicIdPopulator is used");
            idPopulator = new AtomicIdPopulator();
        } else {
            log.info("The default LockIdPopulator is used");
            idPopulator = new LockIdPopulator();
        }
    }

    @Override
    protected void populateId(Id id) {
        idPopulator.populateId(id, this.idMeta);
    }

    public void setIdPopulator(IdPopulator idPopulator) {
        this.idPopulator = idPopulator;
    }
}
