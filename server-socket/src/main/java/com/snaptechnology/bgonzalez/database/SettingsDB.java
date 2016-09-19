package com.snaptechnology.bgonzalez.database;

enum SettingsDB {
    DRIVER("org.postgresql.Driver"),
    URL("jdbc:postgresql://localhost:5432/Book My Room"),
    USERNAME("postgres"),
    PASSWORD("postgres");

    /**
     * Created by bgonzalez on 06/09/2016.
     */
    private final String value;

    SettingsDB(String value) {
        this.value = value; }

    String getValue() {
        return value; }
}