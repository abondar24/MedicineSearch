package org.abondar.industrial.medsearch.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.abondar.industrial.medsearch.service.JsonUtil.EMPTY_RESULT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SearchServiceTest {

  private static SearchService service;

  @BeforeAll
  public static void init() {
    String testDataset = "testDataset.json";
    service = new SearchServiceImpl(testDataset);
  }

  @Test
  public void searchByNameTest() {
    var name = "Emsam";
    service.readData();

    var res = service.searchByName(name);
    var id = "cdf1dcda-19cf-4cbc-ae9c-75d7ae4088ae";

    res.subscribe(ok -> assertTrue(ok.contains(id)));
  }

  @Test
  public void searchByNameNoDocTest() {
    var res = service.searchByName("test");
    res.subscribe(ok -> assertEquals(EMPTY_RESULT, ok));
  }

  @Test
  public void searchByDiseaseTest() {
    var disease = "bladder disease";
    service.readData();

    var res = service.searchByDisease(disease);
    var id1 = "b52d7619-da1f-4d63-805d-1d124fe53df4";
    var id2 = "58b565e6-32a9-45f6-adef-2e24b9b5582e";
    res.subscribe(
        ok -> {
          assertTrue(ok.contains(id1));
          assertTrue(ok.contains(id2));
        });
  }
}
