package com.isoft.eg.service;

import com.isoft.eg.domain.*; // for static metamodels
import com.isoft.eg.domain.VehicleLicenseType;
import com.isoft.eg.repository.VehicleLicenseTypeRepository;
import com.isoft.eg.service.criteria.VehicleLicenseTypeCriteria;
import com.isoft.eg.service.dto.VehicleLicenseTypeDTO;
import com.isoft.eg.service.mapper.VehicleLicenseTypeMapper;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link VehicleLicenseType} entities in the database.
 * The main input is a {@link VehicleLicenseTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link VehicleLicenseTypeDTO} or a {@link Page} of {@link VehicleLicenseTypeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class VehicleLicenseTypeQueryService extends QueryService<VehicleLicenseType> {

    private final Logger log = LoggerFactory.getLogger(VehicleLicenseTypeQueryService.class);

    private final VehicleLicenseTypeRepository vehicleLicenseTypeRepository;

    private final VehicleLicenseTypeMapper vehicleLicenseTypeMapper;

    public VehicleLicenseTypeQueryService(
        VehicleLicenseTypeRepository vehicleLicenseTypeRepository,
        VehicleLicenseTypeMapper vehicleLicenseTypeMapper
    ) {
        this.vehicleLicenseTypeRepository = vehicleLicenseTypeRepository;
        this.vehicleLicenseTypeMapper = vehicleLicenseTypeMapper;
    }

    /**
     * Return a {@link List} of {@link VehicleLicenseTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<VehicleLicenseTypeDTO> findByCriteria(VehicleLicenseTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<VehicleLicenseType> specification = createSpecification(criteria);
        return vehicleLicenseTypeMapper.toDto(vehicleLicenseTypeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link VehicleLicenseTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<VehicleLicenseTypeDTO> findByCriteria(VehicleLicenseTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<VehicleLicenseType> specification = createSpecification(criteria);
        return vehicleLicenseTypeRepository.findAll(specification, page).map(vehicleLicenseTypeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(VehicleLicenseTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<VehicleLicenseType> specification = createSpecification(criteria);
        return vehicleLicenseTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link VehicleLicenseTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<VehicleLicenseType> createSpecification(VehicleLicenseTypeCriteria criteria) {
        Specification<VehicleLicenseType> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), VehicleLicenseType_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), VehicleLicenseType_.name));
            }
            if (criteria.getRank() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRank(), VehicleLicenseType_.rank));
            }
            if (criteria.getEngName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEngName(), VehicleLicenseType_.engName));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), VehicleLicenseType_.code));
            }
        }
        return specification;
    }
}
