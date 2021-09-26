package com.choimory.hellojavacrawling.jsoup.batch;

import com.choimory.hellojavacrawling.common.batch.BugFixedRunIdIncrementer;
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
                    boolean result = checkPriceTag(url, priceTag, Integer.parseInt(limitPrice));

                    /*do slack*/
                    if(result){
                        SlackUtil.doSlack();
                    }

                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    private boolean checkPriceTag(String url, String priceTag, int limitPrice){
        return false;
    }
}
