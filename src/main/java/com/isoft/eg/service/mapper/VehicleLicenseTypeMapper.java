package com.isoft.eg.service.mapper;

import com.isoft.eg.domain.VehicleLicenseType;
import com.isoft.eg.service.dto.VehicleLicenseTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link VehicleLicenseType} and its DTO {@link VehicleLicenseTypeDTO}.
 */
@Mapper(componentModel = "spring")
public interface VehicleLicenseTypeMapper extends EntityMapper<VehicleLicenseTypeDTO, VehicleLicenseType> {}
