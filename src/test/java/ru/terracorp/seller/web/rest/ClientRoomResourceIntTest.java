package ru.terracorp.seller.web.rest;

import ru.terracorp.seller.TerraSeller;
import ru.terracorp.seller.domain.ClientRoom;
import ru.terracorp.seller.repository.ClientRoomRepository;
import ru.terracorp.seller.service.ClientRoomService;
import ru.terracorp.seller.web.rest.dto.ClientRoomDTO;
import ru.terracorp.seller.web.rest.mapper.ClientRoomMapper;

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
 * Test class for the ClientRoomResource REST controller.
 *
 * @see ClientRoomResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TerraSeller.class)
@WebAppConfiguration
@IntegrationTest
public class ClientRoomResourceIntTest {

    private static final String DEFAULT_CLIENT = "AAAAA";
    private static final String UPDATED_CLIENT = "BBBBB";
    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final Integer DEFAULT_R_LENGTH = 1;
    private static final Integer UPDATED_R_LENGTH = 2;

    private static final Integer DEFAULT_R_WIDTH = 1;
    private static final Integer UPDATED_R_WIDTH = 2;

    private static final Integer DEFAULT_R_HEIGHT = 1;
    private static final Integer UPDATED_R_HEIGHT = 2;

    private static final Integer DEFAULT_BOTTOM_BORDER_HEIGHT = 1;
    private static final Integer UPDATED_BOTTOM_BORDER_HEIGHT = 2;

    private static final Integer DEFAULT_TOP_BORDER_HEIGHT = 1;
    private static final Integer UPDATED_TOP_BORDER_HEIGHT = 2;

    @Inject
    private ClientRoomRepository clientRoomRepository;

    @Inject
    private ClientRoomMapper clientRoomMapper;

    @Inject
    private ClientRoomService clientRoomService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restClientRoomMockMvc;

    private ClientRoom clientRoom;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ClientRoomResource clientRoomResource = new ClientRoomResource();
        ReflectionTestUtils.setField(clientRoomResource, "clientRoomService", clientRoomService);
        ReflectionTestUtils.setField(clientRoomResource, "clientRoomMapper", clientRoomMapper);
        this.restClientRoomMockMvc = MockMvcBuilders.standaloneSetup(clientRoomResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        clientRoomRepository.deleteAll();
        clientRoom = new ClientRoom();
        clientRoom.setClient(DEFAULT_CLIENT);
        clientRoom.setName(DEFAULT_NAME);
        clientRoom.setr_length(DEFAULT_R_LENGTH);
        clientRoom.setr_width(DEFAULT_R_WIDTH);
        clientRoom.setr_height(DEFAULT_R_HEIGHT);
        clientRoom.setBottom_border_height(DEFAULT_BOTTOM_BORDER_HEIGHT);
        clientRoom.setTop_border_height(DEFAULT_TOP_BORDER_HEIGHT);
    }

    @Test
    public void createClientRoom() throws Exception {
        int databaseSizeBeforeCreate = clientRoomRepository.findAll().size();

        // Create the ClientRoom
        ClientRoomDTO clientRoomDTO = clientRoomMapper.clientRoomToClientRoomDTO(clientRoom);

        restClientRoomMockMvc.perform(post("/api/client-rooms")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(clientRoomDTO)))
                .andExpect(status().isCreated());

        // Validate the ClientRoom in the database
        List<ClientRoom> clientRooms = clientRoomRepository.findAll();
        assertThat(clientRooms).hasSize(databaseSizeBeforeCreate + 1);
        ClientRoom testClientRoom = clientRooms.get(clientRooms.size() - 1);
        assertThat(testClientRoom.getClient()).isEqualTo(DEFAULT_CLIENT);
        assertThat(testClientRoom.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testClientRoom.getr_length()).isEqualTo(DEFAULT_R_LENGTH);
        assertThat(testClientRoom.getr_width()).isEqualTo(DEFAULT_R_WIDTH);
        assertThat(testClientRoom.getr_height()).isEqualTo(DEFAULT_R_HEIGHT);
        assertThat(testClientRoom.getBottom_border_height()).isEqualTo(DEFAULT_BOTTOM_BORDER_HEIGHT);
        assertThat(testClientRoom.getTop_border_height()).isEqualTo(DEFAULT_TOP_BORDER_HEIGHT);
    }

    @Test
    public void getAllClientRooms() throws Exception {
        // Initialize the database
        clientRoomRepository.save(clientRoom);

        // Get all the clientRooms
        restClientRoomMockMvc.perform(get("/api/client-rooms?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(clientRoom.getId())))
                .andExpect(jsonPath("$.[*].client").value(hasItem(DEFAULT_CLIENT.toString())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].r_length").value(hasItem(DEFAULT_R_LENGTH)))
                .andExpect(jsonPath("$.[*].r_width").value(hasItem(DEFAULT_R_WIDTH)))
                .andExpect(jsonPath("$.[*].r_height").value(hasItem(DEFAULT_R_HEIGHT)))
                .andExpect(jsonPath("$.[*].bottom_border_height").value(hasItem(DEFAULT_BOTTOM_BORDER_HEIGHT)))
                .andExpect(jsonPath("$.[*].top_border_height").value(hasItem(DEFAULT_TOP_BORDER_HEIGHT)));
    }

    @Test
    public void getClientRoom() throws Exception {
        // Initialize the database
        clientRoomRepository.save(clientRoom);

        // Get the clientRoom
        restClientRoomMockMvc.perform(get("/api/client-rooms/{id}", clientRoom.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(clientRoom.getId()))
            .andExpect(jsonPath("$.client").value(DEFAULT_CLIENT.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.r_length").value(DEFAULT_R_LENGTH))
            .andExpect(jsonPath("$.r_width").value(DEFAULT_R_WIDTH))
            .andExpect(jsonPath("$.r_height").value(DEFAULT_R_HEIGHT))
            .andExpect(jsonPath("$.bottom_border_height").value(DEFAULT_BOTTOM_BORDER_HEIGHT))
            .andExpect(jsonPath("$.top_border_height").value(DEFAULT_TOP_BORDER_HEIGHT));
    }

    @Test
    public void getNonExistingClientRoom() throws Exception {
        // Get the clientRoom
        restClientRoomMockMvc.perform(get("/api/client-rooms/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateClientRoom() throws Exception {
        // Initialize the database
        clientRoomRepository.save(clientRoom);
        int databaseSizeBeforeUpdate = clientRoomRepository.findAll().size();

        // Update the clientRoom
        ClientRoom updatedClientRoom = new ClientRoom();
        updatedClientRoom.setId(clientRoom.getId());
        updatedClientRoom.setClient(UPDATED_CLIENT);
        updatedClientRoom.setName(UPDATED_NAME);
        updatedClientRoom.setr_length(UPDATED_R_LENGTH);
        updatedClientRoom.setr_width(UPDATED_R_WIDTH);
        updatedClientRoom.setr_height(UPDATED_R_HEIGHT);
        updatedClientRoom.setBottom_border_height(UPDATED_BOTTOM_BORDER_HEIGHT);
        updatedClientRoom.setTop_border_height(UPDATED_TOP_BORDER_HEIGHT);
        ClientRoomDTO clientRoomDTO = clientRoomMapper.clientRoomToClientRoomDTO(updatedClientRoom);

        restClientRoomMockMvc.perform(put("/api/client-rooms")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(clientRoomDTO)))
                .andExpect(status().isOk());

        // Validate the ClientRoom in the database
        List<ClientRoom> clientRooms = clientRoomRepository.findAll();
        assertThat(clientRooms).hasSize(databaseSizeBeforeUpdate);
        ClientRoom testClientRoom = clientRooms.get(clientRooms.size() - 1);
        assertThat(testClientRoom.getClient()).isEqualTo(UPDATED_CLIENT);
        assertThat(testClientRoom.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testClientRoom.getr_length()).isEqualTo(UPDATED_R_LENGTH);
        assertThat(testClientRoom.getr_width()).isEqualTo(UPDATED_R_WIDTH);
        assertThat(testClientRoom.getr_height()).isEqualTo(UPDATED_R_HEIGHT);
        assertThat(testClientRoom.getBottom_border_height()).isEqualTo(UPDATED_BOTTOM_BORDER_HEIGHT);
        assertThat(testClientRoom.getTop_border_height()).isEqualTo(UPDATED_TOP_BORDER_HEIGHT);
    }

    @Test
    public void deleteClientRoom() throws Exception {
        // Initialize the database
        clientRoomRepository.save(clientRoom);
        int databaseSizeBeforeDelete = clientRoomRepository.findAll().size();

        // Get the clientRoom
        restClientRoomMockMvc.perform(delete("/api/client-rooms/{id}", clientRoom.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ClientRoom> clientRooms = clientRoomRepository.findAll();
        assertThat(clientRooms).hasSize(databaseSizeBeforeDelete - 1);
    }
}
