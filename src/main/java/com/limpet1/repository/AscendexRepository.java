package com.limpet1.repository;

import com.limpet1.model.AscendexAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AscendexRepository extends JpaRepository<AscendexAccount, Integer> {

    List<AscendexAccount> findByUsersId(long userId);
}
