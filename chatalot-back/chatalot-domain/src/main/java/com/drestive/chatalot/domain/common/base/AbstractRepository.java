package com.drestive.chatalot.domain.common.base;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by mustafa on 08/01/2016.
 */
public abstract class AbstractRepository<T extends Identifiable> {

    public enum CombineWith {
        OR, AND
    }

    protected Class<T> entityClass;

    @PersistenceContext
    protected EntityManager entityManager;

    public AbstractRepository() {
        this.entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
                .getActualTypeArguments()[0];
    }

    public T getById(Serializable id) {
        return entityManager.find(entityClass, id);
    }

    public List<T> getAll() {
        Query query = entityManager.createQuery("SELECT e FROM " + entityClass.getName() + " e");
        return query.getResultList();
    }

    public T save(T entity) {
        entityManager.persist(entity);
        Serializable id = entity.getId();
        if (id != null) {
            return getById(id);
        }

        return null;
    }

    public void update(T entity) {
        entityManager.merge(entity);
    }

    public void deleteById(Integer id) {
        Query query = entityManager.createQuery("DELETE FROM " + entityClass.getName() + " e WHERE e.id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }

    protected T getFirstBy(String propertyName, Object propertyValue) {
        Map<String, Object> params = new HashMap<>();
        params.put(propertyName, propertyValue);

        return getFirstByAll(params);
    }

    protected List getBy(String propertyName, Object propertyValue) {
        Map<String, Object> params = new HashMap<>();
        params.put(propertyName, propertyValue);

        return getByAll(params);
    }

    protected T getFirstByAll(Map<String, Object> params) {
        List resultList = getByParams(params, CombineWith.AND, 1);
        if(resultList.size() <= 0){
            return null;
        }

        return entityClass.cast(resultList.get(0));
    }

    protected List getByAll(Map<String, Object> params) {
        return getByParams(params, CombineWith.AND, null);
    }

    protected List getByAny(Map<String, Object> params) {
        return getByParams(params, CombineWith.OR, null);
    }

    protected List getByParams(Map<String, Object> params, CombineWith combineWith, Integer maxResult) {
        StringBuilder sb = new StringBuilder("SELECT e FROM " + entityClass.getName() + " e WHERE ");
        Iterator<String> propertyNameIt = params.keySet().iterator();
        while (propertyNameIt.hasNext()) {
            String propertyName = propertyNameIt.next();
            sb.append("e.").append(propertyName).append(" =:").append(propertyName.replace('.','_'));
            if(propertyNameIt.hasNext()) {
                if(combineWith == CombineWith.OR){
                    sb.append(" or ");
                }else if(combineWith == CombineWith.AND){
                    sb.append(" and ");
                }

            }
        }

        Query query = entityManager.createQuery(sb.toString());
        for (String propertyName : params.keySet()) {
            query.setParameter(propertyName.replace('.','_'), params.get(propertyName));
        }

        if(maxResult != null){
            query.setMaxResults(maxResult);
        }

        return query.getResultList();
    }
}
