package org.mitre.openid.connect.web;

import java.util.List;
import java.util.Map;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


public class ResponseEntityBuilder
{
    private int mStatus;
    private HttpHeaders mHeaders;
    private Object mBody;


    public ResponseEntityBuilder()
    {
        mStatus  = HttpStatus.OK.value();
        mHeaders = new HttpHeaders();
    }


    public ResponseEntityBuilder status(int status)
    {
        mStatus = status;

        return this;
    }


    public ResponseEntityBuilder ok()
    {
        return status(HttpStatus.OK.value());
    }


    public ResponseEntityBuilder header(String headerName, String headerValue)
    {
        if (headerName == null)
        {
            return this;
        }

        mHeaders.add(headerName, headerValue);

        return this;
    }


    public ResponseEntityBuilder contentType(String value)
    {
        return header(HttpHeaders.CONTENT_TYPE, value);
    }


    public ResponseEntityBuilder body(Object body)
    {
        mBody = body;

        return this;
    }


    public ResponseEntityBuilder response(Response response)
    {
        if (response == null)
        {
            return this;
        }

        // Replace the HTTP status code.
        mStatus = response.getStatus();

        // Replace the HTTP headers.
        mHeaders = extractHeaders(response);

        // Replace the response body.
        mBody = response.getEntity();

        return this;
    }


    public ResponseEntityBuilder exception(WebApplicationException exception)
    {
        if (exception == null)
        {
            return this;
        }

        return response(exception.getResponse());
    }


    /**
     * Convert HTTP headers of the response into an
     * {@code HttpHeaders} instance.
     */
    private static HttpHeaders extractHeaders(Response response)
    {
        HttpHeaders targetHeaders = new HttpHeaders();
        MultivaluedMap<String, String> sourceHeaders = response.getStringHeaders();

        // If the response does not have any header.
        if (sourceHeaders == null)
        {
            // Return an empty header list.
            return targetHeaders;
        }

        // For each header name
        for (Map.Entry<String, List<String>> entry : sourceHeaders.entrySet())
        {
            // Header name
            String headerName = entry.getKey();

            // Header values
            List<String> headerValues = entry.getValue();

            // If the header name is not valid.
            if (headerName == null || headerName.length() == 0)
            {
                // Ignore the header.
                continue;
            }

            // If header values are not available.
            if (headerValues == null || headerValues.size() == 0)
            {
                // Add the header with a 'null' value.
                targetHeaders.add(headerName, null);
                continue;
            }

            // For each header value
            for (String headerValue : headerValues)
            {
                // Add the pair of the header name and the header value.
                targetHeaders.add(headerName, headerValue);
            }
        }

        return targetHeaders;
    }


    public ResponseEntity<?> build()
    {
        return ResponseEntity.status(mStatus).headers(mHeaders).body(mBody);
    }
}
