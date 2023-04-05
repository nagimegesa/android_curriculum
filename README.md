## android 课设需求

### 重要说明
1. **关于夜间模式**
夜间模式的设置本质为采用不同的风格渲染组件, 也就是说如果需要实现夜间模式, 再写UI的时候就需要尽量不采用硬编码<br>
举个栗子, 再写 button组件的时候需要设置 textColor属性, **不要这样写**
```xml
<Button
    android:textColor = "#000000" />
```
**必须这样写**
```xml
<!--@color/Black 为 /res/color.xml 中定义的颜色 -->
<Button
    android:textColor = "@color/Black" />
```
2. 关于同一风格的问题
   + 关于写UI界面的id, 必须加上统一的前缀, 举个栗子, 阅读模式的 组件 id 全部为 read_ 开头
   + 关于每个人添加的drawable, style, mipmap 文件, 同上, 只要是只有自己用的上的文件(**注意是资源文件**), 加上自己的前缀(**不要用自己的名字, 和一些没有意义的字符**)
   + 关于每个的java文件非必要不要直接创建在com.xxzz.curriculum目录下, 要在自己的目录下创建(**注意Config类**需要创建在该目录下)

3. 关于 Util类的问题
Util类为工具类, 主要写一些比较常用但是又不是很好归类的函数, 如果自己遇到了一些需求先想想是不是整个项目很常用的功能, 必要时添加Utils类中函数, 然后更新README
并且,如果自己需要Utils里面的功能, **不要自己造轮子**,目前Util类中的函数如下
   1. makeToast, 创建一个 Toast 并显示
   2. readAllFile 读取指定路径文件内容
4. 关于提交代码的问题
<div style="font-size:50px">不要催！！不要催！！不要催！！</div>

### 模块一 初始界面
要求:
1. 可以显示图书的封面, 当然也要可以点进去(留好接口), 点进去需要传参为点击的图书名, 参数名为 book_name,  直接采用putExtra方式
2. 对于已经添加的图书需要记录图书的简要信息(图书封面, 和其他信息)
   1. 整体信息保存在 /data/0/com.curriculum/files/cover文件中, 保存的格式为 json
   2. 封面图片保存在 /data/0/com.curriculum/files/cover/img文件中
   3. 保存其他信息的文件在 /data/0/com.curriculum/cover/cover.json
   **注意 /data/0/com.xxx.xxx/就是上面这个目录可以调用 getFilesDir直接获得**
3. 有一个向上面的图一样的菜单, 菜单实现的功能要有
    + 可以对图书进行排序, 比如按打开的时间进行排序, 按阅读的时长进行排序
    + 需要留出一个添加图书的接口
4. 可以搜索已经添加的本地书
#### 选做(尽量的挑战一下)
1. 如果可以，希望做成图书可以拖动的效果
### 模块二 添加图书模块
要求:
1. 必须实现可以直接选择手机文件夹, 查看文件夹中内容，添加单个文件的需求(需求的文件格式见附录)
2. 在选择了文件后, 如果文件符合格式(附录的格式), 将文件复制到指定的目录(/data/0/com.curriculum/files/Book)
3. 对于添加的图书, 需要返回添加的信息, 返回的信息需要有 图书封面路径, 图书名。
#### 选做(尽量的挑战一下)
1. 实现可以同时选择多个文件的效果
2. 实现智能导入的效果, 即自动搜索符合条件的文件
### 模块三 设置界面
要求
1. 最基本的要求是调整字体大小
2. 书签和收藏管理
3. 对于用户的设置需求保存在本地, 采用 xml 的形式, 参考 **getSharedPreferences** 函数
4. 可以设置打开和关闭背景音乐
5. **夜间模式(增加为必须做)**
#### 具体需求如下
1. 设置 Config 类, 记录当前状态下用户的设置, 比如有没有开启音乐, 字体大小时多少
   + Config 类要求单例模式实现
   + 要求提供 get, set 方法 比如 ``setMouseStat(boolean stat)`` 表示设置背景英语的状态
2. 添加 BooKMark, 和 BookCollection类, 分别表示书签和收藏
   + 其中BookMark类要求继承BooCollection类
   + BookCollection 类中要记录图书名以及那一页的信息
   + BookMark类中要求有书签数据的信息。
3. 为BookMark和BookCollection添加一个管理系统要求可以增伤对应的书签和收藏
4. 用户书签和收藏信息保存在本地以Json的格式, 具体要求如下
   1. 书签文件存在 /data/0/com.curriculum/files/User/Mark.json
   2. 收藏文件存在 /data/0/com.curriculum/files/User/Collection.json
   3. Mark.json格式如下
   ```json
   {
      "count": 1,
      "info": [{
          "book_name" : "name",
          "page": 1,
          "text": "text"
      }]
   } 
   ```
   4. Collection.json 同 Mark.json, 但没有 text 字段
#### 选做(尽量的挑战一下)
1. 护眼模式
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
