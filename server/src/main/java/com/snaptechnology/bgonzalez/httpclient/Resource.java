package com.snaptechnology.bgonzalez.httpclient;

/**
 * Created by bgonzalez on 12/08/2016.
 */
enum Resource {
    PATH("http://localhost:3000"),
    SINGIN("/signin/"),
    SINGUP("/signup/"),
    API("/api/"),
    FORUMS("/api/forum/all/"),
    FORUM("/api/forum/"),
    FORUM_MINE("/api/forum/mine"),
    TOPICS("/api/topic/all?forum="),
    TOPIC("/api/topic/"),
    TOPIC_CREATE("/api/topic/create/"),
    USER("/api/user/"),
    SEARCH_USER("/api/user/search?q="),
    PROFILE("/settings/profile/"),
    PASSWORD("/settings/password/"),
    NOTIFICATIONS("/settings/notifications/"),
    TAG("/api/tag/"),
    TAG_CREATE("/api/tag/create/"),
    TAGS("/api/tag/all/");

    private final String url;

    Resource(String url) {
        this.url = url; }

    String publish(String id) {
        return url + id + "/publish"; }

    String vote(String id) {
        return url + id + "/vote/"; }

    String getUrl() {
        return url; } }