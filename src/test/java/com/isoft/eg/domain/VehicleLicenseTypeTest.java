package com.isoft.eg.domain;

import static com.isoft.eg.domain.VehicleLicenseTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.isoft.eg.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VehicleLicenseTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VehicleLicenseType.class);
        VehicleLicenseType vehicleLicenseType1 = getVehicleLicenseTypeSample1();
        VehicleLicenseType vehicleLicenseType2 = new VehicleLicenseType();
        assertThat(vehicleLicenseType1).isNotEqualTo(vehicleLicenseType2);

        vehicleLicenseType2.setId(vehicleLicenseType1.getId());
        assertThat(vehicleLicenseType1).isEqualTo(vehicleLicenseType2);

        vehicleLicenseType2 = getVehicleLicenseTypeSample2();
        assertThat(vehicleLicenseType1).isNotEqualTo(vehicleLicenseType2);
    }
}
