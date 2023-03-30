## android 课设需求
### 模块一 初始界面
要求:
1. 可以显示图书的封面, 当然也要可以点进去(留好接口), 点进去需要传参为点击的图书名
2. 对于已经添加的图书需要记录图书的简要信息(图书封面, 和其他信息)
   1. 整体信息保存在 /storage/0/xxzz_app/cover文件中, 保存的格式为 json
   2. 封面图片保存在 /storage/0/xxzz_app/cover/img文件中
   3. 保存其他信息的文件在 /storage/0/xxzz_app/cover/cover.json
3. 有一个向上面的图一样的菜单, 菜单实现的功能要有
    + 可以对图书进行排序, 比如按打开的时间进行排序, 按阅读的时长进行排序
    + 需要留出一个添加图书的接口
4. 可以搜索已经添加的本地书
#### 选做(尽量的挑战一下)
1. 如果可以，希望做成图书可以拖动的效果
### 模块二 添加图书模块
要求:
1. 必须实现可以直接选择手机文件夹, 查看文件夹中内容，添加单个文件的需求(需求的文件格式见附录)
2. 在选择了文件后, 如果文件符合格式(附录的格式), 将文件复制到指定的目录(storage/0/xxzz_book/Book)
3. 对于添加的图书, 需要返回添加的信息, 返回的信息需要有 图书封面路径, 图书名。
#### 选做(尽量的挑战一下)
1. 实现可以同时选择多个文件的效果
2. 实现智能导入的效果, 即自动搜索符合条件的文件
### 模块三 设置界面
要求
1. 最基本的要求是调整字体大小
2. 书签和收藏管理
3. 对于以上的需求保存在本地, 以json的格式
4. 可以设置打开和关闭背景音乐
#### 选做(尽量的挑战一下)
1. 护眼模式和夜间开关  
### 模块四 阅读模块
要求
1. 可以调用模块三书签和收藏管理的接口
2. 可以调用调整字体的接口
3. 可以设置系统亮度
4. 可以显示当前页数、电量、目录、书签和收藏的情况
[选做]
1. 可以有多种翻页效果

附录
### 计划图书的文件格式
1. 图书定义<br>
一个zip压缩包, 但是修改后缀, 比如 jbk.
2. 压缩包内容定义
    1. main 目录<br>
    存放图片资源
    2. text 目录
    存放文本资源
    3. jbk_config.json 文件 定义图书的信息, 格式如下
    ```json
    {
        "book_name" : "book_name",
        "cover" : "/cover.png",
        "path" : "/book_path/book_name",
        "pages_count" : 2,
        "pages" : [{
            "pages" : 1,
            "image" : "/page1.png",
            "text" : "/text1.txt"
        }, {
            "pages" : 2,
            "image" : "/page2.png",
            "text" : "/text2.txt"
        }]
    }
    ```

   以上各字段解释如下：
   + book_name 图书名
   + cover 以main为相对路径的图书封面
   + path 图书以某个专用目录(未定)为相对路径的路径
   + pages_count 图书页数
   + page 每一页的信息, 其中pages, image和text 分别表示那一页, 这一页以 main为相对路径的图片和以text为路径的文本
### 模块一保存图书简要格式的json
```json
{
   "count": 2,
   "cover": [
      {
         "book_name": "book_name",
         "cover_path": "path"
      }, {
         "book_name": "book_name",
         "cover_path": "path"
      }
   ],
   "last_read_page" : 0,
   "last_read_time" : "1221212",
   "total_read_time": "123123123"
}
```
以上各字段含义如下
+ count : 有两个图书
+ cover : 图书数组
  + book_name : 图书名
  + cover_path : 封面路径
+ last_read_page 上一次阅读的页数
+ last_read_time 上一次阅读的时间戳, 以字符串的形式表示
+ total_read_time 总的阅读时间, 以字符串的形式表示
