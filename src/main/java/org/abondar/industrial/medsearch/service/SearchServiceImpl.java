package org.abondar.industrial.medsearch.service;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.InvalidJsonException;
import com.jayway.jsonpath.JsonPath;
import io.reactivex.Maybe;
import net.minidev.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

import static org.abondar.industrial.medsearch.service.JsonUtil.DISEASE_SEARCH_QUERY;
import static org.abondar.industrial.medsearch.service.JsonUtil.NAME_SEARCH_QUERY;

public class SearchServiceImpl implements SearchService {

  private static final Logger logger = LoggerFactory.getLogger(SearchServiceImpl.class);
  private final String fileName;
  private Object document;

  public SearchServiceImpl(String fileName) {
    this.fileName = fileName;
  }

  public void readData() {
    try {
      var is = getClass().getClassLoader().getResourceAsStream(fileName);
      document =
          Configuration.defaultConfiguration()
              .jsonProvider()
              .parse(is, StandardCharsets.UTF_8.name());
    } catch (InvalidJsonException ex) {
      logger.error("Error reading file {}", fileName);
    }
  }

  @Override
  public Maybe<String> searchByName(String medicineName) {
    var query = String.format(NAME_SEARCH_QUERY, medicineName);
    return doSearch(query);
  }

  @Override
  public Maybe<String> searchByDisease(String diseaseName) {
    var query = String.format(DISEASE_SEARCH_QUERY, diseaseName);
    return doSearch(query);
  }

  private Maybe<String> doSearch(String query) {
    if (isJsonParsed()) {
      var res = (JSONArray) JsonPath.read(document, query);

      if (res.size() == 0) {
        return Maybe.empty();
      }
      return Maybe.just(res.toJSONString());
    } else {
      return Maybe.error(new RuntimeException("JSON not parsed"));
    }
  }

  private boolean isJsonParsed() {
    if (document == null) {
      logger.error("Json file {} is not parsed", fileName);
      return false;
    }

    return true;
  }
}
