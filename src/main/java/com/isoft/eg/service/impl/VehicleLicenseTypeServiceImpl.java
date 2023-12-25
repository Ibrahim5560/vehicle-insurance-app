package com.isoft.eg.service.impl;

import com.isoft.eg.domain.VehicleLicenseType;
import com.isoft.eg.repository.VehicleLicenseTypeRepository;
import com.isoft.eg.service.VehicleLicenseTypeService;
import com.isoft.eg.service.dto.VehicleLicenseTypeDTO;
import com.isoft.eg.service.mapper.VehicleLicenseTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.isoft.eg.domain.VehicleLicenseType}.
 */
@Service
@Transactional
public class VehicleLicenseTypeServiceImpl implements VehicleLicenseTypeService {

    private final Logger log = LoggerFactory.getLogger(VehicleLicenseTypeServiceImpl.class);

    private final VehicleLicenseTypeRepository vehicleLicenseTypeRepository;

    private final VehicleLicenseTypeMapper vehicleLicenseTypeMapper;

    public VehicleLicenseTypeServiceImpl(
        VehicleLicenseTypeRepository vehicleLicenseTypeRepository,
        VehicleLicenseTypeMapper vehicleLicenseTypeMapper
    ) {
        this.vehicleLicenseTypeRepository = vehicleLicenseTypeRepository;
        this.vehicleLicenseTypeMapper = vehicleLicenseTypeMapper;
    }

    @Override
    public VehicleLicenseTypeDTO save(VehicleLicenseTypeDTO vehicleLicenseTypeDTO) {
        log.debug("Request to save VehicleLicenseType : {}", vehicleLicenseTypeDTO);
        VehicleLicenseType vehicleLicenseType = vehicleLicenseTypeMapper.toEntity(vehicleLicenseTypeDTO);
        vehicleLicenseType = vehicleLicenseTypeRepository.save(vehicleLicenseType);
        return vehicleLicenseTypeMapper.toDto(vehicleLicenseType);
    }

    @Override
    public VehicleLicenseTypeDTO update(VehicleLicenseTypeDTO vehicleLicenseTypeDTO) {
        log.debug("Request to update VehicleLicenseType : {}", vehicleLicenseTypeDTO);
        VehicleLicenseType vehicleLicenseType = vehicleLicenseTypeMapper.toEntity(vehicleLicenseTypeDTO);
        vehicleLicenseType = vehicleLicenseTypeRepository.save(vehicleLicenseType);
        return vehicleLicenseTypeMapper.toDto(vehicleLicenseType);
    }

    @Override
    public Optional<VehicleLicenseTypeDTO> partialUpdate(VehicleLicenseTypeDTO vehicleLicenseTypeDTO) {
        log.debug("Request to partially update VehicleLicenseType : {}", vehicleLicenseTypeDTO);

        return vehicleLicenseTypeRepository
            .findById(vehicleLicenseTypeDTO.getId())
            .map(existingVehicleLicenseType -> {
                vehicleLicenseTypeMapper.partialUpdate(existingVehicleLicenseType, vehicleLicenseTypeDTO);

                return existingVehicleLicenseType;
            })
            .map(vehicleLicenseTypeRepository::save)
            .map(vehicleLicenseTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VehicleLicenseTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all VehicleLicenseTypes");
        return vehicleLicenseTypeRepository.findAll(pageable).map(vehicleLicenseTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<VehicleLicenseTypeDTO> findOne(Long id) {
        log.debug("Request to get VehicleLicenseType : {}", id);
        return vehicleLicenseTypeRepository.findById(id).map(vehicleLicenseTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete VehicleLicenseType : {}", id);
        vehicleLicenseTypeRepository.deleteById(id);
    }
}
