package com.pyrenty.akl.web.rest;

import com.pyrenty.akl.Application;
import com.pyrenty.akl.domain.LocalizedText;
import com.pyrenty.akl.repository.LocalizedTextRepository;
import com.pyrenty.akl.web.rest.dto.LocalizedTextDTO;
import com.pyrenty.akl.web.rest.mapper.LocalizedTextMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.pyrenty.akl.domain.enumeration.Language;

/**
 * Test class for the LocalizedTextResource REST controller.
 *
 * @see LocalizedTextResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class LocalizedTextResourceTest {


    private static final Language DEFAULT_LANGUAGE = Language.fi;
    private static final Language UPDATED_LANGUAGE = Language.en;
    private static final String DEFAULT_TEXT = "SAMPLE_TEXT";
    private static final String UPDATED_TEXT = "UPDATED_TEXT";

    @Inject
    private LocalizedTextRepository localizedTextRepository;

    @Inject
    private LocalizedTextMapper localizedTextMapper;


    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    private MockMvc restLocalizedTextMockMvc;

    private LocalizedText localizedText;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LocalizedTextResource localizedTextResource = new LocalizedTextResource();
        ReflectionTestUtils.setField(localizedTextResource, "localizedTextRepository", localizedTextRepository);
        ReflectionTestUtils.setField(localizedTextResource, "localizedTextMapper", localizedTextMapper);
        this.restLocalizedTextMockMvc = MockMvcBuilders.standaloneSetup(localizedTextResource).setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        localizedText = new LocalizedText();
        localizedText.setLanguage(DEFAULT_LANGUAGE);
        localizedText.setText(DEFAULT_TEXT);
    }

    @Test
    @Transactional
    public void createLocalizedText() throws Exception {
        int databaseSizeBeforeCreate = localizedTextRepository.findAll().size();

        // Create the LocalizedText
        LocalizedTextDTO localizedTextDTO = localizedTextMapper.localizedTextToLocalizedTextDTO(localizedText);

        restLocalizedTextMockMvc.perform(post("/api/localizedTexts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(localizedTextDTO)))
                .andExpect(status().isCreated());

        // Validate the LocalizedText in the database
        List<LocalizedText> localizedTexts = localizedTextRepository.findAll();
        assertThat(localizedTexts).hasSize(databaseSizeBeforeCreate + 1);
        LocalizedText testLocalizedText = localizedTexts.get(localizedTexts.size() - 1);
        assertThat(testLocalizedText.getLanguage()).isEqualTo(DEFAULT_LANGUAGE);
        assertThat(testLocalizedText.getText()).isEqualTo(DEFAULT_TEXT);
    }

    @Test
    @Transactional
    public void getAllLocalizedTexts() throws Exception {
        // Initialize the database
        localizedTextRepository.saveAndFlush(localizedText);

        // Get all the localizedTexts
        restLocalizedTextMockMvc.perform(get("/api/localizedTexts"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(localizedText.getId().intValue())))
                .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE.toString())))
                .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT)));
    }

    @Test
    @Transactional
    public void getLocalizedText() throws Exception {
        // Initialize the database
        localizedTextRepository.saveAndFlush(localizedText);

        // Get the localizedText
        restLocalizedTextMockMvc.perform(get("/api/localizedTexts/{id}", localizedText.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(localizedText.getId().intValue()))
            .andExpect(jsonPath("$.language").value(DEFAULT_LANGUAGE.toString()))
            .andExpect(jsonPath("$.text").value(DEFAULT_TEXT));
    }

    @Test
    @Transactional
    public void getNonExistingLocalizedText() throws Exception {
        // Get the localizedText
        restLocalizedTextMockMvc.perform(get("/api/localizedTexts/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLocalizedText() throws Exception {
        // Initialize the database
        localizedTextRepository.saveAndFlush(localizedText);

		int databaseSizeBeforeUpdate = localizedTextRepository.findAll().size();

        // Update the localizedText
        localizedText.setLanguage(UPDATED_LANGUAGE);
        localizedText.setText(UPDATED_TEXT);

        LocalizedTextDTO localizedTextDTO = localizedTextMapper.localizedTextToLocalizedTextDTO(localizedText);

        restLocalizedTextMockMvc.perform(put("/api/localizedTexts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(localizedTextDTO)))
                .andExpect(status().isOk());

        // Validate the LocalizedText in the database
        List<LocalizedText> localizedTexts = localizedTextRepository.findAll();
        assertThat(localizedTexts).hasSize(databaseSizeBeforeUpdate);
        LocalizedText testLocalizedText = localizedTexts.get(localizedTexts.size() - 1);
        assertThat(testLocalizedText.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
        assertThat(testLocalizedText.getText()).isEqualTo(UPDATED_TEXT);
    }

    @Test
    @Transactional
    public void deleteLocalizedText() throws Exception {
        // Initialize the database
        localizedTextRepository.saveAndFlush(localizedText);

		int databaseSizeBeforeDelete = localizedTextRepository.findAll().size();

        // Get the localizedText
        restLocalizedTextMockMvc.perform(delete("/api/localizedTexts/{id}", localizedText.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<LocalizedText> localizedTexts = localizedTextRepository.findAll();
        assertThat(localizedTexts).hasSize(databaseSizeBeforeDelete - 1);
    }
}
