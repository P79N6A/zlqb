package com.tasfe.zh.base.dao.entity;

import com.tasfe.zh.base.dao.id.IdGenerator;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.util.ClassUtils;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Lait on 2017/8/8.
 */
@MappedSuperclass
@Access(AccessType.FIELD)
@Data
public abstract class IdEntity<PK extends Serializable> /*implements Persistable<PK>*/ {

    @Id
    @GenericGenerator(name = "slid", strategy = IdGenerator.TYPE)
    @GeneratedValue(generator = "slid")
    @Column(length = 32)
    /*
    @GenericGenerator(name = "generatedkey", strategy = "uuid")
    @GeneratedValue(generator = "generatedkey")
    @Column(length = 32)
    */
    private PK id;

    public PK getId() {
        return id;
    }

    protected void setId(final PK id) {
        this.id = id;
    }

    @Transient
    public boolean isNew() {
        return null == getId();
    }

    @Override
    public String toString() {
        return String.format("Entity of type %s with id: %s", this.getClass().getName(), getId());
    }

    @Override
    public boolean equals(Object obj) {

        if (null == obj) {
            return false;
        }

        if (this == obj) {
            return true;
        }

        if (!getClass().equals(ClassUtils.getUserClass(obj))) {
            return false;
        }

        AbstractPersistable<?> that = (AbstractPersistable<?>) obj;

        return null != this.getId() && this.getId().equals(that.getId());
    }

    @Override
    public int hashCode() {

        int hashCode = 17;

        hashCode += null == getId() ? 0 : getId().hashCode() * 31;

        return hashCode;
    }









   /* @Id
    @GeneratedValue(generator = "generatedkey")
    @GenericGenerator(name = "generatedkey", strategy = "uuid")
    @Column(length = 32)
    protected String id;

    *//**
     * 设置id，如果id为空字符，则id=null
     *
     * @param id
     *//*
    public void setId(String id) {
        this.id = StringUtils.isNotBlank(id) ? id : null;
    }
*/

}
