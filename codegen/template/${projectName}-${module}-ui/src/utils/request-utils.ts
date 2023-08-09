import axios, { AxiosInstance, AxiosRequestConfig, AxiosResponse } from 'axios'
import { notification } from 'ant-design-vue';
import * as qs from "qs";
import * as cookieUtils from './cookie-utils'

const axiosRequest: AxiosInstance = (() => {
    const axiosRequest = axios.create({
        headers: {
            'Content-Type': 'application/json',
        },
        timeout: 30000
    });

    axiosRequest.interceptors.request.use((config: AxiosRequestConfig) => {
        config.paramsSerializer = params => qs.stringify(params, { skipNulls: true, arrayFormat: 'comma' })
        config.headers['Authorization'] = cookieUtils.getCookie('Authorization');
        return config;
    });

    axiosRequest.interceptors.response.use(response => response, error => {
        if (error && error.response && error.response.data && error.response.data.message) {
            notification.error({ message: error.response.data.message });
        } else {
            notification.error({ message: error.message });
        }
        return Promise.reject(error);
    });

    return axiosRequest;
})()

export function request<T = any, R = any>(config: AxiosRequestConfig, direct = false): Promise<R> {
    if (config.url && !direct) {
        config = { ...config, url: process.env.VUE_APP_NODE_API_ROOT + config.url }
    }
    return new Promise((resolve, reject) => {
        axiosRequest.request(config).then((res: AxiosResponse) => {
            resolve(res.data)
        }).catch(error => {
            reject(error);
        });
    })
}

export function getQueryParam(name: string, queryString = '') {
    const reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    const r = (queryString ? queryString : window.location.search).substr(1).match(reg);
    if (r != null) return decodeURI(r[2]);
    return '';
}