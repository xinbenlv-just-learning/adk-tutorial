// The example requires the Google GData Client library,
// which in turn requires the
// Google Collection library. These can be downloaded from
// http://code.google.com/p/gdata-java-client/downloads/list and
// http://code.google.com/p/google-collections/downloads/list.

import com.google.gdata.client.ClientLoginAccountType;
import com.google.gdata.client.GoogleService;
import com.google.gdata.client.Service.GDataRequest;
import com.google.gdata.client.Service.GDataRequest.RequestType;

import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ContentType;
import com.google.gdata.util.ServiceException;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

/**
 * Java example using the Google Fusion Tables API
 * to query, insert, update, and delete.
 * Uses the Google GDATA core library.
 *
 * @author googletables-feedback@google.com (Google Fusion Tables Team)
 */
public class ApiExample {

  /**
   * Google Fusion Tables API URL stem.
   * All requests to the Google Fusion Tables server
   * begin with this URL.
   *
   * The next line is Google Fusion Tables API-specific code:
   */
  private static final String SERVICE_URL =
      "https://www.google.com/fusiontables/api/query";

  /**
   * CSV values are terminated by comma or end-of-line and consist either of
   * plain text without commas or quotes, or a quoted expression, where inner
   * quotes are escaped by doubling.
   */
  private static final Pattern CSV_VALUE_PATTERN =
      Pattern.compile("([^,\\r\\n\"]*|\"(([^\"]*\"\")*[^\"]*)\")(,|\\r?\\n)");

  /**
   * Handle to the authenticated Google Fusion Tables service.
   *
   * This code uses the GoogleService class from the
   * Google GData APIs Client Library.
   */
  private GoogleService service;

  /**
   * Two versions of ApiExample() are provided:
   * one that accepts a Google user account ID and password for authentication,
   * and one that accepts an existing auth token.
   */

  /**
   * Authenticates the given account for {@code fusiontables} service using a
   * given email ID and password.
   *
   * @param email    Google account email. (For more information, see
   *                 http://www.google.com/support/accounts.)
   * @param password Password for the given Google account.
   *
   * This code instantiates the GoogleService class from the
   * Google GData APIs Client Library,
   * passing in Google Fusion Tables API-specific parameters.
   * It then goes back to the Google GData APIs Client Library for the
   * setUserCredentials() method.
   */
  public ApiExample(String email, String password)
      throws AuthenticationException {
    service = new GoogleService("fusiontables", "fusiontables.ApiExample");
    service.setUserCredentials(email, password, ClientLoginAccountType.GOOGLE);
  }

  /**
   * Authenticates for {@code fusiontables} service using the auth token. The
   * auth token can be retrieved for an authenticated user by invoking
   * service.getAuthToken() on the email and password. The auth token can be
   * reused rather than specifying the user name and password repeatedly.
   *
   * @param authToken The auth token. (For more information, see
   *                  http://code.google.com/apis/gdata/auth.html#ClientLogin.)
   *
   * @throws AuthenticationException
   *
   * This code instantiates the GoogleService class from the
   * Google Data APIs Client Library,
   * passing in Google Fusion Tables API-specific parameters.
   * It then goes back to the Google Data APIs Client Library for the
   * setUserToken() method.
   */
  public ApiExample(String authToken) throws AuthenticationException {
    service = new GoogleService("fusiontables", "fusiontables.ApiExample");
    service.setUserToken(authToken);
  }

  /**
   * Fetches the results for a select query. Prints them to standard
   * output, surrounding every field with (@code |}.
   *
   * This code uses the GDataRequest class and getRequestFactory() method
   * from the Google Data APIs Client Library.
   * The Google Fusion Tables API-specific part is in the construction
   * of the service URL. A Google Fusion Tables API SELECT statement
   * will be passed in to this method in the selectQuery parameter.
   */
  public void runSelect(String selectQuery) throws IOException,
      ServiceException {
    URL url = new URL(
        SERVICE_URL + "?sql=" + URLEncoder.encode(selectQuery, "UTF-8"));
    GDataRequest request = service.getRequestFactory().getRequest(
            RequestType.QUERY, url, ContentType.TEXT_PLAIN);

    request.execute();

  /* Prints the results of the query.                */
  /* No Google Fusion Tables API-specific code here. */

    Scanner scanner = new Scanner(request.getResponseStream(),"UTF-8");
    while (scanner.hasNextLine()) {
      scanner.findWithinHorizon(CSV_VALUE_PATTERN, 0);
      MatchResult match = scanner.match();
      String quotedString = match.group(2);
      String decoded = quotedString == null ? match.group(1)
          : quotedString.replaceAll("\"\"", "\"");
      System.out.print("|" + decoded);
      if (!match.group(4).equals(",")) {
        System.out.println("|");
      }
    }
  }

  /**
   * Executes insert, update, and delete statements.
   * Prints out results, if any.
   *
   * This code uses the GDataRequest class and getRequestFactory() method
   * from the Google Data APIs Client Library to construct a POST request.
   * The Google Fusion Tables API-specific part is in the use
   * of the service URL. A Google Fusion Tables API INSERT, UPDATE,
   * or DELETE statement will be passed into this method in the
   * updateQuery parameter.
   */

  public void runUpdate(String updateQuery) throws IOException,
      ServiceException {
    URL url = new URL(SERVICE_URL);
    GDataRequest request = service.getRequestFactory().getRequest(
        RequestType.INSERT, url,
        new ContentType("application/x-www-form-urlencoded"));
    OutputStreamWriter writer =
        new OutputStreamWriter(request.getRequestStream());
    writer.append("sql=" + URLEncoder.encode(updateQuery, "UTF-8"));
    writer.flush();

    request.execute();

  /* Prints the results of the statement.            */
  /* No Google Fusion Tables API-specific code here. */

    Scanner scanner = new Scanner(request.getResponseStream(),"UTF-8");
    while (scanner.hasNextLine()) {
      scanner.findWithinHorizon(CSV_VALUE_PATTERN, 0);
      MatchResult match = scanner.match();
      String quotedString = match.group(2);
      String decoded = quotedString == null ? match.group(1)
          : quotedString.replaceAll("\"\"", "\"");
      System.out.print("|" + decoded);
      if (!match.group(4).equals(",")) {
        System.out.println("|");
      }
    }
  }

  /**
   *
   * Authorizes the user with either a Google Account email and password
   * or auth token, then exercises runSelect() and runUpdate() with some
   * hard-coded Google Fusion Tables API statements.
   *
   */
  public static void main(String[] args) throws ServiceException, IOException {
    if (args.length == 0) {
      System.err.println("Usage java ApiExample <email> <password> OR " +
       "java ApiExample <auth-token>");
      System.exit(1);
    }
    ApiExample example = (args.length == 1) ?
    //For simplicity, you can hardcode your example account's Username and Password here, if you are using eclipse
        new ApiExample(args[0]) : new ApiExample(args[0], args[1]);

    // Example select queries. These are queries over real public tables.
    // What was the price of electricity in California in different years?
    example.runSelect("select Year, Price from 57407 where State = 'California'");
    // What was the average price across all states in different years?
    example.runSelect("select Year, average(Price) from 57407 group by Year");
    // What were the top names for boys in years that Mary was the top name for girls?
    example.runSelect("select 'Male name', year from 1219 where rank = 1 and " +
        "'Female name' = 'Mary'");

    // Example update queries.
    // These examples use an imaginary table of paintings and artists.
    // To create working examples, substitute your own tables and data.

    // An incorrect entry is added for the Mona Lisa.
    example.runUpdate("insert into 123456 (painting, artist) values " +
        "('mona lisa', 'michelangelo')");
    // To correct the entry, we first retrieve its row ID.
    example.runSelect("select rowid from 123456 where painting = 'mona lisa'");
    // Update the entry. It is assumed that the previous statement
    // returned a row ID of 25.
    example.runUpdate("update 123456 set artist = 'da vinci' where rowid = '25'");
    // The entry is deleted.
    example.runUpdate("delete from 123456 where rowid = '25'");
  }
}