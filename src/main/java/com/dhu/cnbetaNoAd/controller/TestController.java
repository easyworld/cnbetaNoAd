package com.dhu.cnbetaNoAd.controller;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.dhu.cnbetaNoAd.util.Constants;

@Controller
public class TestController {

	private static Logger logger = LoggerFactory
			.getLogger(TestController.class);

	/**
	 * 首页的数据处理
	 * 
	 * @param page
	 * @return
	 */
	@RequestMapping("index")
	public ModelAndView index(
			@RequestParam(required = false, defaultValue = "1") String page) {

		ModelAndView mav = new ModelAndView("page");

		// 使用jsoup 获取文字
		try {
			Document doc = Jsoup.connect(Constants.CNBETA_INDEX)
					.data("page", page).timeout(5000).get();
			String articles = doc.body().select("div.list:eq(0)").parents()
					.eq(0).toString();
			String pager = doc.body().select("div.pager").toString();
			String web_content = articles.replace("/wap/", "./wap/") + "<br>"
					+ pager.replace("/wap/", "./wap/");
			mav.addObject("content", Constants.NO_AD_HTML_HEAD + web_content
					+ "<br>");
		} catch (IOException ioe) {
			logger.error("can not open website");
			mav.addObject("content", "<h1>Something was wrong!</h1>");
		}

		return mav;
	}

	/**
	 * 非首页的数据处理
	 * 
	 * @param page
	 * @return
	 */
	@RequestMapping("/wap/index")
	public ModelAndView index2(
			@RequestParam(required = false, defaultValue = "1") String page) {

		ModelAndView mav = new ModelAndView("page");

		// 使用jsoup 获取文字
		try {
			Document doc = Jsoup.connect(Constants.CNBETA_INDEX)
					.data("page", page).timeout(5000).get();
			String articles = doc.body().select("div.list:eq(0)").parents()
					.eq(0).toString();
			String pager = doc.body().select("div.pager").toString();
			String web_content = articles.replace("/wap/", "./") + "<br>"
					+ pager.replace("/wap/", "./");
			mav.addObject("content", Constants.NO_AD_HTML_HEAD + web_content
					+ "<br>");
		} catch (IOException ioe) {
			logger.error("Can not open website:{}", Constants.CNBETA_INDEX);
			mav.addObject("content", "<h1>Can not access "
					+ Constants.CNBETA_INDEX + "</h1>");
		}

		return mav;
	}

	@RequestMapping("/wap/view_{id}")
	public ModelAndView getView(@PathVariable String id) {
		ModelAndView mav = new ModelAndView("page");
		// 使用jsoup 获取文章内容
		String url = Constants.CNBETA_ARTICLE_PREFIX + id + ".htm";
		try {
			// 获取页面
			Document doc = Jsoup.connect(url).timeout(5000).get();
			// 获取标题
			String title = doc.body().select("div.title").toString();
			// 获取时间稿源等
			String time = doc.body().select("div.time").toString();
			// 获取新闻内容
			String content = doc.body().select("div.content").toString();
			// 获取评论url
			String comment = String.format(
					"<a href=\"./hotcomments.htm?id=%s\">查看网友评论</a>", id);
			mav.addObject("content", title + time + content + comment + "<br>"
					+ Constants.BACK_A_HTML + "<br>");
		} catch (IOException ioe) {
			logger.error("Can not open website:{}", url);
			mav.addObject("content", "<h1>Can not access " + url + "</h1>");
		}
		return mav;
	}

	/**
	 * 热门评论
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/wap/hotcomments")
	public ModelAndView hotcomments(
			@RequestParam(required = false, defaultValue = "3") String id) {
		ModelAndView mav = new ModelAndView("page");
		// 使用jsoup 获取文章内容
		String url = Constants.CNBETA_HOTCOMMENTS_PREFIX + ".htm";
		try {
			// 获取页面
			Document doc = Jsoup.connect(url).data("id", id).timeout(5000)
					.get();
			// 获取热门评论内容
			String content = doc.body().select("div.content").toString();
			// 所有评论
			String allcomment = "<a href='./comments.htm?id=" + id
					+ "'>所有评论</a>";
			mav.addObject("content", content + "<br>" + allcomment + "<br>"
					+ Constants.BACK_A_HTML + "<br>");
		} catch (IOException ioe) {
			logger.error("Can not open website:{}", url);
			mav.addObject("content", "<h1>Can not access " + url + "</h1>");
		}
		return mav;
	}

	/**
	 * 所有评论
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/wap/comments")
	public ModelAndView comments(
			@RequestParam(required = false, defaultValue = "3") String id) {
		ModelAndView mav = new ModelAndView("page");
		// 使用jsoup 获取文章内容
		String url = Constants.CNBETA_COMMENTS_PREFIX + ".htm";
		try {
			// 获取页面
			Document doc = Jsoup.connect(url).data("id", id).timeout(5000)
					.get();
			// 获取评论内容
			String content = doc.body().select("div.content").toString();
			mav.addObject("content", content + "<br>" + Constants.BACK_A_HTML
					+ "<br>");
		} catch (IOException ioe) {
			logger.error("Can not open website:{}", url);
			mav.addObject("content", "<h1>Can not access " + url + "</h1>");
		}
		return mav;
	}
}
