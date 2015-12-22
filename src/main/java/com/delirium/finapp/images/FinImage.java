package com.delirium.finapp.images;

import com.delirium.finapp.auditing.AbstractAuditingEntity;
import com.delirium.finapp.exceptions.FinappUrlException;

import javax.persistence.*;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

    @Column(name = "IMAGE_DATA")
    private byte[] data;

    public FinImage() {

    }

    public FinImage(byte[] data) {
        this.data = data;
    }

    public static FinImage createFromUrl(URL url) throws FinappUrlException {
        try {
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
        } catch (IOException e) {
            throw new FinappUrlException("Couldn't get URL", url, e);
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
