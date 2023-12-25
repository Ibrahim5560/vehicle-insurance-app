package com.isoft.eg.repository;

import com.isoft.eg.domain.VehicleLicenseType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the VehicleLicenseType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VehicleLicenseTypeRepository
    extends JpaRepository<VehicleLicenseType, Long>, JpaSpecificationExecutor<VehicleLicenseType> {}
