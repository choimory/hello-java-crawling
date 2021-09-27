package com.choimory.hellojavacrawling.common.webhook.slack;

import lombok.Builder;

@Builder
public class SlackDto {
    private final String channel;
    private final String username;
    private final String icon_url;
    private final String icon_emoji;
    private final String text;
}
