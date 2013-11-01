# Cytosol for Android

[**cytosol**][bio-def-wiki] _n._ The liquid found inside cells, a.k.a.
_intracellular fluid_ (_ICF_) or _cytoplasmic matrix_. / 细胞质基质

[bio-def-wiki]: https://en.wikipedia.org/wiki/Cytosol

![Cytosol logo](/logo.png)

Cytosol helps you better compose your Android app's "cells!"
/ 帮你更好地组织你安卓应用的 "细胞"!


**Disclaimer** Due to use of a forked SlidingMenu library, integrating this
library can be really tiring. The codebase is also changing frequently.
Therefore, it is not recommended to integrate this library for now; however,
experimental uses and patches are always welcome. If you tried this library,
please open an issue and tell us your feeling!

**声明**: 因为使用了 fork 的 SlidingMenu 库, 在项目里集成本程序库可能会很麻烦,
而且代码也在不断地变化. 所以现在暂时不推荐在你的项目里应用本库;
不过我们总是欢迎试用, 也非常乐意接受补丁. 如果你试用了这个库, 欢迎开个 issue
告诉我们你的试用体会!


## Introduction / 简介

Cytosol is an Android library comprised of boilerplate code which the author
finds himself repeatedly writing. The author released this library first
because he was (again) going to write a new app, and instantly realized he
would be duplicating much boilerplate if such a library did not exist. He went
on to refactoring code, and this library was made. The code is originated from
the [JNRain for Android][jnrain-android] project, with minimal modification to
make it suitable for other apps.

Cytosol 是一个 Android 辅助库, 内含一些作者发现自己在不断重复实现的代码.
某一天, 作者 (又) 准备开始写一个新应用, 然后就发现如果没有这样一个库可用的话,
自己就会重复很多辅助代码了. 作者马上着手重构自己之前应用的代码,
于是就诞生了这个项目. 这个库的代码是源自[江南听雨安卓客户端][jnrain-android]的,
包含一些小改动, 让它适用于其他应用的环境.

[jnrain-android]: https://github.com/jnrainerds/jnrain-android


## License / 许可证

This project is licensed under the [Apache Version 2.0][apache-2.0] license.

本项目采用 [Apache Version 2.0][apache-2.0] 许可证.

[apache-2.0]: http://www.apache.org/licenses/LICENSE-2.0.html


## Features / 功能特性

*   Base classes for working with RoboGuice, RoboSpice and SlidingMenu
	/ 集成 RoboGuice 和 RoboSpice 的基类

	Inherit our base RoboSpice supporting service, then make your activities
	and fragments inherit from their `SpicedRobo`-relatives. You can now issue
	robust asynchronous HTTP requests with a single `makeSpiceRequest` call!
    Usage is nearly the same as using RoboSpice directly, of course.

	You can also find base classes that provide integration with the excellent
	[SlidingMenu][slidingmenu] library. SlidingMenu's upstream seems pretty
    inactive for now, so the SlidingMenu used in this library is forked by
    the developer to provide several bugfixes and improvements.

	继承我们的 RoboSpice 支持服务, 然后让你的 `Activity` 和 `Fragment`
	继承它们加了 `SpicedRobo` 前缀的基类. 现在就可以用一个 `makeSpiceRequest`
	调用来发送可靠的异步 HTTP 请求了! 当然了, 用法和直接用 RoboSpice
	几乎是一样的.

    你还能找到和非常靠谱的 [SlidingMenu][slidingmenu] 库集成的基类. SlidingMenu
    的上游项目现在相当不活跃, 所以我们使用的是本项目开发者的一个 fork,
    含有一些 bug 修复补丁和改进.

*	Common boilerplate for Android apps / 安卓应用的常见辅助代码

	- Toast directly with resource ID and message format / 直接使用资源 ID 发送 toast, 支持格式化
	- Set HTML text with resource ID and message format / 设置 HTML 文本, 同样支持资源 ID 和格式化

*   Update mechanism / 应用升级机制

	If your app is to be published on the Play Store, you may not want to use
	this.

	如果你的应用是要发布到 Play 商店的, 你可能不想用这部分功能.

	- Support for multiple update channels / 支持多个更新频道
	- Easy-to-write JSON for version info / 容易撰写的 JSON 格式版本信息
	- Customizable interval for update check / 可自定义的检查更新时间间隔
	- Download using user's web browser / 使用用户指定的浏览器下载

[slidingmenu]: https://github.com/jfeinstein10/SlidingMenu


## Known issues and limitations / 已知的问题和限制

*   `URLImageGetter` when rendering simple HTML in `TextView`'s can't display
	the loading placeholder. / `URLImageGetter` 用来在 `TextView` 里渲染简单
	HTML 的时候不能成功显示出下载占位符.


## Roadmap / 项目路线图

* Improve docs! / 完善文档!
* Write tests! / 写测试!
* Work on making the `URLImageGetter` more robust / 让 `URLImageGetter` 更加健壮


## Acknowledgements / 致谢

* GitHub
* StackOverflow
* [@yunxiyinzhe](https://github.com/yunxiyinzhe) for several ideas


<!-- vim:set ai et ts=4 sw=4 sts=4 fenc=utf-8 syn=markdown: -->
