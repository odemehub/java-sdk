package com.odemehub;

import com.fasterxml.jackson.databind.JsonNode;
import com.odemehub.exception.SignatureException;
import com.odemehub.request.Message;
import java.net.http.HttpClient;
import java.nio.charset.StandardCharsets;

/**
 * The gateway, as the merchant's application talks to it. Every request
 * leaves signed with the team's secret and every answer is checked against
 * it, so both sides can tell the other is really who it says it is.
 *
 * <p>There is one method per endpoint, named after it: {@code create-order}
 * is {@code createOrder()} and takes a {@code request.CreateOrder}.
 *
 * <p>A client holds no state beyond its options, so one can be shared across
 * threads for the life of the application.
 */
public final class Client {

    private final Options options;
    private final Signature signature;
    private final Transport transport;

    public Client(Options options) {
        this(options, HttpClient.newBuilder().connectTimeout(options.getTimeout()).build());
    }

    /**
     * @param http The HTTP client the requests go through, for a merchant that
     *             already configures one (a proxy, an executor).
     */
    public Client(Options options, HttpClient http) {
        this.options = options;
        this.signature = new Signature(options.getApiSecret());
        this.transport = new Transport(http, signature, options.getApiKey(), options.getTimeout());
    }

    /**
     * Start a payment the customer confirms with their bank. A successful
     * answer is not a settled payment: the customer is still to be sent to the
     * address it comes back with, and {@code retrievePayments} says what became
     * of it once they are back.
     */
    public com.odemehub.response.SecurePayment securePayment(com.odemehub.request.SecurePayment payment) {
        return com.odemehub.response.SecurePayment.fromBody(send(payment));
    }

    /**
     * Charge a payment straight to the card. A successful answer is a settled
     * payment.
     */
    public com.odemehub.response.RegularPayment regularPayment(com.odemehub.request.RegularPayment payment) {
        return com.odemehub.response.RegularPayment.fromBody(send(payment));
    }

    /**
     * Give money back out of a payment the provider has settled, whole or in
     * part. A refund that names no amount gives back everything the payment
     * has left in it.
     */
    public com.odemehub.response.GiveBack refundPayment(com.odemehub.request.RefundPayment refund) {
        return com.odemehub.response.GiveBack.fromBody(send(refund));
    }

    /**
     * Take back the whole of a payment the provider has not settled yet.
     * Anything less than the whole of it goes back as a refund instead.
     */
    public com.odemehub.response.GiveBack cancelPayment(com.odemehub.request.CancelPayment cancel) {
        return com.odemehub.response.GiveBack.fromBody(send(cancel));
    }

    /**
     * Payments as they stand — by token, every attempt under one of the
     * merchant's own references, or the ones made between two days; the
     * refused ones included, oldest first.
     */
    public com.odemehub.response.PaymentList retrievePayments(com.odemehub.request.RetrievePayments payments) {
        return com.odemehub.response.PaymentList.fromBody(send(payments));
    }

    /**
     * Ask what the gateway's provider knows about a card by the head of its
     * number, and how an amount may be paid off on it. Nothing is charged and
     * nothing is written down.
     */
    public com.odemehub.response.Bin retrieveBin(com.odemehub.request.RetrieveBin bin) {
        return com.odemehub.response.Bin.fromBody(send(bin));
    }

    /**
     * Open an order to be paid on the gateway's own page. Every call opens a
     * new order under a new token, even under a reference sent before, so
     * keep the token it comes back with. Nothing is charged here; the
     * customer is sent to the address it comes back with and pays there.
     */
    public com.odemehub.response.OrderDetails createOrder(com.odemehub.request.CreateOrder order) {
        return com.odemehub.response.OrderDetails.fromBody(send(order));
    }

    /**
     * Orders as they stand, each with its customer.
     */
    public com.odemehub.response.OrderList retrieveOrders(com.odemehub.request.RetrieveOrders orders) {
        return com.odemehub.response.OrderList.fromBody(send(orders));
    }

    /**
     * Change an open order. Only what is sent is written.
     */
    public com.odemehub.response.OrderDetails updateOrder(com.odemehub.request.UpdateOrder order) {
        return com.odemehub.response.OrderDetails.fromBody(send(order));
    }

    /**
     * Open a payment link. Every call opens a new link under a new token,
     * even under a reference sent before, so keep the token it comes back
     * with. The address it comes back with is the link itself.
     */
    public com.odemehub.response.PaymentLinkDetails createPaymentLink(com.odemehub.request.CreatePaymentLink link) {
        return com.odemehub.response.PaymentLinkDetails.fromBody(send(link));
    }

    /**
     * Payment links as they stand, each with the latest fifty payment attempts
     * made on it and how many there have been in all.
     */
    public com.odemehub.response.PaymentLinkList retrievePaymentLinks(com.odemehub.request.RetrievePaymentLinks links) {
        return com.odemehub.response.PaymentLinkList.fromBody(send(links));
    }

    /**
     * Change a payment link, or switch it off. Only what is sent is written.
     */
    public com.odemehub.response.PaymentLinkDetails updatePaymentLink(com.odemehub.request.UpdatePaymentLink link) {
        return com.odemehub.response.PaymentLinkDetails.fromBody(send(link));
    }

    /**
     * Payments made at the team's links, each with what was paid, the link it
     * was made at, the payer as they billed themselves and, once it is paid,
     * the attempt that paid it. They are opened by the payers paying, never
     * by the merchant, so they are only asked after.
     */
    public com.odemehub.response.LinkPaymentList retrieveLinkPayments(com.odemehub.request.RetrieveLinkPayments linkPayments) {
        return com.odemehub.response.LinkPaymentList.fromBody(send(linkPayments));
    }

    /**
     * Open a subscription. Every call opens a new subscription under a new
     * token, even under a reference sent before, so keep the token it comes
     * back with. The customer is sent to the address it comes back with and
     * pays the first renewal there; the rest are taken from the card they pay
     * with.
     */
    public com.odemehub.response.SubscriptionDetails createSubscription(com.odemehub.request.CreateSubscription subscription) {
        return com.odemehub.response.SubscriptionDetails.fromBody(send(subscription));
    }

    /**
     * Subscriptions as they stand, each with its customer and the renewal it
     * is on.
     */
    public com.odemehub.response.SubscriptionList retrieveSubscriptions(com.odemehub.request.RetrieveSubscriptions subscriptions) {
        return com.odemehub.response.SubscriptionList.fromBody(send(subscriptions));
    }

    /**
     * Change a subscription, or call it off with the status
     * {@code cancelled}. Only what is sent is written. Nothing is given back
     * on a cancellation: the customer is served to the end of what they paid
     * for, and nothing is charged after that.
     */
    public com.odemehub.response.SubscriptionDetails updateSubscription(com.odemehub.request.UpdateSubscription subscription) {
        return com.odemehub.response.SubscriptionDetails.fromBody(send(subscription));
    }

    /**
     * Keep a card for a customer without making a payment on it.
     */
    public com.odemehub.response.SavedCardDetails createSavedCard(com.odemehub.request.CreateSavedCard savedCard) {
        return com.odemehub.response.SavedCardDetails.fromBody(send(savedCard));
    }

    /**
     * Kept cards — by token, every card of a customer by their reference, or
     * the ones kept between two days — each with its customer, the default
     * first.
     */
    public com.odemehub.response.SavedCardList retrieveSavedCards(com.odemehub.request.RetrieveSavedCards savedCards) {
        return com.odemehub.response.SavedCardList.fromBody(send(savedCards));
    }

    /**
     * Make a kept card the one the customer pays with unless they say
     * otherwise.
     */
    public com.odemehub.response.SavedCardDetails updateSavedCard(com.odemehub.request.UpdateSavedCard savedCard) {
        return com.odemehub.response.SavedCardDetails.fromBody(send(savedCard));
    }

    /**
     * Let go of a kept card, at the provider and here.
     */
    public com.odemehub.response.DeletedSavedCard deleteSavedCard(com.odemehub.request.DeleteSavedCard savedCard) {
        return com.odemehub.response.DeletedSavedCard.fromBody(send(savedCard));
    }

    /**
     * The banks whose accounts may be added for their movements to be read,
     * each with the credentials it asks for.
     */
    public com.odemehub.response.BankList retrieveBanks(com.odemehub.request.RetrieveBanks banks) {
        return com.odemehub.response.BankList.fromBody(send(banks));
    }

    /**
     * Add a bank account whose movements are to be read. The bank is asked
     * first whether the credentials read the account; one it turns down is
     * not added, and comes back as an unsuccessful result in the bank's own
     * words rather than being thrown. Keep the token it comes back with.
     */
    public com.odemehub.response.BankAccountDetails createBankAccount(com.odemehub.request.CreateBankAccount bankAccount) {
        return com.odemehub.response.BankAccountDetails.fromBody(send(bankAccount));
    }

    /**
     * Bank accounts as they stand — by token, or every account of the
     * team's — each with when it was last read and why the last reading
     * failed, if it did; oldest first.
     */
    public com.odemehub.response.BankAccountList retrieveBankAccounts(com.odemehub.request.RetrieveBankAccounts bankAccounts) {
        return com.odemehub.response.BankAccountList.fromBody(send(bankAccounts));
    }

    /**
     * Rename a bank account, or replace its credentials. Only what is sent
     * is written; new credentials are tried against the bank first and,
     * turned down, change nothing.
     */
    public com.odemehub.response.BankAccountDetails updateBankAccount(com.odemehub.request.UpdateBankAccount bankAccount) {
        return com.odemehub.response.BankAccountDetails.fromBody(send(bankAccount));
    }

    /**
     * Remove a bank account and every movement read off it.
     */
    public com.odemehub.response.DeletedBankAccount deleteBankAccount(com.odemehub.request.DeleteBankAccount bankAccount) {
        return com.odemehub.response.DeletedBankAccount.fromBody(send(bankAccount));
    }

    /**
     * The movements on the team's bank accounts, a page at a time, oldest
     * first. They are read from what the gateway keeps, not from the bank;
     * the gateway reads every account at the bank once an hour, so the
     * latest movements may be up to an hour behind.
     */
    public com.odemehub.response.BankTransactionList retrieveBankTransactions(com.odemehub.request.RetrieveBankTransactions bankTransactions) {
        return com.odemehub.response.BankTransactionList.fromBody(send(bankTransactions));
    }

    /**
     * The providers a payment account may be added for, each with the
     * settings it asks for. Open only on a team a partner opened, like every
     * endpoint down to {@code createLogo}; any other team is turned away with
     * a {@code ForbiddenException}.
     */
    public com.odemehub.response.ProviderList retrieveProviders(com.odemehub.request.RetrieveProviders providers) {
        return com.odemehub.response.ProviderList.fromBody(send(providers));
    }

    /**
     * Add a payment account. Its settings are not tried against the provider
     * here. Keep the token it comes back with: a payment names the account
     * by it as its {@code paymentProviderToken}.
     */
    public com.odemehub.response.PaymentProviderDetails createPaymentProvider(com.odemehub.request.CreatePaymentProvider paymentProvider) {
        return com.odemehub.response.PaymentProviderDetails.fromBody(send(paymentProvider));
    }

    /**
     * Payment accounts as they stand — by token, or every account of the
     * team's — never with their settings. The answer is not cut into pages.
     */
    public com.odemehub.response.PaymentProviderList retrievePaymentProviders(com.odemehub.request.RetrievePaymentProviders paymentProviders) {
        return com.odemehub.response.PaymentProviderList.fromBody(send(paymentProviders));
    }

    /**
     * Rename a payment account, replace some of its settings, or make it the
     * default. Only what is sent is written.
     */
    public com.odemehub.response.PaymentProviderDetails updatePaymentProvider(com.odemehub.request.UpdatePaymentProvider paymentProvider) {
        return com.odemehub.response.PaymentProviderDetails.fromBody(send(paymentProvider));
    }

    /**
     * Remove a payment account.
     */
    public com.odemehub.response.DeletedPaymentProvider deletePaymentProvider(com.odemehub.request.DeletePaymentProvider paymentProvider) {
        return com.odemehub.response.DeletedPaymentProvider.fromBody(send(paymentProvider));
    }

    /**
     * Put down an address the gateway posts webhooks to, for the events
     * named. What is posted there is signed with this team's secret.
     */
    public com.odemehub.response.WebhookEndpointDetails createWebhookEndpoint(com.odemehub.request.CreateWebhookEndpoint webhookEndpoint) {
        return com.odemehub.response.WebhookEndpointDetails.fromBody(send(webhookEndpoint));
    }

    /**
     * Webhook addresses as they stand — by token, or every address of the
     * team's. The answer is not cut into pages.
     */
    public com.odemehub.response.WebhookEndpointList retrieveWebhookEndpoints(com.odemehub.request.RetrieveWebhookEndpoints webhookEndpoints) {
        return com.odemehub.response.WebhookEndpointList.fromBody(send(webhookEndpoints));
    }

    /**
     * Change a webhook address, the events it hears or whether it is
     * switched on. Only what is sent is written.
     */
    public com.odemehub.response.WebhookEndpointDetails updateWebhookEndpoint(com.odemehub.request.UpdateWebhookEndpoint webhookEndpoint) {
        return com.odemehub.response.WebhookEndpointDetails.fromBody(send(webhookEndpoint));
    }

    /**
     * Take a webhook address away.
     */
    public com.odemehub.response.DeletedWebhookEndpoint deleteWebhookEndpoint(com.odemehub.request.DeleteWebhookEndpoint webhookEndpoint) {
        return com.odemehub.response.DeletedWebhookEndpoint.fromBody(send(webhookEndpoint));
    }

    /**
     * Rename the team, or give it another timezone. Only what is sent is
     * written.
     */
    public com.odemehub.response.TeamDetails updateTeam(com.odemehub.request.UpdateTeam team) {
        return com.odemehub.response.TeamDetails.fromBody(send(team));
    }

    /**
     * How the team's checkout pages are dressed.
     */
    public com.odemehub.response.CheckoutDesignDetails retrieveCheckoutDesign(com.odemehub.request.RetrieveCheckoutDesign checkoutDesign) {
        return com.odemehub.response.CheckoutDesignDetails.fromBody(send(checkoutDesign));
    }

    /**
     * Change how the team's checkout pages are dressed; every payer sees it
     * from the next page they open. Only what is sent is written.
     */
    public com.odemehub.response.CheckoutDesignDetails updateCheckoutDesign(com.odemehub.request.UpdateCheckoutDesign checkoutDesign) {
        return com.odemehub.response.CheckoutDesignDetails.fromBody(send(checkoutDesign));
    }

    /**
     * Keep a logo for the team's checkout. The address it comes back with is
     * what {@code updateCheckoutDesign} takes as its {@code logo}; kept alone,
     * it is shown nowhere.
     */
    public com.odemehub.response.LogoDetails createLogo(com.odemehub.request.CreateLogo logo) {
        return com.odemehub.response.LogoDetails.fromBody(send(logo));
    }

    /**
     * Read a word the gateway posted to one of the merchant's webhook
     * addresses. Hand it the request exactly as it arrived — the method, the
     * path of the address it came to (without the query string), the raw
     * body byte for byte and the two headers — and nothing in it is believed
     * until the signature is checked against the secret.
     *
     * <p>The word only names what it is about; ask the gateway what became of
     * it before acting on it. Answer with any 2xx once the word is taken; the
     * gateway tries again, up to five times, until it hears one.
     *
     * @throws SignatureException when the signature does not hold.
     */
    public com.odemehub.response.Webhook webhook(String method, String path, byte[] payload, String timestamp, String signature) {
        if (!verifyWebhook(method, path, payload, timestamp, signature)) {
            throw new SignatureException("Bildirimin imzası doğrulanamadı; bildirim ödeme geçidinden gelmemiş olabilir.");
        }

        return com.odemehub.response.Webhook.fromBody(Transport.decode(new String(payload, StandardCharsets.UTF_8), 0));
    }

    public com.odemehub.response.Webhook webhook(String method, String path, String payload, String timestamp, String signature) {
        return webhook(method, path, payload.getBytes(StandardCharsets.UTF_8), timestamp, signature);
    }

    /**
     * Whether a word that arrived at a webhook address was signed by the
     * gateway with this team's secret, recently enough to be taken. The path
     * is the address's own, with its leading slash and without the query
     * string; the body is the raw bytes as they arrived.
     */
    public boolean verifyWebhook(String method, String path, byte[] payload, String timestamp, String signature) {
        return this.signature.verify(method, path, payload, timestamp, signature);
    }

    public boolean verifyWebhook(String method, String path, String payload, String timestamp, String signature) {
        return verifyWebhook(method, path, payload.getBytes(StandardCharsets.UTF_8), timestamp, signature);
    }

    /**
     * Hand a message to this team's gateway and read the answer back, signed
     * and checked with the team's secret.
     */
    private JsonNode send(Message message) {
        return transport.send(message, options.path(message.path()), options.url(message.path()));
    }
}
