package io.github.fbiville.api;

import io.github.fbiville.Application;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.restdocs.RestDocumentation;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebIntegrationTest(randomPort = true)
@SpringApplicationConfiguration(Application.class)
public class GenreApiTest {

    @Rule public RestDocumentation restDocumentation = new RestDocumentation("target/snippets");
    @Autowired WebApplicationContext webApplicationContext;
    MockMvc client;

    @Before
    public void setUp() throws Exception {
        client = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(this.restDocumentation))
                .build();
    }

    @Test
    public void finds_all_genres() throws Exception {
        String response = client.perform(get("/genres"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andDo(document("all_genres"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(response)
                .containsOnlyOnce("\"label\" : \"Epic\"")
                .containsOnlyOnce("\"label\" : \"Lame\"")
                .containsOnlyOnce("\"label\" : \"Super epic\"");
    }

    @Test
    public void finds_all_genres_per_page() throws Exception {
        String response = client.perform(get("/genres?page=1&size=1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andDo(document("all_genres_per_page"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(response)
                .containsOnlyOnce("\"label\" : \"Lame\"")
                .containsOnlyOnce("\"totalPages\" : 3")
                .containsOnlyOnce("\"number\" : 1");
    }
}
