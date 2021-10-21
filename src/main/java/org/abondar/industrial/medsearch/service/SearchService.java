package org.abondar.industrial.medsearch.service;

public interface SearchService {

    void readData();

    String searchByName(String medicineName);

    String searchByDisease(String diseaseName);
}
