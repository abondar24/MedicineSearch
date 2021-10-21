# Medicine search

Small API making medicine search in dataset.

## Access
```yaml
API ROOT: http://localhost:8000/api/v1

  - Find medicine by name or disease /search/:term 
         Response: 
              200 - List of found medicines,
              404 - Empty result
              500 - server error.
```

## Build and run

```yaml
./gradlew clean build

java -jar build/libs/MedicineSearch-1.0-SNAPSHOT-all.jar 
```
## Assumptions

- Drugs don't have disease name and vice versa
