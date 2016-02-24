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
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebIntegrationTest(randomPort = true)
@SpringApplicationConfiguration(Application.class)
public class LameVidApiTest {

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
    public void finds_all_vids() throws Exception {
        String response = client.perform(get("/lame-vids"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andDo(document("all_vids", responseFields(
                        fieldWithPath("_links").ignored(),
                        fieldWithPath("page").ignored(),
                        fieldWithPath("_embedded.lame-vids[].title").description("Lame vid title (duh!)"),
                        fieldWithPath("_embedded.lame-vids[].uri").description("Lame vid URI (duh!)"),
                        fieldWithPath("_embedded").ignored())))
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
                .andDo(document("all_vids_per_page"))
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
                .andDo(document("vids_by_genre"))
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
                .andDo(document("vids_by_title"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(response)
                .containsSequence(
                        "\"title\" : \"The Final Countdown\""
                );
    }
}
