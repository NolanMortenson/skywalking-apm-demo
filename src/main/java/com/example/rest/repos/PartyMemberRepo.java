package com.example.rest.repos;

import com.example.rest.entities.PartyMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
//@Component
public interface PartyMemberRepo extends JpaRepository<PartyMember, Long> {
    Optional<PartyMember> findByUsername(String username);

    List<PartyMember> findPartyMemberByPartyId(Long id);
}
