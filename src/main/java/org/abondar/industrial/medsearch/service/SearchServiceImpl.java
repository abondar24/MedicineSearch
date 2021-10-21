package org.abondar.industrial.medsearch.service;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.InvalidJsonException;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

import static org.abondar.industrial.medsearch.service.JsonUtil.DISEASE_SEARCH_QUERY;
import static org.abondar.industrial.medsearch.service.JsonUtil.EMPTY_RESULT;
import static org.abondar.industrial.medsearch.service.JsonUtil.NAME_SEARCH_QUERY;

public class SearchServiceImpl implements SearchService {

  private static final Logger logger = LoggerFactory.getLogger(SearchServiceImpl.class);

  private Object document;

  private final String fileName;

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
  public String searchByName(String medicineName) {
    if (isJsonParsed()) {
      var query = String.format(NAME_SEARCH_QUERY, medicineName);
      var res =(JSONArray) JsonPath.read(document, query);
      return res.toJSONString();
    } else {
      return EMPTY_RESULT;
    }
  }

  @Override
  public String searchByDisease(String diseaseName) {
      if (isJsonParsed()) {
          var query = String.format(DISEASE_SEARCH_QUERY, diseaseName);
          var res = (JSONArray) JsonPath.read(document, query);
          return res.toJSONString();
      } else {
          return EMPTY_RESULT;
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
