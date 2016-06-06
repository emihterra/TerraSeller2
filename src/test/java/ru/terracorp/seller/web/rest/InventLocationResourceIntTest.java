package ru.terracorp.seller.web.rest;

import ru.terracorp.seller.TerraSeller;
import ru.terracorp.seller.domain.InventLocation;
import ru.terracorp.seller.repository.InventLocationRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the InventLocationResource REST controller.
 *
 * @see InventLocationResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TerraSeller.class)
@WebAppConfiguration
@IntegrationTest
public class InventLocationResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAA";
    private static final String UPDATED_CODE = "BBBBB";
    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final Integer DEFAULT_PRIORITY = 1;
    private static final Integer UPDATED_PRIORITY = 2;
    private static final String DEFAULT_INFO = "AAAAA";
    private static final String UPDATED_INFO = "BBBBB";

    @Inject
    private InventLocationRepository inventLocationRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restInventLocationMockMvc;

    private InventLocation inventLocation;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        InventLocationResource inventLocationResource = new InventLocationResource();
        ReflectionTestUtils.setField(inventLocationResource, "inventLocationRepository", inventLocationRepository);
        this.restInventLocationMockMvc = MockMvcBuilders.standaloneSetup(inventLocationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        inventLocationRepository.deleteAll();
        inventLocation = new InventLocation();
        inventLocation.setCode(DEFAULT_CODE);
        inventLocation.setName(DEFAULT_NAME);
        inventLocation.setPriority(DEFAULT_PRIORITY);
        inventLocation.setInfo(DEFAULT_INFO);
    }

    @Test
    public void createInventLocation() throws Exception {
        int databaseSizeBeforeCreate = inventLocationRepository.findAll().size();

        // Create the InventLocation

        restInventLocationMockMvc.perform(post("/api/invent-locations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(inventLocation)))
                .andExpect(status().isCreated());

        // Validate the InventLocation in the database
        List<InventLocation> inventLocations = inventLocationRepository.findAll();
        assertThat(inventLocations).hasSize(databaseSizeBeforeCreate + 1);
        InventLocation testInventLocation = inventLocations.get(inventLocations.size() - 1);
        assertThat(testInventLocation.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testInventLocation.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testInventLocation.getPriority()).isEqualTo(DEFAULT_PRIORITY);
        assertThat(testInventLocation.getInfo()).isEqualTo(DEFAULT_INFO);
    }

    @Test
    public void getAllInventLocations() throws Exception {
        // Initialize the database
        inventLocationRepository.save(inventLocation);

        // Get all the inventLocations
        restInventLocationMockMvc.perform(get("/api/invent-locations?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(inventLocation.getId())))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY)))
                .andExpect(jsonPath("$.[*].info").value(hasItem(DEFAULT_INFO.toString())));
    }

    @Test
    public void getInventLocation() throws Exception {
        // Initialize the database
        inventLocationRepository.save(inventLocation);

        // Get the inventLocation
        restInventLocationMockMvc.perform(get("/api/invent-locations/{id}", inventLocation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(inventLocation.getId()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.priority").value(DEFAULT_PRIORITY))
            .andExpect(jsonPath("$.info").value(DEFAULT_INFO.toString()));
    }

    @Test
    public void getNonExistingInventLocation() throws Exception {
        // Get the inventLocation
        restInventLocationMockMvc.perform(get("/api/invent-locations/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateInventLocation() throws Exception {
        // Initialize the database
        inventLocationRepository.save(inventLocation);
        int databaseSizeBeforeUpdate = inventLocationRepository.findAll().size();

        // Update the inventLocation
        InventLocation updatedInventLocation = new InventLocation();
        updatedInventLocation.setId(inventLocation.getId());
        updatedInventLocation.setCode(UPDATED_CODE);
        updatedInventLocation.setName(UPDATED_NAME);
        updatedInventLocation.setPriority(UPDATED_PRIORITY);
        updatedInventLocation.setInfo(UPDATED_INFO);

        restInventLocationMockMvc.perform(put("/api/invent-locations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedInventLocation)))
                .andExpect(status().isOk());

        // Validate the InventLocation in the database
        List<InventLocation> inventLocations = inventLocationRepository.findAll();
        assertThat(inventLocations).hasSize(databaseSizeBeforeUpdate);
        InventLocation testInventLocation = inventLocations.get(inventLocations.size() - 1);
        assertThat(testInventLocation.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testInventLocation.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testInventLocation.getPriority()).isEqualTo(UPDATED_PRIORITY);
        assertThat(testInventLocation.getInfo()).isEqualTo(UPDATED_INFO);
    }

    @Test
    public void deleteInventLocation() throws Exception {
        // Initialize the database
        inventLocationRepository.save(inventLocation);
        int databaseSizeBeforeDelete = inventLocationRepository.findAll().size();

        // Get the inventLocation
        restInventLocationMockMvc.perform(delete("/api/invent-locations/{id}", inventLocation.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<InventLocation> inventLocations = inventLocationRepository.findAll();
        assertThat(inventLocations).hasSize(databaseSizeBeforeDelete - 1);
    }
}
