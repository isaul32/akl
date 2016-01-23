package com.pyrenty.akl.web.rest;

import com.pyrenty.akl.Application;
import com.pyrenty.akl.domain.Text;
import com.pyrenty.akl.repository.TextRepository;
import com.pyrenty.akl.repository.search.TextSearchRepository;

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


/**
 * Test class for the TextResource REST controller.
 *
 * @see TextResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class TextResourceTest {


    @Inject
    private TextRepository textRepository;

    @Inject
    private TextSearchRepository textSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    private MockMvc restTextMockMvc;

    private Text text;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TextResource textResource = new TextResource();
        ReflectionTestUtils.setField(textResource, "textRepository", textRepository);
        ReflectionTestUtils.setField(textResource, "textSearchRepository", textSearchRepository);
        this.restTextMockMvc = MockMvcBuilders.standaloneSetup(textResource).setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        text = new Text();
    }

    @Test
    @Transactional
    public void createText() throws Exception {
        int databaseSizeBeforeCreate = textRepository.findAll().size();

        // Create the Text

        restTextMockMvc.perform(post("/api/texts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(text)))
                .andExpect(status().isCreated());

        // Validate the Text in the database
        List<Text> texts = textRepository.findAll();
        assertThat(texts).hasSize(databaseSizeBeforeCreate + 1);
        Text testText = texts.get(texts.size() - 1);
    }

    @Test
    @Transactional
    public void getAllTexts() throws Exception {
        // Initialize the database
        textRepository.saveAndFlush(text);

        // Get all the texts
        restTextMockMvc.perform(get("/api/texts"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(text.getId().intValue())));
    }

    @Test
    @Transactional
    public void getText() throws Exception {
        // Initialize the database
        textRepository.saveAndFlush(text);

        // Get the text
        restTextMockMvc.perform(get("/api/texts/{id}", text.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(text.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingText() throws Exception {
        // Get the text
        restTextMockMvc.perform(get("/api/texts/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateText() throws Exception {
        // Initialize the database
        textRepository.saveAndFlush(text);

		int databaseSizeBeforeUpdate = textRepository.findAll().size();

        // Update the text
        

        restTextMockMvc.perform(put("/api/texts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(text)))
                .andExpect(status().isOk());

        // Validate the Text in the database
        List<Text> texts = textRepository.findAll();
        assertThat(texts).hasSize(databaseSizeBeforeUpdate);
        Text testText = texts.get(texts.size() - 1);
    }

    @Test
    @Transactional
    public void deleteText() throws Exception {
        // Initialize the database
        textRepository.saveAndFlush(text);

		int databaseSizeBeforeDelete = textRepository.findAll().size();

        // Get the text
        restTextMockMvc.perform(delete("/api/texts/{id}", text.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Text> texts = textRepository.findAll();
        assertThat(texts).hasSize(databaseSizeBeforeDelete - 1);
    }
}
