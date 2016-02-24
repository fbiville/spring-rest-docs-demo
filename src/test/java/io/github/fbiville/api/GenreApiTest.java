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
public class GenreApiTest {

    @Autowired WebApplicationContext webApplicationContext;
    MockMvc client;

    @Before
    public void setUp() throws Exception {
        client = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void finds_all_genres() throws Exception {
        // tag::_1_All_Genres[]
        String response = client.perform(get("/genres"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(response)
                .containsOnlyOnce("\"label\" : \"Epic\"")
                .containsOnlyOnce("\"label\" : \"Lame\"")
                .containsOnlyOnce("\"label\" : \"Super epic\"");
        // end::_1_All_Genres[]
    }

    @Test
    public void finds_all_genres_per_page() throws Exception {
        // tag::_2_All_Genres_Per_Page[]
        String response = client.perform(get("/genres?page=1&size=1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(response)
                .containsOnlyOnce("\"label\" : \"Lame\"")
                .containsOnlyOnce("\"totalPages\" : 3")
                .containsOnlyOnce("\"number\" : 1");
        // end::_2_All_Genres_Per_Page[]
    }
}
