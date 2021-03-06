/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zols.datastore.mongo;

import com.zols.datastore.DataStore;
import com.zols.datastore.domain.BaseObject;
import com.zols.datastore.domain.Entity;
import java.beans.PropertyDescriptor;
import java.util.List;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
public class MongoDataStore extends DataStore {

    @Autowired
    private MongoOperations mongoOperation;

    @SuppressWarnings("unchecked")
    public <T> T create(Object object, Class<T> clazz) {
        mongoOperation.insert(object);
        return (T) object;
    }

    public <T> T read(String name, Class<T> clazz) {
        T object = mongoOperation.findById(name, clazz);
        return object;
    }

    /**
     *
     * @param <T>
     * @param object
     * @param clazz
     * @return
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T update(Object object, Class<T> clazz) {
        mongoOperation.save(object);
        return (T) object;
    }

    @Override
    public <T> T delete(String name, Class<T> clazz) {
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(name));
        T object = mongoOperation.findAndRemove(query, clazz);
        return object;
    }

    @Override
    public <T> Page<T> list(Pageable pageable, Class<T> clazz) {
        Query query = getListQuery(pageable, null);
        int totalRecords = (int) mongoOperation.count(query, clazz);
        if (totalRecords != 0) {
            Page<T> objects = new PageImpl<T>(mongoOperation.find(query, clazz), pageable, totalRecords);
            return objects;
        }
        return null;
    }

    private Query getListQuery(Pageable pageable, Object searchObjct) {
        Query query = new Query();
        if (pageable != null) {
            query.skip(pageable.getPageNumber() * pageable.getPageSize());
            query.limit(pageable.getPageSize());
        }
        if (searchObjct != null) {
            Object propertyValue = null;
            Criteria criteria = null;
            BeanWrapper beanWrapper = new BeanWrapperImpl(searchObjct);
            for (PropertyDescriptor propertyDescriptor : beanWrapper.getPropertyDescriptors()) {
                propertyValue = beanWrapper.getPropertyValue(propertyDescriptor.getName());
                if (propertyValue != null && !(propertyValue instanceof Class)) {
                    if (criteria == null) {
                        criteria = Criteria.where(propertyDescriptor.getName()).is(propertyValue);
                    } else {
                        criteria.and(propertyDescriptor.getName()).is(propertyValue);
                    }
                }
            }
            if (criteria != null) {
                query.addCriteria(criteria);
            }

        }
        return query;
    }

    @Override
    public <T> List<T> list(Class<T> aClass) {
        Query query = getListQuery(null, null);
        int totalRecords = (int) mongoOperation.count(query, aClass);
        if (totalRecords != 0) {
            return mongoOperation.find(query, aClass);
        }
        return null;
    }

    @Override
    public <T> List<T> listByExample(T searchObject) {
        Query query = getListQuery(null, searchObject);
        return (List<T>) mongoOperation.find(query, searchObject.getClass());
    }

    @Override
    public <T> T deleteByExample(T searchObject) {
        Query query = getListQuery(null, searchObject);
        return (T) mongoOperation.findAndRemove(query, searchObject.getClass());
    }

}
