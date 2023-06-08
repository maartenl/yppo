package com.mrbear.yppo.jobs.delete;


import com.mrbear.yppo.services.PhotoService;
import jakarta.batch.api.chunk.AbstractItemReader;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

@Named("deletePhotographReader")
public class Reader extends AbstractItemReader
{

    private static final Logger LOGGER = Logger.getLogger(Reader.class.getName());

    private List<Long> list;
    private Iterator<Long> iterator;

    @Inject
    private PhotoService photoService;

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    @Override
    public void open(Serializable checkpoint) throws Exception
    {
        list = photoService.getAllPrimaryKeysOfPhotographs();
        iterator = list.iterator();
    }

    @Override
    public Object readItem() throws Exception
    {
        return iterator.hasNext() ? iterator.next() : null;
    }

}
