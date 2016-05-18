package ru.terracorp.seller.web.rest;

import ru.terracorp.seller.TerraSeller;
import ru.terracorp.seller.domain.ClientBasketItem;
import ru.terracorp.seller.repository.ClientBasketItemRepository;
import ru.terracorp.seller.service.ClientBasketItemService;
import ru.terracorp.seller.web.rest.dto.ClientBasketItemDTO;
import ru.terracorp.seller.web.rest.mapper.ClientBasketItemMapper;

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
 * Test class for the ClientBasketItemResource REST controller.
 *
 * @see ClientBasketItemResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TerraSeller.class)
@WebAppConfiguration
@IntegrationTest
public class ClientBasketItemResourceIntTest {

    private static final String DEFAULT_ID_CLIENT_BASKET = "AAAAA";
    private static final String UPDATED_ID_CLIENT_BASKET = "BBBBB";
    private static final String DEFAULT_CODE = "AAAAA";
    private static final String UPDATED_CODE = "BBBBB";
    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final Double DEFAULT_QTY = 1D;
    private static final Double UPDATED_QTY = 2D;

    private static final Double DEFAULT_PRICE = 1D;
    private static final Double UPDATED_PRICE = 2D;
    private static final String DEFAULT_IMGLINK = "AAAAA";
    private static final String UPDATED_IMGLINK = "BBBBB";
    private static final String DEFAULT_UNIT = "AAAAA";
    private static final String UPDATED_UNIT = "BBBBB";

    private static final Double DEFAULT_RESERV = 1D;
    private static final Double UPDATED_RESERV = 2D;
    private static final String DEFAULT_PART = "AAAAA";
    private static final String UPDATED_PART = "BBBBB";
    private static final String DEFAULT_COMBO = "AAAAA";
    private static final String UPDATED_COMBO = "BBBBB";
    private static final String DEFAULT_STOCK = "AAAAA";
    private static final String UPDATED_STOCK = "BBBBB";
    private static final String DEFAULT_USE_TYPE = "AAAAA";
    private static final String UPDATED_USE_TYPE = "BBBBB";
    private static final String DEFAULT_INFO = "AAAAA";
    private static final String UPDATED_INFO = "BBBBB";

    @Inject
    private ClientBasketItemRepository clientBasketItemRepository;

    @Inject
    private ClientBasketItemMapper clientBasketItemMapper;

    @Inject
    private ClientBasketItemService clientBasketItemService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restClientBasketItemMockMvc;

    private ClientBasketItem clientBasketItem;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ClientBasketItemResource clientBasketItemResource = new ClientBasketItemResource();
        ReflectionTestUtils.setField(clientBasketItemResource, "clientBasketItemService", clientBasketItemService);
        ReflectionTestUtils.setField(clientBasketItemResource, "clientBasketItemMapper", clientBasketItemMapper);
        this.restClientBasketItemMockMvc = MockMvcBuilders.standaloneSetup(clientBasketItemResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        clientBasketItemRepository.deleteAll();
        clientBasketItem = new ClientBasketItem();
        clientBasketItem.setIdClientBasket(DEFAULT_ID_CLIENT_BASKET);
        clientBasketItem.setCode(DEFAULT_CODE);
        clientBasketItem.setName(DEFAULT_NAME);
        clientBasketItem.setQty(DEFAULT_QTY);
        clientBasketItem.setPrice(DEFAULT_PRICE);
        clientBasketItem.setImglink(DEFAULT_IMGLINK);
        clientBasketItem.setUnit(DEFAULT_UNIT);
        clientBasketItem.setReserv(DEFAULT_RESERV);
        clientBasketItem.setPart(DEFAULT_PART);
        clientBasketItem.setCombo(DEFAULT_COMBO);
        clientBasketItem.setStock(DEFAULT_STOCK);
        clientBasketItem.setUseType(DEFAULT_USE_TYPE);
        clientBasketItem.setInfo(DEFAULT_INFO);
    }

    @Test
    public void createClientBasketItem() throws Exception {
        int databaseSizeBeforeCreate = clientBasketItemRepository.findAll().size();

        // Create the ClientBasketItem
        ClientBasketItemDTO clientBasketItemDTO = clientBasketItemMapper.clientBasketItemToClientBasketItemDTO(clientBasketItem);

        restClientBasketItemMockMvc.perform(post("/api/client-basket-items")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(clientBasketItemDTO)))
                .andExpect(status().isCreated());

        // Validate the ClientBasketItem in the database
        List<ClientBasketItem> clientBasketItems = clientBasketItemRepository.findAll();
        assertThat(clientBasketItems).hasSize(databaseSizeBeforeCreate + 1);
        ClientBasketItem testClientBasketItem = clientBasketItems.get(clientBasketItems.size() - 1);
        assertThat(testClientBasketItem.getIdClientBasket()).isEqualTo(DEFAULT_ID_CLIENT_BASKET);
        assertThat(testClientBasketItem.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testClientBasketItem.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testClientBasketItem.getQty()).isEqualTo(DEFAULT_QTY);
        assertThat(testClientBasketItem.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testClientBasketItem.getImglink()).isEqualTo(DEFAULT_IMGLINK);
        assertThat(testClientBasketItem.getUnit()).isEqualTo(DEFAULT_UNIT);
        assertThat(testClientBasketItem.getReserv()).isEqualTo(DEFAULT_RESERV);
        assertThat(testClientBasketItem.getPart()).isEqualTo(DEFAULT_PART);
        assertThat(testClientBasketItem.getCombo()).isEqualTo(DEFAULT_COMBO);
        assertThat(testClientBasketItem.getStock()).isEqualTo(DEFAULT_STOCK);
        assertThat(testClientBasketItem.getUseType()).isEqualTo(DEFAULT_USE_TYPE);
        assertThat(testClientBasketItem.getInfo()).isEqualTo(DEFAULT_INFO);
    }

    @Test
    public void getAllClientBasketItems() throws Exception {
        // Initialize the database
        clientBasketItemRepository.save(clientBasketItem);

        // Get all the clientBasketItems
        restClientBasketItemMockMvc.perform(get("/api/client-basket-items?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(clientBasketItem.getId())))
                .andExpect(jsonPath("$.[*].idClientBasket").value(hasItem(DEFAULT_ID_CLIENT_BASKET.toString())))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].qty").value(hasItem(DEFAULT_QTY.doubleValue())))
                .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
                .andExpect(jsonPath("$.[*].imglink").value(hasItem(DEFAULT_IMGLINK.toString())))
                .andExpect(jsonPath("$.[*].unit").value(hasItem(DEFAULT_UNIT.toString())))
                .andExpect(jsonPath("$.[*].reserv").value(hasItem(DEFAULT_RESERV.doubleValue())))
                .andExpect(jsonPath("$.[*].part").value(hasItem(DEFAULT_PART.toString())))
                .andExpect(jsonPath("$.[*].combo").value(hasItem(DEFAULT_COMBO.toString())))
                .andExpect(jsonPath("$.[*].stock").value(hasItem(DEFAULT_STOCK.toString())))
                .andExpect(jsonPath("$.[*].useType").value(hasItem(DEFAULT_USE_TYPE.toString())))
                .andExpect(jsonPath("$.[*].info").value(hasItem(DEFAULT_INFO.toString())));
    }

    @Test
    public void getClientBasketItem() throws Exception {
        // Initialize the database
        clientBasketItemRepository.save(clientBasketItem);

        // Get the clientBasketItem
        restClientBasketItemMockMvc.perform(get("/api/client-basket-items/{id}", clientBasketItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(clientBasketItem.getId()))
            .andExpect(jsonPath("$.idClientBasket").value(DEFAULT_ID_CLIENT_BASKET.toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.qty").value(DEFAULT_QTY.doubleValue()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.imglink").value(DEFAULT_IMGLINK.toString()))
            .andExpect(jsonPath("$.unit").value(DEFAULT_UNIT.toString()))
            .andExpect(jsonPath("$.reserv").value(DEFAULT_RESERV.doubleValue()))
            .andExpect(jsonPath("$.part").value(DEFAULT_PART.toString()))
            .andExpect(jsonPath("$.combo").value(DEFAULT_COMBO.toString()))
            .andExpect(jsonPath("$.stock").value(DEFAULT_STOCK.toString()))
            .andExpect(jsonPath("$.useType").value(DEFAULT_USE_TYPE.toString()))
            .andExpect(jsonPath("$.info").value(DEFAULT_INFO.toString()));
    }

    @Test
    public void getNonExistingClientBasketItem() throws Exception {
        // Get the clientBasketItem
        restClientBasketItemMockMvc.perform(get("/api/client-basket-items/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateClientBasketItem() throws Exception {
        // Initialize the database
        clientBasketItemRepository.save(clientBasketItem);
        int databaseSizeBeforeUpdate = clientBasketItemRepository.findAll().size();

        // Update the clientBasketItem
        ClientBasketItem updatedClientBasketItem = new ClientBasketItem();
        updatedClientBasketItem.setId(clientBasketItem.getId());
        updatedClientBasketItem.setIdClientBasket(UPDATED_ID_CLIENT_BASKET);
        updatedClientBasketItem.setCode(UPDATED_CODE);
        updatedClientBasketItem.setName(UPDATED_NAME);
        updatedClientBasketItem.setQty(UPDATED_QTY);
        updatedClientBasketItem.setPrice(UPDATED_PRICE);
        updatedClientBasketItem.setImglink(UPDATED_IMGLINK);
        updatedClientBasketItem.setUnit(UPDATED_UNIT);
        updatedClientBasketItem.setReserv(UPDATED_RESERV);
        updatedClientBasketItem.setPart(UPDATED_PART);
        updatedClientBasketItem.setCombo(UPDATED_COMBO);
        updatedClientBasketItem.setStock(UPDATED_STOCK);
        updatedClientBasketItem.setUseType(UPDATED_USE_TYPE);
        updatedClientBasketItem.setInfo(UPDATED_INFO);
        ClientBasketItemDTO clientBasketItemDTO = clientBasketItemMapper.clientBasketItemToClientBasketItemDTO(updatedClientBasketItem);

        restClientBasketItemMockMvc.perform(put("/api/client-basket-items")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(clientBasketItemDTO)))
                .andExpect(status().isOk());

        // Validate the ClientBasketItem in the database
        List<ClientBasketItem> clientBasketItems = clientBasketItemRepository.findAll();
        assertThat(clientBasketItems).hasSize(databaseSizeBeforeUpdate);
        ClientBasketItem testClientBasketItem = clientBasketItems.get(clientBasketItems.size() - 1);
        assertThat(testClientBasketItem.getIdClientBasket()).isEqualTo(UPDATED_ID_CLIENT_BASKET);
        assertThat(testClientBasketItem.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testClientBasketItem.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testClientBasketItem.getQty()).isEqualTo(UPDATED_QTY);
        assertThat(testClientBasketItem.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testClientBasketItem.getImglink()).isEqualTo(UPDATED_IMGLINK);
        assertThat(testClientBasketItem.getUnit()).isEqualTo(UPDATED_UNIT);
        assertThat(testClientBasketItem.getReserv()).isEqualTo(UPDATED_RESERV);
        assertThat(testClientBasketItem.getPart()).isEqualTo(UPDATED_PART);
        assertThat(testClientBasketItem.getCombo()).isEqualTo(UPDATED_COMBO);
        assertThat(testClientBasketItem.getStock()).isEqualTo(UPDATED_STOCK);
        assertThat(testClientBasketItem.getUseType()).isEqualTo(UPDATED_USE_TYPE);
        assertThat(testClientBasketItem.getInfo()).isEqualTo(UPDATED_INFO);
    }

    @Test
    public void deleteClientBasketItem() throws Exception {
        // Initialize the database
        clientBasketItemRepository.save(clientBasketItem);
        int databaseSizeBeforeDelete = clientBasketItemRepository.findAll().size();

        // Get the clientBasketItem
        restClientBasketItemMockMvc.perform(delete("/api/client-basket-items/{id}", clientBasketItem.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ClientBasketItem> clientBasketItems = clientBasketItemRepository.findAll();
        assertThat(clientBasketItems).hasSize(databaseSizeBeforeDelete - 1);
    }
}
