package com.luther.base.service.impl.populater;

import com.luther.base.intf.Id;
import com.luther.base.service.impl.bean.IdMeta;

public interface IdPopulator {
    void populateId(Id id, IdMeta idMeta);
}
