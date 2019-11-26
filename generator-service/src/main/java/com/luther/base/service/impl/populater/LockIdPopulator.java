package com.luther.base.service.impl.populater;

import com.luther.base.intf.Id;
import com.luther.base.service.impl.bean.IdMeta;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * All rights Reserved, Designed by Luther
 *
 * @auther: Luther
 * @createdTime: 2019/11/24 18:49
 * @version： 0.0.1
 * @copyRight: @2019
 * TODO:
 */
public class LockIdPopulator extends BasePopulator {

    private Lock lock = new ReentrantLock();

    public LockIdPopulator(){
        super();
    }

    public void populateId(Id id, IdMeta idMeta){
        lock.lock();
        try {
            super.populateId(id, idMeta);
        }finally {
            lock.unlock();
        }
    }
}
