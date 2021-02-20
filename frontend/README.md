## StudentTaskChecker webes kliens

### Rendszerkövetelmények
- NodeJs 15.7.0 (ezt használtam, de lehet régebbi is jó)
- npm 7.0.11

### Fontos parancsok
1. `npm install`
    - lehúzza a függőségeket
2. `npm run serve`
    - elindít egy dev szervert a `localhost:3000`
    - a kéréseket proxyzni fogja a szervernek (egészen pontosan a `localhost:8060`-ra)

### Futattás
Akkor fog csak jól működni ha teljesen felfutott a [backenden](https://github.com/me-kry-student-task-checker/main/blob/master/backend/README.md) az összes szerviz.
Sorrendben az *1.* és *2.* parancs szükséges, majd a `localhost:3000`-en lesz elérhető a weboldal.
