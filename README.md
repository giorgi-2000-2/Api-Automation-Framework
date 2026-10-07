# Platzi Fake Store API Test Automation Framework

REST API-ის ავტომატიზებული სატესტო ფრეიმვორკი Platzi Fake Store API-სთვის (Categories, Users, Auth და Products რესურსებისთვის). აგებულია **Java 21 + REST Assured + TestNG + Google Guice**-ზე და ფარავს CRUD ციკლს — პოზიტიური, ნეგატიური და E2E სცენარებით.

# არქიტექტურული გადაწყვეტილებები
პირველი არქიტექტურული ცვლილება იყო როცა Guice ზე გადავიდა მთელი პრეოქტი.

მანამდე იყო **lazy initialization** ხელით გაწერილი **ObjectManager** კლასი რომელიც ამენეჯებდა ყველა ობიექტს მთელი პროცესის განმავლობაში.
ობიექტების სასიცოცხლო ციკლი იმართებოდა **BaseApiTest**-დან

**Guice** როცა შემოვიდა არქიტექტურა გადაეწყო მასზე. **Singleton** ავტომატურად აქვს მაგრამ არ ქონდა კონკრეტული **scope** რომელიც დაიწერა.

**TestScope** უზრუნველყოფს, რომ ობიექტი ცოცხლობდეს მხოლოდ 1 ტესტ–მეთოდის განმავლობაში.

**report** ის სისტემა ამჟამად არის ორი allure და extent report. კონფიგურაციებიდან იმართება რეპორტინგი report.engine
ალურის რეპორტის სანახავად საჭიროა ბრძანება ტერმინალში allure serve allure-results
პროექტი არის სასწავლი და სადემონსტრაციო შესაბამისად არ არის მორგებული მხოლოდ ერთ რეპორტზე...

**metric** დამატებულია მეტრიკა რომელიც აკეთებს response time ის აღრიცხვას. როგორც 1 გაშვების ასევე აგროვებს მონაცემებს
და რამდენიმე გაშვების საშუალო response time წერს. ეს გროვდება target ში. როდესაც mvn clean ბრძანება გაეშვება იშლება რეზულტატი ავტომატურად.

ნეგატიური ტესტების ზრდასთან ერთად საჭირო იყო მისი სუფთად დაწერა. ვინაიდან 1 ნეგატიური ტესტის დამატებისთვის კლასში მიწევდა ახალი მეთოდების დამატება ეს
ეწინააღმდეგებოდა **solid – open/closed** პრინციპს. შესაბამისად მომიწია ალტერნატივის მოძიება და dataProvider ზე გადასვლა.
 
მონაცემების გასუფთავების **cleanup** მექანიზმი.
LIFO (Last In, First Out) მუშაობს ანუ ეს ნიშნავს რომ რესურსები იხურება ან იწმინდება იმ თანმიმდევრობის შებრუნებით,
რა თანმიმდევრობითაც ისინი შეიქმნა ან გამოიყო.

**assert** მექანიზმი. სტატუკოდი მოწმდება მხოლოდ hard asssert – ით ვინაიდან სტატუსკოდის შეუსაბამობის დროს მნიშვნელობა აღარ ექნება სხვა 
შემოწმებებს.

# არქიტექტურის ხარვეზები
* jackson ბიბლიოთეკა არ არის მოქნილი და თუ api–იმ უკვე არსებული სქემებისგან განსხვავებული სქემა დააბრუნა პროცესი მკაცრად ჩერდება. (ჯერჯერობით არ ვასწორებ, რადგან პროექტი ამ ეტაპზე ეცნობა სქემებს)
* პროექტში არის მეტრიკის გამოყენების მცდელობა მაგრამ ვინაიდან ეს სასწავლო პროექტია არ აქვს ღირებულება.
* მეტრიკაში არ მუშაობს flaky rate. ასევე არ არის დაყენებული retry ლოგიკა და არ არის გამოყენებული testng ის retryanalyzer.
* რეპორტი არ ინახება CI ში.
* unit ტესტები არ არის დაწერილი 
* პროექტის არქიტექტურა არ არის მორგებული რომელიმე რეპორტზე შესაბამისად არ გამოიყენება რომელიმე რეპორტის შიდა მეთოდები. (მაგ. allure).


# ტესტირების ხარვეზები
* პროექტი ორიენტირებულია გამართული არქიტექტურის აწყობაზე და არა ტესტების, ბიზნეს ვალიდაციის სიღრმის დაფარვაზე. 
შესაბამისად ტესტები არ ფარავვს მთლიან ბიზნეს სცენარებს.
* testGetCategoryLimit-ის ზუსტი size()==limit შემოწმება მთლიანად დამოკიდებულია სხვა მომხმარებლების მიერ შექმნილ მონაცმებზე.
* ასევე ბევრი ნეგატიური ტესტი არ დაიწერა რადგან ბევრი ბაგი და არასწორი შედეგი ბრუნდებოდა. 
* რადგან ეს პროექტი კონცენტრირებულია არქიტექტურაზე ბაგები შევფუთე მოსალოდნელ შედეგად.



# API-ის პრობლემები

* ზოგიერთ Negative Test Case-ში API null მნიშვნელობის მიღებისას აბრუნებს 500 Internal Server Error-ს.

* ეს მიჩნეულია API-ის პრობლემად, რადგან არავალიდური client input-ის შემთხვევაში მოსალოდნელი behavior ჩვეულებრივ არის 
4xx სტატუსკოდი, მაგალითად 400 Bad Request, და არა 500 Internal Server Error.

* ტესტებში 500 სტატუსკოდი განზრახ არის მითითებული მოსალოდნელ შედეგად, 
რათა დაფიქსირდეს და დემონსტრირდეს API-ის ამჟამინდელი რეალური behavior.

შემთხვევები, რომლებიც აბრუნებს 500-ს:
* POST /users — name = null → 500
* POST /users — email = null, name = null → 500
* POST /users — ყველა სავალდებულო ველი null → 500
* POST /products — title = null → 500
* PUT /products/{id} — images = null → 500
* POST /categories — name = null → 500
* POST /categories — name = null, image = null → 500

აღნიშნული შემთხვევები გადამოწმებულია Postman-ში.


##  ძირითადი მახასიათებლები

* **ავტომატური Cleanup:** შექმნილი ტესტ-მონაცემები იშლება ავტომატურად(LIFO პრინციპით).
* **პარალელური გაშვება:** თითოეულ ტესტს აქვს საკუთარი იზოლირებული DI კონტექსტი (Custom `TestScope`), რაც გამორიცხავს shared state-ის პრობლემებს 10+ თრედზე გაშვებისას.
* **მრავალდონიანი ვალიდაცია:** ყოველი პასუხი მოწმდება 4 დონეზე: HTTP სტატუსი → Content-Type → JSON სქემა → პასუხის დრო.
* **დეკლარატიული Setup:** ტესტის დაწყებამდე მონაცემების შექმნა ხდება მარტივი ანოტაციებით (`@RequiresCategory`, `@RequiresProduct`).
* **დეტალური რეპორტინგი:** Extent Reports იწერს ყველა HTTP request/response-ს და ნაბიჯს.

##  ტექნოლოგიური სტეკი

* **ენა:** Java 21
* **Build Tool:** Maven 3.8+
* **API კლიენტი:** REST Assured 5.5.6
* **ტესტ-რანერი:** TestNG 7.11.0
* **DI & არქიტექტურა:** Google Guice 7.0.0
* **სხვა:** Lombok, Datafaker, JSON Schema Validator, Extent Reports

## პროექტის სტრუქტურა

### `src/main/java/ge/gmikeladze/platzi` — ფრეიმვორკი

**`annotations`**
- `RequiresCategory` — ტესტის წინ იქმნება კატეგორია
- `RequiresProduct` — ტესტის წინ იქმნება კატეგორია და მასში პროდუქტი
- `TestScoped` — scope ანოტაცია: ობიექტი ცოცხლობს ერთი ტესტის განმავლობაში

**`apiclient`**
- `ApiEndpoint` — enum ყველა endpoint-ის მისამართით
- `GenericClient` — მოთხოვნის გაგზავნა არჩეულ endpoint-ზე (create, get, update, delete)
- `Pagination` — `limit` და `offset` პარამეტრები

**`apiservice`**
- `ApiRequest` — HTTP მოთხოვნები REST Assured-ით (POST, GET, PUT, DELETE)
- `HttpStatusCode` — enum HTTP სტატუს კოდებით

**`assertions`**
- `ResponseValidator` — პასუხის ტექნიკური ვალიდაცია (დრო, სტატუსი, Content-Type, სქემა) და DTO-ში გარდაქმნა
- `SchemaMapping` — enum: რომელ DTO-ს რომელი JSON სქემა შეესაბამება

**`assertions/validator`**
- `Validator` — ვალიდატორების საბაზო კლასი, შედეგს წერს რეპორტში
- `StatusValidator` — სტატუს კოდის შემოწმება (hard assert)
- `ContentTypeValidator` — Content-Type-ის შემოწმება (soft assert)
- `SchemaValidator` — JSON სქემასთან შესაბამისობა (soft assert)
- `ResponseTimeValidator` — პასუხის დროის ლიმიტი (soft assert)

**`assertions/assertsbusiness`**
- `IBaseAssert` — fluent assert-ების ინტერფეისი
- `BaseAssert` — საერთო შემოწმებები: ველის მნიშვნელობა, სიის ზომა, წაშლის პასუხი
- `ResponseCategoryAssert` — კატეგორიის პასუხის შემოწმება
- `ResponseProductAssert` — პროდუქტის პასუხის შემოწმება
- `ResponseUserAssert` — მომხმარებლის პასუხის შემოწმება
- `ResponseAuthAssert` — access და refresh token-ების შემოწმება
- `ResponseErrorAssert` — შეცდომის ტექსტის შემოწმება

**`cleanup`**
- `CleanupRegistry` — ტესტში შექმნილი მონაცემების აღრიცხვა და წაშლა LIFO რიგით
- `ResourceKey` — რესურსის იდენტიფიკატორი (ტიპი + id)

**`datafactories`**
- `RandomDataFactory` — უნიკალური და შემთხვევითი მნიშვნელობები (სახელი, email, პაროლი)
- `CategoryDataFactory` — კატეგორიის შექმნისა და განახლების მონაცემები
- `ProductDataFactory` — პროდუქტის შექმნისა და განახლების მონაცემები
- `UserDataFactory` — მომხმარებლის შექმნისა და განახლების მონაცემები
- `AuthDataFactory` — login-ის მონაცემები

**`datafactories/negative`**
- `NegativeCase` — ერთი ნეგატიური ქეისი: სახელი, payload, მოსალოდნელი სტატუსი და შეცდომის ტექსტი
- `CategoryNegativeData`, `ProductNegativeData`, `UserNegativeData`, `AuthNegativeData` — DataProvider-ები არავალიდური მონაცემებით

**`di`**
- `FrameworkModule` — Guice-ის მოდული: binding-ები, რეპორტერის არჩევა, RequestSpecification
- `TestScope` — custom scope: თითო ტესტს საკუთარი ობიექტები აქვს
- `TestContext` — ერთი ტესტის მდგომარეობა (შექმნილი კატეგორია, პროდუქტი, token-ები)
- `SoftAssertListener` — ტესტის ბოლოს აჯამებს soft assert-ებს და შეცდომისას ტესტს აგდებს

**`dtos/request`**
- `CreateCategoryRequestDto`, `UpdateCategoryRequestDto` — კატეგორიის მოთხოვნის body
- `CreateProductRequestDto`, `UpdateProductRequestDto` — პროდუქტის მოთხოვნის body
- `CreateUserDto`, `UpdateUserDto` — მომხმარებლის მოთხოვნის body
- `LoginRequestDto`, `RefreshTokenRequestDto` — ავტორიზაციის მოთხოვნის body
- `GetCategoryLimitRequestDto` — კატეგორიების სიის `limit`
- `AuthTokensDto` — login-ისა და refresh-ის პასუხი (access და refresh token)

**`dtos/response`**
- `GetResponseCategoryDto`, `GetResponseProductDto`, `GetUserResponseDto` — რესურსების პასუხები
- `Identifiable` — ინტერფეისი `getId()` მეთოდით
- `UnauthorizedErrorDto` — 401 პასუხი

**`dtos/response/error`**
- `ApiError` — შეცდომის DTO-ების საერთო ინტერფეისი
- `BadRequestResponse` — 400: ჩანაწერი ვერ მოიძებნა
- `ValidationErrorDto` — 400: ვალიდაციის შეცდომები
- `PutBadRequestResponseDto` — 400 განახლებისას: ბაზის შეზღუდვის დარღვევა
- `InternalServerErrorDto` — 500 პასუხი

**`steps`**
- `BaseSteps` — ნაბიჯების საბაზო კლასი (რეპორტერი, ვალიდატორი, ნაბიჯის ლოგირება)
- `IResourceSteps` — CRUD ნაბიჯების ინტერფეისი
- `AbstractResourceSteps` — CRUD ნაბიჯების საერთო იმპლემენტაცია და შექმნილი რესურსის cleanup-ზე რეგისტრაცია
- `CategorySteps` — კატეგორიის ნაბიჯები (სია, slug-ით ძებნა, კატეგორიის პროდუქტები)
- `ProductSteps` — პროდუქტის ნაბიჯები
- `UserSteps` — მომხმარებლის ნაბიჯები
- `AuthSteps` — login, profile, refresh token
- `E2ESteps` — რამდენიმე რესურსის გაერთიანებული სცენარები

**`utils`**
- `LogFilter` — ყველა request-სა და response-ს წერს რეპორტში
- `TestListenerManager` — TestNG listener: ტესტის შედეგს წერს კონსოლსა და რეპორტში

**`utils/config`**
- `ConfigSource` — `.properties` ფაილის წაკითხვა classpath-იდან
- `PropertiesConfig` — კონფიგურაციის მნიშვნელობები `config.properties`-დან
- `IConfigForRequest`, `IConfigForData` — კონფიგის ინტერფეისები მოთხოვნებისა და ტესტ-მონაცემებისთვის

**`utils/reporter`**
- `ITestReporter` — რეპორტერის ინტერფეისი
- `IReportConfig` — რეპორტის კონფიგის ინტერფეისი
- `ReportEngine` — enum: `ALLURE` ან `EXTENT`
- `ReportStatus` — enum: PASS, FAIL, SKIP, INFO, WARNING
- `allure/AllureTestReporter` — Allure-ის იმპლემენტაცია
- `extent/ExtentTestReporter` — Extent Reports-ის იმპლემენტაცია

**`utils/metrics`**
- `MetricsFilter` — ზომავს თითო HTTP მოთხოვნის დროს
- `MetricsRegistry` — ინახავს ტესტების მთვლელებსა და endpoint-ების სტატისტიკას
- `SuiteMetricsListener` — ითვლის passed/failed/flaky ტესტებს და suite-ის ბოლოს უშვებს რეპორტის გენერაციას
- `MetricsReportService` — აგროვებს მონაცემებს და იძახებს გენერატორებს
- `PercentileCalculator` — პერცენტილის გამოთვლა (p50, p95, p99)
- `generator/MetricsJsonReportBuilder` — ქმნის `metrics.json`-ს
- `generator/MetricsHistoryStore` — გაშვებების ისტორია `history.json`-ში
- `generator/MetricsHtmlGenerator` — ქმნის `metrics.html` dashboard-ს

**`utils/retry`**
- `RetryAnalyzer` — ჩავარდნილ ტესტს თავიდან უშვებს (რაოდენობა: `-Dretry.count`, default 1)
- `RetryTransformer` — `RetryAnalyzer`-ს ყველა ტესტს ადებს

### `src/test/java/ge/gmikeladze/platzi` — ტესტები

- `BaseApiTest` — ყველა API ტესტის მშობელი კლასი: DI, setUp, tearDown, cleanup
- `testdata/TestDataPreparer` — ანოტაციების მიხედვით ქმნის ტესტის წინასწარ მონაცემებს
- `tests/CategoryTest`, `tests/ProductTest`, `tests/UserTest` — CRUD-ის პოზიტიური და ნეგატიური ტესტები
- `tests/AuthTest` — login, profile და refresh token-ის ტესტები
- `tests/e2e/E2ETest` — რამდენიმე რესურსის სცენარები
- `unit/CleanupRegistryTest` — `CleanupRegistry`-ის unit ტესტები
- `unit/FakeReporter` — რეპორტერის შემცვლელი unit ტესტებისთვის

### რესურსები

- `src/main/resources/config.properties` — base URL, ლიმიტები, რეპორტის engine, ტესტ-მონაცემები
- `src/main/resources/schemas/` — JSON სქემები პასუხების ვალიდაციისთვის
- `testexecution/` — TestNG suite-ები: smoke, positive, negative, regression, e2e


##  სწრაფი სტარტი

**წინაპირობა:** დაინსტალირებული JDK 21 და Maven.

```bash
# პროექტის გადმოწერა
git clone https://github.com/giorgi-2000-2/platzi-api-automation.git
cd platzi-api-automation

# ტესტების გაშვება (უშვებს Regression სუიტს)
mvn clean test
```
*შენიშვნა: IDE-ში პროექტის გახსნისას აუცილებლად ჩართეთ Annotation Processors (Lombok-ისთვის).*

##  ტესტების გაშვება

ფრეიმვორკში კონფიგურირებულია რამდენიმე XML სუიტა (Smoke, Positive, Negative, E2E). კონკრეტული სუიტის გასაშვებად გამოიყენეთ:

```bash
# Smoke ტესტები
mvn clean test -DsuiteXmlFile=testexecution/smoketesting.xml

# E2E სცენარები
mvn clean test -DsuiteXmlFile=testexecution/e2etesting.xml


```

## რეპორტინგი & CI/CD

* **რეპორტი:** ტესტების დასრულების შემდეგ ავტომატურად გენერირდება HTML რეპორტი: `report/extentReport.html`. გახსენით ნებისმიერ ბრაუზერში.
* **CI/CD:** პროექტში ინტეგრირებულია **GitHub Actions**. ტესტები ეშვება ავტომატურად (Push/PR), ხოლო არტიფაქტის სახით ინახება ტესტების რეპორტი. ასევე კონფიგურირებულია **Qodana** კოდის ხარისხის სტატიკური ანალიზისთვის.

## ავტორი
**Giorgi Mikeladze** — Test Automation Engineer
* GitHub: [@giorgi-2000-2](https://github.com/giorgi-2000-2)

---
*ტესტები ეშვება რეალურ საჯარო API-ზე, შესაბამისად შედეგები შესაძლოა დამოკიდებული იყოს სერვისის ხელმისაწვდომობაზე.*