package org.abondar.industrial.medsearch.service;

import io.reactivex.Single;

public interface SearchService {

    void readData();

    Single<String> searchByName(String medicineName);

    Single<String> searchByDisease(String diseaseName);
}
