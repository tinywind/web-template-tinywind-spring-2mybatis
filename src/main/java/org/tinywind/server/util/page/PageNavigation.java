package org.tinywind.server.util.page;

/**
 * @author tinywind
 * @since 2016-09-03
 */
public class PageNavigation {
    private final int first = 0;
    private final int[] items;
    private final int last;
    private final int next;
    private final int previous;
    private final int page;
    private final int rowsPerPage;

    public PageNavigation(int page, int totalCount, int numberOfRowsPerPage, int numberOfPagesPerNavigation) {
        this.page = page;
        this.rowsPerPage = numberOfRowsPerPage;
        final int last = ((int) Math.ceil(totalCount * 1.0 / numberOfRowsPerPage));
        final int begin = page - (page % numberOfPagesPerNavigation);
        final int end = Math.min(begin + numberOfPagesPerNavigation, last);

        final int max = Math.max(end, 1);
        final int size = max - begin;
        items = new int[size > 0 ? size : 0];
        for (int i = begin; i < max; i++)
            items[i - begin] = i;
        previous = Math.max(page - 1, first);
        next = Math.max(Math.min(last - 1, page + 1), first);
        this.last = Math.max(last - 1, first);
    }

    public boolean contains(int i) {
        for (int item : items)
            if (item == i)
                return true;
        return false;
    }

    public int getFirst() {
        return first;
    }

    public int[] getItems() {
        return items;
    }

    public int getLast() {
        return last;
    }

    public int getNext() {
        return next;
    }

    public int getPrevious() {
        return previous;
    }

    public int getPage() {
        return page;
    }

    public int getRowsPerPage() {
        return rowsPerPage;
    }
}
