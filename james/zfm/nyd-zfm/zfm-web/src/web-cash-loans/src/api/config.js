var ipconfi = {
    fx: 'http://192.168.23.79:8086/marketing/', //fei
    test: 'http://172.16.10.176:8081/marketing/',
    huang: '/cls'
}
var cfg = {
    // dev: 'http://172.16.10.103:8083/cls', //测试环境   
    // dev: 'http://47.103.128.134:8095/zfm', //测试环境 
    // dev: 'http://192.168.1.150:8095/zfm',//
    dev: '/zfm',
    pro: '/zfm',
    pro_download: '/zfm',
    pro_baseurlVerifyImg: '/',
};
var base_url = '';
var base_url_download = '';
var baseurlVerifyImg = '';
switch (process.env.NODE_ENV) {
    case "development":
        base_url = cfg.dev;
        base_url_download = cfg.dev_download;
        baseurlVerifyImg = cfg.dev_baseurlVerifyImg
        break;
    case "testing":
        base_url = cfg.dev;
        base_url_download = cfg.dev_download;
        baseurlVerifyImg = cfg.dev_baseurlVerifyImg
        break;
    case "production":
        base_url = cfg.pro;
        base_url_download = cfg.pro_download;
        baseurlVerifyImg = cfg.pro_baseurlVerifyImg
        break;
}

export const baseURL = base_url
export const baseURLDownload = base_url_download
export const baseURLVerifyImg = baseurlVerifyImg