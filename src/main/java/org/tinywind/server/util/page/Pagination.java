package org.tinywind.server.util.page;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author tinywind
 * @since 2016-08-09
 */
public class Pagination<T> {
    private static final Logger logger = LoggerFactory.getLogger(PageQueryableForm.class);

    private List<T> rows;
    private int page;
    private int totalCount;
    private PageNavigation navigation;

    public Pagination() {
    }

    public Pagination(List<T> rows, int page, int totalCount, int numberOfRowsPerPage) {
        set(rows, page, totalCount, numberOfRowsPerPage);
    }

    public Pagination(List<T> rows, int page, int totalCount, int numberOfRowsPerPage, int numberOfRowsPerNavigation) {
        set(rows, page, totalCount, numberOfRowsPerPage, numberOfRowsPerNavigation);
    }

    public static Logger getLogger() {
        return logger;
    }

    public void set(List<T> rows, int page, int totalCount, int numberOfRowsPerPage) {
        set(rows, page, totalCount, numberOfRowsPerPage, 5);
    }

    public void set(List<T> rows, int page, int totalCount, int numberOfRowsPerPage, int numberOfRowsPerNavigation) {
        this.rows = rows;
        this.page = page;
        this.totalCount = totalCount;
        this.navigation = new PageNavigation(page, totalCount, numberOfRowsPerPage, numberOfRowsPerNavigation);
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public PageNavigation getNavigation() {
        return navigation;
    }

    public void setNavigation(PageNavigation navigation) {
        this.navigation = navigation;
    }
}
