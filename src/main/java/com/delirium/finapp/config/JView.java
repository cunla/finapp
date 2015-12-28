package com.delirium.finapp.config;

/**
 * Created by style on 28/12/2015.
 */
public class JView {
    public interface UserSummary {
    }

    public interface UserWithImage extends UserSummary {
    }

    public interface UserWithGroups extends UserWithImage {
    }
}
