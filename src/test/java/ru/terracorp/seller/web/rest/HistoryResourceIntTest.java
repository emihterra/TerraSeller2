package ru.terracorp.seller.web.rest;

import ru.terracorp.seller.TerraSeller;
import ru.terracorp.seller.domain.History;
import ru.terracorp.seller.repository.HistoryRepository;

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
 * Test class for the HistoryResource REST controller.
 *
 * @see HistoryResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TerraSeller.class)
@WebAppConfiguration
@IntegrationTest
public class HistoryResourceIntTest {

    private static final String DEFAULT_CLIENT = "AAAAA";
    private static final String UPDATED_CLIENT = "BBBBB";
    private static final String DEFAULT_EMPLCODE = "AAAAA";
    private static final String UPDATED_EMPLCODE = "BBBBB";

    private static final Integer DEFAULT_TYPE = 1;
    private static final Integer UPDATED_TYPE = 2;
    private static final String DEFAULT_INFO = "AAAAA";
    private static final String UPDATED_INFO = "BBBBB";
    private static final String DEFAULT_ITEMS = "AAAAA";
    private static final String UPDATED_ITEMS = "BBBBB";

    @Inject
    private HistoryRepository historyRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restHistoryMockMvc;

    private History history;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        HistoryResource historyResource = new HistoryResource();
        ReflectionTestUtils.setField(historyResource, "historyRepository", historyRepository);
        this.restHistoryMockMvc = MockMvcBuilders.standaloneSetup(historyResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        historyRepository.deleteAll();
        history = new History();
        history.setClient(DEFAULT_CLIENT);
        history.setEmplcode(DEFAULT_EMPLCODE);
        history.setType(DEFAULT_TYPE);
        history.setInfo(DEFAULT_INFO);
        history.setItems(DEFAULT_ITEMS);
    }

    @Test
    public void createHistory() throws Exception {
        int databaseSizeBeforeCreate = historyRepository.findAll().size();

        // Create the History

        restHistoryMockMvc.perform(post("/api/histories")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(history)))
                .andExpect(status().isCreated());

        // Validate the History in the database
        List<History> histories = historyRepository.findAll();
        assertThat(histories).hasSize(databaseSizeBeforeCreate + 1);
        History testHistory = histories.get(histories.size() - 1);
        assertThat(testHistory.getClient()).isEqualTo(DEFAULT_CLIENT);
        assertThat(testHistory.getEmplcode()).isEqualTo(DEFAULT_EMPLCODE);
        assertThat(testHistory.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testHistory.getInfo()).isEqualTo(DEFAULT_INFO);
        assertThat(testHistory.getItems()).isEqualTo(DEFAULT_ITEMS);
    }

    @Test
    public void getAllHistories() throws Exception {
        // Initialize the database
        historyRepository.save(history);

        // Get all the histories
        restHistoryMockMvc.perform(get("/api/histories?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(history.getId())))
                .andExpect(jsonPath("$.[*].client").value(hasItem(DEFAULT_CLIENT.toString())))
                .andExpect(jsonPath("$.[*].emplcode").value(hasItem(DEFAULT_EMPLCODE.toString())))
                .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
                .andExpect(jsonPath("$.[*].info").value(hasItem(DEFAULT_INFO.toString())))
                .andExpect(jsonPath("$.[*].items").value(hasItem(DEFAULT_ITEMS.toString())));
    }

    @Test
    public void getHistory() throws Exception {
        // Initialize the database
        historyRepository.save(history);

        // Get the history
        restHistoryMockMvc.perform(get("/api/histories/{id}", history.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(history.getId()))
            .andExpect(jsonPath("$.client").value(DEFAULT_CLIENT.toString()))
            .andExpect(jsonPath("$.emplcode").value(DEFAULT_EMPLCODE.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.info").value(DEFAULT_INFO.toString()))
            .andExpect(jsonPath("$.items").value(DEFAULT_ITEMS.toString()));
    }

    @Test
    public void getNonExistingHistory() throws Exception {
        // Get the history
        restHistoryMockMvc.perform(get("/api/histories/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateHistory() throws Exception {
        // Initialize the database
        historyRepository.save(history);
        int databaseSizeBeforeUpdate = historyRepository.findAll().size();

        // Update the history
        History updatedHistory = new History();
        updatedHistory.setId(history.getId());
        updatedHistory.setClient(UPDATED_CLIENT);
        updatedHistory.setEmplcode(UPDATED_EMPLCODE);
        updatedHistory.setType(UPDATED_TYPE);
        updatedHistory.setInfo(UPDATED_INFO);
        updatedHistory.setItems(UPDATED_ITEMS);

        restHistoryMockMvc.perform(put("/api/histories")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedHistory)))
                .andExpect(status().isOk());

        // Validate the History in the database
        List<History> histories = historyRepository.findAll();
        assertThat(histories).hasSize(databaseSizeBeforeUpdate);
        History testHistory = histories.get(histories.size() - 1);
        assertThat(testHistory.getClient()).isEqualTo(UPDATED_CLIENT);
        assertThat(testHistory.getEmplcode()).isEqualTo(UPDATED_EMPLCODE);
        assertThat(testHistory.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testHistory.getInfo()).isEqualTo(UPDATED_INFO);
        assertThat(testHistory.getItems()).isEqualTo(UPDATED_ITEMS);
    }

    @Test
    public void deleteHistory() throws Exception {
        // Initialize the database
        historyRepository.save(history);
        int databaseSizeBeforeDelete = historyRepository.findAll().size();

        // Get the history
        restHistoryMockMvc.perform(delete("/api/histories/{id}", history.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<History> histories = historyRepository.findAll();
        assertThat(histories).hasSize(databaseSizeBeforeDelete - 1);
    }
}
