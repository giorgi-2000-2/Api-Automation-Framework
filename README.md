# PlatziStore API Automation Framework

## პროექტის აღწერა
API ტესტირების ავტომატიზაციის ფრეიმვორქი, რომელიც დაწერილია Java-ზე **RestAssured**-ისა და **TestNG**-ის გამოყენებით. ფრეიმვორქი აგებულია ფენებად დაყოფილ (Layered) არქიტექტურაზე, მხარს უჭერს პარალელურ გაშვებას (Thread-safe) და გენერირებს დეტალურ HTML რეპორტებს **ExtentReports**-ის საშუალებით.

## ტექნოლოგიური სტეკი
* **ენა:** Java 21
* **ავტომატიზაციის ბიბლიოთეკა:** RestAssured
* **ტესტ-რანერი:** TestNG
* **რეპორტინგი:** ExtentReports 5
* **მონაცემთა გენერაცია:** JavaFaker
* **დამხმარე ხელსაწყოები:** Lombok, Maven

## არქიტექტურა და დიზაინ პატერნები
პროექტში გამოყენებულია შემდეგი მიდგომები და პატერნები:
* **Builder Pattern:** DTO (Data Transfer Object) კლასების მარტივად შესაქმნელად.
* **Data Factory Pattern:** სატესტო მონაცემების დინამიური და ცენტრალიზებული გენერაციისთვის (JavaFaker-ის გამოყენებით).
* **Step Object Pattern:** ბიზნეს ლოგიკისა და API გამოძახებების ტესტებისგან გასამიჯნად.
* **Fluent Assertions:** კითხვადი და ჯაჭვური (Chained) ვალიდაციებისთვის.
* **ThreadLocal:** უზრუნველყოფს ტესტების უსაფრთხო პარალელურ გაშვებას (Thread-Safety), რაც აუცილებელია CI/CD გარემოში სტაბილურობისთვის.

## პროექტის სტრუქტურა
```text
src/main/java/org/example/
├── ApiClient/          # REST API მოთხოვნების (GET, POST, PUT, DELETE) კლასები
├── ApiService/         # კონფიგურაცია, ბაზური Request სპეციფიკაციები და ფილტრები
├── AssertionManager/   # Response-ების ვალიდაციები (Fluent Assertions & JSON/Schema Validators)
├── DataFactories/      # სატესტო მონაცემების (Payload-ების) გენერატორები
├── DTOs/               # Request/Response მოდელები (POJO კლასები)
├── Managers/           # Object, Factory და Assertion მენეჯერები ცენტრალიზებული მართვისთვის
├── Steps/              # ბიზნეს სცენარები და API-ის დაკავშირება Assert-ებთან
└── Utils/              # დამხმარე კლასები (ConfigReader, ExtentReportManager, TestListeners)
```

## ინსტალაცია და ლოკალურად გაშვება

### წინაპირობები (Prerequisites)
* **Java 21** ან უფრო ახალი
* **Maven** (Apache Maven)

### პროექტის მომზადება
1. დააკოპირეთ (Clone) პროექტი:
   ```bash
   git clone <repository_url>
   ```
2. გადადით პროექტის დირექტორიაში და ჩამოტვირთეთ დეპენდენსები:
   ```bash
   mvn clean install -DskipTests
   ```

### ტესტების გაშვება
ტესტების გასაშვებად ტერმინალში გამოიყენეთ შემდეგი ბრძანება:
```bash
mvn clean test
```
*შენიშვნა: პროექტი კონფიგურირებულია პარალელური გაშვებისთვის. `testng.xml` ფაილში მითითებულია `parallel="methods" thread-count="8"`.*

## რეპორტინგი (Reports)
ტესტების დასრულების შემდეგ, ავტომატურად შეიქმნება დეტალური Extent Report.
რეპორტის სანახავად გახსენით ფაილი თქვენს ბრაუზერში:
`report/extentReport.html`

## CI/CD ინტეგრაცია
პროექტს მოყვება GitHub Actions-ის კონფიგურაცია (`.github/workflows`), რომელიც ყოველ Push/Pull Request-ზე ავტომატურად:
1. აყენებს Java 21-ს.
2. უშვებს ტესტებს `mvn clean test` ბრძანებით.
3. ინახავს Extent Report-ს როგორც Artifact-ს შემდგომი ანალიზისთვის.
4. (სურვილისამებრ) ატარებს კოდის ანალიზს Qodana-ს საშუალებით.