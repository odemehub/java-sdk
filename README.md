# ödemehub Java SDK

ödemehub ödeme geçidini kendi uygulamanızdan kullanmak için hazırlanmış Java istemcisi. Kart çekmek, 3D ödeme başlatmak, müşteriyi ödeme sayfasına yollamak (sipariş, ödeme linki, abonelik), kart saklamak, iade ve iptal yapmak ve bir kartın taksit seçeneklerini sormak için gereken her şey burada.

İstemci her isteği takımınızın gizli anahtarıyla imzalar, gelen her yanıtın imzasını doğrular. Siz imza, başlık ya da JSON ayrıntılarıyla uğraşmazsınız. İstekler Java'nın kendi `HttpClient`'ıyla gider; tek bağımlılık JSON için Jackson'dır.

## Kurulum

Java 17 ve üzeri gerekir.

Maven:

```xml
<dependency>
    <groupId>com.odemehub</groupId>
    <artifactId>java-sdk</artifactId>
    <version>1.0.1</version>
</dependency>
```

Gradle:

```groovy
implementation 'com.odemehub:java-sdk:1.0.1'
```

## Yapılandırma

Üç bilgiye ihtiyacınız var. Hepsi paneldeki **Entegrasyon** sayfasındadır (menünün en altında): API anahtarı, gizli anahtar ve Çalışma Alanı Kimliğiniz. Ödeme hesaplarınızın token'ları da oradadır.

```java
import com.odemehub.Client;
import com.odemehub.Options;

Client client = new Client(Options.builder()
    .baseUrl("https://app.odemehub.com")
    .team("4829301756")                                   // Çalışma Alanı Kimliğiniz
    .apiKey(System.getenv("ODEMEHUB_API_KEY"))
    .apiSecret(System.getenv("ODEMEHUB_API_SECRET"))
    .build());
```

Gizli anahtar hiçbir zaman tel üzerinden gitmez; yalnızca imza üretmekte kullanılır. Anahtarları kodun içine yazmayın, ortam değişkeninde tutun. İmza, isteğin atıldığı anı da kapsar: sunucunuzun saati beş dakikadan fazla kaymışsa istekler `AuthenticationException` ile geri çevrilir, saatinizi NTP ile eşitleyin.

Geçit hiçbir yerde veritabanı numarası kullanmaz: ödeme hesabı, işlem, sipariş, ödeme linki, abonelik ve kayıtlı kart her zaman token'ıyla ya da sizin referansınızla adlanır.

İstemci durum tutmaz; uygulama boyunca tek bir nesneyi bütün thread'lerde paylaşabilirsiniz (Spring'de bir `@Bean`). İstek bir dakika içinde yanıt almazsa kesilir; süreyi `Options.builder().timeout(Duration.ofSeconds(30))` ile değiştirebilirsiniz. Vekil sunucu gibi ayarlar için kendi `HttpClient`'ınızı `new Client(options, httpClient)` ile verebilirsiniz.

Her uç için bir metot vardır ve adı uçla aynıdır: `create-order` → `client.createOrder(CreateOrder)`. İstekler `com.odemehub.request` paketindeki değişmez nesnelerdir; alanı çok olanlar builder ile, az olanlar yapıcıyla kurulur. Zorunlu bir alan eksikse `build()` `NullPointerException` fırlatır ve hangi alanın eksik olduğunu söyler. Alanların içeriğini SDK denetlemez, geçit denetler; reddedilen alanlar `ValidationException` ile döner. İsteğe bağlı bir alanı vermezseniz gövdeye hiç yazılmaz. Kart numarası ve güvenlik kodu `toString()` çıktısında `*****` görünür; kart nesnesi yanlışlıkla loglansa da kart bilgisi görünmez.

Yanıtlar `com.odemehub.response` paketindedir ve API'nin JSON'unu iç içe olduğu gibi yansıtır: ödeme yanıtında ödeme `getTransaction()` altında, iadede iade `getRefund()` altında durur. Ödeme uçlarında istek ve yanıt sınıfları aynı adı taşır (`request.SecurePayment` → `response.SecurePayment`); ikisini aynı dosyada kullanırken yanıtı `var` ile karşılamak en rahatıdır.

Sabit değer kümeleri `com.odemehub.enums` paketindeki enum'lardır: `Currency`, `Period`, `OrderStatus`, `SubscriptionStatus`, `TransactionStatus`, `PaymentStatus`, `SecurityType`, `RefundType`, `RefundStatus`, `CardScheme`, `CardType`. Her biri geçidin yazdığı değeri `getValue()` ile verir. Geçit bu sürümün tanımadığı yeni bir değer gönderirse SDK çökmez; o alan `null` okunur.

## Uçlar

| Metot | Uç | Yanıt |
| --- | --- | --- |
| `securePayment` | `POST secure-payment` | `SecurePayment` |
| `regularPayment` | `POST regular-payment` | `RegularPayment` |
| `refundPayment` | `POST refund-payment` | `GiveBack` |
| `cancelPayment` | `POST cancel-payment` | `GiveBack` |
| `retrievePayments` | `POST retrieve-payments` | `PaymentList` |
| `retrieveBin` | `POST retrieve-bin` | `Bin` |
| `createOrder` | `POST create-order` | `OrderDetails` |
| `retrieveOrders` | `POST retrieve-orders` | `OrderList` |
| `updateOrder` | `POST update-order/{token}` | `OrderDetails` |
| `createPaymentLink` | `POST create-payment-link` | `PaymentLinkDetails` |
| `retrievePaymentLinks` | `POST retrieve-payment-links` | `PaymentLinkList` |
| `updatePaymentLink` | `POST update-payment-link/{token}` | `PaymentLinkDetails` |
| `createSubscription` | `POST create-subscription` | `SubscriptionDetails` |
| `retrieveSubscriptions` | `POST retrieve-subscriptions` | `SubscriptionList` |
| `updateSubscription` | `POST update-subscription/{token}` | `SubscriptionDetails` |
| `createSavedCard` | `POST create-saved-card` | `SavedCardDetails` |
| `retrieveSavedCards` | `POST retrieve-saved-cards` | `SavedCardList` |
| `updateSavedCard` | `POST update-saved-card/{token}` | `SavedCardDetails` |
| `deleteSavedCard` | `POST delete-saved-card/{token}` | `DeletedSavedCard` |

Her kaynağa aynı yoldan ulaşılır: `create-*` açar ve token'ını döner, `retrieve-*` token'la, sizin referansınızla ya da en fazla yedi günlük bir tarih aralığıyla sorar ve her zaman liste döner (eşleşen yoksa boş liste), `update-*/{token}` yalnızca gönderdiğiniz alanları değiştirir. Sorgu istekleri `byToken(...)`, `byReference(...)`, `between(...)` ya da `latest()` ile kurulur. Bütün uçlar POST'tur. Silinebilen tek kaynak kayıtlı karttır.

## Müşteri

Müşteri üç parçadan oluşur: sizdeki referansı (`reference`), fatura adresi (`billingAddress`) ve teslimat adresi (`shippingAddress`). Adreslerde ad, soyad, e-posta, telefon, adres, ilçe, il ve ülke vardır; şirket adına alışverişte fatura adresine `companyTitle`, `taxNumber` ve `taxOffice` üçü birlikte eklenir.

```java
import com.odemehub.request.Address;
import com.odemehub.request.Customer;

Customer customer = Customer.builder()
    .reference("musteri-88")   // müşterinin sizdeki referansı
    .billingAddress(Address.builder()
        .firstname("Ahmet")
        .lastname("Yılmaz")
        .email("ahmet@ornek.com")
        .phone("05551112233")
        .address("Kızılırmak Mah. Dumlupınar Blv. No:3")
        .district("Çankaya")
        .province("Ankara")
        .country("Türkiye")
        .build())
    .build();
```

- **Ödeme ve kart saklama:** fatura adresinin sekiz alanı da zorunludur; teslimat adresi gönderilmez. Referans, kart saklayan ya da kayıtlı kartla yapılan ödemede ve kart saklamada zorunludur.
- **Sipariş ve abonelik:** elinizde ne varsa onu gönderin; ödeme sayfası eksikleri müşteriye sorar. Abonelikte referans zorunludur; siparişte referans verilmezse ödeyen müşteri listenize yazılmaz.
- **Müşteri listesi:** referans gönderdiğiniz ödeme başarılı olunca geçit müşteriyi o referansla çalışma alanınızın müşteri listesine yazar ya da günceller; başarısız ödeme müşteriye dokunmaz. Referanssız ödeme yine alınır ama müşteri kaydedilmez ve kart saklanamaz. Saklanan kart müşteriye bağlanır; müşterinin son ödeme yaptığı kart varsayılan kartı olur.

## Karttan doğrudan çekim

Müşteriyi bankasına göndermeden çekim yapar. Başarılı yanıt, paranın alındığı anlamına gelir.

```java
import com.odemehub.request.Card;
import com.odemehub.request.RegularPayment;

var payment = client.regularPayment(RegularPayment.builder()
    .reference("SIP-10231")          // işlemin sizdeki referansı, en az bir rakam
    .amount("450.00")
    .installmentNumber(1)
    .ip(request.getRemoteAddr())
    .customer(customer)
    .card(Card.builder()
        .holderName("AHMET YILMAZ")
        .number("5400 3600 0000 0003")      // boşluklu ya da boşluksuz
        .expiryMonth("12")
        .expiryYear("2030")
        .securityCode("000")
        .build())
    .build());

if (payment.getResult().isSuccessful()) {
    payment.getTransaction().getToken();         // iade, iptal ve sorguda bununla adlandırılır
    payment.getTransaction().getStatus();        // TransactionStatus.SUCCESSFUL
    payment.getTransaction().getPaymentStatus(); // PaymentStatus.PAID
}
```

Tutarlar her zaman `String`'dir (`"450.00"`): imzalanıp gönderildiği gibi kalır, yolda yuvarlanmaz. Tutar sıfırdan büyük ve en fazla 10.000.000,00 olabilir; kuruş noktayla ayrılır.

Yanıttaki `getTransaction()` ödemeyi bütünüyle taşır: `getToken()`, `getReference()`, `getStatus()`, `getPaymentStatus()`, `getSecurityType()`, `getAmount()`, `getBaseAmount()`, `getCurrency()`, `getInstallmentNumber()`, `isTest()`, `getCreatedAt()`; `isSuccessful()` ve `isFinished()` kısayolları da vardır. `getCustomer()` ödemenin müşteri kopyasını (`getReference()`, `getBillingAddress()`), `getConversion()` kur çevirisini, `getSavedCard()` saklanan kartı verir.

## 3D ödeme

3D'de çekim iki adımdır: siz ödemeyi başlatırsınız, müşteri bankasına gider, banka sonucu sizin adresinize gönderir.

```java
import com.odemehub.request.SecurePayment;

var payment = client.securePayment(SecurePayment.builder()
    .reference("SIP-10232")
    .amount("450.00")
    .installmentNumber(1)
    .ip(request.getRemoteAddr())
    .callbackUrl("https://magazam.com/odeme/donus")
    .customer(customer)
    .card(card)
    .build());

if (payment.getResult().isSuccessful()) {
    return "redirect:" + payment.getRedirectUrl();   // müşteriyi bankaya gönderin
}
```

Başarılı yanıt **ödeme alındı demek değildir**; yalnızca müşterinin gideceği adres hazır demektir.

Müşteriyi **15 dakika içinde** bu adrese yönlendirin. Sayfası o süre içinde açılmayan ödemenin süresi dolar (`expired`). Süresi dolmuş bağlantıyı açan müşteri doğrudan `callbackUrl` adresinize, `successful=0` ile geri gönderilir; `retrievePayments()` sorgusu da başarısız sonucu ve nedenini döner.

Banka işini bitirince müşteri, tarayıcısı üzerinden `callbackUrl` adresinize döner. O POST (form gövdesi) **sonucu taşımaz**, yalnızca sonucun hazır olduğunu haber verir:

| Alan | Anlamı |
| --- | --- |
| `transaction_token` | ödemenin geçitteki token'ı |
| `reference` | sizin kendi referansınız |
| `successful` | `1` / `0` — yalnızca ipucu, **güvenilmez** |

Sonucu kendi imzalı bağlantınızdan sorun:

```java
import com.odemehub.request.RetrievePayments;

@PostMapping("/odeme/donus")
public String odemeDonus(@RequestParam("transaction_token") String transactionToken) {
    var payments = client.retrievePayments(RetrievePayments.byToken(transactionToken)).getPayments();

    if (!payments.isEmpty() && payments.get(0).isSuccessful()) {
        // siparişi ödendi olarak işaretleyin
    }
    // ...
}
```

Neden böyle: o POST'u bizim sunucumuz değil, müşterinin tarayıcısı gönderir; tarayıcıya imzalayacak bir sır verilemez. `successful` alanına bakıp sipariş kapatmayın — onu herkes gönderebilir; yalnız "başarısız" ipucunda gereksiz sorgudan kaçınmak için kullanın. Geçide sorduğunuz yanıt ise her zaman imzalıdır ve SDK imzayı sizin için doğrular. Çalışma alanınızda olmayan bir token sorarsanız liste boş döner.

Müşteri bankadan sonra sekmeyi kapatırsa tarayıcı `callbackUrl` adresinize hiç dönmez; bunun için panelde `transaction.*` webhook'u tanımlayın (bkz. [Webhook](#webhook)).

## Ödemeyi sorgulamak

Ödemenin token'ı elinizdeyse `RetrievePayments.byToken(...)`, yalnızca kendi referansınız varsa `RetrievePayments.byReference(...)` kullanın; ikincisi o referansla yapılmış **bütün** denemeleri eskiden yeniye döner. Bağlantı koptuğu için yanıtını alamadığınız bir ödemeyi yeniden denemeden önce bununla sorun.

```java
import com.odemehub.request.RetrievePayments;

var attempts = client.retrievePayments(RetrievePayments.byReference("SIP-10232")).getPayments();

// Belli günlerdeki bütün denemeler (reddedilen ve süresi dolanlar dahil), eskiden yeniye.
// Tarihler çalışma alanınızın saat diliminde, en fazla 7 gün; RetrievePayments.latest() son 7 gün.
var payments = client.retrievePayments(RetrievePayments.between("2026-09-26", "2026-10-02"));

for (var attempt : payments.getPayments()) {
    System.out.println(attempt.getReference() + " " + attempt.getStatus() + " " + attempt.getPaymentStatus() + " " + attempt.getErrorMessage());
}
```

Listedeki her deneme (`response.Transaction`) `getToken()`, `getStatus()` (`TransactionStatus`: `STARTED`, `REDIRECTED_TO_SECURE_PAGE`, `RETURNED_FROM_SECURE_PAGE`, `TIMEOUT`, `FAILED`, `EXPIRED`, `SUCCESSFUL`), `getPaymentStatus()` (`PaymentStatus`: `UNPAID`, `PAID`, `CANCELLED`, `REFUNDED`, `PARTIALLY_REFUNDED`), `getSecurityType()`, `getAmount()` / `getBaseAmount()` / `getCurrency()`, `getInstallmentNumber()`, `isTest()`, `getErrorCode()` / `getErrorMessage()`, `getCreatedAt()`, `getCustomer()`, `getConversion()`, kart saklanması istendiyse `getSavedCard()` ve bağlı olduğu `getOrderToken()` / `getPaymentLinkToken()` / `getSubscriptionToken()` alanlarını taşır. Ödeme sayfasında yapılan denemeler de burada görünür. `payments.successful()` geçen denemeleri verir.

## Kalemler ve gönderim

Sipariş, ödeme linki ve abonelik kalemlerden oluşur. Her kalem kendi adını ve fiyatını taşır; katalogdan hiçbir şey okunmaz. Birim fiyat **KDV dahildir**: %20 KDV'li 120,00'lik bir kalemin 100,00'ü mal, 20,00'si vergidir. Toplamı siz göndermezsiniz; geçit kalemleri toplar ve yanıtta `getSubtotal()`, `getTaxAmount()` ve `getAmount()` olarak döner.

```java
import com.odemehub.request.Item;

var items = List.of(
    Item.builder().name("Kahve makinesi").unitAmount("450.00").quantity(1).taxRate("20")
        .reference("KAHVE-MAKINESI")                                     // isteğe bağlı
        .saveAsProduct(true)                                             // ürün listenize de yazılır; referans ister
        .image("https://magazam.com/img/kahve-makinesi.jpg")              // isteğe bağlı
        .build(),
    Item.builder().name("Kahve 500 g").unitAmount("180.00").quantity(2).taxRate("10").build()
);
```

`taxRate` verilmezse kalem vergisizdir. Gönderim yöntemleri istekte gönderilmez: sipariş ya da abonelikte `requiresShipping(true)` verirseniz ödeme sayfası teslimat adresini sorar ve panelinizdeki **Gönderim Yöntemleri** listesinden o adrese gönderenleri sunar; seçilenin ücreti toplama eklenir ve yanıtta `getShippingMethod()` olarak döner.

## Sipariş (ödeme sayfası)

Kart bilgisini hiç görmek istemiyorsanız sipariş açıp müşteriyi geçidin kendi sayfasına yollayabilirsiniz.

```java
import com.odemehub.request.CreateOrder;

var created = client.createOrder(CreateOrder.builder()
    .reference("SIPARIS-10233")
    .successUrl("https://magazam.com/tesekkurler")
    .cancelUrl("https://magazam.com/sepet")
    .items(items)
    .requiresShipping(true)
    .customer(customer)
    .build());

var order = created.getOrder();
order.getToken();   // siparişi sonra sorgulamak ve değiştirmek için saklayın

return "redirect:" + order.getCheckoutUrl();
```

Aynı referansla tekrar `createOrder` çağırırsanız yeni sipariş açılmaz: açık sipariş gönderdiklerinizle güncellenir ve aynı token'la döner. Ödenmiş ya da ödemesi sürmekte olan sipariş değiştirilemez; istek `order.reference` alanında reddedilir.

Ödeme tamamlanınca müşteri, 3D'dekiyle aynı biçimde `successUrl` adresinize döner: aynı üç alan gelir, sonucu yine `retrievePayments()` ile sorarsınız. Müşteri ödeme sayfasında karttan kaynaklı bir hata alırsa size dönmez, sayfada kalıp başka kartla dener.

`response.Order` siparişi bütünüyle taşır: `getToken()`, `getReference()`, `getDescription()`, `getPaymentProviderToken()`, `getStatus()` (`OrderStatus.OPEN` / `PAID`), `getItems()`, `getShippingMethod()` (müşterinin seçtiği, seçene kadar `null`), `getSubtotal()`, `getShippingAmount()`, `getTaxAmount()`, `getAmount()`, `getCurrency()`, `isTest()`, `getCreatedAt()`, `getCheckoutUrl()` (ödenebilir değilse `null`), ödeyen işlem `getTransaction()` (açıkken `null`) ve müşteri `getCustomer()` (`getReference()` — referanssız açılan siparişte `null` —, `getBillingAddress()`, `getShippingAddress()`). Müşteri `OrderDetails` üzerinde de (`created.getCustomer()`) aynen durur; listelerde siparişin kendisindedir.

Sorgu ve değişiklik:

```java
import com.odemehub.request.RetrieveOrders;
import com.odemehub.request.UpdateOrder;

var order = client.retrieveOrders(RetrieveOrders.byToken(token)).getOrders().get(0);
var sameOrders = client.retrieveOrders(RetrieveOrders.byReference("SIPARIS-10233")).getOrders();
var lastWeek = client.retrieveOrders(RetrieveOrders.latest()).getOrders();

if (order.isPaid()) {
    order.getTransaction().getToken();   // iade / iptal / retrievePayments için
}

// Yalnızca gönderdiğiniz alanlar yazılır; kalem gönderirseniz eski kalemlerin hepsinin yerine geçer.
client.updateOrder(UpdateOrder.builder(token)
    .items(items)
    .description("Hediye paketli")
    .clear("cancel_url")           // bir alanı boşaltmak için adını verin
    .build());
```

Gönderdiğiniz müşteri alanları siparişteki müşterinin üzerine yazılır; yeni bir referans eskisinin yerine geçer. Ödenmiş siparişe hiçbir şey yazılmaz.

Müşteri ödedikten sonra sekmeyi kapatırsa tarayıcı `successUrl` adresinize hiç dönmez; panelde `order.paid` webhook'u tanımlayın (bkz. [Webhook](#webhook)). Siparişin bütün denemelerini (reddedilenler dahil) görmek için `retrievePayments(RetrievePayments.byReference(...))` listesinde `getOrderToken()` değerine bakın.

## Ödeme linki

Ödeme linki, adresi bilen herkesin tekrar tekrar ödeyebildiği bir sayfadır; kapatılana ya da son günü geçene kadar ödeme alır. Müşteri istemez; ödeyen kişi kendini sayfada tanıtır.

```java
import com.odemehub.enums.Currency;
import com.odemehub.request.CreatePaymentLink;

var link = client.createPaymentLink(CreatePaymentLink.builder()
    .items(items)
    .currency(Currency.TRY)
    .reference("LINK-77")             // isteğe bağlı, en az bir rakam; verilmezse geçit verir
    .description("Atölye kaydı")
    .expiresAt("2026-12-31")          // son gün, çalışma alanınızın saat diliminde; verilmezse süresiz
    .build()).getPaymentLink();

link.getCheckoutUrl();   // müşteriyle paylaşacağınız adres
```

`response.PaymentLink`: `getToken()`, `getReference()`, `getDescription()`, `getPaymentProviderToken()`, `getItems()`, `getSubtotal()`, `getTaxAmount()`, `getAmount()`, `getCurrency()`, `isActive()` (açık ve son günü geçmemiş), `isTest()`, `getExpiresAt()` (son an, ISO 8601 UTC), `getCheckoutUrl()` (ödenemiyorsa `null`), `getCreatedAt()`; sorgu yanıtında ayrıca `getTransactions()` (son 50 deneme), `getTransactionsCount()` ve `successful()`.

```java
import com.odemehub.request.RetrievePaymentLinks;
import com.odemehub.request.UpdatePaymentLink;

var details = client.retrievePaymentLinks(RetrievePaymentLinks.byToken(token)).getPaymentLinks().get(0);
details.isActive();
details.getTransactionsCount();   // linkteki toplam deneme sayısı
details.getTransactions();        // son 50 deneme, yeniden eskiye
details.successful();             // bunlardan geçenler

client.updatePaymentLink(UpdatePaymentLink.builder(token).isActive(false).build());   // kapat
client.updatePaymentLink(UpdatePaymentLink.builder(token).isActive(true).expiresAt("2027-01-31").build());
```

Panelde açılan linkler de aynı uçlarla, token'ı ya da referansıyla bulunur. Linkle ödeyen kişi müşteri listenize yazılmaz ve kartı saklanmaz. Linkin 50'den eski denemeleri `retrievePayments()` ile tarih aralığıyla okunur.

## Abonelikler

Müşteriden dönem dönem tahsilat yapmak için abonelik açarsınız. İlk ödeme geçidin kendi sayfasında yapılır ve kart müşteriye saklanıp varsayılan kartı olur; sonraki yenilemeler müşterinin varsayılan kartından (son ödeme yaptığı kart) çekilir. Abonelik, siparişle aynı alanları (kalemler, gönderim, müşteri, adresler) ve ek olarak dönemi alır.

```java
import com.odemehub.enums.Period;
import com.odemehub.request.CreateSubscription;

var subscription = client.createSubscription(CreateSubscription.builder()
    .reference("UYELIK-4471")
    .period(Period.MONTHLY)             // DAILY | WEEKLY | MONTHLY | ANNUALLY
    .renewalLimit(12)                   // isteğe bağlı; verilmezse iptal edilene kadar sürer
    .items(List.of(Item.builder().name("Premium üyelik").unitAmount("149.90").quantity(1).taxRate("20").build()))
    .successUrl("https://magazam.com/tesekkurler")
    .customer(Customer.builder().reference("musteri-88").build())   // referans zorunlu
    .build()).getSubscription();

subscription.getToken(); // aboneliği sonra sorgulamak ve değiştirmek için saklayın

return "redirect:" + subscription.getCheckoutUrl();
```

Aboneliğin açılabilmesi için ödeme hesabının kart saklayabiliyor ve 3D ödeme alabiliyor olması, planınızın da kayıtlı kartları kapsaması gerekir; aksi halde istek `subscription.payment_provider_token` alanında reddedilir.

Dönem bitince yeni dönem müşterinin varsayılan kartından çekilir. Banka kabul etmezse çekim beş kez denenir; bu sırada abonelik `active` kalır. Son deneme de olmazsa abonelik `past_due` olur ve panelde tanımlı webhook adresinize `subscription.past_due` gider; o dönemin ödenebileceği adres `getCheckoutUrl()`'dedir, müşterinize siz iletirsiniz. Müşteri ödediği anda abonelik kaldığı yerden devam eder.

```java
import com.odemehub.request.RetrieveSubscriptions;

var subscription = client.retrieveSubscriptions(RetrieveSubscriptions.byToken(token)).getSubscriptions().get(0);

subscription.getStatus();                 // SubscriptionStatus: PENDING | ACTIVE | PAST_DUE | CANCELLED | COMPLETED
subscription.getRenewalsPaid();           // ödenen yenileme sayısı
subscription.getRenewal().getAmount();    // içinde bulunulan dönemin tutarı
subscription.getRenewal().getEndsAt();    // dönemin bittiği an
subscription.getNextPaymentAt();          // sonraki tahsilat
subscription.getCheckoutUrl();            // ödenmemiş dönem varsa müşteriye verilecek adres
subscription.getCustomer().getReference();
```

`RetrieveSubscriptions.byReference(...)`, `between(...)` ve `latest()` siparişteki gibi çalışır.

Değiştirmek ve iptal etmek için `updateSubscription()` kullanılır. İlk ödemeden önce her alan değişebilir; ilk ödemeden sonra yalnızca iptal, ödeme sayısı, dönem ve aynı kalemlerin birim fiyatı değişebilir; müşteri dahil başka bir alan gönderirseniz `ValidationException`. Gönderdiğiniz kalemler henüz ödenmemiş bütün yenilemelerin fiyatını değiştirir.

```java
import com.odemehub.enums.SubscriptionStatus;
import com.odemehub.request.UpdateSubscription;

// Kalemleri değiştir
client.updateSubscription(UpdateSubscription.builder(token).items(newItems).build());

// İptal: ödenmiş günler yanmaz, iade yapılmaz
var cancelled = client.updateSubscription(UpdateSubscription.builder(token).status(SubscriptionStatus.CANCELLED).build()).getSubscription();
cancelled.getCancelledAt();
```

İptal edilen abonelikte müşteri ödediği dönemin sonuna kadar hizmeti almaya devam eder ve bir daha tahsilat yapılmaz.

## Webhook

Sipariş ödendiğinde, link ödemesi alındığında, abonelik durum değiştirdiğinde, API ödemesi bittiğinde ve bir ödeme iade ya da iptal edildiğinde geçidin **kendi sunucusu** imzalı bir JSON POST gönderir. Müşteri sekmeyi kapatıp `callbackUrl` / `successUrl` adresinize hiç dönmese de bu bildirim gelir. Adresler kodda verilmez; panelde **Ayarlar → Webhook** sayfasında olay ve adres seçilerek tanımlanır.

| Kaynak | Olaylar |
| --- | --- |
| Sipariş | `order.paid`, `order.payment_refunded`, `order.payment_cancelled` |
| Ödeme linki | `payment_link.paid`, `payment_link.payment_refunded`, `payment_link.payment_cancelled` |
| Abonelik | `subscription.active`, `subscription.past_due`, `subscription.cancelled`, `subscription.ended`, `subscription.completed`, `subscription.payment_refunded`, `subscription.payment_cancelled` |
| API ödemesi | `transaction.successful`, `transaction.failed`, `transaction.expired`, `transaction.payment_refunded`, `transaction.payment_cancelled` |

Sipariş, link ya da abonelikte alınan ödeme için `transaction.*` gelmez; o kaynağın kendi olayı gelir.

**Webhook nihai sonuç değildir.** Gövde yalnızca kaynağın token'ını (para hareketi varsa yanında ödemenin token'ını) taşır. Kararı, token ile geçide sorduğunuz yanıta göre verin ve yanıtı kendi kaydınızla (referans, tutar, durum) karşılaştırın. Gövdeyi **ham** (`byte[]`) okuyun; bir nesneye çevirip yeniden yazarsanız imza tutmaz.

```java
import com.odemehub.exception.SignatureException;
import com.odemehub.request.RetrieveOrders;
import com.odemehub.request.RetrievePayments;
import com.odemehub.request.RetrieveSubscriptions;

@PostMapping("/odemehub/webhook")
public ResponseEntity<Void> webhook(
    HttpServletRequest request,
    @RequestBody byte[] payload,
    @RequestHeader(value = "X-Timestamp", required = false) String timestamp,
    @RequestHeader(value = "X-Signature", required = false) String signature
) {
    com.odemehub.response.Webhook webhook;

    try {
        webhook = client.webhook(request.getMethod(), request.getRequestURI(), payload, timestamp, signature);
    } catch (SignatureException exception) {
        return ResponseEntity.status(401).build();
    }

    webhook.getId();      // aynı bildirim tekrar gelebilir; bununla ayıklayın
    webhook.getEvent();   // "order.paid", "subscription.active" ...

    if (webhook.getOrderToken() != null) {
        var order = client.retrieveOrders(RetrieveOrders.byToken(webhook.getOrderToken())).getOrders().get(0);
        order.getStatus();                               // OrderStatus.PAID
        order.getTransaction().getPaymentStatus();       // PaymentStatus.REFUNDED ...
    } else if (webhook.getSubscriptionToken() != null) {
        var subscription = client.retrieveSubscriptions(RetrieveSubscriptions.byToken(webhook.getSubscriptionToken())).getSubscriptions().get(0);
    } else if (webhook.getTransactionToken() != null) {   // transaction.* ve payment_link.*
        var transaction = client.retrievePayments(RetrievePayments.byToken(webhook.getTransactionToken())).getPayments().get(0);
        transaction.getPaymentLinkToken();               // linkte alınan ödemede linkin token'ı
    }

    return ResponseEntity.noContent().build();
}
```

Abonelik ve link ödemelerinin iade/iptal olaylarında `getTransactionToken()` da gelir; `retrievePayments()` yanıtındaki `getOrderToken()` / `getPaymentLinkToken()` / `getSubscriptionToken()` ödemenin gerçekten o kaynağa ait olduğunu gösterir. Yalnızca doğrulamak için `client.verifyWebhook(...)` `boolean` döner. 2xx dışında bir yanıt (ya da yanıtsızlık) başarısız sayılır; geçit 60 sn, 5 dk, 15 dk ve 30 dk arayla toplam 5 kez dener ve yönlendirmeleri izlemez. Ulaşmayan bildirimler panelde ilgili kaydın sayfasında HTTP kodu ve yanıtıyla listelenir.

## Ödeme hangi hesaptan geçer

`paymentProviderToken` verirseniz ödeme o hesaptan geçer; sipariş, ödeme linki ve abonelik açarken de aynı alan vardır ve müşteri ödeme sayfasında o hesaptan öder. Vermezseniz hesabı çalışma alanınız seçer: panelde **Ödeme Ayarları → Gate (Yönlendirme)** altındaki kurallar sırayla denenir ve ödemenin karşıladığı ilk kural hesabı belirler. Kurallar kartın bankasına, şemasına, programına, tipine, ticari kart olup olmadığına, tutara ve para birimine bakabilir. Hiçbir kural tutmazsa ödeme varsayılan hesaptan geçer.

- Kuralın hesabı ödemeyi alamıyorsa (ödeme türünü ya da para birimini desteklemiyorsa) o kural atlanır.
- Kayıtlı kartla ödeme her zaman kartın saklandığı hesaptan geçer; bu ödemede `paymentProviderToken` gönderilmez.
- Taksitleri `retrieveBin()` ile gösteriyorsanız orada da hesap vermeyin: taksitler ödemenin gideceği hesaptan gelir ve çekilen tutar gösterdiğinizle aynı olur.

## Kur çevirisi

Panelde **Ödeme Ayarları → Kur Çevirici** altında bir kural tanımladıysanız, o para biriminde gelen ödeme karttan kuralın para biriminde çekilir. Örneğin 100 USD istersiniz, karttan 4.985,56 TRY çekilir. Kur, TCMB'nin güncel döviz satış kuru ve üzerine eklediğiniz marjdır ya da sizin girdiğiniz sabit kurdur.

İsteğinizde hiçbir şey değişmez: tutarı ve para birimini her zamanki gibi gönderirsiniz. Yanıttaki `getConversion()` karttan ne çekildiğini söyler:

```java
var payment = client.regularPayment(RegularPayment.builder()
    .amount("100.00")
    .currency(Currency.USD)
    // ...
    .build());

if (payment.getConversion() != null) {
    payment.getConversion().getAmount();   // 4985.56
    payment.getConversion().getCurrency(); // Currency.TRY
    payment.getConversion().getRate();     // 49.855560
}
```

- Çevrilmeyen ödemede `getConversion()` `null` döner. `retrievePayments()` aynı bilgiyi yeniden verir.
- İade tutarını çekilen para biriminde gönderin (yukarıdaki örnekte TRY).
- Güncel kur alınamıyorsa ödeme alınmaz; `422` ile `transaction.currency` alanında hata döner. Birkaç dakika sonra tekrar deneyin.

## Kart sorgusu ve taksitler

Kart numarasının ilk hanelerinden kartın kim tarafından verildiğini, hangi programa ait olduğunu ve tutarın kaç taksite bölünebileceğini sorar. Hiçbir şey çekilmez.

```java
import com.odemehub.request.RetrieveBin;

var bin = client.retrieveBin(RetrieveBin.builder().bin("54003600").amount("450.00").build());

if (bin.getResult().isSuccessful()) {
    bin.getIssuerName();   // Garanti Bankası
    bin.getProgram();      // Bonus
    bin.getScheme();       // CardScheme.MASTERCARD
    bin.getType();         // CardType.CREDIT
    bin.isCommercial();

    for (var installment : bin.getInstallments()) {
        // 3 taksitte ayda 157.87, toplam 473.60
        System.out.println(installment.getNumber() + " x " + installment.getAmount() + " = " + installment.getTotal());
    }
}
```

Kartın tamamını göndermeyin; ilk 6-8 hane yeter ve yalnızca o kadarı kabul edilir. Sorgu başarısız dönebilir: kart tanınmıyor olabilir ya da hesabınızın sağlayıcısı taksit vermiyor olabilir. İki durumda da satışı durdurmayın, tek çekimle devam edin.

## Tutarlar ve taksit

İki tutar vardır ve karıştırılmamalıdır:

| Alan | Anlamı |
| --- | --- |
| `amount` | **Karttan çekilecek** tutar. Vade farkı varsa içindedir. |
| `baseAmount` | **Sattığınız** tutar, vade farkından önceki hâli. Gönderilmezse `amount` ile aynı kabul edilir; `amount`'tan büyük olamaz. |

Taksit 1 ile 12 arasındadır ve yalnızca Türk Lirası ödemelerde yapılır. USD, EUR ya da GBP ödemede `installmentNumber` `1` olmalıdır ve `retrieveBin()` taksit listesini boş döner; kur çevirisiyle TRY'den başka bir para birimine çekilen ödeme için de aynısı geçerlidir.

Taksitsiz satışta ikisi eşittir ve `baseAmount` göndermenize gerek yoktur. Taksitli satışta `retrieveBin` size o taksidin toplamını verir; onu `amount` olarak, sattığınız tutarı `baseAmount` olarak gönderin:

```java
var payment = client.regularPayment(RegularPayment.builder()
    .reference("SIP-10234")
    .amount("473.60")        // 3 taksitin toplamı
    .baseAmount("450.00")    // satılan tutar
    .installmentNumber(3)
    // ...
    .build());
```

Bazı sağlayıcılar vade farkını kendileri ekler; geçit bunu bilir ve gerekirse sağlayıcıya taban tutarı gönderir. Sizin tarafınızda değişen bir şey yoktur.

## Kayıtlı kartlar

Müşterinin kartını saklayıp sonraki ödemelerde numara sormadan çekim yapabilirsiniz. Kart, müşterinin sizdeki referansıyla müşteriye bağlanır; planınızın kayıtlı kartları kapsaması ve ödeme hesabının kart saklayabilmesi gerekir.

```java
import com.odemehub.request.CreateSavedCard;
import com.odemehub.request.DeleteSavedCard;
import com.odemehub.request.RetrieveSavedCards;
import com.odemehub.request.UpdateSavedCard;

// Ödeme sırasında saklamak için: karta shouldSave(true) verin, müşteriye referans verin.
// Ödeme olmadan saklamak için (müşteride referans ve fatura adresinin tamamı zorunlu);
// sağlayıcı kartı alınca müşteri gönderdiğiniz bilgilerle listenize yazılır:
var kept = client.createSavedCard(CreateSavedCard.builder().customer(customer).card(card).build());
kept.getSavedCard();   // saklanamadıysa null; nedeni getResult().getMessage()

// Müşterinin kartları: varsayılan önce, sonra eskiden yeniye
var cards = client.retrieveSavedCards(RetrieveSavedCards.byReference("musteri-88"));
String token = cards.getSavedCards().get(0).getToken();

client.updateSavedCard(new UpdateSavedCard(token));   // varsayılan yap
client.deleteSavedCard(new DeleteSavedCard(token));   // sağlayıcıdan ve geçitten sil
```

Kayıtlı kartla ödeme alırken `card` yerine kartın token'ını verin; müşterinin referansı kartın saklandığı referansla aynı olmalıdır:

```java
var payment = client.regularPayment(RegularPayment.builder()
    .reference("SIP-10235")
    .amount("120.00")
    .installmentNumber(1)
    .ip(request.getRemoteAddr())
    .customer(customer)
    .savedCardToken(token)
    .build());
```

`card` ile `savedCardToken` birlikte gönderilmez; ikisi birden ya da hiçbiri verilirse geçit isteği reddeder.

Kart saklayan bir ödemenin yanıtında `payment.getSavedCard()` dolu gelir; kartın token'ını oradan öğrenirsiniz. Kart her yerde token ile adlandırılır; yanıtlardaki `getCustomer().getReference()` kartın kimin için saklandığını söyler.

## İade ve iptal

```java
import com.odemehub.request.CancelPayment;
import com.odemehub.request.RefundPayment;

// Gün sonu almamış ödemenin tamamını geri alır
client.cancelPayment(new CancelPayment(payment.getTransaction().getToken()));

// Tutar verilirse kısmi, verilmezse kalanın tamamı iade edilir.
// Kur çevirisiyle çekilen ödemede tutar çekilen para birimindedir.
var refund = client.refundPayment(new RefundPayment(payment.getTransaction().getToken(), "100.00"));
refund.getRefund().getType();     // RefundType.REFUND
refund.getRefund().getAmount();   // 100.00
refund.getTransaction().getPaymentStatus();   // PaymentStatus.PARTIALLY_REFUNDED
```

## 1.0.1'deki kırıcı değişiklikler

1.0.1, SDK'yı geçidin bugünkü API'sine taşır ve 1.0.0 koduyla uyumlu değildir. 1.0.0'dan geçerken dikkat edilecekler:

- **Kaldırılan uçlar:** `orderPayment`, `subscriptionPayment`, `saveProduct`, `cancelSubscription`, `retrieveTransactions`, `saveCard`, `savedCards`, `defaultSavedCard`. Yerlerine: `createOrder`, `createSubscription`, kalemlerin isteğin içinde gönderilmesi (ürün kataloğu yok), `updateSubscription(...status(SubscriptionStatus.CANCELLED))`, `retrievePayments` / `retrievePayments`, `createSavedCard`, `retrieveSavedCards`, `updateSavedCard`.
- **Yeni metotlar:** ödeme linki uçları, bütün güncelleme uçları ve her kaynakta tek `retrieve*` sorgusu.
- **Müşteri:** `Customer` artık `reference` + `billingAddress` + `shippingAddress` (`Address`) taşır; `channelReference`, düz adres alanları ve `TaxDetails` kalktı (şirket alanları fatura adresinde).
- **Kalemler:** `OrderItem` / `SubscriptionItem` yerine ad, fiyat, adet ve KDV oranı zorunlu tek `Item`.
- **Token parametresi:** tek kaydı adlandıran her istek parametresi `token`'dır (`new RefundPayment(token)`, `UpdateOrder.builder(token)`).
- **Yanıtlar iç içe:** ödeme bilgisi `payment.getTransaction()` altında (`getTransactionToken()`, `getChannelReference()` gibi düz kısayollar kalktı); iade `getRefund()` altında; `OrderDetails` / `SubscriptionDetails` müşteriyi hem üstte hem varlığın kendisinde taşır; ödeme linkinin işlemleri `PaymentLinkDetails` üzerindedir.
- **Enum'lar:** para birimi, dönem, durumlar, güvenlik tipi, iade tipi, kart şeması ve tipi `String` yerine `com.odemehub.enums` enum'larıdır; istekler de enum alır (`currency(Currency.TRY)`).
- **İmza:** imza artık an, metot, yol ve gövdeyi kapsar ve `X-Timestamp` başlığıyla gider (aşağıda).
- **Hatalar:** güncellenen, silinen, iade ya da iptal edilen kaydın token'ı bulunamazsa `NotFoundException` (404) atılır; eskiden bu durum 422 dönüyordu. Sorgular bulunamayan kayıtta boş liste döner. Yeni istisnalar: `ForbiddenException` (403), `NotFoundException` (404), `RateLimitException` (429).
- **İstemci tarafı denetim yok:** kart ile kayıtlı kartın birlikte verilmesi artık `build()`'de değil, geçitte reddedilir.
- **Webhook:** `orderWebhook()`, `subscriptionWebhook()`, `transactionWebhook()` yerine tek `webhook(method, path, body, timestamp, signature)` (ve `verifyWebhook()`); imza istek ve yanıtlarla aynı şemadadır. Gövde yalnızca token taşır (`getOrderToken()`, `getPaymentLinkToken()`, `getSubscriptionToken()`, `getTransactionToken()`); durum `retrieve*()` ile sorulur. Adresler panelde tanımlandığı için `SecurePayment`, `CreateOrder`, `UpdateOrder`, `CreateSubscription`, `UpdateSubscription` builder'larında `webhookUrl(...)` yok. `Signature`'ın yalnız gövdeyi imzalayan `sign(body)` / `verify(body, signature)` metotları kalktı.
- **Kanal kalktı.** `Options.builder().channelToken(...)`, isteklerdeki `channelToken(...)` ve `ChannelMessage` yoktur. Referans alanları `channelReference` yerine `reference` adını taşır (ödeme, sipariş, abonelik, link, kalem); yanıtlarda `getChannelToken()` yoktur, `getChannelReference()` yerine `getReference()`. Geri dönüşte tarayıcı `channel_reference` değil `reference` POST eder.
- **Sorgular tek uçta.** Her kaynakta tek sorgu metodu vardır: `retrievePayments`, `retrieveOrders`, `retrieveSubscriptions`, `retrievePaymentLinks`, `retrieveSavedCards`. İstek `byToken`, `byReference`, `between` ya da `latest` ile kurulur; yanıt her zaman listedir, bulunamayan kayıt `NotFoundException` değil boş listedir. GET isteği kalmadı.
- **Gönderim:** sipariş ve abonelik `requiresShipping` ile ödeme sayfasında gönderim adresi ister; gönderim yöntemleri panelde tanımlanır, istekte gönderilmez. Yanıtta yalnızca ödeyenin seçtiği yöntem (`shippingMethod`: `reference`, `title`, `amount`, `taxRate`) gelir.
- **Kalemler:** `taxRate` isteğe bağlı; yeni `saveAsProduct`.
- **Müşteri:** referans gönderilmeyebilir; o zaman müşteri kaydedilmez ve kart saklanamaz. Abonelikte ve kart saklamada zorunludur. `NamedCustomer.getReference()` ve `getBillingAddress()` `null` olabilir.
- **Ödeme linki:** son 50 deneme ve `getTransactionsCount()` `retrievePaymentLinks` yanıtında her `PaymentLink` üzerindedir; `PaymentLinkDetails` yalnızca linki taşır.
- **Kayıtlı kart:** listede her kart kendi `getCustomer()`'ını taşır; `SavedCardList.getCustomer()` kalktı, varsayılan kart `defaultCard()` ile alınır. Listelenen ödemede `getSavedCard()`, kartın saklanması istendiyse saklanan kartı verir.

## Hatalar

Bütün hatalar `com.odemehub.exception.OdemehubException`'dan türer ve denetimsizdir (`RuntimeException`); tek bir `catch` hepsini yakalar.

| Hata | HTTP | Ne demek |
| --- | --- | --- |
| `ValidationException` | 422 | Gönderdiğiniz alanlar kabul edilmedi. Ödeme denenmedi. `getErrors()` alan alan söyler. |
| `NotFoundException` | 404 | Güncellenmek, silinmek, iade ya da iptal edilmek istenen kayıt çalışma alanınızda yok (sorgularda boş liste döner). |
| `AuthenticationException` | 401 | API anahtarı bu çalışma alanına ait değil, imza gizli anahtarla tutmuyor ya da saatiniz kaymış. |
| `ForbiddenException` | 403 | Çalışma alanınız şu an işlem yapamıyor ya da planınız / modülleriniz bu ucu kapsamıyor. |
| `RateLimitException` | 429 | Çok sık istek. `getRetryAfter()` kaç saniye beklemeniz gerektiğini söyler. |
| `SignatureException` | — | Gelen yanıtın ya da bildirimin imzası tutmadı. Geçitten geldiği kanıtlanamaz; **işleme almayın**. |
| `TransportException` | — | Geçide ulaşılamadı ya da yanıt okunamadı. Ödemenin ne olduğu belirsizdir; geçitteki kayıt asıl doğruyu söyler. |
| `UnexpectedResponseException` | diğer | Beklenmeyen bir yanıt geldi (ör. 500). `getStatus()` HTTP kodunu verir. |

```java
import com.odemehub.exception.OdemehubException;
import com.odemehub.exception.ValidationException;

try {
    client.regularPayment(payment);
} catch (ValidationException exception) {
    exception.getErrors(); // {transaction.amount=[...]}
} catch (OdemehubException exception) {
    exception.getMessage();
}
```

Ağ hatasında ödemeyi körlemesine tekrarlamayın: `TransportException` "olmadı" demek değil, "bilmiyorum" demektir. Önce `retrievePayments(RetrievePayments.byReference(...))` ile sorun.

## İmzayı elle doğrulamak

Her istek, yanıt ve webhook iki başlık taşır: imzalandığı an (`X-Timestamp`, Unix saniyesi) ve imza (`X-Signature`). İmza; an, HTTP metodu, yol ve gövdenin tam metninin satır sonuyla birleştirilip gizli anahtarla alınmış HMAC-SHA256'sıdır, küçük harf hex olarak yazılır:

```
HMAC-SHA256(apiSecret, "{timestamp}\n{METHOD}\n{path}\n{body}")
```

Yol, adresteki baştaki `/` dahil ve sorgu dizgisi hariç yoldur (`/api/{takım}/gateway/{uç}`). Bütün uçlar POST'tur. Beş dakikadan eski imza kabul edilmez. SDK bunu `com.odemehub.Signature` sınıfıyla yapar:

```java
new Signature(apiSecret).sign("POST", "/api/1000000001/gateway/regular-payment", body, timestamp);
```

Test vektörü: `secret_test` anahtarıyla, `1700000000` anında, `POST` `/api/1000000001/gateway/regular-payment` yoluna `{"a":1}` gövdesinin imzası `4d6225c9dd46837418b40dd8140d76a24cd7520d81ff3b280bf98da8da6a8771`'dir.

## Geliştirme

Maven kurulu olması gerekmez; depo Maven wrapper'ıyla gelir:

```bash
./mvnw package
```
