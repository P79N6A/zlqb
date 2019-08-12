import axios from 'axios';
import { baseURL } from './config.js';
import Vue from 'vue'
import router from '../router/index'
var Axios = axios.create({
    baseURL: baseURL,
})
Axios.interceptors.request.use(
    (config) => {
        return config;
    }, (error) => {
        return Promise.reject(error);
    }
);
Axios.interceptors.response.use(
    (response) => {

        return response;
    }, (error) => {

        if (error.response) {
            switch (error.response.status) {
                case 401:
                    window.location.href = baseURL + "/login"
                        // router.push({ path: '/home' });
                        // location.reload();
                        // break;
                        /*      default :
                             vm.$message({
                               duration: 5000,
                               type: 'warning',
                               message: error.response.status + " " + error.response.statusText
                           }) */
                        // case 404:
                        //     break;
                        // case 500:
                        //     break;

                case 404:
                    // router.push({ path: '/404' });
                    break;
                case 500:
                    // router.push({ path: '/500' });
                    break;
                default:
            }

        }
        return Promise.reject(error);
    }
);
export default Axios