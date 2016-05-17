package ru.terracorp.seller.web.rest;

import ru.terracorp.seller.SchoolApp;
import ru.terracorp.seller.domain.ClientBasket;
import ru.terracorp.seller.repository.ClientBasketRepository;
import ru.terracorp.seller.service.ClientBasketService;
import ru.terracorp.seller.web.rest.dto.ClientBasketDTO;
import ru.terracorp.seller.web.rest.mapper.ClientBasketMapper;

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
 * Test class for the ClientBasketResource REST controller.
 *
 * @see ClientBasketResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SchoolApp.class)
@WebAppConfiguration
@IntegrationTest
public class ClientBasketResourceIntTest {

    private static final String DEFAULT_CLIENT = "AAAAA";
    private static final String UPDATED_CLIENT = "BBBBB";
    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_EMPLCODE = "AAAAA";
    private static final String UPDATED_EMPLCODE = "BBBBB";
    private static final String DEFAULT_ID_CLIENT_ROOM = "AAAAA";
    private static final String UPDATED_ID_CLIENT_ROOM = "BBBBB";
    private static final String DEFAULT_INFO = "AAAAA";
    private static final String UPDATED_INFO = "BBBBB";

    @Inject
    private ClientBasketRepository clientBasketRepository;

    @Inject
    private ClientBasketMapper clientBasketMapper;

    @Inject
    private ClientBasketService clientBasketService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restClientBasketMockMvc;

    private ClientBasket clientBasket;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ClientBasketResource clientBasketResource = new ClientBasketResource();
        ReflectionTestUtils.setField(clientBasketResource, "clientBasketService", clientBasketService);
        ReflectionTestUtils.setField(clientBasketResource, "clientBasketMapper", clientBasketMapper);
        this.restClientBasketMockMvc = MockMvcBuilders.standaloneSetup(clientBasketResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        clientBasketRepository.deleteAll();
        clientBasket = new ClientBasket();
        clientBasket.setClient(DEFAULT_CLIENT);
        clientBasket.setName(DEFAULT_NAME);
        clientBasket.setEmplcode(DEFAULT_EMPLCODE);
        clientBasket.setIdClientRoom(DEFAULT_ID_CLIENT_ROOM);
        clientBasket.setInfo(DEFAULT_INFO);
    }

    @Test
    public void createClientBasket() throws Exception {
        int databaseSizeBeforeCreate = clientBasketRepository.findAll().size();

        // Create the ClientBasket
        ClientBasketDTO clientBasketDTO = clientBasketMapper.clientBasketToClientBasketDTO(clientBasket);

        restClientBasketMockMvc.perform(post("/api/client-baskets")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(clientBasketDTO)))
                .andExpect(status().isCreated());

        // Validate the ClientBasket in the database
        List<ClientBasket> clientBaskets = clientBasketRepository.findAll();
        assertThat(clientBaskets).hasSize(databaseSizeBeforeCreate + 1);
        ClientBasket testClientBasket = clientBaskets.get(clientBaskets.size() - 1);
        assertThat(testClientBasket.getClient()).isEqualTo(DEFAULT_CLIENT);
        assertThat(testClientBasket.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testClientBasket.getEmplcode()).isEqualTo(DEFAULT_EMPLCODE);
        assertThat(testClientBasket.getIdClientRoom()).isEqualTo(DEFAULT_ID_CLIENT_ROOM);
        assertThat(testClientBasket.getInfo()).isEqualTo(DEFAULT_INFO);
    }

    @Test
    public void getAllClientBaskets() throws Exception {
        // Initialize the database
        clientBasketRepository.save(clientBasket);

        // Get all the clientBaskets
        restClientBasketMockMvc.perform(get("/api/client-baskets?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(clientBasket.getId())))
                .andExpect(jsonPath("$.[*].client").value(hasItem(DEFAULT_CLIENT.toString())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].emplcode").value(hasItem(DEFAULT_EMPLCODE.toString())))
                .andExpect(jsonPath("$.[*].idClientRoom").value(hasItem(DEFAULT_ID_CLIENT_ROOM.toString())))
                .andExpect(jsonPath("$.[*].info").value(hasItem(DEFAULT_INFO.toString())));
    }

    @Test
    public void getClientBasket() throws Exception {
        // Initialize the database
        clientBasketRepository.save(clientBasket);

        // Get the clientBasket
        restClientBasketMockMvc.perform(get("/api/client-baskets/{id}", clientBasket.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(clientBasket.getId()))
            .andExpect(jsonPath("$.client").value(DEFAULT_CLIENT.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.emplcode").value(DEFAULT_EMPLCODE.toString()))
            .andExpect(jsonPath("$.idClientRoom").value(DEFAULT_ID_CLIENT_ROOM.toString()))
            .andExpect(jsonPath("$.info").value(DEFAULT_INFO.toString()));
    }

    @Test
    public void getNonExistingClientBasket() throws Exception {
        // Get the clientBasket
        restClientBasketMockMvc.perform(get("/api/client-baskets/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateClientBasket() throws Exception {
        // Initialize the database
        clientBasketRepository.save(clientBasket);
        int databaseSizeBeforeUpdate = clientBasketRepository.findAll().size();

        // Update the clientBasket
        ClientBasket updatedClientBasket = new ClientBasket();
        updatedClientBasket.setId(clientBasket.getId());
        updatedClientBasket.setClient(UPDATED_CLIENT);
        updatedClientBasket.setName(UPDATED_NAME);
        updatedClientBasket.setEmplcode(UPDATED_EMPLCODE);
        updatedClientBasket.setIdClientRoom(UPDATED_ID_CLIENT_ROOM);
        updatedClientBasket.setInfo(UPDATED_INFO);
        ClientBasketDTO clientBasketDTO = clientBasketMapper.clientBasketToClientBasketDTO(updatedClientBasket);

        restClientBasketMockMvc.perform(put("/api/client-baskets")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(clientBasketDTO)))
                .andExpect(status().isOk());

        // Validate the ClientBasket in the database
        List<ClientBasket> clientBaskets = clientBasketRepository.findAll();
        assertThat(clientBaskets).hasSize(databaseSizeBeforeUpdate);
        ClientBasket testClientBasket = clientBaskets.get(clientBaskets.size() - 1);
        assertThat(testClientBasket.getClient()).isEqualTo(UPDATED_CLIENT);
        assertThat(testClientBasket.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testClientBasket.getEmplcode()).isEqualTo(UPDATED_EMPLCODE);
        assertThat(testClientBasket.getIdClientRoom()).isEqualTo(UPDATED_ID_CLIENT_ROOM);
        assertThat(testClientBasket.getInfo()).isEqualTo(UPDATED_INFO);
    }

    @Test
    public void deleteClientBasket() throws Exception {
        // Initialize the database
        clientBasketRepository.save(clientBasket);
        int databaseSizeBeforeDelete = clientBasketRepository.findAll().size();

        // Get the clientBasket
        restClientBasketMockMvc.perform(delete("/api/client-baskets/{id}", clientBasket.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ClientBasket> clientBaskets = clientBasketRepository.findAll();
        assertThat(clientBaskets).hasSize(databaseSizeBeforeDelete - 1);
    }
}
