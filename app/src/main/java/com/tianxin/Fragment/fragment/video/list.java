package com.tianxin.Fragment.fragment.video;

import com.tianxin.listener.Paymnets;
import com.tencent.opensource.model.videolist;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class list extends Thread {
    private static final String TAG = "list";
    private String geturl;
    private String page;
    private Paymnets paymnets;
    private final List<videolist> list = new ArrayList<>();

    public void setPaymnets(Paymnets paymnets) {
        this.paymnets = paymnets;
    }

    @Override
    public void run() {
        super.run();
        http(this.geturl, this.page);
    }

    public void seturl(String url, String page) {
        this.geturl = url;
        this.page = page;
    }

    public void http(String url, String page) {
        try {
            Document document = Jsoup.connect(url).get();
            Elements es = document.getElementsByClass("slider-item");
            for (Element element : es) {
                String mp4 = element.getElementsByClass("slider-info-box").attr("abs:data-preview");
                String img = element.getElementsByTag("img ").attr("src");
                String title = element.getElementsByTag("h2 ").text();
                String duration = element.getElementsByTag("var  ").text();

                videolist video = new videolist();
                video.setId("0");
                video.setUserid("0");
                video.setTitle(title);
                video.setPlayurl(mp4);
                video.setPicuser(img);
                video.setPicurl(img);
                video.setBigpicurl(img);
                video.setAvatar(img);
                video.setTime(duration);
                video.setType(0);
                list.add(video);
            }

            if (paymnets != null) {
                paymnets.onClick(list);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
