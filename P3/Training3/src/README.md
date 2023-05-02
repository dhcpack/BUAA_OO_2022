#### 通过JUnit单元测试所找到的bug:
1. MySet.java 中 insert 函数left初值设置错误，应为`int left = 0;`
2. MySet.java 中 delete 函数while循环的条件设置错误，应为`while (left <= right)`