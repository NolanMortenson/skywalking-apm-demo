package com.example.rest.repos;

import com.example.rest.entities.Party;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
//@Component
public interface PartyRepo extends JpaRepository<Party, Long> {
}
