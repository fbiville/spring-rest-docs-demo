package io.github.fbiville.api;

import io.github.fbiville.Application;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebIntegrationTest(randomPort = true)
@SpringApplicationConfiguration(Application.class)
public class LameVidApiTest {

    @Autowired WebApplicationContext webApplicationContext;
    MockMvc client;

    @Before
    public void setUp() throws Exception {
        client = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void finds_all_vids() throws Exception {
        String response = client.perform(get("/lame-vids"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(response)
                .containsOnlyOnce("\"title\" : \"The Final Countdown\"")
                .containsOnlyOnce("\"title\" : \"Summertime Is Great\"")
                .containsOnlyOnce("\"title\" : \"J.-M. VdC\"")
                .containsOnlyOnce("\"title\" : \"Joli Dauphin\"")
                .containsOnlyOnce("\"title\" : \"I Get Around\"");
    }

    @Test
    public void finds_all_vids_per_page() throws Exception {
        String response = client.perform(get("/lame-vids?page=1&size=1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(response)
                .containsOnlyOnce("\"title\" : \"Summertime Is Great\"");
    }

    @Test
    public void finds_all_vids_by_matching_genre_label() throws Exception {
        String response = client.perform(get("/lame-vids/search/genre?genre=epic&sort=asc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(response)
                .containsSequence(
                    "\"title\" : \"The Final Countdown\"",
                    "\"title\" : \"J.-M. VdC\""
                );
    }

    @Test
    public void finds_all_vids_by_title() throws Exception {
        String response = client.perform(get("/lame-vids/search/title?title=The Final Countdown"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(response)
                .containsSequence(
                        "\"title\" : \"The Final Countdown\""
                );
    }
}
