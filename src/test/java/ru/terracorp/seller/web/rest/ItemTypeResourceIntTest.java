package ru.terracorp.seller.web.rest;

import ru.terracorp.seller.TerraSeller;
import ru.terracorp.seller.domain.ItemType;
import ru.terracorp.seller.repository.ItemTypeRepository;

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
 * Test class for the ItemTypeResource REST controller.
 *
 * @see ItemTypeResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TerraSeller.class)
@WebAppConfiguration
@IntegrationTest
public class ItemTypeResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAA";
    private static final String UPDATED_CODE = "BBBBB";
    private static final String DEFAULT_USE_TYPE = "AAAAA";
    private static final String UPDATED_USE_TYPE = "BBBBB";

    @Inject
    private ItemTypeRepository itemTypeRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restItemTypeMockMvc;

    private ItemType itemType;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ItemTypeResource itemTypeResource = new ItemTypeResource();
        ReflectionTestUtils.setField(itemTypeResource, "itemTypeRepository", itemTypeRepository);
        this.restItemTypeMockMvc = MockMvcBuilders.standaloneSetup(itemTypeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        itemTypeRepository.deleteAll();
        itemType = new ItemType();
        itemType.setCode(DEFAULT_CODE);
        itemType.setUseType(DEFAULT_USE_TYPE);
    }

    @Test
    public void createItemType() throws Exception {
        int databaseSizeBeforeCreate = itemTypeRepository.findAll().size();

        // Create the ItemType

        restItemTypeMockMvc.perform(post("/api/item-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(itemType)))
                .andExpect(status().isCreated());

        // Validate the ItemType in the database
        List<ItemType> itemTypes = itemTypeRepository.findAll();
        assertThat(itemTypes).hasSize(databaseSizeBeforeCreate + 1);
        ItemType testItemType = itemTypes.get(itemTypes.size() - 1);
        assertThat(testItemType.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testItemType.getUseType()).isEqualTo(DEFAULT_USE_TYPE);
    }

    @Test
    public void getAllItemTypes() throws Exception {
        // Initialize the database
        itemTypeRepository.save(itemType);

        // Get all the itemTypes
        restItemTypeMockMvc.perform(get("/api/item-types?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(itemType.getId())))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
                .andExpect(jsonPath("$.[*].useType").value(hasItem(DEFAULT_USE_TYPE.toString())));
    }

    @Test
    public void getItemType() throws Exception {
        // Initialize the database
        itemTypeRepository.save(itemType);

        // Get the itemType
        restItemTypeMockMvc.perform(get("/api/item-types/{id}", itemType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(itemType.getId()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.useType").value(DEFAULT_USE_TYPE.toString()));
    }

    @Test
    public void getNonExistingItemType() throws Exception {
        // Get the itemType
        restItemTypeMockMvc.perform(get("/api/item-types/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateItemType() throws Exception {
        // Initialize the database
        itemTypeRepository.save(itemType);
        int databaseSizeBeforeUpdate = itemTypeRepository.findAll().size();

        // Update the itemType
        ItemType updatedItemType = new ItemType();
        updatedItemType.setId(itemType.getId());
        updatedItemType.setCode(UPDATED_CODE);
        updatedItemType.setUseType(UPDATED_USE_TYPE);

        restItemTypeMockMvc.perform(put("/api/item-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedItemType)))
                .andExpect(status().isOk());

        // Validate the ItemType in the database
        List<ItemType> itemTypes = itemTypeRepository.findAll();
        assertThat(itemTypes).hasSize(databaseSizeBeforeUpdate);
        ItemType testItemType = itemTypes.get(itemTypes.size() - 1);
        assertThat(testItemType.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testItemType.getUseType()).isEqualTo(UPDATED_USE_TYPE);
    }

    @Test
    public void deleteItemType() throws Exception {
        // Initialize the database
        itemTypeRepository.save(itemType);
        int databaseSizeBeforeDelete = itemTypeRepository.findAll().size();

        // Get the itemType
        restItemTypeMockMvc.perform(delete("/api/item-types/{id}", itemType.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ItemType> itemTypes = itemTypeRepository.findAll();
        assertThat(itemTypes).hasSize(databaseSizeBeforeDelete - 1);
    }
}
