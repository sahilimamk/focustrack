package com.focustrack.repository;

import com.focustrack.model.FocusEntry;
import com.focustrack.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FocusEntryRepository extends JpaRepository<FocusEntry, Long> {
    
    List<FocusEntry> findBySession(Session session);
}

