package com.luther.base.service.impl.populater;

import com.luther.base.intf.Id;
import com.luther.base.service.impl.bean.IdMeta;

/**
 * All rights Reserved, Designed by Luther
 *
 * @auther: Luther
 * @createdTime: 2019/11/24 18:51
 * @version： 0.0.1
 * @copyRight: @2019
 * TODO:
 */
public class SyncIdPopulator extends BasePopulator {

    public SyncIdPopulator() {
        super();
    }

    public synchronized void populatorId(Id id, IdMeta idMeta) {
        super.populateId(id, idMeta);
    }
}
