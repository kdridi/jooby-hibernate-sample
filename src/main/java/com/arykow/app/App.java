package com.arykow.app;

import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

import org.jooby.Jooby;
import org.jooby.Results;
import org.jooby.hbm.Hbm;
import org.jooby.hbm.UnitOfWork;
import org.jooby.pac4j.Auth;
import org.pac4j.oauth.client.FacebookClient;
import org.pac4j.oauth.profile.facebook.FacebookProfile;

import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

import j2html.TagCreator;
import j2html.attributes.Attr;

public class App extends Jooby {
	{
		use(new Hbm().classes(Feed.class));

		get("/", (req) -> {
			return require(UnitOfWork.class).apply(em -> {
				final List<Feed> feeds = em.createQuery("from Feed", Feed.class).getResultList();
				return TagCreator.html()
						.with(TagCreator.head().with(TagCreator.title("My Feeds")),
								TagCreator.body().with(TagCreator.table().with(feeds.stream()
										.map(feed -> TagCreator.tr().with(TagCreator.td(feed.getId().toString()),
												TagCreator.td(feed.getUserId()), TagCreator.td(feed.getUri()),
												TagCreator.td(feed.getTitle())))
										.collect(Collectors.toList()))))
						.render();
			});

		});

		use(new Auth()
				.client(conf -> new FacebookClient(conf.getString("facebook.key"), conf.getString("facebook.secret"))));

		post("/feeds", req -> {
			final FacebookProfile profile = require(FacebookProfile.class);
			final FeedAddForm form = req.params(FeedAddForm.class);
			System.out.println(form.toString());
			System.out.println(profile.toString());

			SyndFeedInput input = new SyndFeedInput();
			SyndFeed feed = input.build(new XmlReader(new URL(form.getLocation())));

			return require(UnitOfWork.class).apply(em -> {
				em.save(new Feed(profile.getId(), feed.getUri(), feed.getTitle()));
				return Results.redirect("/feeds");
			});
		});

		get("/feeds", req -> {
			final FacebookProfile profile = require(FacebookProfile.class);

			return require(UnitOfWork.class).apply(em -> {
				final List<Feed> feeds = em.createQuery("from Feed where userId = :userId", Feed.class)
						.setParameter("userId", profile.getId()).getResultList();
				return TagCreator.html().with(TagCreator.head().with(TagCreator.title("My Feeds")),
						TagCreator.body().with(TagCreator.h1(String.format("Welcome %s!", profile.getDisplayName())),
								TagCreator.form().attr(Attr.ENCTYPE, "application/x-www-form-urlencoded")
										.withAction("/feeds").withMethod("POST").with(
												TagCreator.input().withType("text").withName("location"),
												TagCreator.input().withType("submit").withName("add")),
								TagCreator.table().with(feeds.stream()
										.map(feed -> TagCreator.tr().with(TagCreator.td(feed.getId().toString()),
												TagCreator.td(feed.getUserId()), TagCreator.td(feed.getUri()),
												TagCreator.td(feed.getTitle())))
										.collect(Collectors.toList()))))
						.render();
			});

		});
	}

	public static void main(final String[] args) {
		run(App::new, args);
	}

}