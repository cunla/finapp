package com.delirium.finapp.images;

import com.delirium.finapp.auditing.AbstractAuditingEntity;
import com.delirium.finapp.exceptions.FinappUrlException;

import javax.persistence.*;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by style on 22/12/2015.
 */
@Entity
@Table(name = "IMAGES")
public class FinImage extends AbstractAuditingEntity {
    @Id
    @Column(name = "IMAGE_ID")
    @GeneratedValue
    private Long id;

    @Lob
    @Column(name = "IMAGE_DATA", columnDefinition = "blob")
    private byte[] data;

    public FinImage() {
        super();
    }

    public FinImage(byte[] data) {
        this();
        this.data = data;
    }

    public static FinImage createFromUrl(String sUrl) throws FinappUrlException {
        try {
            URL url = new URL(sUrl);
            InputStream in = new BufferedInputStream(url.openStream());
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int n = 0;
            while (-1 != (n = in.read(buf))) {
                out.write(buf, 0, n);
            }
            out.close();
            in.close();
            byte[] response = out.toByteArray();
            return new FinImage(response);
        } catch (MalformedURLException e) {
            throw new FinappUrlException("Url is malformed", sUrl, e);
        } catch (IOException e) {
            throw new FinappUrlException("Couldn't get URL", sUrl, e);
        }
    }

    public Long getId() {
        return id;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
