# java-sdk (`com.odemehub:java-sdk`, Maven Central)

ödemehub Gateway API'sinin istemcisi. API'nin doğruluk kaynağı kardeş depo `../payment-app`; sözleşme özeti `../payment-app/.ai/rules/gateway-api.md`. API değişmeden SDK'ya alan eklenmez, API değişince beş SDK birlikte güncellenir.

## Sürüm ve yayın
Sürüm çıkarmadan ya da yayınla ilgili bir şeye dokunmadan önce `../payment-app/.ai/rules/sdk-release.md`'yi oku. Kısaca:

- Sürüm `pom.xml` `<version>`'da.
- README'deki "Değişiklikler" bölümüne sürüm notu eklenir; commit mesajı `X.Y.Z: kısa özet`.
- `git tag vX.Y.Z` ve `git push origin main --tags`. Tag `.github/workflows/publish.yml`'i tetikler; `sh ./mvnw -P release deploy` imzalar ve Central'a yükler, Central yayını bitirene kadar bekler (10–15 dk normal). İmza ve yükleme yalnız `release` profilinde.
- Beş SDK (php, node, python, java, dotnet) aynı sürüm numarasını taşır.
- Yayınlanmış sürüm geri alınmaz, aynı numarayla yeniden yayınlanamaz; düzeltme yeni yama sürümüyle gider. Sürüm silme/geri çekme kullanıcıya sorulmadan yapılmaz.
- Token, parola, GPG anahtarı agent tarafından okunmaz, yazılmaz; `gh secret set` kullanıcıya bırakılır.
