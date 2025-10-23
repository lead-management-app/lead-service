package com.blitz.lead_service.repo;

import com.blitz.lead_service.domain.lead.Lead;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LeadRepo extends JpaRepository<Lead, UUID> {

    Optional<Lead> findByName(String name);

    Optional<Lead> findByNameOrEmailAllIgnoreCase(String name, String email);

    List<Lead> findAllByUserId(UUID userId);

    @Query("""
        select count(l.id)
        from Lead l
        where l.demoInd = 1
        """)
    int isDemoLeadsExists();

    @Query("""
        select *
        from Lead l
        where l.demo_ind = 1
        """)
    List<Lead> findAllDemoLeads();
}