# ödemehub Java SDK

ödemehub ödeme geçidini kendi uygulamanızdan kullanmak için hazırlanmış Java istemcisi. Kart çekmek, 3D ödeme başlatmak, müşteriyi ödeme sayfasına yollamak, ürün kataloğunuzu eşlemek, abonelik açmak, kart saklamak, iade ve iptal yapmak ve bir kartın taksit seçeneklerini sormak için gereken her şey burada.

İstemci her isteği takımınızın gizli anahtarıyla imzalar, gelen her yanıtın imzasını doğrular. Siz imza, başlık ya da JSON ayrıntılarıyla uğraşmazsınız. İstekler Java'nın kendi `HttpClient`'ıyla gider; tek bağımlılık JSON için Jackson'dır.

## Kurulum

Java 17 ve üzeri gerekir.

Maven:

```xml
<dependency>
    <groupId>com.odemehub</groupId>
    <artifactId>java-sdk</artifactId>
    <version>1.0.0</version>
</dependency>
```

Gradle:

```groovy
implementation 'com.odemehub:java-sdk:1.0.0'
```

## Yapılandırma

Dört bilgiye ihtiyacınız var. Hepsi paneldeki **Entegrasyon** sayfasındadır (menünün en altında): API anahtarı, gizli anahtar, Çalışma Alanı Kimliğiniz ve kanallarınızla ödeme hesaplarınızın token'ları.

```java
import com.odemehub.Client;
import com.odemehub.Options;

Client client = new Client(Options.builder()
    .baseUrl("https://odeme.gurmehub.com")
    .team("4829301756")                                   // Çalışma Alanı Kimliğiniz
    .channelToken("6f1c2e7a-4b3d-4c8e-9a61-2f5d7b0c3e14") // müşterinin size ulaştığı kanal
    .apiKey(System.getenv("ODEMEHUB_API_KEY"))
    .apiSecret(System.getenv("ODEMEHUB_API_SECRET"))
    .build());
```

Gizli anahtar hiçbir zaman tel üzerinden gitmez; yalnızca imza üretmekte kullanılır. Anahtarları kodun içine yazmayın, ortam değişkeninde tutun.

Kanal token'ı entegrasyonun tamamı için bir kez verilir. Birden çok kanalda satıyorsanız tek bir istekte `channelToken(...)` vererek o isteği başka kanala yazdırabilirsiniz. Geçit hiçbir yerde veritabanı numarası kullanmaz: kanal, ödeme hesabı, işlem, kayıtlı kart, abonelik ve sipariş her zaman token'ıyla adlanır.

İstemci durum tutmaz; uygulama boyunca tek bir nesneyi bütün thread'lerde paylaşabilirsiniz (Spring'de bir `@Bean`). İstek bir dakika içinde yanıt almazsa kesilir; süreyi `Options.builder().timeout(Duration.ofSeconds(30))` ile değiştirebilirsiniz. Vekil sunucu gibi ayarlar için kendi `HttpClient`'ınızı `new Client(options, httpClient)` ile verebilirsiniz.

İstekler `com.odemehub.request` paketindeki değişmez nesnelerdir; alanı çok olanlar builder ile, az olanlar yapıcıyla kurulur. Zorunlu bir alan eksikse `build()` `NullPointerException` fırlatır ve hangi alanın eksik olduğunu söyler. İsteğe bağlı bir alanı vermezseniz gövdeye hiç yazılmaz. Kart numarası ve güvenlik kodu `toString()` çıktısında `*****` görünür; kart nesnesi yanlışlıkla loglansa da kart bilgisi görünmez.

İstek ve yanıt sınıfları aynı adları taşır (`request.SecurePayment` → `response.SecurePayment`). İkisini aynı dosyada kullanırken yanıtı `var` ile karşılamak en rahatıdır.

## Karttan doğrudan çekim

Müşteriyi bankasına göndermeden çekim yapar. Başarılı yanıt, paranın alındığı anlamına gelir.

```java
import com.odemehub.request.Card;
import com.odemehub.request.Customer;
import com.odemehub.request.RegularPayment;

var payment = client.regularPayment(RegularPayment.builder()
    .channelReference("SIP-10231")          // işlemin sizdeki referansı
    .amount("450.00")
    .installmentNumber(1)
    .ip(request.getRemoteAddr())
    .customer(Customer.builder()
        .channelReference("musteri-88")
        .firstname("Ahmet")
        .lastname("Yılmaz")
        .email("ahmet@ornek.com")
        .phone("05551112233")
        .address("Kızılırmak Mah. Dumlupınar Blv. No:3")
        .district("Çankaya")
        .province("Ankara")
        .country("Türkiye")
        .build())
    .card(Card.builder()
        .holderName("AHMET YILMAZ")
        .number("5400360000000003")
        .expiryMonth("12")
        .expiryYear("2030")
        .securityCode("000")
        .build())
    .build());

if (payment.getResult().isSuccessful()) {
    // payment.getTransactionToken() — ödemenin geçitteki token'ı; iade ve iptalde bununla adlandırılır
}
```

Tutarlar her zaman `String`'dir (`"450.00"`): imzalanıp gönderildiği gibi kalır, yolda yuvarlanmaz.

## 3D ödeme

3D'de çekim iki adımdır: siz ödemeyi başlatırsınız, müşteri bankasına gider, banka sonucu sizin adresinize gönderir.

```java
import com.odemehub.request.SecurePayment;

var payment = client.securePayment(SecurePayment.builder()
    .channelReference("SIP-10232")
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

Müşteriyi **15 dakika içinde** bu adrese yönlendirin. Sayfası o süre içinde açılmayan ödemenin süresi dolar (`expired`). Süresi dolmuş bağlantıyı açan müşteri doğrudan `callbackUrl` adresinize, `successful=0` ile geri gönderilir; `retrievePayment()` sorgusu da başarısız sonucu ve nedenini döner.

Banka işini bitirince müşteri, tarayıcısı üzerinden `callbackUrl` adresinize döner. O POST (form gövdesi) **sonucu taşımaz**, yalnızca sonucun hazır olduğunu haber verir:

| Alan | Anlamı |
| --- | --- |
| `transaction_token` | ödemenin geçitteki token'ı |
| `channel_reference` | sizin kendi referansınız |
| `successful` | `1` / `0` — yalnızca ipucu, **güvenilmez** |

Sonucu kendi imzalı bağlantınızdan sorun:

```java
import com.odemehub.request.RetrievePayment;

@PostMapping("/odeme/donus")
public String odemeDonus(@RequestParam("transaction_token") String transactionToken) {
    var outcome = client.retrievePayment(new RetrievePayment(transactionToken));

    if (outcome.getResult().isSuccessful()) {
        // siparişi ödendi olarak işaretleyin
    }
    // ...
}
```

Neden böyle: o POST'u bizim sunucumuz değil, müşterinin tarayıcısı gönderir; tarayıcıya imzalayacak bir sır verilemez. `successful` alanına bakıp sipariş kapatmayın — onu herkes gönderebilir; yalnız "başarısız" ipucunda gereksiz sorgudan kaçınmak için kullanın. Geçide sorduğunuz yanıt ise her zaman imzalıdır ve SDK imzayı sizin için doğrular. Başkasının işlemini sorarsanız `ValidationException` alırsınız.

## Ürünler

Sipariş kalemleri ve abonelikler ürünleri **sizdeki referanslarıyla** adlandırır. Ürünü panelde (Ürünler sayfası) tanımlayabilir ya da kendi kataloğunuzdan geçide yazabilirsiniz:

```java
import com.odemehub.request.SaveProduct;

var product = client.saveProduct(SaveProduct.builder()
    .channelReference("KAHVE-MAKINESI")
    .name("Kahve makinesi")
    .type("simple")          // simple | recurring
    .amount("450.00")
    .taxRate("20")           // fiyatın içindeki KDV oranı
    .build());

client.saveProduct(SaveProduct.builder()
    .channelReference("PREMIUM-AYLIK")
    .name("Premium üyelik")
    .type("recurring")
    .amount("149.90")
    .taxRate("20")
    .period("monthly")       // monthly | annually — yalnız recurring için zorunlu
    .build());
```

Aynı kanalda aynı referans aynı üründür: tekrar gönderirseniz ikinci ürün açılmaz, mevcut olan güncellenir. `currency` verilmezse TRY, `isActive` verilmezse `true` kabul edilir. Ürün silinmez; `isActive(false)` ile satışa kapatılır.

Ödeme istekleri ürünü hiçbir zaman değiştirmez; ürünün tek yazıldığı yer bu çağrı ve panel.

## Ödeme sayfası

Kart bilgisini hiç görmek istemiyorsanız sipariş açıp müşteriyi geçidin kendi sayfasına yollayabilirsiniz.

```java
import com.odemehub.request.OrderItem;
import com.odemehub.request.OrderPayment;

var order = client.orderPayment(OrderPayment.builder()
    .channelReference("SIPARIS-10233")
    .successUrl("https://magazam.com/tesekkurler")
    .cancelUrl("https://magazam.com/sepet")
    .customer(customer)
    .items(List.of(
        OrderItem.of("KAHVE-MAKINESI"),
        OrderItem.builder().channelReference("KAHVE-500G").quantity(2).unitAmount("180.00").build(),
        OrderItem.builder().channelReference("HEDIYE-PAKETI").name("Hediye paketi").unitAmount("25.00").build()
    ))
    .build());

return "redirect:" + order.getCheckoutUrl();
```

Sipariş tutarını göndermezsiniz; geçit kalemleri toplar ve `order.getAmount()` olarak döner. Bir kalemin boş bıraktığı ad, fiyat ve KDV oranı kayıtlı üründen gelir; kalemde verdiğiniz değerler yalnızca o sipariş için geçerlidir, ürünü değiştirmez. Kayıtlı olmayan bir referansla da kalem gönderebilirsiniz, ama o zaman `name` ve `unitAmount` zorunludur.

Ödeme tamamlanınca müşteri, 3D'dekiyle aynı biçimde `successUrl` adresinize döner: aynı üç alan gelir, sonucu yine `retrievePayment()` ile sorarsınız. Müşteri ödeme sayfasında karttan kaynaklı bir hata alırsa size dönmez, sayfada kalıp başka kartla dener.

## Abonelikler

Müşteriden dönem dönem tahsilat yapmak için abonelik açarsınız. Neye abone olunduğu bir ya da birkaç **abonelik ürünüdür** (`type("recurring")`), sizdeki referanslarıyla adlandırılır; fiyatı, para birimini ve dönemini ürün taşır. Aynı aboneliğe konan ürünlerin dönemi ve para birimi aynı olmalıdır.

```java
import com.odemehub.request.SubscriptionItem;
import com.odemehub.request.SubscriptionPayment;

var subscription = client.subscriptionPayment(SubscriptionPayment.builder()
    .channelReference("UYELIK-4471")
    .items(List.of(
        SubscriptionItem.of("PREMIUM-AYLIK"),
        SubscriptionItem.builder().channelReference("EK-KULLANICI").quantity(3).build()
    ))
    .successUrl("https://magazam.com/tesekkurler")
    .customer(customer)
    .build());

subscription.getToken(); // aboneliği sonra sorgulamak ve iptal etmek için saklayın

return "redirect:" + subscription.getCheckoutUrl();
```

Bir kaleme `unitAmount` verirseniz o fiyat **yalnızca ilk dönem** için geçerlidir (ör. ilk ay yarı fiyat); sonraki dönemler ürünün kendi fiyatından çekilir.

İlk ödeme her zaman geçidin kendi sayfasında yapılır ve kart zorunlu olarak saklanır: sonraki dönemler o karttan çekilir. Ödeme tamamlanınca müşteri `successUrl` adresinize döner ve sonucu yine `retrievePayment()` ile sorarsınız; abonelik `active` olur ve aşağıdaki bildirim de gider.

Dönem bitince yeni dönem açılır ve müşterinin varsayılan kartından çekilir. Banka kabul etmezse çekim bir buçuk gün içinde beş kez denenir (araları 3, 6, 9 ve 12 saat); bu sırada abonelik `active` kalır. Beşinci deneme de olmazsa abonelik `past_due` olur; çalışma alanı yöneticilerinize e-posta, `webhookUrl` adresinize bildirim gider. İkisi de o dönemin dilediği kartla ödenebileceği bağlantıyı taşır; bağlantıyı müşterinize siz iletirsiniz. Süre sınırı yoktur; müşteri ödediği anda abonelik kaldığı yerden devam eder.

Aboneliğin durumunu sorabilirsiniz:

```java
import com.odemehub.request.RetrieveSubscription;

var subscription = client.retrieveSubscription(new RetrieveSubscription(token));

subscription.getStatus();      // pending | active | past_due | cancelled
subscription.getAmount();      // 149.90 — içinde bulunulan dönemin fiyatı
subscription.getEndsAt();      // sonraki tahsilat zamanı
subscription.getCheckoutUrl(); // ödenmemiş dönem varsa müşteriye verilecek adres

for (var item : subscription.getItems()) {
    System.out.println(item.getQuantity() + " x " + item.getName() + " (" + item.getChannelReference() + ")");
}

if (subscription.isPastDue()) {
    // müşteriyi kendi ödeme sayfanızda uyarabilirsiniz
}
```

Tutar, aboneliğin **içinde bulunduğu dönemin** fiyatıdır. Ürünün fiyatını yükseltirseniz yürüyen dönem çekildiği fiyatta kalır, yeni fiyat sonraki dönemden itibaren işler.

İptalde ödenmiş günler yanmaz:

```java
import com.odemehub.request.CancelSubscription;

var subscription = client.cancelSubscription(new CancelSubscription(token));

subscription.getCancelledAt(); // iptal edildiği an
subscription.getEndsAt();      // hizmetin süreceği son gün
subscription.isCancelled();    // ödenmiş dönem sürüyorsa henüz false
```

Müşteri, ödediği dönemin sonuna kadar hizmeti almaya devam eder; o güne kadar abonelik `active` görünür, dönem bitince `cancelled` olur ve bir daha tahsilat yapılmaz. Ödenmemiş bir aboneliğin (ilk ödemesi yapılmamış ya da `past_due`) iptali hemen geçerlidir. İade yapılmaz.

Aboneliğin açılabilmesi için varsayılan ödeme hesabınızın kart saklayabiliyor olması gerekir; saklamayan bir hesapla açmaya çalışırsanız istek `subscription.payment_provider_token` alanında reddedilir.

### Abonelik bildirimleri (webhook)

Abonelik açarken `webhookUrl` verirseniz, aboneliğin durumu her değiştiğinde o adrese imzalı bir POST gönderilir. Gövde düz JSON'dur ve imza `X-Signature` başlığındadır — yani geçidin API yanıtlarıyla aynı yöntem.

```java
var subscription = client.subscriptionPayment(SubscriptionPayment.builder()
    .channelReference("UYELIK-4471")
    .items(List.of(SubscriptionItem.of("PREMIUM-AYLIK")))
    .successUrl("https://magazam.com/tesekkurler")
    .customer(customer)
    .webhookUrl("https://magazam.com/odemehub/abonelik")
    .build());
```

Bildirimi karşılayan uçta gövdeyi **ham** (`byte[]`) okuyup imzayla birlikte SDK'ya verin. İmza gövdenin bayt bayt kendisini kapsar; gövdeyi bir nesneye çevirip yeniden yazarsanız imza tutmaz.

```java
import com.odemehub.exception.SignatureException;

@PostMapping("/odemehub/abonelik")
public ResponseEntity<Void> abonelikBildirimi(
    @RequestBody byte[] payload,
    @RequestHeader(value = "X-Signature", required = false) String signature
) {
    com.odemehub.response.SubscriptionWebhook webhook;

    try {
        webhook = client.subscriptionWebhook(payload, signature);
    } catch (SignatureException exception) {
        return ResponseEntity.badRequest().build();
    }

    var subscription = webhook.getSubscription();   // sorgudakiyle aynı nesne

    if (webhook.isActive()) {
        aboneligeErisimAc(subscription.getChannelReference(), subscription.getEndsAt());
    } else if (webhook.isPastDue()) {
        musteriyiUyar(subscription.getCheckoutUrl());
    } else if (webhook.isCancelled()) {
        yenilemeyiDurdur(subscription.getEndsAt());
    } else if (webhook.isEnded()) {
        erisimiKapat(subscription.getChannelReference());
    }

    return ResponseEntity.ok().build();
}
```

Gönderilen olaylar aboneliğin **durumudur**, yapılan işlem değil:

| Olay | Ne zaman gider |
| --- | --- |
| `active` | bir dönem ödendi (ilk ödeme ya da yenileme) |
| `past_due` | dönem kayıtlı karttan tahsil edilemedi, müşteriden bekleniyor |
| `cancelled` | abonelik iptal edildi; müşteri `endsAt` tarihine kadar hizmeti almaya devam eder |
| `ended` | ödenmiş dönem doldu, abonelik kapandı |

2xx dışında bir yanıt (ya da yanıtsızlık) başarısız sayılır; bildirim 5 dakika sonra bir kez daha denenir. Ulaşmayan bildirimler panelde aboneliğin sayfasında HTTP kodu ve yanıtıyla listelenir.

## Ödeme hangi hesaptan geçer

`paymentProviderToken` verirseniz ödeme o hesaptan geçer; sipariş ve abonelik açarken de aynı alan vardır ve müşteri ödeme sayfasında o hesaptan öder. Vermezseniz hesabı çalışma alanınız seçer: panelde **Ödeme Ayarları → Gate (Yönlendirme)** altındaki kurallar sırayla denenir ve ödemenin karşıladığı ilk kural hesabı belirler. Kurallar kartın bankasına, şemasına, programına, tipine, ticari kart olup olmadığına, tutara ve para birimine bakabilir. Hiçbir kural tutmazsa ödeme varsayılan hesaptan geçer.

- Kuralın hesabı ödemeyi alamıyorsa (ödeme türünü ya da para birimini desteklemiyorsa) o kural atlanır.
- Kayıtlı kartla ödeme her zaman kartın saklandığı hesaptan geçer.
- Taksitleri `retrieveBin()` ile gösteriyorsanız orada da hesap vermeyin: taksitler ödemenin gideceği hesaptan gelir ve çekilen tutar gösterdiğinizle aynı olur.

## Kur çevirisi

Panelde **Ödeme Ayarları → Kur Çevirici** altında bir kural tanımladıysanız, o para biriminde gelen ödeme karttan kuralın para biriminde çekilir. Örneğin 100 USD istersiniz, karttan 4.985,56 TRY çekilir. Kur, TCMB'nin güncel döviz satış kuru ve üzerine eklediğiniz marjdır ya da sizin girdiğiniz sabit kurdur.

İsteğinizde hiçbir şey değişmez: tutarı ve para birimini her zamanki gibi gönderirsiniz. Yanıttaki `getConversion()` karttan ne çekildiğini söyler:

```java
var payment = client.regularPayment(RegularPayment.builder()
    .amount("100.00")
    .currency("USD")
    // ...
    .build());

if (payment.getConversion() != null) {
    payment.getConversion().getAmount();   // 4985.56
    payment.getConversion().getCurrency(); // TRY
    payment.getConversion().getRate();     // 49.855560
}
```

- Çevrilmeyen ödemede `getConversion()` `null` döner. `retrievePayment()` aynı bilgiyi yeniden verir.
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
    bin.getScheme();       // mastercard
    bin.getType();         // credit
    bin.isCommercial();

    for (var installment : bin.getInstallments()) {
        // 3 taksitte ayda 157.87, toplam 473.60
        System.out.println(installment.getNumber() + " x " + installment.getAmount() + " = " + installment.getTotal());
    }
}
```

Kartın tamamını göndermeyin; ilk 6-8 hane yeter ve yalnızca o kadarı kabul edilir.

Sorgu başarısız dönebilir: kart tanınmıyor olabilir ya da hesabınızın sağlayıcısı taksit vermiyor olabilir. İki durumda da satışı durdurmayın, tek çekimle devam edin.

## Tutarlar ve taksit

İki tutar vardır ve karıştırılmamalıdır:

| Alan | Anlamı |
| --- | --- |
| `amount` | **Karttan çekilecek** tutar. Vade farkı varsa içindedir. |
| `baseAmount` | **Sattığınız** tutar, vade farkından önceki hâli. Gönderilmezse `amount` ile aynı kabul edilir. |

Taksit yalnızca Türk Lirası ödemelerde yapılır. USD, EUR ya da GBP ödemede `installmentNumber` `1` olmalıdır ve `retrieveBin()` taksit listesini boş döner; kur çevirisiyle TRY'den başka bir para birimine çekilen ödeme için de aynısı geçerlidir.

Taksitsiz satışta ikisi eşittir ve `baseAmount` göndermenize gerek yoktur. Taksitli satışta `retrieveBin` size o taksidin toplamını verir; onu `amount` olarak, sattığınız tutarı `baseAmount` olarak gönderin:

```java
var payment = client.regularPayment(RegularPayment.builder()
    .channelReference("SIP-10234")
    .amount("473.60")        // 3 taksitin toplamı
    .baseAmount("450.00")    // satılan tutar
    .installmentNumber(3)
    // ...
    .build());
```

Bazı sağlayıcılar vade farkını kendileri ekler; geçit bunu bilir ve gerekirse sağlayıcıya taban tutarı gönderir. Sizin tarafınızda değişen bir şey yoktur.

## Kayıtlı kartlar

Müşterinin kartını saklayıp sonraki ödemelerde numara sormadan çekim yapabilirsiniz.

```java
import com.odemehub.request.DefaultSavedCard;
import com.odemehub.request.DeleteSavedCard;
import com.odemehub.request.NamedCustomer;
import com.odemehub.request.SaveCard;
import com.odemehub.request.SavedCards;

// Ödeme sırasında saklamak için: karta shouldSave(true) verin.
// Ödeme olmadan saklamak için:
var kept = client.saveCard(SaveCard.builder().customer(customer).card(card).build());

var musteri = new NamedCustomer("musteri-88");

// Müşterinin kartları
var cards = client.savedCards(new SavedCards(musteri));

// Varsayılan yapma / silme
String token = cards.getSavedCards().get(0).getToken();

client.defaultSavedCard(new DefaultSavedCard(musteri, token));
client.deleteSavedCard(new DeleteSavedCard(musteri, token));
```

Kayıtlı kartla ödeme alırken `card` yerine kartın token'ını verin:

```java
var payment = client.regularPayment(RegularPayment.builder()
    .channelReference("SIP-10235")
    .amount("120.00")
    .installmentNumber(1)
    .ip(request.getRemoteAddr())
    .customer(customer)
    .savedCardToken(token)
    .build());
```

`card` ile `savedCardToken` birlikte ya da hiçbiri verilmezse `build()` `IllegalArgumentException` fırlatır.

Kart saklayan bir ödemenin yanıtında `payment.getSavedCard()` dolu gelir; kartın token'ını oradan öğrenirsiniz. Kart her yerde token ile adlandırılır.

## İade ve iptal

```java
import com.odemehub.request.CancelPayment;
import com.odemehub.request.RefundPayment;

// Gün sonu almamış ödemenin tamamını geri alır
client.cancelPayment(new CancelPayment(payment.getTransactionToken()));

// Tutar verilirse kısmi, verilmezse kalanın tamamı iade edilir.
// Kur çevirisiyle çekilen ödemede tutar çekilen para birimindedir.
client.refundPayment(new RefundPayment(payment.getTransactionToken(), "100.00"));
```

## Hatalar

Bütün hatalar `com.odemehub.exception.OdemehubException`'dan türer ve denetimsizdir (`RuntimeException`); tek bir `catch` hepsini yakalar.

| Hata | Ne demek |
| --- | --- |
| `ValidationException` | Gönderdiğiniz alanlar kabul edilmedi. Ödeme denenmedi. `getErrors()` alan alan söyler. |
| `AuthenticationException` | API anahtarı bu takıma ait değil ya da imza gizli anahtarla tutmuyor. |
| `SignatureException` | Gelen yanıtın ya da bildirimin imzası tutmadı. Geçitten geldiği kanıtlanamaz; **işleme almayın**. |
| `TransportException` | Geçide ulaşılamadı ya da yanıt okunamadı. Ödemenin ne olduğu belirsizdir; geçitteki kayıt asıl doğruyu söyler. |
| `UnexpectedResponseException` | Beklenmeyen bir yanıt geldi. `getStatus()` HTTP kodunu verir. |

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

Ağ hatasında ödemeyi körlemesine tekrarlamayın: `TransportException` "olmadı" demek değil, "bilmiyorum" demektir.

## İmzayı elle doğrulamak

İmza, gövdenin tam metninin gizli anahtarla HMAC-SHA256'sıdır, küçük harf hex olarak yazılır. SDK bunu `com.odemehub.Signature` sınıfıyla yapar:

```java
new Signature(apiSecret).sign(body);
```

Test vektörü: `secret_test` anahtarıyla `{"a":1}` gövdesinin imzası `6d0c951564cdd2b6b70e75b214293a8cd2542815ba54fe91c7f6ce105bc3d592`'dir.

## Geliştirme

Maven kurulu olması gerekmez; depo Maven wrapper'ıyla gelir:

```bash
./mvnw package
```
