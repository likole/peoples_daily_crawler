package com.likole;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.List;

public class MyPageProcessor implements PageProcessor {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000).addHeader("cookie", "");

    public void process(Page page) {
        String title = page.getHtml().xpath("//div[@class='text_c']/h1//allText()").get();
        if (title == null) {
            //添加板块中的所有文章
            page.addTargetRequests(page.getHtml().xpath("//div[@id='titleList']").links().all());
            page.setSkip(true);
        } else {
            page.putField("title", title);
            page.putField("subtitle", page.getHtml().xpath("//div[@class='text_c']/h2//allText()").get());
            page.putField("source", page.getHtml().xpath("//div[@class='lai']//allText()").get());
            page.putField("content", page.getHtml().xpath("//div[@class='c_c']//allText()").get());
        }
    }

    public Site getSite() {
        return site;
    }

    private static void addByDate(Spider spider, String date){
        for (int i=1;i<=20;i++){
            spider.addUrl("http://paper.people.com.cn/rmrb/html/" + date + "/nbs.D110000renmrb_"+String.format("%02d",i)+".htm");
        }
    }

    public static void main(String[] args) {
        Spider spider = Spider.create(new MyPageProcessor());
        for(int i=1;i<=31;i++){
            addByDate(spider,"2020-01/"+String.format("%02d",i));
        }
        for(int i=1;i<=25;i++){
            addByDate(spider,"2020-02/"+String.format("%02d",i));
        }
        spider.addPipeline(new MyPipeline())
                .addPipeline(new ConsolePipeline())
                .thread(1000)
                .run();

    }
}