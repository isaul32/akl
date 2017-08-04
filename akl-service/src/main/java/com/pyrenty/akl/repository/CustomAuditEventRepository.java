package com.pyrenty.akl.repository;

import com.pyrenty.akl.config.audit.AuditEventConverter;
import com.pyrenty.akl.domain.PersistentAuditEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.boot.actuate.audit.AuditEventRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 * Wraps an implementation of Spring Boot's AuditEventRepository.
 */
@Slf4j
@Repository
public class CustomAuditEventRepository {

    @Inject
    private PersistenceAuditEventRepository persistenceAuditEventRepository;

    @Bean
    public AuditEventRepository auditEventRepository() {
        return new AuditEventRepository() {

            @Inject
            private AuditEventConverter auditEventConverter;

            @Override
            public List<AuditEvent> find(String principal, Date after) {
                Iterable<PersistentAuditEvent> persistentAuditEvents;
                if (principal == null && after == null) {
                    persistentAuditEvents = persistenceAuditEventRepository.findAll();
                } else if (after == null) {
                    persistentAuditEvents = persistenceAuditEventRepository.findByPrincipal(principal);
                } else {
                    persistentAuditEvents = persistenceAuditEventRepository
                            .findByPrincipalAndAuditEventDateAfter(principal, LocalDateTime.ofInstant(after.toInstant(), ZoneId.systemDefault()));
                }
                return auditEventConverter.convertToAuditEvent(persistentAuditEvents);
            }

            @Override
            public List<AuditEvent> find(String principal, Date after, String type) {
                return null;
            }

            @Override
            @Transactional(propagation = Propagation.REQUIRES_NEW)
            public void add(AuditEvent event) {
                PersistentAuditEvent persistentAuditEvent = new PersistentAuditEvent();
                persistentAuditEvent.setPrincipal(event.getPrincipal());
                persistentAuditEvent.setAuditEventType(event.getType());
                Date timestamp = event.getTimestamp();
                persistentAuditEvent.setAuditEventDate(LocalDateTime.ofInstant(timestamp.toInstant(), ZoneId.systemDefault()));
                persistentAuditEvent.setData(auditEventConverter.convertDataToStrings(event.getData()));

                persistenceAuditEventRepository.save(persistentAuditEvent);
            }

            @Override
            public List<AuditEvent> find(Date after) {
                Iterable<PersistentAuditEvent> persistentAuditEvents;
                persistentAuditEvents = persistenceAuditEventRepository.findAll();

                return auditEventConverter.convertToAuditEvent(persistentAuditEvents);
            }
        };
    }
}
