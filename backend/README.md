## StudentTaskChecker szerver

### Rendszerkövetelmények
- OpenJDK 11
- Maven
- Docker (Linux és Windows is lehet, Windowson WSL2-es megoldás ajánlott)
- Docker Compose

### Fontos parancsok

1. `mvn clean install`
   - összerakja a jar fájlokat amiket majd a konténerek futtatnak.
   - kell az alábbiakhoz.

2. `docker-compose up -d`
   - elindul az alkalmazás, a docker felépíti a konténereket és imageket ha még nem léteznek.
   - nem frissülnek a konténerek a hatására ha már léteznek, csak elindulnak.
   - -d => detached, nem tolja ki a konzolra az összes konténer logját.
    
3. `docker-compose down` 
    - leállítja a konténereket

4. `docker-compose build --no-cache --parallel` 
    - Felépíti az összes konténert, jó akkor ha szeretnénk az egészet teljesen újraépíteni mert például van valami változás a forráskódban.
    - --no-cache => nem használja a cachelt fájlokat építéskor, tehát mindig aktuális de lassabb.
    - --parallel => több szálon épít, gyorsabb.

5. `docker-compose build --no-cache --parallel ${szerviz-név}` 
    - ugyanaz mint a felette lévő, csak meg lehet adni egy konténert a `docker-compose.yml` fájlból és csak azt építi újra.

### Futattás
Az *1.* és *2.* parancs elég a futtatáshoz, a *4.* és *5.* akkor szükséges ha változásokat szeretnénk eszközölni és látni.
Eltart pár percig míg felfut az egész, géptől függően. 
A folyamatot a Docker Desktop GUI-ban lehet nyomonkövetni, ott lehet látni az egyes konténerek logjait.
Pár exception lehetséges, nem kell megijedni, ha nem hal meg teljesen és exitál akkor majd megoldja. 
Ilyenkor regisztrálni akar a discovery szervizbe és ha nem megy neki akkor a kitolja a stacktracet a logra, de később újra próbálja.
Továbbá ha a discovery szerviz felfutott akkor a `localhost:8761`-en lesz látható az egyes szervizek állapotáról egy táblázat.
Ha minden UP állapotú akkor a [frontend](https://github.com/me-kry-student-task-checker/main/blob/master/frontend/README.md) is indulhat.

#### Előre felvitt bejelentkezési adatok: 
- *e-mail:* bob@ross.com
- *password:* happy123