package com.cursomc.relatorios;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public final class GenerateReportsUtil {

    private GenerateReportsUtil() { }

    private static final String HEADER_CACHE_CONTROL = "Cache-Control";
    private static final String HEADER_CACHE_CONTROL_VALUE = "no-cache, no-store, must-revalidate";
    private static final String HEADER_PRAGA = "Pragma";
    private static final String HEADER_PRAGA_VALUE = "no-cache";
    private static final String HEADER_EXPIRES = "Expires";
    private static final String HEADER_EXPIRES_VALUE = "0";
    private static final String HEADER_CONTENT_DISPOSITION = "Content-Disposition";

    private static HttpHeaders getHttpHeaders(String nome) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HEADER_CACHE_CONTROL, HEADER_CACHE_CONTROL_VALUE);
        headers.add(HEADER_PRAGA, HEADER_PRAGA_VALUE);
        headers.add(HEADER_EXPIRES, HEADER_EXPIRES_VALUE);
        headers.add(HEADER_CONTENT_DISPOSITION, "attachment; filename=" + nome);

        return headers;
    }

    public static ResponseEntity<InputStreamResource> output(byte[] byteArrayOutputStream, HttpHeaders headers) {
        return ResponseEntity.ok().headers(headers).contentLength(byteArrayOutputStream.length)
            .contentType(MediaType.parseMediaType(MediaType.APPLICATION_OCTET_STREAM_VALUE))
            .body(new InputStreamResource(new ByteArrayInputStream(byteArrayOutputStream)));
    }

    public static ResponseEntity<InputStreamResource> output(ByteArrayOutputStream byteArrayOutputStream, String nome) {
        return output(byteArrayOutputStream.toByteArray(), getHttpHeaders(nome));
    }

}
