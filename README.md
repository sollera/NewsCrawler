# NewsCrawler

This project aims to study.

This is Crawler for collecting news.

Its targets are '조선일보','동아일보','서울신문','YTN','세계일보','NewDaily' Newspapers.

When the crawler starts operating, it is performed once per minute.

I used the 'jsoup' library to create this crawler for html parsing.


!How to use

Default PATH is 'localhost:8082/ctest/'.

This page will provide you with crawled news information.

And crawler behavior can be controlled from '~/status';

You can turn on/off the crawler on this page.

This crawler stores information in the specified DB.
(So you need to use this, you must change DB settings.)


!Licenses of open source used

MySQL               GPL for study (http://www.mysqlkorea.com/sub.html?mcode=product&scode=08)<br />
Spring Framework    Apache License 2.0  (https://docs.oracle.com/cd/E17952_01/mysql-monitor-2.3-en/license-spring-framework.html)<br />
jQuery              MIT (https://jquery.org/license/)<br />
MyBatis             Apache License 2.0  (http://www.mybatis.org/spring/license.html)<br />
jsoup               MIT (https://jsoup.org/license)<br />
Bootstrap           MIT (https://getbootstrap.com/docs/4.0/about/license/)<br />
Chart.js            MIT (https://www.chartjs.org/docs/latest/notes/license.html)<br />