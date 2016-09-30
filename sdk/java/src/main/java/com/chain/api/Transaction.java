package com.chain.api;

import com.chain.exception.*;
import com.chain.http.Context;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import java.util.*;

/**
 * A single transaction on a Chain Core.
 */
public class Transaction {
  /**
   * Height of the block containing a transaction.
   */
  @SerializedName("block_height")
  public int blockHeight;

  /**
   * Unique identifier, or block hash, of the block containing a transaction.
   */
  @SerializedName("block_id")
  public String blockId;

  /**
   * Unique identifier, or transaction hash, of a transaction.
   */
  public String id;

  /**
   * List of specified inputs for a transaction.
   */
  public List<Input> inputs;

  /**
   * List of specified outputs for a transaction.
   */
  public List<Output> outputs;

  /**
   * Position of a transaction within the block.
   */
  public int position;

  /**
   * Time of transaction.
   */
  public Date timestamp;

  /**
   * User specified, unstructured data embedded within a transaction.
   */
  @SerializedName("reference_data")
  public Map<String, Object> referenceData;

  /**
   * A flag indicating one or more inputs or outputs are local.
   * Possible values are "yes" or "no".
   */
  @SerializedName("is_local")
  public String isLocal;

  /**
   * Paged results of a transaction query.
   */
  public static class Items extends PagedItems<Transaction> {
    /**
     * Returns a new page of transactions based on the underlying query.
     * @return a page of transactions
     * @throws APIException This exception is raised if the api returns errors while processing the query.
     * @throws BadURLException This exception wraps java.net.MalformedURLException.
     * @throws ConnectivityException This exception is raised if there are connectivity issues with the server.
     * @throws HTTPException This exception is raised when errors occur making http requests.
     * @throws JSONException This exception is raised due to malformed json requests or responses.
     */
    public Items getPage() throws ChainException {
      Items items = this.context.request("list-transactions", this.next, Items.class);
      items.setContext(this.context);
      return items;
    }
  }

  /**
   * A builder class for transaction queries.
   */
  public static class QueryBuilder extends BaseQueryBuilder<QueryBuilder> {
    /**
     * Executes a transaction query based on provided parameters.
     * @param ctx context object which makes server requests
     * @return a page of transactions
     * @throws APIException This exception is raised if the api returns errors while processing the query.
     * @throws BadURLException This exception wraps java.net.MalformedURLException.
     * @throws ConnectivityException This exception is raised if there are connectivity issues with the server.
     * @throws HTTPException This exception is raised when errors occur making http requests.
     * @throws JSONException This exception is raised due to malformed json requests or responses.
     */
    public Items execute(Context ctx) throws ChainException {
      Items items = new Items();
      items.setContext(ctx);
      items.setNext(this.next);
      return items.getPage();
    }

    /**
     * Sets the earliest transaction timestamp to include in results
     * @param time start time in UTC format
     * @return updated QueryBuilder object
     */
    public QueryBuilder setStartTime(long time) {
      this.next.startTime = time;
      return this;
    }

    /**
     * Sets the latest transaction timestamp to include in results
     * @param time end time in UTC format
     * @return updated QueryBuilder object
     */
    public QueryBuilder setEndTime(long time) {
      this.next.endTime = time;
      return this;
    }

    /**
     * Sets the order of this query to ascending ("asc") to facilitate
     * notifications.
     * @return updated QueryBuilder object
     */
    public QueryBuilder setAscending() {
      this.next.order = "asc";
      return this;
    }

    /**
     * Sets a timeout on this query.
     * @param timeoutMS timeout in milliseconds
     * @return updated QueryBuilder object
     */
    public QueryBuilder setTimeout(long timeoutMS) {
      this.next.timeout = timeoutMS;
      return this;
    }
  }

  /**
   * A single input included in a transaction.
   */
  public static class Input {
    /**
     * The type of action being taken on an input.<br>
     * Possible actions are "issue", "spend_account", and "spend_account_unspent_output".
     */
    public String action;

    /**
     * The number of units of the asset being issued or spent.
     */
    public long amount;

    /**
     * The id of the asset being issued or spent.
     */
    @SerializedName("asset_id")
    public String assetId;

    /**
     * The id of the account transferring the asset (possibly null if the input is an issuance or an unspent output is specified).
     */
    @SerializedName("account_id")
    public String accountId;

    /**
     * The tags associated with the account (possibly null).
     */
    @SerializedName("account_tags")
    public Map<String, Object> accountTags;

    /**
     * The tags associated with the asset (possibly null).
     */
    @SerializedName("asset_tags")
    public Map<String, Object> assetTags;

    /**
     * A flag indicating if the asset is locally controlled.
     * Possible values are "yes" or "no".
     */
    @SerializedName("asset_is_local")
    public String assetIsLocal;

    /**
     * Inputs to the control program used to verify the ability to take the specified action (possibly null).
     */
    @SerializedName("input_witness")
    public String[] inputWitness;

    /**
     * A program specifying a predicate for issuing an asset (possibly null if input is not an issuance).
     */
    @SerializedName("issuance_program")
    public String issuanceProgram;

    /**
     * User specified, unstructured data embedded within an input (possibly null).
     */
    @SerializedName("reference_data")
    public Map<String, Object> referenceData;

    /**
     * A flag indicating if the input is local.
     * Possible values are "yes" or "no".
     */
    @SerializedName("is_local")
    public String isLocal;

  }

  /**
   * A single output included in a transaction.
   */
  public static class Output {
    /**
     * The type of action being taken on the output.<br>
     * Possible actions are "control_account", "control_program", and "retire".
     */
    public String action;

    /**
     * The purpose of the output.<br>
     * Possible purposes are "receive" and "change". Only populated if the
     * output's control program was generated locally.
     */
    public String purpose;

    /**
     * The number of units of the asset being controlled.
     */
    public long amount;

    /**
     * The id of the asset being controlled.
     */
    @SerializedName("asset_id")
    public String assetId;

    /**
     * The control program which must be satisfied to transfer this output.
     */
    @SerializedName("control_program")
    public String controlProgram;

    /**
     * The output's position in a transaction's list of outputs
     */
    public int position;

    /**
     * The id of the account controlling this output (possibly null if a control program is specified).
     */
    @SerializedName("account_id")
    public String accountId;

    /**
     * The tags associated with the account controlling this output (possibly null if a control program is specified).
     */
    @SerializedName("account_tags")
    public Map<String, Object> accountTags;

    /**
     * The tags associated with the asset being controlled.
     */
    @SerializedName("asset_tags")
    public Map<String, Object> assetTags;

    /**
     * A flag indicating if the asset is locally controlled.
     * Possible values are "yes" or "no".
     */
    @SerializedName("asset_is_local")
    public String assetIsLocal;

    /**
     * User specified, unstructured data embedded within an input (possibly null).
     */
    @SerializedName("reference_data")
    public Map<String, Object> referenceData;

    /**
     * A flag indicating if the output is local.
     * Possible values are "yes" or "no".
     */
    @SerializedName("is_local")
    public String isLocal;
  }

  /**
   * A built transaction that has not been submitted for block inclusion (returned from {@link Transaction#buildBatch(Context, List)}).
   */
  public static class Template {
    /**
     * A hex-encoded representation of a transaction template.
     */
    @SerializedName("raw_transaction")
    public String rawTransaction;

    /**
     * The list of signing instructions for inputs in the transaction.
     */
    @SerializedName("signing_instructions")
    public List<SigningInstruction> signingInstructions;

    /**
     * For core use only.
     */
    private Boolean local;

    /**
     * False (the default) makes the transaction "final" when signing,
     * preventing further changes - the signature program commits to
     * the transaction's signature hash.  True makes the transaction
     * extensible, committing only to the elements in the transaction
     * so far, permitting the addition of new elements.
     */
    @SerializedName("allow_additional_actions")
    public Boolean allowAdditionalActions;

    /**
     * The Chain error code.
     */
    public String code;

    /**
     * The Chain error message.
     */
    public String message;

    /**
     * Additional details about the error.
     */
    public String detail;

    /**
     * A single signing instruction included in a transaction template.
     */
    public static class SigningInstruction {
      /**
       * The id of the asset being issued or spent.
       */
      @SerializedName("asset_id")
      public String assetID;

      /**
       * The number of units of the asset being issued or spent.
       */
      public long amount;

      /**
       * The input's position in a transaction's list of inputs.
       */
      public int position;

      /**
       * A list of components used to coordinate the signing of an input.
       */
      @SerializedName("witness_components")
      public WitnessComponent[] witnessComponents;
    }

    /**
     * A single witness component, holding information that will become the input witness.
     */
    public static class WitnessComponent {
      /**
       * The type of witness component.<br>
       * Possible types are "data" and "signature".
       */
      public String type;

      /**
       * Data to be included in the input witness (null unless type is "data").
       */
      public String data;

      /**
       * The number of signatures required for an input (null unless type is "signature").
       */
      public int quorum;

      /**
       * The list of keys to sign with (null unless type is "signature").
       */
      public KeyID[] keys;

      /**
       * The program whose hash is signed. If empty, it is
       * inferred during signing from aspects of the
       * transaction.
       */
      public String program;

      /**
       * The list of signatures made with the specified keys (null unless type is "signature").
       */
      public String[] signatures;
    }

    /**
     * A class representing a derived signing key.
     */
    public static class KeyID {
      /**
       * The extended public key associated with the private key used to sign.
       */
      public String xpub;

      /**
       * The derivation path of the extended public key.
       */
      @SerializedName("derivation_path")
      public String[] derivationPath;
    }
  }

  /**
   * A single response from a call to {@link Transaction#submitBatch(Context, List)}
   */
  public static class SubmitResponse {
    /**
     * The transaction id.
     */
    public String id;

    /**
     * The Chain error code.
     */
    public String code;

    /**
     * The Chain error message.
     */
    public String message;

    /**
     * Additional details about the error.
     */
    public String detail;
  }

  /**
   * Builds a batch of transaction templates.
   * @param ctx context object which makes server requests
   * @param builders list of transaction builders
   * @return a list of transaction templates
   * @throws APIException This exception is raised if the api returns errors while building transaction templates.
   * @throws BadURLException This exception wraps java.net.MalformedURLException.
   * @throws ConnectivityException This exception is raised if there are connectivity issues with the server.
   * @throws HTTPException This exception is raised when errors occur making http requests.
   * @throws JSONException This exception is raised due to malformed json requests or responses.
   */
  public static List<Template> buildBatch(Context ctx, List<Transaction.Builder> builders)
      throws ChainException {
    Type type = new TypeToken<ArrayList<Template>>() {}.getType();
    return ctx.request("build-transaction", builders, type);
  }

  /**
   * Submits a batch of signed transaction templates for inclusion into a block.
   * @param ctx context object which makes server requests
   * @param templates list of transaction templates
   * @return a list of submit responses (individual objects can hold transaction ids or error info)
   * @throws APIException This exception is raised if the api returns errors while submitting transactions.
   * @throws BadURLException This exception wraps java.net.MalformedURLException.
   * @throws ConnectivityException This exception is raised if there are connectivity issues with the server.
   * @throws HTTPException This exception is raised when errors occur making http requests.
   * @throws JSONException This exception is raised due to malformed json requests or responses.
   */
  public static List<SubmitResponse> submitBatch(Context ctx, List<Template> templates)
      throws ChainException {
    Type type = new TypeToken<ArrayList<SubmitResponse>>() {}.getType();
    HashMap<String, Object> requestBody = new HashMap<>();
    requestBody.put("transactions", templates);
    return ctx.request("submit-transaction", requestBody, type);
  }

  /**
   * Submits signed transaction template for inclusion into a block.
   * @param ctx context object which makes server requests
   * @param template transaction template
   * @return submit responses
   * @throws APIException This exception is raised if the api returns errors while submitting a transaction.
   * @throws BadURLException This exception wraps java.net.MalformedURLException.
   * @throws ConnectivityException This exception is raised if there are connectivity issues with the server.
   * @throws HTTPException This exception is raised when errors occur making http requests.
   * @throws JSONException This exception is raised due to malformed json requests or responses.
   */
  public static SubmitResponse submit(Context ctx, Template template) throws ChainException {
    List<SubmitResponse> responses = submitBatch(ctx, Arrays.asList(template));
    SubmitResponse response = responses.get(0);
    if (response.code != null) {
      throw new APIException(response.code, response.message, response.detail, null);
    }
    return response;
  }

  /**
   * Base class representing actions that can be taken within a transaction.
   */
  public static class Action extends HashMap<String, Object> {
    /**
     * Default constructor initializes list and sets the client token.
     */
    public Action() {
      // Several action types require client_token as an idempotency key.
      // It's safest to include a default value for this param.
      this.put("client_token", UUID.randomUUID().toString());
    }

    /**
     * Adds a k,v pair to the action's reference data object.<br>
     * Since most/all current action types use the reference_data parameter, we provide this method in the base class to avoid repetition.
     * @param key key of the reference data field
     * @param value value of reference data field
     * @return updated action object
     */
    public Action addReferenceDataField(String key, Object value) {
      Map<String, Object> referenceData = (HashMap<String, Object>) this.get("reference_data");
      if (referenceData == null) {
        referenceData = new HashMap<>();
        this.put("reference_data", referenceData);
      }
      referenceData.put(key, value);
      return this;
    }

    /**
     * Specifies the reference data to associate with the action
     * Since most/all current action types use the reference_data parameter, we provide this method in the base class to avoid repetition.
     * @param referenceData reference data to embed into the action
     * @return updated action object
     */
    public Action setReferenceData(Map<String, Object> referenceData) {
      this.put("reference_data", referenceData);
      return this;
    }

    /**
     * Represents an issuance action.
     */
    public static class Issue extends Action {
      /**
       * Default constructor defines the action type as "issue"
       */
      public Issue() {
        this.put("type", "issue");
      }

      /**
       * Specifies the asset to be issued using its alias
       * @param alias alias of the asset to be issued
       * @return updated action object
       */
      public Issue setAssetAlias(String alias) {
        this.put("asset_alias", alias);
        return this;
      }

      /**
       * Specifies the asset to be issued using its id
       * @param id id of the asset to be issued
       * @return updated action object
       */
      public Issue setAssetId(String id) {
        this.put("asset_id", id);
        return this;
      }

      /**
       * Specifies the amount of the asset to be issued
       * @param amount number of units of the asset to be issued
       * @return updated action object
       */
      public Issue setAmount(long amount) {
        this.put("amount", amount);
        return this;
      }
    }

    /**
     * Represents a spend action taken on a particular unspent output.
     */
    public static class SpendAccountUnspentOutput extends Action {
      /**
       * Default constructor defines the action type as "spend_account_unspent_output"
       */
      public SpendAccountUnspentOutput() {
        this.put("type", "spend_account_unspent_output");
      }

      /**
       * Specifies the unspent output to be spent
       * @param unspentOutput unspent output to be spent
       * @return updated action object
       */
      public SpendAccountUnspentOutput setUnspentOutput(UnspentOutput unspentOutput) {
        this.put("transaction_id", unspentOutput.transactionId);
        this.put("position", unspentOutput.position);
        return this;
      }

      /**
       * Specifies a list of unspent outputs to be spent
       * @param unspentOutputs unspent outputs to be spent
       * @return updated action object
       */
      public SpendAccountUnspentOutput spendUnspentOutputs(List<UnspentOutput> unspentOutputs) {
        for (UnspentOutput unspentOutput : unspentOutputs) {
          this.setUnspentOutput(unspentOutput);
        }
        return this;
      }
    }

    /**
     * Represents a spend action taken on a particular account.
     */
    public static class SpendFromAccount extends Action {
      /**
       * Default constructor defines the action type as "spend_account"
       */
      public SpendFromAccount() {
        this.put("type", "spend_account");
      }

      /**
       * Specifies the spending account using its alias.<br>
       * <strong>Must</strong> be used with {@link SpendFromAccount#setAssetAlias(String)}
       * @param alias alias of the spending account
       * @return updated action object
       */
      public SpendFromAccount setAccountAlias(String alias) {
        this.put("account_alias", alias);
        return this;
      }

      /**
       * Specifies the spending account using its id.<br>
       * <strong>Must</strong> be used with {@link SpendFromAccount#setAssetId(String)}
       * @param id id of the spending account
       * @return updated action object
       */
      public SpendFromAccount setAccountId(String id) {
        this.put("account_id", id);
        return this;
      }

      /**
       * Specifies the asset to be spent using its alias
       * @param alias alias of the asset to be spent
       * <strong>Must</strong> be used with {@link SpendFromAccount#setAccountAlias(String)}}
       * @return updated action object
       */
      public SpendFromAccount setAssetAlias(String alias) {
        this.put("asset_alias", alias);
        return this;
      }

      /**
       * Specifies the asset to be spent using its id
       * @param id id of the asset to be spent
       * <strong>Must</strong> be used with {@link SpendFromAccount#setAccountId(String)}
       * @return updated action object
       */
      public SpendFromAccount setAssetId(String id) {
        this.put("asset_id", id);
        return this;
      }

      /**
       * Specifies the amount of asset to be spent
       * @param amount number of units of the asset to be spent
       * @return updated action object
       */
      public SpendFromAccount setAmount(long amount) {
        this.put("amount", amount);
        return this;
      }
    }

    /**
     * Represents a control action taken on a particular account.
     */
    public static class ControlWithAccount extends Action {
      /**
       * Default constructor defines the action type as "control_account"
       */
      public ControlWithAccount() {
        this.put("type", "control_account");
      }

      /**
       * Specifies the controlling account using its alias.<br>
       * <strong>Must</strong> be used with {@link ControlWithAccount#setAssetAlias(String)}
       * @param alias alias of the controlling account
       * @return updated action object
       */
      public ControlWithAccount setAccountAlias(String alias) {
        this.put("account_alias", alias);
        return this;
      }

      /**
       * Specifies the controlling account using its id.<br>
       * <strong>Must</strong> be used with {@link ControlWithAccount#setAssetId(String)}
       * @param id id of the controlling account
       * @return updated action object
       */
      public ControlWithAccount setAccountId(String id) {
        this.put("account_id", id);
        return this;
      }

      /**
       * Specifies the asset to be controlled using its alias.<br>
       * <strong>Must</strong> be used with {@link ControlWithAccount#setAccountAlias(String)}
       * @param alias alias of the asset to be controlled
       * @return updated action object
       */
      public ControlWithAccount setAssetAlias(String alias) {
        this.put("asset_alias", alias);
        return this;
      }

      /**
       * Specifies the asset to be controlled using its id.<br>
       * <strong>Must</strong> be used with {@link ControlWithAccount#setAccountId(String)}
       * @param id id of the asset to be controlled
       * @return updated action object
       */
      public ControlWithAccount setAssetId(String id) {
        this.put("asset_id", id);
        return this;
      }

      /**
       * Specifies the amount of the asset to be controlled.
       * @param amount number of units of the asset to be controlled
       * @return updated action object
       */
      public ControlWithAccount setAmount(long amount) {
        this.put("amount", amount);
        return this;
      }
    }

    /**
     * Represents a control action taken on a control program.
     */
    public static class ControlWithProgram extends Action {
      /**
       * Default constructor defines the action type as "control_program"
       */
      public ControlWithProgram() {
        this.put("type", "control_program");
      }

      /**
       * Specifies the control program to be used.
       * @param controlProgram the control program to be used
       * @return updated action object
       */
      public ControlWithProgram setControlProgram(String controlProgram) {
        this.put("control_program", controlProgram);
        return this;
      }

      /**
       * Specifies the asset to be controlled using its alias
       * @param alias alias of the asset to be controlled
       * @return updated action object
       */
      public ControlWithProgram setAssetAlias(String alias) {
        this.put("asset_alias", alias);
        return this;
      }

      /**
       * Specifies the asset to be controlled using its id
       * @param id id of the asset to be controlled
       * @return updated action object
       */
      public ControlWithProgram setAssetId(String id) {
        this.put("asset_id", id);
        return this;
      }

      /**
       * Specifies the amount of the asset to be controlled.
       * @param amount number of units of the asset to be controlled
       * @return updated action object
       */
      public ControlWithProgram setAmount(long amount) {
        this.put("amount", amount);
        return this;
      }
    }

    /**
     * Represents a retire action.
     */
    public static class Retire extends Action {
      /**
       * Default constructor defines the action type as "control_program"
       */
      public Retire() {
        this.put("type", "control_program");
        this.put("control_program", ControlProgram.retireProgram());
      }

      /**
       * Specifies the amount of the asset to be retired.
       * @param amount number of units of the asset to be retired
       * @return updated action object
       */
      public Retire setAmount(long amount) {
        this.put("amount", amount);
        return this;
      }

      /**
       * Specifies the asset to be retired using its alias
       * @param alias alias of the asset to be retired
       * @return updated action object
       */
      public Retire setAssetAlias(String alias) {
        this.put("asset_alias", alias);
        return this;
      }

      /**
       * Specifies the asset to be retired using its id
       * @param id id of the asset to be retired
       * @return updated action object
       */
      public Retire setAssetId(String id) {
        this.put("asset_id", id);
        return this;
      }
    }

    /**
     * Sets a k,v parameter pair.
     * @param key the key on the parameter object
     * @param value the corresponding value
     * @return updated action object
     */
    public Action setParameter(String key, Object value) {
      this.put(key, value);
      return this;
    }
  }

  /**
   * A builder class for transaction templates.
   */
  public static class Builder {
    /**
     * Hex-encoded serialization of a transaction to add to the current template.
     */
    @SerializedName("base_transaction")
    private String baseTransaction;

    /**
     * List of actions in a transaction.
     */
    private List<Action> actions;

    /**
     * User specified, unstructured data embedded at the top level of the transaction.
     */
    @SerializedName("reference_data")
    private Map<String, Object> referenceData;

    /**
     * A time duration in milliseconds. If the transaction is not fully
     * signed and submitted within this time, it will be rejected by the
     * blockchain. Additionally, any outputs reserved when building this
     * transaction will remain reserved for this duration.
     */
    private long ttl;

    /**
     * Builds a single transaction template.
     * @param ctx context object which makes requests to the server
     * @return a transaction template
     * @throws APIException This exception is raised if the api returns errors while building the transaction.
     * @throws BadURLException This exception wraps java.net.MalformedURLException.
     * @throws ConnectivityException This exception is raised if there are connectivity issues with the server.
     * @throws HTTPException This exception is raised when errors occur making http requests.
     * @throws JSONException This exception is raised due to malformed json requests or responses.
     */
    public Template build(Context ctx) throws ChainException {
      return ctx.singletonBatchRequest("build-transaction", this, Template.class);
    }

    /**
     * Default constructor initializes actions list.
     */
    public Builder() {
      this.actions = new ArrayList<>();
    }

    /**
     * Sets the baseTransaction field and initializes the actions lists.<br>
     * This constructor can be used when executing an atomic swap and the counter party has sent an initialized tx template.
     */
    public Builder(String rawTransaction) {
      this.baseTransaction = rawTransaction;
      this.actions = new ArrayList<>();
    }

    /**
     * Sets the rawTransaction that will be added to the current template.
     */
    public Builder setBaseTransaction(String rawTransaction) {
      this.baseTransaction = rawTransaction;
      return this;
    }

    /**
     * Adds an action to a transaction builder.
     * @param action action to add
     * @return updated builder object
     */
    public Builder addAction(Action action) {
      this.actions.add(action);
      return this;
    }

    /**
     * Adds a k,v pair to the transaction's reference data object
     * @param key key of the reference data field
     * @param value value of reference data field
     */
    public Builder addReferenceDataField(String key, Object value) {
      if (this.referenceData == null) {
        this.referenceData = new HashMap<>();
      }
      this.referenceData.put(key, value);
      return this;
    }

    /**
     * Sets a transaction's reference data.
     * @param referenceData info to embed into a transaction.
     * @return updated builder object
     */
    public Builder setReferenceData(Map<String, Object> referenceData) {
      this.referenceData = referenceData;
      return this;
    }

    /**
     * Sets a transaction's time-to-live, which indicates how long outputs
     * will be reserved for, and how long the transaction will remain valid.
     * Passing zero will use the default TTL, which is 300000ms (5 minutes).
     * @param ms the duration of the TTL, in milliseconds.
     * @return updated builder object
     */
    public Builder setTtl(long ms) {
      this.ttl = ms;
      return this;
    }
  }

  /**
   * When used in conjunction with /list-transactions, Consumers can be used to
   * receive notifications about transactions.
   */
  public static class Consumer {
    /**
     * Consumer ID, automatically generated when a consumer is created.
     */
    public String id;

    /**
     * An optional, user-supplied alias that can be used to uniquely identify
     * this consumer.
     */
    public String alias;

    /**
     * The query filter used in /list-transactions.
     */
    public String filter;

    /**
     * The direction ("asc" or "desc") that this consumer moves through the
     * transaction list. Only "asc" (oldest transactions first) is supported
     * currently.
     */
    public String order;

    /**
     * Indicates the last transaction consumed by this consumer.
     */
    public String after;

    /**
     * Creates a consumer.
     *
     * @param ctx context object that makes requests to core
     * @param alias an alias which uniquely identifies this consumer
     * @param filter a query filter which identifies which transactions this consumer consumes
     * @return a consumer object
     * @throws ChainException
     */
    public static Consumer create(Context ctx, String alias, String filter) throws ChainException {
      Map<String, Object> req = new HashMap<>();
      req.put("alias", alias);
      req.put("filter", filter);
      req.put("client_token", UUID.randomUUID().toString());
      return ctx.request("create-transaction-consumer", req, Consumer.class);
    }

    /**
     * Retrieves a consumer by ID.
     *
     * @param ctx context object that makes requests to core
     * @param id the consumer id
     * @return a consumer object
     * @throws ChainException
     */
    public static Consumer getByID(Context ctx, String id) throws ChainException {
      Map<String, Object> req = new HashMap<>();
      req.put("id", id);
      return ctx.request("get-consumer", req, Consumer.class);
    }

    /**
     * Retrieves a consumer by alias.
     *
     * @param ctx context object that makes requests to core
     * @param alias the consumer alias
     * @return a consumer object
     * @throws ChainException
     */
    public static Consumer getByAlias(Context ctx, String alias) throws ChainException {
      Map<String, Object> req = new HashMap<>();
      req.put("alias", alias);
      return ctx.request("get-consumer", req, Consumer.class);
    }

    /**
     * Updates a consumer with a new `after`. Consumers can only be updated forwards
     * (i.e., a consumer cannot be updated with a value that is previous to its
     * current value).
     *
     * @param ctx context object that makes requests to core
     * @param after an indicator of the last transaction processed
     * @return a consumer object
     * @throws ChainException
     */
    public Consumer update(Context ctx, String after) throws ChainException {
      Map<String, Object> req = new HashMap<>();
      req.put("id", this.id);
      req.put("previous_after", this.after);
      req.put("after", after);
      return ctx.request("update-consumer", req, Consumer.class);
    }
  }
}
