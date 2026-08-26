package com.EDebugger.booking_logic.repositories;

import com.EDebugger.booking_logic.models.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company,Long>{
}
