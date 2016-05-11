package ru.terracorp.seller.web.rest;

import ru.terracorp.seller.TerraSeller;
import ru.terracorp.seller.domain.ClientRoomItem;
import ru.terracorp.seller.repository.ClientRoomItemRepository;
import ru.terracorp.seller.service.ClientRoomItemService;
import ru.terracorp.seller.web.rest.dto.ClientRoomItemDTO;
import ru.terracorp.seller.web.rest.mapper.ClientRoomItemMapper;

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
 * Test class for the ClientRoomItemResource REST controller.
 *
 * @see ClientRoomItemResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TerraSeller.class)
@WebAppConfiguration
@IntegrationTest
public class ClientRoomItemResourceIntTest {


    private static final Long DEFAULT_ID_CLIENT_ROOM = 1L;
    private static final Long UPDATED_ID_CLIENT_ROOM = 2L;
    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final Integer DEFAULT_ITEM_TYPE = 1;
    private static final Integer UPDATED_ITEM_TYPE = 2;

    private static final Integer DEFAULT_I_WIDTH = 1;
    private static final Integer UPDATED_I_WIDTH = 2;

    private static final Integer DEFAULT_I_HEIGHT = 1;
    private static final Integer UPDATED_I_HEIGHT = 2;

    private static final Integer DEFAULT_I_TOP = 1;
    private static final Integer UPDATED_I_TOP = 2;

    @Inject
    private ClientRoomItemRepository clientRoomItemRepository;

    @Inject
    private ClientRoomItemMapper clientRoomItemMapper;

    @Inject
    private ClientRoomItemService clientRoomItemService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restClientRoomItemMockMvc;

    private ClientRoomItem clientRoomItem;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ClientRoomItemResource clientRoomItemResource = new ClientRoomItemResource();
        ReflectionTestUtils.setField(clientRoomItemResource, "clientRoomItemService", clientRoomItemService);
        ReflectionTestUtils.setField(clientRoomItemResource, "clientRoomItemMapper", clientRoomItemMapper);
        this.restClientRoomItemMockMvc = MockMvcBuilders.standaloneSetup(clientRoomItemResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        clientRoomItemRepository.deleteAll();
        clientRoomItem = new ClientRoomItem();
        clientRoomItem.setId_client_room(DEFAULT_ID_CLIENT_ROOM);
        clientRoomItem.setName(DEFAULT_NAME);
        clientRoomItem.setItem_type(DEFAULT_ITEM_TYPE);
        clientRoomItem.seti_width(DEFAULT_I_WIDTH);
        clientRoomItem.seti_height(DEFAULT_I_HEIGHT);
        clientRoomItem.seti_top(DEFAULT_I_TOP);
    }

    @Test
    public void createClientRoomItem() throws Exception {
        int databaseSizeBeforeCreate = clientRoomItemRepository.findAll().size();

        // Create the ClientRoomItem
        ClientRoomItemDTO clientRoomItemDTO = clientRoomItemMapper.clientRoomItemToClientRoomItemDTO(clientRoomItem);

        restClientRoomItemMockMvc.perform(post("/api/client-room-items")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(clientRoomItemDTO)))
                .andExpect(status().isCreated());

        // Validate the ClientRoomItem in the database
        List<ClientRoomItem> clientRoomItems = clientRoomItemRepository.findAll();
        assertThat(clientRoomItems).hasSize(databaseSizeBeforeCreate + 1);
        ClientRoomItem testClientRoomItem = clientRoomItems.get(clientRoomItems.size() - 1);
        assertThat(testClientRoomItem.getId_client_room()).isEqualTo(DEFAULT_ID_CLIENT_ROOM);
        assertThat(testClientRoomItem.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testClientRoomItem.getItem_type()).isEqualTo(DEFAULT_ITEM_TYPE);
        assertThat(testClientRoomItem.geti_width()).isEqualTo(DEFAULT_I_WIDTH);
        assertThat(testClientRoomItem.geti_height()).isEqualTo(DEFAULT_I_HEIGHT);
        assertThat(testClientRoomItem.geti_top()).isEqualTo(DEFAULT_I_TOP);
    }

    @Test
    public void getAllClientRoomItems() throws Exception {
        // Initialize the database
        clientRoomItemRepository.save(clientRoomItem);

        // Get all the clientRoomItems
        restClientRoomItemMockMvc.perform(get("/api/client-room-items?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(clientRoomItem.getId())))
                .andExpect(jsonPath("$.[*].id_client_room").value(hasItem(DEFAULT_ID_CLIENT_ROOM.intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].item_type").value(hasItem(DEFAULT_ITEM_TYPE)))
                .andExpect(jsonPath("$.[*].i_width").value(hasItem(DEFAULT_I_WIDTH)))
                .andExpect(jsonPath("$.[*].i_height").value(hasItem(DEFAULT_I_HEIGHT)))
                .andExpect(jsonPath("$.[*].i_top").value(hasItem(DEFAULT_I_TOP)));
    }

    @Test
    public void getClientRoomItem() throws Exception {
        // Initialize the database
        clientRoomItemRepository.save(clientRoomItem);

        // Get the clientRoomItem
        restClientRoomItemMockMvc.perform(get("/api/client-room-items/{id}", clientRoomItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(clientRoomItem.getId()))
            .andExpect(jsonPath("$.id_client_room").value(DEFAULT_ID_CLIENT_ROOM.intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.item_type").value(DEFAULT_ITEM_TYPE))
            .andExpect(jsonPath("$.i_width").value(DEFAULT_I_WIDTH))
            .andExpect(jsonPath("$.i_height").value(DEFAULT_I_HEIGHT))
            .andExpect(jsonPath("$.i_top").value(DEFAULT_I_TOP));
    }

    @Test
    public void getNonExistingClientRoomItem() throws Exception {
        // Get the clientRoomItem
        restClientRoomItemMockMvc.perform(get("/api/client-room-items/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateClientRoomItem() throws Exception {
        // Initialize the database
        clientRoomItemRepository.save(clientRoomItem);
        int databaseSizeBeforeUpdate = clientRoomItemRepository.findAll().size();

        // Update the clientRoomItem
        ClientRoomItem updatedClientRoomItem = new ClientRoomItem();
        updatedClientRoomItem.setId(clientRoomItem.getId());
        updatedClientRoomItem.setId_client_room(UPDATED_ID_CLIENT_ROOM);
        updatedClientRoomItem.setName(UPDATED_NAME);
        updatedClientRoomItem.setItem_type(UPDATED_ITEM_TYPE);
        updatedClientRoomItem.seti_width(UPDATED_I_WIDTH);
        updatedClientRoomItem.seti_height(UPDATED_I_HEIGHT);
        updatedClientRoomItem.seti_top(UPDATED_I_TOP);
        ClientRoomItemDTO clientRoomItemDTO = clientRoomItemMapper.clientRoomItemToClientRoomItemDTO(updatedClientRoomItem);

        restClientRoomItemMockMvc.perform(put("/api/client-room-items")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(clientRoomItemDTO)))
                .andExpect(status().isOk());

        // Validate the ClientRoomItem in the database
        List<ClientRoomItem> clientRoomItems = clientRoomItemRepository.findAll();
        assertThat(clientRoomItems).hasSize(databaseSizeBeforeUpdate);
        ClientRoomItem testClientRoomItem = clientRoomItems.get(clientRoomItems.size() - 1);
        assertThat(testClientRoomItem.getId_client_room()).isEqualTo(UPDATED_ID_CLIENT_ROOM);
        assertThat(testClientRoomItem.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testClientRoomItem.getItem_type()).isEqualTo(UPDATED_ITEM_TYPE);
        assertThat(testClientRoomItem.geti_width()).isEqualTo(UPDATED_I_WIDTH);
        assertThat(testClientRoomItem.geti_height()).isEqualTo(UPDATED_I_HEIGHT);
        assertThat(testClientRoomItem.geti_top()).isEqualTo(UPDATED_I_TOP);
    }

    @Test
    public void deleteClientRoomItem() throws Exception {
        // Initialize the database
        clientRoomItemRepository.save(clientRoomItem);
        int databaseSizeBeforeDelete = clientRoomItemRepository.findAll().size();

        // Get the clientRoomItem
        restClientRoomItemMockMvc.perform(delete("/api/client-room-items/{id}", clientRoomItem.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ClientRoomItem> clientRoomItems = clientRoomItemRepository.findAll();
        assertThat(clientRoomItems).hasSize(databaseSizeBeforeDelete - 1);
    }
}
