package com.luther.base.service.impl.converter;

import com.luther.base.intf.Id;

public interface IdConverter {
    long convert(Id id);

    Id convert(long id);
}
