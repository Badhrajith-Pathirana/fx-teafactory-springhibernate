package lk.beempz.tf.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

public abstract class CrudDAOImpl<T,ID> implements CrudDAO<T,ID>{

//    protected Session session;

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Session getSession(){
        return sessionFactory.getCurrentSession();
    }
    private Class<T> entity;
    public CrudDAOImpl() {
        entity = (Class<T>)((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    /*@Override
    public void setSession(Session session) {
        this.session = session;
    }*/

    @Override
    public void save(T entity) {
        getSession().persist(entity);
    }

    @Override
    public void delete(ID id) {
        T t = getSession().find(entity, id);
        getSession().remove(t);
    }

    @Override
    public void update(T entity) {
        getSession().persist(entity);
    }

    @Override
    public List<T> getAll() {
        return getSession().createQuery("FROM "+entity.getName()).list();
    }

    @Override
    public T findById(ID id) {
        return getSession().find(entity,id);
    }

    @Override
    public T saveAndGetGenerated(T entity) {
        getSession().persist(entity);
//        session.refresh(entity);
        return entity;
    }


}
