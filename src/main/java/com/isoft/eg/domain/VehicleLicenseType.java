package com.isoft.eg.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * VehicleLicenseType (Vehicle_license_type) entity.
 *  @author Ibrahim Mohamed.
 */
@Entity
@Table(name = "vehicle_license_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VehicleLicenseType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "rank")
    private Integer rank;

    @Column(name = "eng_name")
    private String engName;

    @Column(name = "code")
    private String code;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public VehicleLicenseType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public VehicleLicenseType name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRank() {
        return this.rank;
    }

    public VehicleLicenseType rank(Integer rank) {
        this.setRank(rank);
        return this;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public String getEngName() {
        return this.engName;
    }

    public VehicleLicenseType engName(String engName) {
        this.setEngName(engName);
        return this;
    }

    public void setEngName(String engName) {
        this.engName = engName;
    }

    public String getCode() {
        return this.code;
    }

    public VehicleLicenseType code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VehicleLicenseType)) {
            return false;
        }
        return getId() != null && getId().equals(((VehicleLicenseType) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VehicleLicenseType{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", rank=" + getRank() +
            ", engName='" + getEngName() + "'" +
            ", code='" + getCode() + "'" +
            "}";
    }
}
