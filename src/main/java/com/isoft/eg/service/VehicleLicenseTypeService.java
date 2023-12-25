package com.isoft.eg.service;

import com.isoft.eg.service.dto.VehicleLicenseTypeDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.isoft.eg.domain.VehicleLicenseType}.
 */
public interface VehicleLicenseTypeService {
    /**
     * Save a vehicleLicenseType.
     *
     * @param vehicleLicenseTypeDTO the entity to save.
     * @return the persisted entity.
     */
    VehicleLicenseTypeDTO save(VehicleLicenseTypeDTO vehicleLicenseTypeDTO);

    /**
     * Updates a vehicleLicenseType.
     *
     * @param vehicleLicenseTypeDTO the entity to update.
     * @return the persisted entity.
     */
    VehicleLicenseTypeDTO update(VehicleLicenseTypeDTO vehicleLicenseTypeDTO);

    /**
     * Partially updates a vehicleLicenseType.
     *
     * @param vehicleLicenseTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<VehicleLicenseTypeDTO> partialUpdate(VehicleLicenseTypeDTO vehicleLicenseTypeDTO);

    /**
     * Get all the vehicleLicenseTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<VehicleLicenseTypeDTO> findAll(Pageable pageable);

    /**
     * Get the "id" vehicleLicenseType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<VehicleLicenseTypeDTO> findOne(Long id);

    /**
     * Delete the "id" vehicleLicenseType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
