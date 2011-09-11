//Most of code here comes from Official Google Fusion Api Java Example
package com.example;

import android.util.Log;

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

public class FusionHandler {
	  private static final String TAG = "FusionHandler"; 
	  private static final String SERVICE_URL =
	      "https://www.google.com/fusiontables/api/query";
	  /**
	   * CSV values are terminated by comma or end-of-line and consist either of
	   * plain text without commas or quotes, or a quoted expression, where inner
	   * quotes are escaped by doubling.
	   */
	  private static final Pattern CSV_VALUE_PATTERN =
	      Pattern.compile("([^,\\r\\n\"]*|\"(([^\"]*\"\")*[^\"]*)\")(,|\\r?\\n)");

	  private GoogleService service;
	  public FusionHandler(String email, String password)
      throws AuthenticationException {
	    service = new GoogleService("fusiontables", "fusiontables.FusionHandler");
	    service.setUserCredentials(email, password, ClientLoginAccountType.GOOGLE);
  }

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
      Log.d(TAG,"|" + decoded);
      System.out.print("|" + decoded);
      if (!match.group(4).equals(",")) {
    	Log.d(TAG,"|");
    	System.out.println("|");
      }
    }
  }
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
      Log.d(TAG,"|" + decoded);
      System.out.print("|" + decoded);
      if (!match.group(4).equals(",")) {
        Log.d(TAG,"|");
    	System.out.println("|");
      }
    }
  }
	  
}
