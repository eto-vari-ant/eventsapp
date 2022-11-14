package com.test.eventsapp.dao;


import com.test.eventsapp.model.Event;
import com.test.eventsapp.model.EventFilter;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class EventDAOImpl implements EventDAO {

    private static final String SQL_SELECT_ALL = "SELECT e FROM Event e";

    private final SessionFactory sessionFactory;

    @Autowired
    public EventDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Event> findAll() {
        Session currentSession = sessionFactory.getCurrentSession();
        return currentSession.createQuery(SQL_SELECT_ALL, Event.class).getResultList();
    }

    @Override
    public List<Event> findAllSorted(List<String> eventsSorter) {
        Session currentSession = sessionFactory.getCurrentSession();
        Criteria criteria = currentSession.createCriteria(Event.class);
        for (String eventSorter : eventsSorter) {
            criteria.addOrder(Order.asc(eventSorter));
        }
        return (List<Event>) criteria.list();
    }

    @Override
    public List<Event> findAllFiltered(EventFilter eventFilter, List<Event> events) {
        Session currentSession = sessionFactory.getCurrentSession();
        CriteriaQuery<Event> criteriaQuery;
        Root<Event> root;
        CriteriaBuilder criteriaBuilder;
        List<Predicate> predicates;
        if (events.isEmpty()) {
            events = currentSession.createQuery(SQL_SELECT_ALL, Event.class).getResultList();
        } else{
            criteriaBuilder = currentSession.getCriteriaBuilder();
            criteriaQuery = criteriaBuilder.createQuery(Event.class);
            root = criteriaQuery.from(Event.class);
            predicates = new ArrayList<>();
            if(eventFilter.getOrganizer() != null){
                predicates.add(criteriaBuilder.equal(root.get("organizer"), eventFilter.getOrganizer()));
            }
            if(eventFilter.getTopic() != null){
                predicates.add(criteriaBuilder.equal(root.get("topic"), eventFilter.getTopic()));
            }
            if(eventFilter.getEventTime() != null){
                predicates.add(criteriaBuilder.equal(root.get("eventTime"), eventFilter.getEventTime()));
            }
            criteriaQuery = criteriaQuery.select(root).where(predicates.toArray(new Predicate[0]));
            TypedQuery<Event> typedQuery = currentSession.createQuery(criteriaQuery);
            events = typedQuery.getResultList();
        }
        return events;
    }

    @Override
    public Optional<Event> findOneById(long id) {
        Session currentSession = sessionFactory.getCurrentSession();
        return currentSession.byId(Event.class).loadOptional(id);
    }

    @Override
    public void addEvent(Event event) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.save(event);
    }

    @Override
    public void deleteEventById(long id) {
        Session currentSession = sessionFactory.getCurrentSession();
        Transaction transaction = currentSession.beginTransaction();
        Query query = currentSession.createQuery("delete Event where id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
        transaction.commit();
    }

    @Override
    public void updateEvent(Event event) {
        Session currentSession = sessionFactory.getCurrentSession();
        Transaction transaction = currentSession.beginTransaction();
        currentSession.merge(event);
        transaction.commit();
    }
}
