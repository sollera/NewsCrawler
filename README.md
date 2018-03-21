# NewsCrawler

This project aims to study.

This is Crawler for collecting news.

Its targets are '조선일보','동아일보','서울신문','YTN','세계일보','NewDaily' Newspapers.

When the crawler starts operating, it is performed once per minute.

I used the 'jsoup' library to create this crawler for html parsing.


<!-- How to use -->
Default PATH is 'localhost:8082/ctest/'.

This page will provide you with crawled news information.

And crawler behavior can be controlled from '~/status';

You can turn on/off the crawler on this page.

This crawler stores information in the specified DB.
(So you need to use this, you must change DB settings.)
