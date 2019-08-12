package com.nyd.user.ws.controller;


import com.nyd.user.model.dto.InnerTestDto;
import com.nyd.user.service.InnerTestUserService;
import com.tasfe.framework.support.model.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by hwei on 2018/11/26.
 */
@RestController
@RequestMapping("/user/internal")
public class InnerTestUserController {
    @Autowired
    InnerTestUserService innerTestUserService;

//    /**
//     * 在测试表中删除手机号
//     * @param innerTestDto
//     * @return
//     * @throws Throwable
//     */
//    @RequestMapping(value = "/remove/innertest", method = RequestMethod.POST, produces = "application/json")
//    public ResponseD{"data":{"curPage":1,"datas":[{"apkLink":"","author":"kaina404","chapterId":249,"chapterName":"干货资源","collect":false,"courseId":13,"desc":"","envelopePic":"","fresh":true,"id":8327,"link":"https://github.com/kaina404/FlutterDouBan","niceDate":"6小时前","origin":"","prefix":"","projectLink":"","publishTime":1556499743000,"superChapterId":249,"superChapterName":"干货资源","tags":[],"title":"Flutter豆瓣客户端(仿)，诚心开源","type":0,"userId":-1,"visible":1,"zan":0},{"apkLink":"","author":"玉刚说","chapterId":410,"chapterName":"玉刚说","collect":false,"courseId":13,"desc":"","envelopePic":"","fresh":false,"id":8312,"link":"https://mp.weixin.qq.com/s/78DB2RjOik1yeYE71QCmYA","niceDate":"1天前","origin":"","prefix":"","projectLink":"","publishTime":1556380800000,"superChapterId":408,"superChapterName":"公众号","tags":[{"name":"公众号","url":"/wxarticle/list/410/1"}],"title":"Android 实现动态背景&ldquo;五彩蛛网&rdquo;特效，让你大开眼界","type":0,"userId":-1,"visible":1,"zan":0},{"apkLink":"","author":"郭霖","chapterId":409,"chapterName":"郭霖","collect":false,"courseId":13,"desc":"","envelopePic":"","fresh":false,"id":8321,"link":"https://mp.weixin.qq.com/s/4VgI0CMKnDmcbkhiiRmTgg","niceDate":"1天前","origin":"","prefix":"","projectLink":"","publishTime":1556380800000,"superChapterId":408,"superChapterName":"公众号","tags":[{"name":"公众号","url":"/wxarticle/list/409/1"}],"title":"让你的方法数不要超过64K，MainDex优化记","type":0,"userId":-1,"visible":1,"zan":0},{"apkLink":"","author":"htkeepmoving","chapterId":78,"chapterName":"性能优化","collect":false,"courseId":13,"desc":"","envelopePic":"","fresh":false,"id":8310,"link":"https://www.jianshu.com/p/7d8bb522d425","niceDate":"1天前","origin":"","prefix":"","projectLink":"","publishTime":1556361814000,"superChapterId":79,"superChapterName":"热门专题","tags":[],"title":"Android性能优化-方法区导致内存问题实例分析","type":0,"userId":-1,"visible":1,"zan":0},{"apkLink":"","author":" toothpickTina","chapterId":169,"chapterName":"gradle","collect":false,"courseId":13,"desc":"","envelopePic":"","fresh":false,"id":8309,"link":"https://juejin.im/post/5cbffc7af265da03a97aed41","niceDate":"1天前","origin":"","prefix":"","projectLink":"","publishTime":1556352123000,"superChapterId":60,"superChapterName":"开发环境","tags":[],"title":"深入理解Transform","type":0,"userId":-1,"visible":1,"zan":0},{"apkLink":"","author":"小编","chapterId":274,"chapterName":"个人博客","collect":false,"courseId":13,"desc":"","envelopePic":"","fresh":false,"id":8308,"link":"https://yutiantina.github.io/archives/","niceDate":"1天前","origin":"","prefix":"","projectLink":"","publishTime":1556352099000,"superChapterId":272,"superChapterName":"导航主Tab","tags":[{"name":"导航","url":"/navi#274"}],"title":"天晴日无风","type":0,"userId":-1,"visible":1,"zan":0},{"apkLink":"","author":"code小生","chapterId":414,"chapterName":"code小生","collect":false,"courseId":13,"desc":"","envelopePic":"","fresh":false,"id":8315,"link":"https://mp.weixin.qq.com/s/JmDwxjUKMG-U05VJPoo-_Q","niceDate":"2019-04-26","origin":"","prefix":"","projectLink":"","publishTime":1556208000000,"superChapterId":408,"superChapterName":"公众号","tags":[{"name":"公众号","url":"/wxarticle/list/414/1"}],"title":"Android 日常开发问题总结","type":0,"userId":-1,"visible":1,"zan":0},{"apkLink":"","author":"郭霖","chapterId":409,"chapterName":"郭霖","collect":false,"courseId":13,"desc":"","envelopePic":"","fresh":false,"id":8320,"link":"https://mp.weixin.qq.com/s/xb6eQx9iaUAWur6K9Xv9yg","niceDate":"2019-04-26","origin":"","prefix":"","projectLink":"","publishTime":1556208000000,"superChapterId":408,"superChapterName":"公众号","tags":[{"name":"公众号","url":"/wxarticle/list/409/1"}],"title":"Handler全家桶之 &mdash;&mdash; Handler 源码解析","type":0,"userId":-1,"visible":1,"zan":0},{"apkLink":"","author":"code小生","chapterId":414,"chapterName":"code小生","collect":false,"courseId":13,"desc":"","envelopePic":"","fresh":false,"id":8314,"link":"https://mp.weixin.qq.com/s/nRiBSDsA0cJdxSill0-1OQ","niceDate":"2019-04-25","origin":"","prefix":"","projectLink":"","publishTime":1556121600000,"superChapterId":408,"superChapterName":"公众号","tags":[{"name":"公众号","url":"/wxarticle/list/414/1"}],"title":"Android AutoBundle 像 Retrofit 一样构建 Bundle","type":0,"userId":-1,"visible":1,"zan":0},{"apkLink":"","author":"鸿洋","chapterId":408,"chapterName":"鸿洋","collect":false,"courseId":13,"desc":"","envelopePic":"","fresh":false,"id":8317,"link":"https://mp.weixin.qq.com/s/MSL7h43mSA1UyiqcJpfi1Q","niceDate":"2019-04-25","origin":"","prefix":"","projectLink":"","publishTime":1556121600000,"superChapterId":408,"superChapterName":"公众号","tags":[{"name":"公众号","url":"/wxarticle/list/408/1"}],"title":"放荡不羁SVG讲解与实战&mdash;&mdash;Android高级UI","type":0,"userId":-1,"visible":1,"zan":0},{"apkLink":"","author":"承香墨影","chapterId":411,"chapterName":"承香墨影","collect":false,"courseId":13,"desc":"","envelopePic":"","fresh":false,"id":8322,"link":"https://mp.weixin.qq.com/s/hf4kI1XDkD8_x4p9Q0l-_A","niceDate":"2019-04-25","origin":"","prefix":"","projectLink":"","publishTime":1556121600000,"superChapterId":408,"superChapterName":"公众号","tags":[{"name":"公众号","url":"/wxarticle/list/411/1"}],"title":"滴滴 App 质量优化黑科技，为了稳定都做了什么？","type":0,"userId":-1,"visible":1,"zan":0},{"apkLink":"","author":"MrTrying","chapterId":99,"chapterName":"具体案例","collect":false,"courseId":13,"desc":"","envelopePic":"","fresh":false,"id":8306,"link":"https://www.jianshu.com/p/348fa44a4ca0","niceDate":"2019-04-24","origin":"","prefix":"","projectLink":"","publishTime":1556120335000,"superChapterId":94,"superChapterName":"自定义控件","tags":[],"title":"Android - 仿小红书自定义展开/收起的TextView","type":0,"userId":-1,"visible":1,"zan":0},{"apkLink":"","author":"程序亦非猿","chapterId":450,"chapterName":"Lottie","collect":false,"courseId":13,"desc":"","envelopePic":"","fresh":false,"id":8305,"link":"https://www.jianshu.com/p/3950355f554e","niceDate":"2019-04-24","origin":"","prefix":"","projectLink":"","publishTime":1556120314000,"superChapterId":91,"superChapterName":"动画效果","tags":[],"title":"【源码分析】Lottie 实现炫酷动画背后的原理","type":0,"userId":-1,"visible":1,"zan":0},{"apkLink":"","author":"郭霖","chapterId":409,"chapterName":"郭霖","collect":false,"courseId":13,"desc":"","envelopePic":"","fresh":false,"id":8319,"link":"https://mp.weixin.qq.com/s/P3J467KKS36hJZwIcb1onQ","niceDate":"2019-04-24","origin":"","prefix":"","projectLink":"","publishTime":1556035200000,"superChapterId":408,"superChapterName":"公众号","tags":[{"name":"公众号","url":"/wxarticle/list/409/1"}],"title":"在Android Studio中编写一个自己的模板","type":0,"userId":-1,"visible":1,"zan":0},{"apkLink":"","author":"程序亦非猿","chapterId":428,"chapterName":"程序亦非猿","collect":false,"courseId":13,"desc":"","envelopePic":"","fresh":false,"id":8324,"link":"https://mp.weixin.qq.com/s/g44ql1hOMdsfgKMXGjUo6w","niceDate":"2019-04-24","origin":"","prefix":"","projectLink":"","publishTime":1556035200000,"superChapterId":408,"superChapterName":"公众号","tags":[{"name":"公众号","url":"/wxarticle/list/428/1"}],"title":"移动开发的跨平台技术演进","type":0,"userId":-1,"visible":1,"zan":0},{"apkLink":"","author":"张旭童","chapterId":77,"chapterName":"响应式编程","collect":false,"courseId":13,"desc":"","envelopePic":"","fresh":false,"id":8304,"link":"https://www.jianshu.com/p/23c38a4ed360","niceDate":"2019-04-23","origin":"","prefix":"","projectLink":"","publishTime":1556027559000,"superChapterId":79,"superChapterName":"热门专题","tags":[],"title":"RxJava2 源码解析（一）","type":0,"userId":-1,"visible":1,"zan":0},{"apkLink":"","author":"小巨人9527","chapterId":77,"chapterName":"响应式编程","collect":false,"courseId":13,"desc":"","envelopePic":"","fresh":false,"id":8303,"link":"https://www.jianshu.com/p/4b25ac968afe","niceDate":"2019-04-23","origin":"","prefix":"","projectLink":"","publishTime":1556027399000,"superChapterId":79,"superChapterName":"热门专题","tags":[],"title":"AutoDispose解决RxJava内存泄漏（Android）","type":0,"userId":-1,"visible":1,"zan":0},{"apkLink":"","author":"Reone_JS","chapterId":78,"chapterName":"性能优化","collect":false,"courseId":13,"desc":"","envelopePic":"","fresh":false,"id":8302,"link":"https://www.jianshu.com/p/49239eac7a76","niceDate":"2019-04-23","origin":"","prefix":"","projectLink":"","publishTime":1555981845000,"superChapterId":79,"superChapterName":"热门专题","tags":[],"title":"内存泄漏检测之LeakCanary源码导读与解析","type":0,"userId":-1,"visible":1,"zan":0},{"apkLink":"","author":"code小生","chapterId":414,"chapterName":"code小生","collect":false,"courseId":13,"desc":"","envelopePic":"","fresh":false,"id":8313,"link":"https://mp.weixin.qq.com/s/a5PfzO5qh_1P1TcCwdWonQ","niceDate":"2019-04-23","origin":"","prefix":"","projectLink":"","publishTime":1555948800000,"superChapterId":408,"superChapterName":"公众号","tags":[{"name":"公众号","url":"/wxarticle/list/414/1"}],"title":"开发杂谈：Android 移动应用架构发展简史","type":0,"userId":-1,"visible":1,"zan":0},{"apkLink":"","author":"鸿洋","chapterId":408,"chapterName":"鸿洋","collect":false,"courseId":13,"desc":"","envelopePic":"","fresh":false,"id":8316,"link":"https://mp.weixin.qq.com/s/idjFaJsLpVLw52RSYHA_Vg","niceDate":"2019-04-23","origin":"","prefix":"","projectLink":"","publishTime":1555948800000,"superChapterId":408,"superChapterName":"公众号","tags":[{"name":"公众号","url":"/wxarticle/list/408/1"}],"title":"天天用LeakCanary，不了解原理能忍？","type":0,"userId":-1,"visible":1,"zan":0}],"offset":0,"over":false,"pageCount":321,"size":20,"total":6401},"errorCode":0,"errorMsg":""}ata removeInnerTestUser(@RequestBody InnerTestDto innerTestDto) {
//        return innerTestUserService.removeInnerTestUser(innerTestDto);
//    }

    /**
     * 解绑接口
     * @param innerTestDto
     * @return
     */
    @RequestMapping(value = "/unbind/innertest", method = RequestMethod.POST, produces = "application/json")
    public ResponseData unBindTestUser(@RequestBody InnerTestDto innerTestDto) {
        return innerTestUserService.unBindTestUser(innerTestDto);
    }

    /**
     * 解绑接口
     * @param innerTestDto
     * @return
     */
    @RequestMapping(value = "/unbind", method = RequestMethod.POST, produces = "application/json")
    public ResponseData unBindTestUserNew(@RequestBody InnerTestDto innerTestDto) {
        return innerTestUserService.unBindTestUser(innerTestDto);
    }




}
