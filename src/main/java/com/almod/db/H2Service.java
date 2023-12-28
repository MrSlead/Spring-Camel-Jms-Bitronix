package com.almod.db;

import java.io.InputStream;

public interface H2Service {
    void writeInDB(InputStream inputStream);
    void showDataDB();
}
