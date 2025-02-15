package com.mrbear.yppo.jobs.delete;


import com.mrbear.yppo.services.PhotoService;
import jakarta.batch.api.chunk.AbstractItemReader;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

@Named
public class DeletePhotographReader extends AbstractItemReader
{

    private static final Logger LOGGER = Logger.getLogger(DeletePhotographReader.class.getName());

    public static final AtomicLong size = new AtomicLong(1L);
    public static final AtomicLong position = new AtomicLong(0L);

    private List<Long> ids;
    private Iterator<Long> iterator;

    @Inject
    private PhotoService photoService;

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    @Override
    public void open(Serializable checkpoint) throws Exception
    {
        ids = photoService.getAllPrimaryKeysOfPhotographs();
        iterator = ids.iterator();
        size.getAndSet(ids.size());
        position.getAndSet(0L);
    }

    @Override
    public Object readItem() throws Exception
    {
        position.addAndGet(1L);
        return iterator.hasNext() ? iterator.next() : null;
    }

}
