package com.alliance.claimsvalidationapp.repository;

import com.alliance.claimsvalidationapp.entity.Claim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClaimRespository extends JpaRepository<Claim, Long> {

}
