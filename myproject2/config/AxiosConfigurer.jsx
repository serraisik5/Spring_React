import axios from "axios";

const AxiosConfigurer = (function () {
    const _configure = () => {
        axios.defaults.withCredentials = true
        axios.interceptors.request.use((config) => {
            
            return config;
        },
        (error) => {
            return Promise.reject(error);
        });
    };
    return {
        configure: _configure 
    };
})();

export default AxiosConfigurer;