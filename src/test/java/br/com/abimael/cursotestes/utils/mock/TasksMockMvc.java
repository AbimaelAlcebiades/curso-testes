package br.com.abimael.cursotestes.utils.mock;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class TasksMockMvc {

  private final MockMvc mockMvc;

  public TasksMockMvc(MockMvc mockMvc) {
    this.mockMvc = mockMvc;
  }

  public ResultActions performPostJson(String urlTemplate, String content) throws Exception {
    return mockMvc.perform(
        post(urlTemplate).contentType(APPLICATION_JSON).content(content).accept(APPLICATION_JSON));
  }

  public ResultActions performGet(String urlTemplate) throws Exception {
    return mockMvc.perform(get(urlTemplate).contentType(APPLICATION_JSON));
  }

  public ResultActions performGetWithQueryParameters(
      String baseUrl, Map<String, Object> queryParameters) throws Exception {

    UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(baseUrl);

    queryParameters.forEach(
        (key, value) ->
            Optional.ofNullable(value).ifPresent(v -> uriComponentsBuilder.queryParam(key, v)));

    return performGet(uriComponentsBuilder.toUriString());
  }
}
