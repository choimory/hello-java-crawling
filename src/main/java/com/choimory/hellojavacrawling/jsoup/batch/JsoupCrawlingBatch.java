package com.choimory.hellojavacrawling.jsoup.batch;

import com.choimory.hellojavacrawling.common.batch.BugFixedRunIdIncrementer;
import com.choimory.hellojavacrawling.common.webhook.slack.SlackDto;
import com.choimory.hellojavacrawling.common.webhook.slack.SlackUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class JsoupCrawlingBatch {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    public static final String PREFIX = "JsoupCrawling";
    private final SlackUtil slackUtil;

    @Bean(PREFIX + "Job")
    public Job jsoupCrawlJob(){
        return jobBuilderFactory.get(PREFIX + "Job")
                .incrementer(new BugFixedRunIdIncrementer())
                .start(crawlAndSave(null, null, null))
                .build();
    }

    @Bean(PREFIX + "CrawlStep")
    @JobScope
    public Step crawlAndSave(@Value("#{jobParameters[url]}") String url,
                             @Value("#{jobParameters[priceTag]}") String priceTag,
                             @Value("#{jobParameters[limitPrice]}") String limitPrice){
        return stepBuilderFactory.get(PREFIX + "CrawlStep")
                .tasklet((contribution, chunkContext) -> {
                    /*do crawl*/
                    Integer priceNow = checkPriceNow(url, priceTag);
                    int result = priceNow.compareTo(Integer.parseInt(limitPrice));

                    /*do slack*/
                    if(result == 1){
                        slackUtil.send(SlackDto.builder()
                                .username("Jsoup 크롤링 알람 봇")
                                .text("해당 상품의 가격이 목표가격 이하로 내려갔어요!\r\n" +
                                        "목표가격 : " + limitPrice + "\r\n" +
                                        "현재가격 : " + priceNow)
                                .build());
                    }

                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    private Integer checkPriceNow(String url, String priceTag){
        return null;
    }
}
