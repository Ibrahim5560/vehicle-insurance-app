package com.isoft.eg.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.isoft.eg.IntegrationTest;
import com.isoft.eg.domain.VehicleLicenseType;
import com.isoft.eg.repository.VehicleLicenseTypeRepository;
import com.isoft.eg.service.dto.VehicleLicenseTypeDTO;
import com.isoft.eg.service.mapper.VehicleLicenseTypeMapper;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link VehicleLicenseTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VehicleLicenseTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_RANK = 1;
    private static final Integer UPDATED_RANK = 2;
    private static final Integer SMALLER_RANK = 1 - 1;

    private static final String DEFAULT_ENG_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ENG_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/vehicle-license-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VehicleLicenseTypeRepository vehicleLicenseTypeRepository;

    @Autowired
    private VehicleLicenseTypeMapper vehicleLicenseTypeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVehicleLicenseTypeMockMvc;

    private VehicleLicenseType vehicleLicenseType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VehicleLicenseType createEntity(EntityManager em) {
        VehicleLicenseType vehicleLicenseType = new VehicleLicenseType()
            .name(DEFAULT_NAME)
            .rank(DEFAULT_RANK)
            .engName(DEFAULT_ENG_NAME)
            .code(DEFAULT_CODE);
        return vehicleLicenseType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VehicleLicenseType createUpdatedEntity(EntityManager em) {
        VehicleLicenseType vehicleLicenseType = new VehicleLicenseType()
            .name(UPDATED_NAME)
            .rank(UPDATED_RANK)
            .engName(UPDATED_ENG_NAME)
            .code(UPDATED_CODE);
        return vehicleLicenseType;
    }

    @BeforeEach
    public void initTest() {
        vehicleLicenseType = createEntity(em);
    }

    @Test
    @Transactional
    void createVehicleLicenseType() throws Exception {
        int databaseSizeBeforeCreate = vehicleLicenseTypeRepository.findAll().size();
        // Create the VehicleLicenseType
        VehicleLicenseTypeDTO vehicleLicenseTypeDTO = vehicleLicenseTypeMapper.toDto(vehicleLicenseType);
        restVehicleLicenseTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vehicleLicenseTypeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the VehicleLicenseType in the database
        List<VehicleLicenseType> vehicleLicenseTypeList = vehicleLicenseTypeRepository.findAll();
        assertThat(vehicleLicenseTypeList).hasSize(databaseSizeBeforeCreate + 1);
        VehicleLicenseType testVehicleLicenseType = vehicleLicenseTypeList.get(vehicleLicenseTypeList.size() - 1);
        assertThat(testVehicleLicenseType.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testVehicleLicenseType.getRank()).isEqualTo(DEFAULT_RANK);
        assertThat(testVehicleLicenseType.getEngName()).isEqualTo(DEFAULT_ENG_NAME);
        assertThat(testVehicleLicenseType.getCode()).isEqualTo(DEFAULT_CODE);
    }

    @Test
    @Transactional
    void createVehicleLicenseTypeWithExistingId() throws Exception {
        // Create the VehicleLicenseType with an existing ID
        vehicleLicenseType.setId(1L);
        VehicleLicenseTypeDTO vehicleLicenseTypeDTO = vehicleLicenseTypeMapper.toDto(vehicleLicenseType);

        int databaseSizeBeforeCreate = vehicleLicenseTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVehicleLicenseTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vehicleLicenseTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VehicleLicenseType in the database
        List<VehicleLicenseType> vehicleLicenseTypeList = vehicleLicenseTypeRepository.findAll();
        assertThat(vehicleLicenseTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllVehicleLicenseTypes() throws Exception {
        // Initialize the database
        vehicleLicenseTypeRepository.saveAndFlush(vehicleLicenseType);

        // Get all the vehicleLicenseTypeList
        restVehicleLicenseTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vehicleLicenseType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].rank").value(hasItem(DEFAULT_RANK)))
            .andExpect(jsonPath("$.[*].engName").value(hasItem(DEFAULT_ENG_NAME)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)));
    }

    @Test
    @Transactional
    void getVehicleLicenseType() throws Exception {
        // Initialize the database
        vehicleLicenseTypeRepository.saveAndFlush(vehicleLicenseType);

        // Get the vehicleLicenseType
        restVehicleLicenseTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, vehicleLicenseType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vehicleLicenseType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.rank").value(DEFAULT_RANK))
            .andExpect(jsonPath("$.engName").value(DEFAULT_ENG_NAME))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE));
    }

    @Test
    @Transactional
    void getVehicleLicenseTypesByIdFiltering() throws Exception {
        // Initialize the database
        vehicleLicenseTypeRepository.saveAndFlush(vehicleLicenseType);

        Long id = vehicleLicenseType.getId();

        defaultVehicleLicenseTypeShouldBeFound("id.equals=" + id);
        defaultVehicleLicenseTypeShouldNotBeFound("id.notEquals=" + id);

        defaultVehicleLicenseTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultVehicleLicenseTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultVehicleLicenseTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultVehicleLicenseTypeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllVehicleLicenseTypesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        vehicleLicenseTypeRepository.saveAndFlush(vehicleLicenseType);

        // Get all the vehicleLicenseTypeList where name equals to DEFAULT_NAME
        defaultVehicleLicenseTypeShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the vehicleLicenseTypeList where name equals to UPDATED_NAME
        defaultVehicleLicenseTypeShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllVehicleLicenseTypesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        vehicleLicenseTypeRepository.saveAndFlush(vehicleLicenseType);

        // Get all the vehicleLicenseTypeList where name in DEFAULT_NAME or UPDATED_NAME
        defaultVehicleLicenseTypeShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the vehicleLicenseTypeList where name equals to UPDATED_NAME
        defaultVehicleLicenseTypeShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllVehicleLicenseTypesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        vehicleLicenseTypeRepository.saveAndFlush(vehicleLicenseType);

        // Get all the vehicleLicenseTypeList where name is not null
        defaultVehicleLicenseTypeShouldBeFound("name.specified=true");

        // Get all the vehicleLicenseTypeList where name is null
        defaultVehicleLicenseTypeShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllVehicleLicenseTypesByNameContainsSomething() throws Exception {
        // Initialize the database
        vehicleLicenseTypeRepository.saveAndFlush(vehicleLicenseType);

        // Get all the vehicleLicenseTypeList where name contains DEFAULT_NAME
        defaultVehicleLicenseTypeShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the vehicleLicenseTypeList where name contains UPDATED_NAME
        defaultVehicleLicenseTypeShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllVehicleLicenseTypesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        vehicleLicenseTypeRepository.saveAndFlush(vehicleLicenseType);

        // Get all the vehicleLicenseTypeList where name does not contain DEFAULT_NAME
        defaultVehicleLicenseTypeShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the vehicleLicenseTypeList where name does not contain UPDATED_NAME
        defaultVehicleLicenseTypeShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllVehicleLicenseTypesByRankIsEqualToSomething() throws Exception {
        // Initialize the database
        vehicleLicenseTypeRepository.saveAndFlush(vehicleLicenseType);

        // Get all the vehicleLicenseTypeList where rank equals to DEFAULT_RANK
        defaultVehicleLicenseTypeShouldBeFound("rank.equals=" + DEFAULT_RANK);

        // Get all the vehicleLicenseTypeList where rank equals to UPDATED_RANK
        defaultVehicleLicenseTypeShouldNotBeFound("rank.equals=" + UPDATED_RANK);
    }

    @Test
    @Transactional
    void getAllVehicleLicenseTypesByRankIsInShouldWork() throws Exception {
        // Initialize the database
        vehicleLicenseTypeRepository.saveAndFlush(vehicleLicenseType);

        // Get all the vehicleLicenseTypeList where rank in DEFAULT_RANK or UPDATED_RANK
        defaultVehicleLicenseTypeShouldBeFound("rank.in=" + DEFAULT_RANK + "," + UPDATED_RANK);

        // Get all the vehicleLicenseTypeList where rank equals to UPDATED_RANK
        defaultVehicleLicenseTypeShouldNotBeFound("rank.in=" + UPDATED_RANK);
    }

    @Test
    @Transactional
    void getAllVehicleLicenseTypesByRankIsNullOrNotNull() throws Exception {
        // Initialize the database
        vehicleLicenseTypeRepository.saveAndFlush(vehicleLicenseType);

        // Get all the vehicleLicenseTypeList where rank is not null
        defaultVehicleLicenseTypeShouldBeFound("rank.specified=true");

        // Get all the vehicleLicenseTypeList where rank is null
        defaultVehicleLicenseTypeShouldNotBeFound("rank.specified=false");
    }

    @Test
    @Transactional
    void getAllVehicleLicenseTypesByRankIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        vehicleLicenseTypeRepository.saveAndFlush(vehicleLicenseType);

        // Get all the vehicleLicenseTypeList where rank is greater than or equal to DEFAULT_RANK
        defaultVehicleLicenseTypeShouldBeFound("rank.greaterThanOrEqual=" + DEFAULT_RANK);

        // Get all the vehicleLicenseTypeList where rank is greater than or equal to UPDATED_RANK
        defaultVehicleLicenseTypeShouldNotBeFound("rank.greaterThanOrEqual=" + UPDATED_RANK);
    }

    @Test
    @Transactional
    void getAllVehicleLicenseTypesByRankIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        vehicleLicenseTypeRepository.saveAndFlush(vehicleLicenseType);

        // Get all the vehicleLicenseTypeList where rank is less than or equal to DEFAULT_RANK
        defaultVehicleLicenseTypeShouldBeFound("rank.lessThanOrEqual=" + DEFAULT_RANK);

        // Get all the vehicleLicenseTypeList where rank is less than or equal to SMALLER_RANK
        defaultVehicleLicenseTypeShouldNotBeFound("rank.lessThanOrEqual=" + SMALLER_RANK);
    }

    @Test
    @Transactional
    void getAllVehicleLicenseTypesByRankIsLessThanSomething() throws Exception {
        // Initialize the database
        vehicleLicenseTypeRepository.saveAndFlush(vehicleLicenseType);

        // Get all the vehicleLicenseTypeList where rank is less than DEFAULT_RANK
        defaultVehicleLicenseTypeShouldNotBeFound("rank.lessThan=" + DEFAULT_RANK);

        // Get all the vehicleLicenseTypeList where rank is less than UPDATED_RANK
        defaultVehicleLicenseTypeShouldBeFound("rank.lessThan=" + UPDATED_RANK);
    }

    @Test
    @Transactional
    void getAllVehicleLicenseTypesByRankIsGreaterThanSomething() throws Exception {
        // Initialize the database
        vehicleLicenseTypeRepository.saveAndFlush(vehicleLicenseType);

        // Get all the vehicleLicenseTypeList where rank is greater than DEFAULT_RANK
        defaultVehicleLicenseTypeShouldNotBeFound("rank.greaterThan=" + DEFAULT_RANK);

        // Get all the vehicleLicenseTypeList where rank is greater than SMALLER_RANK
        defaultVehicleLicenseTypeShouldBeFound("rank.greaterThan=" + SMALLER_RANK);
    }

    @Test
    @Transactional
    void getAllVehicleLicenseTypesByEngNameIsEqualToSomething() throws Exception {
        // Initialize the database
        vehicleLicenseTypeRepository.saveAndFlush(vehicleLicenseType);

        // Get all the vehicleLicenseTypeList where engName equals to DEFAULT_ENG_NAME
        defaultVehicleLicenseTypeShouldBeFound("engName.equals=" + DEFAULT_ENG_NAME);

        // Get all the vehicleLicenseTypeList where engName equals to UPDATED_ENG_NAME
        defaultVehicleLicenseTypeShouldNotBeFound("engName.equals=" + UPDATED_ENG_NAME);
    }

    @Test
    @Transactional
    void getAllVehicleLicenseTypesByEngNameIsInShouldWork() throws Exception {
        // Initialize the database
        vehicleLicenseTypeRepository.saveAndFlush(vehicleLicenseType);

        // Get all the vehicleLicenseTypeList where engName in DEFAULT_ENG_NAME or UPDATED_ENG_NAME
        defaultVehicleLicenseTypeShouldBeFound("engName.in=" + DEFAULT_ENG_NAME + "," + UPDATED_ENG_NAME);

        // Get all the vehicleLicenseTypeList where engName equals to UPDATED_ENG_NAME
        defaultVehicleLicenseTypeShouldNotBeFound("engName.in=" + UPDATED_ENG_NAME);
    }

    @Test
    @Transactional
    void getAllVehicleLicenseTypesByEngNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        vehicleLicenseTypeRepository.saveAndFlush(vehicleLicenseType);

        // Get all the vehicleLicenseTypeList where engName is not null
        defaultVehicleLicenseTypeShouldBeFound("engName.specified=true");

        // Get all the vehicleLicenseTypeList where engName is null
        defaultVehicleLicenseTypeShouldNotBeFound("engName.specified=false");
    }

    @Test
    @Transactional
    void getAllVehicleLicenseTypesByEngNameContainsSomething() throws Exception {
        // Initialize the database
        vehicleLicenseTypeRepository.saveAndFlush(vehicleLicenseType);

        // Get all the vehicleLicenseTypeList where engName contains DEFAULT_ENG_NAME
        defaultVehicleLicenseTypeShouldBeFound("engName.contains=" + DEFAULT_ENG_NAME);

        // Get all the vehicleLicenseTypeList where engName contains UPDATED_ENG_NAME
        defaultVehicleLicenseTypeShouldNotBeFound("engName.contains=" + UPDATED_ENG_NAME);
    }

    @Test
    @Transactional
    void getAllVehicleLicenseTypesByEngNameNotContainsSomething() throws Exception {
        // Initialize the database
        vehicleLicenseTypeRepository.saveAndFlush(vehicleLicenseType);

        // Get all the vehicleLicenseTypeList where engName does not contain DEFAULT_ENG_NAME
        defaultVehicleLicenseTypeShouldNotBeFound("engName.doesNotContain=" + DEFAULT_ENG_NAME);

        // Get all the vehicleLicenseTypeList where engName does not contain UPDATED_ENG_NAME
        defaultVehicleLicenseTypeShouldBeFound("engName.doesNotContain=" + UPDATED_ENG_NAME);
    }

    @Test
    @Transactional
    void getAllVehicleLicenseTypesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        vehicleLicenseTypeRepository.saveAndFlush(vehicleLicenseType);

        // Get all the vehicleLicenseTypeList where code equals to DEFAULT_CODE
        defaultVehicleLicenseTypeShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the vehicleLicenseTypeList where code equals to UPDATED_CODE
        defaultVehicleLicenseTypeShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllVehicleLicenseTypesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        vehicleLicenseTypeRepository.saveAndFlush(vehicleLicenseType);

        // Get all the vehicleLicenseTypeList where code in DEFAULT_CODE or UPDATED_CODE
        defaultVehicleLicenseTypeShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the vehicleLicenseTypeList where code equals to UPDATED_CODE
        defaultVehicleLicenseTypeShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllVehicleLicenseTypesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        vehicleLicenseTypeRepository.saveAndFlush(vehicleLicenseType);

        // Get all the vehicleLicenseTypeList where code is not null
        defaultVehicleLicenseTypeShouldBeFound("code.specified=true");

        // Get all the vehicleLicenseTypeList where code is null
        defaultVehicleLicenseTypeShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllVehicleLicenseTypesByCodeContainsSomething() throws Exception {
        // Initialize the database
        vehicleLicenseTypeRepository.saveAndFlush(vehicleLicenseType);

        // Get all the vehicleLicenseTypeList where code contains DEFAULT_CODE
        defaultVehicleLicenseTypeShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the vehicleLicenseTypeList where code contains UPDATED_CODE
        defaultVehicleLicenseTypeShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllVehicleLicenseTypesByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        vehicleLicenseTypeRepository.saveAndFlush(vehicleLicenseType);

        // Get all the vehicleLicenseTypeList where code does not contain DEFAULT_CODE
        defaultVehicleLicenseTypeShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the vehicleLicenseTypeList where code does not contain UPDATED_CODE
        defaultVehicleLicenseTypeShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultVehicleLicenseTypeShouldBeFound(String filter) throws Exception {
        restVehicleLicenseTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vehicleLicenseType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].rank").value(hasItem(DEFAULT_RANK)))
            .andExpect(jsonPath("$.[*].engName").value(hasItem(DEFAULT_ENG_NAME)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)));

        // Check, that the count call also returns 1
        restVehicleLicenseTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultVehicleLicenseTypeShouldNotBeFound(String filter) throws Exception {
        restVehicleLicenseTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restVehicleLicenseTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingVehicleLicenseType() throws Exception {
        // Get the vehicleLicenseType
        restVehicleLicenseTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVehicleLicenseType() throws Exception {
        // Initialize the database
        vehicleLicenseTypeRepository.saveAndFlush(vehicleLicenseType);

        int databaseSizeBeforeUpdate = vehicleLicenseTypeRepository.findAll().size();

        // Update the vehicleLicenseType
        VehicleLicenseType updatedVehicleLicenseType = vehicleLicenseTypeRepository.findById(vehicleLicenseType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedVehicleLicenseType are not directly saved in db
        em.detach(updatedVehicleLicenseType);
        updatedVehicleLicenseType.name(UPDATED_NAME).rank(UPDATED_RANK).engName(UPDATED_ENG_NAME).code(UPDATED_CODE);
        VehicleLicenseTypeDTO vehicleLicenseTypeDTO = vehicleLicenseTypeMapper.toDto(updatedVehicleLicenseType);

        restVehicleLicenseTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vehicleLicenseTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vehicleLicenseTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the VehicleLicenseType in the database
        List<VehicleLicenseType> vehicleLicenseTypeList = vehicleLicenseTypeRepository.findAll();
        assertThat(vehicleLicenseTypeList).hasSize(databaseSizeBeforeUpdate);
        VehicleLicenseType testVehicleLicenseType = vehicleLicenseTypeList.get(vehicleLicenseTypeList.size() - 1);
        assertThat(testVehicleLicenseType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testVehicleLicenseType.getRank()).isEqualTo(UPDATED_RANK);
        assertThat(testVehicleLicenseType.getEngName()).isEqualTo(UPDATED_ENG_NAME);
        assertThat(testVehicleLicenseType.getCode()).isEqualTo(UPDATED_CODE);
    }

    @Test
    @Transactional
    void putNonExistingVehicleLicenseType() throws Exception {
        int databaseSizeBeforeUpdate = vehicleLicenseTypeRepository.findAll().size();
        vehicleLicenseType.setId(longCount.incrementAndGet());

        // Create the VehicleLicenseType
        VehicleLicenseTypeDTO vehicleLicenseTypeDTO = vehicleLicenseTypeMapper.toDto(vehicleLicenseType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVehicleLicenseTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vehicleLicenseTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vehicleLicenseTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VehicleLicenseType in the database
        List<VehicleLicenseType> vehicleLicenseTypeList = vehicleLicenseTypeRepository.findAll();
        assertThat(vehicleLicenseTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVehicleLicenseType() throws Exception {
        int databaseSizeBeforeUpdate = vehicleLicenseTypeRepository.findAll().size();
        vehicleLicenseType.setId(longCount.incrementAndGet());

        // Create the VehicleLicenseType
        VehicleLicenseTypeDTO vehicleLicenseTypeDTO = vehicleLicenseTypeMapper.toDto(vehicleLicenseType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleLicenseTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vehicleLicenseTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VehicleLicenseType in the database
        List<VehicleLicenseType> vehicleLicenseTypeList = vehicleLicenseTypeRepository.findAll();
        assertThat(vehicleLicenseTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVehicleLicenseType() throws Exception {
        int databaseSizeBeforeUpdate = vehicleLicenseTypeRepository.findAll().size();
        vehicleLicenseType.setId(longCount.incrementAndGet());

        // Create the VehicleLicenseType
        VehicleLicenseTypeDTO vehicleLicenseTypeDTO = vehicleLicenseTypeMapper.toDto(vehicleLicenseType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleLicenseTypeMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vehicleLicenseTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the VehicleLicenseType in the database
        List<VehicleLicenseType> vehicleLicenseTypeList = vehicleLicenseTypeRepository.findAll();
        assertThat(vehicleLicenseTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVehicleLicenseTypeWithPatch() throws Exception {
        // Initialize the database
        vehicleLicenseTypeRepository.saveAndFlush(vehicleLicenseType);

        int databaseSizeBeforeUpdate = vehicleLicenseTypeRepository.findAll().size();

        // Update the vehicleLicenseType using partial update
        VehicleLicenseType partialUpdatedVehicleLicenseType = new VehicleLicenseType();
        partialUpdatedVehicleLicenseType.setId(vehicleLicenseType.getId());

        restVehicleLicenseTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVehicleLicenseType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVehicleLicenseType))
            )
            .andExpect(status().isOk());

        // Validate the VehicleLicenseType in the database
        List<VehicleLicenseType> vehicleLicenseTypeList = vehicleLicenseTypeRepository.findAll();
        assertThat(vehicleLicenseTypeList).hasSize(databaseSizeBeforeUpdate);
        VehicleLicenseType testVehicleLicenseType = vehicleLicenseTypeList.get(vehicleLicenseTypeList.size() - 1);
        assertThat(testVehicleLicenseType.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testVehicleLicenseType.getRank()).isEqualTo(DEFAULT_RANK);
        assertThat(testVehicleLicenseType.getEngName()).isEqualTo(DEFAULT_ENG_NAME);
        assertThat(testVehicleLicenseType.getCode()).isEqualTo(DEFAULT_CODE);
    }

    @Test
    @Transactional
    void fullUpdateVehicleLicenseTypeWithPatch() throws Exception {
        // Initialize the database
        vehicleLicenseTypeRepository.saveAndFlush(vehicleLicenseType);

        int databaseSizeBeforeUpdate = vehicleLicenseTypeRepository.findAll().size();

        // Update the vehicleLicenseType using partial update
        VehicleLicenseType partialUpdatedVehicleLicenseType = new VehicleLicenseType();
        partialUpdatedVehicleLicenseType.setId(vehicleLicenseType.getId());

        partialUpdatedVehicleLicenseType.name(UPDATED_NAME).rank(UPDATED_RANK).engName(UPDATED_ENG_NAME).code(UPDATED_CODE);

        restVehicleLicenseTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVehicleLicenseType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVehicleLicenseType))
            )
            .andExpect(status().isOk());

        // Validate the VehicleLicenseType in the database
        List<VehicleLicenseType> vehicleLicenseTypeList = vehicleLicenseTypeRepository.findAll();
        assertThat(vehicleLicenseTypeList).hasSize(databaseSizeBeforeUpdate);
        VehicleLicenseType testVehicleLicenseType = vehicleLicenseTypeList.get(vehicleLicenseTypeList.size() - 1);
        assertThat(testVehicleLicenseType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testVehicleLicenseType.getRank()).isEqualTo(UPDATED_RANK);
        assertThat(testVehicleLicenseType.getEngName()).isEqualTo(UPDATED_ENG_NAME);
        assertThat(testVehicleLicenseType.getCode()).isEqualTo(UPDATED_CODE);
    }

    @Test
    @Transactional
    void patchNonExistingVehicleLicenseType() throws Exception {
        int databaseSizeBeforeUpdate = vehicleLicenseTypeRepository.findAll().size();
        vehicleLicenseType.setId(longCount.incrementAndGet());

        // Create the VehicleLicenseType
        VehicleLicenseTypeDTO vehicleLicenseTypeDTO = vehicleLicenseTypeMapper.toDto(vehicleLicenseType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVehicleLicenseTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vehicleLicenseTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vehicleLicenseTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VehicleLicenseType in the database
        List<VehicleLicenseType> vehicleLicenseTypeList = vehicleLicenseTypeRepository.findAll();
        assertThat(vehicleLicenseTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVehicleLicenseType() throws Exception {
        int databaseSizeBeforeUpdate = vehicleLicenseTypeRepository.findAll().size();
        vehicleLicenseType.setId(longCount.incrementAndGet());

        // Create the VehicleLicenseType
        VehicleLicenseTypeDTO vehicleLicenseTypeDTO = vehicleLicenseTypeMapper.toDto(vehicleLicenseType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleLicenseTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vehicleLicenseTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VehicleLicenseType in the database
        List<VehicleLicenseType> vehicleLicenseTypeList = vehicleLicenseTypeRepository.findAll();
        assertThat(vehicleLicenseTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVehicleLicenseType() throws Exception {
        int databaseSizeBeforeUpdate = vehicleLicenseTypeRepository.findAll().size();
        vehicleLicenseType.setId(longCount.incrementAndGet());

        // Create the VehicleLicenseType
        VehicleLicenseTypeDTO vehicleLicenseTypeDTO = vehicleLicenseTypeMapper.toDto(vehicleLicenseType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleLicenseTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vehicleLicenseTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the VehicleLicenseType in the database
        List<VehicleLicenseType> vehicleLicenseTypeList = vehicleLicenseTypeRepository.findAll();
        assertThat(vehicleLicenseTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVehicleLicenseType() throws Exception {
        // Initialize the database
        vehicleLicenseTypeRepository.saveAndFlush(vehicleLicenseType);

        int databaseSizeBeforeDelete = vehicleLicenseTypeRepository.findAll().size();

        // Delete the vehicleLicenseType
        restVehicleLicenseTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, vehicleLicenseType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<VehicleLicenseType> vehicleLicenseTypeList = vehicleLicenseTypeRepository.findAll();
        assertThat(vehicleLicenseTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
