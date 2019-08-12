package com.nyd.user.dao;

import com.nyd.user.entity.Attachment;

import java.util.List;

/**
 * Created by hwei on 2017/11/2.
 */
public interface AttachmentDao {

    List<Attachment> getAttachmentsByUserId(String userId) throws Exception;

    void save(Attachment attachment) throws Exception;
}
