package org.abondar.industrial.medsearch.service;

import io.reactivex.Maybe;

public interface SearchService {

    void readData();

    Maybe<String> searchByName(String medicineName);

    Maybe<String> searchByDisease(String diseaseName);
}
