package com.tasfe.zh.base.service.impls;

import com.tasfe.zh.base.dao.GenericCrudRepository;
import com.tasfe.zh.base.dao.jpa.impls.Query;
import com.tasfe.zh.base.entity.File;
import com.tasfe.zh.base.service.FileService;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by Lait on 2017/8/1.
 */
@Service
public class FileServiceImpl extends CrudServiceImpl<File, File, Long> implements FileService<File, File, Long> {

    //@PersistenceContext
    private EntityManager entityManager;


    @Override
    public void download(String path, String uri) {

    }

    public boolean isMd5Exist(String md5) {
        Query query = new Query(entityManager);
        @SuppressWarnings("unchecked")
        List<File> result = query.from(File.class)
                .select()
                .whereEqual("MD5", md5)
                .createTypedQuery()
                .getResultList();
        return !result.isEmpty();
    }


    public GenericCrudRepository getGenericCrudRepository() {
        return null;
    }
}
