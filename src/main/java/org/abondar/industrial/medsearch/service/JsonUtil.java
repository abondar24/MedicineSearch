package org.abondar.industrial.medsearch.service;

public class JsonUtil {

    public static final String DATASET_FILE = "dataset.json";

    public static final String NAME_SEARCH_QUERY = "$.drugs[?(@.name == '%s')]";

    public static final String DISEASE_SEARCH_QUERY = "$.drugs[?('%s' in @.diseases)]";


    private JsonUtil(){}
}
