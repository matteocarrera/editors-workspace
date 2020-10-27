package com.urfusoftware.services;

import com.urfusoftware.domain.News;
import com.urfusoftware.domain.User;
import com.urfusoftware.repositories.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NewsService {

    @Autowired
    private NewsRepository newsRepository;

    public void save(News news) {
        newsRepository.save(news);
    }

    public List<News> getNewsList(User user) {
        String role = user.getRole().getName();
        List<News> newsList = new ArrayList<>();

        for (News news : newsRepository.findAllByOrderByIdDesc()) {
            String reportDate = news.getNewsDate().toString().substring(0, 10);
            String dateInRusFormat = reportDate.substring(8, 10) +
                    "." + reportDate.substring(5, 7) +
                    "." + reportDate.substring(0, 4);
            news.setStringDate(dateInRusFormat);
            if (role.equals("Администратор") || role.equals("Менеджер"))
                newsList.add(news);
            else if (news.getText().contains(user.getUsername()))
                newsList.add(news);
        }

        return newsList;
    }
}
